package com.focuslearning.example.kafka;

import com.focuslearning.example.kafka.consumer.KafkaConsumer;
import com.focuslearning.example.kafka.crypto.CipherService;
import com.focuslearning.example.kafka.crypto.CipherUtil;
import com.focuslearning.example.kafka.producer.KafkaPriorityProducer;
import com.focuslearning.example.kafka.streams.config.KafkaStreamConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.bouncycastle.cms.CMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@Import(KafkaStreamConfig.class)
@EnableScheduling
public class Application {

    Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    KafkaPriorityProducer kafkaPriorityProducer;

    @Value("${startProducer:false}")
    boolean startProducer;

    @Value("${startConsumer:false}")
    boolean startConsumer;


    IMap<Optional<String>, Optional<String>> iMap;

    @Autowired
    KafkaConsumer consumer;

    @Autowired
    CipherUtil cipherUtil;

    @Autowired
    CipherService cipherService;

    @Autowired
    JdbcTemplate jdbcTemplate;

   /* @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;*/

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void appReadyEvent() throws InterruptedException, CertificateEncodingException, IOException, CMSException {
        logger.info("********Application Ready !!**********");

        //.
        //
        // 00/jdbcTemplate.execute();

       /* Map<String,String> accountMap  = createData();
        AtomicInteger counter = new AtomicInteger(0);

        for (int i =0;i<70 && startProducer ;i++) {
            Faker faker = new Faker();
            int value = counter.incrementAndGet();
            final String recordKey = "Fresh-" + value;

            kafkaPriorityProducer.sendMessage("bucket-priority-topic", recordKey, String.join(",",faker.name().fullName()));
            Thread.sleep(100);

        }
        counter.setRelease(0);
        for (int i =0;i<20 && startProducer ;i++) {
            Faker faker = new Faker();
            int value = counter.incrementAndGet();
            final String recordKey = "Retry-" + value;
            kafkaPriorityProducer.sendMessage("bucket-priority-topic", recordKey, String.join(",",faker.funnyName().name()));
            Thread.sleep(1000);

        }
        if(startConsumer) {
            int counter1 = 0;
            Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer("priorityListener")).start();
            while (kafkaListenerEndpointRegistry.getListenerContainer("priorityListener").isRunning()) {
                logger.info("Is priorityListener Running");
                counter1++;
                if (counter1 >= 10) {
                    kafkaListenerEndpointRegistry.getListenerContainer("priorityListener").pause();
                    break;
                }
            }
            logger.info("priorityListener paused");
            Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer("nonPriorityListener")).start();

            int counter2 = 0;
            while (kafkaListenerEndpointRegistry.getListenerContainer("nonPriorityListener").isRunning()) {
                logger.info("Is nonPriorityListener Running");
                counter2++;
                if (counter2 >= 5) {
                    kafkaListenerEndpointRegistry.getListenerContainer("nonPriorityListener").pause();
                    break;
                }
            }
            logger.info("nonPriorityListener paused");
        }

        HazelcastInstance client = getHazelcastInstance();
        iMap =  client.getMap("my-encrypted-value-map");

        accountMap.entrySet().stream().forEach((entry)->{
            var key = entry.getKey();
            var keyHash = cipherService.hash(key);

            var value =  entry.getValue();
            var encryptedValue = cipherService.encrypt(value);

            iMap.put(keyHash, encryptedValue);

            var encryptedHzVal = (String)client.getMap("my-encrypted-value-map").get(keyHash);


            logger.info("Decrypted Value [{}]",cipherService.decrypt(encryptedHzVal));
        });


    }

    @Bean
    public HazelcastInstance getHazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hz_personal");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        return client;
    }

    private Map<String, String> createData() {
        return Map.of("456789","304-890798-890",
                "456790","304-890799-890",
                "456791","304-890800-890",
                "456792","304-890801-890",
                "456793","304-890802-890");
    }

        */
    }
}


