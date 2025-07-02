package io.dcloud.feature.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.WebLoadEvent;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.animation.ValueAnimator;
import io.dcloud.nineoldandroids.view.ViewHelper;

/* compiled from: UIWidgetMgr.java */
/* loaded from: classes.dex  old e*/
public class UIWidgetMgr {
    private static HashMap<String, String> e;
    AbsMgr a;
    HashMap<String, AppWidgetMgr> b = new HashMap<>(1);
    final boolean c = false;
    String d;

    /* compiled from: UIWidgetMgr.java */
    /* loaded from: classes.dex */
    private enum WidgetCommand {
        findWindowByName,
        enumWindow,
        getLaunchWebview,
        getWapLaunchWebview,
        currentWebview,
        getTopWebview,
        createView,
        setcallbackid,
        debug,
        setLogs,
        isLogs,
        defaultHardwareAccelerated,
        startAnimation,
        getSecondWebview,
        getDisplayWebview
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UIWidgetMgr(AbsMgr absMgr, String str) {
        this.a = null;
        this.d = null;
        this.a = absMgr;
        this.d = str;
        a();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Can't wrap try/catch for region: R(16:10|(10:15|(2:17|(1:19))(2:193|(1:197))|20|21|22|23|24|(2:26|27)(2:134|(3:136|(1:138)(2:139|(1:141)(1:142))|133)(3:143|(2:145|(8:147|(1:149)(1:158)|150|(2:153|151)|154|155|(1:157)|133)(1:159))(4:161|162|163|(3:165|(6:167|(1:171)|172|(1:174)|175|(3:177|(1:179)(1:181)|180)(2:182|(1:184)(1:185)))|133)(1:186))|160))|29|30)|202|(1:204)(1:213)|205|(1:207)(1:212)|208|(1:211)|20|21|22|23|24|(0)(0)|29|30) */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x0646, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x0647, code lost:
    
        r10 = r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x064e, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x064f, code lost:
    
        io.dcloud.common.adapter.util.Logger.e("UIWidgetMgr", "pActionName=" + r10 + ";pJsArgs=" + r31);
        r1.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x00ed, code lost:
    
        r11 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0080, code lost:
    
        if (r12.e.obtainAppStatus() != 1) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0082, code lost:
    
        r28.b.remove(r4);
     */
    /* JADX WARN: Removed duplicated region for block: B:134:0x03de A[Catch: Exception -> 0x0646, all -> 0x064a, TryCatch #3 {Exception -> 0x0646, blocks: (B:26:0x00f7, B:32:0x010b, B:51:0x01dc, B:54:0x0213, B:58:0x023a, B:60:0x0276, B:62:0x0286, B:63:0x0222, B:65:0x022a, B:66:0x022f, B:68:0x0233, B:86:0x029a, B:88:0x02a2, B:90:0x02ad, B:92:0x02b7, B:96:0x02c1, B:98:0x02c7, B:108:0x02fa, B:109:0x02ff, B:111:0x030b, B:112:0x0312, B:113:0x033f, B:115:0x0349, B:116:0x034f, B:117:0x037d, B:119:0x0383, B:120:0x0389, B:122:0x038f, B:123:0x0395, B:125:0x039b, B:126:0x03a1, B:127:0x03a7, B:129:0x03c6, B:130:0x03cc, B:132:0x03d8, B:134:0x03de, B:136:0x03e6, B:138:0x03ee, B:139:0x03fc, B:141:0x0403, B:142:0x0409, B:143:0x043f, B:145:0x0447, B:147:0x044f, B:149:0x0460, B:150:0x046a, B:151:0x0487, B:153:0x048d, B:155:0x04bc, B:157:0x051e, B:159:0x0523), top: B:24:0x00f5 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00f7 A[Catch: Exception -> 0x0646, all -> 0x064a, TRY_ENTER, TRY_LEAVE, TryCatch #3 {Exception -> 0x0646, blocks: (B:26:0x00f7, B:32:0x010b, B:51:0x01dc, B:54:0x0213, B:58:0x023a, B:60:0x0276, B:62:0x0286, B:63:0x0222, B:65:0x022a, B:66:0x022f, B:68:0x0233, B:86:0x029a, B:88:0x02a2, B:90:0x02ad, B:92:0x02b7, B:96:0x02c1, B:98:0x02c7, B:108:0x02fa, B:109:0x02ff, B:111:0x030b, B:112:0x0312, B:113:0x033f, B:115:0x0349, B:116:0x034f, B:117:0x037d, B:119:0x0383, B:120:0x0389, B:122:0x038f, B:123:0x0395, B:125:0x039b, B:126:0x03a1, B:127:0x03a7, B:129:0x03c6, B:130:0x03cc, B:132:0x03d8, B:134:0x03de, B:136:0x03e6, B:138:0x03ee, B:139:0x03fc, B:141:0x0403, B:142:0x0409, B:143:0x043f, B:145:0x0447, B:147:0x044f, B:149:0x0460, B:150:0x046a, B:151:0x0487, B:153:0x048d, B:155:0x04bc, B:157:0x051e, B:159:0x0523), top: B:24:0x00f5 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0276 A[Catch: Exception -> 0x0646, all -> 0x064a, TryCatch #3 {Exception -> 0x0646, blocks: (B:26:0x00f7, B:32:0x010b, B:51:0x01dc, B:54:0x0213, B:58:0x023a, B:60:0x0276, B:62:0x0286, B:63:0x0222, B:65:0x022a, B:66:0x022f, B:68:0x0233, B:86:0x029a, B:88:0x02a2, B:90:0x02ad, B:92:0x02b7, B:96:0x02c1, B:98:0x02c7, B:108:0x02fa, B:109:0x02ff, B:111:0x030b, B:112:0x0312, B:113:0x033f, B:115:0x0349, B:116:0x034f, B:117:0x037d, B:119:0x0383, B:120:0x0389, B:122:0x038f, B:123:0x0395, B:125:0x039b, B:126:0x03a1, B:127:0x03a7, B:129:0x03c6, B:130:0x03cc, B:132:0x03d8, B:134:0x03de, B:136:0x03e6, B:138:0x03ee, B:139:0x03fc, B:141:0x0403, B:142:0x0409, B:143:0x043f, B:145:0x0447, B:147:0x044f, B:149:0x0460, B:150:0x046a, B:151:0x0487, B:153:0x048d, B:155:0x04bc, B:157:0x051e, B:159:0x0523), top: B:24:0x00f5 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0286 A[Catch: Exception -> 0x0646, all -> 0x064a, TryCatch #3 {Exception -> 0x0646, blocks: (B:26:0x00f7, B:32:0x010b, B:51:0x01dc, B:54:0x0213, B:58:0x023a, B:60:0x0276, B:62:0x0286, B:63:0x0222, B:65:0x022a, B:66:0x022f, B:68:0x0233, B:86:0x029a, B:88:0x02a2, B:90:0x02ad, B:92:0x02b7, B:96:0x02c1, B:98:0x02c7, B:108:0x02fa, B:109:0x02ff, B:111:0x030b, B:112:0x0312, B:113:0x033f, B:115:0x0349, B:116:0x034f, B:117:0x037d, B:119:0x0383, B:120:0x0389, B:122:0x038f, B:123:0x0395, B:125:0x039b, B:126:0x03a1, B:127:0x03a7, B:129:0x03c6, B:130:0x03cc, B:132:0x03d8, B:134:0x03de, B:136:0x03e6, B:138:0x03ee, B:139:0x03fc, B:141:0x0403, B:142:0x0409, B:143:0x043f, B:145:0x0447, B:147:0x044f, B:149:0x0460, B:150:0x046a, B:151:0x0487, B:153:0x048d, B:155:0x04bc, B:157:0x051e, B:159:0x0523), top: B:24:0x00f5 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized java.lang.String a(io.dcloud.common.DHInterface.IWebview r29, java.lang.String r30, org.json.JSONArray r31) {
        /*
            Method dump skipped, instructions count: 1690
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.e.a(io.dcloud.common.DHInterface.IWebview, java.lang.String, org.json.JSONArray):java.lang.String");
    }

    /* compiled from: UIWidgetMgr.java */
    /* renamed from: io.dcloud.feature.ui.e$4, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[WidgetCommand.values().length];
            a = iArr;
            try {
                iArr[WidgetCommand.findWindowByName.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[WidgetCommand.getTopWebview.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[WidgetCommand.enumWindow.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[WidgetCommand.getWapLaunchWebview.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[WidgetCommand.getLaunchWebview.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[WidgetCommand.getSecondWebview.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[WidgetCommand.currentWebview.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[WidgetCommand.createView.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                a[WidgetCommand.setcallbackid.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[WidgetCommand.debug.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                a[WidgetCommand.defaultHardwareAccelerated.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                a[WidgetCommand.startAnimation.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                a[WidgetCommand.getDisplayWebview.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0030  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(java.lang.String r13, final AppWidgetMgr r14, io.dcloud.common.DHInterface.IFrameView r15) {
        /*
            r12 = this;
            int r0 = r15.hashCode()
            java.lang.String r0 = java.lang.String.valueOf(r0)
            io.dcloud.common.DHInterface.IWebview r1 = r15.obtainWebView()
            java.lang.String r3 = r1.obtainUrl()
            r7 = 0
            if (r15 == 0) goto L24
            r2 = r15
            io.dcloud.common.adapter.ui.AdaFrameView r2 = (io.dcloud.common.adapter.ui.AdaFrameView) r2
            io.dcloud.common.adapter.util.ViewOptions r4 = r2.obtainFrameOptions()
            if (r4 == 0) goto L24
            io.dcloud.common.adapter.util.ViewOptions r2 = r2.obtainFrameOptions()
            org.json.JSONObject r2 = r2.mJsonViewOption
            r8 = r2
            goto L25
        L24:
            r8 = r7
        L25:
            java.lang.String r1 = r1.obtainFrameId()
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r1)
            r9 = 2
            if (r2 != 0) goto L32
            r10 = r1
            goto L3b
        L32:
            int r1 = r15.getFrameType()
            if (r1 != r9) goto L3a
            r10 = r13
            goto L3b
        L3a:
            r10 = r3
        L3b:
            io.dcloud.feature.ui.c r11 = new io.dcloud.feature.ui.c
            r1 = r11
            r2 = r14
            r4 = r10
            r5 = r0
            r6 = r8
            r1.<init>(r2, r3, r4, r5, r6)
            android.content.Context r2 = r15.getContext()
            io.dcloud.common.DHInterface.IWebview r4 = r15.obtainWebView()
            r3 = r14
            r1.a(r2, r3, r4, r5, r6)
            int r0 = r15.getFrameType()
            r1 = 0
            r2 = 1
            if (r0 != r9) goto L69
            r0 = r15
            io.dcloud.common.adapter.ui.AdaFrameView r0 = (io.dcloud.common.adapter.ui.AdaFrameView) r0
            android.view.View r0 = r0.obtainMainView()
            int r0 = r0.getVisibility()
            if (r0 != 0) goto L67
            goto L69
        L67:
            r0 = r1
            goto L6a
        L69:
            r0 = r2
        L6a:
            r11.B = r0
            r11.E = r2
            r0 = r15
            io.dcloud.common.adapter.ui.AdaFrameView r0 = (io.dcloud.common.adapter.ui.AdaFrameView) r0
            r0.addFrameViewListener(r11)
            r11.a(r15, r10)
            r14.c(r11)
            r14.a(r13, r11, r1)
            io.dcloud.feature.ui.e$1 r13 = new io.dcloud.feature.ui.e$1
            r13.<init>()
            io.dcloud.common.adapter.util.MessageHandler.sendMessage(r13, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.e.a(java.lang.String, io.dcloud.feature.ui.a, io.dcloud.common.DHInterface.IFrameView):void");
    }

    private NWindow a(AppWidgetMgr aVar, IWebview iWebview, JSONArray jSONArray, IApp iApp, String str, boolean z) throws Exception {
        String string;
        JSONObject jSONObject;
        NWindow c = aVar.c(iWebview.obtainFrameView());
        String string2 = jSONArray.getString(0);
        JSONObject optJSONObject = jSONArray.optJSONObject(1);
        String optString = jSONArray.optString(2);
        JSONObject optJSONObject2 = jSONArray.optJSONObject(4);
        JSONArray optJSONArray = jSONArray.optJSONArray(5);
        if (optJSONObject == null) {
            jSONObject = new JSONObject("{}");
            string = "";
        } else {
            string = JSONUtil.getString(optJSONObject, "name");
            jSONObject = optJSONObject;
        }
        NWindow a2 = a(aVar, iWebview, iApp, string2, string, str, jSONObject, optJSONObject2, optJSONArray, z);
        c.a(a2);
        if (optString != null) {
            a2.b.put(iWebview, optString);
        }
        AnimOptions animOptions = ((AdaFrameItem) a2.u).getAnimOptions();
        ViewOptions obtainFrameOptions = ((AdaFrameItem) a2.u).obtainFrameOptions();
        a2.G = obtainFrameOptions.hasBackground();
        animOptions.parseTransition(obtainFrameOptions.transition);
        animOptions.parseTransform(obtainFrameOptions.transform);
        return a2;
    }

    private NWindow a(AppWidgetMgr aVar, IWebview iWebview, IApp iApp, String str, String str2, String str3, JSONObject jSONObject, JSONObject jSONObject2, JSONArray jSONArray, boolean z) {
        String str4;
        String str5;
        AppWidgetMgr aVar2;
        String str6;
        String str7;
        String convert2WebviewFullPath = iApp.convert2WebviewFullPath(iWebview.obtainFullUrl(), str);
        if (z) {
            str5 = convert2WebviewFullPath;
            str4 = null;
        } else {
            str4 = convert2WebviewFullPath;
            str5 = null;
        }
        iApp.obtainWebviewBaseUrl();
        a(iWebview, iApp, str4);
        String obtainAppId = iApp.obtainAppId();
        boolean z2 = !PdrUtil.isEmpty(str);
        String str8 = str5;
        NWindow cVar = new NWindow(aVar, str, str2, str3, jSONObject);
        cVar.t = jSONObject2;
        IFrameView iFrameView = (IFrameView) this.a.processEvent(IMgr.MgrType.WindowMgr, 3, new Object[]{Integer.valueOf(jSONObject.optInt("winType", 0)), iApp, new Object[]{str, jSONObject}, iWebview.obtainFrameView(), cVar});
        if (z) {
            iFrameView.obtainWebView().setOriginalUrl(str8);
        }
        if (jSONArray != null) {
            cVar.r = jSONArray;
            cVar.s = iWebview;
        }
        if (jSONObject != null && jSONObject.has(AbsoluteConst.JSON_KEY_RENDER)) {
            iFrameView.setNeedRender(PdrUtil.isEquals(jSONObject.optString(AbsoluteConst.JSON_KEY_RENDER, "onscreen"), "always"));
        }
        cVar.a(iFrameView, str2);
        ViewOptions obtainFrameOptions = ((AdaFrameItem) cVar.u).obtainFrameOptions();
        if (!TextUtils.isEmpty(obtainFrameOptions.errorPage) && !URLUtil.isNetworkUrl(obtainFrameOptions.errorPage)) {
            if (!"none".equals(obtainFrameOptions.errorPage)) {
                if (!new File(iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), obtainFrameOptions.errorPage)).exists()) {
                    String obtainConfigProperty = iWebview.obtainApp().obtainConfigProperty("error");
                    if (!"none".equals(obtainConfigProperty)) {
                        obtainFrameOptions.errorPage = iWebview.obtainApp().convert2WebviewFullPath(null, obtainConfigProperty);
                    } else {
                        obtainFrameOptions.errorPage = "none";
                    }
                } else {
                    obtainFrameOptions.errorPage = iWebview.obtainApp().convert2WebviewFullPath(iWebview.obtainFullUrl(), obtainFrameOptions.errorPage);
                }
            }
        } else {
            String obtainConfigProperty2 = iWebview.obtainApp().obtainConfigProperty("error");
            if (!"none".equals(obtainConfigProperty2)) {
                obtainFrameOptions.errorPage = iWebview.obtainApp().convert2WebviewFullPath(null, obtainConfigProperty2);
            } else {
                obtainFrameOptions.errorPage = "none";
            }
        }
        IWebview obtainWebView = cVar.u.obtainWebView();
        if (jSONObject.has("plusrequire")) {
            obtainWebView.setWebviewProperty("plusrequire", jSONObject.optString("plusrequire"));
        }
        if (jSONObject.has("geolocation")) {
            obtainWebView.setWebviewProperty("geolocation", jSONObject.optString("geolocation"));
        }
        if (jSONObject.has("injection")) {
            obtainWebView.setWebviewProperty("injection", String.valueOf(jSONObject.optBoolean("injection")));
        }
        if (jSONObject.has(IApp.ConfigProperty.CONFIG_OVERRIDE_RESOURCE)) {
            obtainWebView.setOverrideResourceRequest(jSONObject.optJSONArray(IApp.ConfigProperty.CONFIG_OVERRIDE_RESOURCE));
        }
        if (jSONObject.has(IApp.ConfigProperty.CONFIG_OVERRIDEURL)) {
            obtainWebView.setOverrideUrlLoadingData(jSONObject.optJSONObject(IApp.ConfigProperty.CONFIG_OVERRIDEURL));
        }
        if (BaseInfo.isWap2AppAppid(obtainAppId) && iApp.isStreamApp() && obtainWebView.obtainFrameView().getFrameType() == 4) {
            if (obtainWebView.getWebviewProperty("plusrequire").equals("none")) {
                str7 = null;
            } else {
                str7 = null;
                obtainWebView.appendPreloadJsFile(iApp.convert2LocalFullPath(null, "_www/__wap2app.js"));
                obtainWebView.appendPreloadJsFile(iApp.convert2LocalFullPath(null, "_www/__wap2appconfig.js"));
            }
            String convert2LocalFullPath = iApp.convert2LocalFullPath(str7, "_www/__wap2app.css");
            if (new File(convert2LocalFullPath).exists()) {
                obtainWebView.setCssFile(convert2LocalFullPath, str7);
            }
        }
        if (z2) {
            aVar2 = aVar;
            str6 = obtainAppId;
            boolean z3 = !PdrUtil.isNetPath(str4) && aVar2.b(str6) && aVar2.d(str4);
            Logger.d("willDownload=" + z3 + ";" + str4);
            if (z3) {
                aVar2.a(cVar, str4);
            } else {
                cVar.u.obtainWebView().loadUrl(str4);
            }
        } else {
            aVar2 = aVar;
            str6 = obtainAppId;
        }
        cVar.a(iWebview.getContext(), aVar, iFrameView.obtainWebView(), str3, jSONObject);
        iFrameView.obtainMainView().setVisibility(View.INVISIBLE);
        if (DeviceInfo.sDeviceSdkVer >= 11) {
            aVar2.a(iFrameView);
        }
        aVar2.c(cVar);
        cVar.a(jSONObject, false);
        Logger.d(Logger.VIEW_VISIBLE_TAG, str6 + " createNWindow webview_name=" + str2);
        return cVar;
    }

    private void a(IWebview iWebview, IApp iApp, String str) {
        if (!BaseInfo.isBase(iWebview.getContext()) || TextUtils.isEmpty(str)) {
            return;
        }
        String obtainUrl = iWebview.obtainUrl();
        if (str.startsWith(DeviceInfo.HTTP_PROTOCOL) || obtainUrl.startsWith(DeviceInfo.HTTP_PROTOCOL) || str.startsWith(DeviceInfo.HTTPS_PROTOCOL) || obtainUrl.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
            return;
        }
        Log.i(AbsoluteConst.HBUILDER_TAG, String.format(AbsoluteConst.OPENLOG, c(WebLoadEvent.getHBuilderPrintUrl(iApp.convert2RelPath(WebLoadEvent.getOriginalUrl(obtainUrl)))), c(WebLoadEvent.getHBuilderPrintUrl(iApp.convert2RelPath(WebLoadEvent.getOriginalUrl(str))))));
    }

    public static NView a(String str) {
        if (PdrUtil.isEmpty(str)) {
            return null;
        }
        try {
            Object newInstance = Class.forName(e.get(str.toLowerCase())).newInstance();
            if (newInstance instanceof NView) {
                return (NView) newInstance;
            }
            return null;
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return null;
        } catch (InstantiationException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    private void a() {
        e = (HashMap) this.a.processEvent(IMgr.MgrType.FeatureMgr, 4, this.d);
    }

    public void b(String str) {
        if (PdrUtil.isEmpty(str)) {
            Iterator<AppWidgetMgr> it = this.b.values().iterator();
            while (it.hasNext()) {
                it.next().a();
            }
            this.b.clear();
            return;
        }
        AppWidgetMgr aVar = this.b.get(str);
        if (aVar != null) {
            Logger.d(Logger.MAIN_TAG, "UIWidgetMgr.dispose pAppid=" + str);
            aVar.a();
        }
        this.b.remove(str);
    }

    public static String c(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.startsWith("./")) {
            return str.substring(2);
        }
        if (str.startsWith("../")) {
            return str.substring(3);
        }
        return str.startsWith(".../") ? str.substring(4) : str;
    }

    private ValueAnimator a(final View view, int i, int i2, final String str, final IWebview iWebview, final String str2, final NWindow cVar) {
        ValueAnimator ofFloat;
        if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
            ofFloat = ValueAnimator.ofInt(i, i2);
        } else {
            ofFloat = view.getLayoutParams() instanceof FrameLayout.LayoutParams ? ValueAnimator.ofFloat(i, i2) : null;
        }
        ofFloat.setDuration(200L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: io.dcloud.feature.ui.e.2
            @Override // io.dcloud.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
                    AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.height = view.getHeight();
                    layoutParams.width = view.getWidth();
                    layoutParams.x = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    view.requestLayout();
                    return;
                }
                if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                    ViewHelper.setX(view, ((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            }
        });
        ofFloat.addListener(new Animator.AnimatorListener() { // from class: io.dcloud.feature.ui.e.3
            @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (iWebview != null && !TextUtils.isEmpty(str2)) {
                    String l = cVar.l();
                    if (TextUtils.isEmpty(l)) {
                        l = "";
                    }
                    JSUtil.execCallback(iWebview, str2, String.format("{\"id\":\"%s\",\"target\":%s}", l, cVar.d()), JSUtil.OK, true, true);
                }
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                if (AbsoluteConst.EVENTS_WEBVIEW_HIDE.equals(str)) {
                    NWindow cVar2 = cVar;
                    cVar2.a(cVar2.k(), AbsoluteConst.EVENTS_WEBVIEW_HIDE, JSONUtil.createJSONArray("[null,null,null]"));
                } else if (AbsoluteConst.EVENTS_CLOSE.equals(str)) {
                    NWindow cVar3 = cVar;
                    cVar3.a(cVar3.k(), AbsoluteConst.EVENTS_CLOSE, JSONUtil.createJSONArray("[null,null,null]"));
                }
            }
        });
        return ofFloat;
    }
}
