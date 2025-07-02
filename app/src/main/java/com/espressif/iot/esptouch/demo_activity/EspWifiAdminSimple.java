package com.espressif.iot.esptouch.demo_activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import io.dcloud.common.util.JSUtil;

/* loaded from: classes.dex */
public class EspWifiAdminSimple {
    private final Context mContext;

    public EspWifiAdminSimple(Context context) {
        this.mContext = context;
    }

    public String getWifiConnectedSsid() {
        WifiInfo connectionInfo = getConnectionInfo();
        if (connectionInfo == null || !isWifiConnected()) {
            return null;
        }
        int length = connectionInfo.getSSID().length();
        if (connectionInfo.getSSID().startsWith(JSUtil.QUOTE) && connectionInfo.getSSID().endsWith(JSUtil.QUOTE)) {
            return connectionInfo.getSSID().substring(1, length - 1);
        }
        return connectionInfo.getSSID();
    }

    public String getWifiConnectedBssid() {
        WifiInfo connectionInfo = getConnectionInfo();
        if (connectionInfo == null || !isWifiConnected()) {
            return null;
        }
        return connectionInfo.getBSSID();
    }

    private WifiInfo getConnectionInfo() {
        return ((WifiManager) this.mContext.getSystemService("wifi")).getConnectionInfo();
    }

    private boolean isWifiConnected() {
        NetworkInfo wifiNetworkInfo = getWifiNetworkInfo();
        if (wifiNetworkInfo != null) {
            return wifiNetworkInfo.isConnected();
        }
        return false;
    }

    private NetworkInfo getWifiNetworkInfo() {
        return ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getNetworkInfo(1);
    }
}
