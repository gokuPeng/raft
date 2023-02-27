package com.yasuo.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author goku peng
 * @since 2023/2/12 23:20
 */
public class ThreadPoolUtil {
    public static ThreadPoolExecutor createThreadPool(final String name, int corePoolSize, int maxPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "netty-rpc-" + name + "-" + r.hashCode()),
                new ThreadPoolExecutor.AbortPolicy());
    }
}

