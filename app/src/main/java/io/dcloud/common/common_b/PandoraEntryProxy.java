package io.dcloud.common.common_b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.internal.sdk.SDK;

/* compiled from: PandoraEntryProxy.java */
/* loaded from: classes.dex old b*/
public class PandoraEntryProxy {
    Context a;
    Core b;
    private long c = 0;

    public PandoraEntryProxy(Context context, ICore.ICoreStatusListener iCoreStatusListener) {
        this.a = null;
        this.b = null;
        this.a = context;
        this.b = Core.a(context, iCoreStatusListener);
    }

    public void a(Activity activity, Bundle bundle, SDK.IntegratedMode integratedMode, IOnCreateSplashView iOnCreateSplashView) {
        if (iOnCreateSplashView != null && integratedMode != SDK.IntegratedMode.WEBAPP && integratedMode != SDK.IntegratedMode.WEBVIEW) {
            iOnCreateSplashView.onCreateSplash(null);
        }
        this.b.a(activity, bundle, integratedMode);
        if (activity.getIntent().getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HAS_STREAM_SPLASH, false) || BaseInfo.sRuntimeMode != null) {
            return;
        }
        a(activity, activity.getIntent(), (IOnCreateSplashView) null);
    }

    public IApp a(Activity activity, Intent intent, IOnCreateSplashView iOnCreateSplashView) {
        Bundle extras;
        String string = (intent == null || (extras = intent.getExtras()) == null) ? null : extras.getString("appid");
        if (PdrUtil.isEmpty(string)) {
            string = BaseInfo.sDefaultBootApp;
        }
        String obtainArgs = IntentConst.obtainArgs(intent, string);
        Logger.i("onStart appid=" + string + ";intentArgs=" + obtainArgs);
        if (iOnCreateSplashView == null) {
            this.b.a(activity, string, obtainArgs);
            return null;
        }
        return this.b.a(activity, string, obtainArgs, iOnCreateSplashView);
    }

    public boolean a(Activity activity, ISysEventListener.SysEventType sysEventType, Object obj) {
        if (activity.getIntent().getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HAS_STREAM_SPLASH, false) && !"all".equalsIgnoreCase(BaseInfo.sSplashExitCondition)) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.c;
        if (currentTimeMillis - j > 500) {
            return this.b.onActivityExecute(activity, sysEventType, obj);
        }
        return j > 0 && sysEventType == ISysEventListener.SysEventType.onKeyUp;
    }

    public boolean a(Activity activity) {
        Logger.i("onStop");
        if (!this.b.a(activity)) {
            return false;
        }
        this.b = null;
        return true;
    }

    public void b(Activity activity) {
        Logger.i("onPause");
        this.b.b(activity);
        this.c = 0L;
    }

    public void c(Activity activity) {
        this.c = System.currentTimeMillis();
        Logger.i("onResume resumeTime=" + this.c);
        this.b.c(activity);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void a(Activity activity, Intent intent) {
        String str;
        if (intent != null) {
            Bundle extras = intent.getExtras();
            str = extras != null ? extras.getString("appid") : null;
        } else {
            str = null;
        }
        String obtainArgs = IntentConst.obtainArgs(intent, str);
        if (!PdrUtil.isEmpty(str)) {
            this.b.a(activity, str, obtainArgs, activity instanceof IOnCreateSplashView ? (IOnCreateSplashView) activity : null, true);
        } else {
            this.b.onActivityExecute(activity, ISysEventListener.SysEventType.onNewIntent, obtainArgs);
        }
    }

    public void a(Activity activity, int i) {
        Logger.i(Logger.LAYOUT_TAG, "onConfigurationChanged pConfig=" + i);
        this.b.onActivityExecute(activity, ISysEventListener.SysEventType.onConfigurationChanged, 1);
    }

    @Deprecated
    public ICore a() {
        return this.b;
    }

    public Context b() {
        return this.a;
    }
}
