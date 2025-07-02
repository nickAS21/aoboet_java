package io.dcloud.common.adapter.ui;

import android.os.Build;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;

import java.util.HashMap;

import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.JSONUtil;

/* loaded from: classes.dex */
public class ReceiveJSValue implements IReflectAble {
    public static String SYNC_HANDLER = "SYNC_HANDLER";
    private static HashMap<String, ReceiveJSValueCallback> arrs = new HashMap<>();
    private String android42Js = null;

    /* loaded from: classes.dex */
    public interface ReceiveJSValueCallback {
        String callback(JSONArray jSONArray);
    }

    public static final String registerCallback(String str, ReceiveJSValueCallback receiveJSValueCallback) {
        return registerCallback(null, str, receiveJSValueCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final String registerCallback(AdaWebview adaWebview, String str, ReceiveJSValueCallback receiveJSValueCallback) {
        String valueOf = String.valueOf(receiveJSValueCallback.hashCode());
        arrs.put(valueOf, receiveJSValueCallback);
        return ((adaWebview == null || adaWebview.mWebViewImpl.mReceiveJSValue_android42 == null) ? "" : adaWebview.mWebViewImpl.mReceiveJSValue_android42.android42Js) + String.format("window.SYNC_HANDLER && " + SYNC_HANDLER + ".__js__call__native__('" + valueOf + "',(function(){var ret = %s;var type = (typeof ret );return JSON.stringify([type,ret]);})());", str);
    }

    public static final void addJavascriptInterface(AdaWebview adaWebview) {
        if (Build.VERSION.SDK_INT > 17) {
            adaWebview.mWebViewImpl.addJavascriptInterface(new ReceiveJSValue(), SYNC_HANDLER);
            return;
        }
        adaWebview.mWebViewImpl.mReceiveJSValue_android42 = new ReceiveJSValue();
        adaWebview.mWebViewImpl.mReceiveJSValue_android42.android42Js = "window.SYNC_HANDLER||(window.SYNC_HANDLER = {__js__call__native__: function(uuid, js) {return window.prompt('__js__call__native__','sync:' + JSON.stringify([uuid, js]));}});";
    }

    @JavascriptInterface
    public final String __js__call__native__(String str, String str2) {
        ReceiveJSValueCallback remove = arrs.remove(str);
        Logger.d("ReceiveJSValue", "__js__call__native__ js=" + str2);
        return remove != null ? remove.callback(JSONUtil.createJSONArray(str2)) : "";
    }
}
