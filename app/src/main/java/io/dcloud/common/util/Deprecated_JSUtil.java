package io.dcloud.common.util;

import io.dcloud.common.DHInterface.IWebview;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JSUtil.java */
/* loaded from: classes.dex */
public class Deprecated_JSUtil {
    @Deprecated
    public static void execCallback(IWebview iWebview, String str, String str2, int i, boolean z, boolean z2) {
        if (iWebview != null) {
            iWebview.executeScript(String.format("(function(w,n){try{var p=((w.__html5plus__&&w.__html5plus__.isReady)?w.__html5plus__:(n.plus&&n.plus.isReady)?n.plus:window.plus);var result= {};result.status = %d;result.message = " + (z ? "%s" : "'%s'") + ";result.keepCallback = " + z2 + ";p && p.bridge.callbackFromNative('%s', result);}catch(e){console.error(e)};})(window,navigator);", Integer.valueOf(i), str2, str));
        }
    }

    @Deprecated
    public static void excCallbackSuccess(IWebview iWebview, String str, String str2) {
        excCallbackSuccess(iWebview, str, str2, false);
    }

    @Deprecated
    public static void excCallbackSuccess(IWebview iWebview, String str, String str2, boolean z) {
        excCallbackSuccess(iWebview, str, str2, z, false);
    }

    @Deprecated
    public static void excCallbackSuccess(IWebview iWebview, String str, String str2, boolean z, boolean z2) {
        execCallback(iWebview, str, str2, 1, z, z2);
    }

    @Deprecated
    public static void excCallbackError(IWebview iWebview, String str, String str2) {
        excCallbackError(iWebview, str, str2, false);
    }

    @Deprecated
    public static void excCallbackError(IWebview iWebview, String str, String str2, boolean z) {
        execCallback(iWebview, str, str2, JSUtil.ERROR, z, false);
    }

    @Deprecated
    public static void excDownloadCallBack(IWebview iWebview, String str, String str2) {
        iWebview.executeScript(String.format("((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus).downloader.__handlerEvt__('%s', %s);", str2, str));
    }

    @Deprecated
    public static void excUploadCallBack(IWebview iWebview, String str, String str2) {
        iWebview.executeScript(String.format("((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus).uploader.__handlerEvt__('%s', %s);", str2, str));
    }

    @Deprecated
    public static String wrapJsVar(String str, boolean z) {
        StringBuffer stringBuffer = new StringBuffer("(function(){return ");
        if (z) {
            stringBuffer.append("'").append(str).append("';");
        } else {
            stringBuffer.append(str).append(";");
        }
        stringBuffer.append("})()");
        return stringBuffer.toString();
    }
}
