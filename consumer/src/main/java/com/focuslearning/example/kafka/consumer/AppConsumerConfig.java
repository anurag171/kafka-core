package com.focuslearning.example.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import com.focuslearning.example.kafka.PaymentData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class AppConsumerConfig {
	
	Logger logger =  LoggerFactory.getLogger(AppConsumerConfig.class.getName());


	@Autowired
	KafkaProperties kafkaProperties;

	@Bean
	public Map<String, Object> consumerConfigs() {
		JsonDeserializer<PaymentData> jsonDeserializer = new JsonDeserializer<>();
		var packageType = PaymentData.class.getTypeName();
		jsonDeserializer.addTrustedPackages(packageType);
		Map<String, Object> props = new HashMap<>(
				kafkaProperties.buildConsumerProperties()
				);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				jsonDeserializer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG,
				"my-group");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return props;
	}

	@Bean
	public ConsumerFactory<String, Object> consumerFactory(){

		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaListenerContainerFactory(){

		ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaListenerContainerFactory =  new ConcurrentKafkaListenerContainerFactory<>();
		concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
		return concurrentKafkaListenerContainerFactory;
	}

	@Bean
	public ConsumerFactory<String, String> stringConsumerFactory() {

		Map<String, Object> props = new HashMap<>(
				kafkaProperties.buildConsumerProperties()
		);
		props.put(ConsumerConfig.GROUP_ID_CONFIG,"my-group-1");
		return new DefaultKafkaConsumerFactory<>(
				props, new StringDeserializer(), new StringDeserializer()
				);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerStringContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(stringConsumerFactory());

		return factory;
	}

	// Byte Array Consumer Configuration

	@Bean
	public ConsumerFactory<String, byte[]> byteArrayConsumerFactory() {
		Map<String, Object> props = new HashMap<>(
				kafkaProperties.buildConsumerProperties()
		);
		props.put(ConsumerConfig.GROUP_ID_CONFIG,"my-group-2");
		return new DefaultKafkaConsumerFactory<>(
				props, new StringDeserializer(), new ByteArrayDeserializer()
				);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerByteArrayContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(byteArrayConsumerFactory());
		return factory;
	}

	

}
