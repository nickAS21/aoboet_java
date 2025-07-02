package io.dcloud.common.util;

import android.text.TextUtils;

import io.dcloud.application.DCloudApplication;

/* loaded from: classes.dex */
public class CheckSignatureUtil {
    private static final String SIGNATURES_MD5 = "9303f6b5f17a1146f3e19d7a955942c8";

    public static boolean check(String str) {
        String signature = getSignature(str);
        if (TextUtils.isEmpty(signature)) {
            return true;
        }
        String appSignatureMd5 = ApkUtils.getAppSignatureMd5(DCloudApplication.getInstance().getApplicationContext(), DCloudApplication.getInstance().getApplicationContext().getPackageName());
        return TextUtils.isEmpty(appSignatureMd5) || appSignatureMd5.equalsIgnoreCase(signature);
    }

    public static String getSignature(String str) {
        String[] apkFileSignatureAndPackageName = ApkUtils.getApkFileSignatureAndPackageName(DCloudApplication.getInstance().getApplicationContext(), str);
        return (apkFileSignatureAndPackageName == null || apkFileSignatureAndPackageName.length <= 0) ? "" : apkFileSignatureAndPackageName[0];
    }
}
