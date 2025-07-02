package io.dcloud.common.adapter.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.dcloud.common.adapter.ui.fresh.ILoadingLayout;
import io.dcloud.common.adapter.ui.fresh.PullToRefreshBase;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class RefreshView implements PullToRefreshBase.OnStateChangeListener {
    public static final String TAG = "RefreshView";
    private RectF dst;
    int fontSize;
    int icon_x;
    int icon_y;
    AdaFrameView mFrameView;
    JSONObject mJSONObject;
    AdaWebview mWebview;
    private float mWebviewScale;
    private Rect src;
    float startX;
    float startY;
    Bitmap mIcon = null;
    float mFontScale = 1.2f;
    int changeStateHeight = 100;
    int maxPullHeight = 100;
    Paint paint = new Paint();
    int index = 0;
    int MAX_FRAME_COUNT = 9;
    int HEIGHT = 25;
    Timer mUpdateProgressBar = null;
    private int contentLeft = 0;
    private int contentTop = 0;
    String mContent_down = "下拉可刷新";
    String mContent_over = "松开后刷新";
    String mContent_refresh = "正在刷新…";
    String mShowContent = null;
    ILoadingLayout.State mState = ILoadingLayout.State.RESET;

    public RefreshView(AdaFrameView adaFrameView, AdaWebview adaWebview) {
        this.mFrameView = adaFrameView;
        this.mWebview = adaWebview;
        this.mWebviewScale = adaWebview.getScale();
        this.paint.setAntiAlias(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paint(Canvas canvas, int i, int i2) {
        canvas.drawColor(-1907998);
        this.paint.setColor(-16777216);
        this.paint.setTextSize(this.fontSize);
        if (this.mShowContent == null || this.mIcon == null) {
            return;
        }
        computePosition(this.paint);
        CanvasHelper.drawString(canvas, this.mShowContent, this.contentLeft + i, this.contentTop + i2, 17, this.paint);
        RectF rectF = this.dst;
        int i3 = this.icon_x;
        int i4 = this.icon_y;
        int i5 = i + i3;
        int i6 = this.HEIGHT;
        rectF.set(i + i3, i2 + i4, i5 + i6, i2 + i4 + i6);
        canvas.drawBitmap(this.mIcon, this.src, this.dst, this.paint);
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
        if (this.mFrameView.obtainMainView() != null) {
            Timer timer = new Timer();
            this.mUpdateProgressBar = timer;
            timer.schedule(new TimerTask() { // from class: io.dcloud.common.adapter.ui.RefreshView.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (RefreshView.this.mFrameView.obtainMainView() == null) {
                        cancel();
                    }
                    try {
                        RefreshView.this.updateScreen();
                        RefreshView.this.mFrameView.obtainMainView().postInvalidate(0, 0, RefreshView.this.mFrameView.obtainFrameOptions().width, RefreshView.this.maxPullHeight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0L, 100L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScreen() {
        int i = this.index + 1;
        this.index = i;
        if (i >= this.MAX_FRAME_COUNT) {
            this.index = 0;
        }
        Rect rect = this.src;
        int i2 = this.HEIGHT;
        int i3 = this.index;
        rect.set(i2 * i3, 0, (i3 + 1) * i2, i2);
    }

    public void init(String str) {
        this.fontSize = (int) (DeviceInfo.DEFAULT_FONT_SIZE * DeviceInfo.sDensity * this.mFontScale);
        this.paint.setAntiAlias(true);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap decodeStream = BitmapFactory.decodeStream(PlatformUtil.getResInputStream(AbsoluteConst.RES_PROGRASS_SNOW1), null, options);
        this.mIcon = decodeStream;
        this.HEIGHT = decodeStream.getHeight();
        int i = this.HEIGHT;
        this.src = new Rect(0, 0, i, i);
        int i2 = this.HEIGHT;
        this.dst = new RectF(0.0f, 150.0f, i2, i2);
        Logger.d(TAG, "height=" + this.changeStateHeight + ";range=" + this.maxPullHeight + ";contentdown=" + this.mShowContent);
        this.MAX_FRAME_COUNT = decodeStream.getWidth() / this.HEIGHT;
    }

    private void computePosition(Paint paint) {
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int i = this.changeStateHeight;
        int i2 = this.HEIGHT;
        int i3 = (i - i2) >> 1;
        this.icon_y = i3;
        this.contentTop = i3 + ((i2 - (fontMetricsInt.bottom - fontMetricsInt.top)) / 2);
        int i4 = this.mFrameView.obtainApp().getInt(0);
        float measureText = paint.measureText(this.mShowContent);
        float f = i4;
        float f2 = 0.02f * f;
        int i5 = this.HEIGHT;
        int i6 = ((int) (((f - f2) - i5) - measureText)) / 2;
        this.icon_x = i6;
        this.contentLeft = (int) (i6 + f2 + i5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onResize() {
        parseJsonOption(this.mJSONObject);
    }

    public void parseJsonOption(JSONObject jSONObject) {
        try {
            JSONObject combinJSONObject = JSONUtil.combinJSONObject(this.mJSONObject, jSONObject);
            this.mJSONObject = combinJSONObject;
            if (!combinJSONObject.isNull("height")) {
                this.changeStateHeight = PdrUtil.convertToScreenInt(JSONUtil.getString(combinJSONObject, "height"), this.mWebview.mFrameView.mViewOptions.height, this.changeStateHeight, this.mWebviewScale);
            }
            if (!combinJSONObject.isNull(AbsoluteConst.PULL_REFRESH_RANGE)) {
                this.maxPullHeight = PdrUtil.convertToScreenInt(combinJSONObject.getString(AbsoluteConst.PULL_REFRESH_RANGE), this.mWebview.mFrameView.mViewOptions.height, this.maxPullHeight, this.mWebviewScale);
            }
            if (!combinJSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTDOWN)) {
                this.mContent_down = JSONUtil.getString(combinJSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTDOWN), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            if (!combinJSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTOVER)) {
                this.mContent_over = JSONUtil.getString(combinJSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTOVER), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            if (!combinJSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTREFRESH)) {
                this.mContent_refresh = JSONUtil.getString(combinJSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTREFRESH), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            init(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeStringInfo(String str) {
        this.mShowContent = str;
        int length = str.length();
        float[] fArr = new float[length];
        this.paint.getTextWidths(str, fArr);
        for (int i = 0; i < length; i++) {
            float f = fArr[i];
        }
    }

    @Override // io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.OnStateChangeListener
    public void onStateChanged(ILoadingLayout.State state, boolean z) {
        boolean z2 = this.mState != state;
        this.mState = state;
        if (z2) {
            if (state == ILoadingLayout.State.RESET) {
                Logger.d("refresh", "RefreshView RESET");
                changeStringInfo(this.mContent_down);
                stopUpdateScreenTimer();
            } else if (state == ILoadingLayout.State.PULL_TO_REFRESH) {
                Logger.d("refresh", "RefreshView PULL_TO_REFRESH");
                changeStringInfo(this.mContent_down);
            } else if (state == ILoadingLayout.State.RELEASE_TO_REFRESH) {
                Logger.d("refresh", "RefreshView RELEASE_TO_REFRESH");
                changeStringInfo(this.mContent_over);
            } else if (state == ILoadingLayout.State.REFRESHING) {
                Logger.d("refresh", "RefreshView REFRESHING");
                changeStringInfo(this.mContent_refresh);
                startUpdateScreenTimer();
                this.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_PULL_DOWN_EVENT, "3");
            }
        }
    }
}
