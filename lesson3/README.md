# Multiplexed streams 
Stream taking input from more than one topic.

## Start the application
App should start once the Kafka is running.
The Binder in the app dependency will create required topic.
This app demonstrates multiplexed topics attached to a single input stream.
The input stream will poll for content from all the topics in the binding.

Pay attention to `spring.cloud.stream.bindings.wordCount-in-0.destination` property in the `application.properties` file.

## Use the local Kafka to generate messages
Access the docker container for the broker.

`docker ps --format '{{.Names}}'`

`docker ps --format '{{.Names}}' | head -n 1`

`docker exec -it $(docker ps --format '{{.Names}}' | head -n 1) /bin/bash`

Use CLI to send messages on the topic.

`kafka-topics --zookeeper zookeeper:2181 --list`

Start one producer console in one shell -

`kafka-console-producer --broker-list localhost:9092 --topic words1`

Start second producer console in second shell -

`kafka-console-producer --broker-list localhost:9092 --topic words2`

Start second producer console in second shell -

`kafka-console-producer --broker-list localhost:9092 --topic words3`

Start consume console in another shell -

`kafka-console-consumer --bootstrap-server localhost:9092 --topic wordCount-out-0`
