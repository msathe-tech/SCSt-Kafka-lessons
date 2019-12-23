# Join KStream and KTable using BiFunction
This application demonstrates ability to join KStream and KTable and generate an output KStream.
The USER_CLICKS KStream has stream of Click count per user. USER_REGION KTable has map of User and Region. 
The REGION_CLICKS KStream produced by this application has a continuous aggregate count of Clicks per Region. 

The application has following components -

1. populateUserRegionMap - this loads USER_REGION topic with map of <User, Region>
2. generateClickStream - this loads USER_CLICKS topic with <User, Clicks>
3. streamTableBiFunction - this joins USER_CLICKS and USER_REGION and produces REGION_CLICKS with map of <Region, Clicks>
4. checkRegionClicks - prints REGION_CLICKS stream 

## Start the application 

Make sure the kafka is running -

`docker ps`

Run the app 

The Binder in the app dependency will create required topic. 

## Check the topics created
Access the docker container for the broker.

`docker ps --format '{{.Names}}'`

`docker ps --format '{{.Names}}' | head -n 1`

`docker exec -it $(docker ps --format '{{.Names}}' | head -n 1) /bin/bash`

Use CLI to send messages on the topic. 

`kafka-topics --zookeeper zookeeper:2181 --list`

In this demo you can't see the data for USER_CLICKS and REGION_CLICKS topics because the value data type is not String. 
The app will print the data output for you so you don't need to watch the topics separately. 
