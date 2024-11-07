# bus-ETA-calculation
Project for an event driven system where the publisher emits the current location of a bus into a message broker and the consumer is an app for the bus stops which use the location of buses to calculate and show ETA to people waiting.

# Functional Testing

## Pulling kafka image
```docker pull apache/kafka```

## Run docker container locally
```docker container run --name kafka-broker -p 9092:9092 apache/kafka:latest```

## Reading messages from kafka
```docker exec --workdir /opt/kafka/bin/ -it kafka-broker sh```

```./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic bus-location-queue```
