package com.nostra13.dcloudimageloader.core.assist;

import android.graphics.Bitmap;

import com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public final class MemoryCacheUtil {
    private static final String URI_AND_SIZE_SEPARATOR = "_";
    private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";

    private MemoryCacheUtil() {
    }

    public static String generateKey(String str, ImageSize imageSize) {
        return str + URI_AND_SIZE_SEPARATOR + imageSize.getWidth() + WIDTH_AND_HEIGHT_SEPARATOR + imageSize.getHeight();
    }

    public static Comparator<String> createFuzzyKeyComparator() {
        return new Comparator<String>() { // from class: com.nostra13.dcloudimageloader.core.assist.MemoryCacheUtil.1
            @Override // java.util.Comparator
            public int compare(String str, String str2) {
                return str.substring(0, str.lastIndexOf(MemoryCacheUtil.URI_AND_SIZE_SEPARATOR)).compareTo(str2.substring(0, str2.lastIndexOf(MemoryCacheUtil.URI_AND_SIZE_SEPARATOR)));
            }
        };
    }

    public static List<Bitmap> findCachedBitmapsForImageUri(String str, MemoryCacheAware memoryCacheAware) {
        ArrayList arrayList = new ArrayList();
        for (String str2 : memoryCacheAware.keys()) {
            if (str2.startsWith(str)) {
                arrayList.add(memoryCacheAware.get(str2));
            }
        }
        return arrayList;
    }

    public static List<String> findCacheKeysForImageUri(String str, MemoryCacheAware memoryCacheAware) {
        ArrayList arrayList = new ArrayList();
        for (String str2 : memoryCacheAware.keys()) {
            if (str2.startsWith(str)) {
                arrayList.add(str2);
            }
        }
        return arrayList;
    }

    public static void removeFromCache(String str, MemoryCacheAware memoryCacheAware) {
        for (String str2 : memoryCacheAware.keys()) {
            if (str2.startsWith(str)) {
                memoryCacheAware.remove(str2);
                return;
            }
        }
    }
}
