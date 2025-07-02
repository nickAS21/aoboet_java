package io.dcloud.feature.nativeObj;

import android.graphics.Typeface;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import io.dcloud.common.DHInterface.IApp;

/* compiled from: NativeTypefaceFactory.java */
/* loaded from: classes.dex old d */
public class NativeTypefaceFactory {
    public static HashMap<String, SoftReference<Typeface>> mapSoftReference = new HashMap<>();

    public static Typeface a(IApp iApp, String str) {
        Typeface createFromFile;
        try {
            if (mapSoftReference.containsKey(str)) {
                SoftReference<Typeface> softReference = mapSoftReference.get(str);
                if (softReference != null && softReference.get() != null) {
                    return softReference.get();
                }
                mapSoftReference.remove(str);
            }
            File file = new File(str);
            if (iApp.obtainRunningAppMode() == 1 && !file.exists()) {
                if (str.startsWith("/")) {
                    str = str.substring(1, str.length());
                }
                createFromFile = Typeface.createFromAsset(iApp.getActivity().getAssets(), str);
            } else {
                createFromFile = Typeface.createFromFile(str);
            }
            Typeface typeface = createFromFile;
            mapSoftReference.put(str, new SoftReference<>(typeface));
            return typeface;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void clearSoftReference() {
        HashMap<String, SoftReference<Typeface>> hashMap = mapSoftReference;
        if (hashMap != null) {
            hashMap.clear();
        }
    }
}
