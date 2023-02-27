package com.yasuo.common.codec;

import com.yasuo.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author goku peng
 * @since 2023/2/14 20:07
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;
    private Serializer serializer;

    public RpcDecoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int len = byteBuf.readInt();
        if (byteBuf.readableBytes() < len) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        try {
            Object obj = serializer.deserialize(bytes, genericClass);
            list.add(obj);
        } catch (Exception ex) {
            log.error("Decode error: " + ex.toString());
        }
    }
}
