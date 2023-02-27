package com.yasuo.common.serializer;

/**
 * @author goku peng
 * @since 2023/2/14 10:07
 */
public interface Serializer {
    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
