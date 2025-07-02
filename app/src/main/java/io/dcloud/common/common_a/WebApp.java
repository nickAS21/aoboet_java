package io.dcloud.common.common_a;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IBoot;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISmartUpdate;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.io.UnicodeInputStream;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.common_b.common_b_a.PermissionControler;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.AppStatus;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.CommitDataUtil;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.LauncherUtil;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ShortCutUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.ZipUtils;
import io.dcloud.dcloud_a.EncryptionConstant;
import io.dcloud.dcloud_a.EncryptionSingleton;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudAdapterUtil;



/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: WebApp.java */
/* loaded from: classes.dex */
public class WebApp extends WebAppInfo implements IApp, ISysEventListener {
    public static String TAG_WEBAPP = "webapp";
    ArrayList<String> mFeaturePermissions;
    String mFeaturePermission = null;
    private String at;
    private String au;
    private String av;
    private String aw;
    private String az;
    f b;
    AppMgr g;
    BaseInfo.BaseAppInfo c = null;
    byte d = 1;
    boolean e = false;
    boolean f = false;

    String i = null;
    String j = "";
    String k = null;
    String l = null;
    String m = null;
    String n = null;
    String o = null;
    String p = null;
    String q = null;
    String r = null;
    String s = null;
    boolean t = true;
    boolean u = true;
    boolean v = true;
    boolean w = false;
    boolean x = false;
    boolean y = true;
    private String ai = null;
    boolean z = false;
    private byte aj = 1;
    private boolean ak = false;
    private boolean al = false;
    private boolean am = true;
    private boolean an = true;
    private int ao = 10000;
    private int ap = 0;
    private int aq = 0;
    private String ar = null;
    private String as = null;
    boolean A = false;
    private String ax = null;
    String B = null;
    String C = null;
    String D = "accept";
    String E = null;
    String F = null;
    private String ay = null;
    String G = null;
    HashMap<ISysEventListener.SysEventType, ArrayList<ISysEventListener>> I = null;
    JSONObject J = null;
    JSONObject K = null;
    JSONObject L = null;
    JSONObject M = null;
    JSONObject N = null;
    Intent O = null;
    ISmartUpdate P = null;
    IApp.IAppStatusListener Q = null;
    String R = null;
    private String aA = "none";
    boolean S = false;
    private boolean aB = false;
    private String aC = "default";
    private int aD = -111111;
    long T = 0;
    boolean U = true;
    IWebviewStateListener V = null;
    BroadcastReceiver W = null;
    private boolean aE = false;
    private String aF = AbsoluteConst.INSTALL_OPTIONS_FORCE;
    private String aG = null;
    private String aH = null;
    HashMap<String, Integer> X = null;
    String Y = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(InputStream inputStream) {
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void stopSmartUpdate() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebApp(AppMgr aVar, String str, byte b) {
        this.b = null;
        this.g = null;
        this.mFeaturePermissions = null;
        this.g = aVar;
        a(b);
        this.b = new f();
        this.mFeaturePermissions = new ArrayList<>(2);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setIAppStatusListener(IApp.IAppStatusListener iAppStatusListener) {
        this.Q = iAppStatusListener;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public IApp.IAppStatusListener getIAppStatusListener() {
        return this.Q;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setLaunchPageStateListener(IWebviewStateListener iWebviewStateListener) {
        this.V = iWebviewStateListener;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public IWebviewStateListener obtainLaunchPageStateListener() {
        return this.V;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String getPathByType(byte b) {
        if (b == 0) {
            return obtainAppDataPath();
        }
        if (b == 1) {
            return obtainAppDocPath();
        }
        if (b == 2) {
            return BaseInfo.sDocumentFullPath;
        }
        if (b == 3) {
            return BaseInfo.sDownloadFullPath;
        }
        if (b == -1) {
            return BaseInfo.sBaseResAppsPath + this.mFeaturePermission + "/" + BaseInfo.APP_WWW_FS_DIR;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0451  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0465  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x04dc  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x05b5  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x05c1  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x05a3  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0364  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0327  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x03f2  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x015f  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x027d  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x02d9  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x034a  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x037e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean a(java.io.InputStream r54, java.lang.String r55, org.json.JSONObject r56) {
        /*
            Method dump skipped, instructions count: 1666
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.a.d.a(java.io.InputStream, java.lang.String, org.json.JSONObject):boolean");
    }

    private String b(InputStream inputStream) {
        try {
            return new JSONObject(new String(IOUtil.getBytes(new UnicodeInputStream(inputStream, Charset.defaultCharset().name())))).optString("version");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void p() {
        try {
            int rename = DHFile.rename(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/", BaseInfo.sBaseWap2AppTemplatePath + "wap2app_temp/");
            DHFile.copyDir("data/wap2app", BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/");
            if (rename == 1) {
                ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.common.a.d.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            DHFile.deleteFile(BaseInfo.sBaseWap2AppTemplatePath + "wap2app_temp/");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0082 A[Catch: Exception -> 0x017c, TRY_LEAVE, TryCatch #1 {Exception -> 0x017c, blocks: (B:3:0x0003, B:6:0x000c, B:8:0x002e, B:10:0x0063, B:11:0x0069, B:13:0x0082, B:16:0x00db, B:18:0x0131, B:20:0x0179, B:23:0x0139, B:25:0x0156, B:34:0x00fb, B:35:0x0107, B:30:0x00ee, B:36:0x0108, B:38:0x012a, B:15:0x00ad, B:29:0x00eb), top: B:2:0x0003, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0108 A[Catch: Exception -> 0x017c, TryCatch #1 {Exception -> 0x017c, blocks: (B:3:0x0003, B:6:0x000c, B:8:0x002e, B:10:0x0063, B:11:0x0069, B:13:0x0082, B:16:0x00db, B:18:0x0131, B:20:0x0179, B:23:0x0139, B:25:0x0156, B:34:0x00fb, B:35:0x0107, B:30:0x00ee, B:36:0x0108, B:38:0x012a, B:15:0x00ad, B:29:0x00eb), top: B:2:0x0003, inners: #0, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String q() {
        /*
            Method dump skipped, instructions count: 385
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.a.d.q():java.lang.String");
    }

    private String r() {
        String str = null;
        try {
            InputStream inputStream = DHFile.getInputStream(BaseInfo.sBaseFsAppsPath + "/" + this.mFeaturePermission + "/www/__template.json");
            str = b(inputStream);
            IOUtil.close(inputStream);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return str;
        }
    }

    private void s() {
        BaseInfo.sWap2AppTemplateVersion = q();
        this.k = BaseInfo.sWap2AppTemplateVersion;
        if (BaseInfo.isWap2AppAppid(this.mFeaturePermission)) {
            try {
                String r = r();
                if (TextUtils.isEmpty(r) || BaseInfo.BaseAppInfo.compareVersion(BaseInfo.sWap2AppTemplateVersion, r)) {
                    DHFile.copyFile(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/", BaseInfo.sBaseFsAppsPath + this.mFeaturePermission + "/www/", true, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        if (!BaseInfo.isWap2AppAppid(this.mFeaturePermission) || BaseInfo.isWap2AppCompleted(this.mFeaturePermission)) {
            return;
        }
        ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.common.a.d.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Logger.d("download_manager", "downloadWap2AppZip " + WebApp.this.mFeaturePermission);
                    IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(WebApp.this.getActivity());
                    if (iActivityHandler != null) {
                        iActivityHandler.downloadSimpleFileTask(WebApp.this, StringConst.STREAMAPP_KEY_BASESERVICEURL() + "w2a/rest?" + DataInterface.getUrlBaseData(WebApp.this.getActivity(), WebApp.this.mFeaturePermission, BaseInfo.getCmitInfo(WebApp.this.mFeaturePermission).plusLauncher, BaseInfo.getCmitInfo(WebApp.this.mFeaturePermission).sfd) + "&tv=" + BaseInfo.getCmitInfo(WebApp.this.mFeaturePermission).templateVersion + "&v=" + WebApp.this.j, BaseInfo.sBaseFsAppsPath + WebApp.this.mFeaturePermission + "/wap2app__rest.zip", AbsoluteConst.STREAMAPP_KEY_WAP2APP_SRC);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        b();
    }

    public void a(IApp iApp) {
        String str = StringConst.STREAMAPP_KEY_ROOTPATH + "splash/" + iApp.obtainAppId() + ".png";
        if (new File(str).exists() || PdrUtil.isEquals(AbsoluteConst.TRUE, SP.getBundleData("pdr", iApp.obtainAppId() + SP.STREAM_APP_NOT_FOUND_SPLASH_AT_SERVER))) {
            return;
        }
        String splashUrl = DataInterface.getSplashUrl(iApp.obtainAppId(), getActivity().getResources().getDisplayMetrics().widthPixels + "", getActivity().getResources().getDisplayMetrics().heightPixels + "");
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(getActivity());
        if (iActivityHandler != null) {
            iActivityHandler.downloadSimpleFileTask(iApp, splashUrl, str, AbsoluteConst.STREAMAPP_KEY_SPLASH);
        }
    }

    void b() {
        if (this.W != null) {
            return;
        }
        String str = getActivity().getPackageName() + ".streamdownload.downloadfinish";
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(str);
        this.W = new BroadcastReceiver() { // from class: io.dcloud.common.a.d.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                String stringExtra = intent.getStringExtra("flag");
                String string = extras.getString(InvokeExecutorHelper.AppStreamUtils.getString("CONTRACT_INTENT_EXTRA_FILE"));
                if (string.contains(WebApp.this.mFeaturePermission)) {
                    if (!string.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        String str2 = DeviceInfo.FILE_PROTOCOL + string;
                    }
                    extras.getInt(InvokeExecutorHelper.AppStreamUtils.getString("CONTRACT_INTENT_EXTRA_TYPE"));
                    int i = extras.getInt(InvokeExecutorHelper.AppStreamUtils.getString("CONTRACT_INTENT_EXTRA_STATUS"));
                    if (stringExtra.compareTo(AbsoluteConst.STREAMAPP_KEY_WAP2APP_SRC) == 0) {
                        try {
                            if (i == InvokeExecutorHelper.AppStreamUtils.getInt("CONTRACT_STATUS_SUCCESS")) {
                                File file = new File(BaseInfo.sBaseFsAppsPath + "/" + WebApp.this.mFeaturePermission + "/wap2app__rest.zip");
                                ZipUtils.upZipFile(file, BaseInfo.sBaseFsAppsPath + "/" + WebApp.this.mFeaturePermission + "/www");
                                InvokeExecutorHelper.AppStreamUtils.invoke("createCompletedFile", WebApp.this.mFeaturePermission);
                                file.delete();
                                WebApp dVar = WebApp.this;
                                dVar.a((IApp) dVar);
                                WebApp.this.smartUpdate();
                                Logger.d("download_manager", "download wap2app successful");
                                WebApp.this.getActivity().unregisterReceiver(WebApp.this.W);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        getActivity().registerReceiver(this.W, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0047 A[Catch: all -> 0x0086, Exception -> 0x0088, TryCatch #1 {Exception -> 0x0088, blocks: (B:3:0x0002, B:5:0x0009, B:7:0x000d, B:10:0x0018, B:12:0x001c, B:14:0x0047, B:16:0x004d, B:21:0x0059, B:24:0x006d, B:26:0x007e, B:27:0x0083, B:30:0x0027, B:32:0x0037, B:34:0x0043), top: B:2:0x0002, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x006d A[Catch: all -> 0x0086, Exception -> 0x0088, TRY_ENTER, TryCatch #1 {Exception -> 0x0088, blocks: (B:3:0x0002, B:5:0x0009, B:7:0x000d, B:10:0x0018, B:12:0x001c, B:14:0x0047, B:16:0x004d, B:21:0x0059, B:24:0x006d, B:26:0x007e, B:27:0x0083, B:30:0x0027, B:32:0x0037, B:34:0x0043), top: B:2:0x0002, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean a(String path, JSONObject json) {
        InputStream inputStream = null;
        boolean result = false;
        this.aH = path;

        try {
            if (this.aj != 0) {
                if (BaseInfo.mBaseAppInfoSet != null && BaseInfo.mBaseAppInfoSet.containsKey(this.aH)) {
                    // Якщо вже є в кеші, нічого не робимо
                } else {
                    // Файл не знайдено або не зчитано з попередніх джерел
                    if (this.aj == 1) {
                        String resolvedPath = a(BaseInfo.sConfigXML);
                        inputStream = PlatformUtil.getResInputStream(resolvedPath);
                    }
                }
            } else {
                // Спробувати через файловий обробник
                String resolvedPath = a(BaseInfo.sConfigXML);
                Object fileHandler = DHFile.createFileHandler(resolvedPath);
                inputStream = DHFile.getInputStream(fileHandler);
                if (inputStream == null) {
                    // fallback на ресурси
                    resolvedPath = a(BaseInfo.sConfigXML);
                    inputStream = PlatformUtil.getResInputStream(resolvedPath);
                    if (inputStream != null) {
                        this.aj = 1;
                    }
                }
            }

            if (inputStream == null) {
                if (isStreamApp() && !TextUtils.isEmpty(this.F)) {
                    IOUtil.close(inputStream);
                    return true;
                }

                this.b.a = true;
                this.b.b = DOMException.toJSON(-1225, "WGTU安装包中www目录下manifest.json不存在"); // "Файл manifest.json не знайдено"
                IOUtil.close(inputStream);
                return false;
            }

            // Попереднє налаштування
            s();

            result = a(inputStream, path, json);

            Activity activity = getActivity();
            IActivityHandler handler = DCloudAdapterUtil.getIActivityHandler(activity);
            if (handler != null) {
                handler.updateSplash(this.G);
            }

            this.e = true;
        } catch (Exception e) {
            Logger.w("parseConfig", e);
        } catch (Throwable t) {
            IOUtil.close(inputStream);
            throw t;
        }

        IOUtil.close(inputStream);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.dcloud.common.a.e
    public void a(Activity activity) {
        super.a(activity);
        this.aE = false;
        BaseInfo.sAppStateMap.remove(this.mFeaturePermission);
        Intent intent = activity.getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(IntentConst.FROM_SHORT_CUT_STRAT) && extras.getBoolean(IntentConst.FROM_SHORT_CUT_STRAT)) {
                this.aE = true;
            }
            String stringExtra = intent.getStringExtra(IntentConst.QIHOO_START_PARAM_FROM);
            if (!TextUtils.isEmpty(stringExtra) && stringExtra.equals("life")) {
                this.aF = "auto";
            } else if (intent.hasExtra(IntentConst.START_FORCE_SHORT)) {
                this.aF = intent.getStringExtra(IntentConst.START_FORCE_SHORT);
            }
            if (TextUtils.isEmpty(this.aF)) {
                if (this.w) {
                    this.aF = AbsoluteConst.INSTALL_OPTIONS_FORCE;
                    return;
                }
                String string = PlatformUtil.getOrCreateBundle("pdr").getString(AbsoluteConst.TEST_RUN + this.mFeaturePermission, null);
                if (!TextUtils.isEmpty(string) && string.equals("__am=t")) {
                    this.aF = AbsoluteConst.INSTALL_OPTIONS_FORCE;
                } else {
                    this.aF = "none";
                }
            }
        }
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public boolean startFromShortCut() {
        return this.aE;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String forceShortCut() {
        return this.aF;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(byte b) {
        this.aj = b;
    }

    String a(String str) {
        return this.ax + str;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String convert2RelPath(String str) {
        int length = obtainAppDataPath().length();
        int length2 = obtainAppDocPath().length();
        int length3 = BaseInfo.sDocumentFullPath.length();
        int length4 = BaseInfo.sDownloadFullPath.length();
        if (str.startsWith(obtainAppDataPath())) {
            return BaseInfo.REL_PRIVATE_WWW_DIR + str.substring(length - 1);
        }
        int i = length - 1;
        if (str.startsWith(obtainAppDataPath().substring(0, i))) {
            return BaseInfo.REL_PRIVATE_WWW_DIR + str.substring(i, str.length());
        }
        if (str.startsWith(obtainAppDocPath())) {
            return BaseInfo.REL_PRIVATE_DOC_DIR + str.substring(length2 - 1);
        }
        int i2 = length2 - 1;
        if (str.startsWith(obtainAppDocPath().substring(0, i2))) {
            return BaseInfo.REL_PRIVATE_DOC_DIR + str.substring(i2);
        }
        if (str.startsWith(BaseInfo.sDocumentFullPath)) {
            return BaseInfo.REL_PUBLIC_DOCUMENTS_DIR + str.substring(length3 - 1);
        }
        int i3 = length3 - 1;
        if (str.startsWith(BaseInfo.sDocumentFullPath.substring(0, i3))) {
            return BaseInfo.REL_PUBLIC_DOCUMENTS_DIR + str.substring(i3);
        }
        if (str.startsWith(BaseInfo.sDownloadFullPath)) {
            return BaseInfo.REL_PUBLIC_DOWNLOADS_DIR + str.substring(length4 - 1);
        }
        int i4 = length4 - 1;
        return str.startsWith(BaseInfo.sDownloadFullPath.substring(0, i4)) ? BaseInfo.REL_PUBLIC_DOWNLOADS_DIR + str.substring(i4) : str;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String convert2AbsFullPath(String str, String str2) {
        try {
            if (!PdrUtil.isEmpty(str2)) {
                if (DHFile.isExist(str2)) {
                    return str2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int indexOf = str2.indexOf("?");
        if (indexOf > 0) {
            str2 = str2.substring(0, indexOf);
        }
        if (str2.startsWith("_documents/")) {
            return BaseInfo.sDocumentFullPath + str2.substring(11);
        }
        if (str2.startsWith(BaseInfo.REL_PUBLIC_DOCUMENTS_DIR)) {
            return BaseInfo.sDocumentFullPath + str2.substring(10);
        }
        if (str2.startsWith(AbsoluteConst.MINI_SERVER_APP_DOC)) {
            return obtainAppDocPath() + str2.substring(5);
        }
        if (str2.startsWith(BaseInfo.REL_PRIVATE_DOC_DIR)) {
            return obtainAppDocPath() + str2.substring(4);
        }
        if (str2.startsWith("_downloads/")) {
            return BaseInfo.sDownloadFullPath + str2.substring(11);
        }
        if (str2.startsWith(BaseInfo.REL_PUBLIC_DOWNLOADS_DIR)) {
            return BaseInfo.sDownloadFullPath + str2.substring(10);
        }
        boolean z = true;
        if (str2.startsWith(AbsoluteConst.MINI_SERVER_APP_WWW)) {
            byte b = this.aj;
            if (b == 1) {
                return BaseInfo.sBaseResAppsPath + this.mFeaturePermission + "/" + BaseInfo.APP_WWW_FS_DIR + str2.substring(5);
            }
            return b == 0 ? this.ax + str2.substring(5) : str2;
        }
        if (str2.startsWith(BaseInfo.REL_PRIVATE_WWW_DIR)) {
            byte b2 = this.aj;
            if (b2 == 1) {
                return BaseInfo.sBaseResAppsPath + this.mFeaturePermission + "/" + BaseInfo.APP_WWW_FS_DIR + str2.substring(4);
            }
            return b2 == 0 ? this.ax + str2.substring(4) : str2;
        }
        if (str2.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            return str2.substring(7);
        }
        if (str2.startsWith(DeviceInfo.sDeviceRootDir)) {
            return str2;
        }
        if (str2.startsWith("http://localhost")) {
            String substring = str2.substring(16);
            return convert2AbsFullPath(null, substring.substring(substring.indexOf("/") + 1));
        }
        if (str2.startsWith("/")) {
            str2 = str2.substring(1);
        } else {
            z = false;
        }
        if (str != null) {
            if (str.startsWith(SDK.ANDROID_ASSET)) {
                str = str.substring(22);
            } else if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                str = str.substring(7);
            }
        }
        if (str != null && !z) {
            return PdrUtil.standardizedURL(str, str2);
        }
        if (!z) {
            return str2;
        }
        String obtainAppDataPath = obtainAppDataPath();
        if (str != null && !PdrUtil.isEquals(str, obtainAppDataPath) && str.contains("www/")) {
            obtainAppDataPath = str.substring(0, str.indexOf("www/") + 4);
        }
        return obtainAppDataPath + d(str2);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String convert2AbsFullPath(String str) {
        return convert2AbsFullPath(null, str);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainRuntimeArgs() {
        return JSONObject.quote(this.l);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setRuntimeArgs(String str) {
        this.l = str;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAdaptationJs() {
        if (this.ai == null && !PdrUtil.isEmpty(this.r)) {
            byte[] fileContent = PlatformUtil.getFileContent(a(this.r), obtainRunningAppMode() == 1 ? 0 : 2);
            if (fileContent != null) {
                this.ai = new String(fileContent);
            } else {
                this.ai = "";
            }
        }
        return this.ai;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String convert2WebviewFullPath(String str, String str2) {
        if (PdrUtil.isEmpty(str2)) {
            return str2;
        }
        if (this.z) {
            return !str2.startsWith(DeviceInfo.HTTP_PROTOCOL) ? this.ay + str2 : str2;
        }
        if (str2.startsWith(DeviceInfo.FILE_PROTOCOL) || str2.startsWith(DeviceInfo.HTTP_PROTOCOL) || str2.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
            return str2;
        }
        try {
            if (DHFile.isExist(str2)) {
                return "file:///" + d(str2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (str2.startsWith(DeviceInfo.sDeviceRootDir)) {
            return DeviceInfo.FILE_PROTOCOL + str2;
        }
        boolean z = true;
        if (str2.startsWith("/")) {
            str2 = str2.substring(1);
        } else {
            z = false;
        }
        if (str2.startsWith(BaseInfo.REL_PRIVATE_WWW_DIR)) {
            return obtainWebviewBaseUrl() + d(str2.substring(4));
        }
        if (str2.startsWith(BaseInfo.REL_PRIVATE_DOC_DIR)) {
            return DeviceInfo.FILE_PROTOCOL + obtainAppDocPath() + d(str2.substring(4));
        }
        if (str2.startsWith(BaseInfo.REL_PUBLIC_DOCUMENTS_DIR)) {
            return DeviceInfo.FILE_PROTOCOL + BaseInfo.sDocumentFullPath + d(str2.substring(10));
        }
        if (str2.startsWith(BaseInfo.REL_PUBLIC_DOWNLOADS_DIR)) {
            return DeviceInfo.FILE_PROTOCOL + BaseInfo.sDownloadFullPath + d(str2.substring(10));
        }
        if (str != null && !z) {
            return PdrUtil.standardizedURL(str, str2);
        }
        String obtainWebviewBaseUrl = obtainWebviewBaseUrl();
        if (str != null && !PdrUtil.isEquals(str, obtainWebviewBaseUrl) && str.contains("www/")) {
            obtainWebviewBaseUrl = str.substring(0, str.indexOf("www/") + 4);
        }
        return obtainWebviewBaseUrl + d(str2);
    }

    private static String d(String str) {
        return (str == null || str.length() <= 0 || str.charAt(0) != '/') ? str : d(str.substring(1));
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void addFeaturePermission(String str) {
        this.mFeaturePermissions.add(str.toLowerCase());
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void addAllFeaturePermission() {
        PermissionControler.a(this.mFeaturePermission);
    }

    void c() {
        PermissionControler.a(this.mFeaturePermission, this.mFeaturePermissions);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r0v3, types: [io.dcloud.common.a.d$5] */
    public void a(final ICallBack iCallBack) {
        if ((BaseInfo.ISDEBUG || this.A) && this.aj == 1) {
            new Thread() { // from class: io.dcloud.common.a.d.5
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    String str = BaseInfo.sBaseFsAppsPath + WebApp.this.mFeaturePermission + DeviceInfo.sSeparatorChar + BaseInfo.APP_WWW_FS_DIR;
                    long currentTimeMillis = System.currentTimeMillis();
                    Logger.d(TAG_WEBAPP, WebApp.this.mFeaturePermission + " copy resoure begin!!!");
                    DHFile.delete(str);
                    DHFile.copyDir(WebApp.this.ax, str);
                    Logger.d(TAG_WEBAPP, WebApp.this.mFeaturePermission + " copy resoure end!!! useTime=" + (System.currentTimeMillis() - currentTimeMillis));
                    WebApp.this.aj = (byte) 0;
                    WebApp.this.setAppDataPath(str);
                    if (WebApp.this.c != null) {
                        WebApp.this.c.saveToBundleData();
                    }
                    MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.a.d.5.1
                        @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                        public void execute(Object obj) {
                            iCallBack.onCallBack(0, null);
                        }
                    }, null);
                }
            }.start();
        } else {
            iCallBack.onCallBack(0, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(String str) {
        if (Build.VERSION.SDK_INT >= 21 && getActivity() != null) {
            if (this.aD != -111111) {
                getActivity().getWindow().setStatusBarColor(this.aD);
            } else {
                getActivity().getWindow().setStatusBarColor(BaseInfo.mDeStatusBarBackground);
            }
        }
        AppStatus.setAppStatus(this.mFeaturePermission, 2);
        BaseInfo.s_Runing_App_Count++;
        this.f = true;
        setRuntimeArgs(str);
        return a(5);
    }

    private boolean a(int i) {
        Logger.i(TAG_WEBAPP, this.mFeaturePermission + (this.aj == 1 ? " APP_RUNNING_MODE" : " FS_RUNNING_MODE"));
        c();
        setStatus((byte) 3);
        InvokeExecutorHelper.QihooInnerStatisticUtil.invoke("statOnPageStart", this.mFeaturePermission);
        IApp.IAppStatusListener iAppStatusListener = this.Q;
        if (iAppStatusListener != null) {
            iAppStatusListener.onStart();
        }
        Logger.i(TAG_WEBAPP, "mLaunchPath=" + this.B);
        Logger.i("download_manager", "webapp start task begin success appid=" + this.mFeaturePermission + " mLaunchPath=" + this.B);
        TestUtil.print(TestUtil.START_STREAM_APP, "webapp start appid=" + this.mFeaturePermission);
        BaseInfo.setLoadingLaunchePage(true, "start0");
        String stringExtra = getActivity().getIntent().getStringExtra(IntentConst.WEBAPP_ACTIVITY_LAUNCH_PATH);
        if (stringExtra != null && !"".equals(stringExtra.trim())) {
            getActivity().getIntent().removeExtra(IntentConst.WEBAPP_ACTIVITY_LAUNCH_PATH);
            if (!"about:blank".equals(stringExtra)) {
                stringExtra = convert2WebviewFullPath(null, stringExtra);
            }
        } else if (BaseInfo.isWap2AppAppid(this.mFeaturePermission) && !TextUtils.isEmpty(this.C)) {
            stringExtra = convert2WebviewFullPath(null, this.C);
        } else {
            stringExtra = convert2WebviewFullPath(null, this.B);
        }
        a();
        Object processEvent = this.g.processEvent(IMgr.MgrType.WindowMgr, i, new Object[]{this, stringExtra, Boolean.valueOf(this.S), this.aC});
        if (processEvent == null) {
            return true;
        }
        return Boolean.parseBoolean(String.valueOf(processEvent));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c(String str) {
        AppStatus.setAppStatus(this.mFeaturePermission, 2);
        setRuntimeArgs(str);
        setStatus((byte) 3);
        Object processEvent = this.g.processEvent(IMgr.MgrType.WindowMgr, 41, new Object[]{this, convert2WebviewFullPath(null, this.F), Boolean.valueOf(this.S)});
        if (processEvent == null) {
            return true;
        }
        return Boolean.parseBoolean(String.valueOf(processEvent));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        setAppDataPath(BaseInfo.sBaseFsAppsPath + this.mFeaturePermission + DeviceInfo.sSeparatorChar + BaseInfo.REAL_PRIVATE_WWW_DIR);
        boolean a2 = a(this.mFeaturePermission, (JSONObject) null);
        if (!a2) {
            return a2;
        }
        showSplash();
        this.g.processEvent(IMgr.MgrType.FeatureMgr, 3, this.mFeaturePermission);
        return a(10);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setStatus(byte b) {
        this.d = b;
        if (b == 3) {
            this.T = System.currentTimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        if (Build.VERSION.SDK_INT >= 21 && getActivity() != null) {
            if (this.aD != -111111) {
                getActivity().getWindow().setStatusBarColor(this.aD);
            } else {
                getActivity().getWindow().setStatusBarColor(BaseInfo.mDeStatusBarBackground);
            }
        }
        setStatus((byte) 3);
        InvokeExecutorHelper.QihooInnerStatisticUtil.invoke("statOnPageStart", this.mFeaturePermission);
        this.aa.onAppActive(this);
        BaseInfo.sAppStateMap.remove(this.mFeaturePermission);
        a(ISysEventListener.SysEventType.onWebAppForeground, IntentConst.obtainArgs(obtainWebAppIntent(), this.mFeaturePermission));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        InvokeExecutorHelper.QihooInnerStatisticUtil.invoke("statOnPageEnd", this.mFeaturePermission);
        this.aa.onAppUnActive(this);
        a(ISysEventListener.SysEventType.onWebAppPause, this);
        a(ISysEventListener.SysEventType.onWebAppBackground, this);
        IApp.IAppStatusListener iAppStatusListener = this.Q;
        if (iAppStatusListener != null) {
            iAppStatusListener.onPause(this, null);
        }
        setStatus((byte) 2);
    }

    public void g() {
        f();
        setStatus((byte) 1);
        this.g.processEvent(IMgr.MgrType.FeatureMgr, 3, this.mFeaturePermission);
        Logger.d(Logger.AppMgr_TAG, this.mFeaturePermission + " will active change to unrunning");
        this.g.processEvent(null, 0, this);
    }

    private void t() {
        if (!Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) || Build.VERSION.SDK_INT < 23 || PlatformUtil.checkGTAndYoumeng()) {
            if (!isStreamApp()) {
                ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.common.a.d.6
                    @Override // java.lang.Runnable
                    public void run() {
                        HashMap hashMap = new HashMap();
                        hashMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                        SharedPreferences orCreateBundle = SP.getOrCreateBundle(AbsoluteConst.START_STATISTICS_DATA);
                        String startData = CommitDataUtil.getStartData(WebApp.this, orCreateBundle);
                        if (TextUtils.isEmpty(startData) || NetTool.httpPost("https://service.dcloud.net.cn/collect/plusapp/startup", startData, hashMap) == null || !startData.contains("&apps=")) {
                            return;
                        }
                        orCreateBundle.edit().putLong(AbsoluteConst.COMMIT_APP_LIST_TIME, System.currentTimeMillis()).commit();
                    }
                });
            } else if (isStreamApp()) {
                ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.common.a.d.7
                    @Override // java.lang.Runnable
                    public void run() {
                        HashMap hashMap = new HashMap();
                        hashMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                        String sreamAppShortName = ShortCutUtil.getSreamAppShortName(WebApp.this.getActivity());
                        String launcherPackageName = LauncherUtil.getLauncherPackageName(WebApp.this.getActivity());
                        JSONObject jSONObject = new JSONObject();
                        if (launcherPackageName != null) {
                            try {
                                if (!"".equals(launcherPackageName.trim())) {
                                    jSONObject.put("pn", launcherPackageName);
                                    PackageInfo packageInfo = WebApp.this.getActivity().getPackageManager().getPackageInfo(launcherPackageName, 0);
                                    jSONObject.put("pv", packageInfo.versionName);
                                    jSONObject.put("pvc", String.valueOf(packageInfo.versionCode));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append(DataInterface.getUrlBaseData(WebApp.this.getActivity(), WebApp.this.obtainAppId(), null, null));
                        if (jSONObject.toString() != null && !"".equals(jSONObject.toString().trim())) {
                            stringBuffer.append("&launcher=" + jSONObject.toString());
                        }
                        stringBuffer.append("&shortcuts=" + sreamAppShortName);
                        stringBuffer.append("&romv=" + DataInterface.getRomVersion());
                        if (TextUtils.isEmpty(stringBuffer)) {
                            return;
                        }
                        NetTool.httpPost(StringConst.STREAMAPP_KEY_BASESERVICEURL() + "collect/shortcuts", stringBuffer.toString(), hashMap);
                    }
                });
            }
        }
    }

    public boolean h() {
        Logger.d(Logger.AppMgr_TAG, this.mFeaturePermission + " onStop");
        IApp.IAppStatusListener iAppStatusListener = this.Q;
        if (iAppStatusListener != null) {
            return iAppStatusListener.onStop();
        }
        return true;
    }

    public void i() {
        BaseInfo.sAppStateMap.put(this.mFeaturePermission, obtainWebAppIntent());
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [io.dcloud.common.a.d$8] */
    public void j() {
        Logger.d(Logger.AppMgr_TAG, "webapp.onStoped");
        BaseInfo.s_Runing_App_Count--;
        if (TextUtils.equals(BaseInfo.sLastRunApp, this.mFeaturePermission)) {
            BaseInfo.sLastRunApp = null;
        }
        a(ISysEventListener.SysEventType.onWebAppStop, this);
        u();
        PlatformUtil.invokeMethod("io.dcloud.feature.apsqh.QHNotifactionReceiver", "doSaveNotifications", null, new Class[]{Context.class}, new Object[]{this.Z.getBaseContext()});
        PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "streamappStop", null, new Class[]{Activity.class, String.class}, new Object[]{getActivity(), obtainAppId()});
        if (this.W != null) {
            try {
                getActivity().unregisterReceiver(this.W);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        t();
        new Thread() { // from class: io.dcloud.common.a.d.8
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    DHFile.deleteFile(WebApp.this.obtainAppTempPath());
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }.start();
        PermissionControler.b(this.mFeaturePermission);
        this.g.c(this);
        this.g.processEvent(IMgr.MgrType.WindowMgr, 25, this);
    }

    private void u() {
        if (BaseInfo.isForQihooHelper(getActivity())) {
            Intent intent = new Intent();
            intent.setClassName(getActivity(), DCloudAdapterUtil.getDcloudDownloadService());
            intent.putExtra("mode", "handle_service");
            getActivity().startService(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IFrameView a(IWebviewStateListener iWebviewStateListener) {
        c();
        return (IFrameView) this.g.processEvent(IMgr.MgrType.WindowMgr, 17, new Object[]{this, convert2WebviewFullPath(null, this.B), iWebviewStateListener});
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAppId() {
        return this.mFeaturePermission;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String checkPrivateDirAndCopy2Temp(String str) {
        if (obtainRunningAppMode() == 1 && checkPrivateDir(str)) {
            String substring = str.substring(str.indexOf(BaseInfo.APP_WWW_FS_DIR) + BaseInfo.APP_WWW_FS_DIR.length());
            String str2 = this.ax + substring;
            str = obtainAppTempPath() + substring;
            if (!DHFile.exists(str)) {
                DHFile.copyAssetsFile(str2, str);
            }
        }
        return str;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public boolean checkPrivateDir(String str) {
        return str.startsWith(obtainAppDataPath()) || str.startsWith(BaseInfo.REL_PRIVATE_WWW_DIR);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void unregisterSysEventListener(ISysEventListener iSysEventListener, ISysEventListener.SysEventType sysEventType) {
        ArrayList<ISysEventListener> arrayList;
        HashMap<ISysEventListener.SysEventType, ArrayList<ISysEventListener>> hashMap = this.I;
        if (hashMap == null || (arrayList = hashMap.get(sysEventType)) == null) {
            return;
        }
        arrayList.remove(iSysEventListener);
        if (arrayList.isEmpty()) {
            this.I.remove(sysEventType);
        }
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void registerSysEventListener(ISysEventListener iSysEventListener, ISysEventListener.SysEventType sysEventType) {
        if (this.I == null) {
            this.I = new HashMap<>(1);
        }
        ArrayList<ISysEventListener> arrayList = this.I.get(sysEventType);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.I.put(sysEventType, arrayList);
        }
        arrayList.add(iSysEventListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(ISysEventListener.SysEventType sysEventType, Object obj) {
        HashMap<ISysEventListener.SysEventType, ArrayList<ISysEventListener>> hashMap = this.I;
        boolean z = false;
        if (hashMap == null) {
            return false;
        }
        ArrayList<ISysEventListener> arrayList = hashMap.get(sysEventType);
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                ISysEventListener iSysEventListener = arrayList.get(size);
                if (a(iSysEventListener, sysEventType) && (z || iSysEventListener.onExecute(sysEventType, obj)) && !a(sysEventType)) {
                    break;
                }
            }
        }
        return z;
    }

    private boolean a(ISysEventListener iSysEventListener, ISysEventListener.SysEventType sysEventType) {
        if (!(iSysEventListener instanceof IBoot) || PdrUtil.parseBoolean(String.valueOf(this.g.processEvent(null, 1, iSysEventListener)), false, false)) {
            return true;
        }
        return (sysEventType == ISysEventListener.SysEventType.onStart || sysEventType == ISysEventListener.SysEventType.onStop || sysEventType == ISysEventListener.SysEventType.onPause || sysEventType == ISysEventListener.SysEventType.onResume) ? false : true;
    }

    @Override // io.dcloud.common.DHInterface.ISysEventListener
    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        byte b = this.d;
        if (b == 3) {
            return a(sysEventType, obj) | a(ISysEventListener.SysEventType.AllSystemEvent, obj);
        }
        if (b == 1 && (sysEventType == ISysEventListener.SysEventType.onWebAppStop || sysEventType == ISysEventListener.SysEventType.onStop)) {
            j();
        }
        return false;
    }

    public static boolean a(ISysEventListener.SysEventType sysEventType) {
        return (sysEventType == ISysEventListener.SysEventType.onKeyDown || sysEventType == ISysEventListener.SysEventType.onKeyUp || sysEventType == ISysEventListener.SysEventType.onKeyLongPress) ? false : true;
    }

    public String k() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("appid", this.mFeaturePermission);
            jSONObject.put("version", this.j);
            jSONObject.put("name", this.G);
            jSONObject.put("description", this.m);
            jSONObject.put("author", this.n);
            jSONObject.put("email", this.o);
            jSONObject.put(IApp.ConfigProperty.CONFIG_LICENSE, this.p);
            jSONObject.put("licensehref", this.q);
            jSONObject.put(IApp.ConfigProperty.CONFIG_FEATURES, new JSONArray((Collection) this.mFeaturePermissions));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAppName() {
        return this.G;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAppVersionName() {
        return this.j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x02b5  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x01e9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0213 A[Catch: Exception -> 0x0298, LOOP:0: B:44:0x0211->B:45:0x0213, LOOP_END, TryCatch #3 {Exception -> 0x0298, blocks: (B:35:0x01e9, B:37:0x01f0, B:39:0x01fe, B:41:0x0206, B:43:0x020c, B:45:0x0213, B:47:0x0236, B:49:0x023c, B:52:0x024c, B:54:0x0251, B:55:0x0268, B:57:0x026f, B:59:0x0272, B:61:0x0288, B:64:0x01f8), top: B:34:0x01e9, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x023c A[Catch: Exception -> 0x0298, TRY_LEAVE, TryCatch #3 {Exception -> 0x0298, blocks: (B:35:0x01e9, B:37:0x01f0, B:39:0x01fe, B:41:0x0206, B:43:0x020c, B:45:0x0213, B:47:0x0236, B:49:0x023c, B:52:0x024c, B:54:0x0251, B:55:0x0268, B:57:0x026f, B:59:0x0272, B:61:0x0288, B:64:0x01f8), top: B:34:0x01e9, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x024c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r15v10 */
    /* JADX WARN: Type inference failed for: r15v15 */
    /* JADX WARN: Type inference failed for: r15v9 */
    /* JADX WARN: Type inference failed for: r7v22 */
    /* JADX WARN: Type inference failed for: r7v23, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r7v29, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r7v5, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r7v6 */
    /* JADX WARN: Type inference failed for: r7v7, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean b(java.lang.String r14, org.json.JSONObject r15) {
        /*
            Method dump skipped, instructions count: 712
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.a.d.b(java.lang.String, org.json.JSONObject):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x00b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean c(java.lang.String r10, org.json.JSONObject r11) {
        /*
            Method dump skipped, instructions count: 257
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.a.d.c(java.lang.String, org.json.JSONObject):boolean");
    }

    void l() {
        if (PdrUtil.isEmpty(this.ax) || !DeviceInfo.startsWithSdcard(this.ax)) {
            setAppDataPath(BaseInfo.sBaseFsAppsPath + this.mFeaturePermission + "/" + BaseInfo.REAL_PRIVATE_WWW_DIR);
        }
        if (PdrUtil.isEmpty(this.aG) || !DeviceInfo.startsWithSdcard(this.aG)) {
            setAppDocPath(BaseInfo.sBaseFsAppsPath + this.mFeaturePermission + "/" + BaseInfo.REAL_PRIVATE_DOC_DIR);
        }
        if (PdrUtil.isEmpty(this.aH) || !DeviceInfo.startsWithSdcard(this.aH)) {
            this.aH = BaseInfo.sBaseFsAppsPath + this.mFeaturePermission + "/" + BaseInfo.APP_WEB_CHACHE;
        }
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAppWebCachePath() {
        return this.aH;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAppDataPath() {
        String str = this.ax;
        return str != null ? str : this.mFeaturePermission + "/www/";
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setAppDataPath(String str) {
        if (this.aj == 1) {
            if (str.startsWith(BaseInfo.sBaseResAppsPath)) {
                this.ax = str;
                return;
            } else {
                this.ax = BaseInfo.sBaseResAppsPath + this.mFeaturePermission + "/" + BaseInfo.APP_WWW_FS_DIR;
                return;
            }
        }
        this.ax = PdrUtil.appendByDeviceRootDir(str);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAppDocPath() {
        return this.aG;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setAppDocPath(String str) {
        this.aG = PdrUtil.appendByDeviceRootDir(str);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAppTempPath() {
        return BaseInfo.sBaseFsAppsPath + this.mFeaturePermission + "/temp/";
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainAppLog() {
        return BaseInfo.sBaseFsAppsPath + this.mFeaturePermission + "/log/";
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public InputStream obtainResInStream(String str, String str2) {
        String convert2AbsFullPath = convert2AbsFullPath(str, str2);
        byte b = this.aj;
        if (b == 1) {
            if (PdrUtil.isDeviceRootDir(convert2AbsFullPath)) {
                try {
                    return DHFile.getInputStream(DHFile.createFileHandler(convert2AbsFullPath));
                } catch (IOException e) {
                    Logger.w("WebApp.obtainResInStream", e);
                }
            } else {
                return PlatformUtil.getResInputStream(convert2AbsFullPath);
            }
        } else if (b == 0) {
            try {
                return DHFile.getInputStream(DHFile.createFileHandler(convert2AbsFullPath));
            } catch (IOException e2) {
                Logger.w("WebApp.obtainResInStream", e2);
            }
        }
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String convert2LocalFullPath(String str, String str2) {
        String convert2AbsFullPath = convert2AbsFullPath(str, str2);
        byte b = this.aj;
        if (b == 1) {
            InputStream resInputStream = PlatformUtil.getResInputStream(convert2AbsFullPath);
            if (resInputStream != null) {
                String str3 = obtainAppTempPath() + System.currentTimeMillis();
                try {
                    DHFile.writeFile(resInputStream, str3);
                    resInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return str3;
            }
        } else if (b == 0) {
        }
        return convert2AbsFullPath;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public InputStream obtainResInStream(String str) {
        return obtainResInStream(null, str);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public byte obtainRunningAppMode() {
        return this.aj;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public byte obtainAppStatus() {
        return this.d;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public boolean isOnAppRunningMode() {
        return this.aj == 1;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainConfigProperty(String str) {
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_AUTOCLOSE)) {
            return String.valueOf(this.am);
        }
        if (PdrUtil.isEquals(str, "timeout")) {
            return String.valueOf(this.ao);
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_DELAY)) {
            return String.valueOf(this.ap);
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_SPLASHSCREEN)) {
            return String.valueOf(this.ak);
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_WAITING)) {
            return String.valueOf(this.al);
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_H5PLUS)) {
            return String.valueOf(this.y);
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_USER_AGENT)) {
            return this.s;
        }
        if (PdrUtil.isEquals(str, "error")) {
            return this.E;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_FULLSCREEN)) {
            return String.valueOf(this.ag);
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_UNTRUSTEDCA)) {
            return this.D;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_LOADED_TIME)) {
            return this.R;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_RAM_CACHE_MODE)) {
            return this.az;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_JSERROR)) {
            return this.u + "";
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_CRASH)) {
            return this.t + "";
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_USE_ENCRYPTION)) {
            return this.aB + "";
        }
        if (PdrUtil.isEquals(str, "w2a_delay")) {
            return String.valueOf(this.aq);
        }
        if (PdrUtil.isEquals(str, "w2a_autoclose")) {
            return String.valueOf(this.an);
        }
        if (PdrUtil.isEquals(str, "wap2app_running_mode")) {
            return this.v + "";
        }
        if (PdrUtil.isEquals(str, "injection")) {
            return this.U + "";
        }
        if (PdrUtil.isEquals(str, "event")) {
            return this.ar;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_TARGET)) {
            return this.as;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_LPLUSERQUIRE)) {
            return this.at;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_SPLUSERQUIRE)) {
            return this.au;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_LGEOLOCATION)) {
            return this.av;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_SGEOLOCATION)) {
            return this.aw;
        }
        if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_STATUSBAR_BC)) {
            return this.aD + "";
        }
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setConfigProperty(String str, String str2) {
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_AUTOCLOSE)) {
            this.am = PdrUtil.parseBoolean(str2, this.am, false);
            return;
        }
        if (PdrUtil.isEquals(str, "timeout")) {
            this.ao = PdrUtil.parseInt(str2, this.ao);
            return;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_DELAY)) {
            this.ap = PdrUtil.parseInt(str2, this.ap);
            return;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_SPLASHSCREEN)) {
            this.ak = PdrUtil.parseBoolean(str2, this.ak, false);
            return;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_WAITING)) {
            this.al = PdrUtil.parseBoolean(str2, this.al, false);
            return;
        }
        if (PdrUtil.isEquals(str, "name")) {
            this.G = str2;
            return;
        }
        if (PdrUtil.isEquals(str, "name")) {
            this.n = str2;
            return;
        }
        if (PdrUtil.isEquals(str, "email")) {
            this.o = str2;
            return;
        }
        if (PdrUtil.isEquals(str, "url")) {
            this.q = str2;
            return;
        }
        if (PdrUtil.isEquals(str, "name")) {
            this.j = str2;
            return;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_RUNMODE_LIBERATE)) {
            this.A = PdrUtil.parseBoolean(str2, this.ak, false);
            return;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_H5PLUS)) {
            this.y = PdrUtil.parseBoolean(str2, true, false);
            return;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_USER_AGENT)) {
            this.s = str2;
            return;
        }
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_FULLSCREEN)) {
            this.ag = PdrUtil.parseBoolean(str2, this.ag, false);
            return;
        }
        if (PdrUtil.isEquals(str, "webcache_path")) {
            this.aH = str2;
            return;
        }
        if (PdrUtil.isEquals(str, "wap2app_running_mode")) {
            this.v = PdrUtil.parseBoolean(str2, false, false);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_LOADED_TIME)) {
            this.R = str2;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_STATUSBAR_BC)) {
            this.aD = Color.parseColor(str2);
        }
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainWebviewBaseUrl() {
        return b(this.aj);
    }

    private String b(byte b) {
        byte b2 = this.aj;
        if (b2 == 1) {
            return BaseInfo.sBaseResAppsFullPath + this.mFeaturePermission + "/" + BaseInfo.APP_WWW_FS_DIR;
        }
        if (b2 == 0) {
            return DeviceInfo.FILE_PROTOCOL + this.ax;
        }
        return null;
    }

    public void m() {
        ArrayList<String> arrayList = this.mFeaturePermissions;
        if (arrayList != null) {
            arrayList.clear();
            this.mFeaturePermissions = null;
        }
        HashMap<ISysEventListener.SysEventType, ArrayList<ISysEventListener>> hashMap = this.I;
        if (hashMap != null) {
            hashMap.clear();
            this.I = null;
        }
        this.g = null;
        this.c = null;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void applySmartUpdate() {
        d();
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public ISmartUpdate startSmartUpdate() {
        if (this.P == null) {
            this.P = InvokeExecutorHelper.createWebAppSmartUpdater(this);
        }
        this.P.checkUpdate();
        return this.P;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public boolean isJustDownload() {
        Intent intent = this.O;
        if (intent != null) {
            return intent.getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_JUST_DOWNLOAD, false);
        }
        return false;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void smartUpdate() {
        if (this.P != null) {
            String bundleData = PlatformUtil.getBundleData("pdr", this.mFeaturePermission + "_smart_update_need_update");
            if (TextUtils.isEmpty(bundleData)) {
                return;
            }
            Logger.d("will update " + bundleData);
            this.P.update(new ISmartUpdate.SmartUpdateCallbackParams(bundleData));
        }
    }

    public String toString() {
        return this.G + "-" + this.mFeaturePermission + "-" + super.toString();
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setWebAppIntent(Intent intent) {
        Intent intent2 = new Intent(intent);
        this.O = intent2;
        String stringExtra = intent2.getStringExtra(IntentConst.FIRST_WEB_URL);
        if (TextUtils.equals(this.F, "__no__")) {
            return;
        }
        this.F = stringExtra;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public void setWebAppActivity(Activity activity) {
        this.Z = activity;
        a(this.Z);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public Intent obtainWebAppIntent() {
        return this.O;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.dcloud.common.DHInterface.IApp
    public void showSplash() {
        Activity activity = getActivity();
        if (activity instanceof IOnCreateSplashView) {
            activity.setIntent(this.O);
            ((IOnCreateSplashView) activity).onCreateSplash(activity);
        }
    }

    /* compiled from: WebApp.java */
    /* renamed from: io.dcloud.common.a.d$9, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[IApp.ConfigProperty.ThridInfo.values().length];
            a = iArr;
            try {
                iArr[IApp.ConfigProperty.ThridInfo.QIHOO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[IApp.ConfigProperty.ThridInfo.OverrideUrlJsonData.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[IApp.ConfigProperty.ThridInfo.OverrideResourceJsonData.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[IApp.ConfigProperty.ThridInfo.NavigationbarJsonData.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public JSONObject obtainThridInfo(IApp.ConfigProperty.ThridInfo thridInfo) {
        int i = AnonymousClass9.a[thridInfo.ordinal()];
        if (i == 1) {
            return this.J;
        }
        if (i == 2) {
            return this.K;
        }
        if (i == 3) {
            return this.L;
        }
        if (i == 4) {
            return this.M;
        }
        if (i != 5) {
            return null;
        }
        return this.N;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String getPopGesture() {
        return this.aA;
    }

    public boolean n() {
        return this.d == 3;
    }

    private void v() {
        String[] split;
        this.X = new HashMap<>();
        String string = PlatformUtil.getOrCreateBundle(this.mFeaturePermission + "_1").getString("Authorize", null);
        this.Y = string;
        if (string == null || (split = string.split("&")) == null || split.length <= 0) {
            return;
        }
        for (String str : split) {
            if (!TextUtils.isEmpty(str)) {
                String[] split2 = str.split("=");
                this.X.put(split2[0], Integer.valueOf(Integer.parseInt(split2[1])));
            }
        }
    }

    public boolean a(String str, String str2) {
        return (this.X.containsKey(new StringBuilder().append(str).append("_*").toString()) || this.X.containsKey(new StringBuilder().append(str).append("_").append(str2).toString())) && this.X.get(new StringBuilder().append(str).append("_").append(str2).toString()).intValue() == 1;
    }

    public void a(String str, int i) {
        this.X.put(str, Integer.valueOf(i));
        if (TextUtils.isEmpty(this.Y)) {
            this.Y = str + "=" + i;
        } else {
            this.Y += "&" + str + "=" + i;
        }
        PlatformUtil.getOrCreateBundle(this.mFeaturePermission + "_1").edit().putString("Authorize", this.Y).commit();
    }

    private void a(JSONObject jSONObject) {
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        if (this.aB && EncryptionSingleton.getInstance().get(this.mFeaturePermission) == null && (jSONObject2 = JSONUtil.getJSONObject(jSONObject, IApp.ConfigProperty.CONFIG_CONFUSION)) != null && jSONObject2.has(IApp.ConfigProperty.CONFIG_RESOURCES) && (jSONObject3 = JSONUtil.getJSONObject(jSONObject2, IApp.ConfigProperty.CONFIG_RESOURCES)) != null) {
            HashMap hashMap = new HashMap();
            Iterator<String> keys = jSONObject3.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                if (jSONObject3.has(next)) {
                    try {
                        JSONObject jSONObject4 = jSONObject3.getJSONObject(next);
                        if (jSONObject4 != null && jSONObject4.has(IApp.ConfigProperty.CONFIG_ALGORITHM) && jSONObject4.has(IApp.ConfigProperty.CONFIG_KEY)) {
                            jSONObject4.getString(IApp.ConfigProperty.CONFIG_ALGORITHM);
                            hashMap.put(next, jSONObject4.getString(IApp.ConfigProperty.CONFIG_KEY));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (hashMap.size() > 0) {
                EncryptionSingleton.getInstance().put(this.mFeaturePermission, hashMap);
            }
        }
    }

    private String a(byte[] bArr) {
        if (bArr == null || bArr.length <= 8) {
            return null;
        }
        if (bArr[0] == 5 && bArr[1] == 15 && bArr[2] == 53 && bArr[3] == 43) {
            this.aB = true;
            int parseInt = (Integer.parseInt(Integer.toHexString(bArr[5] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE), 16) << 8) + Integer.parseInt(Integer.toHexString(bArr[6] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE), 16);
            byte[] bArr2 = new byte[parseInt];
            for (int i = 0; i < parseInt; i++) {
                bArr2[i] = bArr[i + 8];
            }
            try {
                String str = new String((byte[]) InvokeExecutorHelper.RSAUtils.invoke("decryptDataWithPublicKey", new Class[]{byte[].class, PublicKey.class}, bArr2, (PublicKey) InvokeExecutorHelper.RSAUtils.invoke("loadPublicKey", new Class[]{String.class}, EncryptionConstant.a(this.g.getContext()))));
                byte[] bArr3 = new byte[(bArr.length - parseInt) - 8];
                for (int i2 = 0; i2 < (bArr.length - parseInt) - 8; i2++) {
                    bArr3[i2] = bArr[i2 + parseInt + 8];
                }
                return (String) InvokeExecutorHelper.AesEncryptionUtil.invoke("decrypt", new Class[]{String.class, String.class, String.class}, (String) InvokeExecutorHelper.AesEncryptionUtil.invoke("byte2hex", new Class[]{byte[].class}, bArr3), str, EncryptionConstant.a());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        this.aB = false;
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public boolean isCompetentStreamApp() {
        if (isStreamApp()) {
            return this.w;
        }
        return true;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public boolean isStreamApp() {
        Intent obtainWebAppIntent = obtainWebAppIntent();
        if (obtainWebAppIntent == null || !obtainWebAppIntent.hasExtra(IntentConst.IS_STREAM_APP)) {
            return false;
        }
        return obtainWebAppIntent.getBooleanExtra(IntentConst.IS_STREAM_APP, false);
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public String obtainOriginalAppId() {
        return this.i;
    }

    public float o() {
        return (PermissionControler.a(this.mFeaturePermission, IFeature.F_DEVICE.toLowerCase()) && getActivity() != null && NetworkTypeUtil.getNetworkType(getActivity()) == 4)
                ? 1000.0f : 0.0f;
    }

    @Override // io.dcloud.common.DHInterface.IApp
    public Object obtainMgrData(IMgr.MgrType mgrType, int i, Object[] objArr) {
        return this.g.processEvent(mgrType, i, objArr);
    }
}
