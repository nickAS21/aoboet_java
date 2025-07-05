package com.espressif.iot.esptouch.util;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.dcloud.common.DHInterface.IApp;

public class EspNetUtil {

    public static InetAddress getLocalInetAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) return null;

            int ipInt = wifiManager.getConnectionInfo().getIpAddress();
            String ipString = formatIpAddress(ipInt);

            return InetAddress.getByName(ipString);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String formatIpAddress(int ip) {
        // Форматуємо IP-адресу у вигляді x.x.x.x
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 24) & 0xFF);
    }

    public static InetAddress parseInetAddr(byte[] bArr, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(bArr[offset + i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
            if (i != length - 1) {
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

    public static byte[] parseBssid2bytes(String bssid) {
        String[] parts = bssid.split(":");
        byte[] bytes = new byte[parts.length];
        for (int i = 0; i < parts.length; i++) {
            bytes[i] = (byte) Integer.parseInt(parts[i], 16);
        }
        return bytes;
    }
}
