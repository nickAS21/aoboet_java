package com.nostra13.dcloudimageloader.cache.disc;

import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public abstract class LimitedDiscCache extends BaseDiscCache {
    private static final int INVALID_SIZE = -1;
    private final AtomicInteger cacheSize;
    private final Map<File, Long> lastUsageDates;
    private final int sizeLimit;

    protected abstract int getSize(File file);

    public LimitedDiscCache(File file, int i) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator(), i);
    }

    public LimitedDiscCache(File file, FileNameGenerator fileNameGenerator, int i) {
        super(file, fileNameGenerator);
        this.lastUsageDates = Collections.synchronizedMap(new HashMap());
        this.sizeLimit = i;
        this.cacheSize = new AtomicInteger();
        calculateCacheSizeAndFillUsageMap();
    }

    private void calculateCacheSizeAndFillUsageMap() {
        new Thread(new Runnable() { // from class: com.nostra13.dcloudimageloader.cache.disc.LimitedDiscCache.1
            @Override // java.lang.Runnable
            public void run() {
                File[] listFiles = LimitedDiscCache.this.cacheDir.listFiles();
                if (listFiles != null) {
                    int i = 0;
                    for (File file : listFiles) {
                        i += LimitedDiscCache.this.getSize(file);
                        LimitedDiscCache.this.lastUsageDates.put(file, Long.valueOf(file.lastModified()));
                    }
                    LimitedDiscCache.this.cacheSize.set(i);
                }
            }
        }).start();
    }

    @Override // com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware
    public void put(String str, File file) {
        int removeNext;
        int size = getSize(file);
        int i = this.cacheSize.get();
        while (i + size > this.sizeLimit && (removeNext = removeNext()) != -1) {
            i = this.cacheSize.addAndGet(-removeNext);
        }
        this.cacheSize.addAndGet(size);
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        file.setLastModified(valueOf.longValue());
        this.lastUsageDates.put(file, valueOf);
    }

    @Override // com.nostra13.dcloudimageloader.cache.disc.BaseDiscCache, com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware
    public File get(String str) {
        File file = super.get(str);
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        file.setLastModified(valueOf.longValue());
        this.lastUsageDates.put(file, valueOf);
        return file;
    }

    @Override // com.nostra13.dcloudimageloader.cache.disc.BaseDiscCache, com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware
    public void clear() {
        this.lastUsageDates.clear();
        this.cacheSize.set(0);
        super.clear();
    }

    private int removeNext() {
        File file;
        if (this.lastUsageDates.isEmpty()) {
            return -1;
        }
        Set<Map.Entry<File, Long>> entrySet = this.lastUsageDates.entrySet();
        synchronized (this.lastUsageDates) {
            file = null;
            Long l = null;
            for (Map.Entry<File, Long> entry : entrySet) {
                if (file == null) {
                    file = entry.getKey();
                    l = entry.getValue();
                } else {
                    Long value = entry.getValue();
                    if (value.longValue() < l.longValue()) {
                        file = entry.getKey();
                        l = value;
                    }
                }
            }
        }
        int i = 0;
        if (file != null) {
            if (file.exists()) {
                i = getSize(file);
                if (file.delete()) {
                    this.lastUsageDates.remove(file);
                }
            } else {
                this.lastUsageDates.remove(file);
            }
        }
        return i;
    }
}
