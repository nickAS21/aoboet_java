package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import io.dcloud.common.adapter.ui.ReceiveJSValue;

/* loaded from: classes.dex */
public interface IWebview extends IContainerView {
    public static final String COOKIE = "Cookie";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String USER_AGENT = "User-Agent";

    void addJsInterface(String str, IJsInterface iJsInterface);

    void addJsInterface(String str, String str2);

    void addStateListener(IWebviewStateListener iWebviewStateListener);

    void appendPreloadJsFile(String str);

    boolean canGoBack();

    boolean canGoForward();

    boolean checkWhite(String str);

    void clearHistory();

    void endWebViewEvent(String str);

    void evalJS(String str);

    void evalJS(String str, ReceiveJSValue.ReceiveJSValueCallback receiveJSValueCallback);

    void executeScript(String str);

    Activity getActivity();

    Context getContext();

    int getFixBottom();

    Object getFlag();

    String getOriginalUrl();

    float getScale();

    float getScaleOfOpenerWebview();

    String getWebviewProperty(String str);

    String getWebviewUUID();

    void goBackOrForward(int i);

    void initWebviewUUID(String str);

    boolean isLoaded();

    void loadContentData(String str, String str2, String str3);

    void loadUrl(String str);

    IApp obtainApp();

    String obtainFrameId();

    IFrameView obtainFrameView();

    String obtainFullUrl();

    String obtainPageTitle();

    String obtainUrl();

    WebView obtainWebview();

    void onRootViewGlobalLayout(View view);

    void reload();

    void reload(String str);

    void reload(boolean z);

    void removeStateListener(IWebviewStateListener iWebviewStateListener);

    void setCssFile(String str, String str2);

    void setFixBottom(int i);

    void setFlag(Object obj);

    void setFrameId(String str);

    void setListenResourceLoading(JSONObject jSONObject);

    void setOriginalUrl(String str);

    void setOverrideResourceRequest(JSONArray jSONArray);

    void setOverrideUrlLoadingData(JSONObject jSONObject);

    void setPreloadJsFile(String str);

    void setScrollIndicator(String str);

    void setWebViewCacheMode(String str);

    void setWebViewEvent(String str, Object obj);

    void setWebviewProperty(String str, String str2);

    void show(Animation animation);

    void stopLoading();

    boolean unReceiveTitle();
}
