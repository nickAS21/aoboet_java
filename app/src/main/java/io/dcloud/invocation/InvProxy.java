package io.dcloud.invocation;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.lt.batteryManage.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.internal.sdk.SDK;

/* compiled from: InvProxy.java */
/* loaded from: classes.dex  old a*/
public class InvProxy implements IEventCallback {
    static HashMap<IWebview, HashMap<String, NativeObject>> a = new HashMap<>(2);
    static InvProxy b = null;
    ArrayList<IWebview> c = new ArrayList<>();

    public InvProxy(AbsMgr absMgr) {
        b = this;
    }

    public String a(IWebview iWebview, String str, String[] strArr) {
        Class cls;
        String str2;
        String b2;
        Object a2;
        JSONArray jSONArray;
        if (!this.c.contains(iWebview)) {
            this.c.add(iWebview);
            ((AdaFrameView) iWebview.obtainFrameView()).addFrameViewListener(this);
        }
        boolean z = true;
        if ("__Instance".equals(str)) {
            String str3 = strArr[0];
            String str4 = strArr[1];
            if (strArr.length <= 2 || PdrUtil.isEmpty(strArr[2])) {
                jSONArray = null;
            } else {
                JSONArray createJSONArray = JSONUtil.createJSONArray(strArr[2]);
                JSONObject jSONObject = JSONUtil.getJSONObject(createJSONArray, 0);
                if (jSONObject != null) {
                    if ("boolean".equals(jSONObject.optString("type"))) {
                        z = jSONObject.optBoolean(IApp.ConfigProperty.CONFIG_VALUE);
                    } else {
                        z = true ^ PdrUtil.isEquals("__super__constructor__", JSONUtil.getString(jSONObject, IApp.ConfigProperty.CONFIG_VALUE));
                    }
                }
                jSONArray = createJSONArray;
            }
            if (!z) {
                return null;
            }
            try {
                a(iWebview, str3, new NativeObject(iWebview, this, NativeObjectClass.a(str4), str3, jSONArray));
                return null;
            } catch (Exception e) {
                String a3 = a(e, "new " + str4);
                Log.e("InvProxy", "NativeObject.execMethod __Instance " + str4 + " method ; params=" + jSONArray + e);
                return a3;
            }
        }
        if (BuildConfig.BUILD_TYPE.equals(str)) {
            NativeObject a4 = a(iWebview, strArr[0]);
            if (a4 == null) {
                return null;
            }
            a4.a();
            return null;
        }
        if ("getWebviewById".equals(str)) {
            return b(iWebview, SDK.obtainWebview(iWebview.obtainFrameView().obtainApp().obtainAppId(), strArr[0]).obtainWebview());
        }
        if ("currentWebview".equals(str)) {
            return b(iWebview, iWebview.obtainWebview());
        }
        if ("getContext".equals(str)) {
            String str5 = strArr[0];
            String b3 = b(iWebview, iWebview.getActivity());
            a(iWebview, "onActivityResult", str5);
            return b3;
        }
        if ("import".equals(str)) {
            return JSUtil.wrapJsVar(NativeObjectClass.a(iWebview, this, strArr[0]), false);
        }
        if ("__plusGetAttribute".equals(str)) {
            String str6 = strArr[0];
            String str7 = strArr[1];
            NativeObject a5 = a(iWebview, str6);
            if (a5 == null || (a2 = NativeObject.a(a5.b, a5.c, str7)) == null) {
                return null;
            }
            return b(iWebview, a2);
        }
        if ("__plusSetAttribute".equals(str)) {
            String str8 = strArr[0];
            String str9 = strArr[1];
            JSONArray createJSONArray2 = JSONUtil.createJSONArray(strArr[2]);
            NativeObject a6 = a(iWebview, str8);
            if (a6 == null) {
                return null;
            }
            NativeObject.a(iWebview, this, a6.b, a6.c, str9, createJSONArray2);
            return null;
        }
        if ("implements".equals(str)) {
            String str10 = strArr[0];
            InvocationHandlerImpl bVar = new InvocationHandlerImpl(iWebview, strArr[1], JSONUtil.createJSONArray(strArr[2]), strArr[3]);
            bVar.a = str10;
            return b(iWebview, bVar.a(null));
        }
        if ("__loadDylib".equals(str) || "__release".equals(str)) {
            return null;
        }
        if ("__inheritList".equals(str)) {
            String str11 = strArr[0];
            try {
                String str12 = strArr[1];
                if (!TextUtils.isEmpty(str12)) {
                    NativeObject a7 = a(iWebview, str12);
                    if (a7 != null) {
                        b2 = NativeObjectClass.a(a7.b);
                    } else {
                        b2 = NativeObjectClass.b(str11);
                    }
                } else {
                    b2 = NativeObjectClass.b(str11);
                }
            } catch (Exception e2) {
                b2 = a(e2, "importClass " + str11);
            }
        } else {
            if ("__execCFunction".equals(str)) {
                return null;
            }
            if ("__newObject".equals(str)) {
                String str13 = strArr[0];
                JSONArray createJSONArray3 = JSONUtil.createJSONArray(strArr[1]);
                try {
                    b2 = b(iWebview, NativeObject.a(iWebview, this, NativeObjectClass.a(str13), createJSONArray3));
                } catch (Exception e3) {
                    String a8 = a(e3, "newObject " + str13);
                    Log.e("InvProxy", "NativeObject.execMethod __newObject " + str13 + " method ; params=" + createJSONArray3 + e3);
                    return a8;
                }
            } else {
                if ("__execStatic".equals(str)) {
                    String str14 = strArr[0];
                    String str15 = strArr[1];
                    if (a(str14, str15, iWebview)) {
                        return null;
                    }
                    JSONArray createJSONArray4 = (strArr.length <= 2 || PdrUtil.isEmpty(strArr[2])) ? null : JSONUtil.createJSONArray(strArr[2]);
                    Class a9 = NativeObjectClass.a(str14);
                    if (a9 == null) {
                        str2 = str14;
                        cls = String.class;
                    } else {
                        cls = a9;
                        str2 = null;
                    }
                    try {
                        Object b4 = NativeObject.b(iWebview, this, cls, str2, str15, createJSONArray4);
                        if (b4 != null) {
                            return b(iWebview, b4);
                        }
                        return null;
                    } catch (Exception e4) {
                        String a10 = a(e4, "static " + cls.getName() + "." + str15);
                        Log.e("InvProxy", "NativeObject.execMethod " + str15 + " method ; params=" + cls + e4);
                        return a10;
                    }
                }
                if ("__exec".equals(str)) {
                    String str16 = strArr[0];
                    String str17 = strArr[1];
                    if (a("", str17, iWebview)) {
                        return null;
                    }
                    JSONArray createJSONArray5 = JSONUtil.createJSONArray(strArr[2]);
                    NativeObject a11 = a(iWebview, str16);
                    if (a11 == null) {
                        return null;
                    }
                    try {
                        Object a12 = a11.a(iWebview, str17, createJSONArray5);
                        if (a12 != null) {
                            return b(iWebview, a12);
                        }
                        return null;
                    } catch (Exception e5) {
                        String a13 = a(e5, a11.b.getName() + "." + str17);
                        Log.e("InvProxy", "NativeObject.execMethod " + str17 + " method ; params=" + createJSONArray5 + e5);
                        return a13;
                    }
                }
                if (!"__saveContent".equals(str)) {
                    return null;
                }
                DHFile.writeFile(strArr[1].toString().getBytes(), 0, iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[0]));
                return null;
            }
        }
        return b2;
    }

    private static String a(Exception exc, String str) {
        String str2;
        Object[] objArr = new Object[1];
        StringBuilder sb = new StringBuilder();
        if (exc.getCause() != null && !TextUtils.isEmpty(exc.getMessage())) {
            str2 = exc.getMessage();
        } else {
            str2 = "";
        }
        objArr[0] = sb.append((Object) str2).append(";at ").append(str).toString();
        return String.format("throw \"%s\";", objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: InvProxy.java */
    /* renamed from: io.dcloud.invocation.a$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[ISysEventListener.SysEventType.values().length];
            a = iArr;
            try {
                iArr[ISysEventListener.SysEventType.onActivityResult.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private void a(final IWebview iWebview, String str, final String str2) {
        if (AnonymousClass3.a[ISysEventListener.SysEventType.valueOf(str).ordinal()] != 1) {
            return;
        }
        final ISysEventListener iSysEventListener = new ISysEventListener() { // from class: io.dcloud.invocation.a.1
            @Override // io.dcloud.common.DHInterface.ISysEventListener
            public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                if (sysEventType != ISysEventListener.SysEventType.onActivityResult) {
                    return false;
                }
                Object[] objArr = (Object[]) obj;
                int intValue = ((Integer) objArr[0]).intValue();
                int intValue2 = ((Integer) objArr[1]).intValue();
                Intent intent = (Intent) objArr[2];
                StringBuffer stringBuffer = new StringBuffer("[");
                stringBuffer.append(intValue);
                stringBuffer.append(JSUtil.COMMA).append(intValue2);
                if (intent != null) {
                    stringBuffer.append(JSUtil.COMMA).append(InvProxy.a(iWebview, intent));
                }
                stringBuffer.append("]");
                JSUtil.execCallback(iWebview, str2, stringBuffer.toString(), JSUtil.OK, true, true);
                return true;
            }
        };
        iWebview.obtainApp().registerSysEventListener(iSysEventListener, ISysEventListener.SysEventType.onActivityResult);
        iWebview.obtainFrameView().addFrameViewListener(new IEventCallback() { // from class: io.dcloud.invocation.a.2
            @Override // io.dcloud.common.DHInterface.IEventCallback
            public Object onCallBack(String str3, Object obj) {
                if (!PdrUtil.isEquals(str3, AbsoluteConst.EVENTS_WINDOW_CLOSE) && !PdrUtil.isEquals(str3, AbsoluteConst.EVENTS_CLOSE)) {
                    return null;
                }
                iWebview.obtainApp().unregisterSysEventListener(iSysEventListener, ISysEventListener.SysEventType.onActivityResult);
                return null;
            }
        });
    }

    static String a(IWebview iWebview, Object obj) {
        String str;
        Class<?> cls = obj.getClass();
        String b2 = NativeObjectClass.b(cls);
        if (cls == String.class || cls == CharSequence.class) {
            str = JSUtil.QUOTE + String.valueOf(obj) + JSUtil.QUOTE;
        } else if (NativeObjectClass.c(cls)) {
            str = String.valueOf(obj);
        } else {
            String a2 = a(obj);
            a(iWebview, a2, obj);
            return String.format("plus.ios.__Tool.New(%s, true)", JSUtil.wrapJsVar(String.format("{\"type\":\"%s\", \"value\":%s, \"className\":\"%s\",\"superClassNames\":%s}", "object", JSUtil.QUOTE + a2 + JSUtil.QUOTE, b2, a((Class) cls)), false));
        }
        return JSUtil.wrapJsVar(String.format("{\"type\":\"%s\", \"value\":%s, \"className\":\"%s\",\"superClassNames\":%s}", "basic", str, b2, a((Class) cls)), false);
    }

    static String a(Class cls) {
        JSONStringer jSONStringer = new JSONStringer();
        ArrayList arrayList = new ArrayList();
        try {
            jSONStringer.array();
            a(cls, jSONStringer, (ArrayList<String>) arrayList);
            jSONStringer.endArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jSONStringer2 = jSONStringer.toString();
        return jSONStringer2 == null ? "[]" : jSONStringer2;
    }

    static void a(Class cls, JSONStringer jSONStringer, ArrayList<String> arrayList) throws JSONException {
        do {
            cls = cls.getSuperclass();
            if (cls == null) {
                return;
            }
            String name = cls.getName();
            if (!arrayList.contains(name)) {
                jSONStringer.value(name);
                arrayList.add(name);
                b(cls, jSONStringer, arrayList);
            }
        } while (cls != Object.class);
    }

    static void b(Class cls, JSONStringer jSONStringer, ArrayList<String> arrayList) throws JSONException {
        Class<?>[] interfaces = cls.getInterfaces();
        if (interfaces != null) {
            for (Class<?> cls2 : interfaces) {
                String name = cls2.getName();
                if (!arrayList.contains(name)) {
                    jSONStringer.value(name);
                    arrayList.add(name);
                    b(cls2, jSONStringer, arrayList);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(IWebview iWebview, Object obj) {
        Class<?> cls = obj.getClass();
        String b2 = NativeObjectClass.b(cls);
        String str = (NativeObjectClass.c(cls) || cls == String.class || cls == CharSequence.class || cls.isArray()) ? "basic" : "object";
        StringBuffer stringBuffer = new StringBuffer();
        a(iWebview, obj, cls, stringBuffer);
        return JSUtil.wrapJsVar(String.format("{\"type\":\"%s\", \"value\":%s, \"className\":\"%s\",\"superClassNames\":%s}", str, stringBuffer.toString(), b2, a((Class) cls)), false);
    }

    static void a(IWebview iWebview, Object obj, Class cls, StringBuffer stringBuffer) {
        if (cls == String.class || cls == CharSequence.class) {
            stringBuffer.append(JSONObject.quote(String.valueOf(obj)));
            return;
        }
        if (NativeObjectClass.c(cls)) {
            stringBuffer.append(String.valueOf(obj));
            return;
        }
        if (cls.isArray()) {
            int length = Array.getLength(obj);
            stringBuffer.append("[");
            for (int i = 0; i < length; i++) {
                stringBuffer.append(b(iWebview, NativeObjectClass.a(Array.get(obj, i), cls)));
                if (i != length - 1) {
                    stringBuffer.append(JSUtil.COMMA);
                }
            }
            stringBuffer.append("]");
            return;
        }
        String a2 = a(obj);
        a(iWebview, a2, obj);
        stringBuffer.append(JSUtil.QUOTE).append(a2).append(JSUtil.QUOTE);
    }

    private static HashMap<String, NativeObject> a(IWebview iWebview) {
        HashMap<String, NativeObject> hashMap = a.get(iWebview);
        if (hashMap != null) {
            return hashMap;
        }
        HashMap<String, NativeObject> hashMap2 = new HashMap<>(2);
        a.put(iWebview, hashMap2);
        return hashMap2;
    }

    NativeObject a(HashMap<String, NativeObject> hashMap, String str) {
        return hashMap.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NativeObject a(IWebview iWebview, String str) {
        return a(a(iWebview), str);
    }

    private static void a(IWebview iWebview, String str, NativeObject cVar) {
        a(iWebview).put(str, cVar);
    }

    private static NativeObject a(IWebview iWebview, String str, Object obj) {
        Class<?> cls = obj.getClass();
        cls.getName();
        NativeObject cVar = new NativeObject(b, cls, str, obj);
        a(iWebview, str, cVar);
        return cVar;
    }

    static String a(Object obj) {
        return IFeature.F_INVOCATION + obj.hashCode();
    }

    @Override // io.dcloud.common.DHInterface.IEventCallback
    public Object onCallBack(String str, Object obj) {
        if (!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE) || !(obj instanceof IWebview)) {
            return null;
        }
        try {
            ((AdaFrameView) ((IWebview) obj).obtainFrameView()).removeFrameViewListener(this);
            this.c.remove(obj);
            a.remove((IWebview) obj).clear();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean a(String str, String str2, IWebview iWebview) {
        return iWebview != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && Boolean.parseBoolean(iWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_USE_ENCRYPTION)) && "setWebContentsDebuggingEnabled".equalsIgnoreCase(str2) && (TextUtils.isEmpty(str) || "WebView".equalsIgnoreCase(str) || "android.webkit.WebView".equalsIgnoreCase(str));
    }

    public void a(String str) {
        TextUtils.isEmpty(str);
    }
}
