package com.espressif.iot.esptouch.demo_activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import io.dcloud.common.util.JSUtil;

public class EspWifiAdminSimple {
    private final Context mContext;

    public EspWifiAdminSimple(Context context) {
        this.mContext = context;
    }

    /** Повертає SSID підключеної Wi-Fi мережі або null, якщо немає підключення */
    public String getWifiConnectedSsid() {
        WifiInfo connectionInfo = getConnectionInfo();
        if (connectionInfo == null || !isWifiConnected()) {
            return null;
        }
        String ssid = connectionInfo.getSSID();
        if (ssid == null) return null;

        // Прибираємо лапки, якщо вони є
        if (ssid.startsWith(JSUtil.QUOTE) && ssid.endsWith(JSUtil.QUOTE)) {
            return ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    /** Повертає BSSID підключеної Wi-Fi мережі або null, якщо немає підключення */
    public String getWifiConnectedBssid() {
        WifiInfo connectionInfo = getConnectionInfo();
        if (connectionInfo == null || !isWifiConnected()) {
            return null;
        }
        return connectionInfo.getBSSID();
    }

    /** Отримати інформацію про Wi-Fi з'єднання */
    private WifiInfo getConnectionInfo() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null ? wifiManager.getConnectionInfo() : null;
    }

    /** Перевірити, чи Wi-Fi підключено */
    private boolean isWifiConnected() {
        NetworkInfo wifiNetworkInfo = getWifiNetworkInfo();
        return wifiNetworkInfo != null && wifiNetworkInfo.isConnected();
    }

    /** Отримати інформацію про Wi-Fi мережу */
    private NetworkInfo getWifiNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return null;
        return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }
}
