package com.prismamp;

import com.prismamp.model.Certificate;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.kstream.Window;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RestService {
  private final HostInfo hostInfo;
  private final KafkaStreams streams;

  private static final Logger log = LoggerFactory.getLogger(RestService.class);

  RestService(HostInfo hostInfo, KafkaStreams streams) {
    this.hostInfo = hostInfo;
    this.streams = streams;
  }

  ReadOnlyWindowStore<TaxGroupingKey, Certificate> getCertificateStore() {
    return streams.store(
        StoreQueryParameters.fromNameAndType(
            "taxes-withholdings", QueryableStoreTypes.windowStore()));
  }

  void start() {
    Javalin app = Javalin.create().start(hostInfo.port());

    /** Local window store query: all entries */
    app.get("/certificates/all", this::getAll);

    app.get("/certificates/range/:from/:to", this::getAllInRange);

    app.get("/certificates/range/:cuit/:tax/:from/:to", this::getRange);
  }

  void getAll(Context ctx) {
    Map<String, Certificate> certificates = new HashMap<>();

    KeyValueIterator<Windowed<TaxGroupingKey>, Certificate> range = getCertificateStore().all();
    while (range.hasNext()) {
      KeyValue<Windowed<TaxGroupingKey>, Certificate> next = range.next();
      Windowed<TaxGroupingKey> key = next.key;
      Certificate value = next.value;
      certificates.put(key.toString(), value);
    }
    // close the iterator to avoid memory leaks
    range.close();
    // return a JSON response
    ctx.json(certificates);
  }

  void getAllInRange(Context ctx) {
    List<Map<String, Object>> certificates = new ArrayList<>();

    String from = ctx.pathParam("from");
    String to = ctx.pathParam("to");

    Instant fromTime = Instant.ofEpochMilli(Long.valueOf(from));
    Instant toTime = Instant.ofEpochMilli(Long.valueOf(to));

    KeyValueIterator<Windowed<TaxGroupingKey>, Certificate> range =
        getCertificateStore().fetchAll(fromTime, toTime);
    while (range.hasNext()) {
      Map<String, Object> certificate = new HashMap<>();
      KeyValue<Windowed<TaxGroupingKey>, Certificate> next = range.next();

      TaxGroupingKey key = next.key.key();
      Window window = next.key.window();
      Long start = window.start();
      Long end = window.end();
      Certificate value = next.value;
      certificate.put("key", key);
      certificate.put("start", Instant.ofEpochMilli(start).toString());
      certificate.put("end", Instant.ofEpochMilli(end).toString());
      certificate.put("count", value);
      certificates.add(certificate);
    }
    // close the iterator to avoid memory leaks
    range.close();
    // return a JSON response
    ctx.json(certificates);
  }

  void getRange(Context ctx) {
    List<Map<String, Object>> certificates = new ArrayList<>();

    String cuit = ctx.pathParam("cuit");
    String tax = ctx.pathParam("tax");
    String from = ctx.pathParam("from");
    String to = ctx.pathParam("to");

    Instant fromTime = Instant.ofEpochMilli(Long.valueOf(from));
    Instant toTime = Instant.ofEpochMilli(Long.valueOf(to));

    WindowStoreIterator<Certificate> range =
        getCertificateStore().fetch(new TaxGroupingKey(cuit, tax), fromTime, toTime);
    while (range.hasNext()) {
      Map<String, Object> cert = new HashMap<>();
      KeyValue<Long, Certificate> next = range.next();
      Long timestamp = next.key;
      Certificate certificate = next.value;
      cert.put("timestamp", Instant.ofEpochMilli(timestamp).toString());
      cert.put("certificate", certificate);
      certificates.add(cert);
    }
    // close the iterator to avoid memory leaks
    range.close();
    // return a JSON response
    ctx.json(certificates);
  }
}
