package io.dcloud.common.adapter.ui;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class WebJsEvent extends WebChromeClient {
    static final FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(-1, -1, 17);
    public static final int FILECHOOSER_RESULTCODE = 1;
    static final String TAG = "webview";
    private boolean isStreamApp;
    AdaWebview mAdaWebview;
    View mCustomView;
    CustomViewCallback mCustomViewCallback;
    ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mUploadMessage21Level;
    private boolean rptJsErr;
    private boolean mScreemOrientationChanged = false;
    private boolean mDefaultTitleBarVisibility = false;
    private int mDefaultScreemOrientation = -2;
    DialogListener mListener = null;

    private boolean isUrlWhiteListed(String str) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebJsEvent(AdaWebview adaWebview) {
        this.mAdaWebview = null;
        this.rptJsErr = true;
        this.isStreamApp = false;
        this.mAdaWebview = adaWebview;
        this.isStreamApp = adaWebview.obtainApp().isStreamApp();
        this.rptJsErr = BaseInfo.getCmitInfo(BaseInfo.sLastRunApp).rptJse;
    }

    private boolean isCallbackId(String str) {
        return str != null && str.startsWith(IApp.ConfigProperty.CONFIG_PLUS);
    }

    private void handleMessage(final JsPromptResult jsPromptResult, final AdaWebview adaWebview, final String str, final String str2, String str3, final boolean z) {
        JSONArray jSONArray;
        final JSONArray createJSONArray = JSONUtil.createJSONArray(str3);
        if (this.isStreamApp) {
            try {
                final JSONObject optJSONObject = createJSONArray.optJSONObject(createJSONArray.length() - 1);
                if (optJSONObject != null && isCallbackId(optJSONObject.optString("cbid"))) {
                    final IApp obtainApp = adaWebview.obtainApp();
                    final String[] strArr = (String[]) adaWebview.obtainFrameView().obtainWindowMgr().processEvent(IMgr.MgrType.AppMgr, 15, new Object[]{obtainApp, str, str2, optJSONObject.optString("sid")});
                    boolean parseBoolean = Boolean.parseBoolean(strArr[0]);
                    String str4 = strArr[1];
                    if (parseBoolean) {
                        jSONArray = createJSONArray;
                        try {
                            DialogUtil.showConfirm(adaWebview.getActivity(), obtainApp.obtainAppName(), str4, new String[]{"允许", "不允许"}, new ICallBack() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.1
                                @Override // io.dcloud.common.DHInterface.ICallBack
                                public Object onCallBack(int i, Object obj) {
                                    if (i == -1) {
                                        adaWebview.obtainFrameView().obtainWindowMgr().processEvent(IMgr.MgrType.AppMgr, 16, new Object[]{obtainApp, strArr[2]});
                                        jsPromptResult.confirm(WebJsEvent.this.mAdaWebview.execScript(str, str2, createJSONArray, z));
                                    } else if (i == -2) {
                                        JSUtil.execCallback(adaWebview, optJSONObject.optString("cbid"), DOMException.toJSON(-10, DOMException.MSG_AUTHORIZE_FAILED), JSUtil.ERROR, true, false);
                                        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.1.1
                                            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                                            public void execute(Object obj2) {
                                                jsPromptResult.confirm();
                                            }
                                        }, null);
                                    }
                                    return null;
                                }
                            });
                            return;
                        } catch (Exception unused) {
                        }
                    }
                }
            } catch (Exception unused2) {
            }
        }
        jSONArray = createJSONArray;
        jsPromptResult.confirm(this.mAdaWebview.execScript(str, str2, jSONArray, z));
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, final JsPromptResult jsPromptResult) {
        CharSequence charSequence;
        String __js__call__native__ = "";
        CharSequence charSequence2;
        CharSequence charSequence3;
        int i;
        String str4;
        String string = "";
        String string2 = "";
        boolean z = false;
        AdaWebview adaWebview = null;
        boolean isUrlWhiteListed = isUrlWhiteListed(str);
        if (isUrlWhiteListed && str3 != null && str3.length() > 3 && str3.substring(0, 4).equals("pdr:")) {
            try {
                JSONArray jSONArray = new JSONArray(str3.substring(4));
                string = jSONArray.getString(0);
                string2 = jSONArray.getString(1);
                z = jSONArray.getBoolean(2);
                adaWebview = this.mAdaWebview;
                charSequence2 = "\\\"";
                charSequence3 = JSUtil.QUOTE;
                i = 4;
                str4 = "pdr:";
            } catch (JSONException e) {
                e = e;
                charSequence2 = "\\\"";
                charSequence3 = JSUtil.QUOTE;
                i = 4;
                str4 = "pdr:";
            }
            handleMessage(jsPromptResult, adaWebview, string, string2, str2, z);
            return true;
        }
        if (this.mAdaWebview.mWebViewImpl.mReceiveJSValue_android42 != null && isUrlWhiteListed && str3 != null && str3.length() > 5 && str3.substring(0, 5).equals("sync:")) {
            try {
                JSONArray jSONArray2 = new JSONArray(str3.substring(5));
                __js__call__native__ = this.mAdaWebview.mWebViewImpl.mReceiveJSValue_android42.__js__call__native__(jSONArray2.getString(0), jSONArray2.getString(1));
                charSequence = JSUtil.QUOTE;
            } catch (JSONException e3) {
                charSequence = JSUtil.QUOTE;
            }
            jsPromptResult.confirm(__js__call__native__);
            return true;
        }
        final AlertDialog create = new AlertDialog.Builder(this.mAdaWebview.getActivity()).create();
        create.setMessage(str2);
        create.setTitle(this.mAdaWebview.getAppName());
        final EditText editText = new EditText(this.mAdaWebview.getActivity());
        if (str3 != null) {
            editText.setText(str3);
            editText.setSelection(0, str3.length());
            DeviceInfo.showIME(editText);
        }
        create.setView(editText);
        create.setButton(StringConst.getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                jsPromptResult.confirm(editText.getText().toString());
            }
        });
        create.setButton2(StringConst.getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                jsPromptResult.cancel();
            }
        });
        create.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.4
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i2, KeyEvent keyEvent) {
                if (AndroidResources.sIMEAlive || keyEvent.getAction() != 0 || i2 != 4) {
                    return false;
                }
                create.dismiss();
                jsPromptResult.cancel();
                return true;
            }
        });
        create.show();
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsAlert(WebView webView, String str, String str2, final JsResult jsResult) {
        if (!PdrUtil.isEmpty(this.mAdaWebview.getAppName())) {
            final AlertDialog create = new AlertDialog.Builder(this.mAdaWebview.getActivity()).create();
            create.setTitle(this.mAdaWebview.getAppName());
            create.setMessage(str2);
            if (this.mListener == null) {
                this.mListener = new DialogListener();
            }
            this.mListener.mResult = jsResult;
            create.setButton(StringConst.getString(R.string.ok), this.mListener);
            create.setCanceledOnTouchOutside(false);
            create.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.5
                @Override // android.content.DialogInterface.OnKeyListener
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() != 1 || i != 4) {
                        return false;
                    }
                    jsResult.cancel();
                    create.dismiss();
                    return true;
                }
            });
            create.show();
            return true;
        }
        return super.onJsAlert(webView, str, str2, jsResult);
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsConfirm(WebView webView, String str, String str2, final JsResult jsResult) {
        if (!PdrUtil.isEmpty(this.mAdaWebview.getAppName())) {
            try {
                final AlertDialog create = new AlertDialog.Builder(this.mAdaWebview.getActivity()).create();
                create.setMessage(str2);
                create.setTitle(this.mAdaWebview.getAppName());
                create.setButton(StringConst.getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.6
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsResult.confirm();
                    }
                });
                create.setButton2(StringConst.getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.7
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsResult.cancel();
                    }
                });
                create.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.8
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        jsResult.cancel();
                    }
                });
                create.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.9
                    @Override // android.content.DialogInterface.OnKeyListener
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() != 0 || i != 4) {
                            return false;
                        }
                        jsResult.cancel();
                        create.dismiss();
                        return true;
                    }
                });
                create.show();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return super.onJsConfirm(webView, str, str2, jsResult);
            }
        }
        return super.onJsConfirm(webView, str, str2, jsResult);
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        openFileChooserLogic(valueCallback, null, null);
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback, String str) {
        openFileChooserLogic(valueCallback, str, null);
    }

    @Override // android.webkit.WebChromeClient
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        openFileChooserLogic(null, valueCallback, fileChooserParams.getAcceptTypes() != null ? fileChooserParams.getAcceptTypes()[0] : null, "");
        return true;
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
        openFileChooserLogic(valueCallback, str, str2);
    }

    private void openFileChooserLogic(ValueCallback<Uri> valueCallback, String str, String str2) {
        openFileChooserLogic(valueCallback, null, str, str2);
    }

    private void openFileChooserLogic(ValueCallback<Uri> valueCallback, ValueCallback<Uri[]> valueCallback2, String str, String str2) {
        this.mUploadMessage = valueCallback;
        this.mUploadMessage21Level = valueCallback2;
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        if (!PdrUtil.isEmpty(str)) {
            intent.setType(str);
        } else {
            intent.setType("*/*");
        }
        this.mAdaWebview.obtainFrameView().obtainApp().registerSysEventListener(new ISysEventListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.10
            @Override // io.dcloud.common.DHInterface.ISysEventListener
            public boolean onExecute(SysEventType sysEventType, Object obj) {
                Object[] objArr = (Object[]) obj;
                ((Integer) objArr[0]).intValue();
                int intValue = ((Integer) objArr[1]).intValue();
                WebJsEvent.this.mAdaWebview.obtainFrameView().obtainApp().unregisterSysEventListener(this, SysEventType.onActivityResult);
                if (intValue != 0 && sysEventType == SysEventType.onActivityResult) {
                    Uri data = ((Intent) objArr[2]).getData();
                    Logger.i(WebJsEvent.TAG, "openFileChooserLogic  OnActivityResult url=" + data);
                    if (WebJsEvent.this.mUploadMessage21Level != null) {
                        WebJsEvent.this.mUploadMessage21Level.onReceiveValue(new Uri[]{data});
                    } else if (WebJsEvent.this.mUploadMessage != null) {
                        WebJsEvent.this.mUploadMessage.onReceiveValue(data);
                    }
                    return true;
                }
                if (WebJsEvent.this.mUploadMessage21Level != null) {
                    WebJsEvent.this.mUploadMessage21Level.onReceiveValue(null);
                } else if (WebJsEvent.this.mUploadMessage != null) {
                    WebJsEvent.this.mUploadMessage.onReceiveValue(null);
                }
                return false;
            }
        }, ISysEventListener.SysEventType.onActivityResult);
        try {
            this.mAdaWebview.getActivity().startActivityForResult(intent, 1);
        } catch (Exception unused) {
            Logger.e("openFileChooserLogic Exception");
        }
    }

    @Override // android.webkit.WebChromeClient
    public void onProgressChanged(WebView webView, int i) {
        if (i < 20 && !this.mAdaWebview.unReceiveTitle) {
            this.mAdaWebview.unReceiveTitle = true;
        }
        this.mAdaWebview.dispatchWebviewStateEvent(3, Integer.valueOf(i));
        super.onProgressChanged(webView, i);
    }

    @Override // android.webkit.WebChromeClient
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        handleConsoleMessage(consoleMessage);
        return true;
    }

    private void handleConsoleMessage(ConsoleMessage consoleMessage) {
        String str;
        String message = consoleMessage.message();
        int lineNumber = consoleMessage.lineNumber();
        String sourceId = consoleMessage.sourceId();
        String name = consoleMessage.messageLevel().name();
        consoleMessage.messageLevel();
        if (PdrUtil.isEmpty(sourceId)) {
            str = "[" + name + "] " + message;
        } else {
            try {
                sourceId = this.mAdaWebview.mWebViewImpl.convertRelPath(sourceId);
            } catch (Exception unused) {
            }
            String str2 = message + " at " + sourceId + ":" + lineNumber;
            str = "[" + name + "] " + message + " at " + sourceId + ":" + lineNumber;
        }
        Logger.i("console", str);
    }

    @Override // android.webkit.WebChromeClient
    public void onExceededDatabaseQuota(String str, String str2, long j, long j2, long j3, WebStorage.QuotaUpdater quotaUpdater) {
        Logger.i(TAG, "onExceededDatabaseQuota url=" + str);
        quotaUpdater.updateQuota(j2 * 2);
    }

    public void onReachedMaxAppCacheSize(long j, long j2, WebStorage.QuotaUpdater quotaUpdater) {
        Logger.i(TAG, "onReachedMaxAppCacheSize");
        super.onReachedMaxAppCacheSize(j, j2, quotaUpdater);
    }

    @Override // android.webkit.WebChromeClient
    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        Logger.i(TAG, "onGeolocationPermissionsShowPrompt origin=" + str);
        callback.invoke(str, true, false);
        super.onGeolocationPermissionsShowPrompt(str, callback);
    }

    @Override // android.webkit.WebChromeClient
    public void onGeolocationPermissionsHidePrompt() {
        Logger.i(TAG, "onGeolocationPermissionsHidePrompt");
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override // android.webkit.WebChromeClient
    public void onRequestFocus(WebView webView) {
        Logger.i(TAG, "onRequestFocus");
        super.onRequestFocus(webView);
    }

    @Override // android.webkit.WebChromeClient
    public void onReceivedTouchIconUrl(WebView webView, String str, boolean z) {
        Logger.d("super.onReceivedTouchIconUrl(view, url, precomposed");
        super.onReceivedTouchIconUrl(webView, str, z);
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsBeforeUnload(WebView webView, String str, String str2, JsResult jsResult) {
        return super.onJsBeforeUnload(webView, str, str2, jsResult);
    }

    @Override // android.webkit.WebChromeClient
    public void onReceivedTitle(WebView webView, String str) {
        this.mAdaWebview.unReceiveTitle = false;
        this.mAdaWebview.dispatchWebviewStateEvent(4, str);
        this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_TITLE_UPDATE, str);
        this.mAdaWebview.mWebViewImpl.mPageTitle = str;
        Logger.i(TAG, "onReceivedTitle title=" + str);
        this.mAdaWebview.mLoadCompleted = true;
        super.onReceivedTitle(webView, str);
    }

    @Override // android.webkit.WebChromeClient
    public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
        showCustomView(view, customViewCallback);
    }

    @Override // android.webkit.WebChromeClient
    public void onHideCustomView() {
        hideCustomView();
    }

    @SuppressLint("WrongConstant")
    public void showCustomView(View view, CustomViewCallback customViewCallback) {
        View view2;
        Log.d(TAG, "showing Custom View");
        if (this.mCustomView != null) {
            customViewCallback.onCustomViewHidden();
            return;
        }
        view.setBackgroundDrawable(CanvasHelper.getDrawable());
        if (DeviceInfo.sModel.equals("HUAWEI MT1-U06") || DeviceInfo.sModel.equals("SM-T310") || DeviceInfo.sModel.equals("vivo Y51A")) {
            this.mAdaWebview.obtainFrameView().obtainApp().registerSysEventListener(new ISysEventListener() { // from class: io.dcloud.common.adapter.ui.WebJsEvent.11
                @Override // io.dcloud.common.DHInterface.ISysEventListener
                public boolean onExecute(SysEventType sysEventType, Object obj) {
                    if (sysEventType != SysEventType.onKeyUp || ((Integer) ((Object[]) obj)[0]).intValue() != 4) {
                        return false;
                    }
                    WebJsEvent.this.onHideCustomView();
                    WebJsEvent.this.mAdaWebview.obtainFrameView().obtainApp().unregisterSysEventListener(this, SysEventType.onKeyUp);
                    return true;
                }
            }, ISysEventListener.SysEventType.onKeyUp);
        }
        this.mCustomView = view;
        int i = 0;
        if (!(view instanceof ViewGroup) || ((ViewGroup) view).getChildCount() <= 0) {
            view2 = null;
        } else {
            view2 = ((ViewGroup) this.mCustomView).getChildAt(0);
            if (view2 instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view2;
                if (viewGroup.getChildCount() > 0) {
                    view2 = viewGroup.getChildAt(0);
                }
            }
        }
        if (view2 != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = 17;
            view2.setPadding(0, 0, 0, 0);
            view2.setLayoutParams(layoutParams);
            view2.invalidate();
        }
        this.mCustomViewCallback = customViewCallback;
        ViewGroup viewGroup2 = (ViewGroup) this.mAdaWebview.obtainMainView().getParent();
        viewGroup2.addView(view, COVER_SCREEN_GRAVITY_CENTER);
        viewGroup2.setVisibility(View.VISIBLE);
        viewGroup2.bringToFront();
        this.mAdaWebview.obtainMainView().setVisibility(View.GONE);
        if (this.mAdaWebview.obtainMainView().getContext() instanceof Activity) {
            Activity activity = (Activity) this.mAdaWebview.obtainMainView().getContext();
            if (BaseInfo.isShowTitleBar(activity.getBaseContext())) {
                Object invokeMethod = PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "isTitlebarVisible", null, new Class[]{Activity.class, String.class}, new Object[]{activity, this.mAdaWebview.obtainApp().obtainAppId()});
                this.mDefaultTitleBarVisibility = invokeMethod instanceof Boolean ? Boolean.valueOf(invokeMethod.toString()).booleanValue() : false;
                PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "setTitlebarVisibleNow", null, new Class[]{Activity.class, Boolean.TYPE, Boolean.TYPE}, new Object[]{activity, false, false});
            }
            setStatusBarVisibility(activity, false);
            String webviewProperty = this.mAdaWebview.getWebviewProperty(AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN);
            if ("landscape".equals(webviewProperty)) {
                i = 6;
            } else if (!"landscape-primary".equals(webviewProperty)) {
                if ("landscape-secondary".equals(webviewProperty)) {
                    i = 8;
                } else if ("portrait-primary".equals(webviewProperty)) {
                    i = 1;
                } else {
                    i = "portrait-secondary".equals(webviewProperty) ? 9 : activity.getRequestedOrientation();
                }
            }
            if (activity.getRequestedOrientation() != i) {
                if (-2 == this.mDefaultScreemOrientation) {
                    this.mDefaultScreemOrientation = activity.getRequestedOrientation();
                }
                this.mScreemOrientationChanged = true;
                AdaWebview.ScreemOrientationChangedNeedLayout = true;
                activity.setRequestedOrientation(i);
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void hideCustomView() {
        Log.d(TAG, "Hidding Custom View");
        if (this.mCustomView == null) {
            return;
        }
        this.mAdaWebview.obtainMainView().setVisibility(View.VISIBLE);
        this.mCustomView.setVisibility(View.GONE);
        ((ViewGroup) this.mAdaWebview.obtainMainView().getParent()).removeView(this.mCustomView);
        this.mCustomView = null;
        this.mCustomViewCallback.onCustomViewHidden();
        if (this.mAdaWebview.obtainMainView().getContext() instanceof Activity) {
            Activity activity = (Activity) this.mAdaWebview.obtainMainView().getContext();
            if (BaseInfo.isShowTitleBar(activity.getBaseContext())) {
                PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "setTitlebarVisibleNow", null, new Class[]{Activity.class, Boolean.TYPE, Boolean.TYPE}, new Object[]{activity, Boolean.valueOf(this.mDefaultTitleBarVisibility), true});
            }
            setStatusBarVisibility(activity, true);
            if (this.mScreemOrientationChanged) {
                this.mScreemOrientationChanged = false;
                AdaWebview.ScreemOrientationChangedNeedLayout = true;
                activity.setRequestedOrientation(this.mDefaultScreemOrientation);
            }
        }
    }

    /* loaded from: classes.dex */
    class DialogListener implements DialogInterface.OnClickListener {
        JsResult mResult = null;

        DialogListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            this.mResult.cancel();
        }
    }

    private void setStatusBarVisibility(Activity activity, boolean z) {
        int i = z ? 0 : 1024;
        if (activity != null) {
            activity.getWindow().setFlags(i, 1024);
        }
    }
}
