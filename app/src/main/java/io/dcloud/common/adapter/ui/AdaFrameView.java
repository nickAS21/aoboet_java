package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dcloud.android.v4.widget.IRefreshAble;
import com.dcloud.android.widget.AbsoluteLayout;

import java.util.ArrayList;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.common_b.common_b_b.DHImageView;
import io.dcloud.common.common_b.common_b_b.k;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import android.animation.Animator;

/* loaded from: classes.dex */
public abstract class AdaFrameView extends AdaContainerFrameItem implements IFrameView {
    private static final int ERROR = 0;
    private static final int SUCCESS = 1;
    static DHImageView mPageCImageView;
    public boolean interceptTouchEvent;
    public boolean isChildOfFrameView;
    public boolean isSlipping;
    public String mAccelerationType;
    public boolean mAnimationCapture;
    Animation.AnimationListener mAnimationListener;
    protected boolean mAnimationStarted;
    BounceView mBounceView;
    private Handler mCaptureHandler;
    IRefreshAble mCircleRefreshView;
    private Context mContext;
    private ICallBack mErrCallBack;
    protected byte mFrameStatus;
    private int mFrameType;
    private int mLastScreenHeight;
    private ArrayList<IEventCallback> mListeners;
    public Bitmap mLoadingSnapshot;
    RefreshView mRefreshView;
    public Bitmap mSnapshot;
    private ICallBack mSucCallBack;

    /* loaded from: classes.dex */
    public interface OnAnimationEnd {
        void onDone();
    }

    protected abstract void initMainView(Context context, int i, Object obj);

    public boolean isSupportLongTouch() {
        return false;
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public abstract IApp obtainApp();

    public abstract String obtainPrePlusreadyJs();

    @Override // io.dcloud.common.DHInterface.IFrameView
    public abstract IWebview obtainWebView();

    @Override // io.dcloud.common.DHInterface.IFrameView
    public abstract AbsMgr obtainWindowMgr();

    public void onConfigurationChanged() {
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void transition(byte b) {
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public int getFrameType() {
        return this.mFrameType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AdaFrameView(Context context, int i, Object obj) {
        super(context);
        this.isChildOfFrameView = false;
        this.mLoadingSnapshot = null;
        this.mSnapshot = null;
        this.mAnimationStarted = false;
        this.mRefreshView = null;
        this.mBounceView = null;
        this.mCircleRefreshView = null;
        this.mListeners = null;
        this.mFrameType = 0;
        this.mAccelerationType = "auto";
        this.interceptTouchEvent = true;
        this.mLastScreenHeight = 0;
        this.mAnimationCapture = false;
        this.isSlipping = false;
        this.mSucCallBack = null;
        this.mErrCallBack = null;
        this.mCaptureHandler = new Handler() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.4
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    if (AdaFrameView.this.mErrCallBack != null) {
                        AdaFrameView.this.mErrCallBack.onCallBack(message.arg1, message.obj);
                    }
                } else if (AdaFrameView.this.mSucCallBack != null) {
                    AdaFrameView.this.mSucCallBack.onCallBack(0, null);
                }
                super.handleMessage(message);
            }
        };
        this.mAnimationListener = new Animation.AnimationListener() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.7
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                if (AdaFrameView.this.mAnimatorListener != null) {
                    AdaFrameView.this.mAnimatorListener.onAnimationStart(null);
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
                if (AdaFrameView.this.mAnimatorListener != null) {
                    AdaFrameView.this.mAnimatorListener.onAnimationRepeat(null);
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (AdaFrameView.this.mAnimatorListener != null) {
                    AdaFrameView.this.mAnimatorListener.onAnimationEnd(null);
                }
            }
        };
        this.mFrameType = i;
        initMainView(context, i, obj);
        this.mContext = context;
        this.mNeedOrientationUpdate = true;
        this.mLastScreenHeight = PlatformUtil.SCREEN_HEIGHT(context);
    }

    @Override // io.dcloud.common.DHInterface.IFrameViewStatus
    public byte obtainStatus() {
        return this.mFrameStatus;
    }

    public void chkUseCaptureAnimation(boolean z, int i, boolean z2) {
        StringBuilder sb;
        String str;
        boolean z3;
        if (z) {
            sb = new StringBuilder();
            str = "B页面";
        } else {
            sb = new StringBuilder();
            str = "C页面";
        }
        String sb2 = sb.append(str).append(i).toString();
        if (this.mAccelerationType.equals("none") && !z2) {
            this.mAnimationCapture = false;
            return;
        }
        if (Build.VERSION.SDK_INT >= 23 && !PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_POP_IN) && !PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_POP_OUT)) {
            this.mAnimationCapture = false;
            return;
        }
        if (!obtainWebView().isLoaded() || DeviceInfo.sDeviceSdkVer < 11) {
            this.mAnimationCapture = false;
            Logger.e("mabo", sb2 + "1是否启用截图动画方案:" + this.mAnimationCapture);
            return;
        }
        if (this.isChildOfFrameView && !PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_FADE_IN)) {
            this.mAnimationCapture = false;
            Logger.e("mabo", sb2 + "2是否启用截图动画方案:" + this.mAnimationCapture);
            return;
        }
        PlatformUtil.MESURE_SCREEN_STATUSBAR_HEIGHT(obtainWebView().getActivity());
        if (this.mLastScreenHeight != PlatformUtil.SCREEN_HEIGHT(this.mContext)) {
            this.mLastScreenHeight = PlatformUtil.SCREEN_HEIGHT(this.mContext);
            this.mAnimationCapture = false;
            Logger.e("mabo", sb2 + "3是否启用截图动画方案:" + this.mAnimationCapture);
        } else {
            boolean z4 = this.mAnimOptions.mOption == 3 || this.mAnimOptions.mOption == 1;
            boolean z5 = (this.mAccelerationType.equals("auto") && !PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_FADE_IN) && z4) || this.mAccelerationType.equals(AbsoluteConst.CAPTURE);
            if (!z4) {
                z3 = (this.mAnimOptions.mOption == 4 || this.mAnimOptions.mOption == 0) & (z5 || PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_POP_IN) || PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_ZOOM_FADE_OUT));
            } else {
                z3 = true & (z5 || PdrUtil.isEquals(this.mAnimOptions.mAnimType_close, AnimOptions.ANIM_SLIDE_OUT_RIGHT) || PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_SLIDE_OUT_LEFT) || PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_SLIDE_OUT_TOP) || PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_SLIDE_OUT_BOTTOM) || PdrUtil.isEquals(this.mAnimOptions.mAnimType_close, AnimOptions.ANIM_POP_OUT) || PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_ZOOM_FADE_IN));
            }
            this.mAnimationCapture = (this.isChildOfFrameView && PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_FADE_IN)) ? true : z3;
        }
    }

    private void addCaptureImageView(ViewGroup viewGroup, ImageView imageView, Bitmap bitmap) {
        if (imageView.getParent() != viewGroup) {
            if (imageView.getParent() != null) {
                ((ViewGroup) imageView.getParent()).removeView(imageView);
            }
            viewGroup.addView(imageView);
        }
        imageView.bringToFront();
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(VISIBLE);
    }

    private boolean captureAnimation(final Animator animator, int i) {
        Bitmap bitmap;
        boolean z;
        if (this.mAnimationCapture && BaseInfo.sAnimationCaptureC) {
            final ViewGroup viewGroup = (ViewGroup) obtainMainView();
            if (checkITypeofAble()) {
                return false;
            }
            final k kVar = (k) obtainWebAppRootView().obtainMainView();
            if (mPageCImageView != null && kVar.getHeight() != mPageCImageView.getHeight()) {
                mPageCImageView.setImageBitmap(null);
                mPageCImageView = null;
                return false;
            }
            if (mPageCImageView == null) {
                DHImageView c = kVar.c();
                mPageCImageView = c;
                c.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            if (mPageCImageView.a()) {
                return false;
            }
            Logger.e("mabo", "C页面是否启用截图动画方案:" + (this.mAnimationCapture && BaseInfo.sAnimationCaptureC) + " | " + this.mAnimOptions.mAnimType);
            long currentTimeMillis = System.currentTimeMillis();
            if (i == 0) {
                bitmap = this.mLoadingSnapshot;
                if (bitmap == null && (bitmap = this.mSnapshot) == null) {
                    bitmap = PlatformUtil.captureView(viewGroup);
                    z = true;
                }
                z = false;
            } else {
                bitmap = this.mSnapshot;
                if (bitmap == null) {
                    bitmap = PlatformUtil.captureView(viewGroup);
                    z = true;
                }
                z = false;
            }
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            Logger.i("mabo", "==============C截图耗时=" + currentTimeMillis2);
            if (currentTimeMillis2 >= BaseInfo.sTimeoutCapture) {
                BaseInfo.sTimeOutCount++;
                if (BaseInfo.sTimeOutCount > BaseInfo.sTimeOutMax) {
                    BaseInfo.sAnimationCaptureC = false;
                }
            } else if (z) {
                BaseInfo.sTimeOutCount = 0;
            }
            if (bitmap != null && !PlatformUtil.isWhiteBitmap(bitmap)) {
                mPageCImageView.c();
                mPageCImageView.setLayoutParams(new FrameLayout.LayoutParams(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight()));
                addCaptureImageView(kVar, mPageCImageView, bitmap);
                viewGroup.setVisibility(View.INVISIBLE);
                mPageCImageView.setX(this.mViewOptions.left);
                mPageCImageView.setY(this.mViewOptions.top);
                if (this.mAnimOptions.mAnimator == null) {
                    animator.setTarget(mPageCImageView);
                    animator.addListener(new Animator.AnimatorListener() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.1
                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationStart(Animator animator2) {
                            if (AdaFrameView.mPageCImageView != null) {
                                AdaFrameView.mPageCImageView.b(true);
                            }
                            BaseInfo.sDoingAnimation = true;
                            if (AdaFrameView.this.mAnimatorListener != null) {
                                AdaFrameView.this.mAnimatorListener.onAnimationStart(animator2);
                            }
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationRepeat(Animator animator2) {
                            if (AdaFrameView.this.mAnimatorListener != null) {
                                AdaFrameView.this.mAnimatorListener.onAnimationRepeat(animator2);
                            }
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator2) {
                            if (AdaFrameView.mPageCImageView != null) {
                                AdaFrameView.mPageCImageView.b(false);
                            }
                            if (AdaFrameView.this.mAnimatorListener != null) {
                                AdaFrameView.this.mAnimatorListener.onAnimationEnd(animator2);
                            }
                            if (AdaFrameView.this.mAnimOptions.mOption == 3 || AdaFrameView.this.mAnimOptions.mOption == 1) {
                                viewGroup.setVisibility(View.INVISIBLE);
                            } else {
                                viewGroup.setVisibility(View.VISIBLE);
                            }
                            kVar.postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (AdaFrameView.mPageCImageView != null) {
                                        AdaFrameView.mPageCImageView.setVisibility(View.INVISIBLE);
                                        AdaFrameView.mPageCImageView.setImageBitmap(null);
                                    }
                                    BaseInfo.sDoingAnimation = false;
                                    if (BaseInfo.sOpenedCount == 0) {
                                        BaseInfo.sFullScreenChanged = false;
                                    }
                                }
                            }, 240L);
                            animator.removeListener(this);
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator2) {
                            BaseInfo.sDoingAnimation = false;
                            if (BaseInfo.sOpenedCount == 0) {
                                BaseInfo.sFullScreenChanged = false;
                            }
                            if (AdaFrameView.this.mAnimatorListener != null) {
                                AdaFrameView.this.mAnimatorListener.onAnimationCancel(animator2);
                            }
                        }
                    });
                } else {
                    this.mAnimOptions.mAnimator.setAnimationListener(new Animation.AnimationListener() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.2
                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationStart(Animation animation) {
                            if (AdaFrameView.mPageCImageView != null) {
                                AdaFrameView.mPageCImageView.b(true);
                            }
                            BaseInfo.sDoingAnimation = true;
                            if (AdaFrameView.this.mAnimatorListener != null) {
                                AdaFrameView.this.mAnimatorListener.onAnimationStart(null);
                            }
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationRepeat(Animation animation) {
                            if (AdaFrameView.this.mAnimatorListener != null) {
                                AdaFrameView.this.mAnimatorListener.onAnimationRepeat(null);
                            }
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation) {
                            if (AdaFrameView.mPageCImageView != null) {
                                AdaFrameView.mPageCImageView.b(false);
                            }
                            if (AdaFrameView.this.mAnimatorListener != null) {
                                AdaFrameView.this.mAnimatorListener.onAnimationEnd(null);
                            }
                            if (AdaFrameView.this.mAnimOptions.mOption == 3 || AdaFrameView.this.mAnimOptions.mOption == 1) {
                                viewGroup.setVisibility(View.INVISIBLE);
                            } else {
                                viewGroup.setVisibility(View.VISIBLE);
                            }
                            viewGroup.postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (AdaFrameView.mPageCImageView != null && !BaseInfo.sDoingAnimation) {
                                        AdaFrameView.mPageCImageView.clearAnimation();
                                        AdaFrameView.mPageCImageView.setVisibility(View.INVISIBLE);
                                        AdaFrameView.mPageCImageView.setImageBitmap(null);
                                    }
                                    if (BaseInfo.sOpenedCount == 0) {
                                        BaseInfo.sFullScreenChanged = false;
                                    }
                                }
                            }, 240L);
                        }
                    });
                }
                return true;
            }
        }
        return false;
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void startAnimator(int i) {
        startAnimator(null, i);
    }

    public void startAnimator(final OnAnimationEnd onAnimationEnd, int i) {
        if (this.mAnimOptions != null) {
            this.mAnimOptions.mUserFrameItem = this;
            this.mAnimOptions.sScreenWidth = obtainApp().getInt(0);
            this.mAnimOptions.sScreenHeight = obtainApp().getInt(1);
            Animator createAnimation = this.mAnimOptions.createAnimation();
            if (obtainFrameOptions().hasBackground() && this.mAnimOptions.mOption == 2) {
                AdaWebViewParent obtainWebviewParent = obtainWebviewParent();
                this.mAnimOptions.mUserFrameItem = obtainWebviewParent;
                createAnimation.setTarget(obtainWebviewParent.obtainMainView());
                createAnimation.addListener(this.mAnimatorListener);
                createAnimation.start();
            } else if (captureAnimation(createAnimation, i)) {
                if (this.mAnimOptions.mAnimator == null) {
                    createAnimation.start();
                } else {
                    DHImageView eVar = mPageCImageView;
                    if (eVar != null) {
                        eVar.startAnimation(this.mAnimOptions.mAnimator);
                    }
                }
            } else {
                DHImageView eVar2 = mPageCImageView;
                if (eVar2 != null && !eVar2.a()) {
                    mPageCImageView.clearAnimation();
                    mPageCImageView.setVisibility(View.INVISIBLE);
                    mPageCImageView.setImageBitmap(null);
                }
                this.mViewImpl.bringToFront();
                if (this.mAnimOptions.mAnimator == null) {
                    createAnimation.setTarget(this.mViewImpl);
                    createAnimation.addListener(this.mAnimatorListener);
                    createAnimation.start();
                } else {
                    this.mAnimOptions.mAnimator.setAnimationListener(this.mAnimationListener);
                    this.mViewImpl.startAnimation(this.mAnimOptions.mAnimator);
                }
            }
            createAnimation.setInterpolator(new DecelerateInterpolator());
            MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.3
                @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                public void execute(Object obj) {
                    if (!AdaFrameView.this.mAnimationStarted && AdaFrameView.this.mAnimatorListener != null) {
                        AdaFrameView.this.mAnimatorListener.onAnimationEnd(null);
                    }
                    OnAnimationEnd onAnimationEnd2 = onAnimationEnd;
                    if (onAnimationEnd2 != null) {
                        onAnimationEnd2.onDone();
                    }
                    if (BaseInfo.sOpenedCount == 0) {
                        BaseInfo.sFullScreenChanged = false;
                    }
                }
            }, Math.max(this.mAnimOptions.duration_show, Math.max(this.mAnimOptions.duration_close, this.mAnimOptions.duration)), null);
        }
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void setVisible(boolean z, boolean z2) {
        Logger.d(Logger.VIEW_VISIBLE_TAG, "AdaFrameView.setVisible pVisible" + z + "       " + this);
        setVisibility(z ? VISIBLE : GONE);
    }

    @Override // io.dcloud.common.DHInterface.IFrameViewStatus
    public final void addFrameViewListener(IEventCallback iEventCallback) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        if (iEventCallback == null || this.mListeners.contains(iEventCallback)) {
            return;
        }
        this.mListeners.add(iEventCallback);
    }

    @Override // io.dcloud.common.DHInterface.IFrameViewStatus
    public final void removeFrameViewListener(IEventCallback iEventCallback) {
        ArrayList<IEventCallback> arrayList = this.mListeners;
        if (arrayList != null) {
            arrayList.remove(iEventCallback);
        }
    }

    public final void dispatchFrameViewEvents(String str, Object obj) {
        if (this.mListeners != null) {
            Logger.d("AdaFrameView.dispatchFrameViewEvents type=" + str + ";args=" + obj);
            for (int size = this.mListeners.size() - 1; size >= 0; size--) {
                IEventCallback iEventCallback = this.mListeners.get(size);
                if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) {
                    this.mListeners.remove(size);
                }
                iEventCallback.onCallBack(str, obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public void onResize() {
        super.onResize();
        if (obtainApp() == null) {
            return;
        }
        RefreshView refreshView = this.mRefreshView;
        if (refreshView != null) {
            refreshView.onResize();
        }
        BounceView bounceView = this.mBounceView;
        if (bounceView != null) {
            bounceView.onResize();
        }
        dispatchFrameViewEvents(AbsoluteConst.EVENTS_FRAME_ONRESIZE, null);
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public void dispose() {
        super.dispose();
        if (this.mRefreshView != null) {
            this.mRefreshView = null;
        }
        if (this.mBounceView != null) {
            this.mBounceView = null;
        }
        if (this.mCircleRefreshView != null) {
            this.mCircleRefreshView = null;
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void paint(Canvas canvas) {
        super.paint(canvas);
        if (obtainMainView() == null || obtainMainView().getVisibility() != View.VISIBLE || this.mRefreshView == null || this.isSlipping) {
            return;
        }
        Logger.d(Logger.VIEW_VISIBLE_TAG, "AdaFrameView.paint mRefreshView paint" + this);
        ViewOptions viewOptions = obtainWebviewParent().mViewOptions_birth;
        ViewOptions viewOptions2 = obtainWebviewParent().mViewOptions;
        if (viewOptions == null) {
            viewOptions = this.mViewOptions_birth;
        }
        if (viewOptions2 == null) {
            viewOptions2 = this.mViewOptions;
        }
        this.mRefreshView.paint(canvas, (viewOptions.left != viewOptions2.left ? viewOptions2.left : 0) + obtainWebviewParent().obtainMainView().getLeft(), (viewOptions.top != viewOptions2.top ? viewOptions2.top : 0) + obtainWebviewParent().obtainMainView().getTop());
    }

    public void setSlipping(boolean z) {
        this.isSlipping = z;
    }

    @Override // io.dcloud.common.DHInterface.IFrameViewStatus
    public void onInit() {
        this.mFrameStatus = (byte) 0;
    }

    @Override // io.dcloud.common.DHInterface.IFrameViewStatus
    public void onPreLoading() {
        this.mFrameStatus = (byte) 1;
    }

    @Override // io.dcloud.common.DHInterface.IFrameViewStatus
    public void onLoading() {
        this.mFrameStatus = (byte) 2;
    }

    @Override // io.dcloud.common.DHInterface.IFrameViewStatus
    public void onPreShow(IFrameView iFrameView) {
        this.mFrameStatus = (byte) 3;
        transition((byte) 3);
    }

    @Override // io.dcloud.common.DHInterface.IFrameViewStatus
    public void onDestroy() {
        this.mFrameStatus = (byte) 4;
        transition((byte) 4);
        dispose();
    }

    public void updateFrameRelViewRect(ViewRect viewRect) {
        View obtainMainView = obtainMainView();
        if (this.mViewOptions.hasBackground()) {
            if (obtainWebviewParent().obtainFrameOptions().allowUpdate) {
                obtainWebviewParent().obtainFrameOptions().updateViewData(viewRect);
            }
        } else {
            this.mViewOptions.updateViewData(viewRect);
            if (obtainMainView != null && obtainMainView.getVisibility() == View.VISIBLE && this.mRefreshView != null) {
                obtainWebviewParent().reInit();
            }
        }
        if (this.mAnimationCapture) {
            this.mViewImpl.setX(this.mViewOptions.left);
            this.mViewImpl.setY(this.mViewOptions.top);
            Logger.d(Logger.LAYOUT_TAG, "mViewImpl onAnimationEnd X=" + this.mViewImpl.getX() + "Y=" + this.mViewImpl.getY(), this.mViewImpl);
        }
        if (obtainMainView != null) {
            obtainMainView().invalidate();
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public boolean onDispose() {
        boolean onDispose = super.onDispose();
        dispatchFrameViewEvents(AbsoluteConst.EVENTS_CLOSE, obtainWebView());
        DHImageView eVar = mPageCImageView;
        if (eVar != null) {
            eVar.setImageBitmap(null);
        }
        return onDispose;
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void captureSnapshot(final String str, final ICallBack iCallBack, final ICallBack iCallBack2) {
        new Thread(new Runnable() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.5
            /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
                jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:24:0x0088
                	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1166)
                	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1022)
                	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
                */
            @Override // java.lang.Runnable
            public void run() {
                /*
                    r5 = this;
                    java.lang.String r0 = r2
                    boolean r0 = android.text.TextUtils.isEmpty(r0)
                    r1 = 1
                    java.lang.String r2 = "截图失败"
                    r3 = -100
                    if (r0 != 0) goto L58
                    java.lang.String r0 = r2
                    java.lang.String r0 = r0.toLowerCase()
                    java.lang.String r4 = "loading"
                    boolean r0 = r4.equals(r0)
                    if (r0 == 0) goto L58
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L4b
                    android.view.View r4 = r0.obtainMainView()     // Catch: java.lang.Exception -> L4b
                    android.graphics.Bitmap r4 = io.dcloud.common.adapter.util.PlatformUtil.captureView(r4)     // Catch: java.lang.Exception -> L4b
                    r0.mLoadingSnapshot = r4     // Catch: java.lang.Exception -> L4b
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L4b
                    android.graphics.Bitmap r0 = r0.mLoadingSnapshot     // Catch: java.lang.Exception -> L4b
                    if (r0 == 0) goto L3e
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L4b
                    io.dcloud.common.DHInterface.ICallBack r4 = r3     // Catch: java.lang.Exception -> L4b
                    io.dcloud.common.adapter.ui.AdaFrameView.access$002(r0, r4)     // Catch: java.lang.Exception -> L4b
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L4b
                    android.os.Handler r0 = io.dcloud.common.adapter.ui.AdaFrameView.access$200(r0)     // Catch: java.lang.Exception -> L4b
                    r0.sendEmptyMessage(r1)     // Catch: java.lang.Exception -> L4b
                    goto L94
                L3e:
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L4b
                    io.dcloud.common.DHInterface.ICallBack r1 = r4     // Catch: java.lang.Exception -> L4b
                    io.dcloud.common.adapter.ui.AdaFrameView.access$102(r0, r1)     // Catch: java.lang.Exception -> L4b
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L4b
                    io.dcloud.common.adapter.ui.AdaFrameView.access$300(r0, r3, r2)     // Catch: java.lang.Exception -> L4b
                    goto L94
                L4b:
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this
                    io.dcloud.common.DHInterface.ICallBack r1 = r4
                    io.dcloud.common.adapter.ui.AdaFrameView.access$102(r0, r1)
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this
                    io.dcloud.common.adapter.ui.AdaFrameView.access$300(r0, r3, r2)
                    goto L94
                L58:
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L88
                    android.view.View r4 = r0.obtainMainView()     // Catch: java.lang.Exception -> L88
                    android.graphics.Bitmap r4 = io.dcloud.common.adapter.util.PlatformUtil.captureView(r4)     // Catch: java.lang.Exception -> L88
                    r0.mSnapshot = r4     // Catch: java.lang.Exception -> L88
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L88
                    android.graphics.Bitmap r0 = r0.mSnapshot     // Catch: java.lang.Exception -> L88
                    if (r0 == 0) goto L7b
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L88
                    io.dcloud.common.DHInterface.ICallBack r4 = r3     // Catch: java.lang.Exception -> L88
                    io.dcloud.common.adapter.ui.AdaFrameView.access$002(r0, r4)     // Catch: java.lang.Exception -> L88
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L88
                    android.os.Handler r0 = io.dcloud.common.adapter.ui.AdaFrameView.access$200(r0)     // Catch: java.lang.Exception -> L88
                    r0.sendEmptyMessage(r1)     // Catch: java.lang.Exception -> L88
                    goto L94
                L7b:
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L88
                    io.dcloud.common.DHInterface.ICallBack r1 = r4     // Catch: java.lang.Exception -> L88
                    io.dcloud.common.adapter.ui.AdaFrameView.access$102(r0, r1)     // Catch: java.lang.Exception -> L88
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this     // Catch: java.lang.Exception -> L88
                    io.dcloud.common.adapter.ui.AdaFrameView.access$300(r0, r3, r2)     // Catch: java.lang.Exception -> L88
                    goto L94
                L88:
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this
                    io.dcloud.common.DHInterface.ICallBack r1 = r4
                    io.dcloud.common.adapter.ui.AdaFrameView.access$102(r0, r1)
                    io.dcloud.common.adapter.ui.AdaFrameView r0 = io.dcloud.common.adapter.ui.AdaFrameView.this
                    io.dcloud.common.adapter.ui.AdaFrameView.access$300(r0, r3, r2)
                L94:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.AdaFrameView.AnonymousClass5.run():void");
            }
        }).start();
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void clearSnapshot(String str) {
        DHImageView eVar = mPageCImageView;
        if (eVar != null) {
            eVar.setImageBitmap(null);
        }
        if (!TextUtils.isEmpty(str) && "loading".equals(str.toLowerCase())) {
            Bitmap bitmap = this.mLoadingSnapshot;
            if (bitmap != null) {
                try {
                    if (!bitmap.isRecycled()) {
                        this.mLoadingSnapshot.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.mLoadingSnapshot = null;
            return;
        }
        Bitmap bitmap2 = this.mSnapshot;
        if (bitmap2 != null) {
            try {
                if (!bitmap2.isRecycled()) {
                    this.mSnapshot.recycle();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.mSnapshot = null;
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public synchronized void draw(final View view, final INativeBitmap iNativeBitmap, final boolean z, final Rect rect, final ICallBack iCallBack, final ICallBack iCallBack2) {
        this.mCaptureHandler.post(new Runnable() { // from class: io.dcloud.common.adapter.ui.AdaFrameView.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Bitmap captureView = PlatformUtil.captureView(view, z, rect);
                    if (captureView != null) {
                        iNativeBitmap.setBitmap(captureView);
                        AdaFrameView.this.mSucCallBack = iCallBack;
                        AdaFrameView.this.mCaptureHandler.sendEmptyMessage(1);
                    } else {
                        AdaFrameView.this.mErrCallBack = iCallBack2;
                        AdaFrameView.this.sendErrorMessage(-101, "截图为白屏");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AdaFrameView.this.mErrCallBack = iCallBack2;
                    AdaFrameView.this.sendErrorMessage(-100, "截图失败");
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendErrorMessage(int i, String str) {
        Message message = new Message();
        message.what = 0;
        message.arg1 = i;
        message.obj = str;
        this.mCaptureHandler.sendMessage(message);
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void setSnapshot(Bitmap bitmap) {
        Bitmap bitmap2 = this.mSnapshot;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.mSnapshot.recycle();
        }
        this.mSnapshot = bitmap;
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void setAccelerationType(String str) {
        this.mAccelerationType = str;
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public IFrameView findPageB() {
        return obtainWebAppRootView().findFrameViewB(this);
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void animate(IWebview iWebview, String str, String str2) {
        if (obtainMainView() instanceof AbsoluteLayout) {
            ((AbsoluteLayout) obtainMainView()).animate(iWebview, str, str2);
        }
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void restore() {
        if (obtainMainView() instanceof AbsoluteLayout) {
            ((AbsoluteLayout) obtainMainView()).restore();
        }
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public void interceptTouchEvent(boolean z) {
        this.interceptTouchEvent = z;
    }

    @Override // io.dcloud.common.DHInterface.IFrameView
    public boolean isWebviewCovered() {
        WebView obtainWebview;
        IWebview obtainWebView = obtainWebView();
        if (obtainWebView == null || (obtainWebview = obtainWebView.obtainWebview()) == null || obtainWebview.getVisibility() != View.VISIBLE || obtainWebview.getParent() == null) {
            return true;
        }
        Rect rect = new Rect(0, 0, PlatformUtil.SCREEN_WIDTH(obtainWebview.getContext()), PlatformUtil.SCREEN_HEIGHT(obtainWebview.getContext()));
        Rect rect2 = new Rect();
        obtainWebview.getGlobalVisibleRect(rect2);
        if (!rect.contains(rect2)) {
            return true;
        }
        while (obtainWebview.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) obtainWebview.getParent();
            if (viewGroup.getVisibility() != View.VISIBLE) {
                return true;
            }
            for (int indexOfViewInParent = indexOfViewInParent(obtainWebview, viewGroup) + 1; indexOfViewInParent < viewGroup.getChildCount(); indexOfViewInParent++) {
                View childAt = viewGroup.getChildAt(indexOfViewInParent);
                if (childAt.getVisibility() == View.VISIBLE && !(childAt instanceof IWaiter)) {
                    Rect rect3 = new Rect();
                    childAt.getGlobalVisibleRect(rect3);
                    if (rect3.contains(rect2)) {
                        return true;
                    }
                }
            }
            obtainWebview = (WebView) viewGroup;
        }
        return obtainWebview.getParent() == null;
    }

    private int indexOfViewInParent(View view, ViewGroup viewGroup) {
        int i = 0;
        while (i < viewGroup.getChildCount() && viewGroup.getChildAt(i) != view) {
            i++;
        }
        return i;
    }
}
