package io.dcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.nostra13.dcloudimageloader.core.ImageLoader;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.SplashView;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.FrameSwitchView;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.common_b.common_b_b.AndroidBug5497Workaround;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.AppStatus;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.feature.internal.splash.ISplash;
import io.dcloud.feature.internal.splash.SplashViewDBackground;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* loaded from: classes.dex */
public class WebAppActivity extends BaseActivity {
    public static final long ONE_SECOND = 1000;
    public static final long SPLASH_SECOND = 5000;
    static WebAppActivity q;
    private boolean A;
    BroadcastReceiver p;
    private long y;
    private boolean z;
    private Handler B = new Handler();
    private final String C = "remove-app_action";
    Bitmap r = null;
    View s = null;
    long t = 0;
    boolean u = true;
    FrameLayout v = null;
    LinearLayout w = null;
    FrameLayout x = null;

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void addAppStreamTask(String str, String str2) {
        super.addAppStreamTask(str, str2);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void bindDCloudServices() {
        super.bindDCloudServices();
    }

    @Override // io.dcloud.BaseActivity
    public /* bridge */ /* synthetic */ void closeAppStreamSplash() {
        super.closeAppStreamSplash();
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void commitActivateData(String str, String str2) {
        super.commitActivateData(str, str2);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void commitPointData(String str, String str2, String str3, int i, String str4, String str5) {
        super.commitPointData(str, str2, str3, i, str4, str5);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void commitPointData0(String str, int i, int i2, String str2) {
        super.commitPointData0(str, i, i2, str2);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void downloadSimpleFileTask(IApp iApp, String str, String str2, String str3) {
        super.downloadSimpleFileTask(iApp, str, str2, str3);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Override // io.dcloud.a, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public /* bridge */ /* synthetic */ Resources getResources() {
        return super.getResources();
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ String getUrlByFilePath(String str, String str2) {
        return super.getUrlByFilePath(str, str2);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ boolean isStreamAppMode() {
        return super.isStreamAppMode();
    }

    @Override // io.dcloud.a, android.app.Activity
    public /* bridge */ /* synthetic */ void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    @Override // io.dcloud.a, io.dcloud.common.DHInterface.IOnCreateSplashView
    public /* bridge */ /* synthetic */ void onCloseSplash() {
        super.onCloseSplash();
    }

    @Override // io.dcloud.a, android.app.Activity, android.content.ComponentCallbacks
    public /* bridge */ /* synthetic */ void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // io.dcloud.a, android.app.Activity
    public /* bridge */ /* synthetic */ boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override // io.dcloud.a, android.app.Activity, android.view.KeyEvent.Callback
    public /* bridge */ /* synthetic */ boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    @Override // io.dcloud.a, io.dcloud.d
    public /* bridge */ /* synthetic */ boolean onKeyEventExecute(ISysEventListener.SysEventType sysEventType, int i, KeyEvent keyEvent) {
        return super.onKeyEventExecute(sysEventType, i, keyEvent);
    }

    @Override // io.dcloud.a, android.app.Activity, android.view.KeyEvent.Callback
    public /* bridge */ /* synthetic */ boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return super.onKeyLongPress(i, keyEvent);
    }

    @Override // io.dcloud.a, android.app.Activity, android.view.KeyEvent.Callback
    public /* bridge */ /* synthetic */ boolean onKeyUp(int i, KeyEvent keyEvent) {
        return super.onKeyUp(i, keyEvent);
    }

    @Override // io.dcloud.a, android.app.Activity, android.content.ComponentCallbacks
    public /* bridge */ /* synthetic */ void onLowMemory() {
        super.onLowMemory();
    }

    @Override // io.dcloud.a, io.src.dcloud.adapter.DCloudBaseActivity
    public /* bridge */ /* synthetic */ void onNewIntentImpl(Intent intent) {
        super.onNewIntentImpl(intent);
    }

    @Override // io.dcloud.a, android.app.Activity
    public /* bridge */ /* synthetic */ void onPause() {
        super.onPause();
    }

    @Override // io.dcloud.a, android.app.Activity
    public /* bridge */ /* synthetic */ void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // io.dcloud.a, android.app.Activity
    public /* bridge */ /* synthetic */ void onResume() {
        super.onResume();
    }

    @Override // io.dcloud.a, android.app.Activity
    public /* bridge */ /* synthetic */ void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ boolean queryUrl(String str, String str2) {
        return super.queryUrl(str, str2);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ boolean raiseFilePriority(String str, String str2) {
        return super.raiseFilePriority(str, str2);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ Intent registerReceiver(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        return super.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override // io.dcloud.b
    public /* bridge */ /* synthetic */ Intent registerReceiver(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String str, Handler handler) {
        return super.registerReceiver(broadcastReceiver, intentFilter, str, handler);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void resumeAppStreamTask(String str) {
        super.resumeAppStreamTask(str);
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void unbindDCloudServices() {
        super.unbindDCloudServices();
    }

    @Override // io.dcloud.b, io.dcloud.common.DHInterface.IActivityHandler
    public /* bridge */ /* synthetic */ void unregisterReceiver(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver) {
        super.unregisterReceiver(broadcastReceiver);
    }

    @Override // io.dcloud.a, io.dcloud.b, android.app.Activity
    public void onCreate(Bundle bundle) {
        Log.d("WebAppActivity", "onCreate");
        super.onCreate(bundle);
        if (!this.A) {
            TestUtil.record(AbsoluteConst.RUN_5APP_TIME_KEY);
        }
        q = this;
        b();
        IntentFilter intentFilter = new IntentFilter("remove-app_action");
        intentFilter.addAction(AbsoluteConst.APKDOWNLOAD_END);
        intentFilter.addAction(getPackageName() + ".streamdownload.downloadfinish");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: io.dcloud.WebAppActivity.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (TextUtils.equals(action, "remove-app_action")) {
                    WebAppActivity.this.c.getCoreHandler().removeStreamApp(intent.getStringExtra("appid"));
                    return;
                }
                if (TextUtils.equals(action, AbsoluteConst.APKDOWNLOAD_END)) {
                    DialogUtil.showInstallAPKDialog(WebAppActivity.q, intent.getStringExtra(NotificationCompat.CATEGORY_MESSAGE), intent.getStringExtra("apkPath"));
                    return;
                }
                if (TextUtils.equals(action, WebAppActivity.this.getPackageName() + ".streamdownload.downloadfinish")) {
                    String stringExtra = intent.getStringExtra("flag");
                    int intExtra = intent.getIntExtra("status", 2);
                    if ((stringExtra.compareTo(AbsoluteConst.STREAMAPP_KEY_WAP2APP_INDEX) == 0 || stringExtra.compareTo("appstream") == 0 || stringExtra.compareTo(AbsoluteConst.STREAMAPP_KEY_STREAMAPPWGT) == 0) && intExtra == DCloudReceiverActivity.g) {
                        String stringExtra2 = intent.getStringExtra("appid");
                        int intExtra2 = intent.getIntExtra("type", -1);
                        if (intExtra2 == DCloudReceiverActivity.h || intExtra2 == DCloudReceiverActivity.i || intExtra2 == DCloudReceiverActivity.j) {
                            InvokeExecutorHelper.QihooInnerStatisticUtil.invoke("doEvent", new Class[]{String.class, String.class}, stringExtra2, "event_add_shortcut");
                            Logger.d("syncStartApp", "download MAIN_PAGE nAppid=" + stringExtra2);
                            Intent intent2 = new Intent(WebAppActivity.this.getIntent());
                            intent2.putExtra(IntentConst.WEBAPP_ACTIVITY_JUST_DOWNLOAD, true);
                            intent2.putExtra(IntentConst.WEBAPP_ACTIVITY_HAS_STREAM_SPLASH, false);
                            intent2.putExtra(IntentConst.IS_STREAM_APP, true);
                            intent2.putExtra("appid", stringExtra2);
                            WebAppActivity.this.handleNewIntent(intent2);
                            InvokeExecutorHelper.QHPushHelper.invoke("registerApp", stringExtra2, false);
                        }
                    }
                }
            }
        };
        this.p = broadcastReceiver;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        }
        FrameSwitchView frameSwitchView = FrameSwitchView.getInstance(this.that);
        if (!frameSwitchView.isInit()) {
            frameSwitchView.initView();
        }
        a();
    }

    private void a() {
        Intent intent = getIntent();
        boolean booleanExtra = intent != null ? intent.getBooleanExtra(IntentConst.PL_AUTO_HIDE, false) : false;
        Log.d("WebAppActivity", "checkAutoHide " + booleanExtra);
        if (booleanExtra) {
            Intent intent2 = new Intent();
            String stringExtra = intent.getStringExtra(IntentConst.PL_AUTO_HIDE_SHOW_PN);
            String stringExtra2 = intent.getStringExtra(IntentConst.PL_AUTO_HIDE_SHOW_ACTIVITY);
            intent2.putExtra(IntentConst.PL_AUTO_HIDE_SHOW_PN, true);
            intent2.setClassName(stringExtra, stringExtra2);
            this.that.startActivity(intent2);
            this.that.overridePendingTransition(0, 0);
            Log.d("WebAppActivity", "checkAutoHide return mini package " + stringExtra2);
        }
    }

    private void b() {
        Intent intent = getIntent();
        if (intent != null) {
            boolean booleanExtra = intent.getBooleanExtra(IntentConst.IS_STREAM_APP, false);
            this.A = booleanExtra;
            if (booleanExtra) {
                return;
            }
            intent.removeExtra("appid");
        }
    }

    @Override // io.dcloud.a, io.dcloud.b, android.app.Activity
    public void onDestroy() {
        Log.d("WebAppActivity", "onDestroy");
        super.onDestroy();
        unregisterReceiver(this.p);
        q = null;
        AndroidBug5497Workaround.a();
        Handler handler = this.B;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        FrameSwitchView.getInstance(this.that).clearData();
        PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "WebAppDestroy");
    }

    private Bitmap a(String str, String str2, String str3) {
        Bitmap bitmap = null;
        try {
            if (!TextUtils.isEmpty(str) && new File(str).exists()) {
                Logger.d(Logger.MAIN_TAG, "use splashPath=" + str);
                bitmap = BitmapFactory.decodeFile(str);
                if (bitmap != null) {
                    this.u = false;
                    try {
                        DHFile.deleteFile(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Logger.d(Logger.MAIN_TAG, "use splashPath=" + str);
                }
            }
            if (bitmap == null && !TextUtils.isEmpty(str2) && new File(str2).exists()) {
                Logger.d(Logger.MAIN_TAG, "use splashPath=" + str2);
                bitmap = BitmapFactory.decodeFile(str2);
            }
            if (bitmap != null || TextUtils.isEmpty(str3) || !new File(str3).exists()) {
                return bitmap;
            }
            Logger.d(Logger.MAIN_TAG, "use splashPath=" + str3);
            return BitmapFactory.decodeFile(str3);
        } catch (Exception e2) {
            e2.printStackTrace();
            return bitmap;
        }
    }

    @Override // io.dcloud.a, io.dcloud.common.DHInterface.IOnCreateSplashView
    public Object onCreateSplash(Context context) {
        boolean booleanExtra;
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("appid");
        View view = this.s;
        if (view != null && view.getTag() != null) {
            if (this.s.getTag().equals(stringExtra)) {
                return null;
            }
            closeAppStreamSplash(this.s.getTag().toString());
        }
        this.u = true;
        Logger.d("WebAppActivity", "onCreateSplash;intent=" + intent);
        if (intent.getBooleanExtra(IntentConst.IS_START_FIRST_WEB, false) || !(booleanExtra = intent.getBooleanExtra(IntentConst.SPLASH_VIEW, true))) {
            return null;
        }
        Logger.d("WebAppActivity", "onCreateSplash hasSplash=" + booleanExtra);
        if (intent.getBooleanExtra(IntentConst.PL_AUTO_HIDE, false)) {
            return null;
        }
        String stringExtra2 = intent.getStringExtra(IntentConst.WEBAPP_ACTIVITY_SPLASH_MODE);
        intent.removeExtra(IntentConst.WEBAPP_ACTIVITY_SPLASH_MODE);
        if (stringExtra2 == null || "".equals(stringExtra2.trim()) || (!"auto".equals(stringExtra2) && !"default".equals(stringExtra2))) {
            stringExtra2 = "auto";
        }
        Logger.d("WebAppActivity", "onCreateSplash __splash_mode__=" + stringExtra2);
        if (intent != null && intent.getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HIDE_STREAM_SPLASH, false)) {
            setViewAsContentView(new View(context));
            this.t = System.currentTimeMillis();
            this.z = true;
            return null;
        }
        if (intent != null && intent.getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HAS_STREAM_SPLASH, false)) {
            if (this.s == null) {
                if ("auto".equals(stringExtra2)) {
                    Bitmap a2 = a(StringConst.STREAMAPP_KEY_ROOTPATH + "splash_temp/" + stringExtra + ".png", intent.getStringExtra(IntentConst.APP_SPLASH_PATH), StringConst.STREAMAPP_KEY_ROOTPATH + "splash/" + stringExtra + ".png");
                    this.r = a2;
                    if (a2 != null) {
                        SplashView splashView = new SplashView(this.that, this.r);
                        this.s = splashView;
                        if (!this.u) {
                            splashView.showWaiting(SplashView.STYLE_BLACK);
                        }
                    }
                }
                if (this.s == null) {
                    if (BaseInfo.isShowTitleBar(context)) {
                        this.s = new a(this.that);
                    } else {
                        String stringExtra3 = intent.getStringExtra(IntentConst.WEBAPP_ACTIVITY_APPICON);
                        if (!TextUtils.isEmpty(stringExtra3) && new File(stringExtra3).exists()) {
                            this.r = BitmapFactory.decodeFile(stringExtra3);
                        }
                        if (this.r == null) {
                            a(stringExtra);
                        }
                        this.s = new SplashViewDBackground(context, this.r, intent.getStringExtra(IntentConst.NAME));
                    }
                }
            }
            this.s.setTag(stringExtra);
            setViewAsContentView(this.s);
            this.t = System.currentTimeMillis();
            this.z = true;
            return null;
        }
        if (this.s != null) {
            return null;
        }
        try {
            if ("auto".equals(stringExtra2)) {
                Bitmap a3 = a(StringConst.STREAMAPP_KEY_ROOTPATH + "splash_temp/" + stringExtra + ".png", intent.getStringExtra(IntentConst.APP_SPLASH_PATH), StringConst.STREAMAPP_KEY_ROOTPATH + "splash/" + stringExtra + ".png");
                this.r = a3;
                if (a3 == null && !BaseInfo.isForQihooHelper(context) && !BaseInfo.isStreamApp(context) && !BaseInfo.isForQihooBrowser(context)) {
                    String string = PlatformUtil.getOrCreateBundle("pdr").getString(SP.UPDATE_SPLASH_IMG_PATH, "");
                    if (!TextUtils.isEmpty(string)) {
                        try {
                            if (PdrUtil.isDeviceRootDir(string)) {
                                this.r = BitmapFactory.decodeFile(string);
                            } else {
                                InputStream open = getResources().getAssets().open(string);
                                this.r = BitmapFactory.decodeStream(open);
                                open.close();
                            }
                        } catch (Exception unused) {
                            this.r = null;
                        }
                    }
                    if (this.r == null) {
                        this.r = BitmapFactory.decodeResource(getResources(), RInformation.DRAWABLE_SPLASH);
                    }
                }
                if (this.r != null) {
                    SplashView splashView2 = new SplashView(this.that, this.r);
                    this.s = splashView2;
                    if (!this.u) {
                        splashView2.showWaiting(SplashView.STYLE_BLACK);
                    }
                }
            }
            if (this.s == null) {
                if (BaseInfo.isShowTitleBar(context)) {
                    this.s = new a(this.that);
                } else {
                    String stringExtra4 = intent.getStringExtra(IntentConst.WEBAPP_ACTIVITY_APPICON);
                    if (!TextUtils.isEmpty(stringExtra4) && new File(stringExtra4).exists()) {
                        this.r = BitmapFactory.decodeFile(stringExtra4);
                    }
                    if (this.r == null) {
                        a(stringExtra);
                    }
                    Log.d(Logger.MAIN_TAG, "use defaultSplash");
                    this.s = new SplashViewDBackground(context, this.r, intent.getStringExtra(IntentConst.NAME));
                }
            }
            this.s.setTag(stringExtra);
            setViewAsContentView(this.s);
            this.t = System.currentTimeMillis();
            this.z = true;
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // io.dcloud.a, io.dcloud.common.DHInterface.IActivityHandler
    public void updateParam(String str, Object obj) {
        if (NotificationCompat.CATEGORY_PROGRESS.equals(str)) {
            View view = this.s;
            if (view instanceof a) {
                ((a) view).a(((Integer) obj).intValue());
                return;
            }
            return;
        }
        if ("setProgressView".equals(str)) {
            setProgressView();
        } else {
            super.updateParam(str, obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends View {
        int a;
        float b;
        int c;
        Paint d;
        int e;
        int f;
        int g;

        a(Context context) {
            super(context);
            this.c = 0;
            this.d = new Paint();
            this.e = 0;
            this.f = 0;
            this.g = 255;
            this.a = context.getResources().getDisplayMetrics().widthPixels;
            int i = context.getResources().getDisplayMetrics().heightPixels;
            if (i == 1280) {
                this.b = 6.0f;
            } else if (i == 1920) {
                this.b = 9.0f;
            } else {
                this.b = context.getResources().getDisplayMetrics().heightPixels * 0.0045f;
            }
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            setMeasuredDimension(this.a, this.c + ((int) this.b));
        }

        void a() {
            a(100);
        }

        void b() {
            postDelayed(new Runnable() { // from class: io.dcloud.WebAppActivity.a.1
                @Override // java.lang.Runnable
                public void run() {
                    a aVar = a.this;
                    aVar.g -= 5;
                    if (a.this.g > 0) {
                        a.this.postDelayed(this, 5L);
                    } else {
                        ViewGroup viewGroup = (ViewGroup) a.this.getParent();
                        if (viewGroup != null) {
                            viewGroup.removeView(a.this);
                        }
                    }
                    a.this.invalidate();
                }
            }, 50L);
        }

        void a(int i) {
            int i2 = (this.a * i) / 100;
            if (this.e >= this.f) {
                postDelayed(new Runnable() { // from class: io.dcloud.WebAppActivity.a.2
                    @Override // java.lang.Runnable
                    public void run() {
                        int i3 = (a.this.f - a.this.e) / 10;
                        int i4 = i3 <= 10 ? i3 < 1 ? 1 : i3 : 10;
                        a.this.e += i4;
                        if (a.this.f > a.this.e) {
                            a.this.postDelayed(this, 5L);
                        } else if (a.this.f >= a.this.a) {
                            a.this.b();
                        }
                        a.this.invalidate();
                    }
                }, 5L);
            }
            this.f = i2;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.d.setColor(Color.argb(this.g, 26, 173, 25));
            int i = this.c;
            canvas.drawRect(0.0f, i, this.e, i + this.b, this.d);
        }
    }

    @Override // io.dcloud.common.DHInterface.IActivityHandler
    public void updateSplash(String str) {
        KeyEvent.Callback callback = this.s;
        if (callback == null || !(callback instanceof ISplash)) {
            return;
        }
        ((ISplash) callback).a(str);
    }

    private void a(String str) {
        ImageLoader.getInstance().loadImage(DataInterface.getIconImageUrl(str, getResources().getDisplayMetrics().widthPixels + ""), new ImageLoadingListener() { // from class: io.dcloud.WebAppActivity.2
            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingCancelled(String str2, View view) {
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingStarted(String str2, View view) {
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingFailed(String str2, View view, FailReason failReason) {
                if (StringConst.canChangeHost(str2)) {
                    ImageLoader.getInstance().loadImage(StringConst.changeHost(str2), this);
                }
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingComplete(String str2, View view, Bitmap bitmap) {
                if (WebAppActivity.this.s == null || !(WebAppActivity.this.s instanceof ISplash)) {
                    return;
                }
                ((ISplash) WebAppActivity.this.s).a(bitmap);
            }
        });
    }

    @Override // io.dcloud.common.DHInterface.IActivityHandler
    public void showSplashWaiting() {
        if (this.u) {
            View view = this.s;
            if (view instanceof SplashView) {
                ((SplashView) view).showWaiting();
            }
        }
    }

    @Override // io.dcloud.a, io.dcloud.common.DHInterface.IActivityHandler
    public void setViewAsContentView(View view) {
        Object invokeMethod;
        if (this.v == null) {
            this.v = new FrameLayout(this.that);
            if (BaseInfo.isShowTitleBar(this.that) && (invokeMethod = PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "getWebAppRootView", null, new Class[]{Activity.class}, new Object[]{this.that})) != null && (invokeMethod instanceof LinearLayout)) {
                this.w = (LinearLayout) invokeMethod;
            }
            if (this.w != null) {
                this.v.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                this.w.addView(this.v);
                setContentView(this.w);
            } else {
                setContentView(this.v);
            }
        }
        PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "checkNeedTitleView", null, new Class[]{Activity.class, String.class}, new Object[]{this.that, getIntent().getStringExtra("appid")});
        if ((BaseInfo.sGlobalFullScreen || AndroidResources.checkImmersedStatusBar(this.that)) && Build.VERSION.SDK_INT >= 23) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            layoutParams.topMargin = -1;
            this.v.setLayoutParams(layoutParams);
        }
        int indexOfChild = this.v.indexOfChild(this.s);
        int childCount = this.v.getChildCount();
        if (childCount > 0) {
            int i = childCount - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                View childAt = this.v.getChildAt(i);
                if (childAt != view) {
                    if ("AppRootView".equals(childAt.getTag())) {
                        this.v.addView(view, i);
                        this.v.removeView(childAt);
                        break;
                    } else if (i == 0) {
                        if (childAt == this.s) {
                            this.v.addView(view, 0);
                        } else if (indexOfChild > 0) {
                            this.v.addView(view, indexOfChild - 1);
                        } else {
                            this.v.addView(view);
                        }
                    }
                }
                i--;
            }
        } else {
            this.v.addView(view);
        }
        TestUtil.print(TestUtil.START_APP_SET_ROOTVIEW, "启动" + view);
        if (AndroidResources.checkImmersedStatusBar(this.that)) {
            io.dcloud.common.common_b.common_b_b.AndroidBug5497Workaround.a(this.v);
        }
    }

    public void setProgressView() {
        if (BaseInfo.isShowTitleBar(this.that)) {
            int i = 0;
            while (true) {
                if (i < this.v.getChildCount()) {
                    View childAt = this.v.getChildAt(i);
                    if (childAt != null && childAt == this.s) {
                        this.v.removeViewAt(i);
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            a aVar = new a(this.that);
            this.s = aVar;
            this.v.addView(aVar);
        }
    }

    public boolean isCanRefresh() {
        if (!BaseInfo.isShowTitleBar(this.that) || this.v == null) {
            return false;
        }
        for (int i = 0; i < this.v.getChildCount(); i++) {
            View childAt = this.v.getChildAt(i);
            if (childAt != null && (childAt instanceof SplashView)) {
                return false;
            }
        }
        return true;
    }

    @Override // io.dcloud.common.DHInterface.IActivityHandler
    public void setWebViewIntoPreloadView(View view) {
        if (this.x == null) {
            FrameLayout frameLayout = new FrameLayout(this.that);
            this.x = frameLayout;
            this.v.addView(frameLayout, 0);
        }
        this.x.addView(view);
    }

    @Override // io.dcloud.common.DHInterface.IActivityHandler
    public void closeAppStreamSplash(String str) {
        Logger.d("webappActivity closeAppStreamSplash");
        DCloudAdapterUtil.Plugin2Host_closeAppStreamSplash(str);
        Bitmap bitmap = this.r;
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                this.r.recycle();
                this.r = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.s != null) {
            Logger.d("webappActivity removeView mSplashView");
            View view = this.s;
            if (view instanceof a) {
                ((a) view).a();
            } else {
                this.v.removeView(view);
            }
            this.s = null;
        }
        this.z = false;
        this.t = 0L;
    }

    public static AlertDialog showDownloadDialog(String str, final DialogInterface.OnClickListener onClickListener) {
        WebAppActivity webAppActivity = q;
        if (webAppActivity == null || !webAppActivity.z) {
            return null;
        }
        AlertDialog create = new AlertDialog.Builder(q.that).create();
        create.setTitle("提示");
        if (TestUtil.PointTime.mEc == 20) {
            create.setMessage("检测到设备无网络，请检查系统网络设置！");
        } else if (str != null) {
            create.setMessage("进入流应用" + str + "失败" + getErrorTipMsg());
        } else {
            create.setMessage("进入流应用失败" + getErrorTipMsg());
        }
        create.setCanceledOnTouchOutside(false);
        create.setButton(-1, "重试", onClickListener);
        create.setButton(-2, "关闭", new DialogInterface.OnClickListener() { // from class: io.dcloud.WebAppActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                WebAppActivity.q.updateParam("closewebapp", WebAppActivity.q);
                onClickListener.onClick(dialogInterface, i);
            }
        });
        create.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: io.dcloud.WebAppActivity.4
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 1 || i != 4) {
                    return false;
                }
                WebAppActivity.q.updateParam("closewebapp", WebAppActivity.q);
                onClickListener.onClick(dialogInterface, -2);
                return true;
            }
        });
        create.show();
        return create;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        PlatformUtil.SCREEN_WIDTH(this.that);
        PlatformUtil.SCREEN_HEIGHT(this.that);
        PlatformUtil.MESURE_SCREEN_STATUSBAR_HEIGHT(this.that);
    }

    private void c() {
        Toast.makeText(this.that, (getIntent() == null || !getIntent().hasExtra(IntentConst.NAME)) ? "再按一次返回键关闭流应用" : "再按一次返回键关闭" + getIntent().getStringExtra(IntentConst.NAME), Toast.LENGTH_SHORT).show();
    }

    @Override // io.dcloud.a, android.app.Activity
    public void onBackPressed() {
        Logger.e("WebAppActivity", "onBackPressed");
        if (this.A && this.z) {
            if ("all".equalsIgnoreCase(BaseInfo.sSplashExitCondition)) {
                super.onBackPressed();
                return;
            }
            if (this.t == 0 || System.currentTimeMillis() - this.t >= SPLASH_SECOND) {
                if (this.y == 0) {
                    c();
                    this.y = System.currentTimeMillis();
                    this.B.postDelayed(new Runnable() { // from class: io.dcloud.WebAppActivity.5
                        @Override // java.lang.Runnable
                        public void run() {
                            WebAppActivity.this.y = 0L;
                        }
                    }, 1000L);
                    return;
                }
                if (System.currentTimeMillis() - this.y > 1000) {
                    this.y = 0L;
                    c();
                    return;
                }
                String str = this.b;
                if (str == null && getIntent() != null && getIntent().hasExtra("appid")) {
                    str = getIntent().getStringExtra("appid");
                }
                if (str != null) {
                    if (TestUtil.PointTime.hasPointTime(TestUtil.STREAM_APP_POINT)) {
                        TestUtil.PointTime.getPointTime(TestUtil.STREAM_APP_POINT).point();
                    }
                    TestUtil.PointTime.commit(this, str, 2, 2, "");
                    AppStatus.setAppStatus(str, 0);
                } else {
                    Logger.e("onBackPressed appid 不能为null的");
                }
                Logger.i("WebAppActivity.onBackPressed finish");
                finish();
                PlatformUtil.invokeMethod("io.dcloud.appstream.StreamAppMainActivity", "closeSplashPage", null, new Class[]{Boolean.TYPE}, new Object[]{false});
                return;
            }
            return;
        }
        super.onBackPressed();
    }

    public static String getErrorTipMsg() {
        if (TestUtil.PointTime.mEc == 4) {
            return ", 无SD卡！";
        }
        if (TestUtil.PointTime.mEc == 9) {
            return ", SD卡空间不足！";
        }
        if (TestUtil.PointTime.mEt == 1) {
            return ", 配置文件下载失败！";
        }
        return (TestUtil.PointTime.mEt == 3 || TestUtil.PointTime.mEt == 2) ? ", 应用资源下载失败！" : "";
    }
}
