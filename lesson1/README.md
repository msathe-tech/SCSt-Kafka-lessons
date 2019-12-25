#Simple KStream application
Learn how to use Consumer interface to create a basic KStream application.
Consumer interface is used when you only have input but no output.
That means this application doesn't emit any event back to Kafka.

## Start the application
App should start once the Kafka is running.
The Binder in the app dependency will create required topic.

## Use the local Kafka to generate messages
Access the docker container for the broker.

`docker exec -it lesson1_kafka_1 /bin/bash`

Use CLI to send messages on the topic.

`kafka-topics --zookeeper zookeeper:2181 --list`

If the topic is not there then check if the app successfully started.
`kafka-console-producer --broker-list localhost:9092 --topic process-in-0`
