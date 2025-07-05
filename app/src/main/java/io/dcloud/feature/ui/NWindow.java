package io.dcloud.feature.ui;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IContainerView;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.ReceiveJSValue;
import io.dcloud.common.adapter.ui.WebLoadEvent;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.ThreadPool;

/* compiled from: NWindow.java */
/* loaded from: classes.dex  old c*/
public class NWindow extends NView implements IEventCallback {
    private static final HashMap<String, String> Y;
    int A;
    boolean B;
    boolean C;
    boolean D;
    boolean E;
    boolean F;
    boolean G;
    boolean H;
    protected ArrayList<NView> I;
    String J;
    String K;
    IWebview L;
    String M;
    IWebview N;
    String O;
    IWebview P;
    String Q;
    NWindow R;
    Runnable S;
    private boolean T;
    private ArrayList<NWindow> U;
    private boolean V;
    private String W;
    private int X;
    long q;
    JSONArray r;
    IWebview s;
    JSONObject t;
    IFrameView u;
    String v;
    int w;
    Object x;
    boolean y;
    boolean z;

    @Override // io.dcloud.feature.ui.b
    public void a(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    protected void i() {
    }

    public void a(boolean z) {
        this.V = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NWindow(AppWidgetMgr aVar, String str, String str2, String str3, JSONObject jSONObject) {
        this(aVar, null, str, str2, str3, jSONObject);
    }

    private NWindow(AppWidgetMgr aVar, IFrameView iFrameView, String str, String str2, String str3, JSONObject jSONObject) {
        super("NWindow");
        this.q = System.currentTimeMillis();
        this.r = null;
        this.s = null;
        this.t = null;
        this.v = null;
        this.w = -1;
        this.x = null;
        this.y = false;
        this.z = false;
        this.A = 0;
        this.B = false;
        this.C = false;
        this.D = true;
        this.E = false;
        this.F = false;
        this.G = false;
        this.H = false;
        this.I = null;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = null;
        this.P = null;
        this.Q = null;
        this.T = true;
        this.R = null;
        this.U = null;
        this.V = false;
        this.W = "auto";
        this.X = 150;
        this.S = null;
        this.c = aVar;
        this.v = str;
        this.e = str3;
        this.g = jSONObject;
        a(iFrameView, str2);
    }

    public void a(IFrameView iFrameView, String str) {
        if (iFrameView != null) {
            this.u = iFrameView;
            IWebview obtainWebView = iFrameView.obtainWebView();
            if (obtainWebView != null) {
                obtainWebView.initWebviewUUID(this.e);
                obtainWebView.setFrameId(str);
            }
        }
    }

    protected NView b(String str) {
        ArrayList<NView> arrayList = this.I;
        NView bVar = null;
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int size = this.I.size() - 1; size >= 0; size--) {
                bVar = this.I.get(size);
                if (PdrUtil.isEquals(str, bVar.f)) {
                    break;
                }
            }
        }
        return bVar;
    }

    public boolean a(NView bVar) {
        ArrayList<NView> arrayList = this.I;
        if (arrayList == null) {
            return false;
        }
        return arrayList.contains(bVar);
    }

    public boolean h() {
        return !this.u.isWebviewCovered();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(NWindow cVar) {
        if (this.U == null) {
            this.U = new ArrayList<>();
        }
        this.U.add(cVar);
        cVar.R = this;
    }

    protected void b(NView bVar) {
        ArrayList<NView> arrayList = this.I;
        if (arrayList == null || !arrayList.contains(bVar)) {
            return;
        }
        this.I.remove(bVar);
        bVar.a = null;
        byte b = bVar.b();
        boolean z = bVar instanceof NWindow;
        if (b == j) {
            this.u.obtainWebView().removeFrameItem(bVar.e());
            return;
        }
        if (b == k) {
            this.u.obtainWebviewParent().removeFrameItem(bVar.e());
        } else if (b == l) {
            this.u.removeFrameItem(bVar.e());
            if (z) {
                this.u.obtainWebviewParent().obtainFrameOptions().delRelViewRect(bVar.e().obtainFrameOptions());
            }
            e().resize();
        }
    }

    protected void a(IWebview iWebview, String str) {
        this.c.absMgr.processEvent(IMgr.MgrType.FeatureMgr, 10, new Object[]{iWebview, "nativeobj", "removeNativeView", new Object[]{this.u, str}});
    }

    protected void b(IWebview iWebview, String str) {
        this.c.absMgr.processEvent(IMgr.MgrType.FeatureMgr, 10, new Object[]{iWebview, "nativeobj", "addNativeView", new Object[]{this.u, str}});
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0174  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b7  */
    /* JADX WARN: Type inference failed for: r12v2, types: [io.dcloud.common.DHInterface.IWebview] */
    /* JADX WARN: Type inference failed for: r12v4, types: [io.dcloud.common.DHInterface.IFrameView] */
    /* JADX WARN: Type inference failed for: r12v7, types: [io.dcloud.common.DHInterface.IFrameView] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void c(NView r18) {
        /*
            Method dump skipped, instructions count: 409
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.c(io.dcloud.feature.ui.b):void");
    }

    private void a(NView bVar, NWindow cVar) {
        if (!BaseInfo.isBase(bVar.c()) || this.v.startsWith(DeviceInfo.HTTP_PROTOCOL) || this.v.startsWith(DeviceInfo.HTTPS_PROTOCOL) || cVar.v.startsWith(DeviceInfo.HTTP_PROTOCOL) || cVar.v.startsWith(DeviceInfo.HTTPS_PROTOCOL) || TextUtils.isEmpty(this.v) || TextUtils.isEmpty(cVar.v)) {
            return;
        }
        Log.i(AbsoluteConst.HBUILDER_TAG, String.format(AbsoluteConst.FILIATIONLOG, UIWidgetMgr.c(WebLoadEvent.getHBuilderPrintUrl(cVar.k().obtainApp().convert2RelPath(k().obtainUrl()))), UIWidgetMgr.c(WebLoadEvent.getHBuilderPrintUrl(cVar.k().obtainUrl()))));
    }

    private static void a(IContainerView iContainerView, AdaFrameItem adaFrameItem, ViewGroup.LayoutParams layoutParams, int i, int i2, int i3, int i4) {
        boolean z = layoutParams instanceof AbsoluteLayout.LayoutParams;
        ViewOptions obtainFrameOptions = adaFrameItem.obtainFrameOptions();
        obtainFrameOptions.left = i;
        obtainFrameOptions.top = i2;
        obtainFrameOptions.width = i3;
        obtainFrameOptions.height = i4;
        obtainFrameOptions.commitUpdate2JSONObject();
        ((AdaFrameView) adaFrameItem).isChildOfFrameView = true;
        View obtainMainView = adaFrameItem.obtainMainView();
        if (Build.VERSION.SDK_INT > 11) {
            obtainMainView.setTop(0);
            obtainMainView.setLeft(0);
            obtainMainView.setBottom(0);
            obtainMainView.setRight(0);
        } else {
            obtainMainView.layout(0, 0, i3, i4);
        }
        obtainMainView.setX(0.0f);
        obtainMainView.setY(0.0f);
        iContainerView.addFrameItem(adaFrameItem, AdaFrameItem.LayoutParamsUtil.createLayoutParams(i, i2, i3, i4));
        Logger.d(Logger.VIEW_VISIBLE_TAG, "appendNWindow Y=" + obtainMainView.getY());
    }

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        Y = hashMap;
        hashMap.put(AbsoluteConst.EVENTS_CLOSE, "onclose");
        hashMap.put("loading", "onloading");
        hashMap.put(AbsoluteConst.EVENTS_FAILED, "onerror");
        hashMap.put(AbsoluteConst.EVENTS_LOADED, "onloaded");
    }

    @Override // io.dcloud.common.DHInterface.IEventCallback
    public Object onCallBack(String str, Object obj) {
        IWebview iWebview;
        Logger.d("yl", "NWindow.onCallBack pEventType=" + str, obj);
        if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_DRAG_BOUNCE) || PdrUtil.isEquals(str, AbsoluteConst.EVENTS_SLIDE_BOUNCE)) {
            a(str, PdrUtil.isEmpty(obj) ? null : String.valueOf(obj), false);
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_SHOW_ANIMATION_END, str)) {
            if (!PdrUtil.isEmpty(this.K) && (iWebview = this.L) != null) {
                JSUtil.execCallback(iWebview, this.K, "", JSUtil.OK, false, false);
            }
            b(AbsoluteConst.EVENTS_WEBVIEW_SHOW, PdrUtil.isEmpty(obj) ? null : String.valueOf(obj));
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_TITLE_UPDATE, str)) {
            Object[] objArr = new Object[1];
            objArr[0] = obj == null ? "''" : JSONObject.quote(obj.toString());
            a(str, String.format("{title:%s}", objArr), false);
            m();
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_LISTEN_RESOURCE_LOADING, str)) {
            JSUtil.execCallback(this.P, this.O, (String) obj, JSUtil.OK, true, true);
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_OVERRIDE_URL_LOADING, str)) {
            JSUtil.execCallback(this.N, this.M, (String) obj, JSUtil.OK, true, true);
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_HIDE_LOADING, str)) {
            this.c.f(this);
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_SHOW_LOADING, str)) {
            if (this.E) {
                this.c.e(this);
            }
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_FRAME_ONRESIZE, str)) {
            i();
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_WEBAPP_SLIDE_HIDE, str)) {
            n();
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_WEBAPP_SLIDE_CLOSE, str)) {
            o();
        } else if (PdrUtil.isEquals("popGesture", str)) {
            Object[] objArr2 = (Object[]) obj;
            String str2 = (String) objArr2[0];
            Object obj2 = objArr2[1];
            NWindow c = this.c.c((IFrameView) objArr2[2]);
            a(str, String.format("{type:'%s', result:%s, private_args:{uuid:'%s',id:'%s',extras:'%s'}}", str2, obj2, c.e, c.f, c.t), false);
        } else if (PdrUtil.isEquals(AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START, str)) {
            if (this.i != null && this.i.containsKey(AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START)) {
                b(str, PdrUtil.isEmpty(obj) ? null : String.valueOf(obj));
            }
        } else {
            String str3 = Y.get(str);
            if (!PdrUtil.isEmpty(str3)) {
                a(str3, obj, this.c.b, this);
            }
            if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_LOADED)) {
                this.w = -1;
                JSONArray jSONArray = this.r;
                if (jSONArray != null) {
                    a(this.s, jSONArray, this, this.u.obtainApp().obtainAppId());
                }
                TestUtil.print(TestUtil.CREATE_WEBVIEW, this.v + " 从加载完成分发loaded事件到开始分发事件 " + str);
                Logger.d(Logger.MAIN_TAG, "EVENTS_LOADED mUrl=" + this.v);
            } else if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE)) {
                f();
            }
            b(str, PdrUtil.isEmpty(obj) ? null : String.valueOf(obj));
        }
        if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) {
            this.c.d(this);
        }
        return null;
    }

    public static void a(String str, Object obj, ArrayList<NWindow> arrayList, NWindow cVar) {
        Iterator<NWindow> it = arrayList.iterator();
        while (it.hasNext()) {
            JSUtil.broadcastWebviewEvent(it.next().k(), cVar.e, str, JSONUtil.toJSONableString(String.valueOf(obj)));
        }
        if (arrayList.contains(cVar)) {
            return;
        }
        JSUtil.broadcastWebviewEvent(cVar.k(), cVar.e, str, JSONUtil.toJSONableString(String.valueOf(obj)));
    }

    protected String j() {
        ViewOptions obtainFrameOptions = ((AdaFrameItem) this.u).obtainFrameOptions();
        return String.format("{top:%d,left:%d,width:%d,height:%d}", Integer.valueOf((int) (obtainFrameOptions.top / obtainFrameOptions.mWebviewScale)), Integer.valueOf((int) (obtainFrameOptions.left / obtainFrameOptions.mWebviewScale)), Integer.valueOf((int) (obtainFrameOptions.width / obtainFrameOptions.mWebviewScale)), Integer.valueOf((int) (obtainFrameOptions.height / obtainFrameOptions.mWebviewScale)));
    }

    @Override // io.dcloud.feature.ui.b
    public AdaFrameItem e() {
        return (AdaFrameItem) this.u;
    }

    @Override // io.dcloud.feature.ui.b
    public String a(final IWebview iWebview, String str, JSONArray jSONArray) {
        Exception exc;
        IApp obtainApp = null;
        String obtainAppId = null;
        boolean z = false;
        Object processEvent;
        JSONObject json;
        String str2 = "needTouchEvent";
        try {
            obtainApp = iWebview.obtainFrameView().obtainApp();
            obtainAppId = obtainApp.obtainAppId();
            z = true;
            try {
            } catch (Exception e) {
                exc = e;
                str2 = null;
            }
        } catch (Exception e2) {
            exc = e2;
            str2 = null;
        }
        if ("setPullToRefresh".equals(str)) {
            Logger.d(Logger.VIEW_VISIBLE_TAG, "refreshLoadingViewsSize setPullToRefresh args=" + jSONArray);
            JSONObject jSONObject = JSONUtil.getJSONObject(jSONArray, 0);
            String string = JSONUtil.getString(jSONArray, 1);
            if (!PdrUtil.isEmpty(string)) {
                this.Q = string;
            }
            this.u.obtainWebView().setWebViewEvent(AbsoluteConst.PULL_DOWN_REFRESH, jSONObject);
        } else if ("beginPullToRefresh".equals(str)) {
            this.u.obtainWebView().setWebViewEvent(AbsoluteConst.PULL_REFRESH_BEGIN, null);
        } else if ("endPullToRefresh".equals(str)) {
            this.u.obtainWebView().endWebViewEvent(AbsoluteConst.PULL_DOWN_REFRESH);
        } else if ("setBounce".equals(str)) {
            this.u.obtainWebView().setWebViewEvent(AbsoluteConst.BOUNCE_REGISTER, JSONUtil.getJSONObject(jSONArray, 0));
        } else if ("resetBounce".equals(str)) {
            this.u.obtainWebView().endWebViewEvent(AbsoluteConst.BOUNCE_REGISTER);
        } else if ("setBlockNetworkImage".equals(str)) {
            k().setWebviewProperty(AbsoluteConst.JSON_KEY_BLOCK_NETWORK_IMAGE, JSONUtil.getString(jSONArray, 0));
        } else {
            if ("getOption".equals(str)) {
                ViewOptions obtainFrameOptions = ((AdaFrameItem) this.u).obtainFrameOptions();
                if (obtainFrameOptions.hasBackground()) {
                    obtainFrameOptions = this.u.obtainWebviewParent().obtainFrameOptions();
                }
                return JSUtil.wrapJsVar(obtainFrameOptions.mJsonViewOption.toString(), false);
            }
            try {
                if (!"setOption".equals(str) && !"setStyle".equals(str)) {
                if ("getMetrics".equals(str)) {
                    JSUtil.execCallback(iWebview, JSONUtil.getString(jSONArray, 0), j(), JSUtil.OK, true, false);
                } else {
                    if ("getUrl".equals(str)) {
                        return JSUtil.wrapJsVar(this.u.obtainWebView().obtainFullUrl(), true);
                    }
                    if ("setPreloadJsFile".equals(str)) {
                        String string2 = JSONUtil.getString(jSONArray, 0);
                        if (!PdrUtil.isEmpty(string2)) {
                            this.u.obtainWebView().setPreloadJsFile(this.u.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), string2));
                        }
                    } else if ("appendPreloadJsFile".equals(str)) {
                        this.u.obtainWebView().appendPreloadJsFile(this.u.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), JSONUtil.getString(jSONArray, 0)));
                    } else if (AbsoluteConst.EVENTS_LISTEN_RESOURCE_LOADING.equals(str)) {
                        JSONObject optJSONObject = jSONArray.optJSONObject(0);
                        this.O = jSONArray.optString(1);
                        this.P = iWebview;
                        this.u.obtainWebView().setListenResourceLoading(optJSONObject);
                    } else if ("overrideResourceRequest".equals(str)) {
                        this.u.obtainWebView().setOverrideResourceRequest(jSONArray.optJSONArray(0));
                    } else if (AbsoluteConst.EVENTS_OVERRIDE_URL_LOADING.equals(str)) {
                        JSONObject optJSONObject2 = jSONArray.optJSONObject(0);
                        this.M = jSONArray.optString(1);
                        this.N = iWebview;
                        this.u.obtainWebView().setOverrideUrlLoadingData(optJSONObject2);
                    } else if (AbsoluteConst.EVENTS_WEBVIEW_HIDE.equals(str)) {
                        a(jSONArray, this);
                    } else if (AbsoluteConst.EVENTS_WEBVIEW_SHOW.equals(str)) {
                        a(iWebview, jSONArray, this, obtainAppId);
                    } else if (AbsoluteConst.EVENTS_CLOSE.equals(str)) {
                        b(jSONArray, this);
                    } else if ("evalJS".equals(str)) {
                        String string3 = JSONUtil.getString(jSONArray, 0);
                        IWebview obtainWebView = this.u.obtainWebView();
                        final String string4 = JSONUtil.getString(jSONArray, 1);
                        if (!PdrUtil.isEmpty(string4)) {
                            string3 = ReceiveJSValue.registerCallback(string3, new ReceiveJSValue.ReceiveJSValueCallback() { // from class: io.dcloud.feature.ui.c.1
                                @Override // io.dcloud.common.adapter.ui.ReceiveJSValue.ReceiveJSValueCallback
                                public String callback(JSONArray jSONArray2) {
                                    Object obj;
                                    String string5 = JSONUtil.getString(jSONArray2, 0);
                                    try {
                                        obj = jSONArray2.get(1);
                                    } catch (JSONException unused) {
                                        obj = null;
                                    }
                                    if ((obj instanceof String) || "string".equals(string5)) {
                                        JSUtil.execCallback(iWebview, string4, String.valueOf(obj), JSUtil.OK, false, false);
                                    } else if (obj instanceof JSONArray) {
                                        JSUtil.execCallback(iWebview, string4, obj.toString(), JSUtil.OK, true, false);
                                    } else if ((obj instanceof JSONObject) || "object".equals(string5)) {
                                        JSUtil.execCallback(iWebview, string4, obj.toString(), JSUtil.OK, true, false);
                                    } else if ("undefined".equals(string5)) {
                                        JSUtil.execCallback(iWebview, string4, "undefined", JSUtil.OK, true, false);
                                    } else {
                                        JSUtil.execCallback(iWebview, string4, obj.toString(), JSUtil.OK, true, false);
                                    }
                                    return null;
                                }
                            });
                        }
                        obtainWebView.evalJS(string3);
                    } else if ("back".equals(str)) {
                        IWebview obtainWebView2 = this.u.obtainWebView();
                        obtainWebView2.stopLoading();
                        obtainWebView2.goBackOrForward(-1);
                    } else if ("forward".equals(str)) {
                        IWebview obtainWebView3 = this.u.obtainWebView();
                        obtainWebView3.stopLoading();
                        obtainWebView3.goBackOrForward(1);
                    } else if ("canBack".equals(str)) {
                        JSUtil.execCallback(iWebview, JSONUtil.getString(jSONArray, 0), String.valueOf(this.u.obtainWebView().canGoBack()), JSUtil.OK, true, false);
                    } else if ("canForward".equals(str)) {
                        JSUtil.execCallback(iWebview, JSONUtil.getString(jSONArray, 0), String.valueOf(this.u.obtainWebView().canGoForward()), JSUtil.OK, true, false);
                    } else if ("clear".equals(str)) {
                        this.u.obtainWebView().clearHistory();
                    } else if ("load".equals(str)) {
                        String obtainUrl = iWebview.obtainUrl();
                        String string5 = JSONUtil.getString(jSONArray, 0);
                        String convert2WebviewFullPath = iWebview.obtainFrameView().obtainApp().convert2WebviewFullPath(iWebview.obtainFullUrl(), string5);
                        Logger.d("NWindow.load " + convert2WebviewFullPath);
                        if (!PdrUtil.isNetPath(string5) && this.c.b(obtainAppId) && this.c.d(convert2WebviewFullPath)) {
                            if (!this.c.a(this, convert2WebviewFullPath)) {
                                this.c.e(this);
                            }
                        } else {
                            this.u.obtainWebView().setOriginalUrl(string5);
                            this.u.obtainWebView().reload(convert2WebviewFullPath);
                        }
                        a(this, obtainUrl);
                    } else if ("stop".equals(str)) {
                        this.u.obtainWebView().stopLoading();
                    } else if ("reload".equals(str)) {
                        a(this, PdrUtil.parseBoolean(JSONUtil.getString(jSONArray, 0), true, false));
                    } else if ("addEventListener".equals(str)) {
                        String string6 = jSONArray.getString(0);
                        a(jSONArray.getString(1), string6, this.b.get(iWebview));
                        if (!this.u.obtainWebView().unReceiveTitle() && AbsoluteConst.EVENTS_TITLE_UPDATE.equals(string6)) {
                            onCallBack(AbsoluteConst.EVENTS_TITLE_UPDATE, this.u.obtainWebView().obtainWebview().getTitle());
                        }
                    } else if ("removeEventListener".equals(str)) {
                        a(jSONArray.getString(1), jSONArray.getString(0));
                    } else {
                        if ("isVisible".equals(str)) {
                            return JSUtil.wrapJsVar(String.valueOf(this.B), false);
                        }
                        if ("setVisible".equals(str)) {
                            boolean z2 = jSONArray.getBoolean(0);
                            this.B = z2;
                            this.u.setVisible(z2, true);
                        } else if ("setContentVisible".equals(str)) {
                            boolean z3 = jSONArray.getBoolean(0);
                            this.D = z3;
                            ((AdaFrameItem) this.u.obtainWebView()).setVisibility(z3 ? AdaFrameItem.VISIBLE : AdaFrameItem.GONE);
                            this.u.obtainWebviewParent().setBgcolor(-1);
                        } else {
                            if ("findViewById".equals(str)) {
                                return b(JSONUtil.getString(jSONArray, 0)).d();
                            }
                            if ("getTitle".equals(str)) {
                                return JSUtil.wrapJsVar(this.u.obtainWebView().obtainPageTitle(), true);
                            }
                            if ("opener".equals(str)) {
                                NWindow cVar = this.R;
                                if (cVar != null) {
                                    return cVar.d();
                                }
                                return JSUtil.wrapJsVar(String.format("{'uuid':%s,'id':%s}", "undefined", "undefined"), false);
                            }
                            if ("opened".equals(str)) {
                                return a(this.U);
                            }
                            if ("removeFromParent".equals(str)) {
                                NWindow cVar2 = this.a;
                                if (cVar2 != null && cVar2.a((NView) this)) {
                                    cVar2.b((NView) this);
                                }
                            } else {
                                if ("parent".equals(str)) {
                                    if (this.a != null) {
                                        return this.a.d();
                                    }
                                    return JSUtil.wrapJsVar(String.format("{'uuid':%s,'id':%s}", "undefined", "undefined"), false);
                                }
                                if ("children".equals(str)) {
                                    return a(this.I);
                                }
                                if ("loadData".equals(str)) {
                                    this.u.obtainWebView().loadContentData(JSONUtil.getString(jSONArray, 0), PdrUtil.getNonString(JSONUtil.getString(jSONArray, 2), "text/html"), PdrUtil.getNonString(JSONUtil.getString(jSONArray, 3), "utf-8"));
                                } else if ("removeNativeView".equals(str)) {
                                    a(iWebview, JSONUtil.getString(jSONArray, 1));
                                } else if (AbsoluteConst.XML_REMOVE.equals(str)) {
                                    String string7 = JSONUtil.getString(jSONArray, 0);
                                    NView a = this.c.a(string7);
                                    if (a == null) {
                                        a = this.c.a(string7, string7, (String) null);
                                    }
                                    if (a(a)) {
                                        b(a);
                                    }
                                } else if ("append".equals(str)) {
                                    String string8 = JSONUtil.getString(jSONArray, 1);
                                    NView a2 = this.c.a(string8);
                                    if (a2 == null) {
                                        a2 = this.c.a(string8, string8, (String) null);
                                    }
                                    if (!a(a2)) {
                                        c(a2);
                                    }
                                } else if ("appendNativeView".equals(str)) {
                                    b(iWebview, JSONUtil.getString(jSONArray, 1));
                                } else if ("captureSnapshot".equals(str)) {
                                    a(iWebview, jSONArray, this);
                                } else if ("clearSnapshot".equals(str)) {
                                    this.u.clearSnapshot(jSONArray.getString(0));
                                } else if ("draw".equals(str)) {
                                    c(iWebview, jSONArray, this);
                                } else {
                                    if ("isHardwareAccelerated".equals(str)) {
                                        if (!((AdaFrameItem) this.u).obtainFrameOptions().mUseHardwave && (this.u.getFrameType() != 2 || !obtainApp.isStreamApp() || !BaseInfo.isWap2AppAppid(obtainAppId))) {
                                            z = false;
                                        }
                                        return JSUtil.wrapJsVar(z);
                                    }
                                    if ("setCssFile".equals(str)) {
                                        String string9 = JSONUtil.getString(jSONArray, 0);
                                        if (!PdrUtil.isEmpty(string9)) {
                                            this.u.obtainWebView().setCssFile(obtainApp.convert2LocalFullPath(iWebview.obtainFullUrl(), string9), null);
                                        }
                                    } else if ("setFixBottom".equals(str)) {
                                        IWebview obtainWebView4 = this.u.obtainWebView();
                                        obtainWebView4.setFixBottom((int) (jSONArray.getInt(0) * obtainWebView4.getScale()));
                                    } else {
                                        try {
                                        } catch (Exception e4) {
                                            exc = e4;
                                            str2 = null;
                                        }
                                        if ("getNavigationbar".equals(str)) {
                                            Object processEvent2 = this.c.absMgr.processEvent(IMgr.MgrType.FeatureMgr, 10, new Object[]{this.u.obtainWebView(), "nativeobj", "getNativeView", new Object[]{this.u, String.valueOf(this.u.obtainWebView().obtainWebview().hashCode())}});
                                            if (processEvent2 == null || !(processEvent2 instanceof INativeView) || (json = ((INativeView) processEvent2).toJSON()) == null) {
                                                return null;
                                            }
                                            return JSUtil.wrapJsVar(json.toString(), false);
                                        }
                                        try {
                                        } catch (Exception e5) {
                                            str2 = null;
                                            exc = e5;
                                            exc.printStackTrace();
                                            return str2;
                                        }
                                        if ("drag".equals(str)) {
                                            JSONObject jSONObject2 = JSONUtil.getJSONObject(jSONArray, 0);
                                            JSONObject jSONObject3 = JSONUtil.getJSONObject(jSONArray, 1);
                                            String string10 = JSONUtil.getString(jSONArray, 2);
                                            String string11 = JSONUtil.getString(jSONArray, 3);
                                            if (jSONObject2 != null && !TextUtils.isEmpty(JSONUtil.getString(jSONObject2, "direction")) && !TextUtils.isEmpty(JSONUtil.getString(jSONObject2, "moveMode"))) {
                                                ViewOptions obtainFrameOptions2 = e().obtainFrameOptions();
                                                String string12 = JSONUtil.getString(jSONObject3, "view");
                                                NWindow a3 = !TextUtils.isEmpty(string12) ? this.c.a("", string12, string12) : null;
                                                View view = (a3 == null && (processEvent = this.c.absMgr.processEvent(IMgr.MgrType.FeatureMgr, 10, new Object[]{iWebview, "nativeobj", "getNativeView", new Object[]{this.u, string12}})) != null && (processEvent instanceof View)) ? (View) processEvent : null;
                                                NWindow a4 = this.c.a("", string10, string10);
                                                obtainFrameOptions2.setDragData(jSONObject2, jSONObject3, a3 == null ? null : a3.u, a4 == null ? null : a4.u, string11 != null ? string11 : null, view);
                                            }
                                        } else {
                                            if ("needTouchEvent".equals(str)) {
                                                this.u.obtainWebView().setWebviewProperty("needTouchEvent", AbsoluteConst.TRUE);
                                                return AbsoluteConst.FALSE;
                                            }
                                            if ("setCssText".equals(str)) {
                                                String string13 = JSONUtil.getString(jSONArray, 0);
                                                if (!PdrUtil.isEmpty(string13)) {
                                                    try {
                                                        this.u.obtainWebView().setCssFile(null, string13);
                                                    } catch (Exception e6) {
                                                        exc = e6;
                                                        str2 = null;
                                                        exc.printStackTrace();
                                                        return str2;
                                                    }
                                                }
                                            } else {
                                                if ("showBehind".equals(str)) {
                                                    String string14 = JSONUtil.getString(jSONArray, 1);
                                                    if (this.c.a(string14) == null) {
                                                        str2 = null;
                                                        a(this.c.a(string14, string14, (String) null), this, obtainAppId);
                                                    }
                                                } else {
                                                    str2 = null;
                                                    if ("checkRenderedContent".equals(str)) {
                                                        a(iWebview, jSONArray);
                                                    } else if ("webview_animate".equals(str)) {
                                                        this.u.animate(iWebview, JSONUtil.getString(jSONArray, 0), JSONUtil.getString(jSONArray, 1));
                                                    } else if ("webview_restore".equals(str)) {
                                                        this.u.restore();
                                                    } else if ("setRenderedEventOptions".equals(str)) {
                                                        JSONObject jSONObject4 = JSONUtil.getJSONObject(jSONArray, 0);
                                                        this.W = jSONObject4.optString("type", this.W);
                                                        this.X = jSONObject4.optInt(AbsoluteConst.JSON_KEY_INTERVAL, this.X);
                                                    } else if ("interceptTouchEvent".equals(str)) {
                                                        this.u.interceptTouchEvent(Boolean.valueOf(JSONUtil.getString(jSONArray, 0)).booleanValue());
                                                    }
                                                }
                                                return str2;
                                            }
                                        }
                                        str2 = null;
                                        return str2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            } catch (Exception e3) {
                exc = e3;
            }
            str2 = null;
            b(iWebview, jSONArray, this);
            return str2;
        }
        str2 = null;
        return str2;
    }

    private void a(final IWebview iWebview, JSONArray jSONArray) {
        final String string = JSONUtil.getString(jSONArray, 1);
        JSONObject jSONObject = JSONUtil.getJSONObject(jSONArray, 2);
        String str = "auto";
        if (jSONObject != null && jSONObject.has("type")) {
            str = jSONObject.optString("type", "auto");
        }
        String finalStr = str;
        ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.feature.ui.c.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    JSUtil.execCallback(iWebview, string, "{\"code\":100,\"rendered\":" + iWebview.checkWhite(finalStr) + "}", JSUtil.OK, true, false);
                } catch (Exception unused) {
                    JSUtil.execCallback(iWebview, string, "{\"code\":-100,\"message\":\"截图失败\"}", JSUtil.ERROR, true, false);
                }
            }
        });
    }

    private void a(NView bVar, NWindow cVar, String str) {
        NWindow cVar2 = (NWindow) bVar;
        if (cVar2.B) {
            cVar.q = cVar2.q - 1;
            cVar.B = true;
            cVar.E = true;
            cVar.C = false;
            this.c.a(str, cVar, this.c.a(this));
            this.c.absMgr.processEvent(IMgr.MgrType.WindowMgr, 45, new Object[]{cVar.u, cVar2.u});
        }
    }

    private void a(NWindow cVar, String str) {
        IApp obtainApp;
        if (cVar == null || (obtainApp = cVar.k().obtainApp()) == null) {
            return;
        }
        NWindow cVar2 = cVar.R;
        if (cVar2 != null) {
            str = cVar2.k().obtainUrl();
        }
        String obtainUrl = cVar.k().obtainUrl();
        if (!BaseInfo.isBase(cVar.c()) || TextUtils.isEmpty(str) || TextUtils.isEmpty(obtainUrl) || str.startsWith(DeviceInfo.HTTP_PROTOCOL) || obtainUrl.startsWith(DeviceInfo.HTTP_PROTOCOL)) {
            return;
        }
        Log.i(AbsoluteConst.HBUILDER_TAG, String.format(AbsoluteConst.OPENLOG, WebLoadEvent.getHBuilderPrintUrl(obtainApp.convert2RelPath(WebLoadEvent.getOriginalUrl(str))), WebLoadEvent.getHBuilderPrintUrl(obtainApp.convert2RelPath(WebLoadEvent.getOriginalUrl(obtainUrl)))));
    }

    private void b(IWebview iWebview, JSONArray jSONArray, NWindow cVar) {
        if (cVar.F) {
            return;
        }
        JSONObject jSONObject = JSONUtil.getJSONObject(jSONArray, 0);
        boolean a = a(jSONObject, true);
        AdaFrameItem adaFrameItem = (AdaFrameItem) cVar.u;
        if (cVar.G || !jSONObject.isNull("background")) {
            adaFrameItem = cVar.u.obtainWebviewParent();
        }
        adaFrameItem.obtainFrameOptions().allowUpdate = true;
        ViewOptions obtainFrameOptions = adaFrameItem.obtainFrameOptions();
        Logger.d(Logger.VIEW_VISIBLE_TAG, "setOption _old_win_options=" + obtainFrameOptions + ";_new_json_option=" + jSONObject);
        ViewOptions createViewOptionsData = ViewOptions.createViewOptionsData(obtainFrameOptions, obtainFrameOptions.getParentViewRect());
        IWebview obtainWebView = this.u.obtainWebView();
        iWebview.setWebViewCacheMode(createViewOptionsData.mCacheMode);
        JSONUtil.combinJSONObject(this.g, jSONObject);
        a();
        if (!JSONUtil.isNull(jSONObject, "navigationbar")) {
            a(cVar, this.g.optJSONObject("navigationbar"));
        }
        if (cVar.E) {
            this.u.setFrameOptions_Animate(createViewOptionsData);
            if (Build.VERSION.SDK_INT < 23) {
                createViewOptionsData.mWebviewScale = this.u.obtainWebView().getScale();
            }
            int i = createViewOptionsData.background;
            float f = createViewOptionsData.opacity;
            boolean updateViewData = createViewOptionsData.updateViewData(jSONObject);
            boolean z = (PdrUtil.checkAlphaTransparent(i) != PdrUtil.checkAlphaTransparent(createViewOptionsData.background)) | (f != createViewOptionsData.opacity);
            if (jSONObject != null && jSONObject.has(AbsoluteConst.JSON_KEY_RENDER)) {
                cVar.u.setNeedRender(PdrUtil.isEquals(jSONObject.optString(AbsoluteConst.JSON_KEY_RENDER, "onscreen"), "always"));
            }
            adaFrameItem.setFrameOptions_Animate(createViewOptionsData);
            cVar.G = createViewOptionsData.hasBackground();
            if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_SCROLLINDICATOR)) {
                obtainWebView.setScrollIndicator(createViewOptionsData.getScrollIndicator());
            }
            if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_SCALABLE)) {
                obtainWebView.setWebviewProperty(AbsoluteConst.JSON_KEY_SCALABLE, String.valueOf(createViewOptionsData.scalable));
            }
            if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN)) {
                obtainWebView.setWebviewProperty(AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN, String.valueOf(createViewOptionsData.mVideoFullscree));
            }
            obtainWebView.setWebviewProperty("injection", createViewOptionsData.mInjection);
            obtainWebView.setWebviewProperty("plusrequire", createViewOptionsData.mPlusrequire);
            obtainWebView.setWebviewProperty("geolocation", createViewOptionsData.mGeoInject);
            AnimOptions animOptions = adaFrameItem.getAnimOptions();
            if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_TRANSITION)) {
                animOptions.parseTransition(createViewOptionsData.transition);
                if (createViewOptionsData.transition.isNull(AbsoluteConst.TRANS_DURATION)) {
                    animOptions.duration = 0;
                }
            } else {
                animOptions.duration = 0;
            }
            if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_TRANSFORM)) {
                animOptions.parseTransform(createViewOptionsData.transform);
            }
            if (updateViewData || a || z) {
                ((AdaFrameItem) cVar.u).getAnimOptions().mOption = (byte) 2;
            }
            Object[] objArr = new Object[4];
            objArr[0] = cVar.u;
            objArr[1] = Boolean.valueOf(updateViewData);
            objArr[2] = Boolean.valueOf(updateViewData ? false : a);
            objArr[3] = Boolean.valueOf(z);
            this.c.absMgr.processEvent(IMgr.MgrType.WindowMgr, 7, objArr);
            return;
        }
        boolean updateViewData2 = adaFrameItem.obtainFrameOptions().updateViewData(jSONObject);
        adaFrameItem.obtainFrameOptions_Birth().updateViewData(jSONObject);
        if (updateViewData2) {
            ViewOptions obtainFrameOptions2 = adaFrameItem.obtainFrameOptions();
            int i2 = obtainFrameOptions2.left;
            int i3 = obtainFrameOptions2.top;
            int i4 = obtainFrameOptions2.left;
            int i5 = obtainFrameOptions2.width;
            int i6 = obtainFrameOptions2.top;
            int i7 = obtainFrameOptions2.height;
            ViewOptions obtainFrameOptions3 = adaFrameItem.obtainFrameOptions();
            obtainWebView.setScrollIndicator(obtainFrameOptions3.getScrollIndicator());
            obtainWebView.setWebviewProperty(AbsoluteConst.JSON_KEY_SCALABLE, String.valueOf(obtainFrameOptions3.scalable));
            obtainWebView.setWebviewProperty("injection", createViewOptionsData.mInjection);
            obtainWebView.setWebviewProperty("plusrequire", createViewOptionsData.mPlusrequire);
            obtainWebView.setWebviewProperty("geolocation", createViewOptionsData.mGeoInject);
            AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(cVar.u.obtainMainView(), obtainFrameOptions2.left, obtainFrameOptions2.top, obtainFrameOptions2.width, obtainFrameOptions2.height);
        }
    }

    private void a(JSONArray jSONArray, NWindow cVar) {
        String string = JSONUtil.getString(jSONArray, 0);
        String string2 = JSONUtil.getString(jSONArray, 1);
        AnimOptions animOptions = ((AdaFrameItem) cVar.u).getAnimOptions();
        if (!PdrUtil.isEmpty(string2)) {
            animOptions.duration_close = PdrUtil.parseInt(string2, animOptions.duration_close);
        } else {
            animOptions.duration_close = animOptions.duration_show;
        }
        animOptions.setCloseAnimType(string);
        animOptions.mOption = (byte) 3;
        Logger.d(Logger.VIEW_VISIBLE_TAG, "NWindow.hide view=" + cVar.e());
        if (cVar.B) {
            if (cVar.p()) {
                a(JSONUtil.getJSONObject(jSONArray, 2), cVar, string);
                this.c.absMgr.processEvent(IMgr.MgrType.WindowMgr, 23, cVar.u);
            } else {
                onCallBack(AbsoluteConst.EVENTS_WEBVIEW_HIDE, null);
                cVar.u.setVisible(false, true);
            }
            cVar.B = false;
        } else {
            cVar.u.setVisible(false, true);
        }
        cVar.C = true;
    }

    private void n() {
        ((AdaFrameItem) this.u).getAnimOptions().mOption = (byte) 3;
        this.B = false;
        this.C = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(IWebview iWebview, JSONArray jSONArray, NWindow cVar, String str) {
        String str2;
        if (this.c.g(cVar)) {
            Logger.d(Logger.StreamApp_TAG, "showWebview url=" + cVar.v);
            cVar.w = 1;
            this.c.e(cVar);
            cVar.x = new Object[]{iWebview, jSONArray, cVar, str};
            return;
        }
        cVar.q = System.currentTimeMillis();
        cVar.B = true;
        String string = JSONUtil.getString(jSONArray, 0);
        String string2 = JSONUtil.getString(jSONArray, 1);
        String string3 = JSONUtil.getString(jSONArray, 3);
        this.K = string3;
        if (!PdrUtil.isEmpty(string3)) {
            this.L = iWebview;
        }
        AnimOptions animOptions = ((AdaFrameItem) cVar.u).getAnimOptions();
        if (!PdrUtil.isEmpty(string2)) {
            animOptions.duration_show = PdrUtil.parseInt(string2, animOptions.duration_show);
        }
        if (PdrUtil.isEquals("auto", string)) {
            str2 = animOptions.mAnimType;
        } else {
            str2 = PdrUtil.isEmpty(string) ? "none" : string;
        }
        animOptions.mAnimType = str2;
        boolean z = !PdrUtil.isEquals("none", animOptions.mAnimType);
        if (!cVar.C && cVar.E) {
            z = false;
        }
        this.c.a(str, cVar, this.c.a(this));
        a(JSONUtil.getJSONObject(jSONArray, 4), cVar, string);
        if (cVar.C) {
            animOptions.mOption = (byte) 4;
            this.c.absMgr.processEvent(IMgr.MgrType.WindowMgr, 24, cVar.u);
        } else {
            animOptions.mOption = (byte) 0;
            cVar.E = true;
            this.c.absMgr.processEvent(IMgr.MgrType.WindowMgr, 1, new Object[]{cVar.u, Boolean.valueOf(z)});
        }
        cVar.C = false;
        Logger.d(Logger.VIEW_VISIBLE_TAG, "show " + cVar.u + ";webview_name=" + k().obtainFrameId());
    }

    private void b(JSONArray jSONArray, NWindow cVar) {
        if (cVar.E) {
            if (!cVar.F) {
                this.c.d(cVar);
                if (cVar.H) {
                    if (cVar.a != null) {
                        cVar.a.b((NView) cVar);
                    }
                    cVar.e().onDispose();
                    cVar.e().dispose();
                } else {
                    String string = JSONUtil.getString(jSONArray, 0);
                    String string2 = JSONUtil.getString(jSONArray, 1);
                    AnimOptions animOptions = ((AdaFrameItem) cVar.u).getAnimOptions();
                    if (!PdrUtil.isEmpty(string2)) {
                        animOptions.duration_close = PdrUtil.parseInt(string2, animOptions.duration_close);
                    } else {
                        animOptions.duration_close = animOptions.duration_show;
                    }
                    animOptions.setCloseAnimType(PdrUtil.isEmpty(string) ? "auto" : string);
                    animOptions.mOption = (byte) 1;
                    a(JSONUtil.getJSONObject(jSONArray, 2), cVar, string);
                    this.c.absMgr.processEvent(IMgr.MgrType.WindowMgr, 2, cVar.u);
                }
            }
        } else {
            this.c.d(cVar);
            cVar.e().onDispose();
            cVar.e().dispose();
        }
        cVar.g();
    }

    private void o() {
        this.c.d(this);
        if (this.E) {
            if (!this.F) {
                if (this.H) {
                    if (this.a != null) {
                        this.a.b((NView) this);
                    }
                    e().onDispose();
                    e().dispose();
                } else {
                    ((AdaFrameItem) this.u).getAnimOptions().mOption = (byte) 1;
                }
            }
        } else {
            e().onDispose();
            e().dispose();
        }
        g();
    }

    private void a(JSONObject jSONObject, NWindow cVar, String str) {
        if (jSONObject != null) {
            String optString = jSONObject.optString(AbsoluteConst.ACCELERATION);
            String str2 = TextUtils.isEmpty(optString) ? "auto" : optString;
            cVar.u.setAccelerationType(str2);
            IFrameView findPageB = cVar.u.findPageB();
            if (findPageB == null) {
                return;
            }
            findPageB.setAccelerationType(str2);
            if (jSONObject.has(AbsoluteConst.CAPTURE)) {
                INativeBitmap c = c(findPageB.obtainWebView(), jSONObject.optJSONObject(AbsoluteConst.CAPTURE).optString("__id__"));
                cVar.u.setSnapshot(c != null ? c.getBitmap() : null);
            }
            if (jSONObject.has("otherCapture")) {
                INativeBitmap c2 = c(findPageB.obtainWebView(), jSONObject.optJSONObject("otherCapture").optString("__id__"));
                if (findPageB != null) {
                    findPageB.setSnapshot(c2 != null ? c2.getBitmap() : null);
                    return;
                }
                return;
            }
            return;
        }
        cVar.u.setSnapshot(null);
        cVar.u.setAccelerationType("auto");
        IFrameView findPageB2 = cVar.u.findPageB();
        if (findPageB2 != null) {
            findPageB2.setSnapshot(null);
            findPageB2.setAccelerationType("auto");
        }
    }

    void a(final IWebview iWebview, JSONArray jSONArray, NWindow cVar) {
        String string = JSONUtil.getString(jSONArray, 0);
        final String string2 = JSONUtil.getString(jSONArray, 1);
        cVar.u.captureSnapshot(string, TextUtils.isEmpty(string2) ? null : new ICallBack() { // from class: io.dcloud.feature.ui.c.3
            @Override // io.dcloud.common.DHInterface.ICallBack
            public Object onCallBack(int i, Object obj) {
                JSUtil.execCallback(iWebview, string2, null, JSUtil.OK, false, false);
                return null;
            }
        }, TextUtils.isEmpty(string2) ? null : new ICallBack() { // from class: io.dcloud.feature.ui.c.4
            @Override // io.dcloud.common.DHInterface.ICallBack
            public Object onCallBack(int i, Object obj) {
                JSUtil.execCallback(iWebview, string2, "{\"code\":-100,\"message\":\"截图失败\"}", JSUtil.ERROR, true, false);
                return null;
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0081  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void c(final IWebview r12, JSONArray r13, NWindow r14) {
        /*
            r11 = this;
            r14 = 0
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString(r13, r14)
            r1 = 1
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString(r13, r1)
            io.dcloud.feature.ui.a r2 = r11.c
            r3 = 0
            io.dcloud.feature.ui.c r1 = r2.a(r1, r1, r3)
            io.dcloud.common.adapter.ui.AdaFrameItem r1 = r1.e()
            android.view.View r5 = r1.obtainMainView()
            r1 = 2
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString(r13, r1)
            r2 = 3
            org.json.JSONObject r13 = io.dcloud.common.util.JSONUtil.getJSONObject(r13, r2)
            if (r13 == 0) goto L71
            java.lang.String r2 = "check"
            boolean r2 = r13.optBoolean(r2, r14)
            java.lang.String r4 = "clip"
            org.json.JSONObject r13 = r13.optJSONObject(r4)
            if (r13 == 0) goto L6f
            int r4 = r5.getWidth()
            int r6 = r5.getHeight()
            float r7 = r12.getScale()
            java.lang.String r8 = "left"
            java.lang.String r8 = r13.optString(r8)
            int r8 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r8, r4, r14, r7)
            java.lang.String r9 = "top"
            java.lang.String r9 = r13.optString(r9)
            int r14 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r9, r6, r14, r7)
            java.lang.String r9 = "width"
            java.lang.String r9 = r13.optString(r9)
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r9, r4, r4, r7)
            java.lang.String r9 = "height"
            java.lang.String r13 = r13.optString(r9)
            int r13 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r13, r6, r6, r7)
            android.graphics.Rect r6 = new android.graphics.Rect
            r6.<init>(r8, r14, r4, r13)
            r7 = r2
            r8 = r6
            goto L73
        L6f:
            r7 = r2
            goto L72
        L71:
            r7 = r14
        L72:
            r8 = r3
        L73:
            io.dcloud.common.DHInterface.IFrameView r4 = r12.obtainFrameView()
            io.dcloud.common.DHInterface.INativeBitmap r6 = r11.c(r12, r0)
            boolean r13 = android.text.TextUtils.isEmpty(r1)
            if (r13 == 0) goto L83
            r9 = r3
            goto L89
        L83:
            io.dcloud.feature.ui.c$5 r13 = new io.dcloud.feature.ui.c$5
            r13.<init>()
            r9 = r13
        L89:
            boolean r13 = android.text.TextUtils.isEmpty(r1)
            if (r13 == 0) goto L90
            goto L95
        L90:
            io.dcloud.feature.ui.c$6 r3 = new io.dcloud.feature.ui.c$6
            r3.<init>()
        L95:
            r10 = r3
            r4.draw(r5, r6, r7, r8, r9, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.c(io.dcloud.common.DHInterface.IWebview, org.json.JSONArray, io.dcloud.feature.ui.c):void");
    }

    private INativeBitmap c(IWebview iWebview, String str) {
        return (INativeBitmap) iWebview.obtainApp().obtainMgrData(IMgr.MgrType.FeatureMgr, 10, new Object[]{iWebview, "nativeobj", "getNativeBitmap", new String[]{iWebview.obtainApp().obtainAppId(), str}});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(String str, String str2, boolean z) {
        ArrayList<NView> arrayList = this.I;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                NView bVar = this.I.get(size);
                if (bVar instanceof NWindow) {
                    NWindow cVar = (NWindow) bVar;
                    if (cVar.B && cVar.b(str, str2, z)) {
                        return true;
                    }
                }
            }
        }
        return a(str) && a(str, str2, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c(String str) {
        ArrayList<NView> arrayList = this.I;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                NView bVar = this.I.get(size);
                if ((bVar instanceof NWindow) && ((NWindow) bVar).a(str)) {
                    return true;
                }
            }
        }
        return a(str);
    }

    private boolean p() {
        if (this.a != null) {
            return this.a.B && this.a.p();
        }
        return true;
    }

    private void a(NWindow cVar, boolean z) {
        cVar.u.obtainWebView().reload(z);
    }

    public IWebview k() {
        return this.u.obtainWebView();
    }

    public String l() {
        IWebview obtainWebView = this.u.obtainWebView();
        if (obtainWebView != null) {
            return obtainWebView.obtainFrameId();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(JSONObject jSONObject, boolean z) {
        if (jSONObject.isNull(AbsoluteConst.JSON_KEY_ZINDEX)) {
            return false;
        }
        try {
            int parseInt = Integer.parseInt(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_ZINDEX));
            if (parseInt == this.A) {
                return false;
            }
            try {
                this.A = parseInt;
                ((AdaFrameView) this.u).mZIndex = parseInt;
                if (z) {
                    this.c.b(this);
                }
            } catch (Exception unused) {
            }
            return true;
        } catch (Exception unused2) {
            return false;
        }
    }

    @Override // io.dcloud.feature.ui.b
    public String d() {
        if (PdrUtil.isEmpty(k().obtainFrameId())) {
            Object[] objArr = new Object[4];
            objArr[0] = this.e;
            objArr[1] = "undefined";
            objArr[2] = this.d;
            JSONObject jSONObject = this.t;
            objArr[3] = jSONObject != null ? jSONObject.toString() : "{}";
            return String.format("(function(){return {'uuid':'%s','id':%s,'identity':'%s','extras':%s}})()", objArr);
        }
        Object[] objArr2 = new Object[4];
        objArr2[0] = this.e;
        objArr2[1] = k().obtainFrameId();
        objArr2[2] = this.d;
        JSONObject jSONObject2 = this.t;
        objArr2[3] = jSONObject2 != null ? jSONObject2.toString() : "{}";
        return String.format("(function(){return {'uuid':'%s','id':'%s','identity':'%s','extras':%s}})()", objArr2);
    }

    private static String a(ArrayList arrayList) {
        StringBuffer stringBuffer = new StringBuffer("[");
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                NView bVar = (NView) arrayList.get(i);
                if (bVar instanceof NWindow) {
                    stringBuffer.append(((NWindow) bVar).d());
                } else {
                    stringBuffer.append("'" + bVar.e + "'");
                }
                if (i != size - 1) {
                    stringBuffer.append(JSUtil.COMMA);
                }
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.dcloud.feature.ui.b
    public void g() {
        ArrayList<NWindow> arrayList;
        NWindow cVar = this.R;
        if (cVar != null && (arrayList = cVar.U) != null) {
            arrayList.remove(this);
        }
        this.R = null;
        this.a = null;
        ArrayList<NView> arrayList2 = this.I;
        if (arrayList2 != null) {
            Iterator<NView> it = arrayList2.iterator();
            while (it.hasNext()) {
                it.next().g();
            }
            this.I.clear();
            this.I = null;
        }
        this.L = null;
        this.K = null;
        this.M = null;
        this.N = null;
        this.X = 150;
    }

    public void m() {
        if (this.i == null || !this.i.containsKey(AbsoluteConst.EVENTS_WEBVIEW_RENDERED)) {
            return;
        }
        Runnable runnable = this.S;
        if (runnable != null) {
            MessageHandler.removeCallbacks(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: io.dcloud.feature.ui.c.7
            @Override // java.lang.Runnable
            public void run() {
                if (NWindow.this.u.obtainWebView().checkWhite(NWindow.this.W)) {
                    NWindow.this.m();
                } else {
                    NWindow.this.b(AbsoluteConst.EVENTS_WEBVIEW_RENDERED, (String) null);
                }
                NWindow.this.S = null;
            }
        };
        this.S = runnable2;
        MessageHandler.postDelayed(runnable2, this.X);
    }

    private void a(NWindow cVar, JSONObject jSONObject) {
        String valueOf = String.valueOf(cVar.k().obtainWebview().hashCode());
        if (jSONObject != null) {
            String optString = jSONObject.optString("backgroundcolor");
            String optString2 = jSONObject.optString("titletext");
            String optString3 = jSONObject.optString("titlecolor");
            if (!TextUtils.isEmpty(optString)) {
                this.c.absMgr.processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{cVar.k(), "nativeobj", "setStyle", JSONUtil.createJSONArray("['" + valueOf + "','" + valueOf + "',{'backgroudColor':'" + optString + "'}]")});
            }
            if (TextUtils.isEmpty(optString2) || TextUtils.isEmpty(optString3)) {
                return;
            }
            this.c.absMgr.processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{cVar.k(), "nativeobj", "drawText", JSONUtil.createJSONArray("['" + valueOf + "','" + valueOf + "','" + optString2 + "',{'top':'0px','left':'0px','width':'100%','height':'100%'},{'size':'16px','color':'" + optString3 + "'},'" + valueOf + "',null]")});
        }
    }
}
