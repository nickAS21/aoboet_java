package io.dcloud.common.constant;

import android.text.TextUtils;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.AndroidResources;

/* loaded from: classes.dex */
public final class StringConst extends AndroidResources implements AbsoluteConst {
    static String T_URL_BASE_DATA = "appid=%s&imei=%s&net=%d&md=%s&os=%d&vb=%s&sf=%d&p=a&d1=%d&sfd=%s&vd=%s";
    private static long sChangeTime;

    private static String backupHost() {
        return "http://stream.mobihtml5.com/";
    }

    private static String mainHost() {
        return "http://stream.dcloud.net.cn/";
    }

    public static int getIntSF(String str) {
        if (TextUtils.isEmpty(str)) {
            return 1;
        }
        if ("barcode".equals(str)) {
            return 2;
        }
        if ("scheme".equals(str)) {
            return 3;
        }
        if (IApp.ConfigProperty.CONFIG_STREAM.equals(str)) {
            return 6;
        }
        if (IApp.ConfigProperty.CONFIG_SHORTCUT.equals(str)) {
            return 5;
        }
        if ("push".equals(str)) {
            return 4;
        }
        if ("myapp".equals(str)) {
            return 7;
        }
        return str.indexOf("third:") == 0 ? 9 : 1;
    }

    public static String STREAMAPP_KEY_BASESERVICEURL() {
        return mainHost();
    }

    public static String changeHost(String str) {
        return str.replace(mainHost(), backupHost());
    }

    public static boolean canChangeHost(String str) {
        if (str.contains(backupHost())) {
            return false;
        }
        return str.contains(mainHost());
    }
}
