package io.dcloud.common.common_b.common_b_b;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.dcloud.android.widget.AbsoluteLayout;

import java.util.ArrayList;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebAppRootView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaContainerFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.AdaWebViewParent;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.view.ViewHelper;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DHFrameView.java */
/* loaded from: classes.dex  olo d*/
public class DHFrameView extends AdaFrameView {
    public static int i;
    boolean a;
    ArrayList<DHFrameView> b;
    ArrayList<DHFrameView> c;
    boolean d;
    boolean e;
    boolean f;
    boolean g;
    boolean h;
    l j;
    IApp k;
    DHAppRootView l;
    f m;
    AdaWebViewParent n;
    byte o;
    ViewOptions p;
    Animator.AnimatorListener q;
    boolean r;
    private boolean s;
    private boolean t;
    private boolean u;

    private void a(View view) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DHFrameView(Context context, l lVar, IApp iApp, DHAppRootView cVar, int i2, Object obj) {
        super(context, i2, obj);
        this.a = false;
        this.d = false;
        this.s = true;
        this.t = false;
        this.e = false;
        this.f = true;
        this.g = false;
        this.h = false;
        this.j = null;
        this.k = null;
        this.l = null;
        this.m = null;
        this.n = null;
        this.o = (byte) 2;
        this.u = true;
        this.p = null;
        this.q = new Animator.AnimatorListener() { // from class: io.dcloud.common.b.b.d.2
            @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Logger.e("DHFrameView", "---------------------onAnimationEnd");
                if (DHFrameView.this.getAnimOptions().mOption == 3) {
                    DHFrameView.this.k();
                }
                BaseInfo.sDoingAnimation = false;
                if (DHFrameView.this.obtainMainView() == null) {
                    return;
                }
                DHFrameView.this.obtainMainView().post(new Runnable() { // from class: io.dcloud.common.b.b.d.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        a();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void a() {
                byte b = DHFrameView.this.getAnimOptions().mOption;
                if (b == 0) {
                    if (j.a != null) {
//                        j.a.clearAnimation();
                    }
                    DHFrameView.this.h();
                } else {
                    if (b == 1) {
                        DHFrameView.this.g();
                        return;
                    }
                    if (b == 2) {
                        DHFrameView.this.i();
                        return;
                    }
                    if (b == 3) {
                        DHFrameView.this.l();
                    } else {
                        if (b != 4) {
                            return;
                        }
                        if (j.a != null) {
//                            j.a.clearAnimation();
                        }
                        DHFrameView.this.j();
                    }
                }
            }

            @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                BaseInfo.sDoingAnimation = false;
            }

            @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                Logger.e("DHFrameView", "---------------------onAnimationStart");
                BaseInfo.sDoingAnimation = true;
                DHFrameView.this.mAnimationStarted = true;
                if (DHFrameView.this.getAnimOptions().mOption == 2) {
                    DHFrameView.this.a(DHFrameView.this.obtainMainView(), DHFrameView.this.mViewOptions.left, DHFrameView.this.mViewOptions.top, "onAnimationStart");
                }
            }
        };
        this.r = false;
        this.lastShowTime = System.currentTimeMillis();
        i++;
        Logger.i("dhframeview", "construction Count=" + i);
        this.j = lVar;
        this.k = iApp;
        this.l = cVar;
        cVar.c().add(this);
        this.s = iApp.isVerticalScreen();
        this.t = iApp.isFullScreen();
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView
    protected void initMainView(Context context, int i2, Object obj) {
        if (i2 == 1) {
            return;
        }
        setMainView(new AbsoluteLayout(context, this, this.k));
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void setNeedRender(boolean z) {
        this.a = z;
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameView
    public AbsMgr obtainWindowMgr() {
        return this.j;
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.adapter.ui.AdaFrameItem
    public void startAnimator(int i2) {
        chkUseCaptureAnimation(false, hashCode(), this.mSnapshot != null);
        this.m.setScrollIndicator("none");
        final int size = this.m.mChildArrayList.size();
        if (size != 0) {
            for (int i3 = 0; i3 < size; i3++) {
                if (this.m.mChildArrayList.get(i3) instanceof DHFrameView) {
                    DHFrameView dVar = (DHFrameView) this.m.mChildArrayList.get(i3);
                    dVar.m.setScrollIndicator("none");
                    a(dVar.m.getWebView());
                }
            }
        }
        super.startAnimator(new AdaFrameView.OnAnimationEnd() { // from class: io.dcloud.common.b.b.d.1
            @Override // io.dcloud.common.adapter.ui.AdaFrameView.OnAnimationEnd
            public void onDone() {
                DHFrameView.this.obtainMainView().postDelayed(new Runnable() { // from class: io.dcloud.common.b.b.d.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (DHFrameView.this.p != null) {
                                DHFrameView.this.m.setScrollIndicator(DHFrameView.this.p.getScrollIndicator());
                            }
                            for (int i4 = 0; i4 < size; i4++) {
                                if (DHFrameView.this.m.mChildArrayList.get(i4) instanceof DHFrameView) {
                                    DHFrameView dVar2 = (DHFrameView) DHFrameView.this.m.mChildArrayList.get(i4);
                                    dVar2.m.setScrollIndicator(dVar2.p.getScrollIndicator());
                                }
                            }
                        } catch (Exception unused) {
                        }
                    }
                }, 128L);
            }
        }, i2);
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameViewStatus
    public void onInit() {
        super.onInit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a() {
        ArrayList<DHFrameView> arrayList = this.b;
        return arrayList != null && arrayList.size() > 0;
    }

    boolean b() {
        ArrayList<DHFrameView> arrayList = this.c;
        return arrayList != null && arrayList.size() > 0;
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameViewStatus
    public void onPreLoading() {
        super.onPreLoading();
        if (this.o == 0) {
            c();
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameView
    public void transition(byte b) {
        if (this.o == b && b == 2) {
            c();
        }
    }

    void c() {
        f();
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameViewStatus
    public void onLoading() {
        super.onLoading();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        AnimOptions animOptions = getAnimOptions();
        return (animOptions == null || animOptions.mOption == 1) ? false : true;
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public void onPopFromStack(boolean z) {
        super.onPopFromStack(z);
        IApp iApp = this.k;
        if (iApp == null) {
            Logger.d(Logger.ANIMATION_TAG, "已经提前出栈了 " + (this.s ? "竖屏出栈" : "横屏出栈") + this);
            return;
        }
        this.s = iApp.isVerticalScreen();
        this.t = this.k.isFullScreen();
        Logger.d(Logger.ANIMATION_TAG, "onPopFromStack " + (this.s ? "竖屏出栈" : "横屏出栈") + this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        if (this.s == this.k.isVerticalScreen() && this.t == this.k.isFullScreen()) {
            return;
        }
        Logger.d(Logger.ANIMATION_TAG, "onPushToStack frame " + (this.s ? "调整为横屏状态" : "调整为竖屏状态") + this);
        resize();
        this.s = this.k.isVerticalScreen();
        this.t = this.k.isFullScreen();
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameViewStatus
    public void onPreShow(IFrameView iFrameView) {
        super.onPreShow(iFrameView);
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameViewStatus
    public void onDestroy() {
        super.onDestroy();
        i--;
        Logger.i("dhframeview", "onDestroy Count=" + i);
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameView
    public IApp obtainApp() {
        return this.k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        setAnimatorLinstener(this.q);
    }

    void a(ViewOptions viewOptions, AdaFrameItem adaFrameItem, AdaFrameItem adaFrameItem2, AdaFrameItem adaFrameItem3) {
        if (DeviceInfo.sDeviceSdkVer >= 11 && viewOptions.opacity != -1.0f) {
            adaFrameItem.obtainMainView().setAlpha(viewOptions.opacity);
        }
        if (viewOptions.hasBackground()) {
            adaFrameItem.setBgcolor(viewOptions.background);
        } else if (viewOptions.isTransparent()) {
            adaFrameItem2.setBgcolor(0);
            adaFrameItem3.setBgcolor(0);
            adaFrameItem.setBgcolor(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public void b(ViewOptions viewOptions, AdaFrameItem adaFrameItem, AdaFrameItem adaFrameItem2, AdaFrameItem adaFrameItem3) {
        this.p = viewOptions;
        ((IWebview) adaFrameItem3).setScrollIndicator(viewOptions.getScrollIndicator());
        a(viewOptions, adaFrameItem, adaFrameItem2, adaFrameItem3);
    }

    static void a(View view, int i2, int i3, String str) {
        if (DeviceInfo.sDeviceSdkVer <= 10) {
            view.layout(i2, i3, view.getRight() + i2, view.getBottom() + i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        setVisibility(GONE);
        n();
        o();
        q();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onCloseAnimationEnd;" + this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        dispatchFrameViewEvents(AbsoluteConst.EVENTS_SHOW_ANIMATION_END, null);
        if (this.mViewOptions_animate != null) {
            updateFrameRelViewRect(this.mViewOptions_animate);
            this.mViewOptions_animate = null;
        }
        n();
        this.f = true;
        q();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onShowAnimationEnd;" + this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        AdaContainerFrameItem obtainWebviewParent = obtainFrameOptions().hasBackground() ? obtainWebviewParent() : this;
        ViewOptions obtainFrameOptions_Animate = obtainWebviewParent.obtainFrameOptions_Animate();
        if (obtainFrameOptions_Animate != null) {
            updateFrameRelViewRect(obtainFrameOptions_Animate);
            obtainWebviewParent.setFrameOptions_Animate(null);
        }
        a(obtainMainView(), this.mViewOptions.left, this.mViewOptions.top, "onStyleChangedAnimationEnd");
        n();
        q();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onStyleChangedAnimationEnd;" + obtainWebviewParent.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        dispatchFrameViewEvents(AbsoluteConst.EVENTS_SHOW_ANIMATION_END, null);
        if (this.mViewOptions_animate != null) {
            updateFrameRelViewRect(this.mViewOptions_animate);
            this.mViewOptions_animate = null;
        }
        a(obtainMainView(), this.mViewOptions.left, this.mViewOptions.top, "onHideShowAnimationEnd");
        n();
        this.f = true;
        q();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onHideShowAnimationEnd;" + toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        setVisible(false, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l() {
        dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBVIEW_HIDE, null);
        if (!this.mViewOptions.hasBackground() && !this.isChildOfFrameView) {
            ViewHelper.setX(obtainMainView(), this.mViewOptions.left);
            ViewHelper.setY(obtainMainView(), this.mViewOptions.top);
            ViewHelper.setScaleX(obtainMainView(), 1.0f);
            ViewHelper.setScaleY(obtainMainView(), 1.0f);
            if (!this.mViewOptions.hasTransparentValue()) {
                ViewHelper.setAlpha(obtainMainView(), 1.0f);
            }
        }
        if (this.mViewOptions_animate != null) {
            updateFrameRelViewRect(this.mViewOptions_animate);
            this.mViewOptions_animate = null;
        }
        n();
        this.g = false;
        q();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onHideAnimationEnd;" + toString());
    }

    public void m() {
        this.r = true;
        Logger.d(Logger.ANIMATION_TAG, "onWillDoAnimation " + this);
        DHAppRootView cVar = this.l;
        if (cVar != null) {
            cVar.g.a(this);
        }
        if (this.d) {
            ViewOptions obtainFrameOptions_Animate = obtainFrameOptions_Animate();
            this.mViewOptions.opacity = obtainFrameOptions_Animate.opacity;
            this.mViewOptions.background = obtainFrameOptions_Animate.background;
            this.mViewOptions.strBackground = obtainFrameOptions_Animate.strBackground;
            a(this.mViewOptions, this, obtainWebviewParent(), (AdaFrameItem) obtainWebView());
        }
    }

    public void n() {
        this.r = false;
        this.mAnimationStarted = true;
        Logger.d(Logger.ANIMATION_TAG, "onDoneAnimation " + this);
        DHAppRootView cVar = this.l;
        if (cVar != null) {
            this.b = null;
            if (cVar.g.a() == 1) {
                this.l.d(this);
                if (!this.isChildOfFrameView) {
                    if (a()) {
                        Logger.d(Logger.ANIMATION_TAG, "on_Done_Animation 动画完后存在窗口入栈；" + this);
                        this.j.processEvent(IMgr.MgrType.WindowMgr, 28, this.b);
                    }
                    if (b()) {
                        Logger.d(Logger.ANIMATION_TAG, "on_Done_Animation 动画完后存在窗口出栈；" + this);
                        a(this.c);
                    } else if (this.l.a != null) {
                        this.l.a.onCallBack(-1, null);
                    }
                }
            }
            this.l.g.b(this);
            this.c = null;
        }
    }

    private void q() {
        this.b = null;
        this.c = null;
        this.d = false;
    }

    private void a(final ArrayList<DHFrameView> arrayList) {
        Logger.d(Logger.ANIMATION_TAG, "removeFrameViewFromViewStack DoAnimation Frame=" + this + ";Will PopFrames=" + arrayList);
        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.b.b.d.3
            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
            public void execute(Object obj) {
                DHFrameView.this.j.processEvent(IMgr.MgrType.WindowMgr, 27, arrayList);
                if (DHFrameView.this.l.a != null) {
                    DHFrameView.this.l.a.onCallBack(-1, null);
                }
            }
        }, null);
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.DHInterface.IFrameView
    public IWebview obtainWebView() {
        return this.m;
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public AdaWebViewParent obtainWebviewParent() {
        return this.n;
    }

    void o() {
        DHAppRootView cVar = this.l;
        if (cVar != null) {
            cVar.b(this);
        }
    }

    public String toString() {
        f fVar = this.m;
        return fVar != null ? fVar.toString() : super.toString();
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView
    public String obtainPrePlusreadyJs() {
        l lVar = this.j;
        return lVar != null ? (String) lVar.processEvent(IMgr.MgrType.FeatureMgr, 2, new Object[]{this.k, this}) : "";
    }

    public void a(int i2, int i3) {
        ViewGroup.LayoutParams layoutParams = obtainMainView().getLayoutParams();
        if (layoutParams == null) {
            obtainMainView().setLayoutParams(new ViewGroup.LayoutParams(i2, i3));
        } else {
            layoutParams.width = i2;
            layoutParams.height = i3;
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public AdaFrameItem getParent() {
        return this.l;
    }

    public void p() {
        DHAppRootView cVar = this.l;
        if (cVar != null) {
            cVar.c().remove(this);
        }
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public IWebAppRootView obtainWebAppRootView() {
        return this.l;
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView, io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public void dispose() {
        super.dispose();
        DHAppRootView cVar = this.l;
        if (cVar != null) {
            cVar.b().remove(this);
            p();
        }
        this.j = null;
        this.k = null;
        this.mParentFrameItem = null;
        this.l = null;
        this.m = null;
        this.q = null;
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameView
    public void onConfigurationChanged() {
        super.onConfigurationChanged();
        resize();
        this.s = this.k.isVerticalScreen();
        this.t = this.k.isFullScreen();
        Logger.d(Logger.Android_System_TAG, "onConfigurationChanged", this);
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void pushToViewStack() {
        if (this.isChildOfFrameView || this.f) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this);
        this.j.processEvent(IMgr.MgrType.WindowMgr, 28, arrayList);
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void popFromViewStack() {
        if (this.isChildOfFrameView || !this.f) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this);
        this.j.processEvent(IMgr.MgrType.WindowMgr, 27, arrayList);
    }
}
