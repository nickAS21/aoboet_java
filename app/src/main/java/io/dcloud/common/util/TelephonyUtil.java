package io.dcloud.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class TelephonyUtil {
    private static final String K = "DCloud&2*0^1#600";
    private static String sIMSI = null;
    private static String sImei = "";
    private static String sImeiAndBakInfo;

    private static boolean isUnValid(String str) {
        return TextUtils.isEmpty(str) || str.contains("Unknown") || str.contains("00000000");
    }

    public static String getIMEI(Context context) {
        return getIMEI(context, true);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(16:1|2|3|(1:5)|(2:6|7)|(7:44|45|(2:47|(2:49|(1:51)))|10|(4:22|23|(1:27)|(2:29|(4:31|32|(2:34|(2:36|(1:38)))|40)))|14|(1:21)(2:18|19))|9|10|(1:12)|22|23|(2:25|27)|(0)|14|(1:16)|21) */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00a4 A[Catch: Exception -> 0x00c2, TRY_LEAVE, TryCatch #2 {Exception -> 0x00c2, blocks: (B:23:0x0061, B:25:0x009b, B:29:0x00a4), top: B:22:0x0061 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static String[] getMultiIMEI(Context r11) {
        /*
            java.lang.String r0 = "getImei"
            java.lang.String r1 = "phone"
            java.lang.Object r11 = r11.getSystemService(r1)
            android.telephony.TelephonyManager r11 = (android.telephony.TelephonyManager) r11
            r1 = 0
            java.lang.Class r2 = r11.getClass()     // Catch: java.lang.Exception -> L51
            java.lang.String r2 = r2.getName()     // Catch: java.lang.Exception -> L51
            java.lang.Object r2 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r2, r0, r11)     // Catch: java.lang.Exception -> L51
            java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Exception -> L51
            java.lang.Class r3 = r11.getClass()     // Catch: java.lang.Exception -> L51
            java.lang.String r3 = r3.getName()     // Catch: java.lang.Exception -> L51
            java.lang.String r4 = "getImei2"
            java.lang.Object r3 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r3, r4, r11)     // Catch: java.lang.Exception -> L51
            java.lang.String r3 = (java.lang.String) r3     // Catch: java.lang.Exception -> L51
            boolean r4 = isUnValid(r2)     // Catch: java.lang.Exception -> L51
            if (r4 != 0) goto L30
            goto L31
        L30:
            r2 = r1
        L31:
            boolean r4 = isUnValid(r3)     // Catch: java.lang.Exception -> L4f
            if (r4 != 0) goto L4f
            boolean r4 = isUnValid(r2)     // Catch: java.lang.Exception -> L53
            if (r4 == 0) goto L53
            java.lang.String r4 = r11.getDeviceId()     // Catch: java.lang.Exception -> L53
            boolean r5 = isUnValid(r4)     // Catch: java.lang.Exception -> L53
            if (r5 != 0) goto L53
            boolean r5 = android.text.TextUtils.equals(r4, r3)     // Catch: java.lang.Exception -> L53
            if (r5 != 0) goto L53
            r2 = r4
            goto L53
        L4f:
            r3 = r1
            goto L53
        L51:
            r2 = r1
            r3 = r2
        L53:
            boolean r4 = isUnValid(r2)
            r5 = 0
            r6 = 1
            if (r4 != 0) goto L61
            boolean r4 = isUnValid(r3)
            if (r4 == 0) goto Lc2
        L61:
            java.lang.Class r4 = r11.getClass()     // Catch: java.lang.Exception -> Lc2
            java.lang.String r4 = r4.getName()     // Catch: java.lang.Exception -> Lc2
            java.lang.Class[] r7 = new java.lang.Class[r6]     // Catch: java.lang.Exception -> Lc2
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch: java.lang.Exception -> Lc2
            r7[r5] = r8     // Catch: java.lang.Exception -> Lc2
            java.lang.Object[] r8 = new java.lang.Object[r6]     // Catch: java.lang.Exception -> Lc2
            java.lang.Integer r9 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.Exception -> Lc2
            r8[r5] = r9     // Catch: java.lang.Exception -> Lc2
            java.lang.Object r4 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r4, r0, r11, r7, r8)     // Catch: java.lang.Exception -> Lc2
            java.lang.String r4 = (java.lang.String) r4     // Catch: java.lang.Exception -> Lc2
            java.lang.Class r7 = r11.getClass()     // Catch: java.lang.Exception -> Lc2
            java.lang.String r7 = r7.getName()     // Catch: java.lang.Exception -> Lc2
            java.lang.Class[] r8 = new java.lang.Class[r6]     // Catch: java.lang.Exception -> Lc2
            java.lang.Class r9 = java.lang.Integer.TYPE     // Catch: java.lang.Exception -> Lc2
            r8[r5] = r9     // Catch: java.lang.Exception -> Lc2
            java.lang.Object[] r9 = new java.lang.Object[r6]     // Catch: java.lang.Exception -> Lc2
            java.lang.Integer r10 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.Exception -> Lc2
            r9[r5] = r10     // Catch: java.lang.Exception -> Lc2
            java.lang.Object r0 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r7, r0, r11, r8, r9)     // Catch: java.lang.Exception -> Lc2
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Exception -> Lc2
            if (r4 == 0) goto La2
            boolean r7 = android.text.TextUtils.equals(r2, r4)     // Catch: java.lang.Exception -> Lc2
            if (r7 != 0) goto La2
            r2 = r4
        La2:
            if (r0 == 0) goto Lc2
            boolean r4 = android.text.TextUtils.equals(r3, r0)     // Catch: java.lang.Exception -> Lc2
            if (r4 != 0) goto Lc2
            boolean r3 = isUnValid(r2)     // Catch: java.lang.Exception -> Lc1
            if (r3 == 0) goto Lc1
            java.lang.String r11 = r11.getDeviceId()     // Catch: java.lang.Exception -> Lc1
            boolean r3 = isUnValid(r11)     // Catch: java.lang.Exception -> Lc1
            if (r3 != 0) goto Lc1
            boolean r3 = android.text.TextUtils.equals(r11, r0)     // Catch: java.lang.Exception -> Lc1
            if (r3 != 0) goto Lc1
            r2 = r11
        Lc1:
            r3 = r0
        Lc2:
            boolean r11 = isUnValid(r2)
            if (r11 != 0) goto Ld6
            boolean r11 = isUnValid(r3)
            if (r11 != 0) goto Ld6
            r11 = 2
            java.lang.String[] r11 = new java.lang.String[r11]
            r11[r5] = r2
            r11[r6] = r3
            return r11
        Ld6:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TelephonyUtil.getMultiIMEI(android.content.Context):java.lang.String[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x028e, code lost:
    
        r7.append(r9).append("|");
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0295, code lost:
    
        r0 = android.util.Base64.encodeToString(io.dcloud.common.util.AESUtil.encrypt(io.dcloud.common.util.TelephonyUtil.K, 128, "HTML5PLUSRUNTIME", r7.toString()), 2);
        io.dcloud.common.util.TelephonyUtil.sImeiAndBakInfo = r0;
        io.dcloud.common.util.TelephonyUtil.sImeiAndBakInfo = java.net.URLEncoder.encode(r0);
        r0 = io.dcloud.common.util.TelephonyUtil.sImeiAndBakInfo + "&ie=1";
        io.dcloud.common.util.TelephonyUtil.sImeiAndBakInfo = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x02be, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x01dc, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x01dd, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x01d7, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x01d8, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x020a, code lost:
    
        if (r0.getParentFile().exists() == false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x020c, code lost:
    
        r0.getParentFile().mkdirs();
        r0.createNewFile();
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x021a, code lost:
    
        if (r14.isDirectory() != false) goto L138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x021c, code lost:
    
        r14.delete();
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0223, code lost:
    
        if (r14.exists() != false) goto L141;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0236, code lost:
    
        r12 = io.dcloud.common.util.IOUtil.toString(new java.io.FileInputStream(r14));
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x026b, code lost:
    
        io.dcloud.common.adapter.io.DHFile.copyFile(r2, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x0238, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0239, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x023d, code lost:
    
        r12 = java.util.UUID.randomUUID().toString().replaceAll("-", "").replace("\n", "");
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x024f, code lost:
    
        r0 = new java.io.FileOutputStream(r14);
        r0.write(r12.getBytes());
        r0.flush();
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0267, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0268, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x0262, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0263, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x01b2, code lost:
    
        r2 = null;
        r14 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x027d, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x027e, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0281, code lost:
    
        if (r21 != false) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x0287, code lost:
    
        if (android.text.TextUtils.isEmpty(r12) != false) goto L171;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x027b, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x02bf, code lost:
    
        if (r21 != false) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x02c5, code lost:
    
        if (android.text.TextUtils.isEmpty(r12) == false) goto L178;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x02c8, code lost:
    
        r9 = r12.replace("\n", "");
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x02cc, code lost:
    
        r7.append(r9).append("|");
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x02d3, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x013d, code lost:
    
        io.dcloud.common.util.TelephonyUtil.sImei = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x0140, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x0150, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0153, code lost:
    
        if (r21 != false) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0159, code lost:
    
        if (android.text.TextUtils.isEmpty(r13) != false) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x02d4, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x02d5, code lost:
    
        r12 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x02d6, code lost:
    
        if (r21 != false) goto L184;
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x02dc, code lost:
    
        if (android.text.TextUtils.isEmpty(r12) == false) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x02df, code lost:
    
        r9 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x02e0, code lost:
    
        r7.append(r9).append("|");
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x02e7, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x014e, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x014f, code lost:
    
        r13 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x014b, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x00fd, code lost:
    
        io.dcloud.common.util.TelephonyUtil.sImei = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0100, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0110, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x0113, code lost:
    
        if (r21 != false) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x0119, code lost:
    
        if (android.text.TextUtils.isEmpty(r13) != false) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x00fa, code lost:
    
        r13 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x02e8, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02e9, code lost:
    
        r12 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x02ea, code lost:
    
        if (r21 != false) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x02f0, code lost:
    
        if (android.text.TextUtils.isEmpty(r12) == false) goto L196;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x02f3, code lost:
    
        r9 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x02f4, code lost:
    
        r7.append(r9).append("|");
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x02fb, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x010e, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x010f, code lost:
    
        r13 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x010b, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x00c5, code lost:
    
        r7.append(io.dcloud.common.util.TelephonyUtil.sImei).append("|");
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x00c3, code lost:
    
        if (r21 == false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00b9, code lost:
    
        if (r21 != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ce, code lost:
    
        r0 = isUnValid(io.dcloud.common.util.TelephonyUtil.sImei);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00d4, code lost:
    
        if (r0 != false) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00d6, code lost:
    
        if (r21 == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00db, code lost:
    
        return io.dcloud.common.util.TelephonyUtil.sImei;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00dc, code lost:
    
        r12 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00dd, code lost:
    
        r13 = (android.net.wifi.WifiManager) r20.getSystemService("wifi");
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00e9, code lost:
    
        if (r13.isWifiEnabled() == false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00eb, code lost:
    
        r13 = r13.getConnectionInfo().getMacAddress().replace(":", "");
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00fb, code lost:
    
        if (r0 == false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0102, code lost:
    
        if (r21 == false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0108, code lost:
    
        if (android.text.TextUtils.isEmpty(r13) == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x011c, code lost:
    
        r7.append(r13).append("|");
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x011b, code lost:
    
        r13 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0123, code lost:
    
        r0 = isUnValid(io.dcloud.common.util.TelephonyUtil.sImei);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0129, code lost:
    
        if (r0 != false) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0130, code lost:
    
        return io.dcloud.common.util.TelephonyUtil.sImei;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0131, code lost:
    
        r13 = android.provider.Settings.Secure.getString(r20.getContentResolver(), "android_id");
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x013b, code lost:
    
        if (r0 != false) goto L205;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0142, code lost:
    
        if (r21 != false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0148, code lost:
    
        if (android.text.TextUtils.isEmpty(r13) != false) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x015c, code lost:
    
        r7.append(r13).append("|");
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x015b, code lost:
    
        r13 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0163, code lost:
    
        r13 = isUnValid(io.dcloud.common.util.TelephonyUtil.sImei);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0169, code lost:
    
        if (r13 == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0170, code lost:
    
        return io.dcloud.common.util.TelephonyUtil.sImei;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0171, code lost:
    
        r1 = r20.getFilesDir() + "/.imei.txt";
        r0 = new java.io.File(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0195, code lost:
    
        if ("mounted".equalsIgnoreCase(android.os.Environment.getExternalStorageState()) == false) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0197, code lost:
    
        r2 = android.os.Environment.getExternalStorageDirectory() + "/.imei.txt";
        r14 = new java.io.File(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01b8, code lost:
    
        if (r0.isDirectory() != false) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x01ba, code lost:
    
        r0.delete();
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01c3, code lost:
    
        if (r0.exists() != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x01cd, code lost:
    
        r12 = io.dcloud.common.util.IOUtil.toString(new java.io.FileInputStream(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x01e0, code lost:
    
        if (r14 != null) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x01ea, code lost:
    
        if (r14.getParentFile().exists() == false) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01ec, code lost:
    
        r14.getParentFile().mkdirs();
        r14.createNewFile();
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01fc, code lost:
    
        if (r14.length() <= 0) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01fe, code lost:
    
        io.dcloud.common.adapter.io.DHFile.copyFile(r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x026e, code lost:
    
        if (r13 != false) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0270, code lost:
    
        io.dcloud.common.util.TelephonyUtil.sImei = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0272, code lost:
    
        if (r21 != false) goto L159;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0278, code lost:
    
        if (android.text.TextUtils.isEmpty(r12) != false) goto L171;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x028a, code lost:
    
        r9 = r12.replace("\n", "");
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:110:0x020c A[Catch: all -> 0x027b, Exception -> 0x027d, TryCatch #11 {Exception -> 0x027d, blocks: (B:73:0x0171, B:75:0x0197, B:76:0x01b4, B:78:0x01ba, B:79:0x01bd, B:81:0x01c5, B:85:0x01cd, B:87:0x01e2, B:89:0x01ec, B:90:0x01f6, B:92:0x01fe, B:94:0x0270, B:107:0x01d8, B:105:0x01dd, B:108:0x0202, B:110:0x020c, B:111:0x0216, B:113:0x021c, B:114:0x021f, B:116:0x0225, B:120:0x022d, B:122:0x026b, B:125:0x0239, B:126:0x023d, B:128:0x024f, B:133:0x0263, B:131:0x0268), top: B:72:0x0171, outer: #15 }] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x021c A[Catch: all -> 0x027b, Exception -> 0x027d, TryCatch #11 {Exception -> 0x027d, blocks: (B:73:0x0171, B:75:0x0197, B:76:0x01b4, B:78:0x01ba, B:79:0x01bd, B:81:0x01c5, B:85:0x01cd, B:87:0x01e2, B:89:0x01ec, B:90:0x01f6, B:92:0x01fe, B:94:0x0270, B:107:0x01d8, B:105:0x01dd, B:108:0x0202, B:110:0x020c, B:111:0x0216, B:113:0x021c, B:114:0x021f, B:116:0x0225, B:120:0x022d, B:122:0x026b, B:125:0x0239, B:126:0x023d, B:128:0x024f, B:133:0x0263, B:131:0x0268), top: B:72:0x0171, outer: #15 }] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x01b2  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x02d8  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x02ec  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0197 A[Catch: all -> 0x027b, Exception -> 0x027d, TryCatch #11 {Exception -> 0x027d, blocks: (B:73:0x0171, B:75:0x0197, B:76:0x01b4, B:78:0x01ba, B:79:0x01bd, B:81:0x01c5, B:85:0x01cd, B:87:0x01e2, B:89:0x01ec, B:90:0x01f6, B:92:0x01fe, B:94:0x0270, B:107:0x01d8, B:105:0x01dd, B:108:0x0202, B:110:0x020c, B:111:0x0216, B:113:0x021c, B:114:0x021f, B:116:0x0225, B:120:0x022d, B:122:0x026b, B:125:0x0239, B:126:0x023d, B:128:0x024f, B:133:0x0263, B:131:0x0268), top: B:72:0x0171, outer: #15 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01ba A[Catch: all -> 0x027b, Exception -> 0x027d, TryCatch #11 {Exception -> 0x027d, blocks: (B:73:0x0171, B:75:0x0197, B:76:0x01b4, B:78:0x01ba, B:79:0x01bd, B:81:0x01c5, B:85:0x01cd, B:87:0x01e2, B:89:0x01ec, B:90:0x01f6, B:92:0x01fe, B:94:0x0270, B:107:0x01d8, B:105:0x01dd, B:108:0x0202, B:110:0x020c, B:111:0x0216, B:113:0x021c, B:114:0x021f, B:116:0x0225, B:120:0x022d, B:122:0x026b, B:125:0x0239, B:126:0x023d, B:128:0x024f, B:133:0x0263, B:131:0x0268), top: B:72:0x0171, outer: #15 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0270 A[Catch: all -> 0x027b, Exception -> 0x027d, TRY_LEAVE, TryCatch #11 {Exception -> 0x027d, blocks: (B:73:0x0171, B:75:0x0197, B:76:0x01b4, B:78:0x01ba, B:79:0x01bd, B:81:0x01c5, B:85:0x01cd, B:87:0x01e2, B:89:0x01ec, B:90:0x01f6, B:92:0x01fe, B:94:0x0270, B:107:0x01d8, B:105:0x01dd, B:108:0x0202, B:110:0x020c, B:111:0x0216, B:113:0x021c, B:114:0x021f, B:116:0x0225, B:120:0x022d, B:122:0x026b, B:125:0x0239, B:126:0x023d, B:128:0x024f, B:133:0x0263, B:131:0x0268), top: B:72:0x0171, outer: #15 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0274  */
    /* JADX WARN: Type inference failed for: r13v24 */
    /* JADX WARN: Type inference failed for: r13v28 */
    /* JADX WARN: Type inference failed for: r13v29 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static String getIMEI(Context r20, boolean r21) {
        /*
            Method dump skipped, instructions count: 776
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TelephonyUtil.getIMEI(android.content.Context, boolean):java.lang.String");
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMSI(Context context) {
        if (context == null) {
            return "";
        }
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                String imsi = tm.getSubscriberId();
                if (imsi != null) {
                    return imsi;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
