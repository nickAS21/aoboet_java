package io.dcloud.common.adapter.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class PullRefreshView extends View {
    static final byte FLAG_MOVEED = 1;
    static final byte FLAG_NO_THING = -1;
    static final byte FLAG_STARTED = 0;
    static int HEIGHT = 25;
    static int MAX_FRAME_COUNT = 9;
    static final byte STATE_NO_REFRESH = 0;
    static final byte STATE_ON_MOVE_ING = 1;
    static final byte STATE_ON_OVER = 2;
    static final byte STATE_ON_REFRESH_ING = 3;
    public static final String TAG = "PullRefreshView";
    public static final byte TYPE_PULL_DOWN_REFRESH = 1;
    public static final byte TYPE_PULL_UP_REFRESH = 0;
    static final int color_tr = 16711920;
    byte SCROLL_STATE_MAX;
    byte SCROLL_STATE_MIDDLE;
    byte SCROLL_STATE_MIN;
    int changeStateHeight;
    private int contentLeft;
    private int contentTop;
    private int contentWidth;
    private RectF dst;
    int fontSize;
    int icon_x;
    int icon_y;
    int index;
    float lastScrollY;
    boolean mCaptureTouchEnd;
    String mContent_down;
    String mContent_over;
    String mContent_refresh;
    boolean mEnableScrollMaxHeight;
    boolean mEnableScrollMinHeight;
    byte mFlag;
    float mFontScale;
    Bitmap mIcon;
    AdaFrameItem mParent;
    boolean mRefreshState;
    int mScrollHeight;
    byte mScrollState;
    String mSecInfo;
    String mShowContent;
    byte mState;
    byte mType;
    Timer mUpdateProgressBar;
    AdaWebview mWebview;
    private float mWebviewScale;
    int maxPullHeight;
    Paint paint;
    int sScreenHeight;
    int sScreenWidth;
    private Rect src;
    float startX;
    float startY;
    boolean touch_started;

    public PullRefreshView(AdaFrameItem adaFrameItem, AdaWebview adaWebview) {
        super(adaFrameItem.getContext());
        this.mType = (byte) 1;
        this.mContent_down = "下拉可刷新";
        this.mContent_over = "松开后刷新";
        this.mContent_refresh = "正在刷新…";
        this.mShowContent = null;
        this.mSecInfo = null;
        this.mIcon = null;
        this.mFontScale = 1.2f;
        this.changeStateHeight = 100;
        this.maxPullHeight = 100;
        this.paint = new Paint();
        this.index = 0;
        this.contentLeft = 0;
        this.contentTop = 0;
        this.contentWidth = 0;
        this.mState = (byte) 0;
        this.touch_started = false;
        this.mCaptureTouchEnd = false;
        this.mFlag = (byte) 0;
        this.mScrollHeight = 0;
        this.SCROLL_STATE_MIN = (byte) 0;
        this.SCROLL_STATE_MAX = (byte) 1;
        this.SCROLL_STATE_MIDDLE = (byte) 2;
        this.mScrollState = (byte) 0;
        this.mEnableScrollMinHeight = true;
        this.mEnableScrollMaxHeight = true;
        this.mRefreshState = false;
        this.mUpdateProgressBar = null;
        this.mParent = adaFrameItem;
        this.mWebview = adaWebview;
        this.sScreenWidth = adaWebview.obtainApp().getInt(0);
        this.sScreenHeight = adaWebview.obtainApp().getInt(1);
        this.mWebviewScale = adaWebview.getScale();
        init(null);
        this.paint.setAntiAlias(true);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mState != 0) {
            canvas.drawColor(-1907998);
            this.paint.setColor(-16777216);
            this.paint.setTextSize(this.fontSize);
            canvas.drawText(this.mShowContent, this.contentLeft, this.contentTop, this.paint);
            Bitmap bitmap = this.mIcon;
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, this.src, this.dst, this.paint);
            }
        }
    }

    public void updateScreen() {
        int i = this.index + 1;
        this.index = i;
        if (i >= MAX_FRAME_COUNT) {
            this.index = 0;
        }
        Rect rect = this.src;
        int i2 = HEIGHT;
        int i3 = this.index;
        rect.set(i2 * i3, 0, (i3 + 1) * i2, i2);
        postInvalidate();
    }

    public void init(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap decodeStream = BitmapFactory.decodeStream(PlatformUtil.getResInputStream(AbsoluteConst.RES_PROGRASS_SNOW1), null, options);
        this.mIcon = decodeStream;
        int height = decodeStream.getHeight();
        HEIGHT = height;
        int i = this.sScreenWidth;
        this.contentLeft = (int) (i * 0.43f);
        this.icon_x = ((int) (i * 0.41f)) - height;
        int i2 = HEIGHT;
        this.src = new Rect(0, 0, i2, i2);
        int i3 = HEIGHT;
        this.dst = new RectF(0.0f, 150.0f, i3, i3);
        MAX_FRAME_COUNT = decodeStream.getWidth() / HEIGHT;
    }

    public void parseJsonOption(JSONObject jSONObject) {
        try {
            if (!jSONObject.isNull("height")) {
                int convertToScreenInt = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "height"), this.mWebview.mFrameView.mViewOptions.height, this.changeStateHeight, this.mWebviewScale);
                this.changeStateHeight = convertToScreenInt;
                this.maxPullHeight = convertToScreenInt;
            }
            if (!jSONObject.isNull(AbsoluteConst.PULL_REFRESH_RANGE)) {
                this.maxPullHeight = PdrUtil.convertToScreenInt(jSONObject.getString(AbsoluteConst.PULL_REFRESH_RANGE), this.mWebview.mFrameView.mViewOptions.height, this.changeStateHeight, this.mWebviewScale);
            }
            if (!jSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTDOWN)) {
                changeStringInfo(JSONUtil.getString(jSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTDOWN), AbsoluteConst.PULL_REFRESH_CAPTION));
            }
            if (!jSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTOVER)) {
                this.mContent_over = JSONUtil.getString(jSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTOVER), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            if (!jSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTREFRESH)) {
                this.mContent_refresh = JSONUtil.getString(jSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTREFRESH), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            int max = Math.max(this.maxPullHeight - this.changeStateHeight, 0);
            Paint.FontMetricsInt fontMetricsInt = DeviceInfo.sPaint.getFontMetricsInt();
            this.contentTop = (this.changeStateHeight >> 1) + max + ((fontMetricsInt.bottom - fontMetricsInt.top) >> 1);
            int i = max + ((this.changeStateHeight - HEIGHT) >> 1);
            this.icon_y = i;
            int r1 = mIcon != null ? mIcon.getHeight() : 0;
            int r2 = mIcon != null ? mIcon.getWidth() : 0;
            this.dst.set(this.icon_x, i, r2 + r1, i + r1);
            Logger.d(TAG, "height=" + this.changeStateHeight + ";range=" + this.maxPullHeight + ";contentdown=" + this.mShowContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeStringInfo(String str) {
        this.mShowContent = str;
        this.fontSize = (int) (DeviceInfo.DEFAULT_FONT_SIZE * DeviceInfo.sDensity * this.mFontScale);
        this.paint.setAntiAlias(true);
        int length = str.length();
        float[] fArr = new float[length];
        this.paint.getTextWidths(str, fArr);
        float f = 0.0f;
        for (int i = 0; i < length; i++) {
            f += fArr[i];
        }
        this.contentWidth = (int) f;
    }

    void setColorByParentChild(View view) {
        for (int i = 0; i != 2; i++) {
            view = (View) view.getParent();
            view.setBackgroundColor(color_tr);
        }
    }

    void onPullDown_start(float f, float f2) {
        if (this.touch_started) {
            return;
        }
        Logger.d(TAG, "onPullDown_start");
        this.startX = f;
        this.startY = f2;
        this.lastScrollY = f2;
        if (getParent() == null) {
            ((ViewGroup) this.mParent.obtainMainView()).addView(this, 0, new AbsoluteLayout.LayoutParams(-1, this.maxPullHeight, this.mWebview.mViewOptions.left, this.mWebview.mViewOptions.top - this.maxPullHeight));
        }
        byte b = this.mState;
        if (b == 0) {
            this.mState = (byte) 1;
            this.mFlag = (byte) 0;
        } else if (b == 3) {
            this.mFlag = (byte) 0;
        }
        this.touch_started = true;
    }

    private boolean updateScrollState(byte b) {
        this.mScrollState = b;
        boolean z = false;
        if (b == this.SCROLL_STATE_MAX) {
            this.mScrollHeight = this.maxPullHeight;
            if (this.mEnableScrollMaxHeight) {
                this.mEnableScrollMaxHeight = false;
                z = true;
            }
            this.mEnableScrollMinHeight = true;
            return z;
        }
        if (b == this.SCROLL_STATE_MIN) {
            this.mEnableScrollMaxHeight = true;
            this.mScrollHeight = 0;
            if (!this.mEnableScrollMinHeight) {
                return false;
            }
            this.mEnableScrollMinHeight = false;
        } else {
            if (b != this.SCROLL_STATE_MIDDLE) {
                return false;
            }
            this.mEnableScrollMinHeight = true;
            this.mEnableScrollMaxHeight = true;
        }
        return true;
    }

    boolean onMove(float f, float f2) {
        boolean updateScrollState;
        int i = (int) (((f2 - this.lastScrollY) * this.maxPullHeight) / this.mWebview.mFrameView.mViewOptions.height);
        if (Math.abs(i) < 1) {
            if (this.mScrollHeight > 0) {
                return true;
            }
            float f3 = i;
            if ((f3 <= 0.5f && f3 >= -0.5f) || this.mFlag != 0) {
                return false;
            }
            this.mFlag = (byte) 1;
            return false;
        }
        int i2 = this.mScrollHeight + i;
        this.mScrollHeight = i2;
        if (i2 >= this.maxPullHeight) {
            updateScrollState = updateScrollState(this.SCROLL_STATE_MAX);
        } else if (i2 <= 0) {
            updateScrollState = updateScrollState(this.SCROLL_STATE_MIN);
        } else {
            updateScrollState = updateScrollState(this.SCROLL_STATE_MIDDLE);
        }
        byte b = this.mState;
        if (b != 3) {
            if (b == 1 && this.mScrollHeight >= this.changeStateHeight) {
                this.mState = (byte) 2;
                changeStringInfo(this.mContent_over);
            } else if (b == 2 && this.mScrollHeight < this.changeStateHeight) {
                this.mState = (byte) 1;
                changeStringInfo(this.mContent_down);
            }
        }
        if (updateScrollState) {
            if (this.mFlag == 0) {
                this.mFlag = (byte) 1;
                Logger.d(TAG, "onMove; mFlag=FLAG_MOVEED");
                startUpdateScreenTimer();
            }
            this.mParent.obtainMainView().scrollBy(0, -i);
            this.lastScrollY = f2;
        }
        return updateScrollState;
    }

    void onExecuting() {
        Logger.d(TAG, "onExecuting");
        this.mState = (byte) 3;
        this.mRefreshState = true;
        this.mFlag = (byte) -1;
        Logger.d(TAG, "onExecuting; mFlag = FLAG_NO_THING");
        changeStringInfo(this.mContent_refresh);
        this.mScrollHeight = this.changeStateHeight;
        smoothScrollTo((ViewGroup) this.mParent.obtainMainView(), null, 0, -this.changeStateHeight, 1);
    }

    private void stopUpdateScreenTimer() {
        Timer timer = this.mUpdateProgressBar;
        if (timer != null) {
            timer.cancel();
            this.mUpdateProgressBar = null;
        }
    }

    private void startUpdateScreenTimer() {
        stopUpdateScreenTimer();
        Timer timer = new Timer();
        this.mUpdateProgressBar = timer;
        timer.schedule(new TimerTask() { // from class: io.dcloud.common.adapter.ui.PullRefreshView.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                PullRefreshView.this.updateScreen();
            }
        }, 0L, 100L);
    }

    void onPullDown_end() {
        if (this.mScrollHeight <= this.changeStateHeight) {
            this.mState = (byte) 0;
            this.mScrollHeight = 0;
            this.mFlag = (byte) -1;
            Logger.d(TAG, "onPullDown_end; mFlag = FLAG_NO_THING");
            changeStringInfo(this.mContent_down);
            Timer timer = this.mUpdateProgressBar;
            if (timer != null) {
                timer.cancel();
            }
            this.mParent.obtainMainView().scrollTo(0, 0);
            stopUpdateScreenTimer();
        } else {
            smoothScrollToStateHeight(true);
        }
        this.mRefreshState = false;
    }

    void smoothScrollToStateHeight(boolean z) {
        if (z) {
            this.mScrollHeight = this.changeStateHeight;
            smoothScrollTo((ViewGroup) this.mParent.obtainMainView(), null, 0, -this.changeStateHeight, 1);
        } else if (this.mScrollHeight > this.changeStateHeight) {
            smoothScrollToStateHeight(true);
        } else {
            this.mRefreshState = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: io.dcloud.common.adapter.ui.PullRefreshView$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public static class AnonymousClass2 extends TimerTask {
        View child;
        int fromX;
        int fromY;
        ViewGroup parent;
        int timesCount;
        int toX;
        int toY;
        int vX;
        int vY;
        final /* synthetic */ Timer val$_timer;
        final /* synthetic */ ViewGroup val$pParent;
        final /* synthetic */ View val$pView;
        final /* synthetic */ int val$x;
        final /* synthetic */ int val$y;
        final int TIME = 500;
        int flagTimes = 1;

        AnonymousClass2(View view, ViewGroup viewGroup, int i, int i2, Timer timer) {
            this.val$pView = view;
            this.val$pParent = viewGroup;
            this.val$x = i;
            this.val$y = i2;
            this.val$_timer = timer;
            this.child = view;
            this.parent = viewGroup;
            this.toX = i;
            this.toY = i2;
            this.fromX = viewGroup.getScrollX();
            this.fromY = this.parent.getScrollY();
            this.timesCount = 10;
            this.vX = Math.abs(this.toX - this.fromX) / this.timesCount;
            int abs = Math.abs(this.toY - this.fromY) / this.timesCount;
            this.vY = abs;
            if (abs >= 5) {
                this.vY = 5;
                this.timesCount = Math.abs(this.toY - this.fromY) / this.vY;
            }
            this.vX = Math.abs(this.toX - this.fromX) / this.timesCount;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            int i = this.fromX + this.vX;
            int i2 = this.fromY + this.vY;
            if (this.flagTimes == this.timesCount) {
                i = this.toX;
                i2 = this.toY;
            }
            PullRefreshView.scrollToByMessage(this.parent, i, i2);
            if (this.flagTimes == this.timesCount) {
                if (this.child != null) {
                    this.parent.post(new Runnable() { // from class: io.dcloud.common.adapter.ui.PullRefreshView.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass2.this.parent.removeView(AnonymousClass2.this.child);
                        }
                    });
                }
                this.val$_timer.cancel();
            }
            this.fromX = i;
            this.fromY = i2;
            this.flagTimes++;
        }
    }

    static void smoothScrollTo(ViewGroup viewGroup, View view, int i, int i2, int i3) {
        Timer timer = new Timer();
        timer.schedule(new AnonymousClass2(view, viewGroup, i, i2, timer), 0L, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void scrollToByMessage(final View view, final int i, final int i2) {
        view.post(new Runnable() { // from class: io.dcloud.common.adapter.ui.PullRefreshView.3
            @Override // java.lang.Runnable
            public void run() {
                view.scrollTo(i, i2);
            }
        });
    }
}
