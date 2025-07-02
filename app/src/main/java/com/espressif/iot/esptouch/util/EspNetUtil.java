package com.espressif.iot.esptouch.util;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.dcloud.common.DHInterface.IApp;

/* loaded from: classes.dex */
public class EspNetUtil {
    public static InetAddress getLocalInetAddress(Context context) {
        try {
            return InetAddress.getByName(__formatString(((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String __formatString(int i) {
        byte[] __intToByteArray = __intToByteArray(i);
        String str = "";
        for (int length = __intToByteArray.length - 1; length >= 0; length--) {
            str = str + (__intToByteArray[length] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
            if (length > 0) {
                str = str + ".";
            }
        }
        return str;
    }

    private static byte[] __intToByteArray(int i) {
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < 4; i2++) {
            bArr[i2] = (byte) ((i >>> ((3 - i2) * 8)) & 255);
        }
        return bArr;
    }

    public static InetAddress parseInetAddr(byte[] bArr, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < i2; i3++) {
            sb.append(Integer.toString(bArr[i + i3] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE));
            if (i3 != i2 - 1) {
                sb.append('.');
            }
        }
        try {
            return InetAddress.getByName(sb.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] parseBssid2bytes(String str) {
        String[] split = str.split(":");
        byte[] bArr = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            bArr[i] = (byte) Integer.parseInt(split[i], 16);
        }
        return bArr;
    }
}
