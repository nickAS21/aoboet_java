package io.dcloud.common.adapter.ui;

import static io.dcloud.common.util.NotificationUtil.showNotification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

import com.dcloud.android.v4.widget.IRefreshAble;
import com.dcloud.zxing.common.StringUtils;

import java.lang.reflect.Method;

import io.dcloud.IKeyHandler;
import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.io.DownloadReceiver;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.DownloadUtil;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdaWebview.java */
/* loaded from: classes.dex */
public class WebViewImpl extends WebView implements DownloadListener, IRefreshAble.OnRefreshListener {
    static final String PLUSSCROLLBOTTOM_JS_TEMPLATE = "(function(){var e = document.createEvent('HTMLEvents');var evt = 'plusscrollbottom';e.initEvent(evt, false, true);document.dispatchEvent(e);})();";
    static final String TAG = "webview";
    static final String UserAgentExtInfo = " Html5Plus/1.0";
    static final String UserAgentExtInfoForHBuilder = " StreamApp/1.0";
    static final String UserAgentQihoBrowser = " qihoobrowser";
    static final String UserAgentQihoo = " qihoo";
    static final String UserAgentStreamApp = " StreamApp/1.0";
    static String sCustomUserAgent;
    static String sDefalutUserAgent;
    CookieManager cm;
    boolean didTouch;
    boolean isToInvalidate;
    AdaWebview mAdaWebview;
    String mBaseUrl;
    private int mContentHeight;
    Context mContext;
    int mDeafaltOverScrollMode;
    private int mEventX;
    private int mEventY;
    private long mLastScrollTimestamp;
    private int mLastScrollY;
    private WebLoadEvent.OnPageFinishedCallack mPageFinishedCallack;
    String mPageTitle;
    ReceiveJSValue mReceiveJSValue_android42;
    float mScale;
    private int mThreshold;
    private int mThresholdTime;
    private int mTouchSlop;
    String mUrl;
    String mUserAgent;
    WebLoadEvent mWebLoadEvent;
    WebSettings webSettings;

    public WebViewImpl(Context context, AdaWebview adaWebview) {
        super(context);
        this.mUserAgent = null;
        this.mReceiveJSValue_android42 = null;
        this.mAdaWebview = null;
        this.mWebLoadEvent = null;
        this.mUrl = null;
        this.mScale = 0.0f;
        this.mContext = null;
        this.mBaseUrl = null;
        this.webSettings = getSettings();
        this.cm = null;
        this.mLastScrollY = 0;
        this.mContentHeight = 0;
        this.mThreshold = 2;
        this.mThresholdTime = 15;
        this.mLastScrollTimestamp = 0L;
        this.mPageTitle = null;
        this.mDeafaltOverScrollMode = 0;
        this.isToInvalidate = false;
        this.didTouch = false;
        this.mEventY = 0;
        this.mEventX = 0;
        this.mTouchSlop = -1;
        Logger.d("WebViewImpl");
        BaseInfo.s_Webview_Count++;
        this.mContext = context;
        this.mAdaWebview = adaWebview;
        if (BaseInfo.isTrafficFree() && DCloudApplication.self().isQihooTrafficFreeValidate) {
            InvokeExecutorHelper.TrafficProxy.setInstance(InvokeExecutorHelper.TrafficProxy.invoke("getInstance", new Class[0], new Object[0])).invoke("start");
        }
    }

    public void onPageStarted() {
        this.isToInvalidate = false;
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        try {
            float contentHeight = getContentHeight() * getScale();
            if (contentHeight > 0.0f) {
                if ((contentHeight > getHeight() || (this.mAdaWebview.mProgress > 60 && contentHeight >= getHeight())) && !this.isToInvalidate) {
                    this.mAdaWebview.dispatchWebviewStateEvent(6, Integer.valueOf(getContentHeight()));
                    this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_RENDERING, Integer.valueOf(getContentHeight()));
                    this.isToInvalidate = true;
                }
            }
        } catch (Exception unused) {
        }
    }

    public boolean checkWhite(String str) {
        Rect rect;
        System.currentTimeMillis();
        if (getWidth() <= 0) {
            return true;
        }
        if (str.equals(AbsoluteConst.JSON_VALUE_CENTER)) {
            int height = getHeight() / 2;
            rect = new Rect(0, height, getWidth(), height + 1);
        } else if (str.equals("top")) {
            int deivceSuitablePixel = DeviceInfo.getDeivceSuitablePixel(this.mAdaWebview.getActivity(), 20);
            rect = new Rect(0, deivceSuitablePixel, getWidth(), deivceSuitablePixel + 1);
        } else if (str.equals("bottom")) {
            int deivceSuitablePixel2 = DeviceInfo.getDeivceSuitablePixel(this.mAdaWebview.getActivity(), 25);
            rect = new Rect(0, (getHeight() - deivceSuitablePixel2) + 1, getWidth(), getHeight() - deivceSuitablePixel2);
        } else {
            int width = getWidth() / 2;
            rect = new Rect(width, 0, width + 5, getHeight());
        }
        Bitmap captureWebView = captureWebView(this, rect);
        if (captureWebView == null) {
            return false;
        }
        boolean isWhiteBitmap = str.equals("auto") ? PlatformUtil.isWhiteBitmap(captureWebView) : PlatformUtil.isLineWhiteBitmap(captureWebView, !this.mAdaWebview.isLoaded());
        captureWebView.recycle();
        return isWhiteBitmap;
    }

    private Bitmap captureWebView(WebView webView, Rect rect) {
        Picture capturePicture = webView.capturePicture();
        Bitmap createBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        canvas.translate(-rect.left, -rect.top);
        capturePicture.draw(canvas);
        return createBitmap;
    }

    public WebViewImpl(Context context, AdaWebview adaWebview, WebLoadEvent.OnPageFinishedCallack onPageFinishedCallack) {
        super(context);
        this.mUserAgent = null;
        this.mReceiveJSValue_android42 = null;
        this.mAdaWebview = null;
        this.mWebLoadEvent = null;
        this.mUrl = null;
        this.mScale = 0.0f;
        this.mContext = null;
        this.mBaseUrl = null;
        this.webSettings = getSettings();
        this.cm = null;
        this.mLastScrollY = 0;
        this.mContentHeight = 0;
        this.mThreshold = 2;
        this.mThresholdTime = 15;
        this.mLastScrollTimestamp = 0L;
        this.mPageTitle = null;
        this.mDeafaltOverScrollMode = 0;
        this.isToInvalidate = false;
        this.didTouch = false;
        this.mEventY = 0;
        this.mEventX = 0;
        this.mTouchSlop = -1;
        Logger.d("WebViewImpl");
        this.mContext = context;
        this.mAdaWebview = adaWebview;
        this.mPageFinishedCallack = onPageFinishedCallack;
    }

    protected boolean isReadyForPullUp(int i) {
        int floor = ((int) Math.floor(getContentHeight() * getScale())) - getHeight();
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = (i >= floor || (i >= floor - this.mThreshold && currentTimeMillis - this.mLastScrollTimestamp > ((long) this.mThresholdTime))) && this.mLastScrollY < this.mContentHeight;
        this.mLastScrollY = i;
        this.mContentHeight = floor;
        long j = this.mLastScrollTimestamp;
        if (currentTimeMillis - j <= 500) {
            this.mLastScrollTimestamp = currentTimeMillis - j;
        }
        return z;
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        if ((i == i3 && i2 == i4) || AndroidResources.sIMEAlive || !isReadyForPullUp(i2)) {
            return;
        }
        Logger.d("onPlusScrollBottom", "上拉事件  url=" + this.mAdaWebview.obtainUrl());
        this.mAdaWebview.executeScript(PLUSSCROLLBOTTOM_JS_TEMPLATE);
    }

    @Override // android.webkit.WebView
    public void loadUrl(String str) {
        if (str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT) && Build.VERSION.SDK_INT >= 19) {
            evaluateJavascript(str, null);
        } else {
            super.loadUrl(str);
        }
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    void initUserAgent(IApp iApp) {
        if (BaseInfo.isQihooLifeHelper(iApp.getActivity()) && !PdrUtil.isEquals(iApp.obtainAppId(), BaseInfo.sDefaultBootApp) && !PdrUtil.isEmpty(BaseInfo.sGlobalUserAgent)) {
            this.webSettings.setUserAgentString(BaseInfo.sGlobalUserAgent);
            return;
        }
        String obtainConfigProperty = iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_USER_AGENT);
        boolean parseBoolean = Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_CONCATENATE));
        boolean parseBoolean2 = Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_H5PLUS));
        if (PdrUtil.isEmpty(sDefalutUserAgent)) {
            sDefalutUserAgent = this.webSettings.getUserAgentString();
        }
        this.mUserAgent = sDefalutUserAgent;
        if (parseBoolean) {
            this.mUserAgent = obtainConfigProperty;
        } else if (!PdrUtil.isEmpty(obtainConfigProperty)) {
            this.mUserAgent += " " + obtainConfigProperty.trim();
        }
        String str = AndroidResources.checkImmersedStatusBar(this.mAdaWebview.getActivity()) ? " (Immersed/" + (DeviceInfo.sStatusBarHeight / getScale()) + ")" : "";
        if (parseBoolean2 && this.mUserAgent.indexOf(UserAgentExtInfo) < 0) {
            if (BaseInfo.isForQihooBrowser(getContext())) {
                this.mUserAgent += UserAgentExtInfo + str + " StreamApp/1.0" + UserAgentQihoBrowser;
            } else if (BaseInfo.isForQihooHelper(getContext()) || BaseInfo.isQihooLifeHelper(getContext())) {
                this.mUserAgent += UserAgentExtInfo + str + " StreamApp/1.0" + UserAgentQihoo;
            } else if (iApp.isStreamApp() || BaseInfo.isStreamApp(getContext())) {
                this.mUserAgent += UserAgentExtInfo + str + " StreamApp/1.0";
            } else if (BaseInfo.ISAMU && BaseInfo.isBase(getContext())) {
                this.mUserAgent += " Html5Plus/1.0 StreamApp/1.0" + str;
            } else {
                this.mUserAgent += UserAgentExtInfo + str;
            }
        }
        Logger.d(TAG, "userAgent=" + this.mUserAgent);
        this.webSettings.setUserAgentString(this.mUserAgent);
        if (BaseInfo.isQihooLifeHelper(iApp.getActivity()) && PdrUtil.isEquals(iApp.obtainAppId(), BaseInfo.sDefaultBootApp) && PdrUtil.isEmpty(BaseInfo.sGlobalUserAgent)) {
            BaseInfo.sGlobalUserAgent = this.mUserAgent;
        }
    }

    public void init() {
        if (!AdaWebview.setedWebViewData) {
            boolean hasFile = DHFile.hasFile();
            boolean parseBoolean = Boolean.parseBoolean(this.mAdaWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_USE_ENCRYPTION));
            if ((BaseInfo.ISDEBUG || hasFile) && !parseBoolean) {
                if (Build.VERSION.SDK_INT >= 19) {
                    PlatformUtil.invokeMethod("android.webkit.WebView", "setWebContentsDebuggingEnabled", null, new Class[]{Boolean.TYPE}, new Object[]{true});
                }
            } else {
                setWebViewData();
            }
            AdaWebview.setedWebViewData = true;
        }
        setDownloadListener(this);
        if (DeviceInfo.sDeviceSdkVer >= 9) {
            this.mDeafaltOverScrollMode = getOverScrollMode();
        }
        this.mScale = getContext().getResources().getDisplayMetrics().density;
        try {
            CookieSyncManager.createInstance(this.mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            this.cm = cookieManager;
            if (cookieManager != null) {
                PlatformUtil.invokeMethod(CookieManager.class.getName(), "setAcceptThirdPartyCookies", this.cm, new Class[]{WebView.class, Boolean.TYPE}, new Object[]{this, true});
                this.cm.setAcceptCookie(true);
                this.cm.removeExpiredCookie();
                CookieSyncManager.getInstance().sync();
            }
        } catch (Throwable th) {
            Logger.e("WebViewImpl CookieManager.getInstance Exception =" + th);
        }
        this.mAdaWebview.obtainFrameView().onInit();
        IApp obtainApp = this.mAdaWebview.obtainFrameView().obtainApp();
        this.mBaseUrl = obtainApp.obtainWebviewBaseUrl();
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        String str = sCustomUserAgent;
        if (str != null) {
            this.webSettings.setUserAgentString(str);
        } else {
            initUserAgent(obtainApp);
        }
        if (DeviceInfo.sDeviceSdkVer > 16) {
            this.webSettings.setAllowUniversalAccessFromFileURLs(true);
            this.webSettings.setAllowFileAccessFromFileURLs(true);
        }
        this.webSettings.setAllowFileAccess(true);
        this.webSettings.setDefaultTextEncodingName(StringUtils.GB2312);
        this.webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        this.webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        this.webSettings.setSaveFormData(false);
        this.webSettings.setJavaScriptEnabled(true);
        this.webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        boolean z = this.mAdaWebview.mFrameView.mViewOptions.scalable;
        this.webSettings.supportZoom();
        this.webSettings.setBuiltInZoomControls(z);
        this.webSettings.setSupportZoom(z);
        this.webSettings.setUseWideViewPort(z);
        this.webSettings.setDatabasePath(this.mAdaWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
//        this.webSettings.setAppCacheEnabled(true);
//        this.webSettings.setAppCachePath(this.mAdaWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
        this.webSettings.setDatabaseEnabled(true);
        if (DeviceInfo.sDeviceSdkVer >= 7) {
//            long j = this.mContext.getSharedPreferences(IApp.obtainAppId(), 0).getLong("maxSize", 0L);
//            this.webSettings.setDomStorageEnabled(true);
//            if (j != 0) {
//                this.webSettings.setAppCacheMaxSize(j);
//            }
        }
        this.webSettings.setAllowFileAccess(true);
        this.webSettings.setGeolocationEnabled(true);
        this.webSettings.setGeolocationDatabasePath(this.mAdaWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
        if (Build.VERSION.SDK_INT >= 21) {
            PlatformUtil.invokeMethod("android.webkit.WebSettings", "setMixedContentMode", this.webSettings, new Class[]{Integer.TYPE}, new Object[]{0});
        }
        setWebChromeClient(new WebJsEvent(this.mAdaWebview));
        WebLoadEvent webLoadEvent = new WebLoadEvent(this.mAdaWebview);
        this.mWebLoadEvent = webLoadEvent;
        this.mScale = getScale();
        webLoadEvent.setPageFinishedCallack(this.mPageFinishedCallack);
        setWebViewClient(webLoadEvent);
        ReceiveJSValue.addJavascriptInterface(this.mAdaWebview);
        requestFocus();
        setClickable(true);
        removeUnSafeJavascriptInterface();
    }

    private void removeUnSafeJavascriptInterface() {
        try {
            if (Build.VERSION.SDK_INT < 11 || Build.VERSION.SDK_INT >= 17) {
                return;
            }
            Method method = getClass().getMethod("removeJavascriptInterface", String.class);
            String[] strArr = {"searchBoxJavaBridge_", "accessibility", "ccessibilityaversal"};
            for (int i = 0; i < 3; i++) {
                method.invoke(this, strArr[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.webkit.DownloadListener
    public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
        Logger.i(TAG, "onDownloadStart " + str + " " + str2 + " " + str3 + " " + str4 + " " + j);
        try {
            if (DeviceInfo.sDeviceSdkVer > 8) {
                final IApp obtainApp = this.mAdaWebview.obtainFrameView().obtainApp();
                String convert2AbsFullPath = obtainApp.convert2AbsFullPath("_downloads/");
                final String downloadFilename = PdrUtil.getDownloadFilename(str3, str4, str);
                final Context context = getContext();
                DownloadUtil.startRequest(context, str, str4, convert2AbsFullPath, downloadFilename, new ICallBack() { // from class: io.dcloud.common.adapter.ui.WebViewImpl.1
                    @Override // io.dcloud.common.DHInterface.ICallBack
                    public Object onCallBack(int i, Object obj) {
                        if (BaseInfo.sRuntimeMode == null) {
                            IntentFilter intentFilter = new IntentFilter();
                            intentFilter.addAction(DownloadReceiver.ACTION_OPEN_FILE);
                            ContextCompat.registerReceiver(context, new DownloadReceiver(), intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED);
                            Intent intent = new Intent();
                            intent.setAction(DownloadReceiver.ACTION_OPEN_FILE);
                            intent.putExtra(DownloadReceiver.KEY_FILEURI, String.valueOf(obj));
                            PendingIntent pendingIntent = PendingIntent.getActivity(
                                    context,
                                    0,
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                            final IApp iApp = mAdaWebview.obtainFrameView().obtainApp();
                            showNotification(WebViewImpl.this.getContext(), iApp.obtainAppName(), downloadFilename + " Download Complete", pendingIntent, null, 1);
                            return null;
                        }
                        Intent intent2 = new Intent();
                        intent2.putExtra(DownloadReceiver.KEY_FILEURI, String.valueOf(obj));
                        intent2.setAction(DownloadReceiver.ACTION_DOWNLOAD_COMPLETED);
                        context.sendBroadcast(intent2);
                        return null;
                    }
                });
            } else {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                this.mAdaWebview.getActivity().startActivity(intent);
            }
        } catch (Exception e) {
            Logger.w("webview onDownloadStart", e);
            Logger.e(TAG, "browser will download url=" + str);
            try {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.setData(Uri.parse(str));
                this.mAdaWebview.getActivity().startActivity(intent2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void setWebViewData() {
        Class[] clsArr = new Class[0];
        Class<?>[] clsArr2 = {Boolean.TYPE};
        try {
            Method declaredMethod = WebView.class.getDeclaredMethod("getFactory", clsArr);
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
                Object[] objArr = {false};
                try {
                    Object invoke = declaredMethod.invoke(null, new Object[0]);
                    Method declaredMethod2 = invoke.getClass().getDeclaredMethod("setWebContentsDebuggingEnabled", clsArr2);
                    if (declaredMethod2 != null) {
                        declaredMethod2.setAccessible(true);
                        declaredMethod2.invoke(invoke, objArr);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e5) {
            e5.printStackTrace();
        }
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Logger.i(TAG, "onConfigurationChanged");
        this.mAdaWebview.executeScript(String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_PLUS_ORIENTATI_ONCHANGE));
    }

    @Override // android.view.View
    public String toString() {
        String str = this.mUrl;
        if (str != null) {
            int indexOf = str.indexOf(this.mBaseUrl);
            String str2 = this.mUrl;
            if (indexOf >= 0) {
                str2 = str2.substring(this.mBaseUrl.length());
            }
            return "<url=" + str2 + ">;<hashcode=" + hashCode() + ">";
        }
        return super.toString();
    }

    @Override // android.view.ViewGroup
    public void removeAllViews() {
        super.removeAllViews();
        if (((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView != null) {
            addView((View) ((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView, -1, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String convertRelPath(String str) {
        if (str.indexOf(this.mBaseUrl) >= 0) {
            return str.substring(this.mBaseUrl.length());
        }
        String substring = this.mBaseUrl.substring(7);
        return str.indexOf(substring) >= 0 ? str.substring(substring.length()) : str;
    }

    @Override // android.webkit.WebView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean onKeyEventExecute;
        if (BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyDown(i, keyEvent);
        }
        IKeyHandler dVar = (IKeyHandler) this.mAdaWebview.getActivity();
        if (keyEvent.getRepeatCount() == 0) {
            onKeyEventExecute = dVar.onKeyEventExecute(ISysEventListener.SysEventType.onKeyDown, i, keyEvent);
        } else {
            onKeyEventExecute = dVar.onKeyEventExecute(ISysEventListener.SysEventType.onKeyDown, i, keyEvent);
        }
        return onKeyEventExecute ? onKeyEventExecute : super.onKeyDown(i, keyEvent);
    }

    @Override // android.webkit.WebView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyUp(i, keyEvent);
        }
        boolean onKeyEventExecute = ((IKeyHandler) this.mAdaWebview.getActivity()).onKeyEventExecute(ISysEventListener.SysEventType.onKeyUp, i, keyEvent);
        return onKeyEventExecute ? onKeyEventExecute : super.onKeyUp(i, keyEvent);
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView == null || !((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView.isRefreshEnable()) {
            return;
        }
        ((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView.onSelfDraw(canvas);
    }

    @Override // com.dcloud.android.v4.widget.IRefreshAble.OnRefreshListener
    public void onRefresh(int i) {
        this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_PULL_DOWN_EVENT, Integer.valueOf(i));
    }

    @Override // android.webkit.WebView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Object invokeMethod;
        this.didTouch = true;
        if (Build.VERSION.SDK_INT == 16 && !PdrUtil.isEquals(Build.BRAND, "samsung") && motionEvent.getAction() == 0) {
            int scrollY = getScrollY();
            scrollTo(getScrollX(), scrollY + 1);
            scrollTo(getScrollX(), scrollY);
        }
        if (motionEvent.getAction() == 0) {
            this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START, Integer.valueOf(getContentHeight()));
        }
        boolean z = false;
        if (BaseInfo.isShowTitleBar(this.mContext)) {
            Context context = this.mContext;
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                int action = motionEvent.getAction();
                if (action == 0) {
                    this.mEventY = (int) motionEvent.getRawY();
                    this.mEventX = (int) motionEvent.getRawX();
                } else if (action == 1) {
                    int rawY = ((int) motionEvent.getRawY()) - this.mEventY;
                    if (Math.abs(rawY) > Math.abs(((int) motionEvent.getRawX()) - this.mEventX)) {
                        if (-1 == this.mTouchSlop) {
                            this.mTouchSlop = ViewConfiguration.get(this.mContext).getScaledTouchSlop();
                        }
                        if (Math.abs(rawY) > this.mTouchSlop) {
                            if (rawY > 0) {
                                PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "showTitleView", null, new Class[]{Activity.class}, new Object[]{activity});
                            } else {
                                PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "hideTitleView", null, new Class[]{Activity.class}, new Object[]{activity});
                            }
                        }
                    }
                }
            }
        }
        final IApp obtainApp = this.mAdaWebview.obtainFrameView().obtainApp();
        if (((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView != null && ((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView.isRefreshEnable() && this.mAdaWebview.mWebViewImpl.getScrollY() <= 0 && (!BaseInfo.isShowTitleBar(this.mContext) || ((invokeMethod = PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "isTitlebarVisible", null, new Class[]{Activity.class, String.class}, new Object[]{(Activity) this.mContext, obtainApp.obtainAppId()})) != null && (invokeMethod instanceof Boolean) && ((Boolean) invokeMethod).booleanValue()))) {
            z = true;
        }
        if (z && ((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView.onSelfTouchEvent(motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @SuppressLint("JavascriptInterface")
    @Override // android.webkit.WebView
//    @JavascriptInterface
    public void addJavascriptInterface(Object obj, String str) {
        super.addJavascriptInterface(obj, str);
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (Build.VERSION.SDK_INT >= 11) {
            return super.startActionMode(new CustomizedSelectActionModeCallback(callback));
        }
        return super.startActionMode(callback);
    }

    /* compiled from: AdaWebview.java */
    /* loaded from: classes.dex */
    public class CustomizedSelectActionModeCallback implements ActionMode.Callback {
        ActionMode.Callback callback;

        public CustomizedSelectActionModeCallback(ActionMode.Callback callback) {
            this.callback = callback;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            this.callback.onCreateActionMode(actionMode, menu);
            int size = menu.size();
            for (int i = 0; i < size; i++) {
                MenuItem item = menu.getItem(i);
                String obj = item.getTitle().toString();
                if (obj.contains("搜索") || obj.toLowerCase().contains(AbsoluteConst.EVENTS_SEARCH)) {
                    menu.removeItem(item.getItemId());
                    return true;
                }
            }
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.callback.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.callback.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.callback.onDestroyActionMode(actionMode);
        }
    }
}
