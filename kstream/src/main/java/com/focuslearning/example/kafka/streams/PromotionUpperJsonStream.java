package com.focuslearning.example.kafka.streams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.focuslearning.example.kafka.message.PromotionMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromotionUpperJsonStream {

    private ObjectMapper mapper = new ObjectMapper();

    @Bean
    public KStream<String,String> kStreamPromotionUpperCaseJsonStream(StreamsBuilder streamsBuilder){
        KStream<String,String> sourceStream = streamsBuilder
                .stream("t-commodity-promotion", Consumed.with(Serdes.String(),Serdes.String()));
        KStream<String,String> upperCaseStream = sourceStream.mapValues(this::upperCasePromotionCode);
        upperCaseStream.to("t-commodity-promotion-uppercase");
        sourceStream.print(Printed.<String,String>toSysOut().withLabel("JSON Source Stream"));
        upperCaseStream.print(Printed.<String,String>toSysOut().withLabel("JSON Upper Case Stream"));
        return sourceStream;
    }

    private String upperCasePromotionCode(String message)  {
        PromotionMessage original = null;
        try {
            original = mapper.readValue(message, PromotionMessage.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        var converted = new PromotionMessage(original.getPromotionCode().toUpperCase());
        return converted.getPromotionCode();

    }
}
