package com.nostra13.dcloudimageloader.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public final class StorageUtils {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String INDIVIDUAL_DIR_NAME = "uil-images";

    private StorageUtils() {
    }

    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    public static File getCacheDirectory(Context context, boolean z) {
        File externalCacheDir = (z && "mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) ? getExternalCacheDir(context) : null;
        if (externalCacheDir == null) {
            externalCacheDir = context.getCacheDir();
        }
        if (externalCacheDir != null) {
            return externalCacheDir;
        }
        String str = "/data/data/" + context.getPackageName() + "/cache/";
        L.w("Can't define system cache directory! '%s' will be used.", str);
        return new File(str);
    }

    public static File getIndividualCacheDirectory(Context context) {
        File cacheDirectory = getCacheDirectory(context);
        File file = new File(cacheDirectory, INDIVIDUAL_DIR_NAME);
        return (file.exists() || file.mkdir()) ? file : cacheDirectory;
    }

    public static File getOwnCacheDirectory(Context context, String str) {
        File file = ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) ? new File(Environment.getExternalStorageDirectory(), str) : null;
        return (file == null || !(file.exists() || file.mkdirs())) ? context.getCacheDir() : file;
    }

    private static File getExternalCacheDir(Context context) {
        File file = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), AbsoluteConst.JSON_KEY_DATA), context.getPackageName()), IApp.ConfigProperty.CONFIG_CACHE);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                L.w("Unable to create external cache directory", new Object[0]);
                return null;
            }
            try {
                new File(file, ".nomedia").createNewFile();
            } catch (IOException unused) {
                L.i("Can't create \".nomedia\" file in application external cache directory", new Object[0]);
            }
        }
        return file;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        return context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }
}
