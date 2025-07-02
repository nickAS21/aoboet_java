package io.dcloud.common.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class ThreadPool {
    private static final int MAX_COUNT = 3;
    ThreadPoolExecutor threadPool;

    private ThreadPool() {
        this.threadPool = null;
        this.threadPool = new ThreadPoolExecutor(3, 3, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    public static ThreadPool self() {
        return ThreadPoolHolder.mInstance;
    }

    public synchronized void addThreadTask(Runnable runnable) {
        this.threadPool.execute(runnable);
    }

    /* loaded from: classes.dex */
    static class ThreadPoolHolder {
        static ThreadPool mInstance = new ThreadPool();

        ThreadPoolHolder() {
        }
    }
}
