package com.focuslearning.example.kafka.controller;

import com.focuslearning.example.kafka.data.KeyValuePair;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/static/")
public class StaticDataController {

    @GetMapping("{key}")
    public ResponseEntity<Object> getValue(@PathVariable(required = true) String key){


        return ResponseEntity.ok("");
    }

    @PostMapping("save")
    public ResponseEntity<String> addValue(@RequestBody(required = true) KeyValuePair obj){
        var stringSerde = Serdes.String();
        var keyValueSerde = new JsonSerde<>(KeyValuePair.class);
        StreamsBuilder builder = new StreamsBuilder();
        var staticDataStream = builder.stream("t-static-data", Consumed.with(stringSerde, keyValueSerde))
                .selectKey((k, v) -> v.key());

//        var staticDataTable = builder.globalTable("staticDataTopic", Consumed.with(stringSerde,keyValueSerde));
//        var offerStream = staticDataStream.join(staticDataTable, (key, value) -> key, this::joiner);
        return ResponseEntity.ok("Added");
    }

    @PutMapping("update")
    public ResponseEntity<String> updateValue(@RequestBody(required = true) KeyValuePair obj){

        return ResponseEntity.ok("Updated");
    }



}
