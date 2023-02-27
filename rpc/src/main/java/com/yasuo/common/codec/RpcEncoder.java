package com.yasuo.common.codec;

import com.yasuo.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author goku peng
 * @since 2023/2/14 19:00
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;
    private Serializer serializer;

    public RpcEncoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf out) {
        if (genericClass.isInstance(in)) {
            try {
                byte[] data = serializer.serialize(in);
                out.writeInt(data.length);
                out.writeBytes(data);
            } catch (Exception ex) {
                log.error("Encode error: {}", ex.toString());
            }
        }
    }
}
