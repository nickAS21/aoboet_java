package io.dcloud.common.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.DOMException;

/* loaded from: classes.dex */
public class JSUtil extends Deprecated_JSUtil {
    public static int CLASS_NOT_FOUND_EXCEPTION = 2;
    public static final String COMMA = ",";
    public static int ERROR = 9;
    public static int ILLEGAL_ACCESS_EXCEPTION = 3;
    public static int INSTANTIATION_EXCEPTION = 4;
    public static int INVALID_ACTION = 7;
    public static int IO_EXCEPTION = 6;
    public static int JSON_EXCEPTION = 8;
    public static int MALFORMED_URL_EXCEPTION = 5;
    public static int NO_RESULT = 0;
    public static int OK = 1;
    public static final String QUOTE = "\"";

    public static String[] jsonArrayToStringArr(JSONArray jSONArray) throws JSONException {
        String[] strArr = new String[jSONArray.length()];
        for (int i = 0; i < jSONArray.length(); i++) {
            strArr[i] = jSONArray.getString(i);
        }
        return strArr;
    }

    public static String wrapJsVar(double d) {
        return wrapJsVar(String.valueOf(d), false);
    }

    public static String wrapJsVar(float f) {
        return wrapJsVar(String.valueOf(f), false);
    }

    public static String wrapJsVar(JSONArray jSONArray) {
        return wrapJsVar(jSONArray.toString(), false);
    }

    public static String wrapJsVar(JSONObject jSONObject) {
        return wrapJsVar(jSONObject.toString(), false);
    }

    public static String wrapJsVar(String str) {
        return wrapJsVar(str, true);
    }

    public static String wrapJsVar(boolean z) {
        return wrapJsVar(String.valueOf(z), false);
    }

    public static String consoleTest(String str) {
        return "console.error('" + str + "');";
    }

    public static String arrayList2JsStringArray(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer("[");
        if (arrayList != null && !arrayList.isEmpty()) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                stringBuffer.append("'").append(arrayList.get(i)).append("'");
                if (i != size - 1) {
                    stringBuffer.append(COMMA);
                }
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    public static void execCallback(IWebview iWebview, String str, String str2, int i, boolean z) {
        execCallback(iWebview, str, str2, i, false, z);
    }

    public static void execCallback(IWebview iWebview, String str, double d, int i, boolean z) {
        execCallback(iWebview, str, String.valueOf(d), i, true, z);
    }

    public static void execCallback(IWebview iWebview, String str, JSONArray jSONArray, int i, boolean z) {
        execCallback(iWebview, str, jSONArray.toString(), i, true, z);
    }

    public static void execCallback(IWebview iWebview, String str, JSONObject jSONObject, int i, boolean z) {
        execCallback(iWebview, str, jSONObject.toString(), i, true, z);
    }

    public static void execGEOCallback(IWebview iWebview, String str, String str2, int i, boolean z, boolean z2) {
        if (iWebview != null) {
            iWebview.executeScript(String.format("(function(){try{var result= {};result.status = %d;result.message = " + (z ? "%s" : "'%s'") + ";result.keepCallback = " + z2 + ";__geo__.callbackFromNative('%s', result);}catch(e){console.error(e)};})(window,navigator);", Integer.valueOf(i), str2, str));
        }
    }

    public static void broadcastWebviewEvent(IWebview iWebview, String str, String str2, String str3) {
        String format = String.format("(function(w,n){var p = ((w.__html5plus__&&w.__html5plus__.isReady)?w.__html5plus__:(n.plus&&n.plus.isReady)?n.plus:window.plus);p && p.webview && p.webview.__Webview_LoadEvent_CallBack_ && p.webview.__Webview_LoadEvent_CallBack_(%s);})(window,navigator)", String.format("{WebviewID:'%s',Event:'%s',Msg:%s}", str, str2, str3));
        if (iWebview != null) {
            iWebview.executeScript(format);
        }
    }

    public static boolean checkOperateDirErrorAndCallback(IWebview iWebview, String str, String str2) {
        if (!iWebview.obtainFrameView().obtainApp().checkPrivateDir(str2)) {
            return false;
        }
        execCallback(iWebview, str, String.format(DOMException.JSON_ERROR_INFO, 9, DOMException.MSG_OPERATE_DIR_ERROR), ERROR, true, false);
        return true;
    }

    public static String toJsResponseText(String str) {
        return !PdrUtil.isEmpty(str) ? str.replace("'", "'").replace(QUOTE, "\\\"").replace("\n", "\\n").replace("\r", "\\r") : str;
    }
}
