package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public abstract class AdaWebview extends AdaContainerFrameItem implements IWebview {
    public static boolean ScreemOrientationChangedNeedLayout = false;
    public static boolean setedWebViewData = false;
    String errorPageUrl;
    public MessageHandler.IMessages executeScriptListener;
    boolean hasErrorPage;
    boolean justClearOption;
    private int mCacheMode;
    String mCssString;
    String mEncoding;
    String[] mEvalJsOptionStack;
    private int mFixBottomHeight;
    private Object mFlag;
    private String mFrameId;
    protected AdaFrameView mFrameView;
    public String mInjectGEO;
    public boolean mInjectGeoLoaded;
    String mInjectPlusLoadedUrl;
    String mInjectPlusWidthJs;
    boolean mIsAdvanceCss;
    IJsInterface mJsInterfaces;
    JSONObject mListenResourceLoadingOptions;
    boolean mLoadCompleted;
    boolean mLoaded;
    public MessageHandler.IMessages mMesssageListener;
    public boolean mNeedInjection;
    JSONArray mOverrideResourceRequestOptions;
    JSONObject mOverrideUrlLoadingDataOptions;
    String mPlusInjectTag;
    boolean mPlusLoaded;
    public String mPlusrequire;
    ArrayList<String> mPreloadJsFile;
    boolean mPreloadJsLoaded;
    boolean mPreloadJsLoading;
    public int mProgress;
    String mRecordLastUrl;
    private ArrayList<IWebviewStateListener> mStateListeners;
    private String mVideoFullscreen;
    WebViewImpl mWebViewImpl;
    AdaWebViewParent mWebViewParent;
    private String mWebviewUUID;
    private String needTouchEvent;
    String originalUrl;
    boolean unReceiveTitle;

    /* loaded from: classes.dex */
    public interface IFExecutePreloadJSContentCallBack {
        void callback(String str, String str2);
    }

    public int getCacheMode() {
        return this.mCacheMode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AdaWebview(Context context, AdaFrameView adaFrameView) {
        super(context);
        this.unReceiveTitle = true;
        this.mWebViewParent = null;
        this.mRecordLastUrl = null;
        this.mWebViewImpl = null;
        this.mFrameView = null;
        this.mLoaded = false;
        this.mLoadCompleted = false;
        this.mPreloadJsLoaded = false;
        this.mPreloadJsLoading = false;
        this.mPlusLoaded = false;
        this.mPlusInjectTag = "page_finished";
        this.mIsAdvanceCss = false;
        this.mNeedInjection = true;
        this.mEncoding = null;
        this.mWebviewUUID = null;
        this.mFrameId = null;
        this.mJsInterfaces = null;
        this.hasErrorPage = false;
        this.errorPageUrl = null;
        this.originalUrl = null;
        this.mVideoFullscreen = "auto";
        this.needTouchEvent = "";
        this.mCacheMode = -1;
        this.mPlusrequire = IApp.ConfigProperty.CONFIG_RUNMODE_NORMAL;
        this.mInjectGEO = "none";
        this.mInjectGeoLoaded = false;
        this.mFlag = null;
        this.mInjectPlusLoadedUrl = null;
        this.mEvalJsOptionStack = null;
        this.mPreloadJsFile = new ArrayList<>(2);
        this.mCssString = "";
        this.executeScriptListener = new MessageHandler.IMessages() { // from class: io.dcloud.common.adapter.ui.AdaWebview.2
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                String str = (String) obj;
                WebViewImpl webViewImpl = AdaWebview.this.mWebViewImpl;
                if (!str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
                    str = AbsoluteConst.PROTOCOL_JAVASCRIPT + str;
                }
                webViewImpl.loadUrl(str);
            }
        };
        this.mMesssageListener = new MessageHandler.IMessages() { // from class: io.dcloud.common.adapter.ui.AdaWebview.3
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                Object[] objArr = (Object[]) obj;
                AdaWebview.this.mJsInterfaces.exec(String.valueOf(objArr[0]), String.valueOf(objArr[1]), (JSONArray) objArr[2]);
            }
        };
        this.mStateListeners = null;
        this.mProgress = 0;
        this.justClearOption = false;
        this.mOverrideResourceRequestOptions = null;
        this.mOverrideUrlLoadingDataOptions = null;
        this.mListenResourceLoadingOptions = null;
        this.mFrameView = adaFrameView;
        Logger.d("AdaWebview");
        try {
            this.mWebViewImpl = new WebViewImpl(getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
            this.mWebViewImpl = new WebViewImpl(getActivity(), this);
        }
        setWebView(this.mWebViewImpl);
        setScrollIndicator("none");
        AdaWebViewParent adaWebViewParent = new AdaWebViewParent(context);
        this.mWebViewParent = adaWebViewParent;
        adaWebViewParent.fillsWithWebview(this);
        if (adaFrameView.getFrameType() == 2) {
            this.mOverrideUrlLoadingDataOptions = this.mFrameView.obtainApp().obtainThridInfo(IApp.ConfigProperty.ThridInfo.OverrideUrlJsonData);
            JSONObject obtainThridInfo = this.mFrameView.obtainApp().obtainThridInfo(IApp.ConfigProperty.ThridInfo.OverrideResourceJsonData);
            if (obtainThridInfo != null) {
                this.mOverrideResourceRequestOptions = obtainThridInfo.optJSONArray("0");
            }
            this.mNeedInjection = PdrUtil.parseBoolean(this.mFrameView.obtainApp().obtainConfigProperty("injection"), this.mNeedInjection, false);
            String obtainConfigProperty = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_LPLUSERQUIRE);
            if (!TextUtils.isEmpty(obtainConfigProperty)) {
                this.mPlusrequire = obtainConfigProperty;
            }
            String obtainConfigProperty2 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_LGEOLOCATION);
            if (TextUtils.isEmpty(obtainConfigProperty2)) {
                return;
            }
            this.mInjectGEO = obtainConfigProperty2;
            return;
        }
        if (adaFrameView.getFrameType() == 4) {
            String obtainConfigProperty3 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_SPLUSERQUIRE);
            if (!TextUtils.isEmpty(obtainConfigProperty3)) {
                this.mPlusrequire = obtainConfigProperty3;
            }
            String obtainConfigProperty4 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_SGEOLOCATION);
            if (TextUtils.isEmpty(obtainConfigProperty4)) {
                return;
            }
            this.mInjectGEO = obtainConfigProperty4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getAppName() {
        String obtainAppName = this.mFrameView.obtainApp().obtainAppName();
        return (TextUtils.isEmpty(obtainAppName) && this.mFrameView.getFrameType() == 3) ? "流应用" : obtainAppName;
    }

    protected AdaWebview(Context context, AdaFrameView adaFrameView, WebLoadEvent.OnPageFinishedCallack onPageFinishedCallack) {
        super(context);
        this.unReceiveTitle = true;
        this.mWebViewParent = null;
        this.mRecordLastUrl = null;
        this.mWebViewImpl = null;
        this.mFrameView = null;
        this.mLoaded = false;
        this.mLoadCompleted = false;
        this.mPreloadJsLoaded = false;
        this.mPreloadJsLoading = false;
        this.mPlusLoaded = false;
        this.mPlusInjectTag = "page_finished";
        this.mIsAdvanceCss = false;
        this.mNeedInjection = true;
        this.mEncoding = null;
        this.mWebviewUUID = null;
        this.mFrameId = null;
        this.mJsInterfaces = null;
        this.hasErrorPage = false;
        this.errorPageUrl = null;
        this.originalUrl = null;
        this.mVideoFullscreen = "auto";
        this.needTouchEvent = "";
        this.mCacheMode = -1;
        this.mPlusrequire = IApp.ConfigProperty.CONFIG_RUNMODE_NORMAL;
        this.mInjectGEO = "none";
        this.mInjectGeoLoaded = false;
        this.mFlag = null;
        this.mInjectPlusLoadedUrl = null;
        this.mEvalJsOptionStack = null;
        this.mPreloadJsFile = new ArrayList<>(2);
        this.mCssString = "";
        this.executeScriptListener = new MessageHandler.IMessages() { // from class: io.dcloud.common.adapter.ui.AdaWebview.2
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                String str = (String) obj;
                WebViewImpl webViewImpl = AdaWebview.this.mWebViewImpl;
                if (!str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
                    str = AbsoluteConst.PROTOCOL_JAVASCRIPT + str;
                }
                webViewImpl.loadUrl(str);
            }
        };
        this.mMesssageListener = new MessageHandler.IMessages() { // from class: io.dcloud.common.adapter.ui.AdaWebview.3
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                Object[] objArr = (Object[]) obj;
                AdaWebview.this.mJsInterfaces.exec(String.valueOf(objArr[0]), String.valueOf(objArr[1]), (JSONArray) objArr[2]);
            }
        };
        this.mStateListeners = null;
        this.mProgress = 0;
        this.justClearOption = false;
        this.mOverrideResourceRequestOptions = null;
        this.mOverrideUrlLoadingDataOptions = null;
        this.mListenResourceLoadingOptions = null;
        this.mFrameView = adaFrameView;
        Logger.d("AdaWebview");
        try {
            this.mWebViewImpl = new WebViewImpl(getActivity(), this, onPageFinishedCallack);
        } catch (Exception e) {
            e.printStackTrace();
            this.mWebViewImpl = new WebViewImpl(getActivity(), this, onPageFinishedCallack);
        }
        setWebView(this.mWebViewImpl);
        setScrollIndicator("none");
        AdaWebViewParent adaWebViewParent = new AdaWebViewParent(context);
        this.mWebViewParent = adaWebViewParent;
        adaWebViewParent.fillsWithWebview(this);
    }

    public void init() {
        this.mWebViewImpl.init();
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setFlag(Object obj) {
        this.mFlag = obj;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public Object getFlag() {
        return this.mFlag;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void loadContentData(String str, String str2, String str3) {
        this.mWebViewImpl.loadDataWithBaseURL("", str, str2, str3, "");
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setFrameId(String str) {
        this.mFrameId = str;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public String obtainFrameId() {
        return this.mFrameId;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public final void initWebviewUUID(String str) {
        this.mWebviewUUID = str;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public final String getWebviewUUID() {
        return this.mWebviewUUID;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public WebView obtainWebview() {
        return this.mWebViewImpl;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void reload() {
        if (PdrUtil.isEmpty(this.mWebViewImpl.mUrl)) {
            return;
        }
        removeAllFrameItem();
        this.mWebViewImpl.reload();
        StringBuilder append = new StringBuilder().append("reload url=");
        WebViewImpl webViewImpl = this.mWebViewImpl;
        Logger.d("webview", append.append(webViewImpl.convertRelPath(webViewImpl.mUrl)).toString());
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setFixBottom(int i) {
        this.mFixBottomHeight = i;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public int getFixBottom() {
        return this.mFixBottomHeight;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void reload(String str) {
        removeAllFrameItem();
        Logger.d("webview", "reload loadUrl url=" + str);
        this.mLoaded = false;
        this.mWebViewImpl.mUrl = str;
        this.mWebViewImpl.loadUrl(str);
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void loadUrl(String str) {
        if (PdrUtil.isEmpty(this.mWebViewImpl.mUrl) && str != null && !str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
            this.mWebViewImpl.mUrl = str;
        }
        this.mWebViewImpl.loadUrl(str);
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public String obtainUrl() {
        if (this.mWebViewImpl.mUrl == null) {
            return "";
        }
        int indexOf = this.mWebViewImpl.mUrl.indexOf(this.mWebViewImpl.mBaseUrl);
        String str = this.mWebViewImpl.mUrl;
        return indexOf >= 0 ? str.substring(this.mWebViewImpl.mBaseUrl.length()) : str;
    }

    public String syncUpdateWebViewData(String str) {
        if (Build.VERSION.SDK_INT <= 19) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        String webviewUUID = getWebviewUUID();
        if (PdrUtil.isEmpty(webviewUUID)) {
            webviewUUID = String.valueOf(this.mFrameView.hashCode());
        }
        stringBuffer.append("window.__HtMl_Id__= '" + webviewUUID + "';");
        if (PdrUtil.isEmpty(obtainFrameId())) {
            stringBuffer.append("window.__WebVieW_Id__= undefined;");
        } else {
            stringBuffer.append("window.__WebVieW_Id__= '" + obtainFrameId() + "';");
        }
        Logger.e("WebViewData", "syncUpdateWebViewData url=" + this.mRecordLastUrl);
        stringBuffer.append("try{window.plus.__tag__='" + this.mPlusInjectTag + "';location.__plusready__=true;/*console.log(location);window.plus.__url__='" + str + "';*/}catch(e){console.log(e)}");
        return AbsoluteConst.PROTOCOL_JAVASCRIPT + stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetPlusLoadSaveData() {
        Logger.e("WebViewData", "resetPlusLoadSaveData url=" + this.mWebViewImpl.getUrl());
        this.mPlusLoaded = false;
        this.mPreloadJsLoaded = false;
        this.mPreloadJsLoading = false;
        this.mInjectPlusWidthJs = null;
        this.mLoaded = false;
        this.mIsAdvanceCss = false;
        this.mInjectGeoLoaded = false;
    }

    public void saveWebViewData(String str) {
        this.mWebViewImpl.mUrl = str;
        Logger.i("WebViewData", "saveWebViewData url=" + str);
        WebViewImpl webViewImpl = this.mWebViewImpl;
        webViewImpl.mScale = webViewImpl.getScale();
        this.mPlusLoaded = true;
        this.mInjectPlusLoadedUrl = str;
        this.mPreloadJsLoaded = this.mPreloadJsLoading;
        this.mWebViewImpl.mWebLoadEvent.onUpdatePlusData(this.mWebViewImpl, str, "saveWebViewData");
        this.mWebViewImpl.mWebLoadEvent.listenPageFinishTimeout(this.mWebViewImpl, str, "saveWebViewData");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRealInject(String str) {
        return this.mPlusLoaded && TextUtils.equals(PdrUtil.getUrlPathName(str), PdrUtil.getUrlPathName(this.mInjectPlusLoadedUrl));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getScreenAndDisplayJson(IWebview iWebview) {
        float scale = iWebview.getScale();
        IApp obtainApp = iWebview.obtainApp();
        int i = obtainApp.getInt(2);
        int i2 = (int) (obtainApp.getInt(0) / scale);
        return String.format(Locale.ENGLISH, "(function(p){p.screen.scale=%f;p.screen.resolutionHeight=%d;p.screen.resolutionWidth=%d;p.screen.dpiX=%f;p.screen.dpiY=%f;p.display.resolutionHeight=%d;p.display.resolutionWidth=%d;})(((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus));", Float.valueOf(scale), Integer.valueOf((int) (i / scale)), Integer.valueOf(i2), Float.valueOf(DeviceInfo.dpiX), Float.valueOf(DeviceInfo.dpiY), Integer.valueOf((int) (obtainApp.getInt(1) / scale)), Integer.valueOf(i2));
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public String obtainFullUrl() {
        if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(this.mWebViewImpl.mUrl)) {
            return this.mWebViewImpl.mUrl;
        }
        return this.mWebViewImpl.getUrl();
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public IFrameView obtainFrameView() {
        return this.mFrameView;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void evalJS(String str) {
        if (this.mPlusLoaded) {
            executeScript(str);
        } else {
            pushEvalJsOption(str);
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void evalJS(String str, ReceiveJSValue.ReceiveJSValueCallback receiveJSValueCallback) {
        if (receiveJSValueCallback != null) {
            str = ReceiveJSValue.registerCallback(str, receiveJSValueCallback);
        }
        evalJS(str);
    }

    private void pushEvalJsOption(String str) {
        String[] strArr = this.mEvalJsOptionStack;
        if (strArr == null) {
            this.mEvalJsOptionStack = new String[1];
        } else {
            String[] strArr2 = new String[strArr.length + 1];
            this.mEvalJsOptionStack = strArr2;
            System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        }
        String[] strArr3 = this.mEvalJsOptionStack;
        strArr3[strArr3.length - 1] = str;
        Logger.d("adawebview", "webviewimp=(" + this.mWebViewImpl + ");pushEvalJs=" + str);
    }

    void execute_eval_js_stack() {
        if (this.mEvalJsOptionStack == null) {
            return;
        }
        Logger.d("adawebview", "webviewimp=" + this.mWebViewImpl + ";execute_eval_js_stack count=" + this.mEvalJsOptionStack.length);
        int i = 0;
        while (true) {
            String[] strArr = this.mEvalJsOptionStack;
            if (i < strArr.length) {
                executeScript(strArr[i]);
                i++;
            } else {
                this.mEvalJsOptionStack = null;
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String get_eval_js_stack() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.mEvalJsOptionStack != null) {
            int i = 0;
            while (true) {
                String[] strArr = this.mEvalJsOptionStack;
                if (i >= strArr.length) {
                    break;
                }
                String str = strArr[i];
                if (str.endsWith(";")) {
                    stringBuffer.append(str);
                } else {
                    stringBuffer.append(str).append(";");
                }
                i++;
            }
            this.mEvalJsOptionStack = null;
        }
        return stringBuffer.toString();
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public void dispose() {
        super.dispose();
        BaseInfo.s_Webview_Count--;
        try {
            this.mWebViewImpl.stopLoading();
        } catch (Exception unused) {
        }
        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.adapter.ui.AdaWebview.1
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                try {
                    AdaWebview.this.mWebViewImpl.clearCache(false);
                } catch (Exception unused2) {
                }
                AdaWebview.this.mWebViewImpl.setVisibility(View.GONE);
                try {
                    AdaWebview.this.mWebViewImpl.destroy();
                } catch (Exception unused3) {
                }
                AdaWebview.this.releaseConfigCallback();
                try {
                    AdaWebview.this.mWebViewImpl.destroyDrawingCache();
                    AdaWebview.this.mWebViewImpl.clearDisappearingChildren();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.gc();
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseConfigCallback() {
        try {
            if (Build.VERSION.SDK_INT < 16) {
                Field declaredField = WebView.class.getDeclaredField("mWebViewCore").getType().getDeclaredField("mBrowserFrame").getType().getDeclaredField("sConfigCallback");
                declaredField.setAccessible(true);
                declaredField.set(null, null);
            } else {
                Field declaredField2 = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (declaredField2 != null) {
                    declaredField2.setAccessible(true);
                    declaredField2.set(null, null);
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused) {
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public boolean onDispose() {
        return super.onDispose();
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void executeScript(String str) {
        if (str != null) {
            MessageHandler.sendMessage(this.executeScriptListener, str);
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void appendPreloadJsFile(String str) {
        this.mPreloadJsFile.add(str);
        Logger.d("AdaWebview", "appendPreloadJsFile mPreloadJsFile=" + this.mPreloadJsFile);
        if (this.mPlusLoaded) {
            Log.d("AdaWebview", "appendPreloadJsFile---=" + str);
            String loadPreLoadJsFile = loadPreLoadJsFile(str, this.mFrameView.obtainApp().obtainRunningAppMode() == 1 ? 0 : 2);
            if (TextUtils.isEmpty(loadPreLoadJsFile)) {
                return;
            }
            loadUrl(AbsoluteConst.PROTOCOL_JAVASCRIPT + loadPreLoadJsFile + ";");
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setPreloadJsFile(String str) {
        this.mPreloadJsFile.clear();
        this.mPreloadJsFile.add(str);
        Logger.d("AdaWebview", "setPreloadJsFile mPreloadJsFile=" + this.mPreloadJsFile);
        if (this.mPlusLoaded) {
            Log.d("AdaWebview", "setPreloadJsFile---=" + str);
            loadPreLoadJsFile(null);
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setCssFile(String str, String str2) {
        if (!PdrUtil.isEmpty(str)) {
            this.mCssString = loadPreLoadJsFile(str, this.mFrameView.obtainApp().obtainRunningAppMode() == 1 ? 0 : 2);
        } else {
            this.mCssString = str2;
        }
    }

    public String getCssString() {
        return this.mCssString;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void directLoadPreLoadJsFile() {
        if (!BaseInfo.isQihooLifeHelper(getContext()) || PdrUtil.isEmpty(this.mPreloadJsFile)) {
            return;
        }
        try {
            IApp obtainApp = this.mFrameView.obtainApp();
            if (obtainApp != null) {
                int i = obtainApp.obtainRunningAppMode() == 1 ? 0 : 2;
                Iterator<String> it = this.mPreloadJsFile.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    if (next.endsWith("www/js/cp.min.js")) {
                        String loadPreLoadJsFile = loadPreLoadJsFile(next, i);
                        if (!TextUtils.isEmpty(loadPreLoadJsFile)) {
                            loadUrl(AbsoluteConst.PROTOCOL_JAVASCRIPT + loadPreLoadJsFile + ";");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadPreLoadJsFile(IFExecutePreloadJSContentCallBack iFExecutePreloadJSContentCallBack) {
        if (PdrUtil.isEmpty(this.mPreloadJsFile)) {
            return;
        }
        try {
            IApp obtainApp = this.mFrameView.obtainApp();
            if (obtainApp != null) {
                int i = obtainApp.obtainRunningAppMode() == 1 ? 0 : 2;
                Iterator<String> it = this.mPreloadJsFile.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    String loadPreLoadJsFile = loadPreLoadJsFile(next, i);
                    if (!TextUtils.isEmpty(loadPreLoadJsFile)) {
                        String str = AbsoluteConst.PROTOCOL_JAVASCRIPT + loadPreLoadJsFile + ";";
                        if (iFExecutePreloadJSContentCallBack == null) {
                            loadUrl(str);
                        } else {
                            iFExecutePreloadJSContentCallBack.callback(this.mWebViewImpl.convertRelPath(next), str);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean loadCssFile() {
        if (PdrUtil.isEmpty(this.mCssString)) {
            return false;
        }
        String replaceAll = this.mCssString.replaceAll(JSUtil.QUOTE, "'");
        this.mCssString = replaceAll;
        loadUrl("javascript:var container = document.getElementsByTagName('head')[0];\nvar addStyle = document.createElement('style');\naddStyle.rel = 'stylesheet';\naddStyle.type = 'text/css';\naddStyle.innerHTML = " + checkRedCssline(replaceAll) + ";\ncontainer.appendChild(addStyle);\nfirstNode = container.children[0];\n    container.appendChild(addStyle);\n");
        return true;
    }

    void loadPlusApiFile() {
        loadUrl("javascript:(function(){var container = document.getElementsByTagName('head')[0];\nvar script = document.createElement('script');\nscript.type = 'text/javascript';\nscript.src = 'html5plus://ready';\ncontainer.appendChild(script);\nfirstNode = container.children[0];\nif(firstNode == null || firstNode==undefined)\n{    container.appendChild(script);}\nelse{\n\tcontainer.insertBefore(script,container.children[0]);\n}})();");
    }

    private String checkRedCssline(String str) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuffer.append(JSUtil.QUOTE + readLine + "\"\n+");
                } else {
                    return stringBuffer.substring(0, stringBuffer.length() - 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getPreLoadJsString() {
        IApp obtainApp;
        if (PdrUtil.isEmpty(this.mPreloadJsFile) || (obtainApp = this.mFrameView.obtainApp()) == null || this.mPreloadJsFile.size() <= 0) {
            return "";
        }
        int i = obtainApp.obtainRunningAppMode() == 1 ? 0 : 2;
        Iterator<String> it = this.mPreloadJsFile.iterator();
        String str = ";";
        while (it.hasNext()) {
            String next = it.next();
            if (!this.mPlusrequire.equals("none") || (!next.contains("__wap2app.js") && !next.contains("__wap2appconfig.js"))) {
                String loadPreLoadJsFile = loadPreLoadJsFile(next, i);
                if (!TextUtils.isEmpty(loadPreLoadJsFile)) {
                    str = str + loadPreLoadJsFile + "\n";
                }
            }
        }
        return str + "\n";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasPreLoadJsFile() {
        return this.mPreloadJsFile.size() > 0;
    }

    private String loadPreLoadJsFile(String str, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        byte[] fileContent = PlatformUtil.getFileContent(str, i);
        if (fileContent != null) {
            stringBuffer.append(new String(fileContent));
        }
        return stringBuffer.toString();
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.DHInterface.IContainerView
    public void addFrameItem(AdaFrameItem adaFrameItem) {
        super.addFrameItem(adaFrameItem);
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void addJsInterface(String str, String str2) {
        this.mWebViewImpl.addJavascriptInterface(str2, str);
    }

    public void addJsInterface(String str, Object obj) {
        this.mWebViewImpl.addJavascriptInterface(obj, str);
    }

    public static IJsInterface[] combineObj2Array(IJsInterface[] iJsInterfaceArr, IJsInterface iJsInterface) {
        IJsInterface[] iJsInterfaceArr2;
        if (iJsInterfaceArr == null) {
            iJsInterfaceArr2 = new IJsInterface[1];
        } else {
            int length = iJsInterfaceArr.length;
            IJsInterface[] iJsInterfaceArr3 = new IJsInterface[length + 1];
            System.arraycopy(iJsInterfaceArr, 0, iJsInterfaceArr3, 0, length);
            iJsInterfaceArr2 = iJsInterfaceArr3;
        }
        iJsInterfaceArr2[iJsInterfaceArr2.length] = iJsInterface;
        return iJsInterfaceArr2;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void addJsInterface(String str, IJsInterface iJsInterface) {
        if (Build.VERSION.SDK_INT > 17) {
            this.mWebViewImpl.addJavascriptInterface(iJsInterface, str);
        }
        setJsInterface(iJsInterface);
    }

    public void setJsInterface(IJsInterface iJsInterface) {
        if (this.mJsInterfaces == null) {
            this.mJsInterfaces = iJsInterface;
        }
    }

    public String execScript(String str, String str2, JSONArray jSONArray, boolean z) {
        if (z) {
            MessageHandler.sendMessage(this.mMesssageListener, new Object[]{str, str2, jSONArray});
            return null;
        }
        return this.mJsInterfaces.exec(str, str2, jSONArray);
    }

    public boolean backOrForward(int i) {
        return this.mWebViewImpl.canGoBackOrForward(i);
    }

    public String toString() {
        try {
            return "<UUID=" + this.mWebviewUUID + ">;" + (obtainMainView() != null ? obtainMainView().toString() : "view = null");
        } catch (Exception e) {
            e.printStackTrace();
            return super.toString();
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setWebViewEvent(String str, Object obj) {
        if (PdrUtil.isEquals(str, AbsoluteConst.PULL_DOWN_REFRESH)) {
            this.mWebViewParent.parsePullToReFresh((JSONObject) obj);
        } else if (PdrUtil.isEquals(str, AbsoluteConst.PULL_REFRESH_BEGIN)) {
            this.mWebViewParent.beginPullRefresh();
        } else if (PdrUtil.isEquals(str, AbsoluteConst.BOUNCE_REGISTER)) {
            this.mWebViewParent.parseBounce((JSONObject) obj);
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void endWebViewEvent(String str) {
        if (PdrUtil.isEquals(str, AbsoluteConst.PULL_DOWN_REFRESH)) {
            this.mWebViewParent.endPullRefresh();
        } else if (PdrUtil.isEquals(str, AbsoluteConst.BOUNCE_REGISTER)) {
            this.mWebViewParent.resetBounce();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AdaWebViewParent getWebviewParent() {
        return this.mWebViewParent;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public float getScale() {
        if (this.mWebViewImpl.mScale > 0.0f && Build.VERSION.SDK_INT >= 19) {
            return this.mWebViewImpl.mScale;
        }
        return this.mWebViewImpl.getScale();
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void addStateListener(IWebviewStateListener iWebviewStateListener) {
        if (this.mStateListeners == null) {
            this.mStateListeners = new ArrayList<>();
        }
        if (iWebviewStateListener != null) {
            this.mStateListeners.add(iWebviewStateListener);
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void removeStateListener(IWebviewStateListener iWebviewStateListener) {
        ArrayList<IWebviewStateListener> arrayList = this.mStateListeners;
        if (arrayList != null) {
            arrayList.remove(iWebviewStateListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dispatchWebviewStateEvent(int i, Object obj) {
        if (3 == i) {
            this.mProgress = Integer.parseInt(String.valueOf(obj));
        }
        ArrayList<IWebviewStateListener> arrayList = this.mStateListeners;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                this.mStateListeners.get(size).onCallBack(i, obj);
            }
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public boolean canGoBack() {
        boolean z = !this.justClearOption && this.mWebViewImpl.canGoBack();
        Logger.d("AdaFrameItem", "canGoBack" + this.mWebViewImpl.mUrl + ";" + this.justClearOption + ";" + z);
        return z;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void goBackOrForward(int i) {
        this.mWebViewImpl.goBackOrForward(i);
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public boolean canGoForward() {
        return !this.justClearOption && this.mWebViewImpl.canGoForward();
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void stopLoading() {
        this.mWebViewImpl.stopLoading();
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void clearHistory() {
        Logger.d("AdaFrameItem", "clearHistory url=" + this.mWebViewImpl.mUrl);
        this.justClearOption = true;
        this.mWebViewImpl.loadData("<html><head><meta charset=\"utf-8\"></head><body></body><html>", "text/html", "utf-8");
        this.mWebViewImpl.mUrl = "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hadClearHistory(String str) {
        return this.justClearOption && PdrUtil.isEquals(str, "data:text/html,<html><head><meta charset=\"utf-8\"></head><body></body><html>");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPageStarted() {
        WebViewImpl webViewImpl = this.mWebViewImpl;
        if (webViewImpl != null) {
            webViewImpl.onPageStarted();
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public boolean checkWhite(String str) {
        WebViewImpl webViewImpl = this.mWebViewImpl;
        if (webViewImpl != null) {
            return webViewImpl.checkWhite(str);
        }
        return false;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void reload(boolean z) {
        if (z) {
            this.mWebViewImpl.webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            this.mWebViewImpl.webSettings.setCacheMode(this.mCacheMode);
        }
        reload();
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public String obtainPageTitle() {
        String title = this.mWebViewImpl.getTitle();
        return TextUtils.isEmpty(title) ? this.mWebViewImpl.mPageTitle : title;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public String getWebviewProperty(String str) {
        if ("needTouchEvent".equals(str)) {
            return String.valueOf(this.needTouchEvent);
        }
        if (IWebview.USER_AGENT.equals(str)) {
            return this.mWebViewImpl.webSettings.getUserAgentString();
        }
        if (AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN.equals(str)) {
            return this.mVideoFullscreen;
        }
        if ("plusrequire".equals(str)) {
            return this.mPlusrequire;
        }
        if (this.mWebViewImpl.cm != null) {
            return this.mWebViewImpl.cm.getCookie(str);
        }
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setWebviewProperty(String str, String str2) {
        if ("needTouchEvent".equals(str)) {
            if (!TextUtils.isEmpty(str2)) {
                this.needTouchEvent = str2;
                return;
            } else {
                this.needTouchEvent = "";
                return;
            }
        }
        if (AbsoluteConst.JSON_KEY_SCALABLE.equals(str)) {
            boolean parseBoolean = PdrUtil.parseBoolean(str2, this.mFrameView.obtainFrameOptions().scalable, false);
            this.mWebViewImpl.webSettings.supportZoom();
            this.mWebViewImpl.webSettings.setBuiltInZoomControls(parseBoolean);
            this.mWebViewImpl.webSettings.setSupportZoom(parseBoolean);
            this.mWebViewImpl.webSettings.setUseWideViewPort(parseBoolean);
            return;
        }
        if (IWebview.USER_AGENT.equals(str)) {
            if (Boolean.parseBoolean(this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_H5PLUS)) && str2.indexOf(" Html5Plus/") < 0) {
                str2 = str2 + " Html5Plus/1.0";
            }
            WebViewImpl.sCustomUserAgent = str2;
            this.mWebViewImpl.webSettings.setUserAgentString(str2);
            return;
        }
        if (AbsoluteConst.JSON_KEY_BLOCK_NETWORK_IMAGE.equals(str)) {
            this.mWebViewImpl.webSettings.setBlockNetworkImage(PdrUtil.parseBoolean(str2, false, false));
            return;
        }
        if ("injection".equals(str)) {
            this.mNeedInjection = PdrUtil.parseBoolean(str2, true, false);
            return;
        }
        if (AbsoluteConst.JSON_KEY_BOUNCE.equals(str)) {
            if (DeviceInfo.sDeviceSdkVer >= 9) {
                if ("vertical".equalsIgnoreCase(str2) || "horizontal".equalsIgnoreCase(str2) || "all".equalsIgnoreCase(str2)) {
                    this.mWebViewImpl.setOverScrollMode(0);
                    return;
                } else {
                    this.mWebViewImpl.setOverScrollMode(2);
                    return;
                }
            }
            return;
        }
        if (AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN.equals(str)) {
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            this.mVideoFullscreen = str2;
        } else if ("plusrequire".equals(str)) {
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            this.mPlusrequire = str2;
        } else if ("geolocation".equals(str)) {
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            this.mInjectGEO = str2;
        } else if (this.mWebViewImpl.cm != null) {
            this.mWebViewImpl.cm.setCookie(str, str2);
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setScrollIndicator(String str) {
        if (PdrUtil.isEquals(str, "none")) {
            this.mWebViewImpl.setHorizontalScrollBarEnabled(false);
            this.mWebViewImpl.setVerticalScrollBarEnabled(false);
        } else if (PdrUtil.isEquals(str, "vertical")) {
            this.mWebViewImpl.setHorizontalScrollBarEnabled(false);
            this.mWebViewImpl.setVerticalScrollBarEnabled(true);
        } else if (PdrUtil.isEquals(str, "horizontal")) {
            this.mWebViewImpl.setHorizontalScrollBarEnabled(true);
            this.mWebViewImpl.setVerticalScrollBarEnabled(false);
        } else {
            this.mWebViewImpl.setHorizontalScrollBarEnabled(true);
            this.mWebViewImpl.setVerticalScrollBarEnabled(true);
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void onRootViewGlobalLayout(View view) {
        try {
            IApp obtainApp = obtainApp();
            if (obtainApp != null) {
                obtainApp.obtainWebAppRootView().onRootViewGlobalLayout(view);
            }
        } catch (Exception unused) {
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setOverrideResourceRequest(JSONArray jSONArray) {
        this.mOverrideResourceRequestOptions = jSONArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OverrideResourceRequestItem checkResourceRequestUrl(String str) {
        if (this.mOverrideResourceRequestOptions == null || Build.VERSION.SDK_INT < 16) {
            return null;
        }
        for (int i = 0; i < this.mOverrideResourceRequestOptions.length(); i++) {
            try {
                JSONObject optJSONObject = this.mOverrideResourceRequestOptions.optJSONObject(i);
                String optString = optJSONObject.optString("match", "");
                if (!TextUtils.isEmpty(optString) && Pattern.compile(optString).matcher(str).matches()) {
                    String convert2AbsFullPath = obtainApp().convert2AbsFullPath(optJSONObject.optString("redirect"));
                    String optString2 = optJSONObject.optString("mime", PdrUtil.getMimeType(convert2AbsFullPath));
                    String optString3 = optJSONObject.optString("encoding", "utf-8");
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject("header");
                    OverrideResourceRequestItem overrideResourceRequestItem = new OverrideResourceRequestItem();
                    overrideResourceRequestItem.redirect = convert2AbsFullPath;
                    overrideResourceRequestItem.encoding = optString3;
                    overrideResourceRequestItem.mime = optString2;
                    overrideResourceRequestItem.headerJson = optJSONObject2;
                    return overrideResourceRequestItem;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /* loaded from: classes.dex */
    class OverrideResourceRequestItem {
        public String redirect = null;
        public String mime = null;
        public String encoding = null;
        JSONObject headerJson = null;

        OverrideResourceRequestItem() {
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setOverrideUrlLoadingData(JSONObject jSONObject) {
        this.mOverrideUrlLoadingDataOptions = jSONObject;
        Logger.d("AdaFrameItem", "setOverrideUrlLoadingData=" + jSONObject);
        if (this.mFrameView.getFrameType() == 2) {
            this.mFrameView.obtainApp().setConfigProperty("wap2app_running_mode", AbsoluteConst.FALSE);
            this.mWebViewImpl.mWebLoadEvent.closeWap2AppBlockDialog(true);
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setListenResourceLoading(JSONObject jSONObject) {
        this.mListenResourceLoadingOptions = jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkResourceLoading(String str) {
        JSONObject jSONObject = this.mListenResourceLoadingOptions;
        if (jSONObject == null || !jSONObject.has("match")) {
            return true;
        }
        try {
            return Pattern.compile(this.mListenResourceLoadingOptions.optString("match")).matcher(str).matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkOverrideUrl(String str) {
        try {
            if (this.mOverrideUrlLoadingDataOptions == null) {
                return false;
            }
            int type = this.mWebViewImpl.getHitTestResult().getType();
            if ("redirect".equalsIgnoreCase(this.mOverrideUrlLoadingDataOptions.optString("exclude")) && type == 0) {
                return false;
            }
            String optString = this.mOverrideUrlLoadingDataOptions.optString("mode");
            boolean matches = this.mOverrideUrlLoadingDataOptions.has("match") ? Pattern.compile(this.mOverrideUrlLoadingDataOptions.optString("match")).matcher(str).matches() : true;
            if ("allow".equals(optString)) {
                if (matches) {
                    return false;
                }
            } else if (!matches) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public boolean isLoaded() {
        return this.mLoaded;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setOriginalUrl(String str) {
        this.originalUrl = str;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public String getOriginalUrl() {
        return this.originalUrl;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void setWebViewCacheMode(String str) {
        if (str.equals("default")) {
            this.mCacheMode = -1;
        } else if (str.equals("cacheElseNetwork")) {
            this.mCacheMode = 1;
        } else if (str.equals("noCache")) {
            this.mCacheMode = 2;
        } else if (str.equals("cacheOnly")) {
            this.mCacheMode = 3;
        }
        WebViewImpl webViewImpl = this.mWebViewImpl;
        if (webViewImpl != null) {
            webViewImpl.webSettings.setCacheMode(this.mCacheMode);
        }
    }

    public static void clearData() {
        setedWebViewData = false;
        WebViewImpl.sCustomUserAgent = null;
        WebViewImpl.sDefalutUserAgent = null;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public boolean unReceiveTitle() {
        return this.unReceiveTitle;
    }
}
