package com.nostra13.dcloudimageloader.cache.disc;

import java.io.File;

/* loaded from: classes.dex */
public interface DiscCacheAware {
    void clear();

    File get(String str);

    void put(String str, File file);
}
