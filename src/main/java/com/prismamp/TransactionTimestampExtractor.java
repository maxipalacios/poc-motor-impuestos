package com.prismamp;

import com.prismamp.model.Transaction;
import java.time.Instant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

/** This class allows us to use event-time semantics for transaction streams */
public class TransactionTimestampExtractor implements TimestampExtractor {

  @Override
  public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
    Transaction transaction = (Transaction) record.value();
    if (transaction != null && transaction.getTimestamp() != null) {
      String timestamp = transaction.getTimestamp();
      // System.out.println("Extracting timestamp: " + timestamp);
      return Instant.parse(timestamp).toEpochMilli();
    }
    // fallback to stream time
    return partitionTime;
  }
}
