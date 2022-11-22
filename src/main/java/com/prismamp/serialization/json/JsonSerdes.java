package com.prismamp.serialization.json;

import com.prismamp.TaxGroupingKey;
import com.prismamp.model.*;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class JsonSerdes {

  public static Serde<Transaction> Transaction() {
    JsonSerializer<Transaction> serializer = new JsonSerializer<>();
    JsonDeserializer<Transaction> deserializer = new JsonDeserializer<>(Transaction.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }

  public static Serde<Tax> Tax() {
    JsonSerializer<Tax> serializer = new JsonSerializer<>();
    JsonDeserializer<Tax> deserializer = new JsonDeserializer<>(Tax.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }

  public static Serde<TaxGroupingKey> TaxGroupingKey() {
    JsonSerializer<TaxGroupingKey> serializer = new JsonSerializer<>();
    JsonDeserializer<TaxGroupingKey> deserializer = new JsonDeserializer<>(TaxGroupingKey.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }

  public static Serde<Merchant> Merchant() {
    JsonSerializer<Merchant> serializer = new JsonSerializer<>();
    JsonDeserializer<Merchant> deserializer = new JsonDeserializer<>(Merchant.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }

  public static Serde<Enriched> Enriched() {
    JsonSerializer<Enriched> serializer = new JsonSerializer<>();
    JsonDeserializer<Enriched> deserializer = new JsonDeserializer<>(Enriched.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }

  public static Serde<EnrichedWithTaxAmountSum> EnrichedWithTaxAmountSum() {
    JsonSerializer<EnrichedWithTaxAmountSum> serializer = new JsonSerializer<>();
    JsonDeserializer<EnrichedWithTaxAmountSum> deserializer =
        new JsonDeserializer<>(EnrichedWithTaxAmountSum.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }

  public static Serde<Certificate> Certificate() {
    JsonSerializer<Certificate> serializer = new JsonSerializer<>();
    JsonDeserializer<Certificate> deserializer = new JsonDeserializer<>(Certificate.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }
}
