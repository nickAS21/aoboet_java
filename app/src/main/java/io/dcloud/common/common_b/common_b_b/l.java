package io.dcloud.common.common_b.common_b_b;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;

/* compiled from: WindowMgr.java */
/* loaded from: classes.dex */
public class l extends AbsMgr implements IMgr.WindowEvent {
    HashMap<String, DHAppRootView> a;
    String b;
    a c;
    WindowManager.LayoutParams d;

    private boolean a(int i, int i2, int i3, int i4, int i5, int i6) {
        return i == 0 && i2 == 0 && i3 == i5 && i4 == i6;
    }

    public l(ICore iCore) {
        super(iCore, "windowmgr", IMgr.MgrType.WindowMgr);
        this.a = new HashMap<>(0);
        this.b = null;
        this.c = null;
        this.d = null;
    }

    void a(ViewGroup viewGroup, IApp iApp, IWebview iWebview, ViewGroup.LayoutParams layoutParams) {
        a(iApp, iApp.obtainAppId());
        DHAppRootView cVar = this.a.get(iApp.obtainAppId());
        DHFrameView dVar = (DHFrameView) iWebview.obtainFrameView();
        dVar.l = cVar;
        View obtainMainView = dVar.obtainMainView();
        if (obtainMainView.getParent() != null) {
            ((ViewGroup) obtainMainView.getParent()).removeView(obtainMainView);
        }
        viewGroup.addView(obtainMainView, layoutParams);
    }

    synchronized boolean a(IApp iApp, String str) {
        boolean z;
        Logger.e("streamsdk", "come into createAppRootView pAppid===" + str);
        z = false;
        DHAppRootView cVar = this.a.get(str);
        if (cVar == null || !cVar.c) {
            if (cVar != null && !cVar.c) {
                this.a.remove(str);
            }
            Logger.e("streamsdk", "come into createAppRootView and new le rootview  pAppid===" + str);
            Logger.d(Logger.MAIN_TAG, "create " + str + " AppRootView");
            DHAppRootView cVar2 = new DHAppRootView(iApp.getActivity(), iApp, null);
            cVar2.onAppStart(iApp);
            cVar2.obtainFrameOptions().setParentViewRect(iApp.getAppViewRect());
            cVar2.obtainFrameOptions().updateViewData(JSONUtil.createJSONObject("{}"));
            z = true;
            this.a.put(str, cVar2);
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:251:0x05ec A[Catch: all -> 0x06c1, TryCatch #0 {all -> 0x06c1, blocks: (B:3:0x000a, B:5:0x0010, B:10:0x002c, B:12:0x006d, B:14:0x0082, B:16:0x008f, B:18:0x0098, B:21:0x009e, B:23:0x00a3, B:25:0x00af, B:28:0x00c1, B:29:0x00d0, B:31:0x00d6, B:34:0x00e4, B:36:0x00e8, B:37:0x0108, B:38:0x0116, B:40:0x011c, B:43:0x0128, B:46:0x0136, B:53:0x00f4, B:55:0x00f8, B:57:0x013c, B:59:0x0151, B:62:0x0156, B:64:0x015e, B:67:0x0168, B:69:0x016d, B:71:0x0173, B:73:0x017b, B:75:0x0183, B:77:0x018f, B:78:0x01c4, B:80:0x01e0, B:84:0x01e5, B:90:0x01f2, B:92:0x01f7, B:94:0x01fe, B:96:0x0206, B:98:0x0212, B:100:0x0216, B:102:0x026f, B:109:0x0272, B:111:0x0277, B:114:0x027f, B:116:0x0297, B:118:0x029b, B:121:0x02a3, B:124:0x02cd, B:126:0x02d1, B:129:0x02d9, B:133:0x0305, B:135:0x030a, B:137:0x0316, B:138:0x0341, B:141:0x0347, B:143:0x034b, B:145:0x0368, B:146:0x036f, B:148:0x0355, B:150:0x0359, B:152:0x0378, B:154:0x037e, B:156:0x03c2, B:157:0x03c9, B:159:0x03cf, B:161:0x03e9, B:163:0x03f6, B:165:0x03ff, B:167:0x0408, B:169:0x0414, B:171:0x0422, B:173:0x042c, B:177:0x0436, B:179:0x043b, B:181:0x0447, B:183:0x0469, B:184:0x046e, B:186:0x0474, B:189:0x04b5, B:192:0x04d4, B:195:0x04e0, B:197:0x04e4, B:199:0x04ef, B:200:0x04f8, B:202:0x0504, B:204:0x050c, B:206:0x051d, B:207:0x0524, B:210:0x052b, B:212:0x052f, B:215:0x0538, B:216:0x0541, B:218:0x0549, B:220:0x055a, B:230:0x0563, B:232:0x0567, B:234:0x0578, B:236:0x0582, B:238:0x0587, B:240:0x05a0, B:242:0x05be, B:244:0x05c4, B:245:0x05d9, B:247:0x05dd, B:249:0x05e3, B:251:0x05ec, B:256:0x0600, B:258:0x0604, B:261:0x060a, B:265:0x061e, B:267:0x062b, B:270:0x05f9, B:273:0x05ca, B:275:0x05d0, B:277:0x0648, B:279:0x064c, B:282:0x0653, B:285:0x067d, B:287:0x0688, B:290:0x06b5), top: B:2:0x000a, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x05f5  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0604 A[Catch: all -> 0x06c1, TRY_LEAVE, TryCatch #0 {all -> 0x06c1, blocks: (B:3:0x000a, B:5:0x0010, B:10:0x002c, B:12:0x006d, B:14:0x0082, B:16:0x008f, B:18:0x0098, B:21:0x009e, B:23:0x00a3, B:25:0x00af, B:28:0x00c1, B:29:0x00d0, B:31:0x00d6, B:34:0x00e4, B:36:0x00e8, B:37:0x0108, B:38:0x0116, B:40:0x011c, B:43:0x0128, B:46:0x0136, B:53:0x00f4, B:55:0x00f8, B:57:0x013c, B:59:0x0151, B:62:0x0156, B:64:0x015e, B:67:0x0168, B:69:0x016d, B:71:0x0173, B:73:0x017b, B:75:0x0183, B:77:0x018f, B:78:0x01c4, B:80:0x01e0, B:84:0x01e5, B:90:0x01f2, B:92:0x01f7, B:94:0x01fe, B:96:0x0206, B:98:0x0212, B:100:0x0216, B:102:0x026f, B:109:0x0272, B:111:0x0277, B:114:0x027f, B:116:0x0297, B:118:0x029b, B:121:0x02a3, B:124:0x02cd, B:126:0x02d1, B:129:0x02d9, B:133:0x0305, B:135:0x030a, B:137:0x0316, B:138:0x0341, B:141:0x0347, B:143:0x034b, B:145:0x0368, B:146:0x036f, B:148:0x0355, B:150:0x0359, B:152:0x0378, B:154:0x037e, B:156:0x03c2, B:157:0x03c9, B:159:0x03cf, B:161:0x03e9, B:163:0x03f6, B:165:0x03ff, B:167:0x0408, B:169:0x0414, B:171:0x0422, B:173:0x042c, B:177:0x0436, B:179:0x043b, B:181:0x0447, B:183:0x0469, B:184:0x046e, B:186:0x0474, B:189:0x04b5, B:192:0x04d4, B:195:0x04e0, B:197:0x04e4, B:199:0x04ef, B:200:0x04f8, B:202:0x0504, B:204:0x050c, B:206:0x051d, B:207:0x0524, B:210:0x052b, B:212:0x052f, B:215:0x0538, B:216:0x0541, B:218:0x0549, B:220:0x055a, B:230:0x0563, B:232:0x0567, B:234:0x0578, B:236:0x0582, B:238:0x0587, B:240:0x05a0, B:242:0x05be, B:244:0x05c4, B:245:0x05d9, B:247:0x05dd, B:249:0x05e3, B:251:0x05ec, B:256:0x0600, B:258:0x0604, B:261:0x060a, B:265:0x061e, B:267:0x062b, B:270:0x05f9, B:273:0x05ca, B:275:0x05d0, B:277:0x0648, B:279:0x064c, B:282:0x0653, B:285:0x067d, B:287:0x0688, B:290:0x06b5), top: B:2:0x000a, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x060a A[Catch: Exception -> 0x062a, all -> 0x06c1, TRY_ENTER, TryCatch #1 {Exception -> 0x062a, blocks: (B:261:0x060a, B:265:0x061e), top: B:259:0x0608, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:265:0x061e A[Catch: Exception -> 0x062a, all -> 0x06c1, TRY_LEAVE, TryCatch #1 {Exception -> 0x062a, blocks: (B:261:0x060a, B:265:0x061e), top: B:259:0x0608, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:269:0x0607  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x05ff  */
    @Override // io.dcloud.common.DHInterface.IMgr
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object processEvent(io.dcloud.common.DHInterface.IMgr.MgrType r16, final int r17, java.lang.Object r18) {
        /*
            Method dump skipped, instructions count: 1834
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.l.processEvent(io.dcloud.common.DHInterface.IMgr$MgrType, int, java.lang.Object):java.lang.Object");
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(int r14, java.lang.Object r15) {
        /*
            r13 = this;
            boolean r14 = r15 instanceof java.lang.Object[]
            if (r14 == 0) goto Lcd
            java.lang.Object[] r15 = (java.lang.Object[]) r15
            java.lang.Object[] r15 = (java.lang.Object[]) r15
            r14 = 0
            r0 = r15[r14]
            io.dcloud.common.DHInterface.IApp r0 = (io.dcloud.common.DHInterface.IApp) r0
            int r1 = r15.length
            r2 = 2
            r3 = 3
            if (r1 < r3) goto L1b
            r1 = r15[r2]
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            goto L1c
        L1b:
            r1 = r14
        L1c:
            java.lang.String r4 = r0.obtainAppId()
            java.util.HashMap<java.lang.String, io.dcloud.common.b.b.c> r5 = r13.a
            java.lang.Object r5 = r5.get(r4)
            io.dcloud.common.b.b.c r5 = (io.dcloud.common.b.b.c) r5
            io.dcloud.common.b.b.d r6 = r5.b
            r7 = 1
            if (r6 != 0) goto L2f
            r8 = r7
            goto L30
        L2f:
            r8 = r14
        L30:
            r9 = 0
            if (r6 != 0) goto L80
            android.content.Intent r6 = r0.obtainWebAppIntent()
            java.lang.String r10 = "__from_stream_open_style__"
            java.lang.String r6 = r6.getStringExtra(r10)
            boolean r11 = android.text.TextUtils.isEmpty(r6)     // Catch: org.json.JSONException -> L59
            if (r11 != 0) goto L52
            org.json.JSONObject r11 = new org.json.JSONObject     // Catch: org.json.JSONException -> L59
            r11.<init>(r6)     // Catch: org.json.JSONException -> L59
            android.content.Intent r6 = r0.obtainWebAppIntent()     // Catch: org.json.JSONException -> L50
            r6.removeExtra(r10)     // Catch: org.json.JSONException -> L50
            goto L5e
        L50:
            r6 = move-exception
            goto L5b
        L52:
            java.lang.String r6 = "{}"
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.createJSONObject(r6)     // Catch: org.json.JSONException -> L59
            goto L5e
        L59:
            r6 = move-exception
            r11 = r9
        L5b:
            r6.printStackTrace()
        L5e:
            io.dcloud.common.DHInterface.IMgr$MgrType r6 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            r10 = 4
            java.lang.Object[] r10 = new java.lang.Object[r10]
            java.lang.Integer r12 = java.lang.Integer.valueOf(r3)
            r10[r14] = r12
            r10[r7] = r0
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r12 = r15[r7]
            r0[r14] = r12
            r0[r7] = r11
            r10[r2] = r0
            r10[r3] = r5
            java.lang.Object r0 = r13.processEvent(r6, r3, r10)
            r6 = r0
            io.dcloud.common.b.b.d r6 = (io.dcloud.common.b.b.d) r6
            r5.b = r6
        L80:
            io.dcloud.common.DHInterface.IWebview r0 = r6.obtainWebView()
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 10
            if (r2 <= r3) goto L9b
            if (r1 != 0) goto L94
            android.webkit.WebView r14 = r0.obtainWebview()
            r14.setLayerType(r7, r9)
            goto L9b
        L94:
            android.webkit.WebView r1 = r0.obtainWebview()
            r1.setLayerType(r14, r9)
        L9b:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r1 = "load "
            java.lang.StringBuilder r14 = r14.append(r1)
            java.lang.StringBuilder r14 = r14.append(r4)
            java.lang.String r1 = " launchPage ="
            java.lang.StringBuilder r14 = r14.append(r1)
            r1 = r15[r7]
            java.lang.StringBuilder r14 = r14.append(r1)
            java.lang.String r14 = r14.toString()
            java.lang.String r1 = "Main_Path"
            io.dcloud.common.adapter.util.Logger.d(r1, r14)
            r14 = r15[r7]
            java.lang.String r14 = java.lang.String.valueOf(r14)
            r0.loadUrl(r14)
            if (r8 == 0) goto Lcd
            r5.e(r6)
        Lcd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.l.a(int, java.lang.Object):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00ee  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x015a  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02d2  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x02da  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x02d5  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x02bd  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x00d6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void b(final int r26, java.lang.Object r27) {
        /*
            Method dump skipped, instructions count: 755
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.l.b(int, java.lang.Object):void");
    }

    void a(IApp iApp, IWebview iWebview) {
        if (iApp.obtainThridInfo(IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData) != null) {
            processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{iWebview, IFeature.F_UI, "n_createSecondWebview", null});
        }
    }

    public void a(final IWebview iWebview, final IApp iApp, final DHAppRootView cVar, final int i, final DHFrameView dVar, final int i2) {
        if (cVar.h) {
            return;
        }
        try {
            if (!iWebview.checkWhite("top")) {
                iApp.setConfigProperty("timeout", "-1");
                cVar.a(cVar, dVar, i2, true);
            } else {
                MessageHandler.postDelayed(new Runnable() { // from class: io.dcloud.common.b.b.l.10
                    @Override // java.lang.Runnable
                    public void run() {
                        l.this.a(iWebview, iApp, cVar, i, dVar, i2);
                    }
                }, 100L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00ee  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0222  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x00bb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    DHFrameView a(int r26, final io.dcloud.common.DHInterface.IApp r27, final DHAppRootView r28, DHFrameView r29, io.dcloud.common.DHInterface.IEventCallback r30, java.lang.Object[] r31) {
        /*
            Method dump skipped, instructions count: 761
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.l.a(int, io.dcloud.common.DHInterface.IApp, io.dcloud.common.b.b.c, io.dcloud.common.b.b.d, io.dcloud.common.DHInterface.IEventCallback, java.lang.Object[]):io.dcloud.common.b.b.d");
    }

    DHFrameView a(int i, IApp iApp, DHAppRootView cVar, DHFrameView dVar, IEventCallback iEventCallback, Object[] objArr, Object obj, float f) {
        DHFrameView dVar2 = new DHFrameView(iApp.getActivity(), this, iApp, cVar, i, obj);
        iApp.getInt(0);
        iApp.getInt(1);
        ViewOptions obtainFrameOptions = dVar2.obtainFrameOptions();
        obtainFrameOptions.setParentViewRect(cVar.obtainFrameOptions());
        dVar2.addFrameViewListener(iEventCallback);
        dVar2.setFrameOptions_Birth(ViewOptions.createViewOptionsData(obtainFrameOptions, cVar.obtainFrameOptions()));
        obtainFrameOptions.mWebviewScale = f;
        return dVar2;
    }

    private DHFrameView a() {
        DHAppRootView b = b();
        if (b != null) {
            return b.a();
        }
        return null;
    }

    private DHAppRootView b() {
        return this.a.get(String.valueOf(processEvent(IMgr.MgrType.AppMgr, 11, null)));
    }

    @Override // io.dcloud.common.DHInterface.AbsMgr
    public void dispose() {
        try {
            Iterator<String> it = this.a.keySet().iterator();
            while (it.hasNext()) {
                this.a.get(it.next()).dispose();
            }
            this.a.clear();
            if (BaseInfo.ISDEBUG) {
                g.a();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(DHFrameView dVar) {
        IApp obtainApp = dVar.obtainApp();
        obtainApp.setMaskLayer(true);
        obtainApp.obtainWebAppRootView().obtainMainView().invalidate();
    }

    public void b(DHFrameView dVar) {
        IApp obtainApp = dVar.obtainApp();
        obtainApp.setMaskLayer(false);
        obtainApp.obtainWebAppRootView().obtainMainView().invalidate();
    }

    /* compiled from: WindowMgr.java */
    /* loaded from: classes.dex */
    class a extends View {
        public a(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
    }

    public void c(DHFrameView dVar) {
        dVar.m();
        dVar.l.d(dVar);
        if (dVar.a()) {
            processEvent(IMgr.MgrType.WindowMgr, 28, dVar.b);
            dVar.b = null;
        }
        dVar.makeViewOptions_animate();
        dVar.k();
        dVar.l();
    }

    public void d(DHFrameView dVar) {
        dVar.m();
        dVar.l.d(dVar);
        if (dVar.a()) {
            processEvent(IMgr.MgrType.WindowMgr, 28, dVar.b);
            dVar.b = null;
        }
        dVar.p();
        dVar.g();
        dVar.h = false;
        dVar.g = false;
        dVar.f = false;
    }

    private void e(DHFrameView dVar) {
        if (dVar.obtainFrameOptions().navigationbar != null) {
            JSONObject jSONObject = dVar.obtainFrameOptions().navigationbar;
            try {
                if (!jSONObject.has("titletext") || TextUtils.isEmpty(jSONObject.optString("titletext"))) {
                    jSONObject.put("titletext", dVar.obtainWebView().obtainFrameView().obtainApp().obtainAppName());
                }
                if (!jSONObject.has("titlecolor") || TextUtils.isEmpty(jSONObject.optString("titlecolor"))) {
                    jSONObject.put("titlecolor", "#FFFFFF");
                }
                if (!jSONObject.has("backgroundcolor") || TextUtils.isEmpty(jSONObject.optString("backgroundcolor"))) {
                    jSONObject.put("backgroundcolor", "#1b1a1f");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String optString = jSONObject.optString("titletext");
            String optString2 = jSONObject.optString("titlecolor");
            String optString3 = jSONObject.optString("backgroundcolor");
            String valueOf = String.valueOf(dVar.obtainWebView().obtainWebview().hashCode());
            Object processEvent = dVar.j.processEvent(IMgr.MgrType.FeatureMgr, 10, new Object[]{dVar.obtainWebView(), "nativeobj", "View", new Object[]{dVar, dVar.obtainWebView(), valueOf, valueOf, JSONUtil.createJSONObject("{'top':'0px','left':'0px','height':'44px','width':'100%','backgroudColor':'" + optString3 + "','position':'dock','dock':'top'}"), null}});
            if (processEvent == null || !(processEvent instanceof View)) {
                return;
            }
            ((View) processEvent).setTag("NavigationBar");
            dVar.j.processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{dVar.obtainWebView(), "nativeobj", "drawText", JSONUtil.createJSONArray("['" + valueOf + "','" + valueOf + "','" + optString + "',{'top':'0px','left':'0px','width':'100%','height':'100%'},{'size':'16px','color':'" + optString2 + "'},'" + valueOf + "',null]")});
            dVar.j.processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{dVar.obtainWebView(), "nativeobj", AbsoluteConst.EVENTS_WEBVIEW_SHOW, JSONUtil.createJSONArray("['" + valueOf + "','" + valueOf + "']")});
            dVar.j.processEvent(IMgr.MgrType.FeatureMgr, 10, new Object[]{dVar.obtainWebView(), "nativeobj", "addNativeView", new Object[]{dVar, valueOf}});
            dVar.mNavigationBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42.0f, dVar.obtainWebView().obtainWebview().getContext().getResources().getDisplayMetrics());
        }
    }
}
