#Stateful streaming transformation
Input stream of text is split up and words are counted, keeping the state alive.
The word counts are emitted to output stream.
This application uses Function interface because it takes one input and produces one output.

## Start the application
App should start once the Kafka is running.
The Binder in the app dependency will create required topic.

## Use the local Kafka to generate messages
Access the docker container for the broker.

`docker ps --format '{{.Names}}'`

`docker ps --format '{{.Names}}' | head -n 1`

`docker exec -it $(docker ps --format '{{.Names}}' | head -n 1) /bin/bash`

Use CLI to send messages on the topic.

`kafka-topics --zookeeper zookeeper:2181 --list`

The topic name is created using the name of the method, in this wordCount. By default, it will create one topic for input stream and one for output stream.


Start producer console in one shell -

`kafka-console-producer --broker-list localhost:9092 --topic wordCount-in-0`

Start consume console in another shell -

`kafka-console-consumer --bootstrap-server localhost:9092 --topic wordCount-out-0`
