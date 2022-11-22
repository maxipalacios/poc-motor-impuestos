package com.prismamp;

import com.prismamp.model.Certificate;
import com.prismamp.model.Enriched;
import com.prismamp.model.Merchant;
import com.prismamp.model.Tax;
import com.prismamp.serialization.json.JsonSerdes;
import java.time.Duration;
import java.util.HashMap;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Topology {
  private static final Logger log = LoggerFactory.getLogger(Topology.class);

  public static org.apache.kafka.streams.Topology build() {

    // the builder is used to construct the topology
    StreamsBuilder builder = new StreamsBuilder();

    // stream de impuestos calculados - key: cuit
    KStream<String, Tax> taxesEvents =
        builder.<String, Tax>stream(
            "taxes-calculations",
            Consumed.with(Serdes.String(), JsonSerdes.Tax())
                .withTimestampExtractor(new TaxTimestampExtractor()));

    // tabla de comercios - key: cuit
    KTable<String, Merchant> merchants =
        builder.table("merchants", Consumed.with(Serdes.String(), JsonSerdes.Merchant()));

    // stream de impuestos filtrados solo retenciones
    KStream<String, Tax> filtered =
        taxesEvents.filter(
            (key, tax) -> {
              return tax.getTaxId().startsWith("RET");
            });

    ValueJoiner<Tax, Merchant, Enriched> taxMerchantJoiner =
        (tax, merchant) -> new Enriched(tax, merchant);

    // join de impuestos y comercios
    KStream<String, Enriched> withMerchant = filtered.join(merchants, taxMerchantJoiner);

    // ventana de tiempo de impuestos - para pruebas durante desa se usa una ventana de 60 seg
    TimeWindows tumblingWindow =
        TimeWindows.of(Duration.ofSeconds(60)).grace(Duration.ofSeconds(5));

    final Aggregator<TaxGroupingKey, Tax, Certificate> withholdingAggregator =
        new WithholdingAggregator();

    // aggregate: sumatoria impuestos retenidos - key: cuit/impuesto
    KTable<Windowed<TaxGroupingKey>, Certificate> taxesWithholdingSum =
        filtered
            .selectKey((key, value) -> new TaxGroupingKey(value.getCuit(), value.getTaxId()))
            .groupByKey(Grouped.with(JsonSerdes.TaxGroupingKey(), JsonSerdes.Tax()))
            .windowedBy(tumblingWindow)
            .aggregate(
                () -> new Certificate(new HashMap<>()),
                withholdingAggregator,
                Materialized.<TaxGroupingKey, Certificate, WindowStore<Bytes, byte[]>>as(
                        "taxes-withholdings")
                    .withValueSerde(JsonSerdes.Certificate()));

    KStream<TaxGroupingKey, Certificate> certificates =
        taxesWithholdingSum
            .toStream()
            .peek((k, v) -> log.info("Observed event: {}", v))
            .map(
                (windowedKey, value) -> {
                  return KeyValue.pair(windowedKey.key(), value);
                })
            .peek((k, v) -> log.info("Transformed event: {}", v));
    certificates.to(
        "certificates", Produced.with(JsonSerdes.TaxGroupingKey(), JsonSerdes.Certificate()));

    return builder.build();
  }
}
