package com.focuslearning.example.kafka.controller;

import com.focuslearning.example.kafka.producer.KafkaProducer;
import com.focuslearning.example.kafka.TransactionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(AppConstant.STREAMS_PATH)
@RestController
public class StreamController {

    @Autowired
    KafkaProducer kafkaProducer;


    @PostMapping(AppConstant.INITIATION_PATH)
    public ResponseEntity<String>  initiateTransactions(@RequestBody TransactionData transactionData){

        kafkaProducer.sendMessage("transaction_initiation",transactionData.getId(),transactionData);
        return ResponseEntity.ok(transactionData.toString());
    }

    @PutMapping(AppConstant.UPDATION_PATH)
    public ResponseEntity<String>  updateTransactions(@RequestBody TransactionData transactionData){

        kafkaProducer.sendMessage("transaction_initiation",transactionData.getId(),transactionData.toString());
        return ResponseEntity.accepted().body(transactionData.toString());
    }

    @GetMapping(AppConstant.GET_PATH)
    public ResponseEntity<String>  getTransactions(@RequestParam String id){
        TransactionData transactionData = new TransactionData();

        //kafkaProducer.sendMessage("transaction_initiation",transactionData.getId(),transactionData.toString());
        return ResponseEntity.ok(transactionData.toString());
    }

    @GetMapping(AppConstant.CANCELLATION_PATH)
    public ResponseEntity<String>  cancellationTransactions(@RequestParam String id){

        //kafkaProducer.sendMessage("transaction_initiation",transactionData.getId(),transactionData.toString());
        return ResponseEntity.ok("done");
    }
}
