# Spring Cloud Stream with Kafka Lessons

This repository is a collection of progressive hands-on labs that will guide you
through building cloud native [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream)
applications with Apache Kafka.

The workshop intends to help developers understand the abstraction provided by
[Spring Cloud Stream for Apache Kafka](https://cloud.spring.io/spring-cloud-stream-binder-kafka/spring-cloud-stream-binder-kafka.html).

In this workshop we will explore *new Functional programming model introduced in Spring Cloud Stream*.
The new Functional API further enhances developers productivity by eliminating even more boilerplate.

## Setting up Kafka
The workshop assumes the Kafka is running on your local machine with default ports.
You can use the docker-compose file given in this repo to try the labs.

You need to have [Docker](https://docs.docker.com/install/) and [Docker Compose](https://docs.docker.com/compose/install/)
installed on your workstation.  

`docker-compose up -d --build`

This should start Kafka and Zookeeper in your local Docker.
Check that the containers are running properly.

`docker ps`

Run `stop-docker.sh` to stop all docker containers on your workstation.

**Note - It is better to stop and start Kafka after each lab.
This will help you practice important commands and will keep the topics separate for each lab for ease of understanding.**

## Labs

The labs are based on series of lessons. Each lesson adds a new functionality.

* [Lesson1](https://github.com/msathe-tech/SCSt-Kafka-lessons/tree/master/lesson1)
* [Lesson2](https://github.com/msathe-tech/SCSt-Kafka-lessons/tree/master/lesson2)
* [Lesson3](https://github.com/msathe-tech/SCSt-Kafka-lessons/tree/master/lesson3)
* [Lesson4](https://github.com/msathe-tech/SCSt-Kafka-lessons/tree/master/lesson4)
* [Lesson5](https://github.com/msathe-tech/SCSt-Kafka-lessons/tree/master/lesson5)

We will use Hoxton.RELEASE for Spring Cloud.

Each lab will walk you through steps to inspect the Kafka setup as needed.

## Lesson1
In this lab you will learn how to use Consumer interface to create a basic Kafka streaming application.
The application simply consumes stream of incoming events and prints them on the console.
The idea here is to show how simple it is to write a Kafka Streaming application with absolutely no boilerplate code or config.
The application demonstrates how the Spring Cloud Stream API creates necessary bindings and resources attached to the bindings.
The Spring Cloud Stream binder for Kafka Streams creates the necessary resources i.e. Topics.
The application level binding with the Kafka topic is also built and injected by Spring Cloud Stream (SCSt)

## Lesson2
In this lab you will learn how to build a stateful streaming transformation application.
This lab introduces few important concepts -
* windowing
* aggregations on tumbling windows
* materialized view
It demonstrates how KStreams make it  easy to compute aggregations on a stream of events.
This application counts words from the stream of incoming events and emits a stream of WordCount objects.
This application uses Function as it takes one input and produces one output.
The SCSt API will create the input as well as output bindings and the topics attached to the bindings.

## Lesson3
Lesson3 is similar to Lesson2. However, there is one major new feature. In this lesson
we will learn how to use multiplexed bindings. The input stream binding takes events from more than one topic.
In this case we will create three topics manually and point them to the bindings.
For the first time we will explicitly mention the topic names for the binding.

## Lesson4
This lab introduces few new concept of content based events routing. The application counts words from the input stream of text.
Based on the language of the text the WordCount events are sent to a different topic. English words go to the english topic, Spanish words go to the spanish topic and Hindi words go to the hindi topic.

## Lesson5
This is the most advanced lab. It helps you understand few advanced concepts -
* bifunction
* KStream and KTable duality
* joining KStream and KTable to produce a KStream

BiFunction is used here as the application takes two inputs and produces one output.

### Contributions
SCSt contributor - Soby Chacko
