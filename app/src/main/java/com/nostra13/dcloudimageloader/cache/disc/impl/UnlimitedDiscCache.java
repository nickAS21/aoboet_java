package com.nostra13.dcloudimageloader.cache.disc.impl;

import com.nostra13.dcloudimageloader.cache.disc.BaseDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;

import java.io.File;

/* loaded from: classes.dex */
public class UnlimitedDiscCache extends BaseDiscCache {
    @Override // com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware
    public void put(String str, File file) {
    }

    public UnlimitedDiscCache(File file) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator());
    }

    public UnlimitedDiscCache(File file, FileNameGenerator fileNameGenerator) {
        super(file, fileNameGenerator);
    }
}
