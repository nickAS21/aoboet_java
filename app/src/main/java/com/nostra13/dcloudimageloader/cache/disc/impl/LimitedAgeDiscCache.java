package com.nostra13.dcloudimageloader.cache.disc.impl;

import com.nostra13.dcloudimageloader.cache.disc.BaseDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class LimitedAgeDiscCache extends BaseDiscCache {
    private final Map<File, Long> loadingDates;
    private final long maxFileAge;

    public LimitedAgeDiscCache(File file, long j) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator(), j);
    }

    public LimitedAgeDiscCache(File file, FileNameGenerator fileNameGenerator, long j) {
        super(file, fileNameGenerator);
        this.loadingDates = Collections.synchronizedMap(new HashMap());
        this.maxFileAge = j * 1000;
    }

    @Override // com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware
    public void put(String str, File file) {
        long currentTimeMillis = System.currentTimeMillis();
        file.setLastModified(currentTimeMillis);
        this.loadingDates.put(file, Long.valueOf(currentTimeMillis));
    }

    @Override // com.nostra13.dcloudimageloader.cache.disc.BaseDiscCache, com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware
    public File get(String str) {
        boolean z;
        File file = super.get(str);
        if (file.exists()) {
            Long l = this.loadingDates.get(file);
            if (l == null) {
                z = false;
                l = Long.valueOf(file.lastModified());
            } else {
                z = true;
            }
            if (System.currentTimeMillis() - l.longValue() > this.maxFileAge) {
                file.delete();
                this.loadingDates.remove(file);
            } else if (!z) {
                this.loadingDates.put(file, l);
            }
        }
        return file;
    }
}
