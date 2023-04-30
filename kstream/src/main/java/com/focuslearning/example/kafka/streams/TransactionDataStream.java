package com.focuslearning.example.kafka.streams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
public class TransactionDataStream {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public KStream<String,TransactionData> kStreamPaymentProcessingStream(StreamsBuilder streamsBuilder){

        var transactionProcessSerde = new  JsonSerde<>(TransactionData.class);

        KStream<String,TransactionData> sourceStream = streamsBuilder
                .stream("transaction_initiation", Consumed.with(Serdes.String(),transactionProcessSerde));
        KStream<String,TransactionData> liveTransactionStreams = sourceStream.filter((key, value) ->  {
            try {
                return getParsedTransactionData(value).equals("CANCEL");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return false;
        });
        liveTransactionStreams.to("transaction_processing");
        sourceStream.print(Printed.<String,TransactionData>toSysOut().withLabel("Source Stream"));
        liveTransactionStreams.print(Printed.<String,TransactionData>toSysOut().withLabel("Upper Case Stream"));
        return liveTransactionStreams;
    }

    private String getParsedTransactionData(TransactionData value) throws JsonProcessingException {
        return value.getState();
    }

}
