package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSUtil;

/* loaded from: classes.dex */
public class FrameSwitchView implements IReflectAble {
    private static final String POP_IN = "pop-in";
    private static final String POP_OUT = "pop-out";
    private static final String SLIDE_IN_RIGHT = "slide-in-right";
    private static final String SLIDE_OUT_RIGHT = "slide-out-right";
    static FrameSwitchView mInstance;
    private Activity mActivity;
    private int mAppScreenHeight;
    private int mAppScreenWidth;
    private FrameBitmapView mLeftFrameBpView;
    private View mLeftView;
    private FrameBitmapView mRightFrameBpView;
    private View mRightView;
    private SwitchLayout mSwitchLayout;
    private boolean isInit = false;
    private int mDuration = 300;
    private boolean isVisibility = false;
    private String mAniType = "pop-in";
    private IWebview mWebViewImpl = null;
    private String mCallbackId = null;
    private boolean isRuning = false;
    FrameBitmapView.ClearAnimationListener clearAnimationListener = new FrameBitmapView.ClearAnimationListener() { // from class: io.dcloud.common.adapter.ui.FrameSwitchView.7
        @Override // io.dcloud.common.adapter.ui.FrameBitmapView.ClearAnimationListener
        public void onAnimationEnd() {
            FrameSwitchView.this.endRefreshView();
        }
    };

    public static FrameSwitchView getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new FrameSwitchView(activity);
        }
        return mInstance;
    }

    public static FrameSwitchView getInstance() {
        return mInstance;
    }

    private FrameSwitchView(Activity activity) {
        this.mActivity = activity;
    }

    public void initView() {
        if (this.isInit) {
            return;
        }
        this.isInit = true;
        ViewGroup viewGroup = (ViewGroup) this.mActivity.getWindow().getDecorView();
        this.mSwitchLayout = new SwitchLayout(this.mActivity);
        this.mLeftFrameBpView = new FrameBitmapView(this.mActivity);
        this.mRightFrameBpView = new FrameBitmapView(this.mActivity);
        this.mSwitchLayout.addView(this.mLeftFrameBpView);
        this.mSwitchLayout.addView(this.mRightFrameBpView);
        this.mSwitchLayout.setVisibility(View.GONE);
        viewGroup.addView(this.mSwitchLayout);
    }

    private void initScreenData() {
        WindowManager windowManager = this.mActivity.getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        int[] iArr = new int[2];
        this.mWebViewImpl.obtainFrameView().obtainWebAppRootView().obtainMainView().getLocationOnScreen(iArr);
        this.mAppScreenWidth = width - iArr[0];
        this.mAppScreenHeight = height - iArr[1];
        this.mSwitchLayout.setPadding(0, iArr[1], 0, 0);
    }

    public boolean isInit() {
        return this.isInit;
    }

    public void startAnimation(IWebview iWebview, String str, Object obj, String str2, Object obj2, String str3, String str4) {
        try {
            this.mWebViewImpl = iWebview;
            JSONObject jSONObject = new JSONObject(str);
            this.mAniType = jSONObject.optString("type", "pop-in");
            this.mDuration = jSONObject.optInt(AbsoluteConst.TRANS_DURATION, this.mDuration);
            initScreenData();
            if (obj != null) {
                if (obj instanceof View) {
                    View view = (View) obj;
                    this.mLeftView = view;
                    addView(view);
                } else {
                    this.mLeftFrameBpView.injectionData(obj, str2, this.mAppScreenWidth, this.mAppScreenHeight, iWebview.getScale());
                }
                if (obj2 != null) {
                    if (obj2 instanceof View) {
                        this.mRightView = (View) obj2;
                        addView((View) obj2);
                    } else {
                        this.mRightFrameBpView.injectionData(obj2, str3, this.mAppScreenWidth, this.mAppScreenHeight, iWebview.getScale());
                    }
                } else if (this.mAniType.equals("pop-in")) {
                    this.mAniType = "slide-in-right";
                } else if (this.mAniType.equals("pop-out")) {
                    this.mAniType = "slide-out-right";
                }
                runingAnimation(iWebview, this.mAppScreenWidth, this.mAppScreenHeight, this.mAniType, str4);
            }
        } catch (Exception unused) {
        }
    }

    private void addView(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup instanceof SwitchLayout) {
            return;
        }
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        this.mSwitchLayout.addView(view);
    }

    private void runingAnimation(final IWebview iWebview, final int i, int i2, String str, final String str2) {
        initScreenData();
        this.isRuning = true;
        if (this.mSwitchLayout.getVisibility() != View.VISIBLE) {
            this.mSwitchLayout.setVisibility(View.VISIBLE);
        }
        View view = this.mLeftView;
        if (view == null) {
            view = this.mLeftFrameBpView;
        }
        View view2 = this.mRightView;
        if (view2 == null) {
            view2 = this.mRightFrameBpView;
        }
        view.setVisibility(View.VISIBLE);
        if (str.equals("pop-in")) {
            view2.setVisibility(View.VISIBLE);
            this.isVisibility = true;
            View finalView = view;
            view.startAnimation(getAnimation(0, -(i / 6), this.mDuration, new Animation.AnimationListener() { // from class: io.dcloud.common.adapter.ui.FrameSwitchView.1
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    finalView.setVisibility(View.GONE);
                    FrameSwitchView.this.isRuning = false;
                    JSUtil.execCallback(iWebview, str2, null, JSUtil.OK, false, false);
                }
            }));
            View finalView1 = view2;
            view2.startAnimation(getAnimation(i, 0, this.mDuration, new Animation.AnimationListener() { // from class: io.dcloud.common.adapter.ui.FrameSwitchView.2
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    FrameSwitchView.this.endAnimationLayout(finalView1, 0);
                }
            }));
            return;
        }
        if (str.equals("pop-out")) {
            view2.setVisibility(View.VISIBLE);
            this.isVisibility = true;
            View finalView2 = view;
            view.startAnimation(getAnimation(-(i / 2), 0, this.mDuration, new Animation.AnimationListener() { // from class: io.dcloud.common.adapter.ui.FrameSwitchView.3
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    FrameSwitchView.this.endAnimationLayout(finalView2, 0);
                    FrameSwitchView.this.isRuning = false;
                    JSUtil.execCallback(iWebview, str2, null, JSUtil.OK, false, false);
                }
            }));
            View finalView3 = view2;
            view2.startAnimation(getAnimation(0, i, this.mDuration, new Animation.AnimationListener() { // from class: io.dcloud.common.adapter.ui.FrameSwitchView.4
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    finalView3.setVisibility(View.GONE);
                }
            }));
            return;
        }
        if (str.equals("slide-in-right")) {
            if (view2 != null) {
                view2.setVisibility(View.GONE);
            }
            this.isVisibility = true;
            View finalView4 = view;
            view.startAnimation(getAnimation(i, 0, this.mDuration, new Animation.AnimationListener() { // from class: io.dcloud.common.adapter.ui.FrameSwitchView.5
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    FrameSwitchView.this.endAnimationLayout(finalView4, 0);
                    FrameSwitchView.this.isRuning = false;
                    JSUtil.execCallback(iWebview, str2, null, JSUtil.OK, false, false);
                }
            }));
            return;
        }
        if (str.equals("slide-out-right")) {
            if (view2 != null) {
                view2.setVisibility(View.GONE);
            }
            this.isVisibility = true;
            final View view3 = view;
            view.startAnimation(getAnimation(0, i, this.mDuration, new Animation.AnimationListener() { // from class: io.dcloud.common.adapter.ui.FrameSwitchView.6
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    FrameSwitchView.this.endAnimationLayout(view3, i);
                    FrameSwitchView.this.isRuning = false;
                    JSUtil.execCallback(iWebview, str2, null, JSUtil.OK, false, false);
                }
            }));
            return;
        }
        this.isRuning = false;
        JSUtil.execCallback(iWebview, str2, null, JSUtil.OK, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endAnimationLayout(View view, int i) {
        int left = view.getLeft() + i;
        int top = view.getTop();
        int width = view.getWidth();
        int height = view.getHeight();
        view.clearAnimation();
        view.layout(left, top, width + left, height + top);
    }

    public void clearSwitchAnimation(String str) {
        endRefreshView();
    }

    public void endRefreshView() {
        if (this.isVisibility) {
            this.mLeftFrameBpView.clearAnimation();
            this.mRightFrameBpView.clearAnimation();
            this.isVisibility = false;
        }
        if (this.mSwitchLayout.getVisibility() == View.VISIBLE) {
            this.mSwitchLayout.setVisibility(View.GONE);
            KeyEvent.Callback callback = this.mLeftView;
            if (callback != null) {
                ((IWaiter) callback).doForFeature("clearAnimate", null);
                this.mSwitchLayout.removeView(this.mLeftView);
                this.mLeftView = null;
            }
            KeyEvent.Callback callback2 = this.mRightView;
            if (callback2 != null) {
                ((IWaiter) callback2).doForFeature("clearAnimate", null);
                this.mSwitchLayout.removeView(this.mRightView);
                this.mRightView = null;
            }
            this.mLeftFrameBpView.clearData();
            this.mRightFrameBpView.clearData();
            this.mLeftFrameBpView.requestLayout();
            this.mRightFrameBpView.requestLayout();
        }
    }

    public void stopAnimation() {
        initScreenData();
        FrameBitmapView frameBitmapView = this.mLeftFrameBpView;
        if (frameBitmapView != null && frameBitmapView.isInit()) {
            this.mLeftFrameBpView.setStopAnimation(true);
            this.mLeftFrameBpView.configurationChanged(this.mAppScreenWidth, this.mAppScreenHeight);
        }
        FrameBitmapView frameBitmapView2 = this.mRightFrameBpView;
        if (frameBitmapView2 != null && frameBitmapView2.isInit()) {
            this.mRightFrameBpView.setStopAnimation(true);
            this.mRightFrameBpView.configurationChanged(this.mAppScreenWidth, this.mAppScreenHeight);
        }
        if ("pop-in".equals(this.mAniType)) {
            View view = this.mRightView;
            if (view == null) {
                view = this.mRightFrameBpView;
            }
            endAnimationLayout(view, 0);
            View view2 = this.mLeftView;
            if (view2 == null) {
                view2 = this.mLeftFrameBpView;
            }
            view2.setVisibility(View.GONE);
            return;
        }
        if ("pop-out".equals(this.mAniType)) {
            View view3 = this.mRightView;
            if (view3 == null) {
                view3 = this.mRightFrameBpView;
            }
            view3.setVisibility(View.GONE);
            View view4 = this.mLeftView;
            if (view4 == null) {
                view4 = this.mLeftFrameBpView;
            }
            endAnimationLayout(view4, 0);
            return;
        }
        endRefreshView();
    }

    private TranslateAnimation getAnimation(int i, int i2, int i3, Animation.AnimationListener animationListener) {
        TranslateAnimation translateAnimation = new TranslateAnimation(i, i2, 0.0f, 0.0f);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setDuration(i3);
        translateAnimation.setAnimationListener(animationListener);
        return translateAnimation;
    }

    public void clearData() {
        if (this.isInit) {
            this.isInit = false;
            this.mActivity = null;
            this.mSwitchLayout = null;
            mInstance = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SwitchLayout extends RelativeLayout implements IWaiter {
        @Override // io.dcloud.common.DHInterface.IWaiter
        public Object doForFeature(String str, Object obj) {
            return null;
        }

        public SwitchLayout(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            if (FrameSwitchView.this.mWebViewImpl == null || !FrameSwitchView.this.isVisibility) {
                return;
            }
            FrameSwitchView.this.stopAnimation();
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (getVisibility() == View.VISIBLE) {
                if (FrameSwitchView.this.mLeftFrameBpView != null && FrameSwitchView.this.mLeftFrameBpView.isInit()) {
                    return true;
                }
                if (FrameSwitchView.this.mRightFrameBpView == null || !FrameSwitchView.this.mRightFrameBpView.isInit()) {
                    return super.onTouchEvent(motionEvent);
                }
                return true;
            }
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (!TextUtils.isEmpty(FrameSwitchView.this.mAniType)) {
                if (!FrameSwitchView.this.mAniType.equals("pop-in") || FrameSwitchView.this.mRightView == null || !(FrameSwitchView.this.mRightView instanceof IWaiter)) {
                    if ((FrameSwitchView.this.mAniType.equals("slide-in-right") || FrameSwitchView.this.mAniType.equals("slide-out-right") || FrameSwitchView.this.mAniType.equals("pop-out")) && FrameSwitchView.this.mLeftView != null && (FrameSwitchView.this.mLeftView instanceof IWaiter)) {
                        return ((Boolean) ((IWaiter) FrameSwitchView.this.mLeftView).doForFeature("checkTouch", motionEvent)).booleanValue();
                    }
                    if ((FrameSwitchView.this.mLeftFrameBpView != null && FrameSwitchView.this.mLeftFrameBpView.isInit()) || (FrameSwitchView.this.mRightFrameBpView != null && FrameSwitchView.this.mRightFrameBpView.isInit())) {
                        return super.dispatchTouchEvent(motionEvent);
                    }
                } else {
                    return ((Boolean) ((IWaiter) FrameSwitchView.this.mRightView).doForFeature("checkTouch", motionEvent)).booleanValue();
                }
            }
            return FrameSwitchView.this.isRuning;
        }
    }
}
