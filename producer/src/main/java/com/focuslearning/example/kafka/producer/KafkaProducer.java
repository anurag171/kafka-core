package com.focuslearning.example.kafka.producer;

import com.focuslearning.example.kafka.PaymentData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaProducer{

	private KafkaTemplate<String, Object> kafkaTemplate;

	public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate =  kafkaTemplate;
	}

	public void sendMessage(String topic,Object message) {
		ListenableFuture<SendResult<String, Object>> listenableFuture	 =kafkaTemplate.send(topic, message);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

			@Override
			public void onSuccess(SendResult<String, Object> result) {
				System.out.println(result.getProducerRecord().value());
				System.out.println(result.getProducerRecord().key());
				System.out.println(result.getRecordMetadata().offset());
				System.out.println(result.getRecordMetadata().partition());
				System.out.println(result.getRecordMetadata().timestamp());
				
			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub
				
			}
		});


	}

	public void sendMessage(Message<PaymentData> message) {
		ListenableFuture<SendResult<String, Object>> listenableFuture	 =kafkaTemplate.send( message);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

			@Override
			public void onSuccess(SendResult<String, Object> result) {
				System.out.println(result.getProducerRecord().value());
				System.out.println(result.getProducerRecord().key());
				System.out.println(result.getRecordMetadata().offset());
				System.out.println(result.getRecordMetadata().partition());
				System.out.println(result.getRecordMetadata().timestamp());

			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub

			}
		});


	}

	public void sendMessage(String topic,String key,Object message) {
		ListenableFuture<SendResult<String, Object>> listenableFuture	 =kafkaTemplate.send((java.lang.String) topic,key, message);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

			@Override
			public void onSuccess(SendResult<String, Object> result) {
				System.out.println(result.getProducerRecord().value());
				System.out.println(result.getProducerRecord().key());
				System.out.println(result.getRecordMetadata().offset());
				System.out.println(result.getRecordMetadata().partition());
				System.out.println(result.getRecordMetadata().timestamp());

			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub

			}
		});


	}

}
