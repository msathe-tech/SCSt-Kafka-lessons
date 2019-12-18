package com.example.demo;

import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
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
	public Function<KStream<Object, String>, KStream<?, WordCount>[]> languageWordCount() {

		Predicate<Object, WordCount> isEnglish = (k, v) -> v.getLanguage().equals("english");
		Predicate<Object, WordCount> isFrench = (k, v) -> v.getLanguage().equals("french");
		Predicate<Object, WordCount> isHindi = (k, v) -> v.getLanguage().equals("hindi");

		return input -> input
				.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
				.map((key, value) -> new KeyValue<>(value, value))
				.groupByKey(Grouped.with(Serdes.String(), Serdes.String()))				.windowedBy(TimeWindows.of(5000))
				.count(Materialized.as("WordCounts-branch"))
				.toStream()
				.map((key, value) -> new KeyValue<>(null, new WordCount(key.key(), value,
						key.window().start(), key.window().end())))
				.branch(isEnglish, isFrench, isHindi);
	}

}
