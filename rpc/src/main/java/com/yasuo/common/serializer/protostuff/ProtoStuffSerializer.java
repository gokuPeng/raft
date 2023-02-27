package com.yasuo.common.serializer.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.yasuo.common.serializer.Serializer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author goku peng
 * @since 2023/2/14 10:09
 */
public class ProtoStuffSerializer implements Serializer {
    private Map<Class<?>, Schema<?>> cacheSchemaMap = new ConcurrentHashMap<>();
    private Objenesis objenesis = new ObjenesisStd(true);

    private <T> Schema<T> getSchema(Class<?> clazz) {
        return (Schema<T>) cacheSchemaMap.computeIfAbsent(clazz, RuntimeSchema::createFrom);
    }

    @Override
    public <T> byte[] serialize(T obj) {
        Class<?> clazz = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<T> schema = getSchema(clazz);
        return ProtobufIOUtil.toByteArray(obj, schema, buffer);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T message = objenesis.newInstance(clazz);
        Schema<T> schema = getSchema(clazz);
        ProtobufIOUtil.mergeFrom(bytes, message, schema);
        return message;
    }
}
