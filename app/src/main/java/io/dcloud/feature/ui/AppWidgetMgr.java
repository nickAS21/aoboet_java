package io.dcloud.feature.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.nostra13.dcloudimageloader.core.ImageLoader;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;

import org.json.JSONArray;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ShortCutUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.feature.internal.reflect.BroadcastReceiver;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* compiled from: AppWidgetMgr.java */
/* loaded from: classes.dex  old a*/
public class AppWidgetMgr implements ISysEventListener {
    AbsMgr absMgr;
    IApp iApp;
    IActivityHandler iActivityHandler;
    String j;
    b p;
    private NWindow s;
    private String t;
    ArrayList<NWindow> a = null;
    ArrayList<NWindow> b = new ArrayList<>(1);
    HashMap<String, NView> d = new HashMap<>();
    InvokeExecutorHelper.InvokeExecutor appStreamUtils = InvokeExecutorHelper.AppStreamUtils;
    InvokeExecutorHelper.InvokeExecutor appidUtils = InvokeExecutorHelper.AppidUtils;
    InvokeExecutorHelper.InvokeExecutor storageUtils = InvokeExecutorHelper.StorageUtils;
    boolean k = false;
    boolean l = false;
    private List<String> u = new ArrayList();
    ArrayList<NWindow> n = null;
    ISysEventListener o = new ISysEventListener() { // from class: io.dcloud.feature.ui.a.2
        @Override // io.dcloud.common.DHInterface.ISysEventListener
        public boolean onExecute(SysEventType sysEventType, Object obj) {
            Object[] objArr = (Object[]) obj;
            if (sysEventType == SysEventType.onKeyUp) {
                int intValue = ((Integer) objArr[0]).intValue();
                if (sysEventType == SysEventType.onKeyUp && intValue == 4 && AppWidgetMgr.this.iApp.getMaskLayerCount() > 0) {
                    AppWidgetMgr.this.b(1);
                    AppWidgetMgr.this.iApp.clearMaskLayerCount();
                    AppWidgetMgr.this.iApp.unregisterSysEventListener(AppWidgetMgr.this.o, SysEventType.onKeyUp);
                    if (AppWidgetMgr.this.n != null) {
                        Iterator<NWindow> it = AppWidgetMgr.this.n.iterator();
                        while (it.hasNext()) {
                            it.next().y = false;
                        }
                        AppWidgetMgr.this.n.clear();
                    }
                    AppWidgetMgr.this.iApp.obtainWebAppRootView().obtainMainView().invalidate();
                    return true;
                }
            }
            return false;
        }
    };
    private C0008a v = new C0008a();
    StringBuffer q = new StringBuffer();
    HashMap<String, Integer> r = new HashMap<>();
    SharedPreferences m = DCloudApplication.getInstance().getApplicationContext().getSharedPreferences("pdr", 0);

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppWidgetMgr(AbsMgr absMgr, IApp iApp) {
        this.iApp = null;
        this.iActivityHandler = null;
        this.j = null;
        this.p = null;
        this.absMgr = absMgr;
        this.iApp = iApp;
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(iApp.getActivity());
        if (iActivityHandler != null) {
            this.iActivityHandler = iActivityHandler;
        }
        iApp.registerSysEventListener(this, SysEventType.onKeyUp);
        iApp.registerSysEventListener(this, SysEventType.onKeyDown);
        iApp.registerSysEventListener(this, SysEventType.onKeyLongPress);
        if (!iApp.isStreamApp() || PdrUtil.isEquals(BaseInfo.sDefaultBootApp, this.iApp.obtainAppId()) || this.iActivityHandler == null) {
            return;
        }
        boolean b2 = b(this.iApp.obtainAppId());
        Logger.d("AppWidgetMgr" + this.iApp.obtainAppId() + "isStreamApp=" + b2);
        BaseInfo.setLoadingLaunchePage(b2, "AppWidgetMgr");
        this.j = this.iApp.getActivity().getPackageName() + this.appStreamUtils.getString("CONTRACT_BROADCAST_ACTION");
        b bVar = new b(this.iActivityHandler);
        this.p = bVar;
        bVar.a();
        if (!b2) {
            b(iApp);
        }
        Logger.d("AppWidgetMgr isStreamApp=" + b2);
        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.feature.ui.a.1
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                AppWidgetMgr.this.a((IApp) obj);
            }
        }, iApp);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(String str, NView bVar) {
        this.d.put(str, bVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NView a(String str) {
        return this.d.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.b.clear();
        if (this.iActivityHandler == null || this.p == null) {
            return;
        }
        Logger.d(Logger.StreamApp_TAG, "AppWidgetMgr.clearData");
        this.iActivityHandler.unregisterReceiver(this.p);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(String str) {
        boolean invoke;
        if (BaseInfo.isWap2AppAppid(str)) {
            invoke = BaseInfo.isWap2AppCompleted(str);
        } else {
            invoke = this.storageUtils.invoke("checkDirResourceComplete", this.appidUtils.invoke("getAppFilePathByAppid", str), false);
        }
        return !invoke;
    }

    public void a(IFrameView iFrameView) {
        this.absMgr.processEvent(IMgr.MgrType.WindowMgr, 8, iFrameView);
    }

    public void b(IFrameView iFrameView) {
        this.absMgr.processEvent(IMgr.MgrType.WindowMgr, 22, iFrameView);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a(NWindow cVar) {
        int size = this.a.size();
        for (int i = size - 1; i >= 0; i--) {
            if (this.a.get(i).A <= cVar.A) {
                return i + 1;
            }
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(String str, NWindow cVar, int i) {
        if (this.a == null) {
            this.a = new ArrayList<>(1);
        }
        if (!this.a.contains(cVar)) {
            this.a.add(i, cVar);
        }
        Collections.sort(this.b, this.v);
        Collections.sort(this.a, this.v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(NWindow cVar) {
        Logger.d(Logger.MAP_TAG, "sortNWindowByZIndex beign");
        Collections.sort(this.b, this.v);
        Collections.sort(this.a, this.v);
        this.absMgr.processEvent(IMgr.MgrType.WindowMgr, 26, cVar.u.obtainWebAppRootView());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(NWindow cVar) {
        if (this.b.contains(cVar)) {
            return;
        }
        this.b.add(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NWindow b() {
        return a(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NWindow a(int i) {
        int size = this.b.size();
        for (int i2 = 0; i2 < size; i2++) {
            NWindow cVar = this.b.get(i2);
            if (cVar.u.getFrameType() == i) {
                return cVar;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NWindow c() {
        return a(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NWindow c(IFrameView iFrameView) {
        int size = this.b.size();
        for (int i = 0; i < size; i++) {
            NWindow cVar = this.b.get(i);
            if (cVar.u.equals(iFrameView)) {
                return cVar;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized NWindow a(String str, String str2, String str3) {
        int size = this.b.size();
        for (int i = 0; i < size; i++) {
            NWindow cVar = this.b.get(i);
            String valueOf = String.valueOf(cVar.u.hashCode());
            String l = cVar.l();
            if (PdrUtil.isEquals(str, valueOf) || PdrUtil.isEquals(str2, cVar.e) || PdrUtil.isEquals(str3, l)) {
                return cVar;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String d() {
        StringBuffer stringBuffer = new StringBuffer("[");
        Iterator<NWindow> it = this.b.iterator();
        boolean z = false;
        while (it.hasNext()) {
            stringBuffer.append(it.next().d()).append(JSUtil.COMMA);
            z = true;
        }
        if (z) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String e() {
        StringBuffer stringBuffer = new StringBuffer("[");
        Iterator<NWindow> it = this.b.iterator();
        boolean z = false;
        while (it.hasNext()) {
            NWindow next = it.next();
            if (next.h()) {
                stringBuffer.append(next.d()).append(JSUtil.COMMA);
                z = true;
            }
        }
        if (z) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(NWindow cVar) {
        this.a.remove(cVar);
        this.b.remove(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(NWindow cVar) {
        if (cVar.y) {
            return;
        }
        Logger.d("AppWidgetMgr.showMaskLayer " + cVar.v);
        if (this.n == null) {
            this.n = new ArrayList<>();
        }
        if (this.iApp.getMaskLayerCount() == 0) {
            this.iApp.registerSysEventListener(this.o, SysEventType.onKeyUp);
        }
        cVar.y = true;
        this.n.add(cVar);
        this.absMgr.processEvent(IMgr.MgrType.WindowMgr, 29, cVar.u);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(NWindow cVar) {
        if (cVar == null) {
            return;
        }
        Logger.d("AppWidgetMgr.hideMaskLayer " + cVar.v);
        this.absMgr.processEvent(IMgr.MgrType.WindowMgr, 30, cVar.u);
        if (this.iApp.getMaskLayerCount() == 0) {
            this.iApp.unregisterSysEventListener(this.o, SysEventType.onKeyUp);
        }
        cVar.y = false;
        ArrayList<NWindow> arrayList = this.n;
        if (arrayList != null) {
            arrayList.remove(cVar);
        }
    }

    private boolean a(String str, SysEventType sysEventType, String str2, int i, boolean z) {
        String format = String.format("{keyType:'%s',keyCode:%d}", str2, Integer.valueOf(i));
        ArrayList<NWindow> arrayList = this.a;
        if (arrayList == null) {
            return false;
        }
        int size = arrayList.size();
        Logger.d("AppWidgetMgr", "syncExecBaseEvent windowCount = " + size);
        for (int i2 = size - 1; i2 >= 0; i2--) {
            NWindow cVar = this.a.get(i2);
            if (cVar != null && cVar.a == null && cVar.B && (cVar.b(str, format, z) || (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT && str2 != null && (cVar.c(str2) || (sysEventType == SysEventType.onKeyDown && ((cVar.c("back") && i == 4) || ((cVar.c(AbsoluteConst.EVENTS_MENU) && i == 82) || ((cVar.c(AbsoluteConst.EVENTS_VOLUME_DOWN) && i == 25) || ((cVar.c(AbsoluteConst.EVENTS_VOLUME_UP) && i == 24) || (cVar.c(AbsoluteConst.EVENTS_SEARCH) && i == 84)))))))))) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0111  */
    @Override // io.dcloud.common.DHInterface.ISysEventListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onExecute(SysEventType r10, Object r11) {
        /*
            Method dump skipped, instructions count: 378
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.a.onExecute(io.dcloud.common.DHInterface.ISysEventListener$SysEventType, java.lang.Object):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean g(NWindow cVar) {
        if (!b(this.iApp.obtainAppId())) {
            cVar.z = false;
        }
        if (cVar.z && c(cVar.v)) {
            cVar.k().loadUrl(cVar.v);
            cVar.z = false;
        }
        return cVar.z;
    }

    public boolean c(String str) {
        boolean queryUrl = false;
        boolean z = true;
        if (this.iActivityHandler != null && str != null) {
            if (!PdrUtil.isNetPath(str.toLowerCase(Locale.getDefault())) && b(this.iApp.obtainAppId())) {
                if (BaseInfo.isWap2AppAppid(this.iApp.obtainAppId())) {
                    if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        str = str.substring(7);
                    }
                    str = PdrUtil.stripQuery(PdrUtil.stripAnchor(str));
                    if (!new File(str).exists()) {
                        queryUrl = false;
                    }
                } else {
                    queryUrl = this.iActivityHandler.queryUrl(str, this.iApp.obtainAppId());
                }
                z = queryUrl;
            }
            Logger.d("hasFile = " + z + ";filePath=" + str);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(String str) {
        if (!BaseInfo.isWap2AppAppid(this.iApp.obtainAppId()) && b(this.iApp.obtainAppId())) {
            return this.appStreamUtils.invoke("checkFilepathValidity", str, false);
        }
        return true;
    }

    private boolean e(String str) {
        return new File(str).exists();
    }

    public void a(IApp iApp, String str, Bitmap bitmap) {
        if (DCloudAdapterUtil.isAutoCreateShortCut()) {
            ShortCutUtil.createShortcut(iApp, str, bitmap, true);
        }
    }

    public void a(final IApp iApp) {
        ImageLoader.getInstance().loadImage(DataInterface.getIconImageUrl(iApp.obtainAppId(), this.absMgr.getContext().getResources().getDisplayMetrics().widthPixels + ""), new ImageLoadingListener() { // from class: io.dcloud.feature.ui.a.3
            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingCancelled(String str, View view) {
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingStarted(String str, View view) {
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingFailed(String str, View view, FailReason failReason) {
                if (StringConst.canChangeHost(str)) {
                    ImageLoader.getInstance().loadImage(StringConst.changeHost(str), this);
                }
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                AppWidgetMgr.this.a(iApp, ImageLoader.getInstance().getDiscCache().get(str).getPath(), bitmap);
            }
        });
    }

    public void b(IApp iApp) {
        String str = StringConst.STREAMAPP_KEY_ROOTPATH + "splash/" + iApp.obtainAppId() + ".png";
        if (e(str) || PdrUtil.isEquals(AbsoluteConst.TRUE, SP.getBundleData("pdr", iApp.obtainAppId() + SP.STREAM_APP_NOT_FOUND_SPLASH_AT_SERVER))) {
            return;
        }
        this.iActivityHandler.downloadSimpleFileTask(iApp, DataInterface.getSplashUrl(iApp.obtainAppId(), this.absMgr.getContext().getResources().getDisplayMetrics().widthPixels + "", this.absMgr.getContext().getResources().getDisplayMetrics().heightPixels + ""), str, AbsoluteConst.STREAMAPP_KEY_SPLASH);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(NWindow cVar, String str) {
        this.s = cVar;
        if (c(str)) {
            Logger.d("webviewLoadUrl will loadUrl");
            cVar.k().loadUrl(str);
            return true;
        }
        cVar.z = true;
        if (BaseInfo.isWap2AppAppid(this.iApp.obtainAppId())) {
            this.absMgr.processEvent(IMgr.MgrType.AppMgr, 18, this.iApp);
        } else {
            this.p.a(cVar, str);
        }
        return false;
    }

    /* compiled from: AppWidgetMgr.java */
    /* renamed from: io.dcloud.feature.ui.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    class C0008a implements Comparator<NWindow> {
        C0008a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(NWindow cVar, NWindow cVar2) {
            int i = cVar.A - cVar2.A;
            if (i == 0) {
                return cVar.q > cVar2.q ? 1 : -1;
            }
            return i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i) {
        if (TestUtil.PointTime.hasStreamAppStatus(this.iApp.getActivity(), this.iApp.obtainAppId(), TestUtil.PointTime.STATUS_DOWNLOAD_COMPLETED) || !TestUtil.PointTime.hasPointTime(TestUtil.PointTime.DATA_CACHE_PAGES)) {
            return;
        }
        TestUtil.PointTime.getPointTime(TestUtil.PointTime.DATA_CACHE_PAGES).point(2, System.currentTimeMillis());
        StringBuffer stringBuffer = new StringBuffer();
        TestUtil.PointTime pointTime = TestUtil.PointTime.getPointTime(TestUtil.PointTime.DATA_CACHE_PAGES);
        stringBuffer.append("&d=").append(pointTime.getPoint(0));
        stringBuffer.append("&de=").append(System.currentTimeMillis());
        stringBuffer.append("&db=").append(pointTime.getPoint(1));
        stringBuffer.append("&dc=").append(pointTime.getPoint(2));
        stringBuffer.append("&v=").append(PdrUtil.encodeURL(this.iApp.obtainAppVersionName()));
        stringBuffer.append("&bc=").append(i);
        stringBuffer.append("&br=").append(f());
        stringBuffer.append(AbsoluteConst.STREAMAPP_KEY_NET).append(NetworkTypeUtil.getNetworkType(this.iApp.getActivity()));
        String str = null;
        try {
            str = URLEncoder.encode(this.q.toString(), "utf-8");
            this.q = new StringBuffer();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        stringBuffer.append("&bl=").append(str);
        TestUtil.PointTime.commit(this.iActivityHandler, this.iApp.obtainAppId(), TestUtil.PointTime.DATA_CACHE_PAGES, null, 6, null, stringBuffer.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(String str) {
        Integer num;
        if (TestUtil.PointTime.hasStreamAppStatus(this.iApp.getActivity(), this.iApp.obtainAppId(), TestUtil.PointTime.STATUS_DOWNLOAD_COMPLETED)) {
            return;
        }
        Integer remove = this.r.remove(str);
        if (remove == null) {
            num = new Integer(1);
        } else {
            num = new Integer(remove.intValue() + 1);
        }
        this.r.put(str, num);
    }

    private int f() {
        Iterator<String> it = this.r.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            int intValue = this.r.get(it.next()).intValue();
            if (intValue > i) {
                i = intValue;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        if (TextUtils.isEmpty(this.t) || !c(this.t)) {
            return;
        }
        f(this.s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        f(this.s);
    }

    /* compiled from: AppWidgetMgr.java */
    /* loaded from: classes.dex */
    class b implements BroadcastReceiver {
        HashMap<String, ArrayList<NWindow>> a = new HashMap<>();
        IActivityHandler b;

        b(IActivityHandler iActivityHandler) {
            this.b = null;
            this.b = iActivityHandler;
        }

        public synchronized void a(NWindow cVar, String str) {
            Logger.d(Logger.StreamApp_TAG, "requestStreamAppPage url=" + str);
            if (TextUtils.isEmpty(AppWidgetMgr.this.t)) {
                AppWidgetMgr.this.t = str;
            }
            if (!AppWidgetMgr.this.u.contains(str)) {
                AppWidgetMgr.this.u.add(str);
            }
            this.b.raiseFilePriority(str, AppWidgetMgr.this.iApp.obtainAppId());
            ArrayList<NWindow> arrayList = this.a.get(str);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.a.put(str, arrayList);
                AppWidgetMgr.this.q.append(str.substring(str.indexOf("www/") + 4, str.length())).append(JSUtil.COMMA);
            }
            arrayList.add(cVar);
            if (!TestUtil.PointTime.hasStreamAppStatus(AppWidgetMgr.this.iApp.getActivity(), AppWidgetMgr.this.iApp.obtainAppId(), TestUtil.PointTime.STATUS_DOWNLOAD_COMPLETED) && TestUtil.PointTime.hasPointTime(TestUtil.PointTime.DATA_CACHE_PAGES)) {
                TestUtil.PointTime.getPointTime(TestUtil.PointTime.DATA_CACHE_PAGES).point(1, System.currentTimeMillis());
            }
        }

        public synchronized void a(int i, String str) {
            ArrayList<NWindow> arrayList = this.a.get(str);
            if (arrayList != null) {
                Logger.i(Logger.StreamApp_TAG, "responseStreamAppPage url=" + str + " status=" + i);
                if (i == AppWidgetMgr.this.appStreamUtils.getInt("CONTRACT_STATUS_SUCCESS")) {
                    Iterator<NWindow> it = arrayList.iterator();
                    while (it.hasNext()) {
                        NWindow next = it.next();
                        next.z = false;
                        if (next.k() != null) {
                            next.k().loadUrl(str);
                        }
                        if (next.w == 1) {
                            Object[] objArr = (Object[]) next.x;
                            next.a((IWebview) objArr[0], (JSONArray) objArr[1], (NWindow) objArr[2], (String) objArr[3]);
                            next.x = null;
                        } else if (next.w != 2) {
                            int i2 = next.w;
                        }
                        if (next.y) {
                            AppWidgetMgr.this.f(next);
                        }
                    }
                    this.a.remove(str);
                    if (this.a.isEmpty()) {
                        AppWidgetMgr.this.b(0);
                    }
                } else {
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AppWidgetMgr.this.f(str);
                    this.b.addAppStreamTask(str, AppWidgetMgr.this.iApp.obtainAppId());
                }
            }
        }

        public void a() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AppWidgetMgr.this.j);
            this.b.registerReceiver(this, intentFilter);
        }

        private void b() {
            if (TextUtils.isEmpty(AppWidgetMgr.this.t)) {
                NWindow b = AppWidgetMgr.this.b();
                AppWidgetMgr.this.t = b.k().getOriginalUrl();
                ArrayList<NWindow> arrayList = this.a.get(AppWidgetMgr.this.t);
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                    this.a.put(AppWidgetMgr.this.t, arrayList);
                    AppWidgetMgr.this.q.append(AppWidgetMgr.this.t.substring(AppWidgetMgr.this.t.indexOf("www/") + 4, AppWidgetMgr.this.t.length())).append(JSUtil.COMMA);
                }
                arrayList.add(b);
            }
        }

        private void c() {
            Iterator<NWindow> it = AppWidgetMgr.this.b.iterator();
            while (it.hasNext()) {
                NWindow next = it.next();
                if (next.z) {
                    next.u.obtainWebView().loadUrl(next.v);
                    next.z = false;
                }
            }
        }

        @Override // io.dcloud.feature.internal.reflect.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (PdrUtil.isEquals(intent.getAction(), AppWidgetMgr.this.j)) {
                Bundle extras = intent.getExtras();
                String stringExtra = intent.getStringExtra("flag");
                String string = extras.getString(AppWidgetMgr.this.appStreamUtils.getString("CONTRACT_INTENT_EXTRA_FILE"));
                if (!string.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                    string = DeviceInfo.FILE_PROTOCOL + string;
                }
                int i = extras.getInt(AppWidgetMgr.this.appStreamUtils.getString("CONTRACT_INTENT_EXTRA_TYPE"));
                int i2 = extras.getInt(AppWidgetMgr.this.appStreamUtils.getString("CONTRACT_INTENT_EXTRA_STATUS"));
                int i3 = 0;
                Logger.d("AppWidgetMgr.onReceive", Integer.valueOf(i), Integer.valueOf(i2), string, stringExtra);
                if (stringExtra.compareTo(AbsoluteConst.JSON_KEY_ICON) == 0) {
                    if (i2 == AppWidgetMgr.this.appStreamUtils.getInt("CONTRACT_STATUS_SUCCESS")) {
                        AppWidgetMgr aVar = AppWidgetMgr.this;
                        aVar.a(aVar.iApp, string, (Bitmap) null);
                        return;
                    } else {
                        AppWidgetMgr aVar2 = AppWidgetMgr.this;
                        aVar2.a(aVar2.iApp);
                        return;
                    }
                }
                if (stringExtra.compareTo(AbsoluteConst.STREAMAPP_KEY_SPLASH) == 0) {
                    if (i2 == AppWidgetMgr.this.appStreamUtils.getInt("CONTRACT_STATUS_NOT_FOUND")) {
                        SP.setBundleData("pdr", AppWidgetMgr.this.iApp.obtainAppId() + SP.STREAM_APP_NOT_FOUND_SPLASH_AT_SERVER, AbsoluteConst.TRUE);
                        return;
                    }
                    return;
                }
                if (AbsoluteConst.STREAM_PAGE_ZIP.equalsIgnoreCase(stringExtra)) {
                    AppWidgetMgr.this.g();
                    b();
                    a(i2, AppWidgetMgr.this.t);
                    while (i3 < AppWidgetMgr.this.u.size()) {
                        a(i2, (String) AppWidgetMgr.this.u.get(i3));
                        i3++;
                    }
                    AppWidgetMgr.this.u.clear();
                    return;
                }
                if (AbsoluteConst.STREAM_IDLE_ZIP.equalsIgnoreCase(stringExtra)) {
                    AppWidgetMgr.this.h();
                    b();
                    a(i2, AppWidgetMgr.this.t);
                    while (i3 < AppWidgetMgr.this.u.size()) {
                        a(i2, (String) AppWidgetMgr.this.u.get(i3));
                        i3++;
                    }
                    AppWidgetMgr.this.u.clear();
                    return;
                }
                if (AbsoluteConst.STREAMAPP_KEY_WAP2APP_SRC.equalsIgnoreCase(stringExtra)) {
                    AppWidgetMgr.this.h();
                    c();
                    return;
                }
                if (i == AppWidgetMgr.this.appStreamUtils.getInt("CONTRACT_TYPE_STREAM_PAGE") || i == AppWidgetMgr.this.appStreamUtils.getInt("CONTRACT_TYPE_STREAM_MAIN_PAGE") || i == AppWidgetMgr.this.appStreamUtils.getInt("CONTRACT_TYPE_SINGLE_FILE")) {
                    a(i2, string);
                    return;
                }
                AppWidgetMgr aVar3 = AppWidgetMgr.this;
                if (aVar3.b(aVar3.iApp.obtainAppId()) && i == AppWidgetMgr.this.appStreamUtils.getInt("CONTRACT_TYPE_STREAM_APP") && i2 == AppWidgetMgr.this.appStreamUtils.getInt("CONTRACT_STATUS_SUCCESS")) {
                    Logger.d(Logger.StreamApp_TAG, "AppWidgetManager onReceive " + extras);
                    AppWidgetMgr.this.iApp.smartUpdate();
                    if (!TestUtil.PointTime.hasStreamAppStatus(context, AppWidgetMgr.this.iApp.obtainAppId(), TestUtil.PointTime.STATUS_INSTALLED) && TestUtil.PointTime.hasPointTime(TestUtil.STREAM_APP_POINT)) {
                        TestUtil.PointTime.commit(AppWidgetMgr.this.iActivityHandler, AppWidgetMgr.this.iApp.obtainAppId(), 1, 0, "&v=" + PdrUtil.encodeURL(AppWidgetMgr.this.iApp.obtainAppVersionName()) + "&ac=5&sf=" + StringConst.getIntSF(BaseInfo.getCmitInfo(AppWidgetMgr.this.iApp.obtainAppId()).plusLauncher) + "&sfd=" + BaseInfo.getCmitInfo(AppWidgetMgr.this.iApp.obtainAppId()).sfd);
                    }
                    String allCommitData = TestUtil.PointTime.getAllCommitData(context, AppWidgetMgr.this.iApp.obtainAppId());
                    if (!TextUtils.isEmpty(allCommitData)) {
                        TestUtil.PointTime.commit(AppWidgetMgr.this.iActivityHandler, AppWidgetMgr.this.iApp.obtainAppId(), 3, 0, allCommitData + "&v=" + PdrUtil.encodeURL(AppWidgetMgr.this.iApp.obtainAppVersionName()));
                    }
                    if (TestUtil.PointTime.hasPointTime(TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED)) {
                        TestUtil.PointTime.getPointTime(TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED).point(2, System.currentTimeMillis());
                    }
                    if (TestUtil.PointTime.hasPointTime(TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED)) {
                        StringBuffer stringBuffer = new StringBuffer();
                        TestUtil.PointTime pointTime = TestUtil.PointTime.getPointTime(TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED);
                        stringBuffer.append("&d=").append(pointTime.getPoint(0));
                        stringBuffer.append("&de=").append(System.currentTimeMillis());
                        stringBuffer.append("&di=").append(pointTime.getPoint(1));
                        stringBuffer.append("&df=").append(pointTime.getPoint(2));
                        stringBuffer.append("&v=").append(PdrUtil.encodeURL(AppWidgetMgr.this.iApp.obtainAppVersionName()));
                        stringBuffer.append(AbsoluteConst.STREAMAPP_KEY_NET).append(NetworkTypeUtil.getNetworkType(AppWidgetMgr.this.iApp.getActivity()));
                        String data = TestUtil.PointTime.getData(context, AppWidgetMgr.this.iApp.obtainAppId(), TestUtil.PointTime.DATA_START_TIMES);
                        TestUtil.PointTime.commit(AppWidgetMgr.this.iActivityHandler, AppWidgetMgr.this.iApp.obtainAppId(), TestUtil.PointTime.DATA_DOWNLOAD_COMPLETED, TestUtil.PointTime.DATA_START_TIMES, 5, TextUtils.isEmpty(data) ? null : "&f=" + data, stringBuffer.toString());
                    }
                    AppWidgetMgr aVar4 = AppWidgetMgr.this;
                    aVar4.b(aVar4.iApp);
                }
            }
        }
    }
}
