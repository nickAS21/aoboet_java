package io.dcloud.common.adapter.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.common.util.ThreadPool;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* loaded from: classes.dex */
public class UEH {
    private static final String CRASH_DIRECTORY = "crash/";
    private static final boolean SAVE_CRASH_LOG = true;
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINESE);
    public static boolean sInited = false;

    private static void commitUncatchException(File file) {
    }

    public static void catchUncaughtException(final Context context) {
        if (sInited) {
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: io.dcloud.common.adapter.util.UEH.1
            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread thread, Throwable th) {
                UEH.handleUncaughtException(context, th);
                try {
                    if (BaseInfo.getCmitInfo(BaseInfo.sLastRunApp).rptCrs) {
                        UEH.commitUncatchException(context, th);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e("UncaughtExceptionHandler", "commitUncatchException");
                }
                th.printStackTrace();
                Logger.e("UncaughtExceptionHandler", th.toString());
                Process.killProcess(Process.myPid());
            }
        });
        sInited = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File handleUncaughtException(Context context, Throwable th) {
        File crashFile = null;
        StringBuilder sb = new StringBuilder();

        // Збір інформації про Build
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                sb.append(field.getName())
                        .append(":")
                        .append(field.get(null))
                        .append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Збір stack trace виключення
        StringWriter sw = new StringWriter();
        th.printStackTrace(new PrintWriter(sw));
        sb.append(sw.toString());

        try {
            File baseDir;
            if ("mounted".equalsIgnoreCase(Environment.getExternalStorageState())) {
                baseDir = new File(BaseInfo.getCrashLogsPath(context) + CRASH_DIRECTORY);
            } else {
                baseDir = new File(context.getCacheDir(), CRASH_DIRECTORY);
            }
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }

            String fileName = "crash_" + System.currentTimeMillis() + "_" + formatter.format(new Date()) + ".log";
            crashFile = new File(baseDir, fileName);

            try (FileOutputStream fos = new FileOutputStream(crashFile)) {
                fos.write(sb.toString().getBytes());
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return crashFile;
    }


    public static void commitUncatchException(Context context, String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        commitBaseUncatchInfo(context, stringBuffer);
        stringBuffer.append("etype=2");
        stringBuffer.append("&log=" + PdrUtil.encodeURL(str2));
        stringBuffer.append("&eurl=" + PdrUtil.encodeURL(str));
        commitErrorLog(context, stringBuffer.toString());
    }

    private static void commitBaseUncatchInfo(Context context, StringBuffer stringBuffer) {
        String str;
        String imei = TelephonyUtil.getIMEI(context);
        int networkType = NetworkTypeUtil.getNetworkType(context);
        try {
            str = URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = null;
        }
        int i = Build.VERSION.SDK_INT;
        stringBuffer.append("s=99");
        stringBuffer.append(AbsoluteConst.STREAMAPP_KEY_IMEI + imei);
        stringBuffer.append("&md=" + str);
        stringBuffer.append("&os=" + i);
        stringBuffer.append("&appid=" + BaseInfo.sLastRunApp);
        stringBuffer.append(AbsoluteConst.STREAMAPP_KEY_NET + networkType);
        stringBuffer.append("&vb=" + BaseInfo.sBaseVersion);
        stringBuffer.append("&appcount=" + BaseInfo.s_Runing_App_Count);
        stringBuffer.append("&wvcount=" + BaseInfo.s_Webview_Count);
        stringBuffer.append("&pn=" + context.getPackageName());
        stringBuffer.append("&mem=" + getAppUseMem(context));
        stringBuffer.append("&vd=" + Build.MANUFACTURER);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void commitUncatchException(Context context, Throwable th) {
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        String stringWriter2 = stringWriter.toString();
        StringBuffer stringBuffer = new StringBuffer();
        commitBaseUncatchInfo(context, stringBuffer);
        stringBuffer.append("etype=1");
        stringBuffer.append("&log=" + PdrUtil.encodeURL(stringWriter2));
        commitErrorLog(context, stringBuffer.toString());
    }

    private static String getAppUseMem(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
                if (context.getApplicationInfo().uid == runningAppProcessInfo.uid) {
                    int i = runningAppProcessInfo.pid;
                    String str = runningAppProcessInfo.processName;
                    return activityManager.getProcessMemoryInfo(new int[]{i})[0].dalvikPrivateDirty + "kb";
                }
            }
            return "";
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private static void commitErrorLog(Context context, final String str) {
        if (BaseInfo.isStreamApp(context) || BaseInfo.isForQihooHelper(context)) {
            Class<?> cls = null;
            try {
                cls = Class.forName(DCloudAdapterUtil.getDcloudDownloadService());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (cls != null) {
                Intent intent = new Intent(context, cls);
                intent.putExtra("_____flag____", true);
                intent.putExtra("_____error____", true);
                intent.putExtra(NotificationCompat.CATEGORY_MESSAGE, str);
                context.startService(intent);
                return;
            }
            return;
        }
        ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.common.adapter.util.UEH.2
            @Override // java.lang.Runnable
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put("Content-Type", "application/x-www-form-urlencoded");
                NetTool.httpPost(StringConst.STREAMAPP_KEY_BASESERVICEURL() + "collect/crash", str, hashMap);
            }
        });
    }
}
