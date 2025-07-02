package com.nostra13.dcloudimageloader.cache.memory.impl;

import android.graphics.Bitmap;

import com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class UsingFreqLimitedMemoryCache extends LimitedMemoryCache {
    private final Map<Bitmap, Integer> usingCounts;

    public UsingFreqLimitedMemoryCache(int i) {
        super(i);
        this.usingCounts = Collections.synchronizedMap(new HashMap());
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache, com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache, com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public boolean put(String str, Bitmap bitmap) {
        if (!super.put(str, bitmap)) {
            return false;
        }
        this.usingCounts.put(bitmap, 0);
        return true;
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache, com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public Bitmap get(String str) {
        Integer num;
        Bitmap bitmap = super.get(str);
        if (bitmap != null && (num = this.usingCounts.get(bitmap)) != null) {
            this.usingCounts.put(bitmap, Integer.valueOf(num.intValue() + 1));
        }
        return bitmap;
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache, com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache, com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public Bitmap remove(String str) {
        Bitmap bitmap = super.get(str);
        if (bitmap != null) {
            this.usingCounts.remove(bitmap);
        }
        return super.remove(str);
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache, com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache, com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public void clear() {
        this.usingCounts.clear();
        super.clear();
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache
    protected int getSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache
    protected Bitmap removeNext() {
        Bitmap bitmap;
        Set<Map.Entry<Bitmap, Integer>> entrySet = this.usingCounts.entrySet();
        synchronized (this.usingCounts) {
            bitmap = null;
            Integer num = null;
            for (Map.Entry<Bitmap, Integer> entry : entrySet) {
                if (bitmap == null) {
                    bitmap = entry.getKey();
                    num = entry.getValue();
                } else {
                    Integer value = entry.getValue();
                    if (value.intValue() < num.intValue()) {
                        bitmap = entry.getKey();
                        num = value;
                    }
                }
            }
        }
        this.usingCounts.remove(bitmap);
        return bitmap;
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache
    protected Reference<Bitmap> createReference(Bitmap bitmap) {
        return new WeakReference(bitmap);
    }
}
