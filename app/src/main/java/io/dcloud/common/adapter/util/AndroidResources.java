package io.dcloud.common.adapter.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public abstract class AndroidResources {
    public static PackageInfo mApplicationInfo = null;
    public static Resources mResources = null;
    private static String packageName = null;
    static AssetManager sAssetMgr = null;
    public static boolean sIMEAlive = false;
    static Bundle sMetaDatas = null;
    public static boolean splashBacking = false;
    public static int versionCode;
    public static String versionName;

    public static void initAndroidResources(Context context) {
        if (mResources != null) {
            return;
        }
        mResources = context.getResources();
        DeviceInfo.sApplicationContext = context;
        sAssetMgr = context.getAssets();
        try {
            packageName = context.getApplicationInfo().packageName;
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 1);
            mApplicationInfo = packageInfo;
            versionName = packageInfo.versionName;
            versionCode = mApplicationInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void clearData() {
        CanvasHelper.clearData();
        AdaWebview.clearData();
        mResources = null;
        sAssetMgr = null;
        mApplicationInfo = null;
    }

    public static String getMetaValue(String str) {
        if (sMetaDatas == null) {
            try {
                sMetaDatas = DeviceInfo.sApplicationContext.getPackageManager().getApplicationInfo(packageName, 128).metaData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        Bundle bundle = sMetaDatas;
        if (bundle == null || PdrUtil.isEmpty(bundle.get(str))) {
            return null;
        }
        return String.valueOf(sMetaDatas.get(str));
    }

    public static void setMetaValue(String str, String str2) {
        if (sMetaDatas == null) {
            try {
                sMetaDatas = DeviceInfo.sApplicationContext.getPackageManager().getApplicationInfo(packageName, 128).metaData;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Bundle bundle = sMetaDatas;
        if (bundle != null) {
            bundle.putString(str, str2);
            System.out.println("meta data = " + sMetaDatas.get(str));
        }
    }

    public static int getIdentifier(String str, String str2) {
        Resources resources = mResources;
        if (resources != null) {
            return resources.getIdentifier(str, str2, packageName);
        }
        return 0;
    }

    public static int getIdentifierFromApk(Context context, String str, String str2) {
        try {
            return context.createPackageContext(context.getPackageName(), Context.CONTEXT_IGNORE_SECURITY).getResources().getIdentifier(str, str2, context.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getString(int i) {
        Resources resources = mResources;
        return resources != null ? resources.getString(i) : "";
    }

    public static boolean checkImmersedStatusBar(Context context) {
        ApplicationInfo applicationInfo;
        try {
            if (Build.VERSION.SDK_INT >= 19 && (applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128)) != null && applicationInfo.metaData != null) {
                return applicationInfo.metaData.getBoolean("immersed.status.bar", false);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
