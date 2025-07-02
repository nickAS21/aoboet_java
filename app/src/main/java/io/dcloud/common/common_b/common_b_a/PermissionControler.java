package io.dcloud.common.common_b.common_b_a;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.AppPermissionUtil;
import io.dcloud.common.util.ShortCutUtil;

/* compiled from: PermissionControler.java */
/* loaded from: classes.dex  old a*/
public final class PermissionControler {
    static HashMap<String, ArrayList<String>> a = new HashMap<>(2);
    static ArrayList<String> b = new ArrayList<>();
    static HashMap<String, String> c;

    public static String a(int i) {
        return i != -1 ? i != 0 ? "unknown" : "authorized" : "undetermined";
    }

    static {
        HashMap<String, String> hashMap = new HashMap<>(2);
        c = hashMap;
        hashMap.put("invocation", "应用未添加invocation模块，无法使用Native.js相关API，请在manifest.json文件中permissions下添加Invocation节点。");
    }

    public static void a(String str, ArrayList<String> arrayList) {
        a.remove(str);
        a.put(str, arrayList);
    }

    public static void a(String str) {
        b.add(str);
    }

    public static void b(String str) {
        b.remove(str);
    }

    public static boolean a(String str, String str2) {
        ArrayList<String> arrayList = a.get(str);
        return b.contains(str) || (arrayList != null && arrayList.contains(str2.toLowerCase()));
    }

    public static boolean b(String str, String str2) {
        return "console".equals(str2) || "events".equals(str2.toLowerCase());
    }

    public static String c(String str) {
        return c.get(str);
    }

    public static String a(IWebview iWebview, String[] strArr) {
        String str = strArr[0];
        iWebview.obtainApp().obtainAppId();
        String obtainAppName = iWebview.obtainApp().obtainAppName();
        Context context = iWebview.getContext();
        if (str.equals("SHORTCUT")) {
            if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
                return !AppPermissionUtil.isFlymeShortcutallowAllow(context, ShortCutUtil.getHeadShortCutIntent(obtainAppName)) ? "denied" : "notdeny";
            }
            if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.SMARTISAN)) {
                return MobilePhoneModel.isSmartisanLauncherPhone(context) ? "denied" : "notdeny";
            }
            if (AppPermissionUtil.getShotCutOpId() == -1) {
                return "unknown";
            }
            AppPermissionUtil.getShotCutOpId();
            if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.XIAOMI)) {
                return Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) ? !AppPermissionUtil.isEmuiShortcutallowAllow() ? "denied" : "notdeny" : "unknown";
            }
            int checkOp = AppPermissionUtil.checkOp(context);
            return checkOp != -1 ? checkOp != 0 ? checkOp != 1 ? (checkOp == 3 || checkOp == 4) ? "undetermined" : "unknown" : "denied" : "authorized" : "unsupported";
        }
        return String.valueOf(a(iWebview.obtainApp().checkSelfPermission(d(str))));
    }

    public static String d(String str) {
        if ("CAMERA".equals(str)) {
            return String.valueOf(PlatformUtil.invokeFieldValue("android.Manifest$permission", "CAMERA", null));
        }
        if ("RECORD".equals(str)) {
            return String.valueOf(PlatformUtil.invokeFieldValue("android.Manifest$permission", "RECORD_AUDIO", null));
        }
        if ("LOCATION".equals(str)) {
            return String.valueOf(PlatformUtil.invokeFieldValue("android.Manifest$permission", "ACCESS_FINE_LOCATION", null));
        }
        if ("CONTACTS".equals(str)) {
            return String.valueOf(PlatformUtil.invokeFieldValue("android.Manifest$permission", "WRITE_CONTACTS", null));
        }
        return "SHORTCUT".equals(str) ? String.valueOf(PlatformUtil.invokeFieldValue("android.Manifest$permission", "INSTALL_SHORTCUT", null)) : str;
    }
}
