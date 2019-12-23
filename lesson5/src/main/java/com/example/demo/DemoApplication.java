package com.example.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.ForeachAction;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {
	static final String BOOTSTRAP_SERVER = "localhost:9092";
	static final String USER_CLICKS_TOPIC = "USER_CLICKS";
	static final String USER_REGION_TOPIC = "USER_REGION";
	static final String[] USERS = new String[] {"1", "2", "3", "4", "5", "6", "7", "8"};
	static final String[] REGIONS = new String[] {"east", "west", "north", "south"};
	static final List<UserRegsionMap> USER_REGION_LIST = Arrays.asList(new UserRegsionMap[] {
			new UserRegsionMap(REGIONS[0], USERS[0]),
			new UserRegsionMap(REGIONS[0], USERS[1]),
			new UserRegsionMap(REGIONS[1], USERS[2]),
			new UserRegsionMap(REGIONS[1], USERS[3]),
			new UserRegsionMap(REGIONS[2], USERS[4]),
			new UserRegsionMap(REGIONS[3], USERS[5]),
			new UserRegsionMap(REGIONS[3], USERS[6]),
			new UserRegsionMap(REGIONS[3], USERS[7]),
	});
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
	static final Properties USER_CLICK_TOPIC_PROPS = getStreamsConfiguration("org.apache.kafka.common.serialization.StringSerializer",
			"org.apache.kafka.common.serialization.LongSerializer");
	static final Properties USER_REGION_TOPIC_PROPS = getStreamsConfiguration("org.apache.kafka.common.serialization.StringSerializer",
			"org.apache.kafka.common.serialization.StringSerializer");

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
	public Consumer<KStream<String, Long>> checkRegionClicks() {
		return input -> input.foreach((region, clicks) ->
				log.info("Region = {} clicks = {}", region, clicks));
	}

	@Scheduled(initialDelay = 1000, fixedRate = 5000)
	public void generateClickStream() {
		Producer<String, Long> producer = new KafkaProducer<String, Long>(USER_CLICK_TOPIC_PROPS);
		ProducerRecord<String, Long> userClick = new ProducerRecord<>(USER_CLICKS_TOPIC,
				USERS[random.nextInt(8)], CLICKS[random.nextInt(10)]);
		try {
			RecordMetadata metadata = producer.send(userClick).get();
			log.info("Message sent to partion = {} offset = {}", metadata.partition(), metadata.offset());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		producer.flush();
		producer.close();
	}

	@Bean
	public CommandLineRunner populateUserRegionMap() {
		return (args) -> {
			USER_REGION_LIST.forEach((userRegion) -> sendUserRegion(userRegion));
		};
	}

	static void sendUserRegion(UserRegsionMap userRegion) {
		Producer<String, String> producer = new KafkaProducer<String, String>(USER_REGION_TOPIC_PROPS);
		ProducerRecord<String, String> userRegionToSend = new ProducerRecord<>(USER_REGION_TOPIC, userRegion.getUser(),
				userRegion.getRegion());
		try {
			RecordMetadata metadata = producer.send(userRegionToSend).get();
			log.info("UserRegion record added to partition = {} offset = {}", metadata.partition(), metadata.offset());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		producer.flush();
		producer.close();
	}

	static Properties getStreamsConfiguration(final String keySerdeClass, final String valueSerdeClass ) {
		final Properties streamsConfiguration = new Properties();

		streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		// Specify Serde for record keys and for record values.
		streamsConfiguration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerdeClass);
		streamsConfiguration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerdeClass);

		return streamsConfiguration;
	}
}


