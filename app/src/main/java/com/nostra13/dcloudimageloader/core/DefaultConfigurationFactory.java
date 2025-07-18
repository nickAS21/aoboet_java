package com.nostra13.dcloudimageloader.core;

import android.content.Context;
import android.os.Build;

import com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware;
import com.nostra13.dcloudimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.dcloudimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.dcloudimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.dcloudimageloader.core.assist.QueueProcessingType;
import com.nostra13.dcloudimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.nostra13.dcloudimageloader.core.decode.BaseImageDecoder;
import com.nostra13.dcloudimageloader.core.decode.ImageDecoder;
import com.nostra13.dcloudimageloader.core.display.BitmapDisplayer;
import com.nostra13.dcloudimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import com.nostra13.dcloudimageloader.utils.StorageUtils;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class DefaultConfigurationFactory {
    public static Executor createExecutor(int i, int i2, QueueProcessingType queueProcessingType) {
        return new ThreadPoolExecutor(i, i, 0L, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>) (queueProcessingType == QueueProcessingType.LIFO ? new LIFOLinkedBlockingDeque() : new LinkedBlockingQueue()), createThreadFactory(i2));
    }

    public static FileNameGenerator createFileNameGenerator() {
        return new HashCodeFileNameGenerator();
    }

    public static DiscCacheAware createDiscCache(Context context, FileNameGenerator fileNameGenerator, int i, int i2) {
        if (i > 0) {
            return new TotalSizeLimitedDiscCache(StorageUtils.getIndividualCacheDirectory(context), fileNameGenerator, i);
        }
        if (i2 > 0) {
            return new FileCountLimitedDiscCache(StorageUtils.getIndividualCacheDirectory(context), fileNameGenerator, i2);
        }
        return new UnlimitedDiscCache(StorageUtils.getCacheDirectory(context), fileNameGenerator);
    }

    public static DiscCacheAware createReserveDiscCache(File file) {
        File file2 = new File(file, "uil-images");
        if (file2.exists() || file2.mkdir()) {
            file = file2;
        }
        return new TotalSizeLimitedDiscCache(file, 2097152);
    }

    public static MemoryCacheAware createMemoryCache(int i) {
        MemoryCacheAware lRULimitedMemoryCache;
        if (i == 0) {
            i = (int) (Runtime.getRuntime().maxMemory() / 8);
        }
        if (Build.VERSION.SDK_INT >= 9) {
            lRULimitedMemoryCache = new LruMemoryCache(i);
        } else {
            lRULimitedMemoryCache = new LRULimitedMemoryCache(i);
        }
        return lRULimitedMemoryCache;
    }

    public static ImageDownloader createImageDownloader(Context context) {
        return new BaseImageDownloader(context);
    }

    public static ImageDecoder createImageDecoder(boolean z) {
        return new BaseImageDecoder(z);
    }

    public static BitmapDisplayer createBitmapDisplayer() {
        return new SimpleBitmapDisplayer();
    }

    private static ThreadFactory createThreadFactory(int i) {
        return new DefaultThreadFactory(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final int threadPriority;

        DefaultThreadFactory(int i) {
            this.threadPriority = i;
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "uil-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.group, runnable, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            thread.setPriority(this.threadPriority);
            return thread;
        }
    }
}
