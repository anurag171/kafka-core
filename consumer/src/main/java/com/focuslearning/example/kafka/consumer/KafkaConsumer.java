package com.focuslearning.example.kafka.consumer;

import java.util.stream.StreamSupport;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
	
	Logger logger =  LoggerFactory.getLogger(KafkaConsumer.class);
	
	/*@KafkaListener(topics = "world_commodities_rates", clientIdPrefix = "string",
			containerFactory = "kafkaListenerStringContainerFactory")
	public void listenasString(ConsumerRecord<String, String> cr,
			@Payload String payload) {
		logger.info("Logger 2 [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
				typeIdHeader(cr.headers()), payload, cr.toString());
		//latch.countDown();
	}

	@KafkaListener(topics = "advice-topic", clientIdPrefix = "bytearray",
			containerFactory = "kafkaListenerByteArrayContainerFactory")
	public void listenAsByteArray(ConsumerRecord<String, byte[]> cr,
			@Payload byte[] payload) {
		logger.info("Logger 3 [ByteArray] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
				typeIdHeader(cr.headers()), payload, cr.toString());
		//latch.countDown();
	}



	@KafkaListener(topics = "t-commodity-promotion_uppercase", clientIdPrefix = "string",
			containerFactory = "kafkaListenerStringContainerFactory")
	public void upperCaseConsumer(ConsumerRecord<String, byte[]> cr,
								  @Payload byte[] payload) {
		logger.info("Logger 3 [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
				typeIdHeader(cr.headers()), payload, cr.toString());
		//latch.countDown();
	}*/

	@KafkaListener(topics = "bucket-priority-topic", clientIdPrefix = "string",
			containerFactory = "concurrentPriorityKafkaListenerContainerFactory",autoStartup = "false",beanRef = "priorityListener",id="priorityListener")
	public void priorityConsumer(ConsumerRecord<String, String> cr,
							   @Payload String payload) {
		logger.info("Priority Consumer [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
				typeIdHeader(cr.headers()), payload, cr.toString());
		//latch.countDown();
	}

	@KafkaListener(topics = "bucket-priority-topic", clientIdPrefix = "string",
			containerFactory = "concurrentLowPriorityKafkaListenerContainerFactory",autoStartup = "false",id ="nonPriorityListener",beanRef = "nonPriorityListener")
	public void lowPriorityConsumer(ConsumerRecord<String, String> cr,
							   @Payload String payload) {
		logger.info("Low Priority Consumer [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
				typeIdHeader(cr.headers()), payload, cr.toString());
		//latch.countDown();
	}

	private static String typeIdHeader(Headers headers) {
		return StreamSupport.stream(headers.spliterator(), false)
				.filter(header -> header.key().equals("__TypeId__"))
				.findFirst().map(header -> new String(header.value())).orElse("N/A");
	}

}
