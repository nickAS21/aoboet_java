package io.dcloud.application;

import android.app.Application;
import android.content.Context;

import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.UEH;

/* loaded from: classes.dex */
public class DCloudApplication extends Application {
    private static final String a = "DCloudApplication";
    private static DCloudApplication b;
    private static Context c;
    public boolean isQihooTrafficFreeValidate = true;

    public static Context getInstance() {
        return c;
    }

    public static void setInstance(Context context) {
        if (c == null) {
            c = context;
        }
    }

    public static DCloudApplication self() {
        return b;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        b = this;
        setInstance(getApplicationContext());
        UEH.catchUncaughtException(getApplicationContext());
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        Logger.e(a, "onLowMemory" + Runtime.getRuntime().maxMemory());
    }

    @Override // android.app.Application, android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        Logger.e(a, "onTrimMemory");
    }
}
