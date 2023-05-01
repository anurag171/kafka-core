package com.focuslearning.example.kafka.controller;

import com.focuslearning.example.kafka.PaymentData;
import com.focuslearning.example.kafka.database.RecordStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@Log4j2
public class StubController {

    @PostMapping(AppConstant.POST_STUB_PATH)
    public ResponseEntity<PaymentData> getTransactions(@RequestBody PaymentData paymentData) throws InterruptedException {
        int x = ThreadLocalRandom.current().nextInt(0,2);
        log.info("Got the value [{}]",x);
        if(x==0) {
            log.info("Sleeping");
            Thread.sleep(30000);
        }

        //kafkaProducer.sendMessage("transaction_initiation",transactionData.getId(),transactionData.toString());
        return ResponseEntity.ok(paymentData.withStatus(RecordStatus.NEW.getAction()));
    }
}
