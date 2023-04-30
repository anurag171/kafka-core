package com.focuslearning.example.kafka.streams;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {
    String id = UUID.randomUUID().toString(),amount,state,dateTime,from,to,senderName,receiverName;
}
