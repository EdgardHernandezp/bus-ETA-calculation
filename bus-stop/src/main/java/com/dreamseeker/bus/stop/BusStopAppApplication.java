package com.dreamseeker.bus.stop;

import java.util.HashMap;
import java.util.Map;

import com.dreamseeker.bus.stop.domains.BusEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@SpringBootApplication
@EnableKafka
public class BusStopAppApplication {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, BusEvent> kafkaListenerContainerFactory(
			ConsumerFactory<String, BusEvent> consumerFactory,
			RecordMessageConverter converter) {
		ConcurrentKafkaListenerContainerFactory<String, BusEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setRecordMessageConverter(converter);
		return factory;
	}

	@Bean
	public RecordMessageConverter converter() {
		return new JsonMessageConverter();
	}

	@Bean
	public ConsumerFactory<String, BusEvent> consumerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServer) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	public static void main(String[] args) {
		SpringApplication.run(BusStopAppApplication.class, args);
	}

}
