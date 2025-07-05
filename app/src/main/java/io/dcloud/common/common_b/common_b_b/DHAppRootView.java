package io.dcloud.common.common_b.common_b_b;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.FeatureMessageDispatcher;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebAppRootView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaContainerFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.common_b.common_b_a.PermissionControler;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DHAppRootView.java */
/* loaded from: classes.dex */
public class DHAppRootView extends AdaContainerFrameItem implements ISysEventListener, IWebAppRootView {
    ICallBack a;
    DHFrameView b;
    boolean c;
    String d;
    IApp e;
    public boolean f;
    a g;
    boolean h;
    int i;
    RunnableC0005c j;
    protected byte k;
    protected String l;
    protected String m;
    private Stack<DHFrameView> n;
    private ArrayList<DHFrameView> o;
    private boolean p;
    private ArrayList<ICallBack> q;
    private d r;
    private e s;
    private DHImageView t;

    private void a(View view) {
    }

    public DHAppRootView(Context context, IApp iApp, DHFrameView dVar) {
        super(context);
        this.a = null;
        this.n = null;
        this.o = null;
        this.b = null;
        this.c = true;
        this.d = null;
        this.e = null;
        this.f = false;
        this.g = new a();
        this.h = false;
        this.i = 0;
        this.p = true;
        this.q = new ArrayList<>();
        this.j = null;
        this.r = new d();
        this.s = new e();
        this.t = null;
        this.h = BaseInfo.sRuntimeMode != null;
        this.e = iApp;
        this.d = iApp.obtainAppId();
        setMainView(new b(context, this));
        this.n = new Stack<>();
        this.o = new ArrayList<>();
        iApp.setWebAppRootView(this);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onPause);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onResume);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onDeviceNetChanged);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onNewIntent);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onConfigurationChanged);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onSimStateChanged);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyboardShow);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onWebAppBackground);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onWebAppForeground);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyboardHide);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onWebAppTrimMemory);
        if (PermissionControler.a(this.d, IFeature.F_DEVICE.toLowerCase())) {
            String bundleData = PlatformUtil.getBundleData(BaseInfo.PDR, "last_notify_net_type");
            String netWorkType = DeviceInfo.getNetWorkType();
            if (PdrUtil.isEquals(bundleData, netWorkType)) {
                return;
            }
            Logger.d("NetCheckReceiver", "netchange last_net_type:" + bundleData + ";cur_net_type:" + netWorkType);
            PlatformUtil.setBundleData(BaseInfo.PDR, "last_notify_net_type", netWorkType);
        }
    }

    private void a(DHFrameView dVar, int i, int i2) {
        Logger.d("DHAppRootView.pushFrameView" + dVar);
        this.n.insertElementAt(dVar, i);
        addFrameItem(dVar, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a(DHFrameView dVar) {
        return this.n.indexOf(dVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(final DHFrameView dVar) {
        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.b.b.c.1
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                if (DHAppRootView.this.n == null || dVar == null) {
                    return;
                }
                Logger.d("DHAppRootView.popFrameView frame" + dVar);
                DHAppRootView.this.n.remove(dVar);
                DHAppRootView.this.f(dVar);
            }
        }, null);
    }

    public DHFrameView a() {
        DHFrameView dVar = null;
        if (!this.n.isEmpty()) {
            for (int size = this.n.size() - 1; size >= 0; size--) {
                dVar = this.n.get(size);
                if (dVar.obtainMainView().getVisibility() == View.VISIBLE && !dVar.isChildOfFrameView) {
                    break;
                }
            }
        }
        return dVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Stack<DHFrameView> b() {
        return this.n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c(DHFrameView dVar) {
        Iterator<DHFrameView> it = this.o.iterator();
        while (it.hasNext()) {
            DHFrameView next = it.next();
            if (dVar != next && !next.isChildOfFrameView && next.obtainMainView().getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<DHFrameView> c() {
        return this.o;
    }

    public void d() {
        Logger.d(Logger.ANIMATION_TAG, "AppRootView dispatchConfigurationChanged(横竖屏切换、全屏非全屏切换、虚拟返回键栏隐藏显示) 引发调整栈窗口");
        ArrayList<DHFrameView> arrayList = new ArrayList<>();
        final ArrayList<DHFrameView> arrayList2 = new ArrayList<>();
        a(arrayList2, arrayList);
        Iterator<DHFrameView> it = arrayList2.iterator();
        while (it.hasNext()) {
            DHFrameView next = it.next();
            boolean contains = this.n.contains(next);
            next.j.processEvent(IMgr.MgrType.WindowMgr, 8, next);
            next.h = !contains;
        }
        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.b.b.c.2
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                try {
                    for (int size = DHAppRootView.this.n.size() - 1; size >= 0; size--) {
                        DHFrameView dVar = (DHFrameView) DHAppRootView.this.n.get(size);
                        if (!arrayList2.contains(dVar)) {
                            dVar.j.processEvent(IMgr.MgrType.WindowMgr, 22, dVar);
                            dVar.g = true;
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Logger.w("DHAppRootView onConfigurationChanged", e2);
                }
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(DHFrameView dVar) {
        int i;
        DHFrameView dVar2;
        byte b2 = dVar.getAnimOptions().mOption;
        byte b3 = 4;
        if ((!dVar.d && !dVar.f && dVar.obtainFrameOptions().hasTransparentValue()) || dVar.obtainMainView().getVisibility() != View.VISIBLE) {
            if (b2 == 3 || b2 == 1) {
                ArrayList<DHFrameView> arrayList = new ArrayList<>();
                b(arrayList, dVar);
                dVar.c = arrayList;
                return;
            } else {
                if (b2 == 2) {
                    return;
                }
                if (b2 == 4 || b2 == 0) {
                    ArrayList<DHFrameView> arrayList2 = new ArrayList<>();
                    a(arrayList2, dVar);
                    dVar.b = arrayList2;
                    return;
                }
            }
        }
        ArrayList<DHFrameView> arrayList3 = new ArrayList<>();
        ArrayList<DHFrameView> arrayList4 = new ArrayList<>();
        Region region = new Region();
        int size = this.o.size() - 1;
        while (size >= 0) {
            DHFrameView dVar3 = this.o.get(size);
            if (dVar3.obtainMainView().getVisibility() == View.VISIBLE) {
                if (!dVar3.isChildOfFrameView) {
                    dVar3.e();
                    ViewOptions obtainFrameOptions = dVar3.obtainFrameOptions();
                    if (b2 == b3 || b2 == 0) {
                        i = size;
                        if (a(region)) {
                            if (b(arrayList3, dVar3)) {
                                break;
                            }
                            size = i - 1;
                            b3 = 4;
                        } else {
                            if (obtainFrameOptions.hasTransparentValue() || !a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height)) {
                                a(arrayList4, dVar3);
                            } else if (b(arrayList3, dVar3)) {
                                break;
                            }
                            size = i - 1;
                            b3 = 4;
                        }
                    } else if (b2 == 2) {
                        ViewOptions obtainFrameOptions_Animate = dVar3.obtainFrameOptions_Animate();
                        if (dVar3 == dVar && obtainFrameOptions_Animate != null) {
                            obtainFrameOptions = obtainFrameOptions_Animate;
                        }
                        if (a(region)) {
                            if (b(arrayList3, dVar3)) {
                                break;
                            }
                        } else {
                            if (obtainFrameOptions.hasTransparentValue()) {
                                dVar2 = dVar3;
                                i = size;
                            } else {
                                dVar2 = dVar3;
                                i = size;
                                if (a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height)) {
                                    if (b(arrayList3, dVar2)) {
                                        break;
                                    }
                                    size = i - 1;
                                    b3 = 4;
                                }
                            }
                            a(arrayList4, dVar2);
                            size = i - 1;
                            b3 = 4;
                        }
                    } else {
                        i = size;
                        if (b2 == 3 || b2 == 1) {
                            if (dVar3 == dVar) {
                                b(arrayList3, dVar3);
                            } else if (a(region)) {
                                b(arrayList3, dVar3);
                            } else if (obtainFrameOptions.hasTransparentValue() || !a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height)) {
                                a(arrayList4, dVar3);
                            } else if (b(arrayList3, dVar3)) {
                                break;
                            }
                        }
                        size = i - 1;
                        b3 = 4;
                    }
                }
                i = size;
                size = i - 1;
                b3 = 4;
            } else {
                i = size;
                if (b(arrayList3, dVar3)) {
                    break;
                }
                size = i - 1;
                b3 = 4;
            }
        }
        dVar.c = arrayList3;
        dVar.b = arrayList4;
    }

    private void a(ArrayList<DHFrameView> arrayList, ArrayList<DHFrameView> arrayList2) {
        Region region = new Region();
        for (int size = this.o.size() - 1; size >= 0; size--) {
            DHFrameView dVar = this.o.get(size);
            if (dVar.obtainMainView().getVisibility() == View.VISIBLE) {
                ViewOptions obtainFrameOptions = dVar.obtainFrameOptions();
                if (dVar.a) {
                    a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height);
                    a(arrayList, dVar);
                } else if (dVar.isChildOfFrameView) {
                    continue;
                } else {
                    dVar.e();
                    if (a(region)) {
                        if (b(arrayList2, dVar)) {
                            return;
                        }
                    } else if (obtainFrameOptions.hasTransparentValue() || !a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height)) {
                        a(arrayList, dVar);
                    } else if (b(arrayList2, dVar)) {
                        return;
                    }
                }
            } else if (b(arrayList2, dVar)) {
                return;
            }
        }
    }

    boolean a(Region region) {
        return region.quickContains(0, 0, this.e.getInt(0), this.e.getInt(1));
    }

    void a(ArrayList<DHFrameView> arrayList, DHFrameView dVar) {
        arrayList.add(dVar);
    }

    boolean b(ArrayList<DHFrameView> arrayList, DHFrameView dVar) {
        arrayList.add(dVar);
        return false;
    }

    boolean a(Region region, int i, int i2, int i3, int i4) {
        int i5 = i + i3;
        int i6 = i2 + i4;
        boolean quickContains = region.quickContains(i, i2, i5, i6);
        if (!quickContains) {
            region.op(i, i2, i5, i6, Region.Op.UNION);
        }
        return quickContains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        Collections.sort(this.o, this.r);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int e(DHFrameView dVar) {
        int i;
        int i2;
        int i3;
        int indexOf = this.o.indexOf(dVar);
        int size = this.n.size();
        while (true) {
            size--;
            if (size < 0) {
                i2 = 0;
                break;
            }
            DHFrameView dVar2 = this.n.get(size);
            if (indexOf > this.o.indexOf(dVar2) && dVar2.getFrameType() != 3) {
                i2 = size + 1;
                break;
            }
        }
        if (i2 != 0) {
            ViewGroup obtainMainViewGroup = obtainMainViewGroup();
            int childCount = obtainMainViewGroup.getChildCount();
            i3 = i2;
            int i4 = 0;
            for (i = 0; i < childCount; i++) {
                View childAt = obtainMainViewGroup.getChildAt(i);
                if ((childAt instanceof ImageView) || ((childAt instanceof RelativeLayout) && childAt.getTag().equals("shade"))) {
                    i3++;
                } else {
                    i4++;
                }
                if (i4 >= i2) {
                    break;
                }
            }
        } else {
            i3 = i2;
        }
        a(dVar, i2, i3);
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        Collections.sort(this.n, this.r);
        int size = this.n.size();
        for (int i = 0; i < size; i++) {
            this.n.get(i).obtainMainView().bringToFront();
        }
    }

    void f(DHFrameView dVar) {
        Logger.d("DHAppRootView.closeFrameView pFrameView=" + dVar);
        dVar.onDispose();
        dVar.onDestroy();
        System.gc();
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public synchronized void dispose() {
        g();
        this.n = null;
        this.o = null;
        RunnableC0005c runnableC0005c = this.j;
        if (runnableC0005c != null) {
            runnableC0005c.b = false;
        }
        DHImageView eVar = this.t;
        if (eVar != null) {
            eVar.setImageBitmap(null);
            this.t = null;
        }
        super.dispose();
    }

    public void g() {
        Logger.d(this.d + " clearFrameView");
        if (BaseInfo.sAppStateMap.containsKey(this.d)) {
            DHFrameView a2 = a();
            View obtainMainView = a2 != null ? a2.obtainMainView() : null;
            if (obtainMainView != null) {
                try {
                    Bitmap captureView = PlatformUtil.captureView(obtainMainView);
                    if (captureView != null) {
                        PdrUtil.saveBitmapToFile(captureView, StringConst.STREAMAPP_KEY_ROOTPATH + "splash_temp/" + this.d + ".png");
                        if (captureView != null) {
                            captureView.recycle();
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        ArrayList<DHFrameView> arrayList = this.o;
        if (arrayList != null) {
            int size = arrayList.size();
            DHFrameView[] dVarArr = new DHFrameView[size];
            this.o.toArray(dVarArr);
            for (int i = 0; i < size; i++) {
                try {
                    dVarArr[i].onDestroy();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            this.o.clear();
        }
        clearView();
        Stack<DHFrameView> stack = this.n;
        if (stack != null) {
            stack.clear();
        }
    }

    /* compiled from: DHAppRootView.java */
    /* renamed from: io.dcloud.common.b.b.c$7, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[ISysEventListener.SysEventType.values().length];
            a = iArr;
            try {
                iArr[ISysEventListener.SysEventType.onPause.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[ISysEventListener.SysEventType.onResume.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[ISysEventListener.SysEventType.onSimStateChanged.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[ISysEventListener.SysEventType.onDeviceNetChanged.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[ISysEventListener.SysEventType.onNewIntent.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[ISysEventListener.SysEventType.onConfigurationChanged.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[ISysEventListener.SysEventType.onKeyboardShow.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[ISysEventListener.SysEventType.onKeyboardHide.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                a[ISysEventListener.SysEventType.onWebAppBackground.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[ISysEventListener.SysEventType.onWebAppForeground.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                a[ISysEventListener.SysEventType.onWebAppTrimMemory.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    @Override // io.dcloud.common.DHInterface.ISysEventListener
    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        String str = null;
        String str2 = "";
        switch (AnonymousClass7.a[sysEventType.ordinal()]) {
            case 1:
                str2 = String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_PAUSE);
                break;
            case 2:
                str2 = String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_RESUME);
                break;
            case 3:
                if (PermissionControler.a(this.d, IFeature.F_DEVICE.toLowerCase())) {
                    str2 = String.format("javascript:plus.device.imsi = ['%s'];", DeviceInfo.getUpdateIMSI()) + String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_IMSI_CHANGE);
                    break;
                }
                break;
            case 4:
                if (!PermissionControler.a(this.d, IFeature.F_DEVICE.toLowerCase())) {
                    return false;
                }
                String bundleData = PlatformUtil.getBundleData(BaseInfo.PDR, "last_notify_net_type");
                String netWorkType = DeviceInfo.getNetWorkType();
                if (PdrUtil.isEquals(bundleData, netWorkType)) {
                    return false;
                }
                Logger.d("NetCheckReceiver", "netchange last_net_type:" + bundleData + ";cur_net_type:" + netWorkType);
                PlatformUtil.setBundleData(BaseInfo.PDR, "last_notify_net_type", netWorkType);
                if (BaseInfo.isQihooLifeHelper(DCloudApplication.getInstance()) && DCloudApplication.self().isQihooTrafficFreeValidate && !BaseInfo.isWifi(DCloudApplication.getInstance())) {
                    InvokeExecutorHelper.TrafficProxy.setInstance(InvokeExecutorHelper.TrafficProxy.invoke("getInstance", new Class[0], new Object[0])).invoke("start");
                }
                str2 = String.format("javascript:plus.device.imsi = ['%s'];", DeviceInfo.getUpdateIMSI()) + String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_NETCHANGE);
                break;
            case 5:
                this.e.setRuntimeArgs(String.valueOf(obj));
                String launcherData = BaseInfo.getLauncherData(this.e.obtainAppId());
                String bundleData2 = SP.getBundleData("pdr", this.e.obtainAppId() + AbsoluteConst.LAUNCHTYPE);
                str2 = (AbsoluteConst.PROTOCOL_JAVASCRIPT + String.format(AbsoluteConst.JS_RUNTIME_ARGUMENTS, this.e.obtainRuntimeArgs()) + String.format(AbsoluteConst.JS_RUNTIME_LAUNCHER, launcherData) + String.format(AbsoluteConst.JS_RUNTIME_ORIGIN, TextUtils.isEmpty(bundleData2) ? "default" : bundleData2)) + String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_NEW_INTENT);
                break;
            case 6:
                this.e.updateScreenInfo(1);
                return false;
            case 7:
                str2 = String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_KEYBOARD_SHOW);
                break;
            case 8:
                str2 = String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_KEYBOARD_HIDE);
                break;
            case 9:
                str2 = String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, "background");
                break;
            case 10:
                String launcherData2 = BaseInfo.getLauncherData(this.e.obtainAppId());
                if (!this.e.obtainWebAppIntent().getBooleanExtra(IntentConst.IS_WEBAPP_REPLY, false)) {
                    this.e.setRuntimeArgs(String.valueOf(obj));
                    str2 = AbsoluteConst.PROTOCOL_JAVASCRIPT + String.format(AbsoluteConst.JS_RUNTIME_ARGUMENTS, this.e.obtainRuntimeArgs()) + String.format(AbsoluteConst.JS_RUNTIME_LAUNCHER, launcherData2);
                    str = launcherData2;
                }
                str2 = str2 + String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE_PARAMETER, AbsoluteConst.EVENTS_WEBAPP_FOREGROUND, str);
                break;
            case 11:
                str2 = String.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_WEBAPP_TRIMMEMORY);
                break;
        }
        if (this.o != null && !TextUtils.isEmpty(str2)) {
            for (int size = this.o.size() - 1; size >= 0; size--) {
                IWebview obtainWebView = this.o.get(size).obtainWebView();
                if (obtainWebView != null) {
                    obtainWebView.loadUrl(str2);
//                    obtainWebView.loadUrl("file:///android_asset/apps/H5057CD3A/www/login.html");
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(boolean z) {
        for (int size = this.o.size() - 1; size >= 0; size--) {
            IWebview obtainWebView = this.o.get(size).obtainWebView();
            if (obtainWebView != null) {
                obtainWebView.reload();
            }
            if (!z) {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(String str) {
        String[] split = str.split("\\|");
        for (int size = this.o.size() - 1; size >= 0; size--) {
            IWebview obtainWebView = this.o.get(size).obtainWebView();
            if (obtainWebView != null) {
                int i = 0;
                while (true) {
                    if (i >= split.length) {
                        break;
                    }
                    if (obtainWebView.obtainUrl().startsWith(split[i])) {
                        obtainWebView.reload();
                        break;
                    }
                    i++;
                }
            }
        }
    }

    void a(final IApp iApp) {
        if (Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_WAITING))) {
            obtainMainView().postDelayed(new Runnable() { // from class: io.dcloud.common.b.b.c.3
                @Override // java.lang.Runnable
                public void run() {
                    IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(iApp.getActivity());
                    if (iActivityHandler != null) {
                        iActivityHandler.showSplashWaiting();
                    }
                }
            }, 100L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(final DHAppRootView cVar, final DHFrameView dVar, int i, final boolean z) {
        if (dVar != null) {
            Logger.d("approotview", "closeSplashScreen0 delay=" + i + ";autoClose=" + z);
            View obtainMainView = dVar.obtainMainView();
            if (obtainMainView != null) {
                obtainMainView.postDelayed(new Runnable() { // from class: io.dcloud.common.b.b.c.4
                    @Override // java.lang.Runnable
                    public void run() {
                        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.b.b.c.4.1
                            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                            public void execute(Object obj) {
                                Logger.d("approotview", "closeSplashScreen1;autoClose=" + z);
                                DHAppRootView.this.a(cVar, z);
                            }
                        }, dVar);
                    }
                }, Math.max(i, 150));
            } else {
                Logger.d("approotview", "closeSplashScreen2;autoClose");
                a(cVar, z);
            }
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebAppRootView
    public boolean didCloseSplash() {
        return this.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(DHAppRootView cVar, boolean z) {
        Logger.d(Logger.MAIN_TAG, "closeSplashScreen0 appid=" + this.d + ";" + z + ";closeSplashDid=" + this.h);
        if (cVar != null && !this.h) {
            if (!this.e.isStreamApp()) {
                BaseInfo.run5appEndTime = TestUtil.getUseTime(AbsoluteConst.RUN_5APP_TIME_KEY, "");
                TestUtil.delete(AbsoluteConst.RUN_5APP_TIME_KEY);
            }
            Activity activity = getActivity();
            IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
            if (iActivityHandler != null) {
                iActivityHandler.closeAppStreamSplash(this.d);
                BaseInfo.setLoadingLaunchePage(false, "closeSplashScreen0");
            }
            IOnCreateSplashView onCreateSplashView = this.e.getOnCreateSplashView();
            if (onCreateSplashView != null) {
                onCreateSplashView.onCloseSplash();
            }
            if (BaseInfo.useStreamAppStatistic(activity)) {
                Logger.d(Logger.MAIN_TAG, "closeSplashScreen0 will commit s=1");
                TestUtil.PointTime pointTime = TestUtil.PointTime.getPointTime(TestUtil.STREAM_APP_POINT);
                if (pointTime == null && BaseInfo.isWap2AppAppid(this.d)) {
                    pointTime = TestUtil.PointTime.createPointTime(TestUtil.STREAM_APP_POINT, 7);
                }
                if (pointTime != null) {
                    if (!TestUtil.PointTime.hasStreamAppStatus(activity, this.d, TestUtil.PointTime.STATUS_INSTALLED)) {
                        pointTime.point(6);
                        Logger.i("download_manager", "closeSplashScreen0 " + this.d + " pointTime = " + pointTime.getPointsString());
                        TestUtil.PointTime.commit(iActivityHandler, this.d, 1, 0, "&v=" + PdrUtil.encodeURL(this.e.obtainAppVersionName()) + "&ac=" + (z ? 1 : 3) + "&sf=" + StringConst.getIntSF(BaseInfo.getCmitInfo(this.d).plusLauncher) + "&sfd=" + BaseInfo.getCmitInfo(this.d).sfd);
                    }
                } else {
                    String data = TestUtil.PointTime.getData(activity, this.d, TestUtil.PointTime.DATA_IN_APP_COMMIT_DATA);
                    if (!TextUtils.isEmpty(data)) {
                        TestUtil.PointTime.commit(iActivityHandler, this.d, 1, 0, data + "&v=" + PdrUtil.encodeURL(this.e.obtainAppVersionName()) + "&ac=" + (z ? 2 : 4) + "&sf=" + StringConst.getIntSF(BaseInfo.getCmitInfo(this.d).plusLauncher) + "&sfd=" + BaseInfo.getCmitInfo(this.d).sfd);
                    }
                }
                if (TestUtil.PointTime.hasPointTime(TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED)) {
                    TestUtil.PointTime.getPointTime(TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED).point(1, System.currentTimeMillis());
                }
                if (TestUtil.PointTime.hasStreamAppStatus(activity, this.d, TestUtil.PointTime.STATUS_DOWNLOAD_COMPLETED)) {
                    String data2 = TestUtil.PointTime.getData(activity, this.d, TestUtil.PointTime.DATA_START_TIMES);
                    if (!TextUtils.isEmpty(data2)) {
                        String str = this.d;
                        TestUtil.PointTime.commit(iActivityHandler, this.d, TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED, TestUtil.PointTime.DATA_START_TIMES, 5, "&f=" + data2, TestUtil.PointTime.getData(activity, str, TestUtil.PointTime.getData(activity, str, TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED)));
                    }
                }
                String data3 = TestUtil.PointTime.getData(activity, this.d, TestUtil.PointTime.DATA_CACHE_PAGES);
                if (!TextUtils.isEmpty(data3)) {
                    TestUtil.PointTime.commit(iActivityHandler, this.d, TestUtil.PointTime.DATA_CACHE_PAGES, null, 6, null, data3);
                }
                String bundleData = PlatformUtil.getBundleData("pdr", AbsoluteConst.INSTALL_APK);
                if (!TextUtils.isEmpty(bundleData)) {
                    try {
                        JSONObject jSONObject = new JSONObject(bundleData);
                        String string = jSONObject.getString("apkPath");
                        String string2 = jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE);
                        PlatformUtil.removeBundleData("pdr", AbsoluteConst.INSTALL_APK);
                        DialogUtil.showInstallAPKDialog(activity, string2, string);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        this.h = true;
    }

    @Override // io.dcloud.common.DHInterface.IWebAppRootView
    public void onAppStart(IApp iApp) {
        this.h = false;
        if (iApp != null) {
            a(iApp);
        }
        obtainMainView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: io.dcloud.common.b.b.c.5
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                View obtainMainView = DHAppRootView.this.obtainMainView();
                DHAppRootView.this.onRootViewGlobalLayout(obtainMainView);
                if (obtainMainView == null || DeviceInfo.sDeviceSdkVer < 16 || obtainMainView.getViewTreeObserver() == null) {
                    return;
                }
                obtainMainView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                obtainMainView.getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
        });
        obtainMainView().setBackgroundColor(-1);
        onAppActive(iApp);
    }

    @Override // io.dcloud.common.DHInterface.IWebAppRootView
    public void onRootViewGlobalLayout(View view) {
        if (isDisposed()) {
            return;
        }
        if (AdaWebview.ScreemOrientationChangedNeedLayout) {
            AdaWebview.ScreemOrientationChangedNeedLayout = false;
            this.e.updateScreenInfo(3);
        }
        int width = view.getWidth();
        int height = view.getHeight() - this.e.getInt(1);
        if (!this.e.isVerticalScreen()) {
            height = width - this.e.getInt(0);
        }
        if (Math.abs(height) > view.getResources().getDisplayMetrics().heightPixels / 3) {
            if (height < 0) {
                AndroidResources.sIMEAlive = true;
            } else {
                if (AndroidResources.sIMEAlive && this.q.size() > 0) {
                    MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.b.b.c.6
                        @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                        public void execute(Object obj) {
                            Iterator it = DHAppRootView.this.q.iterator();
                            while (it.hasNext()) {
                                ((ICallBack) it.next()).onCallBack(-1, null);
                            }
                            DHAppRootView.this.q.clear();
                        }
                    }, 500L, null);
                }
                AndroidResources.sIMEAlive = false;
            }
            ISysEventListener.SysEventType sysEventType = ISysEventListener.SysEventType.onKeyboardShow;
            if (!AndroidResources.sIMEAlive) {
                ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onKeyboardHide;
            }
        }
        if (height != 0) {
            this.e.updateScreenInfo(3);
        }
        if (view.getHeight() != this.i && view.getHeight() == this.e.getInt(1)) {
            PlatformUtil.RESET_H_W();
            if (!this.p) {
                BaseInfo.sFullScreenChanged = true;
            }
            this.p = false;
        }
        this.i = view.getHeight();
    }

    public Object a(View view, ICallBack iCallBack) {
        if (!AndroidResources.sIMEAlive) {
            return iCallBack.onCallBack(-1, null);
        }
        DeviceInfo.hideIME(obtainMainView());
        this.q.add(iCallBack);
        return null;
    }

    private void b(IApp iApp) {
        iApp.setFullScreen(PdrUtil.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_FULLSCREEN), false, false));
    }

    @Override // io.dcloud.common.DHInterface.IWebAppRootView
    public void onAppActive(IApp iApp) {
        boolean z;
        b(iApp);
        Activity activity = iApp.getActivity();
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
        if (iActivityHandler != null) {
            PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "makeTitleViewDefault", null, new Class[]{Activity.class}, new Object[]{activity});
            PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "UpdateTitle", null, new Class[]{Activity.class, IApp.class}, new Object[]{activity, iApp});
            PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "resumeTitleView", null, new Class[]{Activity.class, IApp.class}, new Object[]{activity, iApp});
            iActivityHandler.setViewAsContentView(obtainMainView());
            z = iActivityHandler.isStreamAppMode();
        } else {
            z = false;
        }
        Logger.d(Logger.MAIN_TAG, iApp.obtainAppId() + " onAppActive setContentView");
        a(obtainMainView());
        if (z) {
            if (!iApp.isJustDownload() || BaseInfo.isWap2AppAppid(this.d)) {
                iApp.startSmartUpdate();
            }
            if (!InvokeExecutorHelper.StorageUtils.invoke("checkDirResourceComplete", InvokeExecutorHelper.AppidUtils.invoke("getAppFilePathByAppid", iApp.obtainAppId()), false)) {
                IActivityHandler iActivityHandler2 = DCloudAdapterUtil.getIActivityHandler(activity);
                if (!BaseInfo.isWap2AppAppid(iApp.obtainAppId()) && iActivityHandler2 != null) {
                    iActivityHandler2.resumeAppStreamTask(this.d);
                }
            }
        }
        FeatureMessageDispatcher.dispatchMessage("app_open", 1);
    }

    @Override // io.dcloud.common.DHInterface.IWebAppRootView
    public void onAppUnActive(IApp iApp) {
        RunnableC0005c runnableC0005c = this.j;
        if (runnableC0005c != null) {
            runnableC0005c.a();
            this.j = null;
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebAppRootView
    public void onAppStop(IApp iApp) {
        onAppUnActive(iApp);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DHAppRootView.java */
    /* renamed from: io.dcloud.common.b.b.c$c, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class RunnableC0005c implements Runnable {
        View a;
        boolean b;
//        final AdaContainerFrameImpl c;

        @Override
        public void run() {
            System.gc();

            if (b) {
                Logger.d(Logger.AutoGC_TAG, "================");
                Logger.d(Logger.AutoGC_TAG, "AutoGCRunnable freeMemory=" + Runtime.getRuntime().freeMemory());

                int arrayListSize = (o != null) ? o.size() : 0;
                int stackSize = (n != null) ? n.size() : 0;

                Logger.d(Logger.AutoGC_TAG, "Total of ArrayList is " + arrayListSize);
                Logger.d(Logger.AutoGC_TAG, "Size of Stack is " + stackSize);
                Logger.d(Logger.AutoGC_TAG, "================");

                if (a != null) {
                    a.postDelayed(this, 60000L);
                }
            }
        }

        public void a() {
            this.b = false;
        }
    }


    /* compiled from: DHAppRootView.java */
    /* loaded from: classes.dex */
    class d implements Comparator<DHFrameView> {
        d() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(DHFrameView dVar, DHFrameView dVar2) {
            if (dVar.getFrameType() == 3) {
                return 1;
            }
            if (dVar2.getFrameType() == 3) {
                return -1;
            }
            int i = dVar.mZIndex - dVar2.mZIndex;
            if (i == 0) {
                return dVar.lastShowTime <= dVar2.lastShowTime ? -1 : 1;
            }
            return i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(DHFrameView dVar) {
        ArrayList<AdaFrameItem> arrayList = dVar.getParentFrameItem().mChildArrayList;
        if (arrayList.size() > 1) {
            Collections.sort(arrayList, this.s);
            ArrayList arrayList2 = new ArrayList();
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                AdaFrameItem next = it.next();
                if (next.obtainMainView() != null) {
                    next.obtainMainView().bringToFront();
                } else {
                    arrayList2.add(next);
                }
            }
            if (arrayList2.size() > 0) {
                arrayList.removeAll(arrayList2);
            }
        }
        ArrayList<INativeView> childNativeViewList = dVar.getParentFrameItem().getChildNativeViewList();
        if (childNativeViewList != null) {
            Iterator<INativeView> it2 = childNativeViewList.iterator();
            while (it2.hasNext()) {
                INativeView next2 = it2.next();
                if (next2.obtanMainView() != null) {
                    next2.obtanMainView().bringToFront();
                }
            }
        }
    }

    /* compiled from: DHAppRootView.java */
    /* loaded from: classes.dex */
    class e implements Comparator<AdaFrameItem> {
        e() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(AdaFrameItem adaFrameItem, AdaFrameItem adaFrameItem2) {
            boolean z = adaFrameItem instanceof IFrameView;
            if (z && ((IFrameView) adaFrameItem).getFrameType() == 3) {
                return 1;
            }
            if (z && ((IFrameView) adaFrameItem).getFrameType() == 3) {
                return -1;
            }
            int i = adaFrameItem.mZIndex - adaFrameItem2.mZIndex;
            if (i == 0) {
                return adaFrameItem.lastShowTime <= adaFrameItem2.lastShowTime ? -1 : 1;
            }
            return i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DHAppRootView.java */
    /* loaded from: classes.dex */
    public class b extends k {
        Paint a;
        int b;
        int c;
        int d;
        int e;
        a f;

        public b(Context context, DHAppRootView cVar) {
            super(context, cVar);
            this.a = new Paint();
            this.f = new a();
            this.a.setColor(-13421773);
            this.a.setTextSize((int) (DeviceInfo.DEFAULT_FONT_SIZE * DeviceInfo.sDensity * 1.2d));
            setTag("AppRootView");
            this.b = (int) this.a.measureText("缓冲中 ");
            this.c = (int) this.a.measureText("...");
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.d = ((DHAppRootView.this.e.getInt(0) - this.b) - this.c) / 2;
            this.e = (int) (DHAppRootView.this.e.getInt(2) * 0.8d);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (DHAppRootView.this.e.getMaskLayerCount() > 0) {
                if (!this.f.b) {
                    this.f.b = true;
                    this.f.run();
                }
                canvas.drawColor(-2013265920);
                canvas.drawText("缓冲中 ", this.d, this.e, this.a);
                if (this.f.a == 1) {
                    canvas.drawText(".", this.d + this.b, this.e, this.a);
                    return;
                } else if (this.f.a == 2) {
                    canvas.drawText("..", this.d + this.b, this.e, this.a);
                    return;
                } else {
                    if (this.f.a == 3) {
                        canvas.drawText("...", this.d + this.b, this.e, this.a);
                        return;
                    }
                    return;
                }
            }
            this.f.b = false;
        }

        /* compiled from: DHAppRootView.java */
        /* loaded from: classes.dex */
        class a implements Runnable {
            int a = 0;
            boolean b = false;

            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (this.b) {
                    this.a++;
                    b.this.invalidate();
                    this.a %= 4;
                    b.this.postDelayed(this, 500L);
                    return;
                }
                this.a = 0;
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (DHAppRootView.this.e.getMaskLayerCount() > 0) {
                return true;
            }
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.View
        public void dispatchConfigurationChanged(Configuration configuration) {
            super.dispatchConfigurationChanged(configuration);
            if (BaseInfo.sDoingAnimation) {
                DHAppRootView.this.a = new ICallBack() { // from class: io.dcloud.common.b.b.c.b.1
                    @Override // io.dcloud.common.DHInterface.ICallBack
                    public Object onCallBack(int i, Object obj) {
                        b.this.j.d();
                        DHAppRootView.this.a = null;
                        return null;
                    }
                };
            } else {
                this.j.d();
            }
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            DHAppRootView.this.mViewOptions.onScreenChanged();
            PlatformUtil.RESET_H_W();
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            DHAppRootView.this.mViewOptions.onScreenChanged(i, i2);
            post(new Runnable() { // from class: io.dcloud.common.b.b.c.b.2
                @Override // java.lang.Runnable
                public void run() {
                    if (DHAppRootView.this.o != null) {
                        Iterator it = DHAppRootView.this.o.iterator();
                        while (it.hasNext()) {
                            DHFrameView dVar = (DHFrameView) it.next();
                            if (!dVar.isChildOfFrameView) {
                                dVar.resize();
                            }
                        }
                    }
                }
            });
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DHAppRootView.java */
    /* loaded from: classes.dex */
    public class a {
        int a = 0;
        Vector<DHFrameView> b = new Vector<>();
        private boolean d = false;

        a() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void a(DHFrameView dVar) {
            this.b.add(dVar);
            int i = this.a + 1;
            this.a = i;
            if (i > 1) {
                this.d = true;
            } else {
                this.a = 1;
                this.d = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void b(DHFrameView dVar) {
            this.b.remove(dVar);
            this.a--;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int a() {
            return this.a;
        }
    }

    @Override // io.dcloud.common.DHInterface.IWebAppRootView
    public IFrameView findFrameViewB(IFrameView iFrameView) {
        if (!this.o.contains(iFrameView)) {
            return null;
        }
        ArrayList<DHFrameView> arrayList = new ArrayList<>();
        a(iFrameView, arrayList);
        if (arrayList.size() > 1) {
            return null;
        }
        int indexOf = this.o.indexOf(iFrameView);
        if (this.o != null) {
            for (int i = indexOf - 1; i >= 0; i--) {
                DHFrameView dVar = this.o.get(i);
                if (!dVar.isChildOfFrameView && dVar.obtainMainView().getVisibility() == View.VISIBLE) {
                    return dVar;
                }
            }
        }
        return null;
    }

    public void a(IFrameView iFrameView, ArrayList<DHFrameView> arrayList) {
        if (this.o.contains(iFrameView)) {
            int indexOf = this.o.indexOf(iFrameView);
            if (this.o != null) {
                com.dcloud.android.graphics.Region region = new com.dcloud.android.graphics.Region(1);
                for (int i = indexOf - 1; i >= 0; i--) {
                    DHFrameView dVar = this.o.get(i);
                    if (!dVar.isChildOfFrameView && dVar.obtainMainView().getVisibility() == View.VISIBLE) {
                        arrayList.add(dVar);
                        ViewOptions obtainFrameOptions = dVar.obtainFrameOptions();
                        a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height);
                    }
                    if (a(region)) {
                        return;
                    }
                }
            }
        }
    }

    public void a(DHFrameView dVar, DHFrameView dVar2) {
        AnimOptions animOptions = dVar.getAnimOptions();
        this.k = animOptions.mOption;
        animOptions.mOption = dVar2.getAnimOptions().mOption;
        this.l = animOptions.mAnimType;
        animOptions.mAnimType = dVar2.getAnimOptions().mAnimType;
        this.m = animOptions.mAnimType_close;
        animOptions.mAnimType_close = dVar2.getAnimOptions().mAnimType_close;
    }

    public void h(DHFrameView dVar) {
        AnimOptions animOptions = dVar.getAnimOptions();
        animOptions.mOption = this.k;
        animOptions.mAnimType = this.l;
        animOptions.mAnimType_close = this.m;
    }

    public DHImageView a(DHFrameView dVar, int i, boolean z) {
        Bitmap captureView;
        boolean z2;
        DHImageView eVar;
        long currentTimeMillis = System.currentTimeMillis();
        b bVar = (b) obtainMainView();
        if (a((ViewGroup) bVar)) {
            DHImageView eVar2 = this.t;
            if (eVar2 != null) {
                eVar2.setImageBitmap(null);
                this.t = null;
            }
            return null;
        }
        if (dVar.mSnapshot != null) {
            captureView = dVar.mSnapshot;
            z2 = false;
        } else {
            if (1 == i && (eVar = this.t) != null && eVar.b() != null && this.t.getTag() != null && dVar.hashCode() == ((Integer) this.t.getTag()).intValue()) {
                if (this.t.getParent() != bVar) {
                    if (this.t.getParent() != null) {
                        ((ViewGroup) this.t.getParent()).removeView(this.t);
                    }
                    bVar.addView(this.t);
                }
                this.t.bringToFront();
                this.t.setVisibility(View.VISIBLE);
                return this.t;
            }
            captureView = PlatformUtil.captureView(dVar.obtainMainView());
            z2 = true;
        }
        if (captureView != null && !PlatformUtil.isWhiteBitmap(captureView)) {
            if (this.t == null) {
                this.t = bVar.b();
            }
            if (this.t.getParent() != bVar) {
                if (this.t.getParent() != null) {
                    ((ViewGroup) this.t.getParent()).removeView(this.t);
                }
                bVar.addView(this.t);
            }
            this.t.bringToFront();
            this.t.setImageBitmap(captureView);
            this.t.setVisibility(View.VISIBLE);
        } else {
            DHImageView eVar3 = this.t;
            if (eVar3 != null) {
                eVar3.setImageBitmap(null);
                this.t = null;
            }
        }
        if (this.t.a()) {
            return null;
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        Logger.i("mabo", "==============B截图耗时=" + currentTimeMillis2);
        if (currentTimeMillis2 >= BaseInfo.sTimeoutCapture) {
            BaseInfo.sTimeOutCount++;
            if (BaseInfo.sTimeOutCount > BaseInfo.sTimeOutMax) {
                BaseInfo.sAnimationCaptureB = false;
            }
        } else if (z2) {
            BaseInfo.sTimeOutCount = 0;
        }
        this.t.c();
        return this.t;
    }

    public boolean a(ViewGroup viewGroup) {
        DHImageView eVar = this.t;
        return (eVar == null || eVar.c <= 0 || ((long) viewGroup.getHeight()) == this.t.c) ? false : true;
    }
}
