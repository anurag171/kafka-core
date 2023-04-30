package com.focuslearning.example.kafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

@Configuration
public class AppConfig {
	
	@Autowired
    private KafkaProperties kafkaProperties;
	
	
	@Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return props;
    }
	
	@Bean
	public ProducerFactory<String, Object> producerFactory(){
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}
	
	@Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
	
	/*@Bean
	public RestTemplate restTemplate() throws IOException {
		
//		ClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
//		clientHttpRequestFactory.createRequest(URI.create("http://localhost:8080/customer/1"), HttpMethod.GET);
		
		return new RestTemplate();
	}*/
}
