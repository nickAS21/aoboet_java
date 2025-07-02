package com.demo.smarthome.tools;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import io.dcloud.common.DHInterface.IApp;

/* loaded from: classes.dex */
public class IpTools {
    public static String getIpV4StringByByte(byte[] bArr, int i) {
        if (bArr == null || bArr.length < 4) {
            return "";
        }
        String str = "";
        for (int i2 = 0; i2 < 4; i2++) {
            str = str + (bArr[i2 + i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) + "";
            if (i2 < 3) {
                str = str + ".";
            }
        }
        return str;
    }

    public static byte[] getIpV4Byte(String str) {
        byte[] bArr = new byte[4];
        if (str == null) {
            return bArr;
        }
        int indexOf = str.indexOf(".");
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(".", i);
        int i2 = indexOf2 + 1;
        int indexOf3 = str.indexOf(".", i2);
        long[] jArr = {Long.parseLong(str.substring(0, indexOf)), Long.parseLong(str.substring(i, indexOf2)), Long.parseLong(str.substring(i2, indexOf3)), Long.parseLong(str.substring(indexOf3 + 1))};
        for (int i3 = 0; i3 < 4; i3++) {
            bArr[i3] = (byte) (jArr[i3] % 256);
        }
        return bArr;
    }

    public static String getIp(WifiManager wifiManager) {
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        connectionInfo.getMacAddress();
        connectionInfo.getSSID();
        int ipAddress = connectionInfo.getIpAddress();
        return ipAddress != 0 ? (ipAddress & 255) + "." + ((ipAddress >> 8) & 255) + "." + ((ipAddress >> 16) & 255) + "." + ((ipAddress >> 24) & 255) : "";
    }
}
