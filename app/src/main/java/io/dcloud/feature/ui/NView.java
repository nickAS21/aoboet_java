package io.dcloud.feature.ui;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.JsEventUtil;
import io.dcloud.common.util.PdrUtil;

/* compiled from: NView.java */
/* loaded from: classes.dex */
public abstract class NView {
    public static byte j = ViewRect.POSITION_STATIC;
    public static byte k = ViewRect.POSITION_ABSOLUTE;
    public static byte l = ViewRect.POSITION_DOCK;
    public static byte m = ViewRect.DOCK_LEFT;
    public static byte n = ViewRect.DOCK_RIGHT;
    public static byte o = ViewRect.DOCK_TOP;
    public static byte p = ViewRect.DOCK_BOTTOM;
    protected HashMap<IWebview, String> b;
    protected String d;
    private Context s;
    protected NWindow a = null;
    protected AppWidgetMgr c = null;
    protected String e = null;
    protected String f = null;
    protected JSONObject g = null;
    protected IWebview h = null;
    private byte q = k;
    private byte r = o;
    protected HashMap<String, ArrayList<String[]>> i = null;

    public abstract String a(IWebview iWebview, String str, JSONArray jSONArray);

    public abstract void a(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract AdaFrameItem e();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void g();

    public NView(String str) {
        this.b = null;
        this.d = str;
        this.b = new HashMap<>();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(Context context, AppWidgetMgr aVar, IWebview iWebview, String str, JSONObject jSONObject) {
        this.c = aVar;
        this.s = context;
        this.h = iWebview;
        this.e = str;
        if (jSONObject == null) {
            jSONObject = JSONUtil.createJSONObject("{}");
        }
        this.g = jSONObject;
        a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a() {
        JSONObject jSONObject = this.g;
        if (!JSONUtil.isNull(jSONObject, "id") && !PdrUtil.isEmpty(this.f)) {
            this.f = JSONUtil.getString(jSONObject, "id");
        }
        String string = JSONUtil.getString(jSONObject, "position");
        if (!PdrUtil.isEmpty(string)) {
            if (AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE.equals(string)) {
                this.q = k;
            } else if ("dock".equals(string)) {
                this.q = l;
            } else if (AbsoluteConst.JSON_VALUE_POSITION_STATIC.equals(string)) {
                this.q = j;
            }
        }
        String string2 = JSONUtil.getString(jSONObject, "dock");
        if (PdrUtil.isEmpty(string2)) {
            return;
        }
        if ("bottom".equals(string2)) {
            this.r = p;
            return;
        }
        if ("top".equals(string2)) {
            this.r = o;
        } else if ("left".equals(string2)) {
            this.r = m;
        } else if ("right".equals(string2)) {
            this.r = n;
        }
    }

    public final byte b() {
        return this.q;
    }

    public final Context c() {
        return this.s;
    }

    public String d() {
        return String.format("(function(){return {uuid:'%s',identity:'%s',option:%s}})()", this.e, this.d, this.g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(String str, String str2, String str3) {
        if (this.i == null) {
            this.i = new HashMap<>(2);
        }
        ArrayList<String[]> arrayList = this.i.get(str2);
        if (arrayList == null) {
            arrayList = new ArrayList<>(2);
            this.i.put(str2, arrayList);
        }
        arrayList.add(new String[]{str, str3});
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(String str, String str2) {
        ArrayList<String[]> arrayList;
        HashMap<String, ArrayList<String[]>> hashMap = this.i;
        if (hashMap == null || (arrayList = hashMap.get(str2)) == null) {
            return;
        }
        Iterator<String[]> it = arrayList.iterator();
        while (it.hasNext()) {
            String[] next = it.next();
            if (next[0].equals(str)) {
                arrayList.remove(next);
            }
        }
        if (arrayList.size() == 0) {
            this.i.remove(str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void f() {
        Iterator<NWindow> it = this.c.b.iterator();
        while (it.hasNext()) {
            a(this, it.next().i);
        }
    }

    private static void a(NView bVar, HashMap<String, ArrayList<String[]>> hashMap) {
        if (hashMap != null) {
            for (ArrayList<String[]> arrayList : hashMap.values()) {
                if (arrayList != null) {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        String str = arrayList.get(size)[0];
                        if (bVar.c.a(str, str, (String) null) == bVar) {
                            arrayList.remove(size);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean a(String str) {
        ArrayList<String[]> arrayList;
        HashMap<String, ArrayList<String[]>> hashMap = this.i;
        if (hashMap == null || (arrayList = hashMap.get(str)) == null) {
            return false;
        }
        return !arrayList.isEmpty();
    }

    public final boolean b(String str, String str2) {
        return a(str, str2, true);
    }

    public boolean a(String str, String str2, boolean z) {
        ArrayList<String[]> arrayList;
        IWebview obtainWebView;
        Logger.d("execCallback pEventType=" + str + ";");
        HashMap<String, ArrayList<String[]>> hashMap = this.i;
        if (hashMap == null || (arrayList = hashMap.get(str)) == null) {
            return false;
        }
        int size = arrayList.size();
        String eventListener_format = JsEventUtil.eventListener_format(str, str2, z);
        boolean z2 = false;
        for (int i = size - 1; i >= 0; i--) {
            String[] strArr = arrayList.get(i);
            String str3 = strArr[0];
            String str4 = strArr[1];
            NWindow a = this.c.a(str3, str3, (String) null);
            if (a != null && !a.F && (obtainWebView = a.u.obtainWebView()) != null) {
                JSUtil.execCallback(obtainWebView, str4, eventListener_format, JSUtil.OK, true, true);
                z2 = true;
            }
        }
        return z2;
    }
}
