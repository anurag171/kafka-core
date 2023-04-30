package com.focuslearning.example.kafka;

import com.riferrei.kafka.core.BucketPriorityConfig;
import com.riferrei.kafka.core.BucketPriorityPartitioner;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PriorityAppConfig {


    @Autowired
    private KafkaProperties kafkaProperties;


    @Bean
    public Map<String, Object> producerPriorityConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, BucketPriorityPartitioner.class.getName());
        props.put(BucketPriorityConfig.TOPIC_CONFIG,"bucket-priority-topic");
        //props.put(BucketPriorityConfig.BUCKET_CONFIG,"Fresh,Retry");
        props.put(BucketPriorityConfig.BUCKETS_CONFIG,"Fresh,Retry");
        props.put(BucketPriorityConfig.ALLOCATION_CONFIG,"80%,20%");
        return props;
    }

    @Bean
    public ProducerFactory<String, String> priorityProducerFactory(){
        return new DefaultKafkaProducerFactory<>(producerPriorityConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaPriorityTemplate() {
        return new KafkaTemplate<>(priorityProducerFactory());
    }



}
