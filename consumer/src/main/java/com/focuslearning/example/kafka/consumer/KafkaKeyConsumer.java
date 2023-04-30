package com.focuslearning.example.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaKeyConsumer {

    Logger logger =  LoggerFactory.getLogger(KafkaKeyConsumer.class);

    /*@KafkaListener(topics = "world_commodities_rates",containerFactory = "concurrentKafkaListenerContainerFactory")
    public void consumer(ConsumerRecord<String, String> consumerRecord){
        logger.info("Received key [{}] , partition [{}],offset [{}],headers [{}], message [{}],timestamp [{}] from topic [{}]",
                consumerRecord.key(),
                consumerRecord.partition(),
                consumerRecord.offset(),
                consumerRecord.headers(),
                consumerRecord.value(),
                consumerRecord.timestamp(),
                consumerRecord.topic());
    }*/
}
