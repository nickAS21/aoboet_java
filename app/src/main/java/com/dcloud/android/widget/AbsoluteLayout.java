package com.dcloud.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextUtils;
import android.view.MotionEvent;

import org.json.JSONObject;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.common_b.common_b_b.h;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class AbsoluteLayout extends SlideLayout {
    static final String STATE_CHANGED_TEMPLATE = "{status:'%s',offset:'%s'}";
    boolean canDoMaskClickEvent;
    float downX;
    float downY;
    private boolean isAnimate;
    private boolean isCanDrag;
    IApp mAppHandler;
    String mCallBackID;
    h mDrag;
    AdaFrameView mFrameView;
    private int mRegionBottom;
    private int mRegionLeft;
    private RectF mRegionRect;
    private int mRegionRight;
    private int mRegionTop;
    ViewOptions mViewOptions;

    public AbsoluteLayout(Context context, AdaFrameView adaFrameView, IApp iApp) {
        super(context);
        this.mFrameView = null;
        this.mViewOptions = null;
        this.mAppHandler = null;
        this.isCanDrag = false;
        this.canDoMaskClickEvent = true;
        this.mDrag = new h(adaFrameView, context);
        this.mFrameView = adaFrameView;
        this.mAppHandler = iApp;
        this.mViewOptions = adaFrameView.obtainFrameOptions();
        setOnStateChangeListener(new SlideLayout.OnStateChangeListener() { // from class: com.dcloud.android.widget.AbsoluteLayout.1
            @Override // com.dcloud.android.widget.SlideLayout.OnStateChangeListener
            public void onStateChanged(String str, String str2) {
                AbsoluteLayout.this.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_SLIDE_BOUNCE, String.format(AbsoluteLayout.STATE_CHANGED_TEMPLATE, str, str2));
            }
        });
    }

    private void doClickEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.canDoMaskClickEvent = true;
            this.downX = motionEvent.getX();
            this.downY = motionEvent.getY();
            return;
        }
        if (motionEvent.getAction() == 1) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (this.canDoMaskClickEvent) {
                float f = 10;
                if (Math.abs(this.downX - x) > f || Math.abs(this.downY - y) > f) {
                    return;
                }
                this.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_MASK_CLICK, null);
                return;
            }
            return;
        }
        if (motionEvent.getAction() == 2) {
            float x2 = motionEvent.getX();
            float y2 = motionEvent.getY();
            if (this.canDoMaskClickEvent) {
                float f2 = 10;
                if (Math.abs(this.downX - x2) <= f2 || Math.abs(this.downY - y2) <= f2) {
                    return;
                }
                this.canDoMaskClickEvent = false;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.mFrameView.interceptTouchEvent) {
            return false;
        }
        ViewOptions viewOptions = this.mViewOptions;
        if (viewOptions != null && viewOptions.hasMask()) {
            doClickEvent(motionEvent);
            if (motionEvent.getAction() == 0) {
                this.isCanDrag = false;
            }
            if (!this.isCanDrag) {
                this.isCanDrag = this.mDrag.b(motionEvent);
            }
            if (this.isCanDrag) {
                onTouchEvent(motionEvent);
            }
            return true;
        }
        ViewOptions viewOptions2 = this.mViewOptions;
        if (viewOptions2 != null && viewOptions2.hasBackground()) {
            super.dispatchTouchEvent(motionEvent);
            return true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        RectF rectF = this.mRegionRect;
        if (rectF != null) {
            canvas.clipRect(rectF, Region.Op.DIFFERENCE);
        }
        this.mFrameView.paint(canvas);
        super.dispatchDraw(canvas);
        canvas.restore();
        ViewOptions viewOptions = this.mViewOptions;
        if (viewOptions == null || !viewOptions.hasMask()) {
            return;
        }
        canvas.drawColor(this.mViewOptions.maskColor);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mRegionRect != null) {
            canvas.save();
            canvas.clipRect(this.mViewOptions.left, this.mViewOptions.top, this.mViewOptions.left + this.mViewOptions.width, this.mViewOptions.top + this.mViewOptions.height);
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public String toString() {
        return this.mFrameView.toString();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mFrameView.onConfigurationChanged();
        if (this.isAnimate) {
            this.isAnimate = false;
        }
    }

    public void animate(IWebview iWebview, String str, String str2) {
        if (this.mViewOptions != null) {
            this.mCallBackID = str2;
            try {
                JSONObject jSONObject = new JSONObject(str);
                String optString = jSONObject.optString("type");
                int optInt = jSONObject.optInt(AbsoluteConst.TRANS_DURATION, 200);
                int optInt2 = jSONObject.optInt("frames", 12);
                JSONObject optJSONObject = jSONObject.optJSONObject("region");
                if (optJSONObject != null) {
                    this.mRegionLeft = PdrUtil.convertToScreenInt(optJSONObject.optString("left"), this.mViewOptions.width, 0, this.mViewOptions.mWebviewScale);
                    this.mRegionRight = PdrUtil.convertToScreenInt(optJSONObject.optString("right"), this.mViewOptions.width, 0, this.mViewOptions.mWebviewScale);
                    this.mRegionTop = PdrUtil.convertToScreenInt(optJSONObject.optString("top"), this.mViewOptions.height, 0, this.mViewOptions.mWebviewScale);
                    this.mRegionBottom = PdrUtil.convertToScreenInt(optJSONObject.optString("bottom"), this.mViewOptions.height, 0, this.mViewOptions.mWebviewScale);
                }
                int i = optInt / optInt2;
                int i2 = (this.mViewOptions.height - ((this.mRegionTop + this.mViewOptions.top) + this.mRegionBottom)) / optInt2;
                int i3 = (this.mViewOptions.height - ((this.mRegionTop + this.mViewOptions.top) + this.mRegionBottom)) - (i2 * optInt2);
                if (TextUtils.isEmpty(optString) || !optString.equals("shrink")) {
                    return;
                }
                this.isAnimate = true;
                runDrawRectF(iWebview, str2, this.mRegionLeft, this.mRegionRight, this.mRegionTop + this.mViewOptions.top, this.mViewOptions.height - this.mRegionBottom, i, i2, optInt2, i3, 1);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                endAnimatecallback(iWebview, str2);
                return;
            }
        }
        endAnimatecallback(iWebview, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runDrawRectF(final IWebview iWebview, final String str, final int i, final int i2, final int i3, final int i4, final int i5, final int i6, final int i7, final int i8, final int i9) {
        if (!this.isAnimate) {
            endAnimatecallback(iWebview, str);
            return;
        }
        if (this.mRegionRect == null) {
            this.mRegionRect = new RectF();
        }
        this.mRegionRect.left = i;
        this.mRegionRect.right = this.mViewOptions.width - i2;
        this.mRegionRect.top = i3;
        if (i9 == i7) {
            this.mRegionRect.bottom = (i6 * i9) + i3 + i8;
        } else {
            this.mRegionRect.bottom = (i6 * i9) + i3;
        }
        postDelayed(new Runnable() { // from class: com.dcloud.android.widget.AbsoluteLayout.2
            @Override // java.lang.Runnable
            public void run() {
                AbsoluteLayout.this.invalidate();
                int i10 = i9;
                int i11 = i7;
                if (i10 == i11) {
                    AbsoluteLayout.this.endAnimatecallback(iWebview, str);
                } else {
                    AbsoluteLayout.this.runDrawRectF(iWebview, str, i, i2, i3, i4, i5, i6, i11, i8, i10 + 1);
                }
            }
        }, i5);
    }

    public void restore() {
        this.isAnimate = false;
        this.mRegionRect = null;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endAnimatecallback(IWebview iWebview, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        JSUtil.execCallback(iWebview, str, null, JSUtil.OK, false, false);
    }

    @Override // com.dcloud.android.widget.SlideLayout, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mDrag.b(motionEvent)) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // com.dcloud.android.widget.SlideLayout, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mDrag.c(motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }
}
