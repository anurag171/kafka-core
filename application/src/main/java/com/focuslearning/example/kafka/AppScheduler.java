package com.focuslearning.example.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.focuslearning.example.kafka.consumer.KafkaConsumer;
import com.focuslearning.example.kafka.database.PaymentDataRepository;
import com.focuslearning.example.kafka.database.RecordStatus;
import com.focuslearning.example.kafka.producer.KafkaProducer;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Log4j2
public class AppScheduler {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    PaymentDataRepository paymentDataRepository;

    ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void init(){
        mapper.registerModule(new JavaTimeModule());
    }


    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void freshPaymentGenerator() {
        log.info("Starting fresh payment generator");
        postPayments();
        log.info("Stopping fresh payment generator");
    }

    private void postPayments() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.rangeClosed(1,20).forEach(i->executorService.submit(new CallPayment(i,restTemplate,kafkaProducer)));
    }


    private PaymentData paymentDataGenerator() {
        Faker faker = new Faker();
        return PaymentData.builder().paymentid(UUID.randomUUID().toString())
                .amount(String.valueOf(faker.number().randomDouble(2, 2000, 20000)))
                .dateTime(String.valueOf(faker.date().between(Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()))))
                .from(faker.finance().iban())
                .to(faker.finance().iban())
                .receiverName(faker.name().fullName())
                .senderName(faker.name().fullName())
                .retryTimes(0)
                .country(faker.country().countryCode2().toUpperCase())
                .createdtime(LocalDateTime.now())
                .expiretime(LocalDateTime.now().plusMinutes(1L))
                .status(RecordStatus.NEW.getAction())
                .build();
    }

    @Scheduled(fixedRate = 30000)
    public void clearPayments(){
        log.info("[C] Starting new cycle of scheduled task");
        Predicate<PaymentData> predicate1= paymentData -> paymentData.getStatus().equals(RecordStatus.REPLAY.getAction());
        Predicate<PaymentData> predicate2= paymentData -> paymentData.getStatus().equals(RecordStatus.NEW.getAction());
        Predicate<PaymentData> predicate= predicate1.or(predicate2);

       List<PaymentData> paymentDataList = paymentDataRepository.findAll().stream().map(paymentData -> {
           try {
               return mapper.readValue(paymentData.getMessage(),PaymentData.class);
           } catch (JsonProcessingException e) {
               e.printStackTrace();
           }
       return null;}).collect(Collectors.toList());
       String whereClause = paymentDataList.stream()
                            .filter(predicate)
                            .filter(paymentData ->paymentData.getExpiretime().isBefore(LocalDateTime.now()))
                            .map(paymentData -> paymentData.getPaymentid())
                            .collect(Collectors.joining("','","('","')"));

        log.info("Where clause {}",whereClause);
        paymentDataRepository.updateById(whereClause);


        log.info("[C] Done the cycle of scheduled task");
    }

    @Scheduled(fixedDelay = 15000, initialDelay = 15000)
    public void stalePaymentGenerator() {
        log.info("[B] Starting new cycle of scheduled task");
        List<Object[]> paymentDataList =paymentDataRepository.findAllByLimit(200);
       log.info("Payment data list size {} ,details {}",paymentDataList.size(),paymentDataList);
       paymentDataList.stream().forEach(s -> {
           try {
               PaymentData paymentData = mapper.readValue((String) s[1],PaymentData.class);
              // paymentData.setPaymentid(String.valueOf(s[0]));
               int updateCount = paymentDataRepository.update(paymentData);
               log.info("{} row updated for payment id {}",updateCount,paymentData.paymentid);
               Message<PaymentData> message = MessageBuilder
                       .withPayload(paymentData)
                       .setHeader(KafkaHeaders.TOPIC, "payment_topic")
                       .build();
               kafkaProducer.sendMessage(message);
           } catch (JsonProcessingException e) {
               e.printStackTrace();
           }
       });


        log.info("[B] Done the cycle of scheduled task");
    }

    @KafkaListener(topics = "payment_topic",containerFactory = "kafkaListenerContainerFactory")
    public void paymentConsumer(@Payload PaymentData paymentData) {
        log.info("Payment Consumer [String] received key {}: | Record: {}", paymentData);
        int i =paymentDataRepository.findById(paymentData.getPaymentid()).isPresent() ?
                paymentDataRepository.update(paymentData) : paymentDataRepository.save(paymentData);
        Optional<ResponseEntity<PaymentData>> optionalPaymentDataResponseEntity =  new CallPayment(Integer.MAX_VALUE,this.restTemplate,this.kafkaProducer).getPaymentDataResponseEntity(paymentData);
        int updateP = -1;
        if(optionalPaymentDataResponseEntity.isPresent()){
           updateP = paymentDataRepository.updateProcessed(optionalPaymentDataResponseEntity.get().getBody());
        }
        log.info("Done {} UpdateP {}",i,updateP);
    }

    public class CallPayment implements Runnable{
        private RestTemplate restTemplate;
        private KafkaProducer kafkaProducer;
        private int i;

        public CallPayment(int i,RestTemplate restTemplate, KafkaProducer kafkaProducer) {
            this.i = i;
            this.kafkaProducer = kafkaProducer;
            this.restTemplate = restTemplate;
        }

        @Override
        public void run() {
            log.info("Processing payment number " + i);
            PaymentData paymentData = paymentDataGenerator();
            Optional<ResponseEntity<PaymentData>> responseEntity = getPaymentDataResponseEntity(paymentData);

            if(responseEntity.isPresent()){
                log.info("Response [{}] ", responseEntity.get().getBody().toString());
            }            // Simulate an operation that took 5 seconds.
            long startTime = System.currentTimeMillis();
        }

        private Optional<ResponseEntity<PaymentData>> getPaymentDataResponseEntity(PaymentData paymentData) {
            ResponseEntity<PaymentData> responseEntity = null;
            try {
                responseEntity = restTemplate.postForEntity("http://localhost:8080/init/", paymentData, PaymentData.class);
            } catch (Exception ex) {
                log.error("Exception [{}]", ex.getMessage());
                Message<PaymentData> message = MessageBuilder
                        .withPayload(paymentData)
                        .setHeader(KafkaHeaders.TOPIC, "payment_topic")
                        .build();
                kafkaProducer.sendMessage(message);
            }
            return Optional.ofNullable(responseEntity);
        }
    }

}


