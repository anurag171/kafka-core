package com.focuslearning.example.kafka.controller;

import com.focuslearning.example.kafka.crypto.CipherService;
import com.focuslearning.example.kafka.data.KeyValuePair;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CachedController {

    private CipherService cipherService;

    @Autowired(required = false)
    HazelcastInstance hazelcastInstance;

    public CachedController(CipherService cipherService){
        this.cipherService = cipherService;
    }

    @GetMapping("{key}")
    public ResponseEntity<Object> getValue(@PathVariable(required = true) String key){
        String hashedKay = cipherService.hash(key).orElse("");
        String encryptedValue = String.valueOf(hazelcastInstance.getMap("my-encrypted-value-map").get(hashedKay));
       var decryptedVal = cipherService.decrypt(encryptedValue).orElse("");
       return ResponseEntity.ok(decryptedVal);
    }

    @PostMapping("save")
    public ResponseEntity<String> addValue(@RequestBody(required = true) KeyValuePair obj){
        String hashedKay = cipherService.hash(obj.key()).orElse("");
        String encryptedValue =cipherService.encrypt(obj.value()).orElse("");
        hazelcastInstance.getMap("my-encrypted-value-map").put(hashedKay,encryptedValue);
        return ResponseEntity.ok("Added");
    }
}
