package com.focuslearning.example.kafka.util;

import com.hazelcast.internal.serialization.InternalSerializationService;
import com.hazelcast.internal.serialization.impl.DefaultSerializationServiceBuilder;
import com.hazelcast.internal.serialization.impl.ObjectDataOutputStream;
import com.hazelcast.nio.ObjectDataInput;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

@Log4j2
public final class SerializationUtil {

    private static final InternalSerializationService SERIALIZATION_SERVICE = new DefaultSerializationServiceBuilder().build();

    private SerializationUtil() {}

    public static Optional<byte[]> serialize(Object object) {
        if (object == null) {
            return Optional.empty();
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            ObjectDataOutputStream
                odos = com.hazelcast.internal.serialization.impl.SerializationUtil.createObjectDataOutputStream(oos, SERIALIZATION_SERVICE);
            odos.writeObject(object);
            odos.flush();

            return Optional.of(baos.toByteArray());
        } catch (IOException e) {
            log.error("Failed to serialize object of type: [] " + object.getClass(), e);
            return Optional.empty();
        }
    }

    public static Optional<Object> deserialize(byte[] bytes) {
        if (bytes == null) {
            return Optional.empty();
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            ObjectDataInput
                odi = com.hazelcast.internal.serialization.impl.SerializationUtil.createObjectDataInputStream(ois, SERIALIZATION_SERVICE);

            return odi.readObject();
        } catch (IOException e) {
            log.error("Failed to deserialize object []", e);
            return Optional.empty();
        }
    }
}