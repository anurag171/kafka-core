package com.focuslearning.example.kafka.crypto;

import java.util.Optional;

public interface CipherService {

    Optional<String> hash(Object object);

    Optional<String> encrypt(Object object);

    Optional<Object> decrypt(String data);
}