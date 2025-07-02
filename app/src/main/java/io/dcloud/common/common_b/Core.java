package io.dcloud.common.common_b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IBoot;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.AsyncTaskHandler;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.common_a.AppMgr;
import io.dcloud.common.common_b.common_b_b.l;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.net.NetMgr;
import io.dcloud.feature.FeatureMgr;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Core.java */
/* loaded from: classes.dex  old a*/
public class Core implements ICore {
    private static Core n;
    boolean a = false;
    Context b = null;
    AbsMgr c = null;
    AbsMgr d = null;
    AbsMgr e = null;
    AbsMgr f = null;
    private ICore.ICoreStatusListener l = null;
    HashMap<String, IBoot> g = null;
    final int h = 0;
    final int i = 1;
    final int j = 2;
    final int k = 3;
    private boolean m = false;

    private Core() {
    }

    public static Core a(Context context, ICore.ICoreStatusListener iCoreStatusListener) {
        if (n == null) {
            n = new Core();
        }
        Core aVar = n;
        aVar.b = context;
        aVar.l = iCoreStatusListener;
        SDK.initSDK(aVar);
        return n;
    }

    @Override // io.dcloud.common.DHInterface.ICore
    public final boolean isStreamAppMode() {
        return this.m;
    }

    @Override // io.dcloud.common.DHInterface.ICore
    public final void setIsStreamAppMode(boolean z) {
        this.m = z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.dcloud.common.DHInterface.ICore
    public boolean onActivityExecute(Activity activity, ISysEventListener.SysEventType sysEventType, Object obj) {
        String str;
        Bundle extras;
        if (obj instanceof IApp) {
            str = ((IApp)obj).obtainAppId();
        } else {
            str = BaseInfo.sRuntimeMode != null ? null : BaseInfo.sDefaultBootApp;
            Intent intent = activity.getIntent();
            if (intent != null && (extras = intent.getExtras()) != null && extras.containsKey("appid")) {
                str = extras.getString("appid");
            }
        }
        Object dispatchEvent = dispatchEvent(IMgr.MgrType.AppMgr, 1, new Object[]{sysEventType, obj, str});
        if (sysEventType.equals(ISysEventListener.SysEventType.onKeyUp) && !((Boolean) dispatchEvent).booleanValue() && ((Integer) ((Object[]) obj)[0]).intValue() == 4) {
            if (sysEventType.equals(ISysEventListener.SysEventType.onKeyUp)) {
                if (activity instanceof IActivityHandler) {
                    ((IActivityHandler) activity).closeAppStreamSplash(str);
                }
                a(activity, activity.getIntent(), (IApp) null, str);
            }
            return true;
        }
        return Boolean.parseBoolean(String.valueOf(dispatchEvent));
    }

    public void a(Activity activity, Bundle bundle, SDK.IntegratedMode integratedMode) {
        BaseInfo.sRuntimeMode = integratedMode;
        if (integratedMode != null) {
            BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT = true;
        }
        if (this.a) {
            return;
        }
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
        if (iActivityHandler != null) {
            this.m = iActivityHandler.isStreamAppMode();
        }
        a(activity.getApplicationContext());
        Logger.i("Core onInit mode=" + integratedMode);
        a(ISysEventListener.SysEventType.onStart, bundle);
        Logger.i("Core onInit mCoreListener=" + this.l);
        try {
            if (BaseInfo.sRuntimeMode != null && BaseInfo.sRuntimeMode != SDK.IntegratedMode.RUNTIME) {
                ICore.ICoreStatusListener iCoreStatusListener = this.l;
                if (iCoreStatusListener != null) {
                    iCoreStatusListener.onCoreInitEnd(this);
                }
            }
            ICore.ICoreStatusListener iCoreStatusListener2 = this.l;
            if (iCoreStatusListener2 != null) {
                iCoreStatusListener2.onCoreInitEnd(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void a(Context context) {
        TestUtil.record("core", Thread.currentThread());
        this.f = new FeatureMgr(this);
        this.g = (HashMap) dispatchEvent(IMgr.MgrType.FeatureMgr, 0, this.b);
        BaseInfo.parseControl(this, this.l);
        Logger.initLogger(context);
        DeviceInfo.init(context);
        this.d = new AppMgr(this);
        this.c = new l(this);
        this.e = new NetMgr(this);
        this.a = true;
        try {
            b(context);
        } catch (Exception e) {
            Logger.e("initSDKData " + e.getLocalizedMessage());
        }
    }

    private void b(final Context context) {
        if (!BaseInfo.s_Is_DCloud_Packaged && BaseInfo.existsBase() && TextUtils.isEmpty(PlatformUtil.getBundleData(BaseInfo.PDR, AbsoluteConst.XML_NS))) {
            ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.common.b.a.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        JSONObject jSONObject = new JSONObject(new String(NetTool.httpGet(String.format("https://service.dcloud.net.cn/sta/so?p=a&pn=%s&ver=%s&appid=%s", context.getPackageName(), context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName, BaseInfo.sDefaultBootApp)), "utf-8"));
                        if (jSONObject.has("ret") && jSONObject.optInt("ret") == 0) {
                            PlatformUtil.setBundleData(BaseInfo.PDR, AbsoluteConst.XML_NS, AbsoluteConst.TRUE);
                        }
                    } catch (Exception unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Activity activity, IApp iApp) {
        onActivityExecute(activity, ISysEventListener.SysEventType.onWebAppStop, iApp);
    }

    public void a(final Activity activity, final Intent intent, final IApp iApp, final String str) {
        boolean z = this.l != null ? this.l.onCoreStop() : true;
        if (iApp != null) {
            dispatchEvent(IMgr.MgrType.AppMgr, 13, iApp);
        }
        if (z) {
            IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
            final boolean equals = TextUtils.equals(activity.getIntent().getStringExtra("appid"), str);
            if (iActivityHandler != null && (iApp == null || (iApp != null && iApp.isStreamApp()))) {
                if (iActivityHandler.isStreamAppMode()) {
                    IntentConst.modifyStartFrom(intent);
                    int intExtra = intent.getIntExtra(IntentConst.START_FROM, -1);
                    if (!BaseInfo.sAppStateMap.containsKey(str) && intExtra != 1 && !BaseInfo.isShowTitleBar(activity) && equals) {
                        Logger.d(Logger.MAIN_TAG, "moveTaskToBack");
                        activity.moveTaskToBack(false);
                        BaseInfo.setLoadingLaunchePage(false, "core stopApp");
                    }
                }
                MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.b.a.2
                    @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                    public void execute(Object obj) {
                        Activity activity2 = activity;
                        Intent intent2 = intent;
                        String str2 = str;
                        if (iApp != null) {
                            Core.this.a(activity2, iApp);
                            iApp.getActivity();
                            intent2 = iApp.obtainWebAppIntent();
                            str2 = iApp.obtainAppId();
                        }
                        if (equals) {
                            Core.this.b(activity, intent2, iApp, str2);
                        }
                        if (((Boolean) Core.this.dispatchEvent(IMgr.MgrType.WindowMgr, 32, new Object[]{activity, str})).booleanValue()) {
                            return;
                        }
                        activity.finish();
                    }
                }, 10L, null);
            } else {
                activity.finish();
            }
        }
        BaseInfo.sGlobalFullScreen = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Activity activity, Intent intent, IApp iApp, String str) {
        String lastKey;
        Intent intent2;
        IntentConst.modifyStartFrom(intent);
        int intExtra = intent.getIntExtra(IntentConst.START_FROM, -1);
        if (((intExtra == 2 || intExtra == 5) && !BaseInfo.isShowTitleBar(activity)) || BaseInfo.sAppStateMap.containsKey(str)) {
            return;
        }
        IApp iApp2 = (IApp) dispatchEvent(IMgr.MgrType.AppMgr, 19, null);
        boolean z = iApp2 != null;
        if (z) {
            lastKey = iApp.obtainAppId();
        } else {
            lastKey = !BaseInfo.sAppStateMap.isEmpty() ? BaseInfo.getLastKey(BaseInfo.sAppStateMap) : null;
        }
        if (iApp != null && iApp.getIAppStatusListener() != null) {
            lastKey = iApp.getIAppStatusListener().onStoped(BaseInfo.sAppStateMap.containsKey(iApp.obtainAppId()), lastKey);
        }
        if (lastKey != null) {
            if (z && TextUtils.equals(iApp.obtainAppId(), lastKey)) {
                activity = iApp.getActivity();
                intent2 = iApp2.obtainWebAppIntent();
            } else if (BaseInfo.sAppStateMap.containsKey(lastKey)) {
                intent2 = BaseInfo.sAppStateMap.get(lastKey);
            } else {
                intent2 = new Intent();
                intent2.putExtra("appid", lastKey);
                intent2.putExtra(IntentConst.START_FROM, 1);
            }
            intent2.putExtra(IntentConst.IS_WEBAPP_REPLY, true);
            activity.setIntent(intent2);
            Logger.d(lastKey + " will be the Frontest");
            dispatchEvent(null, 2, new Object[]{activity, lastKey, IntentConst.obtainArgs(intent2, lastKey), activity});
        }
    }

    public IApp a(Activity activity, String str, String str2, IOnCreateSplashView iOnCreateSplashView) {
        return a(activity, str, str2, iOnCreateSplashView, false);
    }

    public IApp a(Activity activity, String str, String str2, IOnCreateSplashView iOnCreateSplashView, boolean z) {
        TestUtil.record("GET_STATUS_BY_APPID");
        Logger.d("syncStartApp " + str);
        byte parseByte = Byte.parseByte(String.valueOf(dispatchEvent(IMgr.MgrType.AppMgr, 12, str)));
        TestUtil.print("GET_STATUS_BY_APPID");
        boolean z2 = !a(activity.getIntent());
        if (1 == parseByte) {
            Logger.d("syncStartApp " + str + " STATUS_UN_RUNNING");
            if (iOnCreateSplashView != null) {
                Logger.d("syncStartApp " + str + " ShowSplash");
                iOnCreateSplashView.onCreateSplash(activity);
            }
        }
        if (!z2) {
            return null;
        }
        try {
            TestUtil.record(TestUtil.START_APP_SET_ROOTVIEW, "启动" + str);
            IApp iApp = (IApp) dispatchEvent(IMgr.MgrType.AppMgr, 0, new Object[]{activity, str, str2});
            iApp.setOnCreateSplashView(iOnCreateSplashView);
            if (z && (3 == parseByte || 2 == parseByte)) {
                onActivityExecute(activity, ISysEventListener.SysEventType.onNewIntent, str2);
            }
            return iApp;
        } catch (Exception unused) {
            Logger.d("syncStartApp appid=" + str);
            return null;
        }
    }

    public boolean a(Intent intent) {
        if (intent != null) {
            return intent.getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HAS_STREAM_SPLASH, false);
        }
        return false;
    }

    public void a(final Activity activity, final String str, final String str2) {
        AsyncTaskHandler.executeAsyncTask(new AsyncTaskHandler.IAsyncTaskListener() { // from class: io.dcloud.common.b.a.3
            @Override // io.dcloud.common.adapter.util.AsyncTaskHandler.IAsyncTaskListener
            public void onCancel() {
            }

            @Override // io.dcloud.common.adapter.util.AsyncTaskHandler.IAsyncTaskListener
            public void onExecuteBegin() {
            }

            @Override // io.dcloud.common.adapter.util.AsyncTaskHandler.IAsyncTaskListener
            public Object onExecuting() {
                return null;
            }

            @Override // io.dcloud.common.adapter.util.AsyncTaskHandler.IAsyncTaskListener
            public void onExecuteEnd(Object obj) {
                MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.b.a.3.1
                    @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                    public void execute(Object obj2) {
                        Core.this.a(activity, str, str2, (IOnCreateSplashView) null);
                    }
                }, null);
            }
        }, null);
    }

    private void a(ISysEventListener.SysEventType sysEventType, Object obj) {
        Collection<IBoot> values;
        HashMap<String, IBoot> hashMap = this.g;
        if (hashMap == null || (values = hashMap.values()) == null) {
            return;
        }
        for (IBoot iBoot : values) {
            if (iBoot != null) {
                try {
                    int i = AnonymousClass4.a[sysEventType.ordinal()];
                    if (i == 1) {
                        iBoot.onStart(this.b, (Bundle) obj, new String[]{BaseInfo.sDefaultBootApp});
                    } else if (i == 2) {
                        iBoot.onStop();
                    } else if (i == 3) {
                        iBoot.onPause();
                    } else if (i == 4) {
                        iBoot.onResume();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean a(Activity activity) {
        PlatformUtil.invokeMethod("io.dcloud.feature.apsqh.QHNotifactionReceiver", "doSaveNotifications", null, new Class[]{Context.class}, new Object[]{activity.getBaseContext()});
        try {
            onActivityExecute(activity, ISysEventListener.SysEventType.onStop, null);
            this.a = false;
            BaseInfo.setLoadingLaunchePage(false, "onStop");
            a(ISysEventListener.SysEventType.onStop, (Object) null);
            n = null;
            this.c.dispose();
            this.c = null;
            this.d.dispose();
            this.d = null;
            this.e.dispose();
            this.e = null;
            this.f.dispose();
            this.f = null;
            Logger.e(Logger.MAIN_TAG, "core exit");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void b(Activity activity) {
        a(ISysEventListener.SysEventType.onPause, (Object) null);
        if (this.a) {
            AbsMgr absMgr = this.e;
            if (absMgr != null) {
                absMgr.onExecute(ISysEventListener.SysEventType.onPause, null);
            }
            onActivityExecute(activity, ISysEventListener.SysEventType.onPause, null);
        }
        System.gc();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void c(Activity activity) {
        Bundle extras;
        a(ISysEventListener.SysEventType.onResume, (Object) null);
        this.e.onExecute(ISysEventListener.SysEventType.onResume, null);
        if (onActivityExecute(activity, ISysEventListener.SysEventType.onResume, null)) {
            return;
        }
        Intent intent = activity.getIntent();
        String string = (intent == null || (extras = intent.getExtras()) == null) ? null : extras.getString("appid");
        if (PdrUtil.isEmpty(string)) {
            return;
        }
        a(activity, string, IntentConst.obtainArgs(intent, string), activity instanceof IOnCreateSplashView ? (IOnCreateSplashView) activity : null);
    }

    @Override // io.dcloud.common.DHInterface.ICore
    public Object dispatchEvent(IMgr.MgrType mgrType, int i, Object obj) {
        Object processEvent;
        if (mgrType == null) {
            return a(i, obj);
        }
        try {
            if (n == null) {
                n = this;
            }
            int i2 = AnonymousClass4.b[mgrType.ordinal()];
            if (i2 == 1) {
                AbsMgr absMgr = n.d;
                if (absMgr == null) {
                    return null;
                }
                processEvent = absMgr.processEvent(mgrType, i, obj);
            } else if (i2 == 2) {
                processEvent = n.e.processEvent(mgrType, i, obj);
            } else if (i2 == 3) {
                processEvent = n.f.processEvent(mgrType, i, obj);
            } else {
                if (i2 != 4) {
                    return null;
                }
                processEvent = n.c.processEvent(mgrType, i, obj);
            }
            return processEvent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Core.java */
    /* renamed from: io.dcloud.common.b.a$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] a;
        static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[IMgr.MgrType.values().length];
            b = iArr;
            try {
                iArr[IMgr.MgrType.AppMgr.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[IMgr.MgrType.NetMgr.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[IMgr.MgrType.FeatureMgr.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                b[IMgr.MgrType.WindowMgr.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[ISysEventListener.SysEventType.values().length];
            a = iArr2;
            try {
                iArr2[ISysEventListener.SysEventType.onStart.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[ISysEventListener.SysEventType.onStop.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[ISysEventListener.SysEventType.onPause.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[ISysEventListener.SysEventType.onResume.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    private Object a(int i, Object obj) {
        Activity activity;
        IApp iApp;
        Intent intent;
        String str;
        if (i == -1) {
            return BaseInfo.sRuntimeMode;
        }
        if (i != 0) {
            if (i == 1) {
                return Boolean.valueOf(this.g.containsValue((IBoot) obj));
            }
            if (i != 2) {
                return null;
            }
            Object[] objArr = (Object[]) obj;
            a((Activity) objArr[0], (String) objArr[1], (String) objArr[2], (IOnCreateSplashView) objArr[3]);
            return null;
        }
        if (obj instanceof IApp) {
            iApp = (IApp) obj;
            activity = iApp.getActivity();
            intent = iApp.obtainWebAppIntent();
            str = iApp.obtainAppId();
        } else if (obj instanceof Object[]) {
            Object[] objArr2 = (Object[]) obj;
            activity = (Activity) objArr2[0];
            intent = (Intent) objArr2[1];
            str = (String) objArr2[2];
            iApp = null;
        } else {
            activity = null;
            iApp = null;
            intent = null;
            str = null;
        }
        a(activity, intent, iApp, str);
        return null;
    }

    @Override // io.dcloud.common.DHInterface.ICore
    public Context obtainContext() {
        return this.b;
    }

    @Override // io.dcloud.common.DHInterface.ICore
    public void removeStreamApp(String str) {
        AbsMgr absMgr = this.d;
        if (absMgr != null) {
            absMgr.processEvent(IMgr.MgrType.AppMgr, 17, str);
        }
        if (this.c != null) {
            this.d.processEvent(IMgr.MgrType.WindowMgr, 40, str);
        }
    }
}
