package io.dcloud.common.common_b.common_b_b;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;

import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.adapter.util.DragBean;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import android.animation.Animator;
import android.animation.ValueAnimator;

/* compiled from: Drag.java */
/* loaded from: classes.dex */
public class h {
    private static boolean O = false;
    private IFrameView E;
    private String F;
    private String G;
    private String H;
    private float I;
    private int J;
    private int K;
    private int P;
    private DisplayMetrics Q;
    private VelocityTracker a;
    private int c;
    private float d;
    private float e;
    private float f;
    private int g;
    private DHFrameView l;
    private DHFrameView m;
    private View o;
    private String r;
    private String t;
    private String v;
    private String x;
    private String y;
    private boolean b = true;
    private int h = 0;
    private int i = 0;
    private int j = 0;
    private int k = 0;
    private View n = null;
    private boolean p = false;
    private int q = Integer.MAX_VALUE;
    private int s = Integer.MAX_VALUE;
    private int u = Integer.MAX_VALUE;
    private int w = Integer.MAX_VALUE;
    private boolean z = false;
    private boolean A = true;
    private boolean B = true;
    private boolean C = true;
    private int D = -1;
    private boolean L = false;
    private boolean M = false;
    private int N = Integer.MAX_VALUE;
    private boolean R = true;
    private boolean S = true;
    private boolean T = true;
    private boolean U = true;
    private boolean V = true;
    private boolean W = true;

    static /* synthetic */ int j(h hVar) {
        int i = hVar.P;
        hVar.P = i + 1;
        return i;
    }

    static /* synthetic */ int t(h hVar) {
        int i = hVar.P;
        hVar.P = i - 1;
        return i;
    }

    public h(IFrameView iFrameView, Context context) {
        this.P = 0;
        O = false;
        this.P = 0;
        if (iFrameView instanceof DHFrameView) {
            this.l = (DHFrameView) iFrameView;
            if (this.Q == null) {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                this.Q = displayMetrics;
                this.J = displayMetrics.widthPixels;
                this.K = this.Q.widthPixels;
            }
        }
        this.c = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public HashMap<String, DragBean> a() {
        DHFrameView dVar = this.l;
        if (dVar == null || dVar.obtainFrameOptions() == null) {
            return null;
        }
        return this.l.obtainFrameOptions().dragData;
    }

    public void a(MotionEvent motionEvent) {
        DisplayMetrics displayMetrics = this.Q;
        if (displayMetrics != null) {
            this.J = displayMetrics.widthPixels;
        }
        this.I = motionEvent.getRawX();
        float rawX = motionEvent.getRawX();
        this.d = rawX;
        this.f = rawX;
        this.e = motionEvent.getRawY();
        this.b = false;
        this.z = false;
        this.p = false;
        this.D = -1;
        this.l.obtainWebView().obtainWebview().loadUrl("javascript:window.__needNotifyNative__=true;");
        this.l.obtainWebView().setWebviewProperty("needTouchEvent", AbsoluteConst.FALSE);
        if (this.a == null) {
            this.a = VelocityTracker.obtain();
        }
        this.a.addMovement(motionEvent);
    }

    public boolean b(MotionEvent motionEvent) {
        View view;
        View view2;
        if (a() == null) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        if (actionMasked == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        if (actionMasked == 0) {
            a(motionEvent);
        } else if (actionMasked == 1) {
            this.p = false;
            this.D = -1;
        } else if (actionMasked == 2) {
            float f = rawX - this.d;
            if (Math.abs(f) >= Math.abs(rawY - this.e)) {
                if (((float) (this.c * 3)) <= Math.abs(motionEvent.getRawX() - this.I) && AbsoluteConst.FALSE.equals(this.l.obtainWebView().getWebviewProperty("needTouchEvent"))) {
                    String str = "right";
                    if (f >= 0.0f) {
                        if (!this.p && b("right")) {
                            this.G = "right";
                            this.b = true;
                            this.p = true;
                        }
                    } else {
                        if (!this.p && b("left")) {
                            this.G = "left";
                            this.b = true;
                            this.p = true;
                        }
                        str = "left";
                    }
                    if (-1 == this.D) {
                        this.d = rawX;
                        View a = a(str);
                        if (a != null) {
                            HashMap<String, DragBean> a2 = a();
                            String e = e(str);
                            if (a2 != null && a2.containsKey(e)) {
                                a(a2.get(e).dragBindViewOp);
                            }
                            this.G = str;
                            this.b = true;
                            this.o = a;
                            this.M = true;
                            this.D = 1;
                        } else {
                            c(this.G);
                        }
                    }
                }
            }
        }
        if (this.b) {
            if (this.M) {
                this.b = j();
            } else {
                boolean z = this.z;
                if (z && this.A && this.B) {
                    if ((!a(this.l) && !a(this.m)) || ((view2 = this.o) != null && (view2 instanceof INativeView))) {
                        this.b = g();
                    }
                } else if (z && !this.A && this.B) {
                    if ((!a(this.l) && !a(this.m)) || ((view = this.o) != null && (view instanceof INativeView))) {
                        this.b = i();
                    }
                } else if (!z && this.A) {
                    if (!a(this.l)) {
                        h();
                    }
                } else if (z && !this.B && this.A) {
                    if (!a(this.l) && !a(this.m)) {
                        this.b = h();
                    }
                } else {
                    this.b = false;
                }
            }
        }
        if (this.b) {
            a("start", false, "0");
        }
        return this.b;
    }

    private boolean b(String str) {
        JSONObject jSONObject;
        JSONObject jSONObject2;
        boolean z;
        this.n = this.l.obtainMainView();
        this.q = Integer.MAX_VALUE;
        this.s = Integer.MAX_VALUE;
        this.r = null;
        this.t = null;
        HashMap<String, DragBean> a = a();
        if (a == null || !a.containsKey(str)) {
            return false;
        }
        DragBean dragBean = a.get(str);
        if (dragBean != null) {
            this.F = dragBean.dragCbId;
            this.E = dragBean.dragCallBackWebView;
            JSONObject jSONObject3 = dragBean.dragCurrentViewOp;
            try {
                this.H = JSONUtil.getString(jSONObject3, "direction");
                if (jSONObject3.has("moveMode")) {
                    String string = JSONUtil.getString(jSONObject3, "moveMode");
                    this.y = string;
                    if (!"followFinger".equalsIgnoreCase(string) && !"follow".equalsIgnoreCase(this.y) && !AbsoluteConst.JSON_KEY_BOUNCE.equalsIgnoreCase(this.y)) {
                        z = false;
                        this.A = z;
                    }
                    z = true;
                    this.A = z;
                }
                if (jSONObject3.has("over") && (jSONObject2 = JSONUtil.getJSONObject(jSONObject3, "over")) != null) {
                    if (jSONObject2.has("left")) {
                        this.q = PdrUtil.parseInt(JSONUtil.getString(jSONObject2, "left"), this.J, Integer.MAX_VALUE);
                    }
                    if (jSONObject2.has("action")) {
                        this.r = JSONUtil.getString(jSONObject2, "action");
                    }
                }
                if (jSONObject3.has("cancel") && (jSONObject = JSONUtil.getJSONObject(jSONObject3, "cancel")) != null) {
                    if (jSONObject.has("left")) {
                        this.s = PdrUtil.parseInt(JSONUtil.getString(jSONObject, "left"), this.J, Integer.MAX_VALUE);
                    }
                    if (jSONObject.has("action")) {
                        this.t = JSONUtil.getString(jSONObject, "action");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void a(JSONObject jSONObject) {
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        if (jSONObject != null) {
            this.u = Integer.MAX_VALUE;
            this.w = Integer.MAX_VALUE;
            this.v = null;
            this.x = null;
            this.B = "follow".equalsIgnoreCase(JSONUtil.getString(jSONObject, "moveMode"));
            if (jSONObject.has("over") && (jSONObject3 = JSONUtil.getJSONObject(jSONObject, "over")) != null) {
                if (jSONObject3.has("left")) {
                    this.u = PdrUtil.parseInt(JSONUtil.getString(jSONObject3, "left"), this.J, Integer.MAX_VALUE);
                }
                if (jSONObject3.has("action")) {
                    this.v = JSONUtil.getString(jSONObject3, "action");
                }
            }
            if (!jSONObject.has("cancel") || (jSONObject2 = JSONUtil.getJSONObject(jSONObject, "cancel")) == null) {
                return;
            }
            if (jSONObject2.has("left")) {
                this.w = PdrUtil.parseInt(JSONUtil.getString(jSONObject2, "left"), this.J, Integer.MAX_VALUE);
            }
            if (jSONObject2.has("action")) {
                this.x = JSONUtil.getString(jSONObject2, "action");
            }
        }
    }

    private boolean a(View view, View view2) {
        if (view != null && view2 != null) {
            ViewParent parent = view2.getParent();
            for (ViewParent parent2 = view.getParent(); parent2 != null && parent2 != view2; parent2 = parent2.getParent()) {
                while (parent != null) {
                    if (parent == view) {
                        return false;
                    }
                    if (parent2 == parent) {
                        return true;
                    }
                    parent = parent.getParent();
                }
                parent = view2.getParent();
            }
            return false;
        }
        return false;
    }

    private int c(String str) {
        View view;
        DragBean dragBean;
        int i = this.D;
        if (i != -1) {
            return i;
        }
        this.m = null;
        this.o = null;
        HashMap<String, DragBean> a = a();
        if (a != null && a.containsKey(str) && (dragBean = a.get(str)) != null) {
            a(dragBean.dragBindViewOp);
            IFrameView iFrameView = dragBean.dragBindWebView;
            if (iFrameView != null && (iFrameView instanceof DHFrameView)) {
                this.z = true;
                DHFrameView dVar = (DHFrameView) iFrameView;
                this.m = dVar;
                this.o = dVar.obtainMainView();
            } else {
                View view2 = dragBean.nativeView;
                this.o = view2;
                if (view2 != null) {
                    this.z = true;
                }
            }
        }
        if (!a(this.m) || ((view = this.o) != null && (view instanceof INativeView))) {
            if (this.o.getVisibility() != View.VISIBLE) {
                this.m = null;
                this.o = null;
                return 0;
            }
            if (this.o.getParent() == null && (this.n.getParent() instanceof FrameLayout) && !(this.o instanceof INativeView)) {
                this.m.pushToViewStack();
            }
            if (!a(this.n, this.o) && !(this.o instanceof INativeView)) {
                this.m = null;
                this.o = null;
                return 0;
            }
            if (!(this.o instanceof INativeView) && this.B && this.n.getParent() != this.o.getParent()) {
                this.D = 0;
                return 0;
            }
            View view3 = this.o;
            if (view3 instanceof INativeView) {
                view3.bringToFront();
            }
            int b = b(this.o);
            int b2 = b(this.n);
            if (b2 == 0) {
                int width = this.n.getWidth();
                int i2 = this.J;
                if (width == i2 && (b >= i2 || b <= (-a(this.o)))) {
                    this.N = b;
                    boolean z = this.A;
                    if (z && this.B) {
                        if ("right".equals(str)) {
                            this.g = b2 - a(this.o);
                        } else if ("left".equals(str)) {
                            this.g = b2 + this.n.getWidth();
                        }
                        b(this.o, this.g);
                    } else if (!z && this.B) {
                        if ("right".equals(str)) {
                            this.g = -a(this.o);
                        } else if ("left".equals(str)) {
                            this.g = this.n.getWidth();
                        }
                        b(this.o, this.g);
                    }
                }
            }
            this.D = 1;
        } else {
            this.D = 0;
        }
        return this.D;
    }

    public boolean c(MotionEvent motionEvent) {
        View view;
        View view2;
        if (this.a == null) {
            this.a = VelocityTracker.obtain();
        }
        if (this.D == -1 || O) {
            return false;
        }
        if (2 == motionEvent.getAction()) {
            e();
            if ("left".equals(this.G)) {
                if (motionEvent.getRawX() < this.d) {
                    this.a.addMovement(motionEvent);
                }
            } else if ("right".equals(this.G) && motionEvent.getRawX() > this.d) {
                this.a.addMovement(motionEvent);
            }
        }
        if (1 == motionEvent.getAction() || 3 == motionEvent.getAction()) {
            this.a.addMovement(motionEvent);
        }
        if (this.M) {
            return g(motionEvent);
        }
        boolean z = this.z;
        if (z && this.A && this.B) {
            if ((!a(this.l) && !a(this.m)) || ((view2 = this.o) != null && (view2 instanceof INativeView))) {
                return f(motionEvent);
            }
        } else if (z && !this.A && this.B) {
            if ((!a(this.l) && !a(this.m)) || ((view = this.o) != null && (view instanceof INativeView))) {
                return d(motionEvent);
            }
        } else if (z && !this.B && this.A) {
            if (!a(this.l) && !a(this.m)) {
                return e(motionEvent);
            }
        } else if (!z && this.A && !a(this.l)) {
            return e(motionEvent);
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0038, code lost:
    
        if (r0 < r4) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0052, code lost:
    
        if (r0 < r4) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x000f, code lost:
    
        if (r4 != 3) goto L29;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean d(android.view.MotionEvent r4) {
        /*
            r3 = this;
            float r0 = r4.getRawX()
            int r4 = r4.getAction()
            r1 = 1
            if (r4 == r1) goto L5c
            r2 = 2
            if (r4 == r2) goto L12
            r0 = 3
            if (r4 == r0) goto L5c
            goto L63
        L12:
            float r4 = r3.d
            float r4 = r0 - r4
            r3.d = r0
            android.view.View r0 = r3.o
            int r0 = r3.b(r0)
            float r0 = (float) r0
            float r0 = r0 + r4
            java.lang.String r4 = r3.G
            java.lang.String r2 = "right"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L3b
            int r4 = r3.k
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L33
        L31:
            float r0 = (float) r4
            goto L55
        L33:
            int r4 = r3.j
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L55
            goto L31
        L3b:
            java.lang.String r4 = r3.G
            java.lang.String r2 = "left"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L55
            int r4 = r3.j
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L4d
            goto L31
        L4d:
            int r4 = r3.k
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L55
            goto L31
        L55:
            android.view.View r4 = r3.o
            int r0 = (int) r0
            r3.b(r4, r0)
            goto L63
        L5c:
            boolean r4 = r3.C
            if (r4 == 0) goto L63
            r3.b()
        L63:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.h.d(android.view.MotionEvent):boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x003c, code lost:
    
        if (r0 < r4) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0056, code lost:
    
        if (r0 < r4) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x000f, code lost:
    
        if (r4 != 3) goto L29;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean e(android.view.MotionEvent r4) {
        /*
            r3 = this;
            float r0 = r4.getRawX()
            int r4 = r4.getAction()
            r1 = 1
            if (r4 == r1) goto L60
            r2 = 2
            if (r4 == r2) goto L12
            r0 = 3
            if (r4 == r0) goto L60
            goto L67
        L12:
            float r4 = r3.d
            float r4 = r0 - r4
            r3.d = r0
            float r4 = r3.a(r4)
            android.view.View r0 = r3.n
            int r0 = r3.b(r0)
            float r0 = (float) r0
            float r0 = r0 + r4
            java.lang.String r4 = r3.G
            java.lang.String r2 = "right"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L3f
            int r4 = r3.i
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L37
        L35:
            float r0 = (float) r4
            goto L59
        L37:
            int r4 = r3.h
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L59
            goto L35
        L3f:
            java.lang.String r4 = r3.G
            java.lang.String r2 = "left"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L59
            int r4 = r3.h
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L51
            goto L35
        L51:
            int r4 = r3.i
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L59
            goto L35
        L59:
            android.view.View r4 = r3.n
            int r0 = (int) r0
            r3.b(r4, r0)
            goto L67
        L60:
            boolean r4 = r3.C
            if (r4 == 0) goto L67
            r3.c()
        L67:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.h.e(android.view.MotionEvent):boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0058, code lost:
    
        if (r5 <= r2) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0047, code lost:
    
        if (r0 <= r2) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0083, code lost:
    
        if (r5 <= r2) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0073, code lost:
    
        if (r0 <= r2) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x000f, code lost:
    
        if (r5 != 3) goto L41;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean f(android.view.MotionEvent r5) {
        /*
            r4 = this;
            float r0 = r5.getRawX()
            int r5 = r5.getAction()
            r1 = 1
            if (r5 == r1) goto L93
            r2 = 2
            if (r5 == r2) goto L13
            r0 = 3
            if (r5 == r0) goto L93
            goto L9a
        L13:
            float r5 = r4.d
            float r5 = r0 - r5
            int r5 = (int) r5
            float r5 = (float) r5
            r4.d = r0
            float r5 = r4.a(r5)
            android.view.View r0 = r4.n
            int r0 = r4.b(r0)
            float r0 = (float) r0
            float r0 = r0 + r5
            android.view.View r2 = r4.o
            int r2 = r4.b(r2)
            float r2 = (float) r2
            float r5 = r5 + r2
            java.lang.String r2 = r4.G
            java.lang.String r3 = "right"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L5b
            int r2 = r4.i
            float r3 = (float) r2
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 < 0) goto L42
        L40:
            float r0 = (float) r2
            goto L4a
        L42:
            int r2 = r4.h
            float r3 = (float) r2
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 > 0) goto L4a
            goto L40
        L4a:
            int r2 = r4.k
            float r3 = (float) r2
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 < 0) goto L53
        L51:
            float r5 = (float) r2
            goto L86
        L53:
            int r2 = r4.j
            float r3 = (float) r2
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 > 0) goto L86
            goto L51
        L5b:
            java.lang.String r2 = r4.G
            java.lang.String r3 = "left"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L86
            int r2 = r4.h
            float r3 = (float) r2
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 < 0) goto L6e
        L6c:
            float r0 = (float) r2
            goto L76
        L6e:
            int r2 = r4.i
            float r3 = (float) r2
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 > 0) goto L76
            goto L6c
        L76:
            int r2 = r4.j
            float r3 = (float) r2
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 < 0) goto L7e
            goto L51
        L7e:
            int r2 = r4.k
            float r3 = (float) r2
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 > 0) goto L86
            goto L51
        L86:
            android.view.View r2 = r4.n
            int r0 = (int) r0
            r4.b(r2, r0)
            android.view.View r0 = r4.o
            int r5 = (int) r5
            r4.b(r0, r5)
            goto L9a
        L93:
            boolean r5 = r4.C
            if (r5 == 0) goto L9a
            r4.d()
        L9a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.h.f(android.view.MotionEvent):boolean");
    }

    private void b() {
        this.L = true;
        O = true;
        View view = this.o;
        if (view != null) {
            view.requestLayout();
            this.o.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: io.dcloud.common.b.b.h.1
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    int i;
                    if (Build.VERSION.SDK_INT >= 16) {
                        h.this.o.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    h hVar = h.this;
                    boolean z = true;
                    if (hVar.d(hVar.G)) {
                        i = h.this.k;
                    } else {
                        h hVar2 = h.this;
                        int a = hVar2.a(hVar2.o) / 2;
                        Rect rect = new Rect();
                        h.this.o.getGlobalVisibleRect(rect);
                        if (rect.right - rect.left >= a) {
                            i = h.this.k;
                        } else {
                            z = false;
                            i = h.this.j;
                        }
                    }
                    h hVar3 = h.this;
                    int b = hVar3.b(hVar3.o);
                    h hVar4 = h.this;
                    hVar4.a(hVar4.o, b, i, z).start();
                }
            });
        }
    }

    private void c() {
        this.L = true;
        O = true;
        this.n.requestLayout();
        this.n.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: io.dcloud.common.b.b.h.2
            /* JADX WARN: Removed duplicated region for block: B:10:0x0172  */
            /* JADX WARN: Removed duplicated region for block: B:16:0x01b7  */
            /* JADX WARN: Removed duplicated region for block: B:20:0x01c7  */
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onGlobalLayout() {
                /*
                    Method dump skipped, instructions count: 482
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.h.AnonymousClass2.onGlobalLayout():void");
            }
        });
    }

    private void d() {
        this.L = true;

        this.n.requestLayout();
        this.n.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: io.dcloud.common.b.b.h.3
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                boolean o = true;
                int i;
                int i2;
                int i3;
                int i4;
                int i5;
                int i6;
                int i7;
                if (Build.VERSION.SDK_INT >= 16) {
                    h.this.n.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                h hVar = h.this;
                boolean z = true;
                int i8 = 0;
                if (hVar.d(hVar.G)) {
                    i = h.this.i;
                    i4 = h.this.k;
                    if (AbsoluteConst.JSON_KEY_BOUNCE.equalsIgnoreCase(h.this.y)) {
                        i = h.this.h;
                        i4 = h.this.j;
                        h.this.y = null;
                        z = false;
                    }
                    h hVar2 = h.this;
                    int b = hVar2.b(hVar2.n);
                    h hVar3 = h.this;
                    hVar3.a(hVar3.n, b, i, z).start();
                    h hVar4 = h.this;
                    int b2 = hVar4.b(hVar4.o);
                    h hVar5 = h.this;
                    hVar5.a(hVar5.o, b2, i4, z).start();
                }
                if (AbsoluteConst.JSON_KEY_BOUNCE.equalsIgnoreCase(h.this.y)) {
                    i = h.this.h;
                    i4 = h.this.j;
                    h.this.y = null;
                } else {
                    int i9 = h.this.J;
                    h hVar6 = h.this;
                    int b3 = hVar6.b(hVar6.o);
                    h hVar7 = h.this;
                    int a = hVar7.a(hVar7.o) + b3;
                    h hVar8 = h.this;
                    hVar8.b(hVar8.n);
                    h.this.n.getWidth();
                    if (h.this.n.getWidth() >= h.this.J) {
                        if (b3 <= 0 && a > 0) {
                            int i10 = a - 0;
                            if (i10 > 0) {
                                h hVar9 = h.this;
                                if (i10 < hVar9.a(hVar9.o)) {
                                    h hVar10 = h.this;
                                    if (i10 >= hVar10.a(hVar10.o) / 2) {
                                        i = h.this.i;
                                        i3 = h.this.k;
                                        i4 = i3;
                                    } else {
                                        i = h.this.h;
                                        i2 = h.this.j;
                                        i4 = i2;
                                    }
                                }
                            }
                            h hVar11 = h.this;
                            if (i10 == hVar11.a(hVar11.o)) {
                                h.this.b(true);
                                h hVar12 = h.this;
                                hVar12.a(hVar12.n, h.this.i);
                                h hVar13 = h.this;
                                hVar13.a(hVar13.o, h.this.k);
                                o = false;
                                return;
                            }
                            h.this.b(false);
                            h hVar14 = h.this;
                            hVar14.a(hVar14.n, h.this.h);
                            h hVar15 = h.this;
                            hVar15.a(hVar15.o, h.this.j);
                            o = false;
                            return;
                        }
                        if (b3 >= i9 || i9 > a) {
                            h.this.b(false);
                            h hVar16 = h.this;
                            hVar16.a(hVar16.n, h.this.h);
                            h hVar17 = h.this;
                            hVar17.a(hVar17.o, h.this.j);
                            o = false;
                            return;
                        }
                        int i11 = i9 - b3;
                        if (i11 > 0) {
                            h hVar18 = h.this;
                            if (i11 < hVar18.a(hVar18.o)) {
                                h hVar19 = h.this;
                                if (i11 >= hVar19.a(hVar19.o) / 2) {
                                    i = h.this.i;
                                    i3 = h.this.k;
                                    i4 = i3;
                                } else {
                                    i = h.this.h;
                                    i2 = h.this.j;
                                    i4 = i2;
                                }
                            }
                        }
                        h hVar20 = h.this;
                        if (i11 == hVar20.a(hVar20.o)) {
                            h.this.b(true);
                            h hVar21 = h.this;
                            hVar21.a(hVar21.n, h.this.i);
                            h hVar22 = h.this;
                            hVar22.a(hVar22.o, h.this.k);
                            o = false;
                            return;
                        }
                        h.this.b(false);
                        h hVar23 = h.this;
                        hVar23.a(hVar23.n, h.this.h);
                        h hVar24 = h.this;
                        hVar24.a(hVar24.o, h.this.j);
                        o = false;
                        return;
                    }
                    Rect rect = new Rect();
                    h.this.n.getGlobalVisibleRect(rect);
                    int i12 = rect.right - rect.left;
                    int width = h.this.n.getWidth() / 2;
                    if (rect.left != 0) {
                        if (h.this.J != rect.right) {
                            i5 = 0;
                            z = false;
                        } else if (i12 <= width) {
                            i8 = h.this.i;
                            i5 = h.this.k;
                        } else {
                            i6 = h.this.h;
                            i7 = h.this.j;
                            z = false;
                            i8 = i6;
                            i5 = i7;
                        }
                        i4 = i5;
                        i = i8;
                    } else if (i12 <= width) {
                        i8 = h.this.i;
                        i5 = h.this.k;
                        i4 = i5;
                        i = i8;
                    } else {
                        i6 = h.this.h;
                        i7 = h.this.j;
                        z = false;
                        i8 = i6;
                        i5 = i7;
                        i4 = i5;
                        i = i8;
                    }
                    h hVar25 = h.this;
                    int b4 = hVar25.b(hVar25.n);
                    h hVar32 = h.this;
                    hVar32.a(hVar32.n, b4, i, z).start();
                    h hVar42 = h.this;
                    int b22 = hVar42.b(hVar42.o);
                    h hVar52 = h.this;
                    hVar52.a(hVar52.o, b22, i4, z).start();
                }
                z = false;
                h hVar252 = h.this;
                int b42 = hVar252.b(hVar252.n);
                h hVar322 = h.this;
                hVar322.a(hVar322.n, b42, i, z).start();
                h hVar422 = h.this;
                int b222 = hVar422.b(hVar422.o);
                h hVar522 = h.this;
                hVar522.a(hVar522.o, b222, i4, z).start();
            }
        });
    }

    private void e() {
        String str;
        float abs = Math.abs(b(this.n) - this.h);
        int abs2 = Math.abs(this.i - this.h);
        if (0.0f <= abs && abs < abs2 * 0.2f && this.R) {
            this.R = false;
            this.S = true;
            this.T = true;
            this.U = true;
            this.V = true;
            this.W = true;
            str = "0";
        } else {
            float f = abs2;
            if (0.2f * f <= abs && abs < f * 0.4f && this.S) {
                this.S = false;
                this.R = true;
                this.T = true;
                this.U = true;
                this.V = true;
                this.W = true;
                str = "20";
            } else if (0.4f * f <= abs && abs < f * 0.6f && this.T) {
                this.T = false;
                this.R = true;
                this.S = true;
                this.U = true;
                this.V = true;
                this.W = true;
                str = "40";
            } else if (0.6f * f <= abs && abs < f * 0.8f && this.U) {
                this.U = false;
                this.R = true;
                this.S = true;
                this.T = true;
                this.V = true;
                this.W = true;
                str = "60";
            } else if (0.8f * f <= abs && abs < f && this.V) {
                this.V = false;
                this.R = true;
                this.S = true;
                this.T = true;
                this.U = true;
                this.W = true;
                str = "80";
            } else if (f > abs || !this.W) {
                str = null;
            } else {
                this.W = false;
                this.R = true;
                this.S = true;
                this.T = true;
                this.U = true;
                this.V = true;
                str = "100";
            }
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        a("move", false, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z) {
        if (this.L) {
            this.L = false;
            a("end", z, z ? "100" : "0");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z, boolean z2) {
        if (this.L) {
            this.L = false;
            a("end", z, z2, z ? "100" : "0");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(View view, int i) {
        DHFrameView dVar;
        DHFrameView dVar2;
        if (view != null) {
            if (view == this.n && (dVar2 = this.l) != null) {
                dVar2.obtainFrameOptions().left = i;
                this.l.obtainFrameOptions().checkValueIsPercentage("left", i, this.J, true, true);
            } else {
                if (view != this.o || (dVar = this.m) == null) {
                    return;
                }
                dVar.obtainFrameOptions().left = i;
                this.m.obtainFrameOptions().checkValueIsPercentage("left", i, this.J, true, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ValueAnimator a(View view, int i, int i2, boolean z) {
        return a(view, i, i2, z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ValueAnimator a(final View view, int i, final int i2, final boolean z, final boolean z2) {
        ValueAnimator ofFloat;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (view instanceof INativeView) {
            ofFloat = ValueAnimator.ofInt(i, i2);
        } else if (layoutParams instanceof AbsoluteLayout.LayoutParams) {
            ofFloat = ValueAnimator.ofInt(i, i2);
        } else {
            ofFloat = layoutParams instanceof FrameLayout.LayoutParams ? ValueAnimator.ofFloat(i, i2) : null;
        }
        ofFloat.setDuration(Math.min(Math.max(new BigDecimal(450).multiply(new BigDecimal(Math.abs(i2 - i)).divide(new BigDecimal(this.J), 4, 4)).longValue(), 200L), 250L));
        ofFloat.addListener(new Animator.AnimatorListener() { // from class: io.dcloud.common.b.b.h.4
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
//                h.j(h.this);
            }

            /* JADX WARN: Removed duplicated region for block: B:34:0x014f  */
            @Override // android.animation.Animator.AnimatorListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onAnimationEnd(android.animation.Animator r12) {
                /*
                    Method dump skipped, instructions count: 549
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.h.AnonymousClass4.onAnimationEnd(android.animation.Animator):void");
            }
        });
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: io.dcloud.common.b.b.h.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
                if (view instanceof INativeView) {
                    h.this.b(view, ((Integer) valueAnimator.getAnimatedValue()).intValue());
                    view.requestLayout();
                    view.invalidate();
                } else if (layoutParams2 instanceof FrameLayout.LayoutParams) {
                    view.setX(((Float) valueAnimator.getAnimatedValue()).floatValue());
                } else if (layoutParams2 instanceof AbsoluteLayout.LayoutParams) {
                    ((AbsoluteLayout.LayoutParams) layoutParams2).x = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    view.requestLayout();
                }
            }
        });
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean d(String str) {
        VelocityTracker velocityTracker = this.a;
        if (velocityTracker == null) {
            return false;
        }
        velocityTracker.computeCurrentVelocity(1000, 1000.0f);
        float xVelocity = velocityTracker.getXVelocity();
        this.a.clear();
        this.a.recycle();
        this.a = null;
        return Math.abs(xVelocity) >= 200.0f;
    }

    private void a(String str, boolean z, String str2) {
        a(str, z, false, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(java.lang.String r11, boolean r12, boolean r13, java.lang.String r14) {
        /*
            Method dump skipped, instructions count: 270
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.h.a(java.lang.String, boolean, boolean, java.lang.String):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x008d, code lost:
    
        if (r11 >= r12) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00b1, code lost:
    
        r15 = r12 - r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00af, code lost:
    
        if (r11 <= r12) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0015, code lost:
    
        if (r0 != 3) goto L83;
     */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00dc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean g(android.view.MotionEvent r15) {
        /*
            Method dump skipped, instructions count: 354
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.h.g(android.view.MotionEvent):boolean");
    }

    private void f() {
        this.L = true;
        O = true;
        if (this.o != null) {
            this.n.requestLayout();
            this.n.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: io.dcloud.common.b.b.h.6
                /* JADX WARN: Removed duplicated region for block: B:12:0x0190  */
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void onGlobalLayout() {
                    /*
                        Method dump skipped, instructions count: 575
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.h.AnonymousClass6.onGlobalLayout():void");
                }
            });
        }
    }

    public View a(String str) {
        DragBean dragBean;
        IFrameView iFrameView;
        View obtainMainView;
        String e = e(str);
        HashMap<String, DragBean> hashMap = this.l.obtainFrameOptions().dragData;
        if (hashMap == null || !hashMap.containsKey(e) || (dragBean = hashMap.get(e)) == null || (iFrameView = dragBean.dragBindWebView) == null || !"follow".equalsIgnoreCase(JSONUtil.getString(dragBean.dragBindViewOp, "moveMode")) || (obtainMainView = iFrameView.obtainMainView()) == null || obtainMainView.getVisibility() != View.VISIBLE || obtainMainView.getWidth() >= this.J) {
            return null;
        }
        int b = b(obtainMainView);
        int width = obtainMainView.getWidth() + b;
        if ((b < 0 || b >= this.J) && (width <= 0 || width > this.J)) {
            return null;
        }
        return obtainMainView;
    }

    private float a(float f) {
        if (!AbsoluteConst.JSON_KEY_BOUNCE.equalsIgnoreCase(this.y) || 0.0f == f) {
            return f;
        }
        boolean z = f < 0.0f;
        float intValue = (int) (new BigDecimal(f).multiply(new BigDecimal(Math.abs(this.i - b(this.n))).divide(new BigDecimal(this.i - this.h), 4, 4)).intValue() * 1.5f);
        return z ? Math.min(intValue, -2.0f) : Math.max(intValue, 2.0f);
    }

    private String e(String str) {
        if ("left".equals(str)) {
            return "right";
        }
        if ("right".equals(str)) {
            return "left";
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public int a(View view) {
        if (view == null) {
            return 0;
        }
        if (view instanceof INativeView) {
            return ((INativeView) view).getStyleWidth();
        }
        return view.getWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public int b(View view) {
        if (view == null) {
            return 0;
        }
        if (view instanceof INativeView) {
            return ((INativeView) view).getStyleLeft();
        }
        if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
            return ((AbsoluteLayout.LayoutParams) view.getLayoutParams()).x;
        }
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            return (int) view.getX();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void b(View view, int i) {
        if (view != null) {
            if (view instanceof INativeView) {
                ((INativeView) view).setStyleLeft(i);
                return;
            }
            if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
                AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = view.getHeight();
                layoutParams.width = view.getWidth();
                layoutParams.x = i;
                view.requestLayout();
                return;
            }
            if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                view.setX(i);
            }
        }
    }

    private boolean a(DHFrameView dVar) {
        if (dVar == null) {
            return true;
        }
        if (dVar != null && dVar.obtainWebView() == null) {
            return true;
        }
        if (dVar == null || dVar.obtainMainView() != null) {
            return dVar != null && dVar.obtainWebView() == null && dVar.obtainMainView() == null;
        }
        return true;
    }

    private boolean g() {
        return h() && i();
    }

    private boolean h() {
        this.h = b(this.n);
        if ("right".equals(this.G)) {
            int i = this.q;
            if (Integer.MAX_VALUE != i) {
                this.i = i;
                return this.h != i;
            }
            this.i = this.J;
            View view = this.o;
            if (view != null) {
                if (AbsoluteConst.JSON_KEY_BOUNCE.equalsIgnoreCase(this.y)) {
                    this.i = this.h + (a(this.n) / 2);
                    return true;
                }
                if (a(this.o) >= this.J) {
                    return true;
                }
                Rect rect = new Rect();
                this.o.getGlobalVisibleRect(rect);
                Rect rect2 = new Rect();
                this.n.getGlobalVisibleRect(rect2);
                if (rect.left == 0 && rect.right == rect2.left) {
                    return false;
                }
                if (!this.B && rect.left == 0) {
                    this.i = a(this.o);
                    return true;
                }
                if (this.B && rect.right == 0) {
                    this.i = a(this.o);
                    return true;
                }
                if (this.J != rect.right) {
                    return true;
                }
                this.i = 0;
                return true;
            }
            if (view != null || this.h >= 0) {
                return true;
            }
            this.i = 0;
            return true;
        }
        if (!"left".equals(this.G)) {
            return true;
        }
        int i2 = this.q;
        if (Integer.MAX_VALUE != i2) {
            this.i = i2;
            return this.h != i2;
        }
        this.i = -a(this.n);
        View view2 = this.o;
        if (view2 != null) {
            if (AbsoluteConst.JSON_KEY_BOUNCE.equalsIgnoreCase(this.y)) {
                this.i = this.h - (a(this.n) / 2);
                return true;
            }
            if (a(this.o) >= this.J) {
                return true;
            }
            Rect rect3 = new Rect();
            this.o.getGlobalVisibleRect(rect3);
            Rect rect4 = new Rect();
            this.n.getGlobalVisibleRect(rect4);
            if (this.J == rect3.right && rect3.left == rect4.right) {
                return false;
            }
            if (!this.B && this.J == rect3.right) {
                this.i = -a(this.o);
                return true;
            }
            if (this.B && this.J == rect3.left) {
                this.i = -a(this.o);
                return true;
            }
            if (rect3.left != 0) {
                return true;
            }
            this.i = 0;
            return true;
        }
        if (view2 != null || this.h <= 0) {
            return true;
        }
        this.i = 0;
        return true;
    }

    private boolean i() {
        this.j = b(this.o);
        if ("right".equals(this.G)) {
            int i = this.u;
            if (Integer.MAX_VALUE != i) {
                this.k = i;
                return this.j != i;
            }
            this.k = a(this.o);
            int i2 = this.j;
            if (i2 == 0 || i2 == this.J) {
                return false;
            }
            if (AbsoluteConst.JSON_KEY_BOUNCE.equalsIgnoreCase(this.y)) {
                this.k = this.j + (a(this.n) / 2);
                return true;
            }
            if (this.j >= 0) {
                return true;
            }
            this.k = 0;
            return true;
        }
        if (!"left".equals(this.G)) {
            return true;
        }
        int i3 = this.u;
        if (Integer.MAX_VALUE != i3) {
            this.k = i3;
            return this.j != i3;
        }
        int a = a(this.o);
        this.k = -a;
        if (AbsoluteConst.JSON_KEY_BOUNCE.equalsIgnoreCase(this.y)) {
            this.k = this.j - (a(this.n) / 2);
            return true;
        }
        int i4 = this.J;
        if (a < i4) {
            int b = b(this.o) + a(this.o);
            if (b == this.J || b == 0) {
                return false;
            }
            Rect rect = new Rect();
            this.o.getGlobalVisibleRect(rect);
            if (this.J == rect.left) {
                this.k = this.J - a;
                return true;
            }
            if (rect.right != 0) {
                return true;
            }
            this.k = 0;
            return true;
        }
        if (a != i4 || this.j <= 0) {
            return true;
        }
        this.k = 0;
        return true;
    }

    private boolean j() {
        return "right".equals(this.G) ? b(this.o) != 0 : ("left".equals(this.G) && b(this.o) + a(this.o) == this.J) ? false : true;
    }
}
