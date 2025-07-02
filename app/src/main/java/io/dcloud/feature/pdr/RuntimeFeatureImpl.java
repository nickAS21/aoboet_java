package io.dcloud.feature.pdr;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.PandoraEntry;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class RuntimeFeatureImpl implements IFeature, MessageHandler.IMessages {
    final String a = PandoraEntry.class.getName();
    AbsMgr b = null;

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        MessageHandler.sendMessage(this, new Object[]{iWebview, str, strArr});
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.b = absMgr;
    }

    /* JADX WARN: Type inference failed for: r2v5, types: [io.dcloud.feature.pdr.RuntimeFeatureImpl$1] */
    @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
    public void execute(Object obj) {
        Object[] objArr = (Object[]) obj;
        final IWebview iWebview = (IWebview) objArr[0];
        String valueOf = String.valueOf(objArr[1]);
        final String[] strArr = (String[]) objArr[2];
        IApp obtainApp = iWebview.obtainFrameView().obtainApp();
        if ("restart".equals(valueOf)) {
            this.b.processEvent(IMgr.MgrType.AppMgr, 3, obtainApp.obtainAppId());
            return;
        }
        if ("install".equals(valueOf)) {
            final String convert2AbsFullPath = obtainApp.convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[0]);
            a(convert2AbsFullPath, obtainApp);
            new Thread() { // from class: io.dcloud.feature.pdr.RuntimeFeatureImpl.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    String[] strArr2 = strArr;
                    String str = strArr2[1];
                    Object[] objArr2 = (Object[]) RuntimeFeatureImpl.this.b.processEvent(IMgr.MgrType.AppMgr, 4, new Object[]{convert2AbsFullPath, !PdrUtil.isEmpty(strArr2[2]) ? JSONUtil.createJSONObject(strArr[2]) : null, iWebview});
                    boolean booleanValue = Boolean.valueOf(String.valueOf(objArr2[0])).booleanValue();
                    String valueOf2 = String.valueOf(objArr2[1]);
                    if (booleanValue) {
                        JSUtil.execCallback(iWebview, str, valueOf2, JSUtil.ERROR, true, false);
                    } else {
                        JSUtil.execCallback(iWebview, str, valueOf2, JSUtil.OK, true, false);
                    }
                }
            }.start();
            return;
        }
        String str = null;
        if ("getProperty".equals(valueOf)) {
            String str2 = strArr[0];
            if (PdrUtil.isEmpty(str2)) {
                str2 = iWebview.obtainFrameView().obtainApp().obtainAppId();
            }
            String str3 = strArr[1];
            String valueOf2 = String.valueOf(this.b.processEvent(IMgr.MgrType.AppMgr, 5, str2));
            if (!PdrUtil.isEmpty(valueOf2)) {
                JSUtil.excCallbackSuccess(iWebview, str3, valueOf2, true);
                return;
            } else {
                JSUtil.excCallbackError(iWebview, str3, null);
                return;
            }
        }
        if ("quit".equals(valueOf)) {
            this.b.processEvent(IMgr.MgrType.WindowMgr, 20, obtainApp);
            return;
        }
        if ("setBadgeNumber".equals(valueOf)) {
            a(iWebview, strArr[0]);
            return;
        }
        if ("openURL".equals(valueOf)) {
            try {
                b(strArr[0], obtainApp);
                PlatformUtil.openURL(iWebview.getActivity(), strArr[0], String.valueOf(PdrUtil.getObject(strArr, 2)));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                JSUtil.excCallbackError(iWebview, strArr[1], e.getMessage());
                return;
            }
        }
        if ("launchApplication".equals(valueOf)) {
            try {
                JSONObject jSONObject = new JSONObject(strArr[0]);
                JSONArray names = jSONObject.names();
                HashMap hashMap = new HashMap();
                String str4 = null;
                for (int i = 0; i < names.length(); i++) {
                    String string = names.getString(i);
                    if (string.equals("pname")) {
                        str = jSONObject.getString(string);
                    } else if (string.equals("action")) {
                        str4 = jSONObject.getString(string);
                    } else if (string.equals("extra")) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject(string);
                        Iterator<String> keys = jSONObject2.keys();
                        while (keys.hasNext()) {
                            String next = keys.next();
                            hashMap.put(next, jSONObject2.get(next));
                        }
                    }
                }
                PlatformUtil.launchApplication(iWebview.getActivity(), str, str4, hashMap);
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                JSUtil.excCallbackError(iWebview, strArr[1], e2.getMessage());
                return;
            }
        }
        if ("openFile".equals(valueOf)) {
            String checkPrivateDirAndCopy2Temp = obtainApp.checkPrivateDirAndCopy2Temp(strArr[0]);
            final String str5 = strArr[2];
            try {
                str = new JSONObject(strArr[1]).optString("pname");
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            String convert2AbsFullPath2 = obtainApp.convert2AbsFullPath(iWebview.obtainFullUrl(), checkPrivateDirAndCopy2Temp);
            if (new File(convert2AbsFullPath2).isFile()) {
                PlatformUtil.openFileBySystem(obtainApp.getActivity(), convert2AbsFullPath2, str, new ICallBack() { // from class: io.dcloud.feature.pdr.RuntimeFeatureImpl.2
                    @Override // io.dcloud.common.DHInterface.ICallBack
                    public Object onCallBack(int i2, Object obj2) {
                        JSUtil.excCallbackError(iWebview, str5, String.valueOf(obj2), true);
                        return null;
                    }
                });
            } else {
                JSUtil.excCallbackError(iWebview, str5, DOMException.MSG_NOT_FOUND_FILE, false);
            }
        }
    }

    private void a(IWebview iWebview, String str) {
        String valueOf = (TextUtils.isEmpty(str) || str.equals("0")) ? "" : String.valueOf(Math.max(0, Math.min(Integer.valueOf(str).intValue(), 99)));
        try {
            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                b(iWebview, valueOf);
            } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                c(iWebview, valueOf);
            } else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
                d(iWebview, valueOf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void b(IWebview iWebview, String str) {
        try {
            Object newInstance = Class.forName("android.app.MiuiNotification").newInstance();
            Field declaredField = newInstance.getClass().getDeclaredField("messageCount");
            declaredField.setAccessible(true);
            declaredField.set(newInstance, str);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            intent.putExtra("android.intent.extra.update_application_component_name", iWebview.getContext().getPackageName() + "/" + this.a);
            intent.putExtra("android.intent.extra.update_application_message_text", str);
            iWebview.getActivity().sendBroadcast(intent);
        }
    }

    private void c(IWebview iWebview, String str) {
        boolean z = !"0".equals(str);
        Intent intent = new Intent();
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", z);
        intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", this.a);
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", str);
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", iWebview.getContext().getPackageName());
        iWebview.getActivity().sendBroadcast(intent);
    }

    private void d(IWebview iWebview, String str) {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", str);
        intent.putExtra("badge_count_package_name", iWebview.getContext().getPackageName());
        intent.putExtra("badge_count_class_name", this.a);
        iWebview.getActivity().sendBroadcast(intent);
    }

    private void a(String str, IApp iApp) {
        if (TextUtils.isEmpty(str) || !BaseInfo.ISAMU) {
            return;
        }
        int length = str.length();
        if ((length - str.indexOf(".apk")) - 4 == 0 || (length - str.indexOf(".wgt")) - 4 == 0 || (length - str.indexOf(".wgtu")) - 5 == 0) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("type", "install");
                jSONObject.put("file", str);
                jSONObject.put("appid", iApp.obtainOriginalAppId());
                jSONObject.put("version", iApp.obtainAppVersionName());
                Log.i(AbsoluteConst.HBUILDER_TAG, jSONObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void b(String str, IApp iApp) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "openurl");
            jSONObject.put("url", str);
            jSONObject.put("appid", iApp.obtainOriginalAppId());
            jSONObject.put("version", iApp.obtainAppVersionName());
            Log.i(AbsoluteConst.HBUILDER_TAG, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
