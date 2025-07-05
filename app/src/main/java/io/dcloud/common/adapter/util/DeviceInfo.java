package io.dcloud.common.adapter.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresPermission;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.feature.internal.sdk.SDK;

/* loaded from: classes.dex */
public class DeviceInfo {
    private static final String CDMA_DATA_NETWORK = "cdma";
    private static String CONNECTION_CELL2G = null;
    private static String CONNECTION_CELL3G = null;
    private static String CONNECTION_CELL4G = null;
    private static String CONNECTION_ETHERNET = null;
    private static String CONNECTION_UNKNOW = null;
    private static final String DEFAULT_DATA_NETWORK = "default_data_network";
    public static float DEFAULT_FONT_SIZE = 0.0f;
    public static String DEVICESTATUS_JS = null;
    public static final String FILE_PROTOCOL = "file://";
    public static int HARDWAREACCELERATED_VIEW = 0;
    public static int HARDWAREACCELERATED_WINDOW = 0;
    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HTTP_PROTOCOL = "http://";
    private static String NETTYPE_NONE = null;
    private static String NETTYPE_WIFI = null;
    private static final String NONE_DATA_NETWORK = "none";
    public static final int OSTYPE_ANDROID = 0;
    public static final int OSTYPE_LEOS10 = 4;
    public static final int OSTYPE_OMS10 = 3;
    public static final int OSTYPE_OMS15 = 2;
    public static final int OSTYPE_OMS20 = 1;
    private static final String SAVED_DATA_NETWORK = "saved_data_network";
    public static final String SWITCH_DIRECTORY = "io.dcloud.streamapp";
    private static final String TAG = "DeviceInfo";
    public static float dpiX;
    public static float dpiY;
    public static boolean isSwitchDirectory;
    public static int osType;
    public static Context sApplicationContext;
    public static String sBSSID;
    public static String sBaseFsCachePath;
    public static String sBaseFsRootFullPath;
    public static String sBaseFsRootPath;
    public static String sBaseResRootFullPath;
    public static String sBaseResRootPathName;
    public static String sCacheRootDir;
    static ConnectivityManager sConnectMgr;
    public static float sDensity;
    public static String sDeviceRootDir;
    public static String sIMEI;
    public static String sIMSI;
    public static String sPackageName;
    public static Paint sPaint;
    public static char sSeparatorChar;
    public static int sStatusBarHeight;
    public static String sWifiAddr;
    public static int sDeviceSdkVer = Build.VERSION.SDK_INT;
    public static String sModel = Build.MODEL;
    public static String sBrand = Build.BRAND;
    public static long sTotalMem = -1;
    public static int sCoreNums = -1;
    public static String sVendor = Build.MANUFACTURER;
    public static String sVersion_release = Build.VERSION.RELEASE;
    public static String sLanguage = Locale.getDefault().getLanguage();
    public static boolean sNetWorkInited = false;
    private static GsmCellLocation sCellLocation = null;
    private static final String GSM_DATA_NETWORK = "gsm";
    public static String sDeftDataNetwork = GSM_DATA_NETWORK;
    public static String sSimOperator = null;

    public static String getPlusCache() {
        return "window.plus.cache = navigator.plus.cache = (function(mkey){return {clear : function(clearCB){var callbackid = mkey.helper.callbackid( function(args){if ( clearCB ) {clearCB()};}, null);mkey.exec('Cache', 'clear', [callbackId]);},calculate : function(calculateCB){var callbackid = mkey.helper.callbackid( function(args){if ( calculateCB ) {calculateCB(args)};}, null);mkey.exec('Cache', 'calculate', [callbackid]);},setMaxSize : function (size) {mkey.exec('Cache', 'setMaxSize', [size]);}};})(window.__Mkey__);";
    }

    static {
        char c = File.separatorChar;
        sSeparatorChar = c;
        sBaseResRootPathName = String.valueOf(c);
        sPackageName = null;
        DEVICESTATUS_JS = null;
        Paint paint = new Paint();
        sPaint = paint;
        DEFAULT_FONT_SIZE = paint.getTextSize();
        osType = 0;
        sApplicationContext = null;
        sConnectMgr = null;
        isSwitchDirectory = false;
        CONNECTION_UNKNOW = "0";
        NETTYPE_NONE = "1";
        CONNECTION_ETHERNET = "2";
        NETTYPE_WIFI = "3";
        CONNECTION_CELL2G = "4";
        CONNECTION_CELL3G = "5";
        CONNECTION_CELL4G = "6";
        sStatusBarHeight = 0;
        HARDWAREACCELERATED_WINDOW = 0;
        HARDWAREACCELERATED_VIEW = 1;
    }

    public static void init(Context context) {
        sDeviceSdkVer = Build.VERSION.SDK_INT;
        String str = Build.MODEL;
        sModel = str;
        if ("OMAP_SS".equals(str)) {
            osType = 1;
        } else if ("OMS1_5".equals(sModel)) {
            osType = 2;
        } else if ("generic".equals(sModel)) {
            osType = 3;
        }
        sBrand = Build.BRAND;
        sVendor = Build.MANUFACTURER;
        sLanguage = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        sDensity = displayMetrics.density;
        dpiX = displayMetrics.xdpi;
        dpiY = displayMetrics.ydpi;
        Logger.i(TAG, "init() sWifiAddr=" + sWifiAddr + ";sDeviceSdkVer=" + sDeviceSdkVer + ";sModel=" + sModel + ";sBrand=" + sBrand + ";sVendor=" + sVendor + ";sLanguage=" + sLanguage + ";dpiX=" + dpiX + ";dpiY=" + dpiY + ";package=" + context.getPackageName());
        sConnectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static void initBaseFsRootPath() {
        initPath(sApplicationContext);
    }

    public static void initPath(Context context) {
        String str;
        String str2;
        AndroidResources.initAndroidResources(context);
        sApplicationContext = context;
        sPackageName = context.getPackageName();
        if (isSwitchDirectory) {
            sPackageName = SWITCH_DIRECTORY;
        }
        if (sDeviceRootDir != null) {
            return;
        }
        BaseInfo.isBase(sApplicationContext);
        boolean equals = sPackageName.equals("io.dcloud.HBuilder");
        boolean isSDcardExists = isSDcardExists();
        boolean z = BaseInfo.ISDEBUG;
        if (equals) {
            str = ".HBuilder/";
        } else {
            BaseInfo.isForQihooHelper(sApplicationContext);
            str = "";
        }
        String str3 = sBaseFsRootPath;
        if (isSDcardExists) {
            if (str3 == null) {
                if (sDeviceSdkVer >= 8) {
                    sDeviceRootDir = Environment.getExternalStorageDirectory().getPath();
                    File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
                    if (externalFilesDir != null) {
                        String absolutePath = externalFilesDir.getAbsolutePath();
                        str2 = absolutePath.substring(0, absolutePath.indexOf(context.getPackageName())) + sPackageName + sSeparatorChar + str;
                        if (!str2.startsWith(sDeviceRootDir)) {
                            sDeviceRootDir = str2.substring(0, str2.indexOf("Android") - 1);
                        }
                    } else {
                        str2 = "/sdcard/android/data/" + sPackageName + sSeparatorChar + str;
                    }
                    str3 = str2;
                } else {
                    str3 = "/sdcard/android/data/" + sPackageName + sSeparatorChar + str;
                }
            }
            if (!isSwitchDirectory && BaseInfo.isGlobal(sApplicationContext)) {
                str3 = sDeviceRootDir + "/.system/streamapp/";
            }
            sCacheRootDir = sDeviceRootDir;
            sBaseFsCachePath = str3;
            sBaseFsRootPath = str3;
        } else {
            BaseInfo.ISDEBUG = false;
            String str4 = context.getFilesDir().getParent() + sSeparatorChar;
            sDeviceRootDir = str4;
            sCacheRootDir = str4;
            String str5 = sDeviceRootDir + str;
            sBaseFsRootPath = str5;
            sBaseFsCachePath = str5;
        }
        updatePath();
    }

    public static void updatePath() {
        sBaseFsRootFullPath = FILE_PROTOCOL + sBaseFsRootPath;
        sBaseResRootFullPath = SDK.ANDROID_ASSET;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("sPackageName=" + sPackageName).append(";\n");
        stringBuffer.append("sDeviceRootDir=" + sDeviceRootDir).append(";\n");
        stringBuffer.append("sBaseFsRootPath=" + sBaseFsRootPath).append(";\n");
        stringBuffer.append("sBaseFsRootFullPath=" + sBaseFsRootFullPath).append(";\n");
        stringBuffer.append("sBaseResRootFullPath=" + sBaseResRootFullPath).append(";\n");
        Logger.d(TAG, stringBuffer.toString());
        BaseInfo.updateBaseInfo();
    }

    private static String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public static boolean isOMS() {
        int i = osType;
        return i == 1 || i == 2 || i == 3;
    }

    public static boolean wifiEnabled() {
        return (PdrUtil.isEmpty(sWifiAddr) || PdrUtil.isEmpty(sBSSID)) ? false : true;
    }

    public static boolean isSDcardExists() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static String getNetWorkType() {
        String str = NETTYPE_NONE;
        ConnectivityManager connectivityManager = sConnectMgr;
        if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null) {
            return str;
        }
        String str2 = CONNECTION_UNKNOW;
        if (sConnectMgr.getActiveNetworkInfo().getType() == 1) {
            return NETTYPE_WIFI;
        }
        if (sConnectMgr.getActiveNetworkInfo().getType() != 0) {
            return str2;
        }
        int subtype = sConnectMgr.getActiveNetworkInfo().getSubtype();
        switch (subtype) {
            case 1:
            case 2:
            case 4:
            case 7:
                return CONNECTION_CELL2G;
            case 3:
            case 8:
                return CONNECTION_CELL3G;
            case 5:
            case 6:
            case 12:
            case 14:
                return CONNECTION_CELL3G;
            case 9:
            case 10:
            case 11:
            case 13:
            case 15:
                return CONNECTION_CELL4G;
            case 16:
            default:
                return "" + subtype;
            case 17:
            case 18:
                return CONNECTION_CELL3G;
        }
    }

    public static String getCurrentAPN() {
        return NetworkTypeUtil.getCurrentAPN(DCloudApplication.getInstance().getApplicationContext());
    }

    public static boolean startsWithSdcard(String str) {
        return str.startsWith(sDeviceRootDir) || str.startsWith("/sdcard/") || str.startsWith("mnt/sdcard/") || str.startsWith(sCacheRootDir);
    }

    public static String getUpdateIMSI() {
        try {
            sIMSI = ((TelephonyManager) sApplicationContext.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sIMSI;
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public static void initGsmCdmaCell() {
        if (sNetWorkInited) {
            return;
        }
        String string = Settings.System.getString(sApplicationContext.getContentResolver(), DEFAULT_DATA_NETWORK);
        sDeftDataNetwork = string;
        if (string == null) {
            sDeftDataNetwork = GSM_DATA_NETWORK;
        }
        Logger.i("DefaultDataNetwork：", sDeftDataNetwork);
        TelephonyManager telephonyManager = (TelephonyManager) sApplicationContext.getSystemService(Context.TELEPHONY_SERVICE);
        int phoneType = telephonyManager.getPhoneType();
        sIMEI = TelephonyUtil.getIMEI(sApplicationContext, false);
        sIMSI = TelephonyUtil.getIMSI(sApplicationContext);
        sSimOperator = telephonyManager.getSimOperator();
        if ("none".equals(sDeftDataNetwork)) {
            sDeftDataNetwork = GSM_DATA_NETWORK;
            if (phoneType == 1) {
                sCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            }
        }
        if (sIMEI == null) {
            sIMEI = "";
        }
        if (sIMSI == null) {
            sIMSI = "";
        }
        Logger.d(TAG, "IMEI=" + sIMEI);
        Logger.d(TAG, "IMSI=" + sIMSI);
        sNetWorkInited = true;
    }

    public static int getNumCores() {
        int i = sCoreNums;
        if (i != -1) {
            return i;
        }
        try {
            File[] listFiles = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() { // from class: io.dcloud.common.adapter.util.DeviceInfo.1CpuFilter
                @Override // java.io.FileFilter
                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]", file.getName());
                }
            });
            sCoreNums = listFiles.length;
            return listFiles.length;
        } catch (Exception unused) {
            return 1;
        }
    }

    public static long getAvailMemory() {
        Context context = sApplicationContext;
        if (context == null) {
            return 0L;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static long getTotalMemory() {
        long j = sTotalMem;
        if (j != -1) {
            return j;
        }
        long j2 = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            j2 = Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).intValue();
            bufferedReader.close();
            sTotalMem = j2;
            return j2;
        } catch (Exception unused) {
            return j2;
        }
    }

    public static String getDevicestatus_js() {
        DEVICESTATUS_JS = "(function(p){p.device.imei='%s';p.device.uuid='%s';p.device.imsi=['%s'];p.device.model='%s';p.device.vendor='%s';p.os.language='%s';p.os.version='%s';p.os.name='%s';p.os.vendor='%s';})(((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus));";
        String str = sIMEI;
        String format = String.format("(function(p){p.device.imei='%s';p.device.uuid='%s';p.device.imsi=['%s'];p.device.model='%s';p.device.vendor='%s';p.os.language='%s';p.os.version='%s';p.os.name='%s';p.os.vendor='%s';})(((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus));", str, str, sIMSI, sModel, sVendor, sLanguage, sVersion_release, "Android", "Google");
        DEVICESTATUS_JS = format;
        return format;
    }

    public static void updateStatusBarHeight(Activity activity) {
        if (sStatusBarHeight == 0) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int i = rect.top;
            sStatusBarHeight = i;
            if (i <= 0) {
                sStatusBarHeight = getStatusHeight(activity);
            }
        }
    }

    public static boolean checkCoverInstallApk() {
        boolean z;
        String bundleData = PlatformUtil.getBundleData(BaseInfo.PDR, "last_apk_modify_date");
        long lastModified = new File(sApplicationContext.getPackageCodePath()).lastModified();
        Logger.d(TAG, "old_apk_modify_date=" + bundleData);
        if (PdrUtil.isEquals(bundleData, String.valueOf(lastModified))) {
            z = false;
        } else {
            PlatformUtil.setBundleData(BaseInfo.PDR, "last_apk_modify_date", String.valueOf(lastModified));
            bundleData = Logger.generateTimeStamp(Logger.TIMESTAMP_YYYY_MM_DD_HH_MM_SS_SSS, new Date(lastModified));
            Logger.d(TAG, "new_apk_modify_date=" + lastModified);
            z = true;
        }
        Logger.d(TAG, "Apk Modify Date=" + bundleData + ";_ret=" + z);
        return z;
    }

    public static void openHardwareAccelerated(Activity activity, int i, Object obj) {
        if (i == HARDWAREACCELERATED_WINDOW) {
            Window window = (Window) obj;
            if (window == null) {
                window = activity != null ? activity.getWindow() : null;
            }
            if (window != null) {
                window.setFlags(16777216, 16777216);
                return;
            }
            return;
        }
        if (i != HARDWAREACCELERATED_VIEW || sDeviceSdkVer < 11) {
            return;
        }
        ((View) obj).setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public static void closeHardwareAccelerated(Activity activity, int i, Object obj) {
        if (i == HARDWAREACCELERATED_WINDOW) {
            Window window = (Window) obj;
            if (window == null) {
                window = activity != null ? activity.getWindow() : null;
            }
            if (window != null) {
                window.clearFlags(16777216);
                return;
            }
            return;
        }
        if (i != HARDWAREACCELERATED_VIEW || sDeviceSdkVer < 11) {
            return;
        }
        ((View) obj).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public static void hideIME(View view) {
        IBinder windowToken;
        Context context = sApplicationContext;
        if (context != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view == null || (windowToken = view.getWindowToken()) == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showIME(final View view) {
        new Timer().schedule(new TimerTask() { // from class: io.dcloud.common.adapter.util.DeviceInfo.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (DeviceInfo.sApplicationContext != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) DeviceInfo.sApplicationContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (view != null) {
                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }, 250L);
    }

    public static int getStatusHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getDeivceSuitablePixel(Activity activity, int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (i * displayMetrics.density);
    }

    public static String getBuildValue(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getDeclaredMethod("get", String.class).invoke(cls, str);
        } catch (Exception unused) {
            return null;
        }
    }
}
