package com.focuslearning.example.kafka.consumer;

import com.riferrei.kafka.core.BucketPriorityAssignor;
import com.riferrei.kafka.core.BucketPriorityConfig;
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

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PriorityAppConsumerConfig {
	
	Logger logger =  LoggerFactory.getLogger(PriorityAppConsumerConfig.class.getName());


	@Autowired
	KafkaProperties kafkaProperties;

	@Bean
	public Map<String, Object> consumerPriorityConfigs() {
		Map<String, Object> props = new HashMap<>(
				kafkaProperties.buildConsumerProperties()
				);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG,
				"my-group");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
				BucketPriorityAssignor.class.getName());
		props.put(BucketPriorityConfig.TOPIC_CONFIG, "bucket-priority-topic");
		props.put(BucketPriorityConfig.BUCKETS_CONFIG, "Fresh, Retry");
		props.put(BucketPriorityConfig.ALLOCATION_CONFIG, "70%, 30%");
		props.put(BucketPriorityConfig.BUCKET_CONFIG, "Fresh");

		return props;
	}

	@Bean
	public Map<String, Object> consumerLowPriorityConfigs() {
		Map<String, Object> props = new HashMap<>(
				kafkaProperties.buildConsumerProperties()
		);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG,
				"my-group");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
				BucketPriorityAssignor.class.getName());
		props.put(BucketPriorityConfig.TOPIC_CONFIG, "bucket-priority-topic");
		props.put(BucketPriorityConfig.BUCKETS_CONFIG, "Fresh, Retry");
		props.put(BucketPriorityConfig.ALLOCATION_CONFIG, "70%, 30%");
		props.put(BucketPriorityConfig.BUCKET_CONFIG, "Retry");

		return props;
	}

	@Bean
	public ConsumerFactory<String, Object> priorityConsumerFactory(){

		return new DefaultKafkaConsumerFactory<>(consumerPriorityConfigs());
	}

	@Bean
	public ConsumerFactory<String, Object> lowPriorityConsumerFactory(){

		return new DefaultKafkaConsumerFactory<>(consumerLowPriorityConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> concurrentPriorityKafkaListenerContainerFactory(){

		ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaListenerContainerFactory =  new ConcurrentKafkaListenerContainerFactory<>();
		concurrentKafkaListenerContainerFactory.setConsumerFactory(priorityConsumerFactory());
		return concurrentKafkaListenerContainerFactory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> concurrentLowPriorityKafkaListenerContainerFactory(){

		ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaListenerContainerFactory =  new ConcurrentKafkaListenerContainerFactory<>();
		concurrentKafkaListenerContainerFactory.setConsumerFactory(lowPriorityConsumerFactory());
		return concurrentKafkaListenerContainerFactory;
	}

	/*@Bean
	public ConsumerFactory<String, String> stringPriorityConsumerFactory() {

		Map<String, Object> props = new HashMap<>(
				kafkaProperties.buildConsumerProperties()
		);
		props.put(ConsumerConfig.GROUP_ID_CONFIG,"my-group-1");
		return new DefaultKafkaConsumerFactory<>(
				props, new StringDeserializer(), new StringDeserializer()
				);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaPriorityListenerStringContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(stringPriorityConsumerFactory());

		return factory;
	}

	// Byte Array Consumer Configuration

	@Bean
	public ConsumerFactory<String, byte[]> bytePriorityArrayConsumerFactory() {
		Map<String, Object> props = new HashMap<>(
				kafkaProperties.buildConsumerProperties()
		);
		props.put(ConsumerConfig.GROUP_ID_CONFIG,"my-group-2");
		return new DefaultKafkaConsumerFactory<>(
				props, new StringDeserializer(), new ByteArrayDeserializer()
				);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerPriorityByteArrayContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(bytePriorityArrayConsumerFactory());
		return factory;
	}*/

	

}
