package io.dcloud.feature.nativeObj;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* compiled from: NativeBitmapMgr.java */
/* loaded from: classes.dex olb b */
public class NativeBitmapMgr {
    private HashMap<String, INativeBitmap> b = new HashMap<>();
    private HashMap<String, String> c = new HashMap<>();
    private HashMap<String, NativeView> d = new HashMap<>();
    public final String a = "{path:'file://%s', w:%d, h:%d, size:%d}";

    /* compiled from: NativeBitmapMgr.java */
    /* loaded from: classes.dex */
    private enum BitmapCommand {
        Bitmap,
        getItems,
        getBitmapById,
        clear,
        load,
        loadBase64Data,
        save,
        toBase64Data,
        View,
        startAnimation,
        clearAnimation,
        getViewById,
        drawBitmap,
        drawText,
        show,
        hide,
        view_animate,
        view_reset,
        view_restore,
        view_drawRect,
        isVisible,
        addEventListener,
        interceptTouchEvent,
        setTouchEventRect,
        bitmapRecycle,
        setStyle,
        view_clearRect,
        view_draw,
        view_close
    }

    public JSONArray a() {
        JSONArray jSONArray = new JSONArray();
        Iterator<Map.Entry<String, INativeBitmap>> it = this.b.entrySet().iterator();
        while (it.hasNext()) {
            try {
                jSONArray.put(new JSONObject(((NativeBitmap) it.next().getValue()).f()));
            } catch (Exception unused) {
            }
        }
        return jSONArray;
    }

    public INativeBitmap a(String str) {
        return this.b.get(str);
    }

    public INativeBitmap b(String str) {
        return a(this.c.get(str));
    }

    public Object a(String str, Object obj) {
        if ("addNativeView".equals(str)) {
            Object[] objArr = (Object[]) obj;
            IFrameView iFrameView = (IFrameView) objArr[0];
            String str2 = (String) objArr[1];
            NativeView a2 = a(str2, str2);
            if (a2 == null) {
                return null;
            }
            a2.b(iFrameView);
            return null;
        }
        if ("removeNativeView".equals(str)) {
            Object[] objArr2 = (Object[]) obj;
            IFrameView iFrameView2 = (IFrameView) objArr2[0];
            String str3 = (String) objArr2[1];
            NativeView a3 = a(str3, str3);
            if (a3 == null) {
                return null;
            }
            a3.a(iFrameView2);
            return null;
        }
        if ("getNativeView".equals(str)) {
            try {
                Object[] objArr3 = (Object[]) obj;
                String str4 = (String) objArr3[1];
                return a(str4, str4);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (!"View".equals(str)) {
            return null;
        }
        try {
            Object[] objArr4 = (Object[]) obj;
            IWebview iWebview = (IWebview) objArr4[1];
            String str5 = (String) objArr4[2];
            String str6 = (String) objArr4[3];
            JSONObject jSONObject = (JSONObject) objArr4[4];
            if (jSONObject == null) {
                jSONObject = new JSONObject();
            }
            JSONObject jSONObject2 = jSONObject;
            if (this.d.containsKey(str5)) {
                return null;
            }
            NativeView eVar = new NativeView(iWebview.getContext(), iWebview, str6, str5, jSONObject2);
            this.d.put(eVar.b, eVar);
            return eVar;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private NativeView a(String str, String str2) {
        NativeView eVar = this.d.get(str);
        if (eVar != null || TextUtils.isEmpty(str2)) {
            return eVar;
        }
        for (NativeView eVar2 : this.d.values()) {
            if (TextUtils.equals(eVar2.c, str2)) {
                return eVar2;
            }
        }
        return eVar;
    }

    public void a(NativeView eVar) {
        NativeView eVar2 = this.d.get(eVar.b);
        eVar.g();
        if (eVar2 == eVar) {
            this.d.remove(eVar.b);
        } else {
            this.d.remove(eVar.c);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:13:0x002d. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0490 A[Catch: Exception -> 0x04a7, TRY_LEAVE, TryCatch #10 {Exception -> 0x04a7, blocks: (B:70:0x0176, B:72:0x017b, B:74:0x018b, B:75:0x0190, B:77:0x01a0, B:78:0x01a5, B:79:0x01b4, B:81:0x01da, B:82:0x01df, B:84:0x01eb, B:90:0x01fb, B:94:0x020b, B:96:0x020e, B:98:0x0216, B:100:0x021c, B:101:0x0227, B:103:0x0230, B:105:0x023c, B:107:0x0242, B:110:0x024a, B:113:0x0254, B:115:0x025e, B:117:0x026a, B:119:0x0274, B:120:0x0276, B:122:0x027e, B:124:0x0284, B:125:0x028d, B:127:0x02a1, B:129:0x02ad, B:130:0x02b2, B:132:0x02be, B:133:0x02c3, B:135:0x02cf, B:136:0x02d8, B:138:0x02e4, B:140:0x02f2, B:141:0x02fb, B:142:0x0300, B:144:0x030c, B:145:0x0311, B:147:0x031d, B:149:0x0338, B:152:0x0342, B:153:0x0350, B:154:0x035b, B:156:0x0369, B:159:0x036f, B:162:0x037d, B:163:0x038a, B:165:0x0385, B:167:0x0377, B:168:0x0399, B:170:0x03a5, B:172:0x03ad, B:175:0x03b3, B:178:0x03c1, B:179:0x03ce, B:181:0x03c9, B:183:0x03bb, B:184:0x03de, B:186:0x03e5, B:187:0x03ea, B:189:0x03f0, B:192:0x03fa, B:194:0x0406, B:196:0x040e, B:198:0x0414, B:199:0x041e, B:201:0x042a, B:202:0x0435, B:204:0x0441, B:205:0x0449, B:207:0x044f, B:208:0x045c, B:210:0x0462, B:212:0x0457, B:213:0x0466, B:215:0x046c, B:216:0x0479, B:223:0x047d, B:219:0x0488, B:221:0x0490, B:228:0x0474), top: B:12:0x002d, inners: #2, #3, #7, #8, #9, #11 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String a(io.dcloud.common.DHInterface.IWebview r20, java.lang.String r21, java.lang.String[] r22) {
        /*
            Method dump skipped, instructions count: 1276
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.b.a(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    /* compiled from: NativeBitmapMgr.java */
    /* renamed from: io.dcloud.feature.nativeObj.b$7, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[BitmapCommand.values().length];
            a = iArr;
            try {
                iArr[BitmapCommand.View.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[BitmapCommand.setStyle.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[BitmapCommand.addEventListener.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[BitmapCommand.interceptTouchEvent.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[BitmapCommand.setTouchEventRect.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[BitmapCommand.getViewById.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[BitmapCommand.drawBitmap.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[BitmapCommand.drawText.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                a[BitmapCommand.show.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[BitmapCommand.hide.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                a[BitmapCommand.view_close.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                a[BitmapCommand.view_animate.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                a[BitmapCommand.view_reset.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                a[BitmapCommand.view_restore.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                a[BitmapCommand.view_drawRect.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                a[BitmapCommand.isVisible.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                a[BitmapCommand.Bitmap.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                a[BitmapCommand.getItems.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                a[BitmapCommand.getBitmapById.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                a[BitmapCommand.clear.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                a[BitmapCommand.bitmapRecycle.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                a[BitmapCommand.load.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                a[BitmapCommand.loadBase64Data.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                a[BitmapCommand.save.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                a[BitmapCommand.toBase64Data.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                a[BitmapCommand.startAnimation.ordinal()] = 26;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                a[BitmapCommand.clearAnimation.ordinal()] = 27;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                a[BitmapCommand.view_clearRect.ordinal()] = 28;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                a[BitmapCommand.view_draw.ordinal()] = 29;
            } catch (NoSuchFieldError unused29) {
            }
        }
    }

    private void a(IWebview iWebview, NativeView eVar, JSONArray jSONArray) {
        JSONObject jSONObject;
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        JSONObject jSONObject4;
        if (jSONArray == null || eVar == null) {
            return;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                JSONObject jSONObject5 = jSONArray.getJSONObject(i);
                JSONObject jSONObject6 = null;
                String optString = jSONObject5.has("id") ? jSONObject5.optString("id") : null;
                String optString2 = jSONObject5.optString("tag");
                if (optString2.equals("img")) {
                    try {
                        NativeBitmap a2 = a(iWebview, iWebview.obtainApp(), jSONObject5.getString(IApp.ConfigProperty.CONFIG_SRC));
                        try {
                            jSONObject3 = new JSONObject(jSONObject5.getString("sprite"));
                        } catch (JSONException unused) {
                            jSONObject3 = new JSONObject();
                        }
                        JSONObject jSONObject7 = jSONObject3;
                        try {
                            jSONObject4 = new JSONObject(jSONObject5.getString("position"));
                        } catch (JSONException unused2) {
                            jSONObject4 = new JSONObject();
                        }
                        eVar.a(iWebview, a2, null, -1, jSONObject7, jSONObject4, null, optString, "img", false);
                    } catch (Exception e) {
                        e = e;
                        e.printStackTrace();
                        return;
                    }
                } else {
                    if (optString2.equals("font")) {
                        String string = jSONObject5.getString("text");
                        if (!TextUtils.isEmpty(string)) {
                            try {
                                jSONObject = new JSONObject(jSONObject5.getString("position"));
                            } catch (JSONException unused3) {
                                jSONObject = new JSONObject();
                            }
                            JSONObject jSONObject8 = jSONObject;
                            try {
                                jSONObject2 = new JSONObject(jSONObject5.getString("textStyles"));
                            } catch (JSONException unused4) {
                                jSONObject2 = new JSONObject();
                            }
                            eVar.a(iWebview, null, string, -1, null, jSONObject8, jSONObject2, optString, "font", false);
                        }
                    } else if (optString2.equals("rect")) {
                        String string2 = jSONObject5.has(AbsoluteConst.JSON_KEY_COLOR) ? jSONObject5.getString(AbsoluteConst.JSON_KEY_COLOR) : "#FFFFFF";
                        String string3 = jSONObject5.getString("position");
                        if (!TextUtils.isEmpty(string3) && !string3.equals("null")) {
                            jSONObject6 = new JSONObject(string3);
                        }
                        eVar.a(iWebview, null, null, PdrUtil.stringToColor(string2), null, jSONObject6, null, optString, "rect", false);
                    }
                }
            } catch (Exception e2) {
//                e = e2;
            }
        }
        eVar.c();
    }

    private void a(final IWebview iWebview, NativeBitmap aVar, String str, final String str2) {
        aVar.a(iWebview, iWebview.obtainFrameView().obtainMainView().getContext(), str, TextUtils.isEmpty(str2) ? null : new ICallBack() { // from class: io.dcloud.feature.nativeObj.b.1
            @Override // io.dcloud.common.DHInterface.ICallBack
            public Object onCallBack(int i, Object obj) {
                JSUtil.execCallback(iWebview, str2, null, JSUtil.OK, false, false);
                return null;
            }
        }, TextUtils.isEmpty(str2) ? null : new ICallBack() { // from class: io.dcloud.feature.nativeObj.b.2
            @Override // io.dcloud.common.DHInterface.ICallBack
            public Object onCallBack(int i, Object obj) {
                JSUtil.execCallback(iWebview, str2, "{\"code\":-100,\"message\":\"" + (obj == null ? "加载失败" : obj.toString()) + "\"}", JSUtil.ERROR, true, false);
                return null;
            }
        });
    }

    private void a(IApp iApp, String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2)) {
            str2 = "" + System.currentTimeMillis();
        }
        if (this.b.containsKey(str)) {
            return;
        }
        this.b.put(str, new NativeBitmap(iApp, str2, str, str3));
        this.c.put(str2, str);
    }

    private void b(final IWebview iWebview, NativeBitmap aVar, String str, final String str2) {
        aVar.a(str, TextUtils.isEmpty(str2) ? null : new ICallBack() { // from class: io.dcloud.feature.nativeObj.b.3
            @Override // io.dcloud.common.DHInterface.ICallBack
            public Object onCallBack(int i, Object obj) {
                JSUtil.execCallback(iWebview, str2, null, JSUtil.OK, false, false);
                return null;
            }
        }, TextUtils.isEmpty(str2) ? null : new ICallBack() { // from class: io.dcloud.feature.nativeObj.b.4
            @Override // io.dcloud.common.DHInterface.ICallBack
            public Object onCallBack(int i, Object obj) {
                JSUtil.execCallback(iWebview, str2, "{\"code\":-100,\"message\":\"加载失败\"}", JSUtil.ERROR, true, false);
                return null;
            }
        });
    }

    private void a(final IWebview iWebview, NativeBitmap aVar, String str, JSONObject jSONObject, final String str2) {
        aVar.a(iWebview.obtainFrameView().obtainApp(), str, new NativeBitmapSaveOptions(jSONObject.toString()), iWebview.getScale(), TextUtils.isEmpty(str2) ? null : new ICallBack() { // from class: io.dcloud.feature.nativeObj.b.5
            @Override // io.dcloud.common.DHInterface.ICallBack
            public Object onCallBack(int i, Object obj) {
                String str3;
                if (obj == null || !(obj instanceof NativeBitmapSaveOptions)) {
                    str3 = null;
                } else {
                    NativeBitmapSaveOptions cVar = (NativeBitmapSaveOptions) obj;
                    str3 = String.format("{path:'file://%s', w:%d, h:%d, size:%d}", cVar.d, Integer.valueOf(cVar.e), Integer.valueOf(cVar.f), Long.valueOf(cVar.g));
                }
                JSUtil.execCallback(iWebview, str2, str3, JSUtil.OK, true, false);
                return null;
            }
        }, TextUtils.isEmpty(str2) ? null : new ICallBack() { // from class: io.dcloud.feature.nativeObj.b.6
            @Override // io.dcloud.common.DHInterface.ICallBack
            public Object onCallBack(int i, Object obj) {
                JSUtil.execCallback(iWebview, str2, "{\"code\":-100,\"message\":\"加载失败\"}", JSUtil.ERROR, true, false);
                return null;
            }
        });
    }

    public void b() {
        try {
            Iterator<INativeBitmap> it = this.b.values().iterator();
            while (it.hasNext()) {
                try {
                    it.next().clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.c.clear();
            this.b.clear();
            for (NativeView eVar : this.d.values()) {
                if (eVar != null) {
                    eVar.g();
                }
            }
            this.d.clear();
            NativeTypefaceFactory.clearSoftReference();
            System.gc();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public NativeBitmap a(IWebview iWebview, IApp iApp, String str) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException unused) {
            jSONObject = null;
        }
        if (jSONObject != null) {
            return (NativeBitmap) b(jSONObject.optString("id"));
        }
        String convert2AbsFullPath = (TextUtils.isEmpty(str) || str.equals("null")) ? null : iApp.convert2AbsFullPath(iWebview.obtainFullUrl(), str);
        if (PdrUtil.isEmpty(convert2AbsFullPath)) {
            return null;
        }
        String str2 = iApp.obtainAppId() + convert2AbsFullPath.hashCode();
        return new NativeBitmap(iApp, str2, str2, convert2AbsFullPath);
    }
}
