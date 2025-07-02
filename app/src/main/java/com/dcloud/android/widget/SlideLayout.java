package com.dcloud.android.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import org.json.JSONObject;

import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class SlideLayout extends android.widget.AbsoluteLayout {
    private static String AFTER_SLIDE = "afterSlide";
    private static String BEFORE_SLIDE = "beforeSlide";
    private static String LEFT = "left";
    private static String RIGHT = "right";
    private static final int SCROLL_DURATION = 150;
    private static final int SNAP_VELOCITY = 1000;
    boolean isLeftSlide;
    boolean isRightSlide;
    boolean isSlideOpen;
    private boolean mCanDoSlideTransverseEvent;
    private OnStateChangeListener mChangeListener;
    private float mFirstX;
    private boolean mInterceptEventEnable;
    private boolean mIsHandledTouchEvent;
    private float mLastMotionX;
    private Scroller mScroller;
    private int mSlideLeftPosition;
    private int mSlideRightPosition;
    private int mSlideTransverseLeftMaxWitch;
    private int mSlideTransverseRightMaxWitch;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    /* loaded from: classes.dex */
    public interface OnStateChangeListener {
        void onStateChanged(String str, String str2);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mChangeListener = onStateChangeListener;
    }

    public SlideLayout(Context context) {
        super(context);
        this.mInterceptEventEnable = true;
        this.mIsHandledTouchEvent = false;
        this.mLastMotionX = -1.0f;
        this.mCanDoSlideTransverseEvent = false;
        this.mSlideTransverseLeftMaxWitch = 0;
        this.mSlideTransverseRightMaxWitch = 0;
        this.mSlideLeftPosition = -1;
        this.mSlideRightPosition = -1;
        this.isRightSlide = false;
        this.isLeftSlide = false;
        this.isSlideOpen = false;
        this.mFirstX = 0.0f;
        this.mScroller = new Scroller(getContext());
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setWidth(int i) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = i;
            requestLayout();
        }
    }

    public void setHeight(int i) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = i;
            requestLayout();
        }
    }

    public void initSlideInfo(JSONObject jSONObject, float f, int i) {
        JSONObject jSONObject2 = JSONUtil.getJSONObject(jSONObject, AbsoluteConst.BOUNCE_SLIDEO_OFFSET);
        if (jSONObject2 != null) {
            JSONObject jSONObject3 = JSONUtil.getJSONObject(jSONObject, "position");
            if (jSONObject3 != null) {
                String optString = jSONObject3.optString(LEFT);
                String optString2 = jSONObject3.optString(RIGHT);
                if (!TextUtils.isEmpty(optString)) {
                    this.mSlideLeftPosition = PdrUtil.convertToScreenInt(optString, i, i / 2, f);
                }
                if (!TextUtils.isEmpty(optString2)) {
                    this.mSlideRightPosition = PdrUtil.convertToScreenInt(optString2, i, i / 2, f);
                }
            }
            this.mInterceptEventEnable = jSONObject.optBoolean("preventTouchEvent", true);
            String string = JSONUtil.getString(jSONObject2, LEFT);
            if (!TextUtils.isEmpty(string)) {
                this.isLeftSlide = this.mSlideLeftPosition > 0;
                this.mSlideTransverseLeftMaxWitch = PdrUtil.convertToScreenInt(string, i, i / 2, f);
            }
            String string2 = JSONUtil.getString(jSONObject2, RIGHT);
            if (TextUtils.isEmpty(string2)) {
                return;
            }
            this.isRightSlide = this.mSlideRightPosition > 0;
            this.mSlideTransverseRightMaxWitch = PdrUtil.convertToScreenInt(string2, i, i / 2, f);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (!this.mInterceptEventEnable) {
            return false;
        }
        if (!this.isLeftSlide && !this.isRightSlide) {
            return false;
        }
        if (action == 3 || action == 1) {
            this.mIsHandledTouchEvent = false;
            clearChildrenCache();
            return this.mIsHandledTouchEvent;
        }
        if (action != 0 && this.mIsHandledTouchEvent) {
            return true;
        }
        if (action == 0) {
            this.mLastMotionX = motionEvent.getX();
            this.mFirstX = motionEvent.getX();
            this.mIsHandledTouchEvent = false;
            this.mCanDoSlideTransverseEvent = false;
        } else if (action == 2 && ((int) Math.abs(motionEvent.getX() - this.mFirstX)) > this.mTouchSlop) {
            enableChildrenCache();
            this.mIsHandledTouchEvent = true;
            this.mCanDoSlideTransverseEvent = true;
            requestDisallowInterceptTouchEvent(true);
        }
        return this.mIsHandledTouchEvent;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x001c, code lost:
    
        if (r0 != 3) goto L96;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r8) {
        /*
            Method dump skipped, instructions count: 379
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.android.widget.SlideLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void upSlideTo(int i) {
        if (i < 0) {
            int abs = Math.abs(i);
            int i2 = this.mSlideTransverseLeftMaxWitch;
            if (abs >= i2 / 2 && this.mSlideLeftPosition >= i2) {
                smoothScrollTo(-(i2 - Math.abs(i)), 0);
                this.isSlideOpen = true;
                setState(LEFT, AFTER_SLIDE);
                return;
            }
        }
        if (i > 0) {
            int abs2 = Math.abs(i);
            int i3 = this.mSlideTransverseRightMaxWitch;
            if (abs2 >= i3 / 2 && this.mSlideRightPosition >= i3) {
                smoothScrollTo(i3 - Math.abs(i), 0);
                this.isSlideOpen = true;
                setState(RIGHT, AFTER_SLIDE);
                return;
            }
        }
        if (i > 0) {
            smoothScrollTo(-i, 0);
            setState(RIGHT, BEFORE_SLIDE);
        } else {
            smoothScrollTo(-i, 0);
            setState(LEFT, BEFORE_SLIDE);
        }
        this.isSlideOpen = false;
    }

    private void setState(final String str, final String str2) {
        if (this.mChangeListener != null) {
            postDelayed(new Runnable() { // from class: com.dcloud.android.widget.SlideLayout.1
                @Override // java.lang.Runnable
                public void run() {
                    SlideLayout.this.mChangeListener.onStateChanged(str2, str);
                }
            }, 150L);
        }
    }

    public void setOffset(String str, String str2, float f) {
        int convertToScreenInt = PdrUtil.convertToScreenInt(str2, getWidth(), 0, f);
        int scrollX = getScrollX();
        if (str.equals(LEFT)) {
            if (convertToScreenInt == 0) {
                if (scrollX != 0) {
                    smoothScrollTo(-scrollX, 0);
                    setState(LEFT, BEFORE_SLIDE);
                    return;
                }
                return;
            }
            int i = this.mSlideLeftPosition;
            if (convertToScreenInt > i) {
                convertToScreenInt = i;
            }
            smoothScrollTo(-(convertToScreenInt - Math.abs(scrollX)), 0);
            postDelayed(new Runnable() { // from class: com.dcloud.android.widget.SlideLayout.2
                @Override // java.lang.Runnable
                public void run() {
                    SlideLayout slideLayout = SlideLayout.this;
                    slideLayout.upSlideTo(slideLayout.getScrollX());
                }
            }, (convertToScreenInt * 2) + 200);
            return;
        }
        if (convertToScreenInt == 0) {
            if (scrollX != 0) {
                smoothScrollTo(-scrollX, 0);
                setState(RIGHT, BEFORE_SLIDE);
                return;
            }
            return;
        }
        int i2 = this.mSlideRightPosition;
        if (convertToScreenInt > i2) {
            convertToScreenInt = i2;
        }
        smoothScrollTo(convertToScreenInt - Math.abs(scrollX), 0);
        postDelayed(new Runnable() { // from class: com.dcloud.android.widget.SlideLayout.3
            @Override // java.lang.Runnable
            public void run() {
                SlideLayout slideLayout = SlideLayout.this;
                slideLayout.upSlideTo(slideLayout.getScrollX());
            }
        }, (convertToScreenInt * 2) + 200);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        } else {
            clearChildrenCache();
        }
        super.computeScroll();
    }

    private void smoothScrollTo(int i, int i2) {
        enableChildrenCache();
        this.mScroller.startScroll(getScrollX(), 0, i, 0, Math.abs(i) * 2);
        invalidate();
    }

    void enableChildrenCache() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setDrawingCacheEnabled(true);
        }
    }

    void clearChildrenCache() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setDrawingCacheEnabled(false);
        }
    }

    public void setInterceptTouchEventEnabled(boolean z) {
        this.mIsHandledTouchEvent = z;
    }

    public void reset() {
        int scrollX = getScrollX();
        if (scrollX == 0) {
            return;
        }
        smoothScrollTo(-scrollX, 0);
        if (scrollX < 0) {
            setState(LEFT, BEFORE_SLIDE);
        } else {
            setState(RIGHT, BEFORE_SLIDE);
        }
    }
}
