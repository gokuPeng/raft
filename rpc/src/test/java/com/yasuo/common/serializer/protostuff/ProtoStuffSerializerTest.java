package com.yasuo.common.serializer.protostuff;

import com.yasuo.common.serializer.Serializer;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author goku peng
 * @since 2023/2/14 10:54
 */
@Data
public class ProtoStuffSerializerTest {
    
    private int num = 1;
    private String str = "hello world";

    @Test
    public void testProtoStufferSerializer() {
        Serializer serializer = new ProtoStuffSerializer();
        ProtoStuffSerializerTest obj = new ProtoStuffSerializerTest();
        byte[] bytes = serializer.serialize(obj);
        ProtoStuffSerializerTest newObj = serializer.deserialize(bytes, ProtoStuffSerializerTest.class);
        Assert.assertEquals(obj, newObj);
    }
}
