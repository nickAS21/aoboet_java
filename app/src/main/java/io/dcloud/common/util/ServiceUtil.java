package io.dcloud.common.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;

/* loaded from: classes.dex */
public class ServiceUtil {
    public static boolean isServiceRunning(Context context, Class<?> cls) {
        Iterator<ActivityManager.RunningServiceInfo> it = ((ActivityManager) context.getSystemService("activity")).getRunningServices(1024).iterator();
        while (it.hasNext()) {
            if (cls.getName().equals(it.next().service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
