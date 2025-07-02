package com.demo.smarthome.service;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class TcpConnectService {
    private static final String TAG = "TcpConnectService";

    /* JADX WARN: Not initialized variable reg: 3, insn: 0x019c: MOVE (r2 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:39:0x019c */
    /* JADX WARN: Removed duplicated region for block: B:14:0x014d A[Catch: Exception -> 0x0160, IOException -> 0x016a, MalformedURLException -> 0x0174, TryCatch #9 {MalformedURLException -> 0x0174, IOException -> 0x016a, Exception -> 0x0160, blocks: (B:12:0x0060, B:14:0x014d, B:17:0x0156), top: B:11:0x0060 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0195  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String registUser(java.lang.String r10, java.lang.String r11, java.lang.String r12, java.lang.String r13, java.lang.String r14, java.lang.String r15) {
        /*
            Method dump skipped, instructions count: 417
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.demo.smarthome.service.TcpConnectService.registUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    private static String getFindResultByString(String str, String str2, String str3) {
        int length;
        int indexOf = str.indexOf(str2);
        int indexOf2 = str.indexOf(str3);
        outMsg(TAG, "=============str:" + str);
        outMsg(TAG, "=============indexStart:" + indexOf + "  indexEnd:" + indexOf2);
        if (indexOf != 0 && indexOf < str.length() && indexOf2 != 0 && indexOf2 > (length = indexOf + str2.length())) {
            String substring = str.substring(length, indexOf2);
            outMsg(TAG, "=============str=" + substring);
            if (substring.length() > 0) {
                return substring;
            }
        }
        return "";
    }

    private static String getResultByStream(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[1024];
        StringBuilder sb = new StringBuilder();
        outMsg(TAG, "=============StringBuffer");
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outMsg(TAG, "=============str=br.readLine()");
                sb.append(new String(bArr, 0, read, "utf-8"));
            } else {
                return sb.toString();
            }
        }
    }

    private static void outMsg(String str, String str2) {
        Log.i(str, str2);
    }
}
