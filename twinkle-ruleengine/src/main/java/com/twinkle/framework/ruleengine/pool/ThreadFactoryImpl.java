package com.twinkle.framework.ruleengine.pool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryImpl implements ThreadFactory {
    private AtomicInteger counter = new AtomicInteger(0);
    private String threadNamePrefix;

    public ThreadFactoryImpl(String var1) {
        this.threadNamePrefix = var1;
    }

    public Thread newThread(Runnable _thread) {
        Thread var2 = new Thread(_thread);
        String tempThreadNamePrefix = this.threadNamePrefix;
        var2.setName(tempThreadNamePrefix + this.counter.incrementAndGet());
        var2.setDaemon(true);
        return var2;
    }
}
