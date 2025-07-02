package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;

import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.animation.AnimatorSet;
import io.dcloud.nineoldandroids.animation.ObjectAnimator;
import io.dcloud.nineoldandroids.view.ViewHelper;

/* loaded from: classes.dex */
public class AdaFrameItem  {
    public static int GONE = 8;
    public static int INVISIBLE = 4;
    static final String TAG = "AdaFrameItem";
    public static int VISIBLE;
    private Context mContextWrapper;
    public int mNavigationBarHeight = 0;
    private boolean mAutoPop = false;
    private boolean mAutoPush = false;
    public boolean mNeedOrientationUpdate = false;
    protected boolean mLongPressed = false;
    protected boolean mPressed = false;
    protected static ViewOptions mViewOptions = null;
    protected ViewOptions mViewOptions_animate = null;
    protected ViewOptions mViewOptions_birth = null;
    protected View mViewImpl = null;
    Animator.AnimatorListener mAnimatorListener = null;
    protected AnimOptions mAnimOptions = null;
    private Animation mAnimation = null;
    public boolean mStranslate = false;
    public int mZIndex = 0;
    public AdaContainerFrameItem mParentFrameItem = null;
    public long lastShowTime = 0;

    public AdaFrameItem findLastFrameItem(AdaFrameItem adaFrameItem) {
        return null;
    }

    public AdaFrameItem getParent() {
        return null;
    }

//    public ViewGroup getRootView() {
//        return (ViewGroup) this.mViewImpl.getParent();
//    }
    public boolean onDispose() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paint(Canvas canvas) {
    }

    public void setParentFrameItem(AdaContainerFrameItem adaContainerFrameItem) {
        this.mParentFrameItem = adaContainerFrameItem;
    }

    public AdaContainerFrameItem getParentFrameItem() {
        return this.mParentFrameItem;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AdaFrameItem(Context context) {
        this.mContextWrapper = null;
        this.mContextWrapper = context;
        setFrameOptions(new ViewOptions());
        this.mViewOptions.mTag = this;
    }

    public AnimOptions getAnimOptions() {
        if (this.mAnimOptions == null) {
            this.mAnimOptions = new AnimOptions();
        }
        return this.mAnimOptions;
    }

    public void setAnimOptions(AnimOptions animOptions) {
        this.mAnimOptions = animOptions;
    }

    public void setVisibility(int i) {
        View view = this.mViewImpl;
        if (view == null || view.getVisibility() == i) {
            return;
        }
        this.mViewImpl.setVisibility(i);
    }

    public final void clearAnimInfo() {
        this.mAnimation = null;
        this.mAnimatorListener = null;
        View view = this.mViewImpl;
        if (view != null) {
            view.clearAnimation();
        }
    }

    public void dispose() {
        clearAnimInfo();
        View view = this.mViewImpl;
        if (view != null) {
            view.setVisibility(View.GONE);
            ViewGroup viewGroup = (ViewGroup) this.mViewImpl.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.mViewImpl);
            }
            this.mViewImpl = null;
        }
    }

    public boolean isDisposed() {
        return this.mViewImpl == null;
    }

    public void makeViewOptions_animate() {
        obtainFrameOptions_Birth();
        ViewOptions obtainFrameOptions = obtainFrameOptions();
        ViewOptions viewOptions = this.mViewOptions_animate;
        if (viewOptions == null) {
            ViewOptions viewOptions2 = this.mViewOptions;
            viewOptions = ViewOptions.createViewOptionsData(viewOptions2, viewOptions2.getParentViewRect());
            this.mViewOptions_animate = viewOptions;
        }
        if (this.mAnimOptions.mOption == 0 || this.mAnimOptions.mOption == 4) {
            String str = this.mAnimOptions.mAnimType;
            if (PdrUtil.isEmpty(str)) {
                str = "none";
            }
            if (PdrUtil.isEquals(str, AnimOptions.ANIM_SLIDE_IN_RIGHT) || PdrUtil.isEquals(str, AnimOptions.ANIM_POP_IN)) {
                obtainFrameOptions.left = this.mAnimOptions.sScreenWidth;
                return;
            }
            if (PdrUtil.isEquals(str, AnimOptions.ANIM_SLIDE_IN_LEFT)) {
                obtainFrameOptions.left = -obtainFrameOptions.width;
                return;
            } else if (PdrUtil.isEquals(str, AnimOptions.ANIM_SLIDE_IN_TOP)) {
                obtainFrameOptions.top = -obtainFrameOptions.height;
                return;
            } else {
                if (PdrUtil.isEquals(str, AnimOptions.ANIM_SLIDE_IN_BOTTOM)) {
                    obtainFrameOptions.top = this.mAnimOptions.sScreenHeight;
                    return;
                }
                return;
            }
        }
        if (this.mAnimOptions.mOption == 1 || 3 == this.mAnimOptions.mOption) {
            String str2 = this.mAnimOptions.mAnimType_close;
            if (!AnimOptions.mAnimTypes.containsValue(str2)) {
                str2 = AnimOptions.mAnimTypes.get(this.mAnimOptions.mAnimType);
            }
            if (PdrUtil.isEquals(str2, AnimOptions.ANIM_SLIDE_OUT_RIGHT) || PdrUtil.isEquals(str2, AnimOptions.ANIM_POP_OUT)) {
                viewOptions.left = this.mAnimOptions.sScreenWidth;
                return;
            }
            if (PdrUtil.isEquals(str2, AnimOptions.ANIM_SLIDE_OUT_LEFT)) {
                viewOptions.left = -viewOptions.width;
            } else if (PdrUtil.isEquals(str2, AnimOptions.ANIM_SLIDE_OUT_TOP)) {
                viewOptions.top = -viewOptions.height;
            } else if (PdrUtil.isEquals(str2, AnimOptions.ANIM_SLIDE_OUT_BOTTOM)) {
                viewOptions.top = this.mAnimOptions.sScreenHeight;
            }
        }
    }

    public boolean isAutoPop() {
        return this.mAutoPop;
    }

    public boolean isAutoPush() {
        return this.mAutoPush;
    }

    public void onPopFromStack(boolean z) {
        this.mAutoPop = z;
    }

    public void onPushToStack(boolean z) {
        this.mAutoPush = z;
    }

    public void scrollTo(int i, int i2) {
        this.mViewImpl.scrollTo(i, i2);
    }

    public void scrollBy(int i, int i2) {
        this.mViewImpl.scrollBy(i, i2);
    }

    public void setFrameOptions_Animate(ViewOptions viewOptions) {
        this.mViewOptions_animate = viewOptions;
    }

    public void setFrameOptions_Birth(ViewOptions viewOptions) {
        this.mViewOptions_birth = viewOptions;
    }

    public void setFrameOptions(ViewOptions viewOptions) {
        this.mViewOptions = viewOptions;
    }

    public static ViewOptions obtainFrameOptions() {
        return mViewOptions;
    }

    public ViewOptions obtainFrameOptions_Animate() {
        return this.mViewOptions_animate;
    }

    public ViewOptions obtainFrameOptions_Birth() {
        return this.mViewOptions_birth;
    }

    public void updateViewRect(AdaFrameItem adaFrameItem, int[] iArr, int[] iArr2) {
        updateViewRect(adaFrameItem, iArr, iArr2, new boolean[]{true, true, true, true}, new boolean[]{true, true, true, false});
    }

    public void updateViewRect(AdaFrameItem adaFrameItem, int[] iArr, int[] iArr2, boolean[] zArr, boolean[] zArr2) {
        this.mViewOptions.left = iArr[0];
        ViewOptions viewOptions = this.mViewOptions;
        viewOptions.checkValueIsPercentage("left", viewOptions.left, iArr2[0], zArr[0], zArr2[0]);
        this.mViewOptions.top = iArr[1];
        ViewOptions viewOptions2 = this.mViewOptions;
        viewOptions2.checkValueIsPercentage("top", viewOptions2.top, iArr2[1], zArr[1], zArr2[1]);
        this.mViewOptions.width = iArr[2];
        ViewOptions viewOptions3 = this.mViewOptions;
        viewOptions3.checkValueIsPercentage(AbsoluteConst.JSON_KEY_WIDTH, viewOptions3.width, iArr2[0], zArr[2], zArr2[2]);
        this.mViewOptions.height = iArr[3];
        ViewOptions viewOptions4 = this.mViewOptions;
        viewOptions4.checkValueIsPercentage("height", viewOptions4.height, iArr2[1], zArr[3], zArr2[3]);
        this.mViewOptions.setParentViewRect(adaFrameItem.mViewOptions);
    }

    public void setAnimatorLinstener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }

    public void startAnimator(int i) {
        AnimOptions animOptions = this.mAnimOptions;
        if (animOptions != null) {
            animOptions.mUserFrameItem = this;
            Animator createAnimation = this.mAnimOptions.createAnimation();
            createAnimation.addListener(this.mAnimatorListener);
            createAnimation.setTarget(obtainMainView());
            createAnimation.start();
        }
    }

    public Context getContext() {
        return this.mContextWrapper;
    }

    public Activity getActivity() {
        return (Activity) this.mContextWrapper;
    }

    public void setBgcolor(int i) {
        this.mViewImpl.setBackgroundColor(i);
    }

    public void setMainView(View view) {
        this.mViewImpl = view;
    }

    public void setWebView(WebViewImpl webViewImpl) {
        this.mViewImpl = webViewImpl;
    }

    public View getWebView() {
        return this.mViewImpl;
    }

    protected void useDefaultMainView() {
        setMainView(new DefaultView(this.mContextWrapper));
    }

    public View obtainMainView() {
        return this.mViewImpl;
    }

    public void resize() {
        onResize();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onResize() {
        if (!this.mNeedOrientationUpdate || isDisposed()) {
            return;
        }
        ViewOptions viewOptions = this.mViewOptions;
        boolean z = this instanceof AdaFrameView;
        boolean z2 = z && ((AdaFrameView) this).isChildOfFrameView;
        viewOptions.onScreenChanged();
        View obtainMainView = obtainMainView();
        ViewGroup.LayoutParams layoutParams = obtainMainView.getLayoutParams();
        if (z && !z2) {
            if (layoutParams == null || !z) {
                return;
            }
            layoutParams.height = layoutParams.height == -1 ? -1 : viewOptions.height;
            layoutParams.width = layoutParams.width != -1 ? viewOptions.width : -1;
            ViewHelper.setX(obtainMainView, viewOptions.left);
            ViewHelper.setY(obtainMainView, viewOptions.top);
            obtainMainView.requestLayout();
            obtainMainView.postInvalidate();
            return;
        }
        if (layoutParams instanceof AbsoluteLayout.LayoutParams) {
            if (z2) {
                AbsoluteLayout.LayoutParams layoutParams2 = (AbsoluteLayout.LayoutParams) layoutParams;
                layoutParams2.x = viewOptions.left;
                layoutParams2.y = viewOptions.top;
            } else {
                AbsoluteLayout.LayoutParams layoutParams3 = (AbsoluteLayout.LayoutParams) layoutParams;
                layoutParams3.x = viewOptions.left;
                layoutParams3.y = viewOptions.top;
            }
        }
        layoutParams.width = viewOptions.width;
        layoutParams.height = viewOptions.height;
    }

    /* loaded from: classes.dex */
    private class DefaultView extends View {
        public DefaultView(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            AdaFrameItem.this.paint(canvas);
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            AdaFrameItem.this.onResize();
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParamsUtil {
        public static ViewGroup.LayoutParams createLayoutParams(int i, int i2, int i3, int i4) {
            return new AbsoluteLayout.LayoutParams(i3, i4, i, i2);
        }

        public static void setViewLayoutParams(View view, int i, int i2, int i3, int i4) {
            ViewHelper.setX(view, i);
            ViewHelper.setY(view, i2);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(i3, i4));
            } else {
                layoutParams.width = i3;
                layoutParams.height = i4;
            }
        }

        private static void preAndroid30SetViewLayoutParams(final View view, final int i, final int i2) {
            if (!(i == 0 && i2 == 0) && DeviceInfo.sDeviceSdkVer <= 10) {
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator objectAnimator = new ObjectAnimator();
                objectAnimator.setPropertyName("x");
                objectAnimator.setFloatValues(i - 1, i);
                animatorSet.playTogether(objectAnimator);
                ObjectAnimator objectAnimator2 = new ObjectAnimator();
                objectAnimator2.setPropertyName("y");
                objectAnimator2.setFloatValues(i2 - 1, i2);
                animatorSet.playTogether(objectAnimator2);
                animatorSet.setDuration(5L);
                animatorSet.setTarget(view);
                animatorSet.addListener(new Animator.AnimatorListener() { // from class: io.dcloud.common.adapter.ui.AdaFrameItem.LayoutParamsUtil.1
                    @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                    }

                    @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
                    public void onAnimationRepeat(Animator animator) {
                    }

                    @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override // io.dcloud.nineoldandroids.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        view.postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.AdaFrameItem.LayoutParamsUtil.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ViewHelper.setX(view, i);
                                ViewHelper.setY(view, i2);
                            }
                        }, 10L);
                    }
                });
                animatorSet.start();
            }
        }
    }
}
