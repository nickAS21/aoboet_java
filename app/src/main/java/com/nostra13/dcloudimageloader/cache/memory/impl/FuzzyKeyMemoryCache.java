package com.nostra13.dcloudimageloader.cache.memory.impl;

import android.graphics.Bitmap;

import com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FuzzyKeyMemoryCache implements MemoryCacheAware {
    private final MemoryCacheAware cache;
    private final Comparator<String> keyComparator;

    public FuzzyKeyMemoryCache(MemoryCacheAware memoryCacheAware, Comparator<String> comparator) {
        this.cache = memoryCacheAware;
        this.keyComparator = comparator;
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public boolean put(String str, Bitmap bitmap) {
        synchronized (this.cache) {
            String str2 = null;
            Iterator<String> it = this.cache.keys().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String next = it.next();
                if (this.keyComparator.compare(str, next) == 0) {
                    str2 = next;
                    break;
                }
            }
            if (str2 != null) {
                this.cache.remove(str2);
            }
        }
        return this.cache.put(str, bitmap);
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public Bitmap get(String str) {
        return this.cache.get(str);
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public Bitmap remove(String str) {
        return this.cache.remove(str);
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public void clear() {
        this.cache.clear();
    }

    @Override // com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware
    public Collection<String> keys() {
        return this.cache.keys();
    }
}
