package io.dcloud.common.adapter.util;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

import io.dcloud.common.util.LauncherUtil;

/* loaded from: classes.dex */
public class MobilePhoneModel {
    public static String COOLPAD = "Coolpad";
    private static final ArrayList<String> CREATE_NO_TOAST;
    private static final ArrayList<String> CREATE_TOAST;
    public static String GIONEE = "GIONEE";
    public static String GOOGLE = "google";
    public static String HTC = "htc";
    public static String HUAWEI = "Huawei";
    public static String LENOVO = "Lenovo";
    public static String LETV = "Letv";
    public static String MEIZU = "Meizu";
    public static String MOTO = "motorola";
    private static final ArrayList<String> NO_CREATE;
    public static String ONEPLUS = "OnePlus";
    public static String OPPO = "OPPO";
    public static String QiKU = "QiKU";
    public static String SAMSUNG = "samsung";
    public static String SMARTISAN = "SMARTISAN";
    public static String VIVO = "vivo";
    public static String XIAOMI = "Xiaomi";
    public static String YULONG = "YuLong";
    public static String ZTE = "ZTE";

    public static boolean checkPhoneBanAcceleration(String str) {
        if (str == null || Build.VERSION.SDK_INT != 21) {
            return true;
        }
        return (str.equalsIgnoreCase(SAMSUNG) || str.equalsIgnoreCase(MOTO) || str.equalsIgnoreCase(LETV) || str.equalsIgnoreCase(LENOVO) || str.equalsIgnoreCase(HTC) || str.equalsIgnoreCase(MEIZU) || str.equalsIgnoreCase(XIAOMI)) ? false : true;
    }

    public static boolean hasToast(String str) {
        System.err.println(Build.MODEL + ";launcherName=" + str);
        return true;
    }

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        NO_CREATE = arrayList;
        arrayList.add("com.android.launcher3");
        arrayList.add("com.zte.mifavor.launcher");
        ArrayList<String> arrayList2 = new ArrayList<>();
        CREATE_TOAST = arrayList2;
        arrayList2.add("com.sec.android.app.twlauncher");
        arrayList2.add("com.oppo.launcher");
        arrayList2.add("com.tencent.qlauncher");
        arrayList2.add("com.sec.android.app.launcher");
        arrayList2.add("com.huawei.android.launcher");
        ArrayList<String> arrayList3 = new ArrayList<>();
        CREATE_NO_TOAST = arrayList3;
        arrayList3.add("com.android.launcher3");
        arrayList3.add("com.android.launcher");
        arrayList3.add("com.lenovo.launcher ");
        arrayList3.add("com.cyanogenmod.trebuchet");
        arrayList3.add("com.miui.home");
        arrayList3.add("com.htc.launcher");
    }

    public static boolean needToast(String str) {
        return CREATE_NO_TOAST.contains(str);
    }

    public static boolean isSpecialPhone(Context context) {
        String str = Build.BRAND.equals(GOOGLE) ? Build.MANUFACTURER : Build.BRAND;
        return QiKU.equalsIgnoreCase(str) || VIVO.equalsIgnoreCase(str) || isSmartisanLauncherPhone(context);
    }

    public static boolean isSmartisanLauncherPhone(Context context) {
        return SMARTISAN.equalsIgnoreCase(Build.BRAND.equalsIgnoreCase(GOOGLE) ? Build.MANUFACTURER : Build.BRAND) && LauncherUtil.getLauncherPackageName(context).contains("com.smartisanos.launcher");
    }

    public static boolean isDLGeoPhone() {
        String str = Build.BRAND.equals(GOOGLE) ? Build.MANUFACTURER : Build.BRAND;
        return (str.equalsIgnoreCase(HUAWEI) || str.equalsIgnoreCase(OPPO) || str.equalsIgnoreCase(GIONEE) || str.equalsIgnoreCase(XIAOMI) || str.equalsIgnoreCase(QiKU) || str.equalsIgnoreCase(VIVO) || str.equalsIgnoreCase(MEIZU) || str.equalsIgnoreCase(LENOVO) || str.equalsIgnoreCase(ZTE) || str.equalsIgnoreCase(COOLPAD) || str.equalsIgnoreCase(ONEPLUS)) ? false : true;
    }
}
