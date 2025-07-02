package io.dcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;

import java.util.ArrayList;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.common_b.PandoraEntryProxy;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.feature.internal.sdk.SDK;

/* loaded from: classes.dex */
public class EntryProxy {
    public static boolean a = false;
    private static EntryProxy e;
    private ArrayList<Activity> d = new ArrayList<>(1);
    boolean b = false;
    PandoraEntryProxy c = null;

    private EntryProxy() {
    }

    public static EntryProxy init(Activity activity, ICore.ICoreStatusListener iCoreStatusListener) {
        a = true;
        Context applicationContext = activity.getApplicationContext();
        DCloudApplication.setInstance(applicationContext);
        AndroidResources.initAndroidResources(applicationContext);
        EntryProxy entryProxy = e;
        if (entryProxy != null && entryProxy.c.b() != applicationContext) {
            e.destroy(activity);
        }
        if (e == null) {
            e = new EntryProxy();
            CookieSyncManager.createInstance(applicationContext);
            e.c = new PandoraEntryProxy(applicationContext, iCoreStatusListener);
        }
        e.d.add(activity);
        return e;
    }

    public static EntryProxy init(Activity activity) {
        return init(activity, null);
    }

    public static EntryProxy getInstnace() {
        return e;
    }

    public boolean didCreate() {
        return this.b;
    }

    @Deprecated
    public boolean onCreate(Bundle bundle, FrameLayout frameLayout, SDK.IntegratedMode integratedMode, IOnCreateSplashView iOnCreateSplashView) {
        return onCreate(bundle, integratedMode, iOnCreateSplashView);
    }

    @Deprecated
    public boolean onCreate(Bundle bundle, SDK.IntegratedMode integratedMode, IOnCreateSplashView iOnCreateSplashView) {
        return onCreate(this.d.get(this.d.size() - 1), bundle, integratedMode, iOnCreateSplashView);
    }

    public boolean onCreate(Activity activity, Bundle bundle, SDK.IntegratedMode integratedMode, IOnCreateSplashView iOnCreateSplashView) {
        AndroidResources.initAndroidResources(activity.getBaseContext());
        this.c.a(activity, bundle, integratedMode, iOnCreateSplashView);
        return true;
    }

    @Deprecated
    public boolean onCreate(Bundle bundle) {
        return onCreate(bundle, (FrameLayout) null, (SDK.IntegratedMode) null, (IOnCreateSplashView) null);
    }

    public boolean onActivityExecute(Activity activity, ISysEventListener.SysEventType sysEventType, Object obj) {
        PandoraEntryProxy bVar = this.c;
        if (bVar != null) {
            return bVar.a(activity, sysEventType, obj);
        }
        return false;
    }

    public void onPause(Activity activity) {
        PandoraEntryProxy bVar = this.c;
        if (bVar != null) {
            bVar.b(activity);
        }
        CookieSyncManager.getInstance().stopSync();
    }

    public void onResume(Activity activity) {
        PandoraEntryProxy bVar = this.c;
        if (bVar != null) {
            bVar.c(activity);
        }
        CookieSyncManager.getInstance().startSync();
    }

    public void onNewIntent(Activity activity, Intent intent) {
        PandoraEntryProxy bVar = this.c;
        if (bVar != null) {
            bVar.a(activity, intent);
        }
    }

    public void onConfigurationChanged(Activity activity, int i) {
        PandoraEntryProxy bVar = this.c;
        if (bVar != null) {
            bVar.a(activity, i);
        }
    }

    public void onStop(Activity activity) {
        try {
            this.d.remove(activity);
            if (this.d.size() == 0) {
                PandoraEntryProxy bVar = this.c;
                if (bVar != null) {
                    if (bVar.a(activity)) {
                        clearData();
                    }
                } else {
                    clearData();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void clearData() {
        Log.d("EntryProxy", " clearData");
        e = null;
        a = false;
        this.b = false;
        AndroidResources.clearData();
        BaseInfo.clearData();
        this.c = null;
    }

    public void destroy(Activity activity) {
        onStop(activity);
    }

    public ICore getCoreHandler() {
        return this.c.a();
    }
}
