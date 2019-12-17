package com.example.demo;

import java.util.Arrays;
import java.util.function.Function;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public Function<KStream<Object, String>, KStream<String, WordCount>> wordCount() {
		return input -> input
				.flatMapValues(text -> Arrays.asList(text.toLowerCase().split("\\W+")))
				.map((key, value) -> new KeyValue<>(value, value))
				.groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
				.windowedBy(TimeWindows.of(5000))
				.count(Materialized.as("wordcount-store"))
				.toStream()
				.map((key, value) -> new KeyValue<>(key.key(),
						new WordCount(key.key(), value, key.window().start(), key.window().end())))
				;
	}

}
