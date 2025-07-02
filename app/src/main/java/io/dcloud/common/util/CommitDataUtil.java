package io.dcloud.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DataInterface;

/* loaded from: classes.dex */
public class CommitDataUtil {
    public static String getStartData(IApp iApp, SharedPreferences sharedPreferences) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append(DataInterface.getUrlBaseData(iApp.getActivity(), iApp.obtainAppId(), BaseInfo.getLaunchType(iApp.getActivity().getIntent()), null));
            stringBuffer.append("&st=" + BaseInfo.run5appEndTime);
            stringBuffer.append("&pn=" + iApp.getActivity().getPackageName());
            stringBuffer.append("&v=" + iApp.obtainAppVersionName());
            stringBuffer.append("&pv=" + AndroidResources.versionName);
            stringBuffer.append("&name=" + URLEncoder.encode(iApp.obtainAppName(), "utf-8"));
            if ((System.currentTimeMillis() - sharedPreferences.getLong(AbsoluteConst.COMMIT_APP_LIST_TIME, 0L)) / 100000 >= 26000) {
                stringBuffer.append("&apps=" + getNoSystemAppData(iApp.getActivity()));
                stringBuffer.append("&imsi=" + TelephonyUtil.getIMSI(iApp.getActivity()));
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("pn", LauncherUtil.getLauncherPackageName(iApp.getActivity()));
                stringBuffer.append("&launcher=" + jSONObject.toString());
                if (!TextUtils.isEmpty(sharedPreferences.getString(AbsoluteConst.GEO_DATA, ""))) {
                    stringBuffer.append("&pos=" + sharedPreferences.getString(AbsoluteConst.GEO_DATA, ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static String getNoSystemAppData(Context context) throws Exception {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedNonSystemApps = ApkUtils.getInstalledNonSystemApps(context, packageManager);
        JSONArray jSONArray = new JSONArray();
        for (PackageInfo packageInfo : installedNonSystemApps) {
            if (!context.getPackageName().equals(packageInfo.packageName)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", (String) packageManager.getApplicationLabel(packageInfo.applicationInfo));
                jSONObject.put("pn", packageInfo.packageName);
                jSONObject.put("ver", packageInfo.versionName);
                jSONArray.put(jSONObject);
            }
        }
        return URLEncoder.encode(jSONArray.toString(), "UTF-8");
    }
}
