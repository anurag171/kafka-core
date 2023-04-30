package com.focuslearning.example.kafka.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromotionUpperCaseSteam {

    @Bean
    public KStream<String,String> kStreamPromotionUpperCaseStream(StreamsBuilder streamsBuilder){
        KStream<String,String> sourceStream = streamsBuilder
                            .stream("t-commodity-promotion", Consumed.with(Serdes.String(),Serdes.String()));
        KStream<String,String> upperCaseStream = sourceStream.mapValues(value -> value.toUpperCase());
        upperCaseStream.to("t-commodity-promotion-uppercase");
        sourceStream.print(Printed.<String,String>toSysOut().withLabel("Source Stream"));
        upperCaseStream.print(Printed.<String,String>toSysOut().withLabel("Upper Case Stream"));
        return sourceStream;
    }
}
