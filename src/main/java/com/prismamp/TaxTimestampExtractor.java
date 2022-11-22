package com.prismamp;

import com.prismamp.model.Tax;
import java.time.Instant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

/** This class allows us to use event-time semantics for transaction streams */
public class TaxTimestampExtractor implements TimestampExtractor {

  @Override
  public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
    Tax tax = (Tax) record.value();
    if (tax != null && tax.getCreatedAt() != null) {
      String timestamp = tax.getCreatedAt();
      // System.out.println("Extracting timestamp: " + timestamp);
      return Instant.parse(timestamp).toEpochMilli();
    }
    // fallback to stream time
    return partitionTime;
  }
}
