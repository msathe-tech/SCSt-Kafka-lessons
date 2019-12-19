package com.example.demo;

import java.util.function.BiFunction;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public BiFunction<KStream<String, Long>, KTable<String, String>, KStream<String, Long>>  streamTableBiFunction() {
		return (userClickStream, userRegionTable) -> (userClickStream
			.leftJoin(userRegionTable,
					(clicks, region) ->
							new RegionWithClicks(region == null ? "UNKNOWN" : region, clicks),
					Joined.with(Serdes.String(), Serdes.Long(), null))
			.map((user, regionWithClicks) -> new KeyValue<>(regionWithClicks.getRegion(), regionWithClicks.getClicks()))
			.groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
			.reduce(Long::sum)
			.toStream()
		);
	}

}
