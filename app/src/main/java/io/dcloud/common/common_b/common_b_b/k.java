package io.dcloud.common.common_b.common_b_b;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import io.dcloud.RInformation;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.constant.AbsoluteConst;

/* compiled from: SwipeBackLayout.java */
/* loaded from: classes.dex */
public class k extends FrameLayout {
    private boolean a;
    private Context b;
    private VelocityTracker c;
    private int d;
    private float e;
    private float f;
    private int g;
    e h;
    e i;
    DHAppRootView j;
    private float k;
    private int l;
    private RelativeLayout m;
    private int n;
    private DHFrameView o;
    private final int p;
    private final int q;
    private int r;

    public k(Context context, DHAppRootView cVar) {
        super(context);
        this.a = true;
        this.j = null;
        this.g = 70;
        this.p = 1;
        this.q = 2;
        this.r = 1;
        this.j = cVar;
        a(context);
    }

    public void a(Context context) {
        this.b = context;
        this.d = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.h = new e(context);
        this.i = new e(context);
        addView(this.h, new FrameLayout.LayoutParams(-1, -1));
        addView(this.i, new FrameLayout.LayoutParams(-1, -1));
        RelativeLayout relativeLayout = new RelativeLayout(context);
        this.m = relativeLayout;
        relativeLayout.setTag("shade");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        this.m.setLayoutParams(layoutParams);
        this.m.setAlpha(0.5f);
        View view = new View(this.b);
        this.n = 33;
        view.setBackgroundResource(RInformation.DRAWEBL_SHADOW_LEFT);
        this.m.addView(view, new RelativeLayout.LayoutParams(this.n, -1));
        addView(this.m, layoutParams);
        this.m.setVisibility(8);
        this.i.setVisibility(8);
        this.h.setVisibility(8);
    }

    public void a() {
        RelativeLayout relativeLayout = this.m;
        if (relativeLayout != null && relativeLayout.getParent() == null) {
            addView(this.m, new RelativeLayout.LayoutParams(-1, -1));
        }
        e eVar = this.h;
        if (eVar != null && eVar.getParent() == null) {
            addView(this.h, new FrameLayout.LayoutParams(-1, -1));
        }
        e eVar2 = this.i;
        if (eVar2 == null || eVar2.getParent() != null) {
            return;
        }
        addView(this.i, new FrameLayout.LayoutParams(-1, -1));
    }

    public e b() {
        return this.h;
    }

    public e c() {
        return this.i;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (actionMasked == 0) {
            this.e = x;
            this.f = y;
            this.k = x;
            this.a = false;
            this.o = this.j.a();
        } else if (actionMasked == 2) {
            if (this.k > this.g || this.o == null) {
                return false;
            }
            float f = x - this.e;
            float abs = Math.abs(f);
            float abs2 = Math.abs(y - this.f);
            if (abs > this.d && abs > abs2) {
                if (this.o.obtainMainView().getScrollX() < 0.0f) {
                    if (a(this.o)) {
                        this.a = true;
                        this.e = x;
                    }
                } else if (f > 0.0f && a(this.o)) {
                    this.a = true;
                    this.e = x;
                }
            }
        }
        return this.a;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0099  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean a(DHFrameView r8) {
        /*
            Method dump skipped, instructions count: 274
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.k.a(io.dcloud.common.b.b.d):boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002a, code lost:
    
        if (r0 != 3) goto L81;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r10) {
        /*
            Method dump skipped, instructions count: 403
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.b.b.k.onTouchEvent(android.view.MotionEvent):boolean");
    }

    void a(int i, int i2, final boolean z, int i3) {
        DHFrameView dVar = this.o;
        if (dVar == null) {
            return;
        }
        a(dVar, "end", Boolean.valueOf(z));
        int scrollX = this.o.obtainMainView().getScrollX();
        int scrollX2 = this.h.getScrollX();
        int scrollX3 = this.m.getScrollX();
        this.o.setSlipping(false);
        this.o.obtainMainView().scrollTo(0, this.o.obtainMainView().getScrollY());
        this.o.obtainMainView().startAnimation(a(-scrollX, i, i3, new Animation.AnimationListener() { // from class: io.dcloud.common.b.b.k.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (z) {
                    k.this.d();
                }
            }
        }));
        RelativeLayout relativeLayout = this.m;
        relativeLayout.scrollTo(0, relativeLayout.getScrollY());
        this.m.startAnimation(a(-scrollX3, i - this.n, i3, new Animation.AnimationListener() { // from class: io.dcloud.common.b.b.k.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                k.this.m.setVisibility(8);
            }
        }));
        e eVar = this.h;
        eVar.scrollTo(0, eVar.getScrollY());
        this.h.startAnimation(a(-scrollX2, i2, i3, new Animation.AnimationListener() { // from class: io.dcloud.common.b.b.k.3
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                k.this.h.a(false);
                k.this.h.postDelayed(new Runnable() { // from class: io.dcloud.common.b.b.k.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        k.this.h.setVisibility(8);
                        k.this.h.setImageBitmap(null);
                    }
                }, 250L);
            }
        }));
    }

    private TranslateAnimation a(int i, int i2, int i3, Animation.AnimationListener animationListener) {
        TranslateAnimation translateAnimation = new TranslateAnimation(i, i2, 0.0f, 0.0f);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setDuration(i3);
        if (animationListener != null) {
            translateAnimation.setAnimationListener(animationListener);
        }
        return translateAnimation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        DHFrameView dVar = this.o;
        if (dVar != null) {
            String str = dVar.obtainFrameOptions().popGesture;
            if (str.equals(AbsoluteConst.EVENTS_WEBVIEW_HIDE)) {
                this.o.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBAPP_SLIDE_HIDE, null);
                this.o.j.c(this.o);
            } else if (str.equals(AbsoluteConst.EVENTS_CLOSE)) {
                this.o.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBAPP_SLIDE_CLOSE, null);
                this.o.j.d(this.o);
            }
            this.o = null;
        }
    }

    @Override // android.view.View
    public void scrollTo(int i, int i2) {
        super.scrollTo(i, i2);
        postInvalidate();
    }

    public void a(AdaFrameView adaFrameView, String str, Object obj) {
        adaFrameView.dispatchFrameViewEvents("popGesture", new Object[]{str, obj, this.o});
    }
}
