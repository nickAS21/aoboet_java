package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import io.dcloud.RInformation;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class SplashView extends RelativeLayout {
    public static int STYLE_BLACK = 1;
    public static int STYLE_DEFAULT = 0;
    public static int STYLE_WHITE = 2;
    final String TAG;
    protected boolean mShowSplashScreen;
    protected boolean mShowSplashWaiting;
    int screenHeight;
    int screenWidth;
    boolean showBitmap;

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public SplashView(Context context, Bitmap bitmap) {
        super(context);
        this.mShowSplashScreen = false;
        this.mShowSplashWaiting = false;
        this.TAG = "SplashView";
        this.showBitmap = false;
        setBackgroundColor(-1);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.screenWidth = displayMetrics.widthPixels;
        this.screenHeight = displayMetrics.heightPixels;
        if (Build.VERSION.SDK_INT < 16) {
            setBackgroundDrawable(new BitmapDrawable(bitmap));
        } else {
            setBackground(new BitmapDrawable(bitmap));
        }
    }

    public void showWaiting() {
        showWaiting(STYLE_DEFAULT);
    }

    protected void onShowProgressBar(ViewGroup viewGroup, int i) {
        Drawable drawable;
        Logger.d(Logger.MAIN_TAG, "showWaiting style=" + i);
        ProgressBar progressBar = new ProgressBar(getContext());
        int parseInt = PdrUtil.parseInt("7%", this.screenWidth, -1);
        setGravity(17);
        if (i == STYLE_BLACK) {
            drawable = getContext().getResources().getDrawable(RInformation.DRAWBLE_PROGRESSBAR_BLACK_DCLOUD);
        } else {
            drawable = i == STYLE_WHITE ? getContext().getResources().getDrawable(RInformation.DRAWBLE_PROGRESSBAR_WHITE_DCLOUD) : null;
        }
        if (drawable != null) {
            progressBar.setIndeterminateDrawable(drawable);
        }
        viewGroup.addView(progressBar, new RelativeLayout.LayoutParams(parseInt, parseInt));
        Logger.d(Logger.MAIN_TAG, "onShowProgressBar");
    }

    public void showWaiting(final int i) {
        post(new Runnable() { // from class: io.dcloud.common.DHInterface.SplashView.1
            @Override // java.lang.Runnable
            public void run() {
                SplashView splashView = SplashView.this;
                splashView.onShowProgressBar(splashView, i);
            }
        });
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (DeviceInfo.sStatusBarHeight <= 0) {
            Logger.d("SplashView", "paint() before DeviceInfo.updateScreenInfo()");
            DeviceInfo.updateStatusBarHeight((Activity) getContext());
        }
        Logger.d("SplashView", "dispatchDraw.....");
        super.dispatchDraw(canvas);
    }
}
