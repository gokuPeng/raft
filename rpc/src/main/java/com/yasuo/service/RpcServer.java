package com.yasuo.service;

import com.yasuo.common.codec.RpcDecoder;
import com.yasuo.common.codec.RpcEncoder;
import com.yasuo.common.protocol.RpcRequest;
import com.yasuo.common.protocol.RpcResponse;
import com.yasuo.common.serializer.protostuff.ProtoStuffSerializer;
import com.yasuo.util.ThreadPoolUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author goku peng
 * @since 2023/2/12 23:19
 */
@Slf4j
public class RpcServer {
    private Thread thread;
    private Map<String, Object> serviceMap = new HashMap<>();
    int port;

    public RpcServer(int port) {
        this.port = port;
    }

    public void addService(String interfaceName, Object serviceBean) {
        log.info("Adding service, interface: {}, bean：{}", interfaceName, serviceBean);
        serviceMap.put(interfaceName, serviceBean);
    }

    public void start() {
        thread = new Thread(new Runnable() {
            ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.createThreadPool(
                    RpcServer.class.getSimpleName(), 16, 32);

            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel channel) throws Exception {
                                    ChannelPipeline cp = channel.pipeline();
                                    ProtoStuffSerializer serializer = ProtoStuffSerializer.class.newInstance();
                                    cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                                    cp.addLast(new RpcDecoder(RpcRequest.class, serializer));
                                    cp.addLast(new RpcEncoder(RpcResponse.class, serializer));
                                    cp.addLast(new RpcServerHandler(serviceMap, threadPoolExecutor));
                                }
                            })
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);

                    ChannelFuture future = bootstrap.bind(port).sync();

                    log.info("Server started on port {}", port);
                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        log.info("Rpc server remoting server stop");
                    } else {
                        log.error("Rpc server remoting server error", e);
                    }
                } finally {
                    try {
                        workerGroup.shutdownGracefully();
                        bossGroup.shutdownGracefully();
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }
            }
        });
        thread.start();
    }

    public void stop() throws Exception {
        // destroy server thread
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}
