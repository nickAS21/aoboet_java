package io.dcloud.feature.internal.sdk;

import android.app.Activity;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.common_b.common_b_a.PermissionControler;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class SDK {
    public static final String ANDROID_ASSET = "file:///android_asset/";
    public static final String DEFAULT_APPID = "Default_Appid";
    static ICore sCore;

    /* loaded from: classes.dex */
    public enum IntegratedMode {
        WEBVIEW,
        WEBAPP,
        RUNTIME
    }

    public static final void loadCustomPath(String str) {
    }

    private SDK() {
    }

    public static void initSDK(ICore iCore) {
        sCore = iCore;
    }

    public static Object dispatchEvent(IMgr.MgrType mgrType, int i, Object obj) {
        return sCore.dispatchEvent(mgrType, i, obj);
    }

    public static void setGlobalDocumentsPath(String str) {
        BaseInfo.sDocumentFullPath = PdrUtil.appendByDeviceRootDir(str);
    }

    public static void setGlobalDownloadsPath(String str) {
        BaseInfo.sDownloadFullPath = PdrUtil.appendByDeviceRootDir(str);
    }

    private static IApp createUnstrictWebApp(String str, String str2, String str3, byte b) {
        return (IApp) sCore.dispatchEvent(IMgr.MgrType.AppMgr, 8, new String[]{str, str2, str3, String.valueOf((int) b)});
    }

    public static void attach(ViewGroup viewGroup, IApp iApp, IWebview iWebview) {
        sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 16, new Object[]{viewGroup, iApp, iWebview, new ViewGroup.LayoutParams(-1, -1)});
    }

    public static void attach(ViewGroup viewGroup, IApp iApp, IWebview iWebview, ViewGroup.LayoutParams layoutParams) {
        sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 16, new Object[]{viewGroup, iApp, iWebview, layoutParams});
    }

    public static void attach(ViewGroup viewGroup, IWebview iWebview) {
        attach(viewGroup, iWebview.obtainFrameView().obtainApp(), iWebview);
    }

    public static IWebview obatinFirstPage(IApp iApp) {
        return ((IFrameView) sCore.dispatchEvent(IMgr.MgrType.AppMgr, 9, new Object[]{iApp, null})).obtainWebView();
    }

    public static IWebview createWebview(Activity activity, String str, IWebviewStateListener iWebviewStateListener) {
        return createWebview(activity, str, DEFAULT_APPID, iWebviewStateListener);
    }

    public static IWebview createWebview(Activity activity, String str, String str2, IWebviewStateListener iWebviewStateListener) {
        if (PdrUtil.isEmpty(str2)) {
            str2 = DEFAULT_APPID;
        }
        String str3 = str2;
        return createWebview(activity, str, null, BaseInfo.sBaseFsAppsPath + str3 + File.separatorChar + BaseInfo.REAL_PRIVATE_DOC_DIR, str3, null, iWebviewStateListener);
    }

    public static IWebview createWebview(Activity activity, String str, String str2, String str3, String str4, String str5, IWebviewStateListener iWebviewStateListener) {
        IApp createUnstrictWebApp = createUnstrictWebApp(str4, str2, str, str.startsWith(ANDROID_ASSET) ? (byte) 1 : (byte) 0);
        PermissionControler.a(str4);
        createUnstrictWebApp.setLaunchPageStateListener(iWebviewStateListener);
        createUnstrictWebApp.setAppDocPath(str3);
        createUnstrictWebApp.setConfigProperty("name", str5);
        createUnstrictWebApp.addAllFeaturePermission();
        createUnstrictWebApp.setWebAppActivity(activity);
        createUnstrictWebApp.setWebAppIntent(activity.getIntent());
        return obatinFirstPage(createUnstrictWebApp, iWebviewStateListener);
    }

    public static IWebview obatinFirstPage(IApp iApp, IWebviewStateListener iWebviewStateListener) {
        return ((IFrameView) sCore.dispatchEvent(IMgr.MgrType.AppMgr, 9, new Object[]{iApp, iWebviewStateListener})).obtainWebView();
    }

    public static IApp startWebApp(Activity activity, String str, String str2, IWebviewStateListener iWebviewStateListener, IOnCreateSplashView iOnCreateSplashView) {
        IApp iApp = (IApp) sCore.dispatchEvent(IMgr.MgrType.AppMgr, 14, str);
        if (iApp != null) {
            iApp.setLaunchPageStateListener(iWebviewStateListener);
            sCore.dispatchEvent(null, 2, new Object[]{activity, iApp.obtainAppId(), str2, iOnCreateSplashView});
        }
        return iApp;
    }

    public static void stopWebApp(IApp iApp) {
        sCore.dispatchEvent(IMgr.MgrType.AppMgr, 10, iApp.obtainAppId());
    }

    public static void requestFeature(String str, String str2, boolean z) {
        sCore.dispatchEvent(IMgr.MgrType.FeatureMgr, 6, new String[]{str, str2, String.valueOf(z)});
    }

    public static void requestAllFeature() {
        sCore.dispatchEvent(IMgr.MgrType.FeatureMgr, 7, null);
    }

    public static void registerJsApi(String str, String str2, String str3) {
        sCore.dispatchEvent(IMgr.MgrType.FeatureMgr, 5, new String[]{str, str2, str3});
    }

    private static IWebview createWebView(IApp iApp, String str, JSONObject jSONObject, IFrameView iFrameView, IEventCallback iEventCallback) {
        IWebview obtainWebView = ((IFrameView) sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 3, new Object[]{0, iApp, new Object[]{str, jSONObject}, iFrameView, iEventCallback})).obtainWebView();
        obtainWebView.loadUrl(str);
//        str = "file:///android_asset/apps/H5057CD3A/www/login.html";
        obtainWebView.loadUrl(str);
        return obtainWebView;
    }

    public static void closeWebView(IWebview iWebview) {
        ((AdaFrameItem) iWebview.obtainFrameView()).getAnimOptions().mOption = (byte) 1;
        sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 2, iWebview.obtainFrameView());
    }

    public static void popWebView(IWebview iWebview) {
        ((AdaFrameItem) iWebview.obtainFrameView()).getAnimOptions().mOption = (byte) 1;
        sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 21, iWebview.obtainFrameView());
    }

    public static ArrayList<IWebview> obtainAllIWebview(String str) {
        ArrayList arrayList = (ArrayList) sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 6, str);
        if (arrayList == null || arrayList.size() <= 0) {
            return null;
        }
        ArrayList<IWebview> arrayList2 = new ArrayList<>();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((IFrameView) it.next()).obtainWebView());
        }
        return arrayList2;
    }

    public static String obtainCurrentRunnbingAppId() {
        return String.valueOf(sCore.dispatchEvent(IMgr.MgrType.AppMgr, 11, null));
    }

    public static IWebview obtainWebview(String str, String str2) {
        Iterator<IWebview> it = obtainAllIWebview(str).iterator();
        while (it.hasNext()) {
            IWebview next = it.next();
            if (PdrUtil.isEquals(str2, next.getWebviewUUID())) {
                return next;
            }
        }
        return null;
    }

    public static ArrayList<IWebview> obtainAllIWebview() {
        return obtainAllIWebview(obtainCurrentRunnbingAppId());
    }

    public static IApp obtainCurrentApp() {
        return (IApp) sCore.dispatchEvent(IMgr.MgrType.AppMgr, 6, obtainCurrentRunnbingAppId());
    }
}
