package com.focuslearning.example.kafka.cache;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelCast {

    @Bean
    public IMap<String,String> iMap(){
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hz_personal");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        return client.getMap("my-distributed-map");
    }
}
