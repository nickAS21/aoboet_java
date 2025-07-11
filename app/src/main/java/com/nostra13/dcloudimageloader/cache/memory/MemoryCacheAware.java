package com.nostra13.dcloudimageloader.cache.memory;

import android.graphics.Bitmap;

import java.util.Collection;

/* loaded from: classes.dex */
public interface MemoryCacheAware {
    void clear();

    Bitmap get(String str);

    Collection<String> keys();

    boolean put(String str, Bitmap bitmap);

    Bitmap remove(String str);
}
