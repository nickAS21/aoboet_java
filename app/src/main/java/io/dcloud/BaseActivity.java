package io.dcloud;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.UEH;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.TestUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.feature.internal.splash.ActivityMgr;

/* compiled from: BaseActivity.java */
/* loaded from: classes.dex */
abstract class BaseActivity extends DCloudReceiverActivity implements IOnCreateSplashView, IKeyHandler {
    String a = null;
    String b = "Main_App";
    EntryProxy c = null;

    public void onCloseSplash() {
    }

    public Object onCreateSplash(Context context) {
        return AbsoluteConst.STREAMAPP_KEY_SPLASH;
    }

    @Override // io.dcloud.b, android.app.Activity
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        DeviceInfo.initPath(this.that);
        UEH.catchUncaughtException(this.that);
        Log.d("download_manager", "BaseActivity onCreate");
        TestUtil.print(TestUtil.START_STREAM_APP, "BaseActivity onCreate");
        onRuntimePreCreate(bundle);
        onCreateSplash(this.that);
        new Handler().postDelayed(new Runnable() { // from class: io.dcloud.a.1
            @Override // java.lang.Runnable
            public void run() {
                BaseActivity aVar = BaseActivity.this;
                aVar.a(aVar.getIntent());
                BaseActivity.this.a = "Main_Path_" + BaseActivity.this.b;
                ActivityMgr.a(BaseActivity.this.b);
                Logger.d(BaseActivity.this.a, "onCreate appid=" + BaseActivity.this.b);
                BaseActivity.this.onRuntimeCreate(bundle);
            }
        }, 200L);
    }

    public void setViewAsContentView(View view) {
        setContentView(view);
    }

    public void closeAppStreamSplash() {
        InvokeExecutorHelper.StreamAppListActivity.invoke("closeSplashPage", new Class[]{Boolean.class}, true);
        Logger.d(Logger.MAIN_TAG, "BaseActivity.closeAppStreamSplash");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null || !extras.containsKey("appid")) {
            return;
        }
        this.b = extras.getString("appid");
    }

    protected void onRuntimePreCreate(Bundle bundle) {
        Log.d(this.a, "onRuntimePreCreate appid=" + this.b);
        this.that.getWindow().setFormat(-3);
        ActivityMgr.a("main", this.that);
        a();
    }

    private void a() {
        if (AndroidResources.checkImmersedStatusBar(this.that)) {
            a(true);
        }
    }

    private void a(boolean z) {
        Window window = this.that.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (z) {
            attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        } else {
            attributes.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        }
        window.setAttributes(attributes);
    }

    protected void onRuntimeCreate(Bundle bundle) {
        Logger.d(this.a, "onRuntimeCreate appid=" + this.b);
        EntryProxy init = EntryProxy.init(this.that);
        this.c = init;
        init.onCreate(this.that, bundle, (SDK.IntegratedMode) null, (IOnCreateSplashView) null);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        Logger.d(this.a, "onCreateOptionsMenu appid=" + this.b);
        EntryProxy entryProxy = this.c;
        return entryProxy != null ? entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onCreateOptionMenu, menu) : super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        Logger.d(this.a, "onPause appid=" + this.b);
        EntryProxy entryProxy = this.c;
        if (entryProxy != null) {
            entryProxy.onPause(this.that);
        }
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        a(getIntent());
        Logger.d(this.a, "onResume appid=" + this.b);
        EntryProxy entryProxy = this.c;
        if (entryProxy != null) {
            entryProxy.onResume(this.that);
        }
        if (Build.VERSION.SDK_INT < 21 || BaseInfo.mDeStatusBarBackground != -111111) {
            return;
        }
        BaseInfo.mDeStatusBarBackground = getWindow().getStatusBarColor();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void updateParam(String str, Object obj) {
        if ("tab_change".equals(str)) {
            Logger.d("BaseActivity updateParam newintent value(appid)=" + obj);
            Intent intent = BaseInfo.sAppStateMap.get((String) obj);
            if (intent != null) {
                handleNewIntent(intent);
                return;
            } else {
                this.c.getCoreHandler().dispatchEvent(IMgr.MgrType.AppMgr, 21, obj);
                return;
            }
        }
        if ("closewebapp".equals(str)) {
            Activity activity = (Activity) obj;
            Bundle extras = activity.getIntent().getExtras();
            String string = (extras == null || !extras.containsKey("appid")) ? null : extras.getString("appid");
            if (activity instanceof IActivityHandler) {
                ((IActivityHandler) activity).closeAppStreamSplash(string);
            }
            this.c.getCoreHandler().dispatchEvent(null, 0, new Object[]{activity, activity.getIntent(), string});
        }
    }

    @Override // io.src.dcloud.adapter.DCloudBaseActivity
    public void onNewIntentImpl(Intent intent) {
        super.onNewIntentImpl(intent);
        Logger.d("syncStartApp", "BaseActivity onNewIntent appid=" + this.b);
        handleNewIntent(intent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void handleNewIntent(Intent intent) {
        EntryProxy entryProxy;
        setIntent(intent);
        a(intent);
        Logger.d("syncStartApp", "BaseActivity handleNewIntent =" + this.b + ";" + (intent.getFlags() != 274726912));
        if (intent.getFlags() == 274726912 || (entryProxy = this.c) == null) {
            return;
        }
        entryProxy.onNewIntent(this.that, intent);
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        EntryProxy entryProxy = this.c;
        if (entryProxy != null) {
            entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onRequestPermissionsResult, new Object[]{Integer.valueOf(i), strArr, iArr});
        }
    }

    @Override // io.dcloud.b, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        ActivityMgr.b(BaseActivity.this.b);
        Logger.d(this.a, "onDestroy appid=" + this.b);
        EntryProxy entryProxy = this.c;
        if (entryProxy != null) {
            entryProxy.onStop(this.that);
        }
        if (BaseInfo.mLaunchers != null) {
            BaseInfo.mLaunchers.clear();
        }
        MessageHandler.removeCallbacksAndMessages();
    }

    public boolean onKeyEventExecute(ISysEventListener.SysEventType sysEventType, int i, KeyEvent keyEvent) {
        EntryProxy entryProxy = this.c;
        if (entryProxy != null) {
            return entryProxy.onActivityExecute(this.that, sysEventType, new Object[]{Integer.valueOf(i), keyEvent});
        }
        return false;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean onKeyEventExecute;
        if (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyDown(i, keyEvent);
        }
        if (keyEvent.getRepeatCount() == 0) {
            onKeyEventExecute = onKeyEventExecute(ISysEventListener.SysEventType.onKeyDown, i, keyEvent);
        } else {
            onKeyEventExecute = onKeyEventExecute(ISysEventListener.SysEventType.onKeyLongPress, i, keyEvent);
        }
        return onKeyEventExecute ? onKeyEventExecute : super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        EntryProxy entryProxy;
        if (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            super.onBackPressed();
        } else {
            if (onKeyEventExecute(ISysEventListener.SysEventType.onKeyUp, 4, null) || (entryProxy = this.c) == null) {
                return;
            }
            entryProxy.destroy(this.that);
            super.onBackPressed();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        EntryProxy entryProxy;
        if (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyUp(i, keyEvent);
        }
        Logger.d(this.a, "onKeyUp");
        boolean z = false;
        if (i != 4 && (entryProxy = this.c) != null) {
            z = entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onKeyUp, new Object[]{Integer.valueOf(i), keyEvent});
        }
        return z ? z : super.onKeyUp(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        if (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyLongPress(i, keyEvent);
        }
        EntryProxy entryProxy = this.c;
        boolean onActivityExecute = entryProxy != null ? entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onKeyLongPress, new Object[]{Integer.valueOf(i), keyEvent}) : false;
        return onActivityExecute ? onActivityExecute : super.onKeyLongPress(i, keyEvent);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        try {
            Logger.d(this.a, "onConfigurationChanged");
            int i = getResources().getConfiguration().orientation;
            EntryProxy entryProxy = this.c;
            if (entryProxy != null) {
                entryProxy.onConfigurationChanged(this.that, i);
            }
            super.onConfigurationChanged(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        Logger.d(this.a, "onActivityResult");
        EntryProxy entryProxy = this.c;
        if (entryProxy != null) {
            entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onActivityResult, new Object[]{Integer.valueOf(i), Integer.valueOf(i2), intent});
        }
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null && getIntent() != null && getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        Logger.d(this.a, "onSaveInstanceState");
        EntryProxy entryProxy = this.c;
        if (entryProxy != null) {
            entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onSaveInstanceState, new Object[]{bundle});
        }
        super.onSaveInstanceState(bundle);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        Logger.d(this.a, "onLowMemory");
        displayBriefMemory();
    }

    protected void displayBriefMemory() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        Logger.i("mabo", "===============================");
        Logger.i("mabo", "程序最高可用内存:" + (Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "M");
        Logger.i("mabo", "程序总共占用内存:" + (Runtime.getRuntime().totalMemory() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "M");
        Logger.i("mabo", "程序所占剩余内存:" + (Runtime.getRuntime().freeMemory() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "M");
        Logger.i("mabo", "系统剩余内存:" + (memoryInfo.availMem / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "M");
        Logger.i("mabo", "系统是否处于低内存运行：" + memoryInfo.lowMemory);
        Logger.i("mabo", "当系统剩余内存低于" + (memoryInfo.threshold / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "M时就看成低内存运行");
    }

    @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        try {
            float parseFloat = Float.parseFloat(BaseInfo.sFontScale);
            if (configuration.fontScale != parseFloat) {
                configuration.fontScale = parseFloat;
            }
        } catch (NumberFormatException unused) {
            if ("none".equals(BaseInfo.sFontScale) && configuration.fontScale != 1.0f) {
                configuration.fontScale = 1.0f;
            }
        }
        return resources;
    }
}
