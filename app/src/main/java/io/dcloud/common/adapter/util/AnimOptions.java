package io.dcloud.common.adapter.util;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

/* loaded from: classes.dex */
public class AnimOptions {
    public static final String ANIM_FADE_IN = "fade-in";
    public static final String ANIM_FADE_OUT = "fade-out";
    public static final String ANIM_FLIP_RX = "flip-rx";
    public static final String ANIM_FLIP_RY = "flip-ry";
    public static final String ANIM_FLIP_X = "flip-x";
    public static final String ANIM_FLIP_Y = "flip-y";
    public static final String ANIM_NONE = "none";
    public static final String ANIM_PAGE_BACKWARD = "page-backward";
    public static final String ANIM_PAGE_FORWARD = "page-forward";
    public static final String ANIM_POP_IN = "pop-in";
    public static final String ANIM_POP_OUT = "pop-out";
    public static final String ANIM_SLIDE_IN_BOTTOM = "slide-in-bottom";
    public static final String ANIM_SLIDE_IN_LEFT = "slide-in-left";
    public static final String ANIM_SLIDE_IN_RIGHT = "slide-in-right";
    public static final String ANIM_SLIDE_IN_TOP = "slide-in-top";
    public static final String ANIM_SLIDE_OUT_BOTTOM = "slide-out-bottom";
    public static final String ANIM_SLIDE_OUT_LEFT = "slide-out-left";
    public static final String ANIM_SLIDE_OUT_RIGHT = "slide-out-right";
    public static final String ANIM_SLIDE_OUT_TOP = "slide-out-top";
    private static final int ANIM_TIME = 200;
    public static final String ANIM_ZOOM_FADE_IN = "zoom-fade-in";
    public static final String ANIM_ZOOM_FADE_OUT = "zoom-fade-out";
    public static final String ANIM_ZOOM_IN = "zoom-in";
    public static final String ANIM_ZOOM_OUT = "zoom-out";
    public static final byte OPTION_CLOSE = 1;
    public static final byte OPTION_HIDE = 3;
    public static final byte OPTION_HIDE_SHOW = 4;
    public static final byte OPTION_SHOW = 0;
    public static final byte OPTION_UPDATE = 2;
    static final String TAG = "AnimOptions";
    public static final String TF_EASE_IN = "ease-in";
    public static final String TF_EASE_IN_OUT = "ease-in-out";
    public static final String TF_EASE_OUT = "ease-out";
    public static final String TF_LINEAR = "linear";
    public static final HashMap<String, String> mAnimTypes;
    public AnimMode mAnimMode;
    public String mAnimType_close;
    public IFrameView mHostFrame;
    public AdaFrameItem mRelFrameItem;
    public AdaFrameItem mUserFrameItem;
    public int sScreenHeight;
    public int sScreenWidth;
    public String timingfunction = TF_LINEAR;
    public int duration = ANIM_TIME;
    public int duration_show = ANIM_TIME;
    public int duration_close = ANIM_TIME;
    public String translate = "";
    public String scale = "";
    public String opacity = "";
    public String rotate = "";
    public String mAnimType = "none";
    public byte mOption = 0;
    public ArrayList<String> mStartCallback = null;
    public ArrayList<String> mEndCallback = null;
    public AnimationSet mAnimator = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum AnimMode {
        CUSTOM
    }

    public void parseTransform(JSONObject jSONObject) {
    }

    public Animator createAnimation() {
        AdaFrameItem adaFrameItem;
        this.mAnimator = null;
        try {
            if (this.mUserFrameItem.obtainFrameOptions_Animate() == null && this.mOption != 2) {
                this.mUserFrameItem.makeViewOptions_animate();
            }
            if (this.mAnimMode == AnimMode.CUSTOM) {
                return null;
            }
            byte b = this.mOption;
            if (2 == b) {
                if (isUseBackground()) {
                    adaFrameItem = ((IFrameView) this.mUserFrameItem).obtainWebviewParent();
                } else {
                    adaFrameItem = this.mUserFrameItem;
                }
                return setStyleOptionAnimator(adaFrameItem, adaFrameItem.obtainFrameOptions(), adaFrameItem.obtainFrameOptions_Animate(), adaFrameItem.obtainFrameOptions_Birth());
            }
            if (b != 0 && b != 4) {
                if (1 != b && 3 != b) {
                    return null;
                }
                AdaFrameItem adaFrameItem2 = this.mUserFrameItem;
                return closeOrHideAnimator(adaFrameItem2, adaFrameItem2.obtainFrameOptions(), this.mUserFrameItem.obtainFrameOptions_Animate(), this.mUserFrameItem.obtainFrameOptions_Birth());
            }
            AdaFrameItem adaFrameItem3 = this.mUserFrameItem;
            return showOrHideShowAnimator(adaFrameItem3, adaFrameItem3.obtainFrameOptions(), this.mUserFrameItem.obtainFrameOptions_Animate(), this.mUserFrameItem.obtainFrameOptions_Birth());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    boolean isUseBackground() {
        return this.mUserFrameItem.obtainFrameOptions().background != -1;
    }

    public void parseTransition(JSONObject jSONObject) {
        String string = JSONUtil.getString(jSONObject, AbsoluteConst.TRANS_DURATION);
        if (string != null && string.toLowerCase().endsWith("ms")) {
            string = string.substring(0, string.length() - 2);
        }
        this.duration = PdrUtil.parseInt(string, this.duration);
        this.timingfunction = JSONUtil.getString(jSONObject, AbsoluteConst.TRANS_TIMING_FUNCTION);
    }

    private void setTimingFunction(Animator animator) {
        String lowerCase = (PdrUtil.isEmpty(this.timingfunction) ? TF_LINEAR : this.timingfunction).toLowerCase();
        Logger.d(TAG, "timingfunction = " + lowerCase);
        if (PdrUtil.isEquals(TF_EASE_IN, lowerCase)) {
            animator.setInterpolator(new AccelerateInterpolator(1.5f));
            return;
        }
        if (PdrUtil.isEquals(TF_EASE_OUT, lowerCase)) {
            animator.setInterpolator(new DecelerateInterpolator(1.5f));
        } else if (PdrUtil.isEquals(TF_LINEAR, lowerCase)) {
            animator.setInterpolator(new LinearInterpolator());
        } else {
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
    }

    private final Animator setStyleOptionAnimator(AdaFrameItem adaFrameItem, ViewOptions viewOptions, ViewOptions viewOptions2, ViewOptions viewOptions3) {
        int i;
        int i2;
        int i3;
        int i4;
        AnimatorSet animatorSet = new AnimatorSet();
        int i5 = viewOptions.left;
        int i6 = viewOptions.top;
        int i7 = viewOptions.width;
        int i8 = viewOptions.height;
        int i9 = viewOptions2.left;
        int i10 = viewOptions2.top;
        int i11 = viewOptions2.width;
        int i12 = viewOptions2.height;
        boolean z = (i7 == i11 && i8 == i12) ? false : true;
        Logger.d(Logger.ANIMATION_TAG, "createAnimSet_update _oldX=" + i5 + ";_oldY=" + i6 + ";_newX=" + i9 + ";_newY=" + i10);
        if (i5 == i9 && i6 == i10) {
            i2 = i8;
            i = i12;
            i4 = i7;
            i3 = i11;
        } else {
            i = i12;
            i2 = i8;
            i3 = i11;
            if (!isUseBackground()) {
                i4 = i7;
                Logger.d(Logger.ANIMATION_TAG, "createAnimSet_update not webview mode fromXDelta=" + i5 + ";toXDelta=" + i9 + ";fromYDelta=" + i6 + ";toYDelta=" + i10);
                ofFloat(animatorSet, adaFrameItem, "x", i5, i9);
                ofFloat(animatorSet, adaFrameItem, "y", i6, i10);
            } else {
                i4 = i7;
                Logger.d(Logger.ANIMATION_TAG, "createAnimSet_update not webview mode fromXDelta=" + i5 + ";toXDelta=" + i9 + ";fromYDelta=" + i6 + ";toYDelta=" + i10);
                ofFloat(animatorSet, adaFrameItem, "x", i5, i9);
                ofFloat(animatorSet, adaFrameItem, "y", i6, i10);
            }
        }
        if (z) {
            int i13 = i4;
            int i14 = this.sScreenWidth;
            float f = i13 / i14;
            int i15 = i3;
            float f2 = i15 / i14;
            int i16 = i2;
            int i17 = this.sScreenHeight;
            int i18 = i;
            Logger.d(Logger.ANIMATION_TAG, "width (" + f + ";=" + f2 + ");height(" + (i16 / i17) + JSUtil.COMMA + (i18 / i17) + ")");
            ofInt(animatorSet, adaFrameItem, AbsoluteConst.JSON_KEY_WIDTH, i13, i15);
            ofInt(animatorSet, adaFrameItem, "height", i16, i18);
        }
        setTimingFunction(animatorSet);
        animatorSet.setDuration(this.duration);
        return animatorSet;
    }

    private final AnimatorSet showOrHideShowAnimator(AdaFrameItem adaFrameItem, ViewOptions viewOptions, ViewOptions viewOptions2, ViewOptions viewOptions3) {
        String str = PdrUtil.isEmpty(this.mAnimType) ? "none" : this.mAnimType;
        AnimatorSet animatorSet = new AnimatorSet();
        Logger.d(Logger.ANIMATION_TAG, "showOrHideShowAnimator _animType=" + str);
        if (PdrUtil.isEquals(str, ANIM_ZOOM_OUT)) {
            ofFloat(animatorSet, adaFrameItem, "scaleX", 0.0f, 1.0f);
            ofFloat(animatorSet, adaFrameItem, "scaleY", 0.0f, 1.0f);
        } else if (!PdrUtil.isEquals(str, ANIM_PAGE_FORWARD) && PdrUtil.isEquals(str, ANIM_ZOOM_FADE_OUT)) {
            ofFloat(animatorSet, adaFrameItem, "scaleX", 0.8f, 1.0f);
            ofFloat(animatorSet, adaFrameItem, "scaleY", 0.8f, 1.0f);
            ofFloat(animatorSet, adaFrameItem, "alpha", 0.0f, 1.0f);
        }
        if (PdrUtil.isEquals(str, ANIM_FADE_IN)) {
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
            this.mAnimator = animationSet;
            animationSet.setDuration(this.duration_show);
        } else {
            int i = viewOptions.left;
            int i2 = viewOptions.top;
            int i3 = viewOptions2.left;
            int i4 = viewOptions2.top;
            Logger.d(Logger.ANIMATION_TAG, "showOrHideShowAnimator _animType=" + str + ";fromXDelta=" + i + ";toXDelta=" + i3 + ";fromYDelta=" + i2 + ";toYDelta=" + i4);
            if (PdrUtil.isEquals(str, ANIM_FLIP_X)) {
                ofFloat(animatorSet, adaFrameItem, "rotationX", -90.0f, 0.0f);
            } else if (PdrUtil.isEquals(str, ANIM_FLIP_RX)) {
                ofFloat(animatorSet, adaFrameItem, "rotationX", 0.0f, 90.0f);
            } else if (PdrUtil.isEquals(str, ANIM_FLIP_Y)) {
                ofFloat(animatorSet, adaFrameItem, "rotationY", -90.0f, 0.0f);
            } else if (PdrUtil.isEquals(str, ANIM_FLIP_RY)) {
                ofFloat(animatorSet, adaFrameItem, "rotationY", 0.0f, 90.0f);
            } else if (PdrUtil.isEquals(str, ANIM_SLIDE_IN_RIGHT) || PdrUtil.isEquals(str, ANIM_POP_IN)) {
                if (isUseBackground()) {
                    ofFloat(animatorSet, adaFrameItem, "x", i, i3);
                } else {
                    ofFloat(animatorSet, adaFrameItem, "x", i, i3);
                }
            } else if (PdrUtil.isEquals(str, ANIM_SLIDE_IN_LEFT)) {
                ofFloat(animatorSet, adaFrameItem, "x", i, i3);
            } else if (PdrUtil.isEquals(str, ANIM_SLIDE_IN_TOP)) {
                ofFloat(animatorSet, adaFrameItem, "y", i2, i4);
            } else if (PdrUtil.isEquals(str, ANIM_SLIDE_IN_BOTTOM)) {
                ofFloat(animatorSet, adaFrameItem, "y", i2, i4);
            }
        }
        setTimingFunction(animatorSet);
        animatorSet.setDuration(this.duration_show);
        return animatorSet;
    }

    ObjectAnimator ofFloat(AnimatorSet animatorSet, Object obj, String str, float... fArr) {
        if (fArr[0] == fArr[1]) {
            return null;
        }
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setPropertyName(str);
        objectAnimator.setFloatValues(fArr);
        animatorSet.playTogether(objectAnimator);
        return objectAnimator;
    }

    ObjectAnimator ofInt(AnimatorSet animatorSet, Object obj, String str, int... iArr) {
        if (iArr[0] == iArr[1]) {
            return null;
        }
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setPropertyName(str);
        objectAnimator.setIntValues(iArr);
        animatorSet.playTogether(objectAnimator);
        return objectAnimator;
    }

    private final Animator closeOrHideAnimator(AdaFrameItem adaFrameItem, ViewOptions viewOptions, ViewOptions viewOptions2, ViewOptions viewOptions3) {
        String str = this.mAnimType_close;
        HashMap<String, String> hashMap = mAnimTypes;
        if (!hashMap.containsValue(str)) {
            str = hashMap.get(this.mAnimType);
        }
        Logger.d(Logger.ANIMATION_TAG, "closeOrHideAnimator _animType=" + str);
        AnimatorSet animatorSet = new AnimatorSet();
        if (PdrUtil.isEquals(str, ANIM_FADE_OUT)) {
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
            this.mAnimator = animationSet;
            animationSet.setDuration(this.duration_show);
        } else if (PdrUtil.isEquals(str, ANIM_ZOOM_IN)) {
            ofFloat(animatorSet, adaFrameItem, "scaleX", 1.0f, 0.0f);
            ofFloat(animatorSet, adaFrameItem, "scaleY", 1.0f, 0.0f);
        } else if (PdrUtil.isEquals(str, ANIM_ZOOM_FADE_IN)) {
            ofFloat(animatorSet, adaFrameItem, "scaleX", 1.0f, 0.8f);
            ofFloat(animatorSet, adaFrameItem, "scaleY", 1.0f, 0.8f);
            ofFloat(animatorSet, adaFrameItem, "alpha", 1.0f, 0.0f);
        } else if (!PdrUtil.isEquals(str, ANIM_PAGE_BACKWARD)) {
            int i = viewOptions.left;
            int i2 = viewOptions.top;
            int i3 = viewOptions2.left;
            int i4 = viewOptions2.top;
            Logger.d(Logger.ANIMATION_TAG, "closeOrHideAnimator _animType=" + str + ";fromXDelta=" + i + ";toXDelta=" + i3 + ";fromYDelta=" + i2 + ";toYDelta=" + i4);
            if (PdrUtil.isEquals(str, ANIM_FLIP_X)) {
                ofFloat(animatorSet, adaFrameItem, "rotationX", -90.0f, 0.0f);
            } else if (PdrUtil.isEquals(str, ANIM_FLIP_Y)) {
                ofFloat(animatorSet, adaFrameItem, "rotationY", -90.0f, 0.0f);
            } else if (PdrUtil.isEquals(str, ANIM_FLIP_RX)) {
                ofFloat(animatorSet, adaFrameItem, "rotationX", 0.0f, 90.0f);
            } else if (PdrUtil.isEquals(str, ANIM_FLIP_RY)) {
                ofFloat(animatorSet, adaFrameItem, "rotationY", 0.0f, 90.0f);
            } else if (PdrUtil.isEquals(str, ANIM_SLIDE_OUT_RIGHT) || PdrUtil.isEquals(str, ANIM_POP_OUT)) {
                ofFloat(animatorSet, adaFrameItem, "x", i, i3);
            } else if (PdrUtil.isEquals(str, ANIM_SLIDE_OUT_LEFT)) {
                if (isUseBackground()) {
                    ofFloat(animatorSet, adaFrameItem, "x", 0.0f, -this.sScreenWidth);
                } else {
                    ofFloat(animatorSet, adaFrameItem, "x", i, i3);
                }
            } else if (PdrUtil.isEquals(str, ANIM_SLIDE_OUT_TOP)) {
                if (isUseBackground()) {
                    ofFloat(animatorSet, adaFrameItem, "y", this.sScreenHeight, 0.0f);
                } else {
                    ofFloat(animatorSet, adaFrameItem, "y", i2, i4);
                }
            } else if (PdrUtil.isEquals(str, ANIM_SLIDE_OUT_BOTTOM)) {
                if (isUseBackground()) {
                    float f = i;
                    ofFloat(animatorSet, adaFrameItem, "x", f, f);
                    ofFloat(animatorSet, adaFrameItem, "y", 0.0f, this.sScreenHeight);
                } else {
                    float f2 = i;
                    ofFloat(animatorSet, adaFrameItem, "x", f2, f2);
                    ofFloat(animatorSet, adaFrameItem, "y", i2, i4);
                }
            }
        }
        setTimingFunction(animatorSet);
        animatorSet.setDuration(this.duration_close);
        return animatorSet;
    }

    static {
        HashMap<String, String> hashMap = new HashMap<>(12);
        mAnimTypes = hashMap;
        hashMap.put(ANIM_SLIDE_IN_RIGHT, ANIM_SLIDE_OUT_RIGHT);
        hashMap.put(ANIM_SLIDE_IN_LEFT, ANIM_SLIDE_OUT_LEFT);
        hashMap.put(ANIM_SLIDE_IN_TOP, ANIM_SLIDE_OUT_TOP);
        hashMap.put(ANIM_SLIDE_IN_BOTTOM, ANIM_SLIDE_OUT_BOTTOM);
        hashMap.put(ANIM_ZOOM_OUT, ANIM_ZOOM_IN);
        hashMap.put(ANIM_ZOOM_FADE_OUT, ANIM_ZOOM_FADE_IN);
        hashMap.put(ANIM_FADE_IN, ANIM_FADE_OUT);
        hashMap.put(ANIM_FLIP_X, ANIM_FLIP_RX);
        hashMap.put(ANIM_FLIP_RX, ANIM_FLIP_X);
        hashMap.put(ANIM_FLIP_Y, ANIM_FLIP_RY);
        hashMap.put(ANIM_FLIP_RY, ANIM_FLIP_Y);
        hashMap.put(ANIM_PAGE_FORWARD, ANIM_PAGE_BACKWARD);
        hashMap.put("none", "none");
        hashMap.put(ANIM_POP_IN, ANIM_POP_OUT);
    }

    public static String getCloseAnimType(String str) {
        HashMap<String, String> hashMap = mAnimTypes;
        if (hashMap != null) {
            return !hashMap.containsValue(str) ? hashMap.get(str) : str;
        }
        return null;
    }

    public void setCloseAnimType(String str) {
        if (PdrUtil.isEquals("auto", str)) {
            this.mAnimType_close = mAnimTypes.get(this.mAnimType);
            return;
        }
        this.mAnimType_close = str;
        if (mAnimTypes.containsValue(str)) {
            return;
        }
        this.mAnimType_close = "none";
    }
}
