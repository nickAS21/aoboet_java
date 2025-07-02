package io.dcloud.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class LauncherUtil {
    public static String getLauncherPackageName(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        return (resolveActivity == null || resolveActivity.activityInfo == null || resolveActivity.activityInfo.packageName.equals("android")) ? "" : resolveActivity.activityInfo.packageName;
    }

    public static String getAuthorityFromPermissionDefault(Context context) {
        String authorityFromPermission = getAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
        if (!TextUtils.isEmpty(authorityFromPermission)) {
            return authorityFromPermission;
        }
        if (Build.VERSION.SDK_INT < 19) {
            return getAuthorityFromPermission(context, "com.android.launcher2.permission.READ_SETTINGS");
        }
        return getAuthorityFromPermission(context, "com.android.launcher3.permission.READ_SETTINGS");
    }

    public static String getAuthorityFromPermission(Context context, String str) {
        List<PackageInfo> installedPackages = null;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            installedPackages = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (installedPackages == null) {
            return "";
        }
        Iterator<PackageInfo> it = installedPackages.iterator();
        while (it.hasNext()) {
            ProviderInfo[] providerInfoArr = it.next().providers;
            if (providerInfoArr != null) {
                for (ProviderInfo providerInfo : providerInfoArr) {
                    if ((str.equals(providerInfo.readPermission) || str.equals(providerInfo.writePermission)) && Pattern.matches("(.)*launcher(.)?\\.settings", providerInfo.authority)) {
                        return providerInfo.authority;
                    }
                }
            }
        }
        return "";
    }
}
