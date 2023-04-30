package com.focuslearning.example.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaPriorityProducer {

	Logger logger = LoggerFactory.getLogger(KafkaPriorityProducer.class);
	private KafkaTemplate<String, String> kafkaPriorityTemplate;


	public KafkaPriorityProducer(KafkaTemplate<String, String> kafkaPriorityTemplate) {
		this.kafkaPriorityTemplate =  kafkaPriorityTemplate;
	}

	public void sendMessage(String topic,String message) {
		ListenableFuture<SendResult<String, String>> listenableFuture	 =kafkaPriorityTemplate.send(topic, message);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				logger.info(result.getProducerRecord().key());
				logger.info(String.valueOf(result.getRecordMetadata().offset()));
				logger.info(String.valueOf(result.getRecordMetadata().partition()));
				logger.info(String.valueOf(result.getRecordMetadata().timestamp()));
				logger.info(result.getProducerRecord().value());

			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub
				
			}
		});


	}

	public void sendMessage(String topic,String key,String message) {
		ListenableFuture<SendResult<String, String>> listenableFuture	 =kafkaPriorityTemplate.send(topic,key, message);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				logger.info(result.getProducerRecord().value());
				logger.info(result.getProducerRecord().key());
				logger.info(String.valueOf(result.getRecordMetadata().offset()));
				logger.info(String.valueOf(result.getRecordMetadata().partition()));
				logger.info(String.valueOf(result.getRecordMetadata().timestamp()));

			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub

			}
		});


	}

}
