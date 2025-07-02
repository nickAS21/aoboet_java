package io.dcloud.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* loaded from: classes.dex */
public class NetworkTypeUtil {
    public static final int NETWORK_TYPE_2G = 4;
    public static final int NETWORK_TYPE_3G = 5;
    public static final int NETWORK_TYPE_4G = 6;
    public static final int NETWORK_TYPE_DISABLED = 1;
    public static final int NETWORK_TYPE_LINE = 2;
    public static final int NETWORK_TYPE_UNKONWN = 0;
    public static final int NETWORK_TYPE_WIFI = 3;

    public static int getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return 1;
        }
        if (activeNetworkInfo.getType() != 0) {
            return activeNetworkInfo.getType() == 1 ? 3 : 0;
        }
        switch (activeNetworkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
                return 4;
            case 3:
            case 5:
            case 6:
            case 8:
            case 12:
                return 5;
            case 7:
            case 14:
            case 16:
            default:
                return 0;
            case 9:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
                return 6;
        }
    }

    public static String getCurrentAPN(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return (activeNetworkInfo == null || activeNetworkInfo.getType() != 0) ? "" : activeNetworkInfo.getExtraInfo();
    }
}
