package io.dcloud.feature;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.BaseFeature;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IBoot;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.common_b.common_b_a.PermissionControler;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DataUtil;
import io.dcloud.common.util.ErrorDialogUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* compiled from: FeatureMgr.java */
/* loaded from: classes.dex old b*/
public class FeatureMgr extends AbsMgr implements IMgr.FeatureEvent {
    HashMap<String, String> a;
    dp b;
    private HashMap<String, HashMap<String, String>> c;
    private HashMap<String, String> d;
    private HashMap<String, IFeature> e;
    private HashMap<String, String> f;
    private HashMap<String, IBoot> g;
    private String h;
    private ArrayList<String> i;

    public FeatureMgr(ICore iCore) {
        super(iCore, "featuremgr", MgrType.FeatureMgr);
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = new ArrayList<>();
        this.a = new HashMap<>();
        this.b = null;
        c();
        try {
            if (BaseInfo.ISDEBUG && DHFile.isExist("/sdcard/dcloud/all.js")) {
                this.h = new String(PlatformUtil.getFileContent("/sdcard/dcloud/all.js", 2));
            } else {
                this.h = new String(PlatformUtil.getFileContent(DCloudAdapterUtil.getRuntimeJsPath(), 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // io.dcloud.common.DHInterface.IMgr
    public Object processEvent(MgrType mgrType, int i, Object obj) {
        Throwable th;
        try {
        } catch (Throwable th1) {
            Logger.w("FeatureMgr.processEvent", th1);
            return null;
        }
        if (!checkMgrId(mgrType)) {
            return this.mCore.dispatchEvent(mgrType, i, obj);
        }
        switch (i) {
            case 0:
                this.e = new HashMap<>(1);
                this.d = new HashMap<>(1);
                this.c = new HashMap<>(1);
                this.g = new HashMap<>();
                this.f = new HashMap<>(1);
                return a();
            case 1:
                return a((Object[]) obj);
            case 2:
                Object[] objArr = (Object[]) obj;
                return a((IApp) objArr[0], (IFrameView) objArr[1]);
            case 3:
                a(String.valueOf(obj));
                return null;
            case 4:
                return this.c.get(String.valueOf(obj));
            case 5:
                String[] strArr = (String[]) obj;
                String str = strArr[0];
                String str2 = strArr[1];
                String str3 = strArr[2];
                if (!PdrUtil.isEmpty(str) && !PdrUtil.isEmpty(str2)) {
                    this.d.put(str.toLowerCase(), str2);
                }
                if (PdrUtil.isEmpty(str3)) {
                    return null;
                }
                this.i.add(str3);
                return null;
            case 6:
            case 7:
            default:
                return null;
            case 8:
                String[] strArr2 = (String[]) obj;
                return PermissionControler.a(strArr2[0], strArr2[1]);
            case 9:
                return Boolean.valueOf(b(String.valueOf(obj)));
            case 10:
                try {
                    Object[] objArr2 = (Object[]) obj;
                    IFeature a = a(String.valueOf(objArr2[1]), ((IWebview) objArr2[0]).getActivity());
                    if (!(a instanceof IWaiter)) {
                        return null;
                    }
                    return ((IWaiter) a).doForFeature(String.valueOf(objArr2[2]), objArr2[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
        }
    }

    private String a(IApp iApp, IFrameView iFrameView) {
        String str = this.a.get(iApp.obtainAppId());
        if (str == null) {
            str = a(iApp, iFrameView.obtainWebView());
            if (Build.VERSION.SDK_INT > 19) {
                this.a.put(iApp.obtainAppId(), str);
            }
        }
        return str;
    }

    public String a(IWebview iWebview) {
        StringBuffer stringBuffer = new StringBuffer();
        String webviewUUID = iWebview.getWebviewUUID();
        if (PdrUtil.isEmpty(webviewUUID)) {
            webviewUUID = String.valueOf(iWebview.obtainFrameView().hashCode());
        }
        stringBuffer.append("window.__HtMl_Id__= '" + webviewUUID + "';");
        if (PdrUtil.isEmpty(iWebview.obtainFrameId())) {
            stringBuffer.append("window.__WebVieW_Id__= undefined;");
        } else {
            stringBuffer.append("window.__WebVieW_Id__= '" + iWebview.obtainFrameId() + "';");
        }
        stringBuffer.append("try{window.plus.__tag__='_plus_all_'}catch(e){}");
        return stringBuffer.toString();
    }

    private String a(IApp iApp, IWebview iWebview) {
        StringBuffer stringBuffer = new StringBuffer(AbsoluteConst.PROTOCOL_JAVASCRIPT);
        stringBuffer.append("function ").append(AbsoluteConst.LOAD_PLUS_FUN_NAME).append("(){try{");
        if (Build.VERSION.SDK_INT <= 19) {
            stringBuffer.append(a(iWebview));
        }
        stringBuffer.append("window._____isDebug_____=" + BaseInfo.ISDEBUG + ";");
        stringBuffer.append("window._____platform_____=1;");
        stringBuffer.append("window._____platform_os_version_____=" + Build.VERSION.SDK_INT + ";");
        stringBuffer.append(this.h);
        if (PermissionControler.a(iApp.obtainAppId(), IFeature.F_DEVICE.toLowerCase())) {
            if (PdrUtil.isEmpty(DeviceInfo.DEVICESTATUS_JS)) {
                try {
                    DeviceInfo.initGsmCdmaCell();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                DeviceInfo.getDevicestatus_js();
            }
            stringBuffer.append(DeviceInfo.DEVICESTATUS_JS);
        }
        stringBuffer.append("window.__NWin_Enable__=" + (BaseInfo.sRuntimeMode == SDK.IntegratedMode.WEBVIEW ? String.valueOf(false) : String.valueOf(true)) + ";");
        if (PermissionControler.a(iApp.obtainAppId(), IFeature.F_RUNTIME)) {
            String obtainConfigProperty = iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_LOADED_TIME);
            stringBuffer.append((iApp.isStreamApp() || BaseInfo.ISAMU) ? String.format(AbsoluteConst.JS_RUNTIME_VERSIONs, iApp.obtainAppVersionName(), BaseInfo.sBaseVersion, iApp.obtainAppId(), obtainConfigProperty) : String.format(AbsoluteConst.JS_RUNTIME_VERSIONs, AndroidResources.mApplicationInfo.versionName, BaseInfo.sBaseVersion, iApp.obtainAppId(), obtainConfigProperty));
            stringBuffer.append(String.format(AbsoluteConst.JS_RUNTIME_ARGUMENTS, DataUtil.utf8ToUnicode(iApp.obtainRuntimeArgs())));
            String launcherData = BaseInfo.getLauncherData(iApp.obtainAppId());
            String stringExtra = iApp.obtainWebAppIntent().getStringExtra(IntentConst.FROM_STREAM_OPEN_FLAG);
            if (!TextUtils.isEmpty(stringExtra) && !TextUtils.equals(stringExtra, iApp.obtainAppId())) {
                launcherData = launcherData + ":" + stringExtra;
            }
            stringBuffer.append(String.format(AbsoluteConst.JS_RUNTIME_LAUNCHER, launcherData));
            stringBuffer.append(String.format(AbsoluteConst.JS_RUNTIME_CHANNEL, BaseInfo.sChannel));
            String bundleData = SP.getBundleData("pdr", iApp.obtainAppId() + AbsoluteConst.LAUNCHTYPE);
            if (TextUtils.isEmpty(bundleData)) {
                bundleData = "default";
            }
            stringBuffer.append(String.format(AbsoluteConst.JS_RUNTIME_ORIGIN, bundleData));
        }
        int size = this.i.size();
        for (int i = 0; i < size; i++) {
            stringBuffer.append(this.i.get(i));
        }
        stringBuffer.append(b());
        stringBuffer.append("}catch(e){console.log('__load__plus__ function error=' + e);}}").append(AbsoluteConst.EXECUTE_LOAD_FUNS_FUN);
        return stringBuffer.toString();
    }

    private String b() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(";Object.defineProperty(plus.screen,\"resolutionHeight\",{get:function(){var e=window,l=e.__html5plus__&&e.__html5plus__.isReady?e.__html5plus__:n.plus&&n.plus.isReady?n.plus:window.plus;return l.bridge.execSync(\"Device\",\"s.resolutionHeight\",[])}}),Object.defineProperty(plus.screen,\"resolutionWidth\",{get:function(){var e=window,l=e.__html5plus__&&e.__html5plus__.isReady?e.__html5plus__:n.plus&&n.plus.isReady?n.plus:window.plus;return l.bridge.execSync(\"Device\",\"s.resolutionWidth\",[])}}),Object.defineProperty(plus.display,\"resolutionHeight\",{get:function(){var e=window,l=e.__html5plus__&&e.__html5plus__.isReady?e.__html5plus__:n.plus&&n.plus.isReady?n.plus:window.plus;return l.bridge.execSync(\"Device\",\"d.resolutionHeight\",[])}}),Object.defineProperty(plus.display,\"resolutionWidth\",{get:function(){var e=window,l=e.__html5plus__&&e.__html5plus__.isReady?e.__html5plus__:n.plus&&n.plus.isReady?n.plus:window.plus;return l.bridge.execSync(\"Device\",\"d.resolutionWidth\",[])}});");
        stringBuffer.append(";plus.webview.__test__('save');");
        if (Build.VERSION.SDK_INT > 19) {
            stringBuffer.append("plus.webview.__test__('update');");
        }
        return stringBuffer.toString();
    }

    private synchronized String a(Object[] objArr) {
        IWebview iWebview = (IWebview) objArr[0];
        String lowerCase = String.valueOf(objArr[1]).toLowerCase();
        String valueOf = String.valueOf(objArr[2]);
        JSONArray jSONArray = (JSONArray) objArr[3];
        IFeature a = a(lowerCase, iWebview.getActivity());
        if (a != null) {
            if ((a instanceof BaseFeature) && !((BaseFeature) a).isOldMode()) {
                return ((BaseFeature) a).execute(iWebview, valueOf, jSONArray);
            }
            String[] strArr = null;
            if (jSONArray != null) {
                try {
                    strArr = JSUtil.jsonArrayToStringArr(jSONArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return a.execute(iWebview, valueOf, strArr);
        }
        if (iWebview.obtainFrameView().getFrameType() != 3 && !PermissionControler.b(iWebview.obtainApp().obtainAppId(), lowerCase) && !iWebview.obtainApp().isStreamApp()) {
            Logger.e("featuremgr", "not found " + lowerCase + " feature plugin ; action=" + valueOf + ";" + ("请检查assets/data/properties.xml文件是否添加" + lowerCase + "相关节点。"));
            a(iWebview, lowerCase);
            return "";
        }
        return "";
    }

    IFeature a(String str, Activity activity) {
        IFeature iFeature;
        ClassNotFoundException e;
        IFeature iFeature2 = null;
        IFeature iFeature3 = this.e.get(str);
        if (iFeature3 != null) {
            return iFeature3;
        }
        String str2 = this.d.get(str);
        if (str2 != null && this.g.containsKey(str)) {
            IBoot iBoot = this.g.get(str);
            if ((iBoot instanceof BaseFeature) && str2.equals(iBoot.getClass().getName())) {
                iFeature3 = (BaseFeature) iBoot;
            }
        }
        if (iFeature3 != null) {
            this.e.put(str, iFeature3);
            return iFeature3;
        }
        if (str2 != null) {
            try {
                IFeature b = FeatureFactory.b(this, str);
                if (b == null) {
                    iFeature = (IFeature) Class.forName(str2).newInstance();
                    this.e.put(str, iFeature);
                    iFeature.init(this, str);
                    return iFeature;
                } else {
                    this.e.put(str, b);
                    return b;
                }
            } catch (ClassNotFoundException e7) {
                iFeature = iFeature3;
                e = e7;
            } catch (IllegalAccessException e8) {
                e8.printStackTrace();
            } catch (InstantiationException e9) {
                e9.printStackTrace();
            }
        } else {
            try {
                iFeature2 = (IFeature) b(str, activity);
                if (iFeature2 != null) {
                    try {
                        this.e.put(str, iFeature2);
                        iFeature2.init(this, str);
                    } catch (Exception e10) {
                        iFeature3 = iFeature2;
                        e10.printStackTrace();
                        return iFeature3;
                    }
                }
            } catch (Exception e11) {
                e11.printStackTrace();
            }
        }
        return iFeature2;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x009a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0023 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    HashMap<String, IBoot> a() {
        String logPrefix = "FeatureMgr.loadBootOptions ";
        String logTag = "Main_Path";

        // g — готова мапа IBoot'ів
        HashMap<String, IBoot> bootMap = this.g;
        // f — boot-фічі: ключ = ім'я, значення = клас
        HashMap<String, String> featureClassMap = this.f;
        // d — додаткові параметри
        HashMap<String, String> paramsMap = this.d;
        // c — вкладена структура параметрів
        HashMap<String, HashMap<String, String>> nestedMap = this.c;

        // s_properties — шлях до properties (або manifest.json)
        String propsPath = BaseInfo.s_properties;

        // читає файл properties -> заповнює featureClassMap, paramsMap, nestedMap
        PdrUtil.loadProperties2HashMap(featureClassMap, paramsMap, nestedMap, propsPath);

        Set<String> featureNames = featureClassMap.keySet();
        if (featureNames == null) {
            return null;
        }

        bootMap = new HashMap<>(2);
        this.g = bootMap;

        for (String featureName : featureNames) {
            IBoot boot = io.dcloud.feature.FeatureFactory.a(this, featureName); // можливо якийсь factory метод

            if (boot == null) {
                try {
                    String className = featureClassMap.get(featureName);
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.newInstance();

                    if (instance instanceof BaseFeature) {
                        ((BaseFeature) instance).init(this, featureName);
                    }

                    boot = (IBoot) instance;

                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();

                    // якщо виняток не ClassNotFoundException
                    String msg = logPrefix + e.getMessage();
                    Log.d(logTag, msg);
                }
            }

            if (boot != null) {
                if (boot instanceof BaseFeature) {
                    ((BaseFeature) boot).init(this, featureName);
                }
                bootMap.put(featureName, boot);
            }
        }

        // Додаткове оброблення вже ініціалізованих
        HashMap<String, IBoot> result = a(bootMap);

        // обробка paramsMap.values()
        Collection<String> values = paramsMap.values();
        this.a(values.iterator());

        // обробка вкладених мап nestedMap.values()
        Collection<HashMap<String, String>> innerMaps = nestedMap.values();
        for (HashMap<String, String> map : innerMaps) {
            this.a(map.values().iterator());
        }

        return result;
    }


    private void c() {
        try {
            this.b = (dp) Class.forName("io.dcloud.feature.d").getConstructor(Context.class).newInstance(getContext());
        } catch (Exception unused) {
            Logger.e("fmgr no dp");
        }
    }

    private Object b(String str, Activity activity) {
        dp cVar = this.b;
        if (cVar != null) {
            return cVar.a(str, activity);
        }
        return null;
    }

    private HashMap<String, IBoot> a(HashMap<String, IBoot> hashMap) {
        if (this.b == null) {
            return hashMap;
        }
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        Set<String> keySet = this.b.a().keySet();
        if (keySet != null) {
            for (String str : keySet) {
                Object a = this.b.a(str);
                if (a != null) {
                    if (a instanceof BaseFeature) {
                        ((BaseFeature) a).init(this, str);
                    }
                    hashMap.put(str, (IBoot) a);
                }
            }
        }
        return hashMap;
    }

    private void a(Iterator<String> it) {
        while (it.hasNext()) {
            String valueOf = String.valueOf(PlatformUtil.invokeMethod(it.next(), "getJsContent"));
            if (!PdrUtil.isEmpty(valueOf)) {
                this.i.add(valueOf);
            }
        }
    }

    private boolean b(String str) {
        return this.d.containsKey(str);
    }

    public void a(String str) {
        Collection<IFeature> values;
        HashMap<String, IFeature> hashMap = this.e;
        if (hashMap != null && (values = hashMap.values()) != null) {
            Iterator<IFeature> it = values.iterator();
            while (it.hasNext()) {
                it.next().dispose(str);
            }
        }
        HashMap<String, String> hashMap2 = this.a;
        if (hashMap2 != null) {
            hashMap2.remove(str);
        }
    }

    @Override // io.dcloud.common.DHInterface.AbsMgr
    public void dispose() {
        HashMap<String, IFeature> hashMap = this.e;
        if (hashMap != null) {
            Collection<IFeature> values = hashMap.values();
            if (values != null) {
                Iterator<IFeature> it = values.iterator();
                while (it.hasNext()) {
                    it.next().dispose(null);
                }
            }
            this.e.clear();
            this.e = null;
        }
    }

    public void a(IWebview iWebview, String str) {
        Dialog lossDialog = ErrorDialogUtil.getLossDialog(iWebview, String.format("打包时未添加%s模块, 请参考 http://ask.dcloud.net.cn/article/283", str), "http://ask.dcloud.net.cn/article/283", str);
        if (lossDialog != null) {
            lossDialog.show();
        }
    }
}
