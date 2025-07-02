package com.nostra13.dcloudimageloader.cache.disc.impl;

import com.nostra13.dcloudimageloader.cache.disc.LimitedDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;

import java.io.File;

/* loaded from: classes.dex */
public class FileCountLimitedDiscCache extends LimitedDiscCache {
    @Override // com.nostra13.dcloudimageloader.cache.disc.LimitedDiscCache
    protected int getSize(File file) {
        return 1;
    }

    public FileCountLimitedDiscCache(File file, int i) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator(), i);
    }

    public FileCountLimitedDiscCache(File file, FileNameGenerator fileNameGenerator, int i) {
        super(file, fileNameGenerator, i);
    }
}
