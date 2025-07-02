package io.dcloud.feature.internal.splash;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* compiled from: SplashViewDBackground.java */
/* loaded from: classes.dex old */
public class SplashViewDBackground extends RelativeLayout implements ISplash {
    String a;
    DBackGround b;
    TextView c;

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public SplashViewDBackground(Context context, Bitmap bitmap, String str) {
        super(context);
        this.b = null;
        this.c = null;
        this.a = str;
        setBackgroundColor(-1);
        b(bitmap);
    }

    private void b(Bitmap bitmap) {
        DBackGround bVar = new DBackGround(getContext());
        this.b = bVar;
        int a = (int) bVar.a(65.0f);
        DBackGround bVar2 = this.b;
        bVar2.a(bitmap, a, a, (int) bVar2.a(1.0f), 12962246, -14570443);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(a, a);
        layoutParams.addRule(14);
        layoutParams.topMargin = (int) this.b.a(80.0f);
        this.b.setId(R.id.button1);
        addView(this.b, layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.addRule(3, this.b.getId());
        layoutParams2.addRule(13);
        layoutParams2.topMargin = (int) this.b.a(6.0f);
        TextView textView = new TextView(getContext());
        this.c = textView;
        textView.setSingleLine();
        this.c.setTextSize(this.b.a(6.0f));
        this.c.setTextColor(-10855846);
        a(this.a);
        this.c.setTypeface(Typeface.create("宋体", 0));
        addView(this.c, layoutParams2);
    }

    @Override // io.dcloud.feature.internal.splash.c
    public void a(String str) {
        TextView textView = this.c;
        if (textView == null || !TextUtils.isEmpty(textView.getText())) {
            return;
        }
        this.c.setText(str);
    }

    @Override // io.dcloud.feature.internal.splash.c
    public void a(Bitmap bitmap) {
        this.b.a(bitmap);
    }
}
