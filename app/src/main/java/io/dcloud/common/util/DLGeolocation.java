package io.dcloud.common.util;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.DOMException;

/* loaded from: classes.dex */
public class DLGeolocation implements IJsInterface {
    IWebview mWebview;

    public static String getGEOJS() {
        return "!function(){window.__geo__={};var a=__geo__;a.callbacks={},a.callbackId=function(o,c){var e=\"dlgeolocation\"+(new Date).valueOf();return a.callbacks[e]={s:o,e:c},e},a.callbackFromNative=function(o,c){var e=a.callbacks[o];e&&(1==c.status&&e.success?e.s&&e.s(c.message):e.e&&e.e(c.message),c.keepCallback||delete a.callbacks[o])},navigator.geolocation.getCurrentPosition=function(o,c,e){var t=o,n=c||function(){},l=e||{},i=JSON.stringify(l);_dlGeolocation.exec(\"getCurrentPosition\",a.callbackId(t,n),i)},navigator.geolocation.watchPosition=function(o,c,e){var t=o,n=c||function(){},l=e||{},i=JSON.stringify(l);i.id=\"dlwatchPosition\"+(new Date).valueOf(),_dlGeolocation.exec(\"watchPosition\",a.callbackId(t,n),i)},navigator.geolocation.clearwatch=function(a){_dlGeolocation.exec(\"clearwatch\",null,{id:a})}}();";
    }

    @Override // io.dcloud.common.DHInterface.IJsInterface
    public String exec(String str, String str2, JSONArray jSONArray) {
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IJsInterface
    public void forceStop(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IJsInterface
    public String prompt(String str, String str2) {
        return null;
    }

    public DLGeolocation(IWebview iWebview) {
        this.mWebview = iWebview;
    }

    @Override // io.dcloud.common.DHInterface.IJsInterface
    @JavascriptInterface
    public String exec(String str, String str2, String str3) {
        runGeolocation(str, str2, str3);
        return null;
    }

    public void runGeolocation(String str, String str2, String str3) {
        IWebview iWebview = this.mWebview;
        if (iWebview != null) {
            try {
                Object invokeMethod = PlatformUtil.invokeMethod("io.dcloud.js.geolocation.amap.AMapGeoManager", "getInstance", null, new Class[]{Context.class}, new Object[]{iWebview.getContext()});
                if (invokeMethod == null) {
                    invokeMethod = Class.forName("io.dcloud.js.geolocation.amap.AMapGeoManager").getConstructor(Context.class).newInstance(this.mWebview.getContext());
                }
                invokeMethod.getClass().getMethod("execute", IWebview.class, String.class, String[].class, Boolean.TYPE).invoke(invokeMethod, this.mWebview, str, getGeoArgs(str, str3, str2), true);
            } catch (Exception e) {
                e.printStackTrace();
                JSUtil.execGEOCallback(this.mWebview, str2, String.format(DOMException.JSON_ERROR_INFO, -100, "定位失败"), JSUtil.ERROR, true, false);
            }
        }
    }

    public static boolean checkAMapGeo() {
        try {
            return Class.forName("io.dcloud.js.geolocation.amap.AMapGeoManager") != null;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean checkInjectGeo(String str) {
        if (str.equals("replace")) {
            return true;
        }
        return str.equals("auto") && MobilePhoneModel.isDLGeoPhone();
    }

    public String[] getGeoArgs(String str, String str2, String str3) {
        JSONObject jSONObject = null;
        int optInt = 0;
        boolean optBoolean = false;
        int optInt2 = 0;
        boolean optBoolean2 = false;
        try {
            jSONObject = new JSONObject(str2);
            optInt = jSONObject.has("maximumAge") ? jSONObject.optInt("maximumAge") : 0;
            optBoolean = jSONObject.has("enableHighAccuracy") ? jSONObject.optBoolean("enableHighAccuracy") : false;
            optInt2 = jSONObject.has("timeout") ? jSONObject.optInt("timeout") : 0;
            optBoolean2 = jSONObject.has("geocode") ? jSONObject.optBoolean("geocode") : true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (str.equals("getCurrentPosition")) {
            return new String[]{str3, optBoolean + "", optInt + "", null, null, optBoolean2 + "", optInt2 + ""};
        }
        if (str.equals("watchPosition")) {
            return new String[]{str3, jSONObject.optString("id"), optBoolean + "", "", "", optBoolean2 + "", optInt2 + "", optInt + ""};
        }
        if (str.equals("clearwatch")) {
            return new String[]{jSONObject.optString("id")};
        }
        return null;
    }
}
