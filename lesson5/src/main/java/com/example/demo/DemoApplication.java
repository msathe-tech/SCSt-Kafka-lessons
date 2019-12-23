package com.example.demo;

import java.util.Properties;
import java.util.Random;
import java.util.function.BiFunction;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	static final String BOOTSTRAP_SERVER = "localhost:9092";
	static final String USER_CLICKS_TOPIC = "streamTableBiFunction-in-0";
	static final String[] USERS = new String[] {"1", "2", "3", "4", "5", "6", "7", "8"};
	static final String[] REGIONS = new String[] {"east", "west", "north", "south"};
	static final UserRegsionMap[] USER_REGIONS = new UserRegsionMap[] {
			new UserRegsionMap(REGIONS[0], USERS[0]),
			new UserRegsionMap(REGIONS[0], USERS[1]),
			new UserRegsionMap(REGIONS[1], USERS[2]),
			new UserRegsionMap(REGIONS[1], USERS[3]),
			new UserRegsionMap(REGIONS[2], USERS[4]),
			new UserRegsionMap(REGIONS[3], USERS[5]),
			new UserRegsionMap(REGIONS[3], USERS[6]),
			new UserRegsionMap(REGIONS[3], USERS[7]),
	};

	static final Long[] CLICKS = new Long[] {
			(long) 10,
			(long) 20,
			(long) 430,
			(long) 190,
			(long) 109,
			(long) 120,
			(long) 205,
			(long) 330,
			(long) 30,
			(long) 80,
	};

	Random random = new Random();

	static final Logger log = LoggerFactory.getLogger(DemoApplication.class);


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

	@Bean
	@ConditionalOnProperty(name="role", havingValue = "generateClickStream", matchIfMissing = false)
	public CommandLineRunner generateClickStream() {
		return (args) -> {
			Properties props = getStreamsConfiguration("generateClickStream", "org.apache.kafka.common.serialization.StringSerializer",
					"org.apache.kafka.common.serialization.LongSerializer");

			Producer<String, Long> producer = new KafkaProducer<String, Long>(props);

			ProducerRecord<String, Long> userClick = new ProducerRecord<>(USER_CLICKS_TOPIC,
					USERS[random.nextInt(8)], CLICKS[random.nextInt(10)]);

			RecordMetadata metadata = producer.send(userClick).get();

			log.info("partion = {} offset = {}", metadata.partition(), metadata.offset());

			producer.flush();
			producer.close();


		};
	}

	static Properties getStreamsConfiguration(final String client_id,
			final String keySerdeClass, final String valueSerdeClass ) {
		final Properties streamsConfiguration = new Properties();
		// Give the Streams application a unique name.  The name must be unique in the Kafka cluster
		// against which the application is run.
		//streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "lesson5-streamTableBiFunction");
		//streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG,  client_id);
		// Where to find Kafka broker(s).
		streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		// Specify default (de)serializers for record keys and for record values.
		streamsConfiguration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerdeClass);
		streamsConfiguration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerdeClass);
		// Records should be flushed every 10 seconds. This is less than the default
		// in order to keep this example interactive.
		// streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
		// For illustrative purposes we disable record caches.
		// streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		// Use a temporary directory for storing state, which will be automatically removed after the test.
		//streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath());
		return streamsConfiguration;
	}


}
