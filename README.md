# poc-motor-impuestos

## Create docker volumes


```
docker volume create zk-data
docker volume create zk-txn-logs
docker volume create kafka-data
docker volume create motor-impuestos-mongo-data
```

## Create topics

```
docker exec -it broker bash
kafka-topics --bootstrap-server broker:9092 --topic taxes-calculations --replication-factor 1 --partitions 4 --create
kafka-topics --bootstrap-server broker:9092 --topic merchants --replication-factor 1 --partitions 4 --create
kafka-topics --bootstrap-server broker:9092 --topic certificates --replication-factor 1 --partitions 4 --create
```

## Create mongo connector

```
curl --location --request POST 'http://192.168.0.100:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "mongo-motorimpuestos-sink",
    "config": {
        "connector.class": "com.mongodb.kafka.connect.MongoSinkConnector",
        "key.converter.schemas.enable": "false",
        "database": "TaxesDocuments",
        "document.id.strategy": "com.mongodb.kafka.connect.sink.processor.id.strategy.FullKeyStrategy",
        "topics": "certificates",
        "connection.uri": "mongodb://root:example@192.168.0.100:27017",
        "value.converter.schemas.enable": "false",
        "name": "mongo-motorimpuestos-sink",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter",
        "collection": "certificates",
        "key.converter": "org.apache.kafka.connect.storage.StringConverter"
    }
}'
```

## Produce messages
```
docker exec -it broker bash
kafka-console-producer --bootstrap-server broker:9092 --topic taxes-calculations --property 'parse.key=true' --property 'key.separator=|' < /data/taxes-calculations-test-window-a.json
```

## References
[Kafka Documentation](https://kafka.apache.org/documentation/)  
[Kafka Connect](https://kafka.apache.org/documentation/#connect)  
[Kafka Streams](https://kafka.apache.org/documentation/streams/)  
[Apache Kafka Architecture - Getting Started with Apache Kafka | by Kerem Kargın | Analytics Vidhya | Medium](https://medium.com/analytics-vidhya/apache-kafka-architecture-getting-started-with-apache-kafka-771d69ac6cef)  
[Confluent Recipes & Tutorials](https://developer.confluent.io/tutorials/)  
[Tutorial: Introduction to Streaming Application Development | Confluent Documentation](https://docs.confluent.io/platform/current/tutorials/examples/microservices-orders/docs/index.html)  
[How to join a stream and a lookup table using Kafka Streams](https://developer.confluent.io/tutorials/join-a-stream-to-a-table/kstreams.html)  
[Confluent REST Proxy API Reference | Confluent Documentation](https://docs.confluent.io/platform/current/kafka-rest/api.html)  
[Connect REST Interface | Confluent Documentation](https://docs.confluent.io/platform/current/connect/references/restapi.html#kconnect-rest-interface)  
[confluentinc/kafka-streams-examples: Demo applications and code examples for Apache Kafka's Streams API](https://github.com/confluentinc/kafka-streams-examples)  
[[KAFKA-10408] Calendar based windows - ASF JIRA](https://issues.apache.org/jira/browse/KAFKA-10408)  
[kafka-streams-examples/DailyTimeWindows.java at 7.1.1-post · confluentinc/kafka-streams-examples](https://github.com/confluentinc/kafka-streams-examples/blob/7.1.1-post/src/test/java/io/confluent/examples/streams/window/DailyTimeWindows.java)  
[Understanding Aggregate KTables Subtractor - Kafka Streams - Confluent Community](https://forum.confluent.io/t/understanding-aggregate-ktables-subtractor/4530)  
[MongoDB Kafka Connector](https://www.mongodb.com/docs/kafka-connector/current/)  
[Distributed Data for Microservices — Event Sourcing vs. Change Data Capture](https://debezium.io/blog/2020/02/10/event-sourcing-vs-cdc/)  
[Kafka Stream aggregation by two fields - Stack Overflow](https://stackoverflow.com/questions/61946838/kafka-stream-aggregation-by-two-fields)  
[Kafka Streams Windowing - Hands On](https://developer.confluent.io/learn-kafka/kafka-streams/hands-on-windowing/)  
[What is Kafka Connect? A Complete Introduction](https://developer.confluent.io/learn-kafka/kafka-connect/intro/)  
[Stream Aggregation In Kafka. Introducing the aggregation in Kafka… | by Narayan Kumar | Medium](https://mail-narayank.medium.com/stream-aggregation-in-kafka-e57aff20d8ad)  
[Understanding Kafka partition assignment strategies and how to write your own custom assignor | by Florian Hussonnois | StreamThoughts | Medium](https://medium.com/streamthoughts/understanding-kafka-partition-assignment-strategies-and-how-to-write-your-own-custom-assignor-ebeda1fc06f3)  
[Understanding Materialized Views — Part 1 | by Dunith Dhanushka | Event-driven Utopia | Medium](https://medium.com/event-driven-utopia/understanding-materialized-views-bb18206f1782)  
[Understanding Materialized Views — Part 2 | by Dunith Dhanushka | Event-driven Utopia | Medium](https://medium.com/event-driven-utopia/understanding-materialized-views-part-2-ae957d40a403)  
[Understanding Materialized Views — 3 : Stream-Table Joins with CDC | by Dunith Dhanushka | Event-driven Utopia | Medium](https://medium.com/event-driven-utopia/understanding-materialized-views-3-stream-table-joins-with-cdc-77591d2d6fa0)  
[mitch-seymour/mastering-kafka-streams-and-ksqldb at 1st-edition](https://github.com/mitch-seymour/mastering-kafka-streams-and-ksqldb/tree/1st-edition)  
[Should You Put Several Event Types in the Same Kafka Topic? | Confluent](https://www.confluent.io/blog/put-several-event-types-kafka-topic/)  
[Processing guarantees in Kafka. Each of the projects I’ve worked on in… | by Andy Bryant | Medium](https://medium.com/@andy.bryant/processing-guarantees-in-kafka-12dd2e30be0e)  
[Exactly-once Semantics is Possible: Here's How Apache Kafka Does it](https://www.confluent.io/blog/exactly-once-semantics-are-possible-heres-how-apache-kafka-does-it/)  
[Kafka Clients (At-Most-Once, At-Least-Once, Exactly-Once, and Avro Client) - DZone Big Data](https://dzone.com/articles/kafka-clients-at-most-once-at-least-once-exactly-o)  
[Kafka Streams' Take on Watermarks and Triggers | Confluent](https://www.confluent.io/blog/kafka-streams-take-on-watermarks-and-triggers/)  
[Microservices, Apache Kafka, and Domain-Driven Design | Confluent](https://www.confluent.io/blog/microservices-apache-kafka-domain-driven-design/)  
[Kafka Streams Data (Re)Processing Scenarios - Apache Kafka - Apache Software Foundation](https://cwiki.apache.org/confluence/display/KAFKA/Kafka+Streams+Data+%28Re%29Processing+Scenarios)  
[Data Reprocessing with the Streams API in Kafka: Resetting a Streams Application | Confluent](https://www.confluent.io/blog/data-reprocessing-with-kafka-streams-resetting-a-streams-application/)  
[Kafka Streams Application Reset Tool | Confluent Documentation](https://docs.confluent.io/platform/current/streams/developer-guide/app-reset-tool.html#)  
[Streaming Event Processors - Axon Reference Guide](https://docs.axoniq.io/reference-guide/axon-framework/events/event-processors/streaming#replay-api)  
[Dual Writes - The Unknown Cause of Data Inconsistencies](https://thorben-janssen.com/dual-writes/)  
[Implementing the Outbox Pattern with CDC using Debezium](https://thorben-janssen.com/outbox-pattern-with-cdc-and-debezium/)  
[Handling Eventual Consistency with Distributed Systems | by Mario Bittencourt | SSENSE-TECH | Medium](https://medium.com/ssense-tech/handling-eventual-consistency-with-distributed-system-9235687ea5b3)  
[Dispelling the Eventual Consistency FUD when using Event Sourcing](https://developer.axoniq.io/w/dispelling-the-eventual-consistency-fud-when-using-event-sourcing)  
[Kappa Architecture is Mainstream Replacing Lambda](https://www.kai-waehner.de/blog/2021/09/23/real-time-kappa-architecture-mainstream-replacing-batch-lambda/)  
[Scaling Microservices with an Event Stream | Thoughtworks](https://www.thoughtworks.com/insights/blog/scaling-microservices-event-stream)  
[Event-Driven Microservices Communication with Confluent](https://www.confluent.io/use-case/event-driven-microservices-communication/)  
[A comparison of stream processing frameworks – Kapernikov](https://kapernikov.com/a-comparison-of-stream-processing-frameworks/)  
[Martin Fowler - Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html)  
[Pattern: Event sourcing](https://microservices.io/patterns/data/event-sourcing.html)  
[Event sourcing pattern - AWS Prescriptive Guidance](https://docs.aws.amazon.com/prescriptive-guidance/latest/modernization-data-persistence/service-per-team.html)  
[Implementing event sourcing using a relational database](https://softwaremill.com/implementing-event-sourcing-using-a-relational-database/)  
[Martin Fowler - CQRS](https://martinfowler.com/bliki/CQRS.html)  
[CQRS pattern - Azure Architecture Center | Microsoft Learn](https://docs.microsoft.com/en-us/azure/architecture/patterns/cqrs)  
[Build a near real-time data aggregation pipeline using a serverless, event-driven architecture | AWS Database Blog](https://aws.amazon.com/es/blogs/database/build-a-near-real-time-data-aggregation-pipeline-using-a-serverless-event-driven-architecture/)  
[Anti-patterns in event modelling - State Obsession - Event-Driven.io](https://event-driven.io/en/state-obsession/)  
[Domain events: Design and implementation | Microsoft Learn](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/domain-events-design-implementation)  

### Experts
[Matthias J. Sax](https://stackoverflow.com/users/4953079/matthias-j-sax?tab=topactivity)  
[Alexey Zimarev](https://stackoverflow.com/users/484041/alexey-zimarev)  
[Kai Waehner](https://www.confluent.io/blog/author/kai-waehner/)  
