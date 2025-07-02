package io.dcloud.common.adapter.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.ThreadPool;

/* loaded from: classes.dex */
public class Logger {
    public static final String ANIMATION_TAG = "Animation_Path";
    public static final String AUTO_POP_PUSH_TAG = "Auto_Pop_Push_Path";
    public static final String Android_System_TAG = "Android_System_Path";
    public static final String AppMgr_TAG = "appmgr";
    public static final String AutoGC_TAG = "AutoGC_Path";
    public static final String Capture_TAG = "Capture_Tag";
    protected static String D = "D";
    protected static String E = "E";
    private static final String EXCEPTION_TAG = "DCloud_Exception";
    public static final String Event_TAG = "Event_Tag";
    protected static String I = "I";
    public static final String LAYOUT_TAG = "Layout_Path";
    private static final String LOGTAG = "DCloud_LOG";
    private static String LogPath = "";
    public static final String MAIN_TAG = "Main_Path";
    public static final String MAP_TAG = "Map_Path";
    private static int MAX_CRASH_FILE_COUNT = 3;
    private static final String MSC_TAG = "DCloud_";
    public static final String StreamApp_TAG = "stream_manager";
    private static long TIMES = 432000000;
    public static final String TIMESTAMP_HH_MM_SS_SSS = "HH:mm:ss.SSS";
    public static final String TIMESTAMP_YYYY_MM_DD = "yyyyMMdd";
    public static final String TIMESTAMP_YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMdd HH:mm:ss.SSS";
    public static final String VIEW_VISIBLE_TAG = "View_Visible_Path";
    protected static String W = "W";
    private static boolean isOpen = true;
    private static boolean isStoreLog = false;
    private static File mLogFile = null;
    static String pkg = "";

    public static void initLogger(Context context) {
        pkg = context.getPackageName();
        if (DeviceInfo.isSwitchDirectory) {
            pkg = DeviceInfo.SWITCH_DIRECTORY;
        }
        boolean z = BaseInfo.isBase(context) || DHFile.hasFile();
        isOpen = z;
        isOpen = z | BaseInfo.ISDEBUG;
        if ("mounted".equalsIgnoreCase(Environment.getExternalStorageState())) {
            LogPath = BaseInfo.getCrashLogsPath(context);
        }
        init(LogPath, IApp.ConfigProperty.CONFIG_CRASH);
        init(LogPath + "crash/", null);
        setOpen(isOpen);
    }

    private static void init(final String str, final String str2) {
        try {
            ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.common.adapter.util.Logger.1
                @Override // java.lang.Runnable
                public void run() {
                    File[] listFiles = new File(str).listFiles();
                    if (listFiles == null || listFiles.length <= Logger.MAX_CRASH_FILE_COUNT) {
                        return;
                    }
                    if (listFiles[0].getName().compareTo(listFiles[1].getName()) < 0) {
                        for (int i = 0; i < listFiles.length - Logger.MAX_CRASH_FILE_COUNT; i++) {
                            if (!TextUtils.equals(str2, listFiles[i].getName())) {
                                listFiles[i].delete();
                            }
                        }
                        return;
                    }
                    for (int length = listFiles.length - 1; length >= Logger.MAX_CRASH_FILE_COUNT; length--) {
                        if (!TextUtils.equals(str2, listFiles[length].getName())) {
                            listFiles[length].delete();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setOpen(boolean z) {
        isOpen = z;
        if (!z || new File(LogPath).exists()) {
            return;
        }
        canStoreLogToSDcard();
        storeLogToSDcard();
    }

    public static boolean isOpen() {
        return isOpen;
    }

    public static void d(String str, String str2) {
        if (isOpen) {
            if (!BaseInfo.ISDEBUG) {
                Log.i(str, str2);
            } else {
                Log.d(str, str2);
            }
            WriteLogToSDcard(D, str, str2);
        }
    }

    public static void d(String str, Object... objArr) {
        if (isOpen) {
            StringBuffer stringBuffer = new StringBuffer();
            if (objArr != null) {
                for (Object obj : objArr) {
                    stringBuffer.append(obj).append(";");
                }
            }
            if (!BaseInfo.ISDEBUG) {
                Log.i(str, stringBuffer.toString());
            } else {
                Log.d(str, stringBuffer.toString());
            }
            WriteLogToSDcard(D, str, stringBuffer.toString());
        }
    }

    public static void d(String str) {
        d(LOGTAG, str);
    }

    public static void i(String str, String str2) {
        if (!isOpen || str2 == null) {
            return;
        }
        Log.i(str, str2);
        WriteLogToSDcard(I, str, str2);
    }

    public static void i(String str) {
        i(LOGTAG, str);
    }

    public static void w(String str, Throwable th) {
        if (isOpen) {
            if (th != null) {
                th.printStackTrace();
            }
            Log.w(EXCEPTION_TAG, str, th);
            WriteExceptionToSDcard(W, EXCEPTION_TAG, str, th);
        }
    }

    public static void w(Throwable th) {
        w("", th);
    }

    public static void e(String str, String str2) {
        if (isOpen) {
            Log.e(str, str2);
            WriteLogToSDcard(E, str, str2);
        }
    }

    public static void e(String str) {
        e(LOGTAG, str);
    }

    public static boolean isTurnOn() {
        return isStoreLog;
    }

    public static void turnOff() {
        isStoreLog = false;
    }

    protected static boolean isSDcardExists() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    private static String concatString(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length() + str2.length());
        stringBuffer.append(str).append(str2);
        return stringBuffer.toString();
    }

    public static boolean canStoreLogToSDcard() {
        if (isSDcardExists() && isOpen) {
            File file = new File(LogPath);
            if (!file.exists()) {
                file.mkdirs();
            } else {
                deleteOldLog(file);
            }
            if (file.exists() && file.canWrite()) {
                isStoreLog = true;
            }
        }
        return isStoreLog;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String generateLog(String str, String str2, String str3) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(generateTimeStamp(true)).append(" ").append(str).append(" ").append("-").append(" ").append(str2).append(" ").append(str3).append("\n");
        return stringBuffer.toString();
    }

    private static void WriteExceptionToSDcard(String str, String str2, String str3, Throwable th) {
        if (th != null) {
            WriteLogToSDcard(str, str2, generateLog(str, EXCEPTION_TAG, th.getClass().getName()));
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    generateLog(str, EXCEPTION_TAG, "    at " + stackTraceElement.toString());
                    WriteLogToSDcard(str, str2, str3);
                }
            }
        }
    }

    private static void WriteLogToSDcard(String tag, String level, String message) {
        if (!isOpen) return;

        String generateLog = generateLog(tag, level, message);
        if (mLogFile == null || generateLog == null) return;

        try (FileOutputStream fos = new FileOutputStream(mLogFile, true)) {
            fos.write(generateLog.getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String generateTimeStamp(Boolean bool) {
        return generateTimeStamp(bool.booleanValue() ? TIMESTAMP_HH_MM_SS_SSS : TIMESTAMP_YYYY_MM_DD, new Date());
    }

    public static String generateTimeStamp(String str, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, Locale.ENGLISH);
        simpleDateFormat.applyPattern(str);
        return simpleDateFormat.format(date);
    }

    public static void storeLogToSDcard() {
        if (isStoreLog) {
            StringBuffer stringBuffer = new StringBuffer();
            String str = generateTimeStamp(false) + ".log";
            String str2 = "crash_" + System.currentTimeMillis() + "_" + str + ".log";
            stringBuffer.append(LogPath).append(File.separatorChar).append(str);
            File file = new File(stringBuffer.toString());
            mLogFile = file;
            if (!file.exists()) {
                try {
                    mLogFile.createNewFile();
                } catch (IOException e) {
                    mLogFile = null;
                    e.printStackTrace();
                }
            }
            WriteLogToSDcard("日志路径:", "Logger", "path=" + stringBuffer.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void deleteOldLog(File file) {
        File[] listFiles = file.listFiles();
        Date date = new Date();
        for (File file2 : listFiles) {
            if (!file2.isDirectory()) {
                try {
                    if (date.getTime() - new SimpleDateFormat(TIMESTAMP_YYYY_MM_DD).parse(file2.getName().substring(0, 8)).getTime() > TIMES) {
                        file2.delete();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
