package io.dcloud.feature.pdr;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.dcloud.common.adapter.util.Logger;

/* compiled from: JsLogger.java */
/* loaded from: classes.dex  old b*/
public class JsLogger extends Logger {
    private static String a;
    private static File b;
    private static Boolean c = true;

    public static void a(String str) {
        if (c.booleanValue()) {
            a = str;
            a();
            c = false;
        }
    }

    public static void a(String str, String str2) {
        Log.d(str, str2);
        a(D, str, str2);
    }

    public static void b(String str, String str2) {
        Log.e(str, str2);
        a(E, str, str2);
    }

    public static void c(String str, String str2) {
        Log.i(str, str2);
        a(W, str, str2);
    }

    public static void f(String str, String str2) {
        Log.i(str, str2);
        a(I, str, str2);
    }

    public static void a() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(a).append(File.separatorChar).append(generateTimeStamp(false)).append(".log");
        File file = new File(a);
        b = new File(stringBuffer.toString());
        if (!file.exists()) {
            file.mkdirs();
        } else {
            deleteOldLog(file);
        }
        if (b.exists()) {
            return;
        }
        try {
            b.createNewFile();
        } catch (IOException e) {
            b = null;
            e.printStackTrace();
        }
    }

    private static void a(String str, String str2, String str3) {
        String generateLog = generateLog(str, str2, str3);
        if (b == null || generateLog == null) {
            return;
        }
        FileOutputStream fileOutputStream = null;
        try {
            try {
                try {
                    FileOutputStream fileOutputStream2 = new FileOutputStream(b, true);
                    try {
                        fileOutputStream2.write(generateLog.getBytes());
                        fileOutputStream2.flush();
                        fileOutputStream2.close();
                    } catch (Exception e) {
                        e = e;
                        fileOutputStream = fileOutputStream2;
                        e.printStackTrace();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            } catch (Exception e3) {
                throw  e3;
            }
        } catch (Throwable e4) {
            e4.printStackTrace();
        }
    }
}
