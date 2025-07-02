package io.dcloud.feature.internal.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* loaded from: classes.dex */
public class SplashView4StreamApp extends RelativeLayout implements ISplash {
    private Bitmap a;
    private ImageView b;
    private TextView c;

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public SplashView4StreamApp(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public SplashView4StreamApp(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // io.dcloud.feature.internal.splash.c
    public void a(Bitmap bitmap) {
        if (bitmap != null) {
            this.a = bitmap;
            this.b.setImageBitmap(bitmap);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
            alphaAnimation.setDuration(800L);
            this.b.startAnimation(alphaAnimation);
        }
    }

    @Override // io.dcloud.feature.internal.splash.c
    public void a(String str) {
        TextView textView = this.c;
        if (textView == null || !TextUtils.isEmpty(textView.getText())) {
            return;
        }
        this.c.setText(str);
    }
}
