# Content based message routing to multiple topics
Streaming app sends events to different topics based on the message content.

## Start the application
App should start once the Kafka is running.
The Binder in the app dependency will create required topic.
This app demonstrates Predicates and Branching.

Pay attention to `spring.cloud.stream.bindings` property in the `application.properties` file.

## Use the local Kafka to generate messages
Access the docker container for the broker.

`docker ps --format '{{.Names}}'`

`docker ps --format '{{.Names}}' | head -n 1`

`docker exec -it $(docker ps --format '{{.Names}}' | head -n 1) /bin/bash`

Use CLI to send messages on the topic.

`kafka-topics --zookeeper zookeeper:2181 --list`

Start one producer console in one shell -

`kafka-console-producer --broker-list localhost:9092 --topic languageWordCount-in-0`

Start first consumer console in second shell -

`kafka-console-consumer --bootstrap-server localhost:9092 --topic english`

Start second consumer console in third shell -

`kafka-console-consumer --bootstrap-server localhost:9092 --topic french`

Start third consumer in another shell -

`kafka-console-consumer --bootstrap-server localhost:9092 --topic hindi`
