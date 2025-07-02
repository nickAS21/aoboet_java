package io.dcloud.common.common_a;

import android.app.Activity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.ShortCutUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: RunningAppMgr.java */
/* loaded from: classes.dex */
public class b {
    AppMgr a;
    private ArrayList<WebApp> b = new ArrayList<>();
    private ArrayList<String> c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(AppMgr aVar) {
        this.a = null;
        ArrayList<String> arrayList = new ArrayList<>();
        BaseInfo.sRunningApp = arrayList;
        this.c = arrayList;
        this.a = aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(WebApp dVar, ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType.equals(ISysEventListener.SysEventType.onResume)) {
            for (int i = 0; i < this.b.size(); i++) {
                WebApp dVar2 = this.b.get(i);
                if (dVar2 != null && (ShortCutUtil.isOpsCreateShortcut(dVar2.getActivity(), dVar2.obtainAppId()) || ShortCutUtil.isRunShortCut(dVar2.obtainAppId()))) {
                    if (!MobilePhoneModel.isSmartisanLauncherPhone(dVar2.getActivity())) {
                        ShortCutUtil.onResumeCreateShortcut(dVar2);
                    } else {
                        ShortCutUtil.commitShortcut(dVar2, 12, true, false, true, 0);
                    }
                }
            }
        } else if (dVar != null && sysEventType.equals(ISysEventListener.SysEventType.onKeyDown) && ((Integer) ((Object[]) obj)[0]).intValue() == 4 && BaseInfo.isPostChcekShortCut) {
            ShortCutUtil.isHasShortCut(dVar, 500L, "back");
            return true;
        }
        boolean z = dVar == null;
        int size = this.b.size();
        WebApp dVar3 = null;
        int i2 = size - 1;
        boolean z2 = false;
        while (true) {
            if (i2 < 0) {
                break;
            }
            WebApp dVar4 = this.b.get(i2);
            if (!z ? dVar4 == dVar : z) {
                z2 |= dVar4.onExecute(sysEventType, obj);
                if (z2 && !WebApp.a(sysEventType)) {
                    dVar3 = dVar4;
                    break;
                }
                dVar3 = dVar4;
            }
            i2--;
        }
        if (z2 || !sysEventType.equals(ISysEventListener.SysEventType.onKeyUp) || size <= 1 || dVar3 == null || ((Integer) ((Object[]) obj)[0]).intValue() != 4) {
            return z2;
        }
        this.a.processEvent(IMgr.MgrType.WindowMgr, 20, dVar3);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WebApp a(String str) {
        int indexOf = this.c.indexOf(str);
        if (indexOf >= 0) {
            return this.b.get(indexOf);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(String str, WebApp dVar) {
        this.c.add(str);
        this.b.add(dVar);
        BaseInfo.isStreamAppRuning = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebApp b(String str) {
        WebApp dVar;
        Iterator<WebApp> it = this.b.iterator();
        while (true) {
            if (!it.hasNext()) {
                dVar = null;
                break;
            }
            dVar = it.next();
            if (TextUtils.equals(dVar.obtainAppId(), str)) {
                break;
            }
        }
        Logger.d("AppCache", "removeWebApp " + dVar + ";mAppIdList=" + this.c);
        this.b.remove(dVar);
        this.c.remove(str);
        if (this.b.size() == 0) {
            BaseInfo.isStreamAppRuning = false;
        }
        return dVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WebApp a() {
        long j = 0;
        WebApp dVar = null;
        for (int size = this.b.size() - 1; size >= 0; size--) {
            WebApp dVar2 = this.b.get(size);
            if (dVar2.d == 3 && dVar2.T > j) {
                j = dVar2.T;
                dVar = dVar2;
            }
        }
        return dVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WebApp a(Activity activity, WebApp dVar) {
        if (this.b.contains(dVar)) {
            return null;
        }
        System.currentTimeMillis();
        if (this.b.size() >= BaseInfo.s_Runing_App_Count_Max) {
            return b();
        }
        return null;
    }

    public WebApp b() {
        long currentTimeMillis = System.currentTimeMillis();
        WebApp dVar = null;
        for (int i = 0; i < this.b.size(); i++) {
            WebApp dVar2 = this.b.get(i);
            if (dVar2.T < currentTimeMillis) {
                currentTimeMillis = dVar2.T;
                dVar = dVar2;
            }
        }
        return dVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ArrayList<WebApp> c() {
        ArrayList<WebApp> arrayList = new ArrayList<>(d());
        int d = d();
        for (int i = 0; i < d; i++) {
            WebApp dVar = this.b.get(i);
            int i2 = 0;
            int i3 = 0;
            while (i2 < arrayList.size()) {
                if (dVar.T > arrayList.get(i2).T) {
                    i3 = i2 + 1;
                    i2 = i3;
                }
            }
            arrayList.add(i3, dVar);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int d() {
        return this.b.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        this.b.clear();
        this.c.clear();
    }
}
