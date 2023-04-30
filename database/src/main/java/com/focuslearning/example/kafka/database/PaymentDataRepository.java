package com.focuslearning.example.kafka.database;

import com.focuslearning.example.kafka.PaymentData;

import java.util.List;
import java.util.Optional;

public interface PaymentDataRepository {

    int save(PaymentData paymentData);

    int update(PaymentData book);

    Optional<PaymentData> findById(String id);

    int deleteById(String id);

    List<PaymentData> findAll();

    List<Object[]> findAllByLimit(int n);

    List<PaymentData> findByPublished(boolean published);

    int deleteAll();

}
