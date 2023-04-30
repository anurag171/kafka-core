package com.focuslearning.example.kafka.consumer;

import com.focuslearning.example.kafka.PaymentData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaymentDataConsumerConfig {

    @Bean
    public Map<String, Object> paymentConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, PaymentData> paymentDataConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                paymentConsumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(PaymentData.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentData> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentData> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentDataConsumerFactory());
        return factory;
    }

}
