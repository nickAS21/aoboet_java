package io.dcloud.feature.internal.splash;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: ActivityMgr.java */
/* loaded from: classes.dex old a */
public class ActivityMgr {
    private static HashMap<String, Activity> b = new HashMap<>();
    static ArrayList<String> a = new ArrayList<>();

    public static void a(String str, Activity activity) {
        b.put(str, activity);
    }

    public static void a(String str) {
        a.add(str);
    }

    public static void b(String str) {
        a.remove(str);
    }
}
