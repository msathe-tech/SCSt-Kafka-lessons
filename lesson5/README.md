# Join KStream and KTable using BiFunction
This application demonstrates ability to join KStream and KTable and generate an output KStream.
The application has following components -

1. populateUserRegionMap - this loads USER_REGION topic with map of <User, Region>
2. generateClickStream - this loads USER_CLICKS topic with <User, Clicks>
3. streamTableBiFunction - this joins USER_CLICKS and USER_REGION and produces REGION_CLICKS with map of <Region, Clicks>
4. checkRegionClicks - prints REGION_CLICKS stream 

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
