package io.dcloud.common.util;

import java.util.HashMap;

/* loaded from: classes.dex */
public class AppStatus {
    public static final int ACTIVE = 2;
    public static final int STOPPED = 0;
    private static HashMap<String, Integer> sMaps = new HashMap<>();

    public static void setAppStatus(String str, int i) {
        sMaps.put(str, Integer.valueOf(i));
    }

    public static int getAppStatus(String str) {
        if (sMaps.containsKey(str)) {
            return sMaps.get(str).intValue();
        }
        return 2;
    }
}
