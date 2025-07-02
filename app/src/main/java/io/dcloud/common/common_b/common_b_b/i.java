package io.dcloud.common.common_b.common_b_b;

import android.app.Dialog;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.util.ErrorDialogUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.TestUtil;

/* compiled from: DHWebview.java */
/* loaded from: classes.dex */
class i implements IJsInterface, IReflectAble {
    static boolean d = false;
    static final Class[] f = {String.class, String.class};
    AbsMgr a;
    IWebview b;
    String c;
    MessageHandler.IMessages e = new MessageHandler.IMessages() { // from class: io.dcloud.common.b.b.i.1
        @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
        public void execute(Object obj) {
            Object[] objArr = (Object[]) obj;
            i.this.exec(String.valueOf(objArr[0]), String.valueOf(objArr[1]), (JSONArray) objArr[2]);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(IWebview iWebview) {
        this.a = null;
        this.b = null;
        this.c = null;
        this.b = iWebview;
        this.c = iWebview.obtainFrameView().obtainApp().obtainAppId();
        this.a = this.b.obtainFrameView().obtainWindowMgr();
    }

    @Override // io.dcloud.common.DHInterface.IJsInterface
    public void forceStop(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.a.processEvent(IMgr.MgrType.WindowMgr, 20, str);
    }

    @Override // io.dcloud.common.DHInterface.IJsInterface
    public String prompt(String str, String str2) {
        if (!d) {
            TestUtil.record("JsInterfaceImpl", Thread.currentThread());
            d = true;
        }
        String str3 = null;
        if (str2 != null && str2.length() > 3 && str2.substring(0, 4).equals("pdr:")) {
            System.currentTimeMillis();
            try {
                JSONArray jSONArray = new JSONArray(str2.substring(4));
                String string = jSONArray.getString(0);
                String string2 = jSONArray.getString(1);
                boolean z = jSONArray.getBoolean(2);
                JSONArray createJSONArray = JSONUtil.createJSONArray(str);
                if (z) {
                    MessageHandler.sendMessage(this.e, new Object[]{string, string2, createJSONArray});
                } else {
                    str3 = exec(string, string2, createJSONArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return str3;
    }

    @Override // io.dcloud.common.DHInterface.IJsInterface
    public String exec(String str, String str2, String str3) {
        return exec(str, str2, JSONUtil.createJSONArray(str3));
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0043 A[Catch: Exception -> 0x00c3, TryCatch #0 {Exception -> 0x00c3, blocks: (B:2:0x0000, B:4:0x002b, B:6:0x0037, B:11:0x0043, B:14:0x005d, B:16:0x0069, B:18:0x006f, B:20:0x0079, B:21:0x0092), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x005d A[Catch: Exception -> 0x00c3, TryCatch #0 {Exception -> 0x00c3, blocks: (B:2:0x0000, B:4:0x002b, B:6:0x0037, B:11:0x0043, B:14:0x005d, B:16:0x0069, B:18:0x006f, B:20:0x0079, B:21:0x0092), top: B:1:0x0000 }] */
    @Override // io.dcloud.common.DHInterface.IJsInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String exec(java.lang.String r9, java.lang.String r10, org.json.JSONArray r11) {
        /*
            r8 = this;
            java.lang.String r0 = r8.c     // Catch: java.lang.Exception -> Lc3
            java.lang.String r9 = r9.toLowerCase()     // Catch: java.lang.Exception -> Lc3
            io.dcloud.common.adapter.util.InvokeExecutorHelper$InvokeExecutor r1 = io.dcloud.common.adapter.util.InvokeExecutorHelper.QihooInnerStatisticUtil     // Catch: java.lang.Exception -> Lc3
            java.lang.String r2 = "statOnEvent"
            java.lang.Class[] r3 = io.dcloud.common.b.b.i.f     // Catch: java.lang.Exception -> Lc3
            r4 = 2
            java.lang.Object[] r5 = new java.lang.Object[r4]     // Catch: java.lang.Exception -> Lc3
            r6 = 0
            r5[r6] = r0     // Catch: java.lang.Exception -> Lc3
            r7 = 1
            r5[r7] = r9     // Catch: java.lang.Exception -> Lc3
            r1.invoke(r2, r3, r5)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r1 = "io.dcloud.HBuilder"
            io.dcloud.common.DHInterface.IWebview r2 = r8.b     // Catch: java.lang.Exception -> Lc3
            android.content.Context r2 = r2.getContext()     // Catch: java.lang.Exception -> Lc3
            java.lang.String r2 = r2.getPackageName()     // Catch: java.lang.Exception -> Lc3
            boolean r1 = r1.equals(r2)     // Catch: java.lang.Exception -> Lc3
            r2 = 3
            if (r1 == 0) goto L40
            io.dcloud.common.DHInterface.IWebview r1 = r8.b     // Catch: java.lang.Exception -> Lc3
            io.dcloud.common.DHInterface.IFrameView r1 = r1.obtainFrameView()     // Catch: java.lang.Exception -> Lc3
            int r1 = r1.getFrameType()     // Catch: java.lang.Exception -> Lc3
            if (r1 == r2) goto L40
            boolean r1 = io.dcloud.common.b.a.a.a(r0, r9)     // Catch: java.lang.Exception -> Lc3
            if (r1 == 0) goto L3e
            goto L40
        L3e:
            r1 = r6
            goto L41
        L40:
            r1 = r7
        L41:
            if (r1 == 0) goto L5d
            io.dcloud.common.DHInterface.AbsMgr r0 = r8.a     // Catch: java.lang.Exception -> Lc3
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch: java.lang.Exception -> Lc3
            r3 = 4
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Exception -> Lc3
            io.dcloud.common.DHInterface.IWebview r5 = r8.b     // Catch: java.lang.Exception -> Lc3
            r3[r6] = r5     // Catch: java.lang.Exception -> Lc3
            r3[r7] = r9     // Catch: java.lang.Exception -> Lc3
            r3[r4] = r10     // Catch: java.lang.Exception -> Lc3
            r3[r2] = r11     // Catch: java.lang.Exception -> Lc3
            java.lang.Object r0 = r0.processEvent(r1, r7, r3)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r9 = java.lang.String.valueOf(r0)     // Catch: java.lang.Exception -> Lc3
            return r9
        L5d:
            io.dcloud.common.DHInterface.IWebview r1 = r8.b     // Catch: java.lang.Exception -> Lc3
            io.dcloud.common.DHInterface.IApp r1 = r1.obtainApp()     // Catch: java.lang.Exception -> Lc3
            boolean r1 = r1.isStreamApp()     // Catch: java.lang.Exception -> Lc3
            if (r1 != 0) goto Lf2
            boolean r1 = io.dcloud.common.b.a.a.b(r0, r9)     // Catch: java.lang.Exception -> Lc3
            if (r1 != 0) goto Lf2
            java.lang.String r1 = io.dcloud.common.b.a.a.c(r9)     // Catch: java.lang.Exception -> Lc3
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r1)     // Catch: java.lang.Exception -> Lc3
            if (r2 == 0) goto L92
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lc3
            r1.<init>()     // Catch: java.lang.Exception -> Lc3
            java.lang.String r2 = "应用未添加"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Exception -> Lc3
            java.lang.StringBuilder r1 = r1.append(r9)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r2 = "权限，请在manifest.json文件中permissions节点进行配置，"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Exception -> Lc3
        L92:
            io.dcloud.common.DHInterface.IWebview r2 = r8.b     // Catch: java.lang.Exception -> Lc3
            r8.a(r2, r9)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r2 = "dhwebview"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lc3
            r3.<init>()     // Catch: java.lang.Exception -> Lc3
            java.lang.String r4 = "_appid="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Exception -> Lc3
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch: java.lang.Exception -> Lc3
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Exception -> Lc3
            io.dcloud.common.adapter.util.Logger.e(r2, r0)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.toJSONableString(r1)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r0 = io.dcloud.common.util.JSUtil.consoleTest(r0)     // Catch: java.lang.Exception -> Lc3
            io.dcloud.common.DHInterface.IWebview r1 = r8.b     // Catch: java.lang.Exception -> Lc3
            r1.executeScript(r0)     // Catch: java.lang.Exception -> Lc3
            java.lang.String r9 = "null"
            return r9
        Lc3:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "JsInterfaceImpl.exec pApiFeatureName="
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r9 = r1.append(r9)
            java.lang.String r1 = ";pActionName="
            java.lang.StringBuilder r9 = r9.append(r1)
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r10 = ";pArgs="
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r10 = java.lang.String.valueOf(r11)
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            io.dcloud.common.adapter.util.Logger.w(r9, r0)
        Lf2:
            r9 = 0
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.i.exec(java.lang.String, java.lang.String, org.json.JSONArray):java.lang.String");
    }

    public void a(IWebview iWebview, String str) {
        Dialog lossDialog = ErrorDialogUtil.getLossDialog(this.b, String.format("manifest.json中未添加%s模块, 请参考 http://ask.dcloud.net.cn/article/283", str), "http://ask.dcloud.net.cn/article/283", str);
        if (lossDialog != null) {
            lossDialog.show();
        }
    }
}
