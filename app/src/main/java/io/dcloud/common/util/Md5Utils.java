package io.dcloud.common.util;

import android.text.TextUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class Md5Utils {
    public static final String ALGORITHM = "MD5";
    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_LOWER_CASE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5LowerCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(str.getBytes("UTF-8"));
            return toLowerCaseHex(messageDigest.digest());
        } catch (Exception unused) {
            return str;
        }
    }

    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(str.getBytes("UTF-8"));
            return toHex(messageDigest.digest());
        } catch (Exception unused) {
            return str;
        }
    }

    public static String md5(byte[] bArr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(bArr);
            return toHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0041: MOVE (r1 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:30:0x0041 */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0044 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String md5(java.io.File r5) {
        /*
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]
            r1 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L30
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L30
            java.lang.String r5 = "MD5"
            java.security.MessageDigest r5 = java.security.MessageDigest.getInstance(r5)     // Catch: java.lang.Exception -> L2c java.lang.Throwable -> L40
        L10:
            int r3 = r2.read(r0)     // Catch: java.lang.Exception -> L2c java.lang.Throwable -> L40
            if (r3 <= 0) goto L1b
            r4 = 0
            r5.update(r0, r4, r3)     // Catch: java.lang.Exception -> L2c java.lang.Throwable -> L40
            goto L10
        L1b:
            byte[] r5 = r5.digest()     // Catch: java.lang.Exception -> L2c java.lang.Throwable -> L40
            java.lang.String r5 = toHex(r5)     // Catch: java.lang.Exception -> L2c java.lang.Throwable -> L40
            r2.close()     // Catch: java.io.IOException -> L27
            goto L2b
        L27:
            r0 = move-exception
            r0.printStackTrace()
        L2b:
            return r5
        L2c:
            r5 = move-exception
            goto L32
        L2e:
            r5 = move-exception
            goto L42
        L30:
            r5 = move-exception
            r2 = r1
        L32:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L40
            if (r2 == 0) goto L3f
            r2.close()     // Catch: java.io.IOException -> L3b
            goto L3f
        L3b:
            r5 = move-exception
            r5.printStackTrace()
        L3f:
            return r1
        L40:
            r5 = move-exception
            r1 = r2
        L42:
            if (r1 == 0) goto L4c
            r1.close()     // Catch: java.io.IOException -> L48
            goto L4c
        L48:
            r0 = move-exception
            r0.printStackTrace()
        L4c:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.Md5Utils.md5(java.io.File):java.lang.String");
    }

    private static String toHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            char[] cArr = HEX;
            sb.append(cArr[(b & 240) >> 4]);
            sb.append(cArr[b & 15]);
        }
        return sb.toString();
    }

    private static String toLowerCaseHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            char[] cArr = HEX_LOWER_CASE;
            sb.append(cArr[(b & 240) >> 4]);
            sb.append(cArr[b & 15]);
        }
        return sb.toString();
    }

    public static boolean verifyFileMd5(String str, String str2) {
        String md5;
        return str.length() > 0 && (md5 = md5(new File(str))) != null && str2 != null && md5.compareToIgnoreCase(str2) == 0;
    }
}
