package io.dcloud.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.UEH;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.constant.StringConst;
import io.dcloud.dcloud_a.EncryptionConstant;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* loaded from: classes.dex */
public final class BaseInfo {
    private static String APPS_NAME = null;
    public static String APP_DB_DATA = null;
    public static String APP_JSDATA = null;
    public static String APP_WEB_CHACHE = null;
    public static String APP_WWW_FS_DIR = null;
    private static final X500Principal DEBUG_DN;
    public static boolean ISAMU = false;
    public static String PDR = null;
    public static String REAL_PRIVATE_DOC_DIR = null;
    public static String REAL_PRIVATE_WWW_DIR = null;
    public static String REAL_PUBLIC_DOCUMENTS_DIR = null;
    public static String REAL_PUBLIC_DOWNLOADS_DIR = null;
    public static final String REL_PRIVATE_DOC_DIR = "_doc";
    public static final String REL_PRIVATE_WWW_DIR = "_www";
    public static final String REL_PUBLIC_DOCUMENTS_DIR = "_documents";
    public static final String REL_PUBLIC_DOWNLOADS_DIR = "_downloads";
    public static boolean USE_ACTIVITY_HANDLE_KEYEVENT = false;
    public static String WGTU_UPDATE_XML = null;
    public static boolean isDefaultAim = false;
    public static boolean isPostChcekShortCut = false;
    public static boolean isStreamAppRuning = false;
    public static boolean isStreamSDK = false;
    public static HashMap<String, BaseAppInfo> mBaseAppInfoSet = null;
    public static int mDeStatusBarBackground = -111111;
    public static HashMap<String, BaseAppInfo> mInstalledAppInfoSet = null;
    public static HashMap<String, BaseAppInfo> mUnInstalledAppInfoSet = null;
    public static long run5appEndTime = 0;
    public static boolean sAnimationCaptureB = true;
    public static boolean sAnimationCaptureC = true;
    public static HashMap<String, String> sAppIsTests = null;
    public static LinkedHashMap<String, Intent> sAppStateMap = null;
    public static String sBaseFsAppsPath = null;
    public static String sBaseNotificationPath = null;
    public static String sBaseResAppsFullPath = null;
    public static String sBaseResAppsPath = null;
    public static String sBaseVersion = null;
    public static String sChannel = "default";
    public static String sConfigXML;
    public static boolean sCoverApkRuning = false;
    public static String sDefaultBootApp = null;
    public static String sDocumentFullPath = null;
    public static boolean sDoingAnimation = false;
    public static String sDownloadFullPath = null;
    public static String sFontScale = "none";
    public static boolean sFullScreenChanged = false;
    public static String sGlobalUserAgent = null;
    public static String sLastRunApp = null;
    public static int sOpenedCount = 0;
    private static boolean sParsedControl = false;
    public static ArrayList<String> sRunningApp = null;
    public static SDK.IntegratedMode sRuntimeMode = null;
    public static boolean sSupportAddByHand = false;
    public static int sTimeOutCount = 0;
    public static int sTimeOutMax = 3;
    public static int sTimeoutCapture = 350;
    public static String sWap2AppTemplateVersion;
    public static boolean s_Is_DCloud_Packaged;
    public static int s_Runing_App_Count;
    public static int s_Runing_App_Count_Max;
    public static int s_Runing_App_Count_Trim;
    public static int s_Webview_Count;
    public static String s_properties;
    public static HashMap<String, CmtInfo> mLaunchers = new HashMap<>();
    public static String sSplashExitCondition = AbsoluteConst.EVENTS_LOADED;
    public static boolean sGlobalFullScreen = false;
    public static String sBaseControlPath = DeviceInfo.sBaseResRootPathName + "data/dcloud_control.xml";
    public static String sBaseWap2AppTemplatePath = null;
    public static String sBaseConfigTemplatePath = DeviceInfo.sBaseResRootPathName + "data/wap2app/__template.json";
    public static String sBaseWap2AppFilePath = DeviceInfo.sBaseResRootPathName + "data/wap2app/__wap2app.js";
    public static String sRuntimeJsPath = "io/dcloud/all.js";
    public static String sApiConfigPath = DeviceInfo.sBaseResRootPathName + "data/api.json";
    public static boolean ISDEBUG = false;

    /* loaded from: classes.dex */
    public static class CmtInfo {
        public String plusLauncher;
        public String sfd;
        public String templateVersion;
        public boolean rptCrs = true;
        public boolean rptJse = true;
        public boolean needUpdate = true;
    }

    public static String getShortCutActivity(Context context) {
        return null;
    }

    static /* synthetic */ String access$000() {
        return installAppMapToString();
    }

    public static synchronized boolean isLoadingLaunchePage() {
        synchronized (BaseInfo.class) {
        }
        return false;
    }

    public static synchronized void setLoadingLaunchePage(boolean z, String str) {
        synchronized (BaseInfo.class) {
        }
    }

    static {
        USE_ACTIVITY_HANDLE_KEYEVENT = (Build.VERSION.SDK_INT < 19) | true;
        sSupportAddByHand = true;
        sRuntimeMode = null;
        PDR = "pdr";
        WGTU_UPDATE_XML = "update.xml";
        APP_WEB_CHACHE = "webcache/";
        APP_JSDATA = "jsdata/";
        APP_DB_DATA = "dbdata/";
        APPS_NAME = "apps/";
        ISAMU = false;
        s_Is_DCloud_Packaged = false;
        s_Webview_Count = 0;
        s_Runing_App_Count = 0;
        s_Runing_App_Count_Max = 3;
        s_Runing_App_Count_Trim = 0;
        sRunningApp = null;
        sBaseResAppsFullPath = null;
        sBaseResAppsPath = null;
        sBaseFsAppsPath = null;
        sDownloadFullPath = null;
        sDocumentFullPath = "";
        sCoverApkRuning = false;
        s_properties = "/data/dcloud_properties.xml";
        sConfigXML = "manifest.json";
        APP_WWW_FS_DIR = "www/";
        sBaseNotificationPath = null;
        REAL_PRIVATE_WWW_DIR = "www/";
        REAL_PRIVATE_DOC_DIR = "doc/";
        REAL_PUBLIC_DOCUMENTS_DIR = "documents/";
        REAL_PUBLIC_DOWNLOADS_DIR = "downloads/";
        sAppIsTests = new HashMap<>();
        isStreamAppRuning = false;
        isDefaultAim = Build.VERSION.SDK_INT >= 21;
        run5appEndTime = 0L;
        sAppStateMap = new LinkedHashMap<>();
        sParsedControl = false;
        mUnInstalledAppInfoSet = new HashMap<>();
        mInstalledAppInfoSet = new HashMap<>();
        mBaseAppInfoSet = new HashMap<>();
        isStreamSDK = false;
        DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
    }

    public static String getCrashLogsPath(Context context) {
        if (isForQihooBrowser(context)) {
            return DeviceInfo.sBaseFsRootPath + "logs/browser_qihoo/";
        }
        if (isForQihooHelper(context)) {
            return DeviceInfo.sBaseFsRootPath + "logs/appsotre_qihoo/";
        }
        if (isStreamApp(context)) {
            return DeviceInfo.sBaseFsRootPath + "logs/streamapps/";
        }
        return DeviceInfo.sBaseFsRootPath + "/log/";
    }

    public static void updateBaseInfo() {
        if (!APPS_NAME.equals("/")) {
            sBaseResAppsFullPath = DeviceInfo.sBaseResRootFullPath + APPS_NAME;
            String str = DeviceInfo.sBaseFsRootPath + APPS_NAME;
            sBaseFsAppsPath = str;
            sBaseWap2AppTemplatePath = str;
            sBaseResAppsPath = DeviceInfo.sBaseResRootPathName + APPS_NAME;
            DHFile.createNewFile(sBaseFsAppsPath);
        }
        String str2 = sDownloadFullPath;
        if (str2 != null && str2.indexOf("sdcard/") > -1) {
            sDownloadFullPath = PdrUtil.appendByDeviceRootDir(sDownloadFullPath);
        } else {
            sDownloadFullPath = DeviceInfo.sBaseFsCachePath + REAL_PUBLIC_DOWNLOADS_DIR;
        }
        String str3 = sDocumentFullPath;
        if (str3 != null && str3.indexOf("sdcard/") > -1) {
            sDocumentFullPath = PdrUtil.appendByDeviceRootDir(sDocumentFullPath);
        } else {
            sDocumentFullPath = DeviceInfo.sBaseFsRootPath + REAL_PUBLIC_DOCUMENTS_DIR;
        }
    }

    public static void updateBaseInfoByApp(String str, String str2) {
        if (str != null) {
            PDR = str;
        }
        if (str2 != null) {
            DeviceInfo.sBaseFsRootPath = str2;
        }
        DeviceInfo.initBaseFsRootPath();
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0185  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x018a  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0195  */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v7, types: [boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static String[] parseControl(ICore core, ICore.ICoreStatusListener listener) {
        final String KEY_APPS = "apps";
        final String KEY_MAIN_PATH = "Main_Path";
        String[] result = null;

        if (!BaseInfo.sParsedControl) {
            boolean hasUpdatedApp = false;
            boolean isFirstApp = true;
            try {
                InputStream configStream = PlatformUtil.getResInputStream(BaseInfo.sBaseControlPath);
                if (configStream != null) {
                    XmlUtil.DHNode root = XmlUtil.XML_Parser(configStream);
                    IOUtil.close(configStream);
                    if (root != null) {
                        // Читаємо атрибути з кореневого вузла
                        BaseInfo.ISDEBUG = Boolean.parseBoolean(XmlUtil.getAttributeValue(root, "debug", "false"));
                        BaseInfo.isStreamSDK = !PdrUtil.isEmpty(XmlUtil.getAttributeValue(root, "streamapp", null));
                        BaseInfo.ISAMU = Boolean.parseBoolean(XmlUtil.getAttributeValue(root, "amu", "false"));

                        String channel = AndroidResources.getMetaValue("DCLOUD_STREAMAPP_CHANNEL");
                        if (PdrUtil.isEmpty(channel)) {
                            channel = XmlUtil.getAttributeValue(root, "channel", BaseInfo.sChannel);
                        }
                        BaseInfo.sChannel = channel;

                        BaseInfo.sSplashExitCondition = XmlUtil.getAttributeValue(root, "back", BaseInfo.sSplashExitCondition);
                        BaseInfo.s_Is_DCloud_Packaged = Boolean.parseBoolean(XmlUtil.getAttributeValue(root, "ns", "false"));

                        String fontScale = XmlUtil.getAttributeValue(root, "fontscale", BaseInfo.sFontScale);
                        if (!TextUtils.isEmpty(fontScale)) {
                            BaseInfo.sFontScale = fontScale;
                        }

                        BaseInfo.sSupportAddByHand = BaseInfo.sSupportAddByHand && BaseInfo.ISDEBUG;

                        if (TextUtils.isEmpty(BaseInfo.sBaseVersion)) {
                            BaseInfo.sBaseVersion = XmlUtil.getAttributeValue(root, "version");
                        }

                        XmlUtil.DHNode appsNode = XmlUtil.getElement(root, "apps");
                        String maxStr = XmlUtil.getAttributeValue(appsNode, "max");
                        BaseInfo.s_Runing_App_Count_Max = PdrUtil.parseInt(maxStr, BaseInfo.s_Runing_App_Count_Max);
                        if (BaseInfo.s_Runing_App_Count_Max <= 0) {
                            BaseInfo.s_Runing_App_Count_Max = Integer.MAX_VALUE;
                        }

                        String trimStr = XmlUtil.getAttributeValue(appsNode, "trim");
                        BaseInfo.s_Runing_App_Count_Trim = PdrUtil.parseInt(trimStr, BaseInfo.s_Runing_App_Count_Trim);
                        if (BaseInfo.s_Runing_App_Count_Trim <= 0) {
                            BaseInfo.s_Runing_App_Count_Trim = 0;
                        }

                        ArrayList<XmlUtil.DHNode> apps = XmlUtil.getElements(appsNode, "app");
                        if (apps != null) {
                            int size = apps.size();
                            String[] appIds = null;
                            boolean isCoverRun = false;
                            boolean baseCheck = true;
                            for (int i = 0; i < size; i++) {
                                XmlUtil.DHNode appNode = apps.get(i);
                                String appid = XmlUtil.getAttributeValue(appNode, "appid");
                                if (i == 0) {
                                    appIds = new String[] { appid };
                                    BaseInfo.sDefaultBootApp = appid;
                                    BaseInfo.updateBaseInfoByApp(appid, null);
                                    BaseInfo.sCoverApkRuning = DeviceInfo.checkCoverInstallApk();
                                    baseCheck = BaseInfo.isBase(DCloudApplication.getInstance());
                                    if (baseCheck) {
                                        baseCheck = !BaseInfo.ISDEBUG;
                                        isCoverRun = BaseInfo.sCoverApkRuning && baseCheck;
                                    }
                                    BaseInfo.loadInstalledAppInfo(core);
                                }

                                String appver = XmlUtil.getAttributeValue(appNode, "appver");
                                BaseInfo.BaseAppInfo appInfo = new BaseInfo.BaseAppInfo(appid, appver);
                                BaseInfo.mBaseAppInfoSet.put(appid, appInfo);

                                if (isCoverRun && BaseInfo.mInstalledAppInfoSet.containsKey(appid)) {
                                    BaseInfo.BaseAppInfo installed = BaseInfo.mInstalledAppInfoSet.get(appid);
                                    if (!BaseInfo.sCoverApkRuning && !appInfo.high(installed)) {
                                        appInfo.mMoreRecent = false;
                                        BaseInfo.mBaseAppInfoSet.remove(appid);
                                    } else {
                                        Log.i(KEY_MAIN_PATH, appid + " App has new version! it is " + appInfo.mAppVer);
                                        appInfo.clearBundleData();
                                        BaseInfo.mInstalledAppInfoSet.remove(appid);
                                        hasUpdatedApp = true;
                                    }
                                }
                            }
                            result = appIds;
                        }
                    }
                } else {
                    DeviceInfo.initBaseFsRootPath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            BaseInfo.sParsedControl = true;
            if (BaseInfo.ISDEBUG) {
                Logger.setOpen(true);
            }
            if (hasUpdatedApp) {
                PlatformUtil.setBundleData(BaseInfo.PDR, "apps", BaseInfo.installAppMapToString());
            }
        }

        if (listener != null) {
            Log.i(KEY_MAIN_PATH, "will exc coreListener.onCoreReady");
            listener.onCoreReady(core);
        }

        return result;
    }

    public static void loadInstalledAppInfo(ICore iCore) {
        String[] listFsAppsFiles;
        String bundleData = PlatformUtil.getBundleData(PDR, AbsoluteConst.XML_APPS);
        if (bundleData != null) {
            for (String str : bundleData.split("\\|")) {
                BaseAppInfo baseAppInfo = new BaseAppInfo(str, PlatformUtil.getBundleData(PDR, str + "_" + AbsoluteConst.XML_APPVER));
                boolean parseBoolean = Boolean.parseBoolean(PlatformUtil.getBundleData(PDR, str + "_" + AbsoluteConst.XML_DELETED));
                baseAppInfo.mDeleted = parseBoolean;
                if (parseBoolean) {
                    mUnInstalledAppInfoSet.put(str, baseAppInfo);
                } else if (!PdrUtil.isEmpty(str)) {
                    mInstalledAppInfoSet.put(str, baseAppInfo);
                }
            }
        }
        if (iCore == null || iCore.isStreamAppMode() || !sSupportAddByHand || (listFsAppsFiles = PlatformUtil.listFsAppsFiles(sBaseFsAppsPath)) == null) {
            return;
        }
        for (String str2 : listFsAppsFiles) {
            if (!mInstalledAppInfoSet.containsKey(str2)) {
                mInstalledAppInfoSet.put(str2, new BaseAppInfo(str2, "0"));
            }
        }
    }

    private static String installAppMapToString() {
        StringBuffer stringBuffer = new StringBuffer();
        Set<String> keySet = mInstalledAppInfoSet.keySet();
        int size = keySet.size();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next()).append("|");
        }
        if (size > 1) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public static void saveInstalledAppInfo() {
        PlatformUtil.setBundleData(PDR, AbsoluteConst.XML_APPS, installAppMapToString());
    }

    public static void loadCustomPath(String str) {
        String[] split = str.split("\\||\r\n|\n");
        if (split.length > 0) {
            for (String str2 : split) {
                String[] split2 = str2.split("=");
                String str3 = split2[0];
                String str4 = split2[1];
                if (str3.equals(CustomPath.CUSTOM_PATH_CONTROL_XML)) {
                    sBaseControlPath = str4;
                } else if (str3.equals(CustomPath.CUSTOM_PATH_FS_ROOT)) {
                    DeviceInfo.sBaseFsRootPath = DeviceInfo.sDeviceRootDir + "/" + str4.substring(str4.indexOf("sdcard/") + 7);
                } else if (str3.equals(CustomPath.CUSTOM_PATH_DOWNLOADS)) {
                    REAL_PUBLIC_DOWNLOADS_DIR = str4;
                } else if (str3.equals(CustomPath.CUSTOM_PATH_DOCUMENTS)) {
                    REAL_PUBLIC_DOCUMENTS_DIR = str4;
                } else if (str3.equals(CustomPath.CUSTOM_PATH_DOC)) {
                    REAL_PRIVATE_DOC_DIR = str4;
                } else if (str3.equals(CustomPath.CUSTOM_PATH_APPS)) {
                    APPS_NAME = str4;
                } else if (str3.equals(CustomPath.CUSTOM_PATH_APP_WWW)) {
                    APP_WWW_FS_DIR = str4;
                    REAL_PRIVATE_WWW_DIR = str4;
                }
            }
            DeviceInfo.updatePath();
        }
    }

    public static boolean isBase(Context context) {
        return context.getPackageName().equals("io.dcloud.HBuilder");
    }

    public static boolean existsBase() {
        try {
            return new File(Environment.getExternalStorageDirectory() + "/Android/data/io.dcloud.HBuilder").exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isQihooLifeHelper(Context context) {
        return context.getPackageName().equals("com.qihoo.life");
    }

    public static boolean useStreamAppStatistic(Context context) {
        boolean z = false;
        try {
            z = false | context.getPackageName().equals(DeviceInfo.SWITCH_DIRECTORY) | context.getPackageName().equals("com.qihoo.appstore") | context.getPackageName().equals("io.dcloud.streamapps") | context.getPackageName().equals("io.dcloud.HBuilder") | context.getPackageName().equals("com.qihoo.life");
            return context.getPackageName().equals("com.qihoo.browser") | z;
        } catch (Throwable th) {
            th.printStackTrace();
            return z;
        }
    }

    public static boolean isTrafficFree() {
        try {
            Context dCloudApplication = DCloudApplication.getInstance();
            if (dCloudApplication != null) {
                Context applicationContext = dCloudApplication.getApplicationContext();
                if (applicationContext.getPackageName().equals("com.qihoo.life")) {
                    return !isWifi(applicationContext);
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isStreamApp(Context context) {
        return context.getPackageName().equals("io.dcloud.streamapps");
    }

    public static boolean isShowTitleBarForStreamAppSDK() {
        boolean isEquals = PdrUtil.isEquals("standard", AndroidResources.getMetaValue("DCLOUD_STREAMAPP_ACTIONBAR"));
        Logger.e("StreamSdk", "isshowtitlebar==" + isEquals);
        return isEquals;
    }

    public static boolean isForQihooHelper(Context context) {
        return context.getPackageName().equals(DeviceInfo.SWITCH_DIRECTORY) || context.getPackageName().equals("com.qihoo.appstore");
    }

    public static boolean isShowTitleBar(Context context) {
        return isForQihooBrowser(context) || isShowTitleBarForStreamAppSDK();
    }

    public static boolean isForQihooBrowser(Context context) {
        return context.getPackageName().equals("com.qihoo.browser") || context.getPackageName().equals("com.qihoo.bclplugintest") || context.getPackageName().equals(DeviceInfo.SWITCH_DIRECTORY);
    }

    public static boolean isForQihooHelper5_0(Context context) {
        return context.getPackageName().equals(DeviceInfo.SWITCH_DIRECTORY);
    }

    public static String getRunningActivity(Context context) {
        return isForQihooHelper(context) ? DCloudAdapterUtil.isPlugin() ? "io.src.dcloud.adapter.StreamAppActivity" : "io.dcloud.appstream.StreamAppListActivity" : "io.dcloud.appstream.StreamAppMainActivity";
    }

    public static boolean isGlobal(Context context) {
        return isForQihooBrowser(context) || isForQihooHelper(context) || isStreamApp(context) || TextUtils.equals("io.dcloud.html5pframework", context.getPackageName()) || isStreamSDK;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }

    /* loaded from: classes.dex */
    public static class BaseAppInfo {
        public String mAppVer;
        String mAppid;
        public boolean mMoreRecent = true;
        public boolean mDeleted = false;

        public BaseAppInfo(String str, String str2) {
            this.mAppid = null;
            this.mAppVer = null;
            this.mAppid = str;
            this.mAppVer = str2;
        }

        boolean high(BaseAppInfo baseAppInfo) {
            return compareVersion(this.mAppVer, baseAppInfo.mAppVer);
        }

        public static final boolean compareVersion(String str, String str2) {
            try {
                String[] split = str.split("\\.");
                String[] split2 = str2.split("\\.");
                int length = split.length;
                int length2 = split2.length;
                for (int i = 0; i < length; i++) {
                    if (i < length2) {
                        int parseInt = Integer.parseInt(split[i]);
                        int parseInt2 = Integer.parseInt(split2[i]);
                        if (parseInt <= parseInt2) {
                            if (parseInt < parseInt2) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        public void saveToBundleData() {
            PlatformUtil.setBundleData(BaseInfo.PDR, this.mAppid + "_" + AbsoluteConst.XML_APPVER, this.mAppVer);
            PlatformUtil.setBundleData(BaseInfo.PDR, this.mAppid + "_" + AbsoluteConst.XML_DELETED, String.valueOf(this.mDeleted));
            String access$000 = BaseInfo.access$000();
            StringBuffer stringBuffer = new StringBuffer();
            if (!PdrUtil.isEmpty(access$000)) {
                stringBuffer.append(access$000).append("|");
            }
            stringBuffer.append(this.mAppid);
            PlatformUtil.setBundleData(BaseInfo.PDR, AbsoluteConst.XML_APPS, stringBuffer.toString());
        }

        public void clearBundleData() {
            PlatformUtil.removeBundleData(BaseInfo.PDR, this.mAppid + "_" + AbsoluteConst.XML_APPVER);
        }
    }

    public static void putLauncherData(String str, String str2) {
        getCmitInfo(str).plusLauncher = str2;
    }

    public static String getLauncherData(String str) {
        CmtInfo cmtInfo = mLaunchers.get(str);
        return (cmtInfo == null || TextUtils.isEmpty(cmtInfo.plusLauncher)) ? "default" : cmtInfo.plusLauncher;
    }

    public static String getLaunchType(Intent intent) {
        if (intent == null) {
            return "default";
        }
        Uri data = intent.getData();
        String stringExtra = intent.hasExtra(IntentConst.RUNING_STREAPP_LAUNCHER) ? intent.getStringExtra(IntentConst.RUNING_STREAPP_LAUNCHER) : "default";
        if (data != null && !URLUtil.isNetworkUrl(data.toString())) {
            if (!intent.getBooleanExtra(IntentConst.FROM_BARCODE, false)) {
                return "scheme";
            }
        } else {
            if (intent.getExtras() == null) {
                return stringExtra;
            }
            if (!TextUtils.isEmpty(intent.getStringExtra(IntentConst.STREAM_LAUNCHER))) {
                return intent.getStringExtra(IntentConst.STREAM_LAUNCHER);
            }
            if (intent.getBooleanExtra(IntentConst.FROM_SHORT_CUT_STRAT, false)) {
                return IApp.ConfigProperty.CONFIG_SHORTCUT;
            }
            if (!intent.getBooleanExtra(IntentConst.FROM_BARCODE, false)) {
                return intent.getIntExtra(IntentConst.START_FROM, -1) == 3 ? "push" : intent.getIntExtra(IntentConst.START_FROM, -1) == 5 ? "myapp" : stringExtra;
            }
        }
        return "barcode";
    }

    public static String getLastKey(LinkedHashMap<String, Intent> linkedHashMap) {
        String str = null;
        if (linkedHashMap != null) {
            Iterator<String> it = linkedHashMap.keySet().iterator();
            while (it.hasNext()) {
                str = it.next();
            }
        }
        return str;
    }

    public static void clearData() {
        sParsedControl = false;
        sAppStateMap.clear();
        sGlobalFullScreen = false;
        UEH.sInited = false;
        sLastRunApp = null;
        sRunningApp = null;
        s_Webview_Count = 0;
        s_Runing_App_Count = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean isDebugSignature(Context context) {
        try {
            Signature[] signatures = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (Signature signature : signatures) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signature.toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                if (cert.getSubjectX500Principal().equals(DEBUG_DN)) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException | CertificateException | RuntimeException e) {
            // Логування можна додати тут за потреби
            return false;
        }
    }


    public static boolean isWap2AppCompleted(String str) {
        return isWap2AppAppid(str) && InvokeExecutorHelper.AppStreamUtils.invoke("hasCompletedFile", str, false);
    }

    public static boolean isWap2AppAppid(String str) {
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return lowerCase.startsWith("__w2a__") || "H52588A9C".equalsIgnoreCase(lowerCase) || "H5B5EEFBB".equalsIgnoreCase(lowerCase) || "H5A0B1958".equalsIgnoreCase(lowerCase) || "H5EA885FD".equalsIgnoreCase(lowerCase) || "H592E7F63".equalsIgnoreCase(lowerCase) || "H5BCD03E4".equalsIgnoreCase(lowerCase);
    }

    public static boolean isTest(String str) {
        return sAppIsTests.containsKey(str);
    }

    public static boolean checkAppIsTest(String str) {
        return new File(StringConst.STREAMAPP_KEY_ROOTPATH + "apps/" + str + "/.test").exists();
    }

    public static boolean checkTestOpenFile() {
        return new File(DeviceInfo.sDeviceRootDir + "/.system/d85a37c6-afdc-11e6-80f5-76304dec7eb7").exists();
    }

    public static void removeTestFile(String str) {
        File file = new File(StringConst.STREAMAPP_KEY_ROOTPATH + "apps/" + str + "/.test");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void createAppTestFile(String str) {
        File file = new File(StringConst.STREAMAPP_KEY_ROOTPATH + "apps/" + str + "/.test");
        if (file.exists()) {
            return;
        }
        try {
            file.mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getkey(Context context) {
        return EncryptionConstant.a(context);
    }

    public static String getiv() {
        return EncryptionConstant.a();
    }

    public static CmtInfo getCmitInfo(String str) {
        CmtInfo cmtInfo = mLaunchers.get(str);
        if (cmtInfo != null) {
            return cmtInfo;
        }
        CmtInfo cmtInfo2 = new CmtInfo();
        mLaunchers.put(str, cmtInfo2);
        return cmtInfo2;
    }
}
