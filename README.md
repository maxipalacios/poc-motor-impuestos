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

