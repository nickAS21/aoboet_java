package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class FrameBitmapView extends View {
    public static String BOLD = "bold";
    public static String ITALIC = "italic";
    public static String NORMAL = "normal";
    private boolean isInit;
    private float mBitmapCX;
    private float mBitmapCY;
    private int mCutIndex;
    private RectF mCutRectF;
    private float mFontCX;
    private float mFontCY;
    private int mHeight;
    private ClearAnimationListener mListener;
    private INativeBitmap mNativeBitmap;
    private Paint mPaint;
    private float mScale;
    private boolean mStopAnimation;
    RectF mTextRect;
    private String mTextValue;
    private String mTexts;
    private int mWidth;

    /* loaded from: classes.dex */
    public interface ClearAnimationListener {
        void onAnimationEnd();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    static /* synthetic */ int access$208(FrameBitmapView frameBitmapView) {
        int i = frameBitmapView.mCutIndex;
        frameBitmapView.mCutIndex = i + 1;
        return i;
    }

    public void setStopAnimation(boolean z) {
        this.mStopAnimation = z;
    }

    public FrameBitmapView(Activity activity) {
        super(activity);
        this.mTextRect = null;
        this.mCutIndex = 0;
        this.isInit = false;
        this.mStopAnimation = false;
        this.mPaint = new Paint();
    }

    public boolean isInit() {
        return this.isInit;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    public void injectionData(Object obj, String str, int i, int i2, float f) {
        this.mWidth = i;
        this.mHeight = i2;
        this.mTexts = str;
        this.mScale = f;
        this.mNativeBitmap = (INativeBitmap) obj;
        this.isInit = true;
        initBitmapXY();
        initTextData();
        bringToFront();
        invalidate();
    }

    public void configurationChanged(int i, int i2) {
        if (this.mNativeBitmap != null) {
            this.mWidth = i;
            this.mHeight = i2;
            initBitmapXY();
            initTextData();
            invalidate();
        }
    }

    private void initBitmapXY() {
        float f = this.mWidth;
        float f2 = this.mHeight;
        if (this.mNativeBitmap.getBitmap() != null) {
            this.mBitmapCX = (f / 2.0f) - (this.mNativeBitmap.getBitmap().getWidth() / 2);
            this.mBitmapCY = (f2 / 2.0f) - (this.mNativeBitmap.getBitmap().getHeight() / 2);
        }
    }

    private void initTextData() {
        String str;
        String str2;
        String str3;
        String str4 = "";
        if (!TextUtils.isEmpty(this.mTexts)) {
            try {
                JSONObject jSONObject = new JSONObject(this.mTexts);
                this.mTextValue = jSONObject.optString(IApp.ConfigProperty.CONFIG_VALUE, "");
                String str5 = this.mWidth + "px";
                String str6 = "44px";
                String str7 = "16px";
                String str8 = "#000000";
                String str9 = NORMAL;
                String str10 = AbsoluteConst.JSON_VALUE_CENTER;
                String str11 = "0px";
                if (jSONObject.has("textRect")) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("textRect");
                    str = jSONObject2.optString("top", "0px");
                    str2 = jSONObject2.optString("left", "0px");
                    str5 = jSONObject2.optString(AbsoluteConst.JSON_KEY_WIDTH, str5);
                    str6 = jSONObject2.optString("height", "44px");
                } else {
                    str = "0px";
                    str2 = str;
                }
                if (jSONObject.has("textStyles")) {
                    JSONObject jSONObject3 = jSONObject.getJSONObject("textStyles");
                    str7 = jSONObject3.optString("size", "16px");
                    str8 = jSONObject3.optString(AbsoluteConst.JSON_KEY_COLOR, "#000000");
                    String optString = jSONObject3.optString("weight", str9);
                    String optString2 = jSONObject3.optString("style", str9);
                    str4 = jSONObject3.optString("family", "");
                    str10 = jSONObject3.optString(AbsoluteConst.JSON_KEY_ALIGN, AbsoluteConst.JSON_VALUE_CENTER);
                    str11 = jSONObject3.optString(AbsoluteConst.JSON_KEY_MARGIN, "0px");
                    str3 = optString2;
                    str9 = optString;
                } else {
                    str3 = str9;
                }
                int i = this.mWidth;
                int convertToScreenInt = PdrUtil.convertToScreenInt(str2, i, i, this.mScale);
                int i2 = this.mHeight;
                int convertToScreenInt2 = PdrUtil.convertToScreenInt(str, i2, i2, this.mScale);
                int i3 = this.mWidth;
                int convertToScreenInt3 = PdrUtil.convertToScreenInt(str5, i3, i3, this.mScale);
                int i4 = this.mHeight;
                int convertToScreenInt4 = PdrUtil.convertToScreenInt(str6, i4, i4, this.mScale);
                int i5 = this.mHeight;
                int convertToScreenInt5 = PdrUtil.convertToScreenInt(str7, i5, i5, this.mScale);
                int convertToScreenInt6 = PdrUtil.convertToScreenInt(str11, convertToScreenInt3, convertToScreenInt4, this.mScale);
                this.mPaint.setTextSize(convertToScreenInt5);
                this.mPaint.setColor(PdrUtil.stringToColor(str8));
                if (!TextUtils.isEmpty(str4)) {
                    this.mPaint.setTypeface(Typeface.create(str4, Typeface.NORMAL));
                }
                this.mTextRect = new RectF(convertToScreenInt + convertToScreenInt6, convertToScreenInt2 + convertToScreenInt6, convertToScreenInt3 - convertToScreenInt6, convertToScreenInt4 - convertToScreenInt6);
                this.mPaint.setFakeBoldText(str9.equals(BOLD));
                if (str3.equals(ITALIC)) {
                    this.mPaint.setTextSkewX(-0.5f);
                }
                float fontlength = getFontlength(this.mPaint, this.mTextValue);
                float fontHeight = getFontHeight(this.mPaint);
                if (str10.equals("right")) {
                    this.mFontCX = this.mTextRect.right - fontlength;
                    this.mFontCY = this.mTextRect.top + (((int) (this.mTextRect.height() - fontHeight)) / 2);
                    return;
                } else if (str10.equals("left")) {
                    this.mFontCX = this.mTextRect.left;
                    this.mFontCY = this.mTextRect.top + (((int) (this.mTextRect.height() - fontHeight)) / 2);
                    return;
                } else {
                    this.mFontCX = this.mTextRect.left + (((int) (this.mTextRect.width() - fontlength)) / 2);
                    this.mFontCY = this.mTextRect.top + (((int) (this.mTextRect.height() - fontHeight)) / 2);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
        if (this.mNativeBitmap == null) {
            setVisibility(View.GONE);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        this.mPaint.setAntiAlias(true);
        canvas.save();
        RectF rectF = this.mCutRectF;
        if (rectF != null) {
            canvas.clipRect(rectF);
        }
        canvas.restore();
        INativeBitmap iNativeBitmap = this.mNativeBitmap;
        if (iNativeBitmap != null && iNativeBitmap.getBitmap() != null) {
            canvas.save();
            canvas.drawBitmap(this.mNativeBitmap.getBitmap(), this.mBitmapCX, this.mBitmapCY, this.mPaint);
            canvas.restore();
        }
        if (TextUtils.isEmpty(this.mTextValue)) {
            return;
        }
        canvas.save();
        canvas.clipRect(this.mTextRect);
        CanvasHelper.drawString(canvas, this.mTextValue, (int) this.mFontCX, (int) this.mFontCY, 17, this.mPaint);
        canvas.restore();
    }

    public void clearData() {
        this.mNativeBitmap = null;
        this.mTextValue = null;
        this.mFontCX = 0.0f;
        this.mFontCY = 0.0f;
        this.mCutIndex = 0;
        this.mCutRectF = null;
        this.mListener = null;
        this.mStopAnimation = false;
        this.isInit = false;
    }

    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }

    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) Math.ceil(fontMetrics.bottom - fontMetrics.top);
    }

    public static float getFontLeading(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.leading - fontMetrics.ascent;
    }

    public void runClearAnimation(int i, int i2, ClearAnimationListener clearAnimationListener) {
        this.mListener = clearAnimationListener;
        runClearAnimation(i, i2);
    }

    public void runClearAnimation(final int i, final int i2) {
        postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.FrameBitmapView.1
            @Override // java.lang.Runnable
            public void run() {
                if (FrameBitmapView.this.mStopAnimation) {
                    if (FrameBitmapView.this.mListener != null) {
                        FrameBitmapView.this.mListener.onAnimationEnd();
                        return;
                    }
                    return;
                }
                FrameBitmapView.access$208(FrameBitmapView.this);
                int height = FrameBitmapView.this.mNativeBitmap.getBitmap().getHeight();
                int width = FrameBitmapView.this.mNativeBitmap.getBitmap().getWidth();
                int i3 = height / i;
                FrameBitmapView.this.mCutRectF = new RectF(0.0f, i3 * FrameBitmapView.this.mCutIndex, width, height);
                FrameBitmapView.this.invalidate();
                int i4 = FrameBitmapView.this.mCutIndex;
                int i5 = i;
                if (i4 >= i5) {
                    if (FrameBitmapView.this.mListener != null) {
                        FrameBitmapView.this.mListener.onAnimationEnd();
                        return;
                    }
                    return;
                }
                FrameBitmapView.this.runClearAnimation(i5, i2);
            }
        }, i2);
    }
}
