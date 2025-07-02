package com.nostra13.dcloudimageloader.cache.memory.impl;

import android.graphics.Bitmap;

import com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class FIFOLimitedMemoryCache extends LimitedMemoryCache {
    private final List<Bitmap> queue;

    public FIFOLimitedMemoryCache(int i) {
        super(i);
        this.queue = Collections.synchronizedList(new LinkedList());
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache, com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache, com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public boolean put(String str, Bitmap bitmap) {
        if (!super.put(str, bitmap)) {
            return false;
        }
        this.queue.add(bitmap);
        return true;
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache, com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache, com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public Bitmap remove(String str) {
        Bitmap bitmap = super.get(str);
        if (bitmap != null) {
            this.queue.remove(bitmap);
        }
        return super.remove(str);
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache, com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache, com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public void clear() {
        this.queue.clear();
        super.clear();
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache
    protected int getSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.LimitedMemoryCache
    protected Bitmap removeNext() {
        return this.queue.remove(0);
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.BaseMemoryCache
    protected Reference<Bitmap> createReference(Bitmap bitmap) {
        return new WeakReference(bitmap);
    }
}
