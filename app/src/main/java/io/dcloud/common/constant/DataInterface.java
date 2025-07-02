package io.dcloud.common.constant;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.TelephonyUtil;

/* loaded from: classes.dex */
public class DataInterface {
    public static String getBaseUrl() {
        return StringConst.STREAMAPP_KEY_BASESERVICEURL();
    }

    public static String getUrlBaseData(Context context, String str, String str2, String str3) {
        String str4;
        String imei = TelephonyUtil.getIMEI(context);
        int networkType = NetworkTypeUtil.getNetworkType(context);
        try {
            str4 = URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str4 = null;
        }
        return String.format(StringConst.T_URL_BASE_DATA, str, imei, Integer.valueOf(networkType), str4, Integer.valueOf(Build.VERSION.SDK_INT), BaseInfo.sBaseVersion, Integer.valueOf(StringConst.getIntSF(str2)), Long.valueOf(System.currentTimeMillis()), str3, Build.MANUFACTURER) + getTestParam(str) + "&mc=" + BaseInfo.sChannel;
    }

    public static String getTestParam(String str) {
        return "&__am=" + ((TextUtils.isEmpty(str) || !BaseInfo.isTest(str)) ? "r" : "t");
    }

    public static String getStatisticsUrl(String str, String str2, int i, String str3, String str4) {
        return String.format(StringConst.STREAMAPP_KEY_STATISTICURL, str, str2, Integer.valueOf(i), str3, str4, Integer.valueOf(Build.VERSION.SDK_INT), Build.MANUFACTURER) + "&p=a" + getTestParam(str) + "&mc=" + BaseInfo.sChannel;
    }

    public static String getIconImageUrl(String str, String str2) {
        return StringConst.STREAMAPP_KEY_BASESERVICEURL() + AbsoluteConst.STREAMAPP_KEY_ICONBASURL + str + "&width=" + str2 + getTestParam(str) + (DeviceInfo.sApplicationContext == null ? "" : AbsoluteConst.STREAMAPP_KEY_IMEI + TelephonyUtil.getIMEI(DeviceInfo.sApplicationContext));
    }

    public static String getWGTUrl(Context context, String str, String str2, String str3) {
        return StringConst.STREAMAPP_KEY_BASESERVICEURL() + AbsoluteConst.STREAMAPP_KEY_WGTBASEURL + getUrlBaseData(context, str, str2, str3);
    }

    public static String getCheckUpdateUrl(String str, String str2, String str3) {
        return StringConst.STREAMAPP_KEY_BASESERVICEURL() + AbsoluteConst.STREAMAPP_KEY_UPDATAURL + str + AbsoluteConst.STREAMAPP_KEY_UPDATAVERSION + str2 + AbsoluteConst.STREAMAPP_KEY_UPDATATYPE + AbsoluteConst.STREAMAPP_KEY_IMEI + str3 + getTestParam(str);
    }

    public static String getDatailUrl(String str) {
        return StringConst.STREAMAPP_KEY_BASESERVICEURL() + AbsoluteConst.STREAMAPP_KEY_DETAIL + str + getTestParam(str);
    }

    public static String getSplashUrl(String str, String str2, String str3) {
        return StringConst.STREAMAPP_KEY_BASESERVICEURL() + AbsoluteConst.STREAMAPP_KEY_SPLASHBASEURL + str + "&width=" + str2 + "&height=" + str3 + getTestParam(str);
    }

    public static String getAppStreamUpdateUrl(Context context, String str, String str2, String str3, String str4, String str5) {
        StringBuilder sb = new StringBuilder();
        sb.append(str).append("check/update?").append(getUrlBaseData(context, str2, str4, str5)).append(AbsoluteConst.STREAMAPP_KEY_UPDATAVERSION).append(str3).append("&plus_version=").append(BaseInfo.sBaseVersion).append("&width=").append(context.getResources().getDisplayMetrics().widthPixels).append("&height=").append(context.getResources().getDisplayMetrics().heightPixels);
        return sb.toString();
    }

    public static String getJsonUrl(String str, String str2, String str3, Context context, String str4) {
        return str + "resource/stream?" + getUrlBaseData(context, str2, str3, str4);
    }

    public static String getAppListUrl(String str) {
        return StringConst.STREAMAPP_KEY_BASESERVICEURL() + AbsoluteConst.STREAMAPP_KEY_APPLISTURL + getTestParam(str);
    }

    public static String getRomVersion() {
        if (Build.MANUFACTURER.equals(MobilePhoneModel.XIAOMI)) {
            String buildValue = DeviceInfo.getBuildValue("ro.miui.ui.version.name");
            return Build.VERSION.INCREMENTAL + (TextUtils.isEmpty(buildValue) ? "" : "-" + buildValue);
        }
        return Build.VERSION.INCREMENTAL;
    }

    public static String getStreamappFrom(Intent intent) {
        if (intent == null) {
            return null;
        }
        if (intent.hasExtra(IntentConst.RUNING_STREAPP_LAUNCHER)) {
            String stringExtra = intent.getStringExtra(IntentConst.RUNING_STREAPP_LAUNCHER);
            if (stringExtra.indexOf("third:") == 0) {
                return stringExtra.substring(6, stringExtra.length());
            }
            return null;
        }
        if (intent.hasExtra(IntentConst.QIHOO_START_PARAM_FROM)) {
            return intent.getStringExtra(IntentConst.QIHOO_START_PARAM_FROM);
        }
        return null;
    }
}
