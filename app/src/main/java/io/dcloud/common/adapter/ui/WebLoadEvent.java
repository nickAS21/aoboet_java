package io.dcloud.common.adapter.ui;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;

import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DCloudTrustManager;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DLGeolocation;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.Md5Utils;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.dcloud_a.EncryptionConstant;
import io.dcloud.dcloud_a.EncryptionSingleton;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* loaded from: classes.dex */
public final class WebLoadEvent extends WebViewClient {
    private static final String DIFFERENT_VERSION_JS = "window.plus && (plus.android.import=plus.android.importClass);";
    public static final String ENABLE = "enable";
    private static final String ERROR_TEMPLATE = "javascript:(function(){var b=document.createEvent('HTMLEvents');var a='%s';b.url='%s';b.href='%s';b.initEvent(a,false,true);console.error(a);document.dispatchEvent(b);})();";
    private static final String IF_LOAD_TEMPLATE = "(function(){/*console.log('eval js loading href=' + location.href);*/if(location.__page__load__over__){return 2}if(location.__plusready__||window.__html5plus__){return 1}return 0})();";
    private static final String IF_PLUSREADY_EVENT_TEMPLATE = "(function(){/*console.log('plusready event loading href=' + location.href);*/if(location.__page__load__over__){return 2}if(location.__plusready__||window.__html5plus__){if(!location.__plusready__event__){location.__plusready__event__=true;return 1}else{return 2}}return 0})();";
    private static final String IF_PLUSREADY_TEMPLATE = "(function(){/*console.log('all.js loading href=' + location.href);*/if(location.__page__load__over__){return 2}if(!location.__plusready__){location.__plusready__=true;return 1}else{return 2}return 0})();";
    private static final String IF_PRELOAD_TEMPLATE = "(function(){/*console.log( 'preload js loading href=' + location.href);*/if(location.__page__load__over__){return 2}var jsfile='%s';if(location.__plusready__||window.__html5plus__){location.__preload__=location.__preload__||[];if(location.__preload__.indexOf(jsfile)<0){location.__preload__.push(jsfile);return 1}else{return 2}}return 0})();";
    private static final int LOADABLE = 1;
    private static final int LOADED = 2;
    private static final int NOLOAD = 0;
    public static String PAGE_FINISHED_FLAG = "javascript:setTimeout(function(){location.__page__load__over__ = true;},2000);";
    public static final String PLUSREADY = "html5plus://ready";
    static final String TAG = "webview";
    static final int Timeout_Page_Finish = 6000;
    static final int Timeout_Plus_Inject = 3000;
    boolean isInitAmapGEO;
    AdaWebview mAdaWebview;
    String mAppid;
    private boolean mClearCache;
    boolean mIsStreamApp;
    String mPlusJS;
    long mShowLoadingTime;
    boolean printLog;
    private OnPageFinishedCallack mPageFinishedCallack = null;
    private String mLastPageUrl = "";
    InvokeExecutorHelper.InvokeExecutor mWap2AppBlockDialog = null;
    ISysEventListener mWap2AppBlockDialogSysEventListener = null;
    String TYPE_JS = "type_js";
    String TYPE_CSS = "type_css";
    ProgressBar mWaitingForWapPage = null;
    Runnable Timeout_Plus_Inject_Runnable = null;
    Runnable Timeout_Page_Finish_Runnable = null;

    /* loaded from: classes.dex */
    public interface OnPageFinishedCallack {
        void onLoad();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebLoadEvent(AdaWebview adaWebview) {
        this.mAdaWebview = null;
        this.printLog = false;
        this.mClearCache = false;
        this.mAppid = null;
        this.mIsStreamApp = false;
        this.mPlusJS = null;
        this.isInitAmapGEO = false;
        this.mAdaWebview = adaWebview;
        this.mAppid = adaWebview.obtainApp().obtainAppId();
        this.printLog = !BaseInfo.isQihooLifeHelper(adaWebview.getContext());
        String obtainConfigProperty = adaWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_RAM_CACHE_MODE);
        this.mIsStreamApp = adaWebview.obtainApp().isStreamApp();
        if (BaseInfo.isBase(adaWebview.getContext()) && !ENABLE.equalsIgnoreCase(obtainConfigProperty)) {
            this.mClearCache = true;
        }
        this.mPlusJS = "(function(){/*console.log('all.js loading href=' + location.href);*/if(location.__page__load__over__){return 2}if(!location.__plusready__){location.__plusready__=true;return 1}else{return 2}return 0})();\n" + this.mAdaWebview.mFrameView.obtainPrePlusreadyJs() + "\n" + DIFFERENT_VERSION_JS;
        this.isInitAmapGEO = DLGeolocation.checkAMapGeo();
    }

    @Override // android.webkit.WebViewClient
    public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
        String str2;
        String str3;
        WebResourceResponse downloadResponseInjection = null;
        WebResourceResponse shouldInterceptRequest = super.shouldInterceptRequest(webView, str);
        AdaWebview.OverrideResourceRequestItem checkResourceRequestUrl = this.mAdaWebview.checkResourceRequestUrl(str);
        String str4 = this.mAdaWebview.mEncoding;
        if (checkResourceRequestUrl != null) {
            String str5 = checkResourceRequestUrl.redirect;
            String str6 = checkResourceRequestUrl.encoding;
            str2 = checkResourceRequestUrl.mime;
            str3 = str5;
            str4 = str6;
        } else {
            str2 = "application/x-javascript";
            str3 = str;
        }
        String str7 = str2;
        try {
            if (this.mIsStreamApp) {
                String originalUrl = getOriginalUrl(str3);
                if (originalUrl.startsWith(this.mAdaWebview.obtainApp().obtainAppDataPath())) {
                    File file = new File(PdrUtil.stripQuery(PdrUtil.stripAnchor(originalUrl)));
                    if (!file.exists() || file.length() == 0) {
                        String urlByFilePath = getUrlByFilePath(str3);
                        sendStatistics(this.mAdaWebview.mFrameView.obtainApp().obtainAppId(), webView.getContext(), str3, this.mAdaWebview.obtainFullUrl(), this.mAdaWebview.mFrameView.obtainApp());
                        return downloadResponse(webView, str3, urlByFilePath, shouldInterceptRequest, file, true);
                    }
                }
            }
            Logger.i(TAG, "shouldInterceptRequest url=" + str3 + ";withJs=" + this.mAdaWebview.mInjectPlusWidthJs);
            shouldInterceptRequest = handleDecode(str3, shouldInterceptRequest);
            if (shouldInterceptRequest == null) {
                if (this.mAdaWebview.mPlusrequire.equals("ahead") && this.mAdaWebview.hasPreLoadJsFile() && ((this.mAdaWebview.mInjectPlusWidthJs == null || TextUtils.equals(this.mAdaWebview.mInjectPlusWidthJs, str3)) && PdrUtil.isNetPath(str3) && checkJsFile(str3))) {
                    shouldInterceptRequest = downloadResponseInjection(shouldInterceptRequest, str3, str7, str4, this.TYPE_JS);
                    if (shouldInterceptRequest != null) {
                        this.mAdaWebview.mInjectPlusWidthJs = str3;
                    }
                } else {
                    if (!TextUtils.isEmpty(this.mAdaWebview.getCssString()) && !this.mAdaWebview.mIsAdvanceCss && PdrUtil.isNetPath(str3) && checkCssFile(str3)) {
                        str7 = "text/css";
                        downloadResponseInjection = downloadResponseInjection(shouldInterceptRequest, str3, "text/css", str4, this.TYPE_CSS);
                    } else if (this.isInitAmapGEO && !this.mAdaWebview.mInjectGeoLoaded && DLGeolocation.checkInjectGeo(this.mAdaWebview.mInjectGEO)) {
                        downloadResponseInjection = downloadResponseInjection(shouldInterceptRequest, str3, str7, str4, this.TYPE_JS);
                    }
                    shouldInterceptRequest = downloadResponseInjection;
                }
            }
            if (shouldInterceptRequest == null && !BaseInfo.isWap2AppAppid(this.mAppid) && PLUSREADY.equals(str3) && !this.mAdaWebview.mPlusLoaded) {
                shouldInterceptRequest = downloadResponseInjection(shouldInterceptRequest, str3, str7, str4, this.TYPE_JS);
            }
            if (shouldInterceptRequest == null && checkResourceRequestUrl != null) {
                try {
                    shouldInterceptRequest = new WebResourceResponse(str7, str4, new FileInputStream(str3));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (shouldInterceptRequest != null && checkResourceRequestUrl != null && checkResourceRequestUrl.headerJson != null && Build.VERSION.SDK_INT >= 21) {
                Iterator<String> keys = checkResourceRequestUrl.headerJson.keys();
                if (checkResourceRequestUrl.headerJson.length() > 0) {
                    Map<String, String> responseHeaders = shouldInterceptRequest.getResponseHeaders();
                    if (responseHeaders == null) {
                        responseHeaders = new HashMap<>();
                    }
                    responseHeaders.put("Access-Control-Allow-Credentials", AbsoluteConst.TRUE);
                    responseHeaders.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                    responseHeaders.put("Access-Control-Allow-Origin", "*");
                    while (keys.hasNext()) {
                        String next = keys.next();
                        responseHeaders.put(next, checkResourceRequestUrl.headerJson.opt(next).toString());
                    }
                    shouldInterceptRequest.setResponseHeaders(responseHeaders);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Logger.e(this.mAppid + ";url=" + str3);
        }
        return shouldInterceptRequest;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0080  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private WebResourceResponse downloadResponse(WebView r11, String r12, String r13, WebResourceResponse r14, File r15, boolean r16) {
        /*
            r10 = this;
            r0 = r13
            r8 = r14
            boolean r1 = android.webkit.URLUtil.isNetworkUrl(r13)
            if (r1 == 0) goto Laa
            if (r15 != 0) goto Lc
            goto Laa
        Lc:
            r10.showLoading()
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch: java.lang.Throwable -> L8b java.lang.Exception -> L8e
            r2.<init>(r13)     // Catch: java.lang.Throwable -> L8b java.lang.Exception -> L8e
            java.net.URLConnection r2 = r2.openConnection()     // Catch: java.lang.Throwable -> L8b java.lang.Exception -> L8e
            r9 = r2
            java.net.HttpURLConnection r9 = (java.net.HttpURLConnection) r9     // Catch: java.lang.Throwable -> L8b java.lang.Exception -> L8e
            r1 = 5000(0x1388, float:7.006E-42)
            r9.setConnectTimeout(r1)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            r9.setReadTimeout(r1)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            java.lang.String r1 = "GET"
            r9.setRequestMethod(r1)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            r1 = 1
            r9.setDoInput(r1)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            int r2 = r9.getResponseCode()     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 == r3) goto L53
            r3 = 206(0xce, float:2.89E-43)
            if (r2 != r3) goto L3a
            goto L53
        L3a:
            r1 = 400(0x190, float:5.6E-43)
            if (r2 < r1) goto L42
            r1 = 500(0x1f4, float:7.0E-43)
            if (r2 < r1) goto L4f
        L42:
            if (r16 == 0) goto L4f
            r7 = 0
            r1 = r10
            r2 = r11
            r3 = r12
            r4 = r13
            r5 = r14
            r6 = r15
            r1.downloadResponse(r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            goto L7e
        L4f:
            r10.hideLoading()     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            goto L7e
        L53:
            java.io.InputStream r2 = r9.getInputStream()     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            java.lang.String r3 = r15.getAbsolutePath()     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            boolean r2 = io.dcloud.common.adapter.io.DHFile.writeFile(r2, r3)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            if (r2 == 0) goto L6e
            io.dcloud.common.adapter.util.InvokeExecutorHelper$InvokeExecutor r2 = io.dcloud.common.adapter.util.InvokeExecutorHelper.createDownloadTaskListManager()     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            java.lang.String r3 = "removeTask"
            r2.invoke(r3, r13, r1)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            r10.hideLoading()     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            goto L7e
        L6e:
            if (r16 == 0) goto L7b
            r7 = 0
            r1 = r10
            r2 = r11
            r3 = r12
            r4 = r13
            r5 = r14
            r6 = r15
            r1.downloadResponse(r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
            goto L7e
        L7b:
            r10.hideLoading()     // Catch: java.lang.Throwable -> L84 java.lang.Exception -> L88
        L7e:
            if (r9 == 0) goto L97
            r9.disconnect()
            goto L97
        L84:
            r0 = move-exception
            r2 = r10
            r1 = r9
            goto La1
        L88:
            r0 = move-exception
            r1 = r9
            goto L8f
        L8b:
            r0 = move-exception
            r2 = r10
            goto La1
        L8e:
            r0 = move-exception
        L8f:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L8b
            if (r1 == 0) goto L97
            r1.disconnect()
        L97:
            r10.hideLoading()
            r2 = r10
            r1 = r12
            android.webkit.WebResourceResponse r0 = r10.handleDecode(r12, r14)
            return r0
        La1:
            if (r1 == 0) goto La6
            r1.disconnect()
        La6:
            r10.hideLoading()
            throw r0
        Laa:
            r2 = r10
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.WebLoadEvent.downloadResponse(android.webkit.WebView, java.lang.String, java.lang.String, android.webkit.WebResourceResponse, java.io.File, boolean):android.webkit.WebResourceResponse");
    }

    private WebResourceResponse handleDecode(String str, WebResourceResponse webResourceResponse) {
        if (TextUtils.isEmpty(str) || !needDecode(str, this.mAdaWebview.mFrameView.obtainApp())) {
            return webResourceResponse;
        }
        try {
            String str2 = (String) InvokeExecutorHelper.AesEncryptionUtil.invoke("decrypt", new Class[]{String.class, String.class, String.class}, (String) InvokeExecutorHelper.AesEncryptionUtil.invoke("byte2hex", new Class[]{byte[].class}, IOUtil.getBytes(new DataInputStream(new FileInputStream(new File(URI.create(str)))))), getAESKey(str, this.mAdaWebview.mFrameView.obtainApp()), EncryptionConstant.a());
            return str2 != null ? new WebResourceResponse(getMimeType(str), "UTF-8", new ByteArrayInputStream(str2.getBytes())) : webResourceResponse;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return webResourceResponse;
        } catch (Exception e2) {
            e2.printStackTrace();
            return webResourceResponse;
        }
    }

    private boolean shouldRuntimeHandle(String str) {
        return PdrUtil.isDeviceRootDir(str) || PdrUtil.isNetPath(str) || str.startsWith(DeviceInfo.FILE_PROTOCOL);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeWap2AppBlockDialog(boolean z) {
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.mWap2AppBlockDialog;
        if (invokeExecutor != null) {
            invokeExecutor.invoke(AbsoluteConst.EVENTS_CLOSE);
            this.mAdaWebview.obtainApp().unregisterSysEventListener(this.mWap2AppBlockDialogSysEventListener, ISysEventListener.SysEventType.onKeyUp);
            this.mWap2AppBlockDialog = null;
            this.mWap2AppBlockDialogSysEventListener = null;
            if (z) {
                AdaWebview adaWebview = this.mAdaWebview;
                adaWebview.loadUrl(adaWebview.mRecordLastUrl);
//                adaWebview.loadUrl("file:///android_asset/apps/H5057CD3A/www/login.html");
            }
        }
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        Logger.i(TAG, "shouldOverrideUrlLoading url=" + str);
        this.mAdaWebview.mRecordLastUrl = str;
        if (this.mAdaWebview.checkOverrideUrl(str)) {
            Logger.i("shutao", "检测是否执行拦截回调shouldOverrideUrlLoading url=" + str);
            this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_OVERRIDE_URL_LOADING, "{url:'" + str + "'}");
            return true;
        }
        if (!shouldRuntimeHandle(str)) {
            try {
                if (str.startsWith("sms:")) {
                    int indexOf = str.indexOf("sms:");
                    int indexOf2 = str.indexOf("?");
                    if (indexOf2 == -1) {
                        this.mAdaWebview.getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                        return true;
                    }
                    String substring = str.substring(indexOf + 4, indexOf2);
                    String substring2 = str.substring(indexOf2 + 1);
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("sms:" + substring));
                    intent.putExtra("address", substring);
                    intent.putExtra("sms_body", substring2);
                    this.mAdaWebview.getActivity().startActivity(intent);
                } else {
                    this.mAdaWebview.getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                }
            } catch (ActivityNotFoundException unused) {
                Logger.e(TAG, "ActivityNotFoundException url=" + str);
            }
            return true;
        }
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null && adaWebview.obtainApp() != null && AbsoluteConst.TRUE.equals(this.mAdaWebview.obtainApp().obtainConfigProperty("wap2app_running_mode")) && this.mAdaWebview.mLoadCompleted && this.mAdaWebview.mWebViewImpl.didTouch && this.mAdaWebview.obtainApp().obtainWebAppRootView().didCloseSplash() && BaseInfo.isWap2AppAppid(this.mAppid) && this.mAdaWebview.obtainFrameView().getFrameType() == 2) {
            if (this.mWap2AppBlockDialog == null) {
                InvokeExecutorHelper.InvokeExecutor createInvokeExecutor = InvokeExecutorHelper.createInvokeExecutor("io.dcloud.base.ui.WaitingView", new Class[]{IWebview.class}, this.mAdaWebview);
                Logger.e(TAG, "shouldOverrideUrlLoading block url =" + str);
                if (createInvokeExecutor.hasObject()) {
                    this.mWap2AppBlockDialogSysEventListener = new ISysEventListener() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.1
                        @Override // io.dcloud.common.DHInterface.ISysEventListener
                        public boolean onExecute(SysEventType sysEventType, Object obj) {
                            if (((Integer) ((Object[]) obj)[0]).intValue() == 4) {
                                WebLoadEvent.this.closeWap2AppBlockDialog(false);
                            }
                            return false;
                        }
                    };
                    this.mAdaWebview.obtainApp().registerSysEventListener(this.mWap2AppBlockDialogSysEventListener, ISysEventListener.SysEventType.onKeyUp);
                    this.mWap2AppBlockDialog = createInvokeExecutor;
                }
            }
            return true;
        }
        this.mAdaWebview.resetPlusLoadSaveData();
        return false;
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
        String obtainConfigProperty = this.mAdaWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNTRUSTEDCA);
        if (PdrUtil.isEquals(obtainConfigProperty, "refuse")) {
            sslErrorHandler.cancel();
            return;
        }
        if (PdrUtil.isEquals(obtainConfigProperty, "warning")) {
            Context context = webView.getContext();
            final AlertDialog create = new AlertDialog.Builder(context).create();
            create.setIcon(R.drawable.ic_secure);
            create.setTitle("安全警告");
            create.setCanceledOnTouchOutside(false);
            String url = Build.VERSION.SDK_INT >= 14 ? sslError.getUrl() : null;
            create.setMessage(TextUtils.isEmpty(url) ? "此站点安全证书存在问题,是否继续?" : url + "\n此站点安全证书存在问题,是否继续?");
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == -2) {
                        create.cancel();
                        create.dismiss();
                    } else if (i == -3) {
                        sslError.getCertificate().getIssuedBy();
                    } else if (i == -1) {
                        sslErrorHandler.proceed();
                        create.dismiss();
                    }
                }
            };
            create.setButton(-2, context.getResources().getString(R.string.cancel), onClickListener);
            create.setButton(-1, context.getResources().getString(R.string.ok), onClickListener);
            create.show();
            return;
        }
        sslErrorHandler.proceed();
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView webView, int i, String str, final String str2) {
        Logger.e(TAG, "onReceivedError description=" + str + ";failingUrl=" + str2 + ";errorCode=" + i);
        this.mAdaWebview.dispatchWebviewStateEvent(5, str);
        this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_FAILED, this.mAdaWebview);
        this.mAdaWebview.hasErrorPage = true;
        this.mAdaWebview.errorPageUrl = str2;
        final IApp obtainApp = this.mAdaWebview.mFrameView.obtainApp();
        if (obtainApp != null) {
            try {
                if (BaseInfo.isWap2AppAppid(obtainApp.obtainAppId()) && this.mAdaWebview.mFrameView.getFrameType() == 2) {
                    final AlertDialog create = new AlertDialog.Builder(this.mAdaWebview.getContext()).create();
                    create.setTitle("提示");
                    create.setCanceledOnTouchOutside(false);
                    create.setMessage("无法连接服务器，请检查网络设置");
                    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            if (i2 == -2) {
                                WebLoadEvent.this.mAdaWebview.getActivity().startActivity(new Intent("android.settings.SETTINGS"));
                            } else if (i2 == -3) {
                                Logger.e(WebLoadEvent.TAG, "onReceivedError try again");
                                IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(WebLoadEvent.this.mAdaWebview.getActivity());
                                if (iActivityHandler != null && BaseInfo.isShowTitleBar(WebLoadEvent.this.mAdaWebview.getActivity())) {
                                    iActivityHandler.updateParam("setProgressView", null);
                                }
                                WebLoadEvent.this.mAdaWebview.loadUrl(str2);
//                                WebLoadEvent.this.mAdaWebview.loadUrl("file:///android_asset/apps/H5057CD3A/www/login.html");
                            } else if (i2 == -1) {
                                Activity activity = WebLoadEvent.this.mAdaWebview.getActivity();
                                DCloudAdapterUtil.getIActivityHandler(activity).updateParam("closewebapp", activity);
                            }
                            create.dismiss();
                        }
                    };
                    create.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.4
                        @Override // android.content.DialogInterface.OnKeyListener
                        public boolean onKey(DialogInterface dialogInterface, int i2, KeyEvent keyEvent) {
                            if (i2 != 4) {
                                return false;
                            }
                            create.dismiss();
                            Activity activity = WebLoadEvent.this.mAdaWebview.getActivity();
                            DCloudAdapterUtil.getIActivityHandler(activity).updateParam("closewebapp", activity);
                            return false;
                        }
                    });
                    create.setButton(-2, "设置网络", onClickListener);
                    create.setButton(-3, "重试", onClickListener);
                    create.setButton(-1, "退出", onClickListener);
                    create.show();
                    obtainApp.registerSysEventListener(new ISysEventListener() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.5
                        @Override // io.dcloud.common.DHInterface.ISysEventListener
                        public boolean onExecute(SysEventType sysEventType, Object obj) {
                            if (SysEventType.onResume != sysEventType) {
                                return false;
                            }
                            WebLoadEvent.this.mAdaWebview.mWebViewImpl.postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.5.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Logger.e(WebLoadEvent.TAG, "onReceivedError 500ms retry after the onResume");
                                    IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(WebLoadEvent.this.mAdaWebview.getActivity());
                                    if (iActivityHandler != null && BaseInfo.isShowTitleBar(WebLoadEvent.this.mAdaWebview.getActivity())) {
                                        iActivityHandler.updateParam("setProgressView", null);
                                    }
                                    WebLoadEvent.this.mAdaWebview.loadUrl(str2);
//                                    WebLoadEvent.this.mAdaWebview.loadUrl("file:///android_asset/apps/H5057CD3A/www/login.html");
                                }
                            }, 500L);
                            obtainApp.unregisterSysEventListener(this, sysEventType);
                            return false;
                        }
                    }, ISysEventListener.SysEventType.onResume);
                    Logger.e(TAG, "onReceivedError do clearHistory");
                    this.mAdaWebview.clearHistory();
                } else {
                    String errorPage = getErrorPage();
                    if (!"none".equals(errorPage)) {
                        Logger.e(TAG, "onReceivedError  load errorPage " + errorPage);
                        this.mAdaWebview.loadUrl(errorPage);
//                        this.mAdaWebview.loadUrl("file:///android_asset/apps/H5057CD3A/www/login.html");
                    } else {
                        this.mAdaWebview.hasErrorPage = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String getErrorPage() {
        return ViewOptions.checkOptionsErrorPage(this.mAdaWebview, this.mAdaWebview.mFrameView.obtainFrameOptions().errorPage);
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        Logger.i(TAG, "onPageStarted url=" + str);
        this.mAdaWebview.onPageStarted();
        printOpenLog(webView, str);
        if (this.mAdaWebview.hadClearHistory(str)) {
            return;
        }
        this.mAdaWebview.directLoadPreLoadJsFile();
        listenPlusInjectTimeout(webView, str, "onPageStarted");
        if (!str.startsWith("data:")) {
            this.mAdaWebview.mWebViewImpl.mUrl = str;
        }
        this.mAdaWebview.resetPlusLoadSaveData();
        if (!PdrUtil.isEmpty(this.mAdaWebview.mWebViewImpl.mUrl)) {
            this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WINDOW_CLOSE, this.mAdaWebview);
        }
        this.mAdaWebview.dispatchWebviewStateEvent(0, str);
        AdaFrameView adaFrameView = this.mAdaWebview.mFrameView;
        this.mAdaWebview.mFrameView.dispatchFrameViewEvents("loading", this.mAdaWebview);
        if (adaFrameView.obtainStatus() != 3) {
            adaFrameView.onPreLoading();
        }
        super.onPageStarted(webView, str, bitmap);
        if (this.mAdaWebview.mFrameView.getFrameType() == 3) {
            try {
                if (this.mWaitingForWapPage == null) {
                    this.mWaitingForWapPage = new ProgressBar(this.mAdaWebview.getContext());
                    int i = AndroidResources.mResources.getDisplayMetrics().widthPixels;
                    int i2 = AndroidResources.mResources.getDisplayMetrics().heightPixels;
                    int parseInt = PdrUtil.parseInt("7%", i, -1);
                    ((ViewGroup) this.mAdaWebview.obtainFrameView().obtainMainView()).addView(this.mWaitingForWapPage, new AbsoluteLayout.LayoutParams(parseInt, parseInt, (i - parseInt) / 2, (i2 - parseInt) / 2));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void printOpenLog(WebView webView, String str) {
        IApp obtainApp;
        String url = webView.getUrl();
        if (!BaseInfo.isBase(webView.getContext()) || TextUtils.isEmpty(str) || TextUtils.isEmpty(url) || (obtainApp = this.mAdaWebview.mFrameView.obtainApp()) == null || str.startsWith(DeviceInfo.HTTP_PROTOCOL) || url.startsWith(DeviceInfo.HTTP_PROTOCOL) || str.startsWith(DeviceInfo.HTTPS_PROTOCOL) || url.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
            return;
        }
        Log.i(AbsoluteConst.HBUILDER_TAG, String.format(AbsoluteConst.OPENLOG, getHBuilderPrintUrl(obtainApp.convert2RelPath(getOriginalUrl(url))), getHBuilderPrintUrl(obtainApp.convert2RelPath(getOriginalUrl(str)))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onLoadPlusJSContent(final WebView webView, final String str, final String str2) {
        if (this.mAdaWebview.mPlusrequire.equals("none")) {
            return false;
        }
        if (this.mAdaWebview.isRealInject(str)) {
            Logger.i("shutao", "all.js已经提前注入JS完成。不需要再注入了" + this.mAdaWebview.getOriginalUrl());
            return true;
        }
        Logger.i("shutao", "plus提前注入JS" + this.mAdaWebview.getOriginalUrl());
        if (this.mAdaWebview.mPlusrequire.equals("later") && str2.equals("onPageFinished")) {
            webView.postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.6
                @Override // java.lang.Runnable
                public void run() {
                    WebLoadEvent.this.mAdaWebview.mPlusInjectTag = str2;
                    WebLoadEvent webLoadEvent = WebLoadEvent.this;
                    webLoadEvent.completeLoadJs(webView, str, str2, new String[]{webLoadEvent.mPlusJS, WebLoadEvent.DIFFERENT_VERSION_JS}, WebLoadEvent.IF_PLUSREADY_TEMPLATE, str);
                }
            }, 2000L);
        } else {
            this.mAdaWebview.mPlusInjectTag = str2;
            completeLoadJs(webView, str, str2, new String[]{this.mPlusJS, DIFFERENT_VERSION_JS}, IF_PLUSREADY_TEMPLATE, str);
        }
        return false;
    }

    private void listenPlusInjectTimeout(final WebView webView, final String str, final String str2) {
        if (this.mAdaWebview.mPlusrequire.equals("none")) {
            return;
        }
        Runnable runnable = this.Timeout_Plus_Inject_Runnable;
        if (runnable != null) {
            MessageHandler.removeCallbacks(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.7
            @Override // java.lang.Runnable
            public void run() {
                if (WebLoadEvent.this.mAdaWebview.isRealInject(str)) {
                    return;
                }
                Logger.i("WebViewData", "listenPlusInjectTimeout url=" + str);
                WebLoadEvent.this.onLoadPlusJSContent(webView, str, "plus_inject_timeout_" + str2);
                WebLoadEvent.this.mAdaWebview.mPreloadJsLoading = false;
                WebLoadEvent.this.Timeout_Plus_Inject_Runnable = null;
            }
        };
        this.Timeout_Plus_Inject_Runnable = runnable2;
        MessageHandler.postDelayed(runnable2, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void listenPageFinishTimeout(final WebView webView, final String str, final String str2) {
        if (this.mAdaWebview.mLoaded && this.mAdaWebview.isRealInject(str)) {
            injectScript(webView, str, str2);
            return;
        }
        Runnable runnable = this.Timeout_Page_Finish_Runnable;
        if (runnable != null) {
            MessageHandler.removeCallbacks(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.8
            @Override // java.lang.Runnable
            public void run() {
                if (WebLoadEvent.this.mAdaWebview.mLoaded || !WebLoadEvent.this.mAdaWebview.isRealInject(str)) {
                    return;
                }
                WebLoadEvent.this.injectScript(webView, str, "page_finished_timeout_" + str2);
                WebLoadEvent.this.Timeout_Page_Finish_Runnable = null;
            }
        };
        this.Timeout_Page_Finish_Runnable = runnable2;
        MessageHandler.postDelayed(runnable2, 6000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPlusreadyEvent(WebView webView, String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_PLUSREADY));
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(String.format(AbsoluteConst.EVENTS_IFRAME_DOUCMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_PLUSREADY));
        completeLoadJs(webView, str, str2, new String[]{stringBuffer.toString(), stringBuffer2.toString(), "plus.webview.currentWebview().__needTouchEvent__()"}, IF_PLUSREADY_EVENT_TEMPLATE, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void completeLoadJs(final WebView webView, final String str, final String str2, final String[] strArr, final String str3, final Object... objArr) {
        this.mAdaWebview.executeScript(ReceiveJSValue.registerCallback(this.mAdaWebview, String.format(str3, objArr), new ReceiveJSValue.ReceiveJSValueCallback() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.9
            @Override // io.dcloud.common.adapter.ui.ReceiveJSValue.ReceiveJSValueCallback
            public String callback(JSONArray jSONArray) {
                try {
                    int i = jSONArray.getInt(1);
                    if (i == 0 && !PdrUtil.isEquals(str2, "onPageFinished")) {
                        WebLoadEvent.this.completeLoadJs(webView, str, str2, strArr, str3, objArr);
                        return null;
                    }
                    if (1 != i) {
                        return null;
                    }
                    for (String str4 : strArr) {
                        WebLoadEvent.this.mAdaWebview.executeScript(str4);
                    }
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUpdatePlusData(WebView webView, String str, String str2) {
        AdaWebview adaWebview = this.mAdaWebview;
        adaWebview.executeScript(adaWebview.getScreenAndDisplayJson(adaWebview));
        onExecuteEvalJSStatck(webView, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPreloadJSContent(final WebView webView, final String str, final String str2) {
        if (this.mAdaWebview.mPreloadJsLoaded) {
            Logger.i("shutao", "mPreloadJs 已经提前注入JS完成。不需要再注入了" + this.mAdaWebview.getOriginalUrl());
        } else {
            this.mAdaWebview.loadPreLoadJsFile(new AdaWebview.IFExecutePreloadJSContentCallBack() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.10
                @Override // io.dcloud.common.adapter.ui.AdaWebview.IFExecutePreloadJSContentCallBack
                public void callback(String str3, String str4) {
                    Logger.i("shutao", "注入mPreloadJs ;" + WebLoadEvent.this.mAdaWebview.getOriginalUrl() + ";" + str3);
                    if (PdrUtil.isEmpty(str4)) {
                        return;
                    }
                    WebLoadEvent.this.mAdaWebview.mPreloadJsLoaded = true;
                    WebLoadEvent.this.completeLoadJs(webView, str, str2, new String[]{str4}, WebLoadEvent.IF_PRELOAD_TEMPLATE, str3);
                }
            });
        }
    }

    private void onLoadCssContent() {
        if (this.mAdaWebview.mIsAdvanceCss) {
            Logger.i("shutao", "已经提前注入CSS完成。不需要再注入了" + this.mAdaWebview.getOriginalUrl());
        } else if (this.mAdaWebview.loadCssFile()) {
            Logger.i("shutao", "提前注入CSS完成" + this.mAdaWebview.getOriginalUrl());
        }
    }

    private void onExecuteEvalJSStatck(WebView webView, String str, String str2) {
        String str3 = this.mAdaWebview.get_eval_js_stack();
        if (PdrUtil.isEmpty(str3)) {
            return;
        }
        completeLoadJs(webView, str, str2, new String[]{str3}, IF_LOAD_TEMPLATE, str);
    }

    private void startTryLoadAllJSContent(WebView webView, String str, String str2) {
        loadAllJSContent(webView, str, str2);
    }

    private void loadAllJSContent(WebView webView, String str, String str2) {
        if (onLoadPlusJSContent(webView, str, str2)) {
            injectScript(webView, str, str2);
        }
    }

    void injectScript(final WebView webView, final String str, final String str2) {
        if (str2.equals("onPageFinished") && this.mAdaWebview.mPlusrequire.equals("later")) {
            webView.postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.11
                @Override // java.lang.Runnable
                public void run() {
                    WebLoadEvent.this.onPreloadJSContent(webView, str, str2);
                    WebLoadEvent.this.onPlusreadyEvent(webView, str, str2);
                }
            }, 2000L);
        } else {
            onPreloadJSContent(webView, str, str2);
            onPlusreadyEvent(webView, str, str2);
        }
        onLoadCssContent();
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String str) {
        boolean z;
        Logger.d(TAG, "onPageFinished=" + str);
        if (this.mAdaWebview.hadClearHistory(str)) {
            this.mAdaWebview.hasErrorPage = false;
            return;
        }
        this.mAdaWebview.mWebViewImpl.mScale = webView.getScale();
        if (this.mAdaWebview.hasErrorPage) {
            String errorPage = getErrorPage();
            if (!PdrUtil.isEquals(str, errorPage) && (!"data:text/html,chromewebdata".equals(str) || !"none".equals(errorPage))) {
                return;
            } else {
                z = true;
            }
        } else {
            z = false;
        }
        if (this.mAdaWebview.unReceiveTitle) {
            Logger.i(TAG, "onPageFinished will exe titleUpdate =" + str);
            this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_TITLE_UPDATE, this.mAdaWebview.mWebViewImpl.getTitle());
            this.mAdaWebview.unReceiveTitle = false;
        }
        CookieSyncManager.getInstance().sync();
        Logger.i("shutao", "onPageFinished" + this.mAdaWebview.getOriginalUrl());
        this.mAdaWebview.dispatchWebviewStateEvent(1, str);
        onLoadPlusJSContent(webView, str, "onPageFinished");
        if (this.mAdaWebview.isRealInject(str)) {
            injectScript(webView, str, "onPageFinished");
        }
        this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_LOADED, this.mAdaWebview);
        if (z) {
            this.mAdaWebview.executeScript(String.format(ERROR_TEMPLATE, "error", this.mAdaWebview.getOriginalUrl(), this.mAdaWebview.errorPageUrl));
            this.mAdaWebview.errorPageUrl = null;
            this.mAdaWebview.hasErrorPage = false;
        }
        AdaFrameView adaFrameView = this.mAdaWebview.mFrameView;
        AdaFrameView adaFrameView2 = adaFrameView;
        if (adaFrameView2.mViewOptions.hasBackground()) {
            adaFrameView2.mViewOptions.mWebviewScale = webView.getScale();
        }
        if (adaFrameView.obtainStatus() != 3) {
            adaFrameView.onPreShow(null);
        }
        if (!this.mAdaWebview.mLoaded) {
            this.mAdaWebview.mLoaded = true;
            this.mAdaWebview.mPlusLoaded = true;
        }
        super.onPageFinished(webView, str);
        if (this.mAdaWebview.justClearOption && !str.startsWith("data:")) {
            Logger.d(TAG, "onPageFinished mWebViewImpl.clearHistory url=" + str);
            this.mAdaWebview.mWebViewImpl.clearHistory();
            this.mAdaWebview.justClearOption = false;
        }
//        this.mAdaWebview.mWebViewImpl.webSettings.setCacheMode(this.mAdaWebview.getCacheMode());
        this.mAdaWebview.mWebViewImpl.webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        OnPageFinishedCallack onPageFinishedCallack = this.mPageFinishedCallack;
        if (onPageFinishedCallack != null) {
            onPageFinishedCallack.onLoad();
        }
        if (this.mWaitingForWapPage != null) {
            try {
                ((ViewGroup) this.mAdaWebview.obtainFrameView().obtainMainView()).removeView(this.mWaitingForWapPage);
                this.mWaitingForWapPage = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.webkit.WebViewClient
    public void onLoadResource(WebView webView, String str) {
        if (this.printLog) {
            Logger.i(TAG, "onLoadResource url=" + str);
        }
        printResourceLog(webView, this.mAdaWebview.mFrameView.obtainApp(), webView.getUrl(), str);
        IFrameView obtainFrameView = this.mAdaWebview.obtainFrameView();
        if (obtainFrameView.obtainStatus() != 3) {
            obtainFrameView.onLoading();
        }
        if (this.mAdaWebview.checkResourceLoading(str)) {
            this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_LISTEN_RESOURCE_LOADING, "{url:'" + str + "'}");
        }
        this.mAdaWebview.dispatchWebviewStateEvent(2, str);
        super.onLoadResource(webView, str);
    }

    public void setPageFinishedCallack(OnPageFinishedCallack onPageFinishedCallack) {
        this.mPageFinishedCallack = onPageFinishedCallack;
    }

    private String getUrlByFilePath(String str) {
        try {
            Activity activity = this.mAdaWebview.obtainApp().getActivity();
            if (activity != null) {
                return DCloudAdapterUtil.getIActivityHandler(activity).getUrlByFilePath(this.mAppid, str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private void showLoading() {
        this.mAdaWebview.mWebViewImpl.post(new Runnable() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.12
            @Override // java.lang.Runnable
            public void run() {
                WebLoadEvent.this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_SHOW_LOADING, WebLoadEvent.this.mAdaWebview.mFrameView);
            }
        });
        this.mShowLoadingTime = System.currentTimeMillis();
    }

    private void hideLoading() {
        this.mAdaWebview.mWebViewImpl.post(new Runnable() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.13
            @Override // java.lang.Runnable
            public void run() {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - WebLoadEvent.this.mShowLoadingTime < 1000) {
                    WebLoadEvent.this.mAdaWebview.mWebViewImpl.postDelayed(this, currentTimeMillis - WebLoadEvent.this.mShowLoadingTime);
                } else {
                    WebLoadEvent.this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_HIDE_LOADING, WebLoadEvent.this.mAdaWebview.mFrameView);
                }
            }
        });
    }

    private void sendStatistics(String str, Context context, String str2, String str3, IApp iApp) {
        String str4;
        try {
            str4 = URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str4 = "";
        }
        String statisticsUrl = DataInterface.getStatisticsUrl(str, TelephonyUtil.getIMEI(context), 9, TestUtil.PointTime.getBaseVer(context), str4);
        String originalUrl = getOriginalUrl(str3);
        if (str2.startsWith(DeviceInfo.HTTP_PROTOCOL) || str2.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
            return;
        }
        NetTool.httpGet(statisticsUrl + AbsoluteConst.STREAMAPP_KEY_NET + NetworkTypeUtil.getNetworkType(context) + "&sr=" + PdrUtil.encodeURL(getHBuilderPrintUrl(iApp.convert2RelPath(getOriginalUrl(str2)))) + "&sh=" + PdrUtil.encodeURL(getHBuilderPrintUrl(iApp.convert2RelPath(originalUrl))));
    }

    private void printResourceLog(WebView webView, IApp iApp, String str, String str2) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str) || webView == null || iApp == null || !BaseInfo.isBase(webView.getContext()) || str.equalsIgnoreCase(str2) || TextUtils.isEmpty(str2)) {
            return;
        }
        if (this.mClearCache && !this.mLastPageUrl.equalsIgnoreCase(str)) {
            webView.clearCache(true);
        }
        this.mLastPageUrl = str;
        String originalUrl = getOriginalUrl(str);
        if (str2.startsWith(DeviceInfo.HTTP_PROTOCOL) || str2.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
            return;
        }
        Log.i(AbsoluteConst.HBUILDER_TAG, String.format(AbsoluteConst.RESOURCELOG, getHBuilderPrintUrl(iApp.convert2RelPath(originalUrl)), getHBuilderPrintUrl(iApp.convert2RelPath(getOriginalUrl(str2)))));
    }

    public static String getOriginalUrl(String str) {
        return TextUtils.isEmpty(str) ? "" : str.startsWith(DeviceInfo.FILE_PROTOCOL) ? str.substring(7) : str;
    }

    public static String getHBuilderPrintUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String str2 = BaseInfo.REL_PRIVATE_WWW_DIR + File.separator;
        return str.startsWith(str2) ? str.substring(str2.length()) : str;
    }

    private static boolean needDecode(String str, IApp iApp) {
        if (TextUtils.isEmpty(str) || iApp == null) {
            return false;
        }

        Map<String, String> map = EncryptionSingleton.getInstance().get(iApp.obtainAppId());
        if (map == null || map.isEmpty()) {
            return false;
        }

        String key = getHBuilderPrintUrl(iApp.convert2RelPath(getOriginalUrl(str)));
        return map.containsKey(key);
    }

    private static String getAESKey(String str, IApp iApp) {
        if (TextUtils.isEmpty(str) || iApp == null) {
            return "";
        }

        Map<String, String> map = EncryptionSingleton.getInstance().get(iApp.obtainAppId());
        if (map == null || map.isEmpty()) {
            return "";
        }

        String key = getHBuilderPrintUrl(iApp.convert2RelPath(getOriginalUrl(str)));
        return map.containsKey(key) ? map.get(key) : "";
    }

    public static String getMimeType(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        String mimeTypeFromExtension = fileExtensionFromUrl != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl) : null;
        return TextUtils.isEmpty(mimeTypeFromExtension) ? "text/plain" : mimeTypeFromExtension;
    }

    private boolean checkJsFile(String str) {
        return (TextUtils.isEmpty(str) || !str.contains(".js") || str.contains(".jsp")) ? false : true;
    }

    private boolean checkCssFile(String str) {
        return !TextUtils.isEmpty(str) && str.contains(".css");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class CatchFile {
        File mFile = null;
        String mEncoding = null;
        String mContentType = null;
        boolean mExist = false;

        CatchFile() {
        }
    }

    private String getCacheLocalFilePath(String str, String str2) {
        if (this.TYPE_JS.equals(str2)) {
            return this.mAdaWebview.obtainApp().obtainAppTempPath() + "__plus__cache__/" + Md5Utils.md5(str) + ".js";
        }
        return this.mAdaWebview.obtainApp().obtainAppTempPath() + "__plus__cache__/" + Md5Utils.md5(str) + ".css";
    }

    private CatchFile getUrlFile(String str, String str2) throws Exception {
        String cacheLocalFilePath = getCacheLocalFilePath(str, str2);
        try {
            if (DHFile.isExist(cacheLocalFilePath)) {
                CatchFile catchFile = new CatchFile();
                catchFile.mFile = new File(cacheLocalFilePath);
                catchFile.mExist = catchFile.mFile.exists();
                return catchFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(str);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection instanceof HttpsURLConnection) {
                try {
                    SSLContext sSLContext = SSLContext.getInstance("TLSv1");
                    sSLContext.init(null, new TrustManager[]{new DCloudTrustManager()}, new SecureRandom());
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sSLContext.getSocketFactory());
                    ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(new HostnameVerifier() { // from class: io.dcloud.common.adapter.ui.WebLoadEvent.14
                        @Override // javax.net.ssl.HostnameVerifier
                        public boolean verify(String str3, SSLSession sSLSession) {
                            return true;
                        }
                    });
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
            httpURLConnection.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            int responseCode = httpURLConnection.getResponseCode();
            String contentType = httpURLConnection.getContentType();
            if (!TextUtils.isEmpty(contentType) && (((str2.equals(this.TYPE_JS) && contentType.contains("javascript")) || (str2.equals(this.TYPE_CSS) && (contentType.contains("text/css") || url.getPath().endsWith(".css")))) && (responseCode == 200 || responseCode == 206))) {
                InputStream inputStream = httpURLConnection.getInputStream();
                boolean writeFile = DHFile.writeFile(inputStream, cacheLocalFilePath);
                IOUtil.close(inputStream);
                if (writeFile) {
                    CatchFile catchFile2 = new CatchFile();
                    catchFile2.mFile = new File(cacheLocalFilePath);
                    catchFile2.mExist = catchFile2.mFile.exists();
                    catchFile2.mEncoding = httpURLConnection.getContentEncoding();
                    catchFile2.mContentType = contentType;
                    return catchFile2;
                }
                File file = new File(cacheLocalFilePath);
                if (file.exists()) {
                    file.delete();
                }
            }
            return null;
        } catch (IOException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x016f A[Catch: Exception -> 0x01ac, TRY_LEAVE, TryCatch #2 {Exception -> 0x01ac, blocks: (B:10:0x002f, B:12:0x0035, B:14:0x003b, B:16:0x0041, B:19:0x0050, B:21:0x005a, B:22:0x0067, B:24:0x006d, B:26:0x0077, B:27:0x0082, B:29:0x0086, B:31:0x0090, B:33:0x009a, B:34:0x00c5, B:61:0x00c9, B:62:0x00d2, B:64:0x00d8, B:66:0x00dc, B:36:0x00e4, B:38:0x00ec, B:39:0x00f1, B:41:0x00f9, B:42:0x00fe, B:44:0x0111, B:47:0x016f, B:69:0x00e1, B:71:0x0122, B:73:0x012a, B:75:0x0132, B:76:0x015e, B:78:0x0164), top: B:9:0x002f, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0178 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0175  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private WebResourceResponse downloadResponseInjection(WebResourceResponse r15, String r16, String r17, String r18, String r19) {
        /*
            Method dump skipped, instructions count: 599
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.WebLoadEvent.downloadResponseInjection(android.webkit.WebResourceResponse, java.lang.String, java.lang.String, java.lang.String, java.lang.String):android.webkit.WebResourceResponse");
    }
}
