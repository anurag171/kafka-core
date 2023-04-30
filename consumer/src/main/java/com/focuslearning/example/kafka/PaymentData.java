package com.focuslearning.example.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@With(AccessLevel.PUBLIC)
public class PaymentData {

    String amount;
    String paymentid;
    String state;
    String dateTime;
    String from;
    String to;
    String senderName;
    String receiverName;
    Integer retryTimes = 0;
    String message;
    String country;
    LocalDateTime createdtime;
    LocalDateTime expiretime;
}
