package io.dcloud.common.common_b.common_b_b;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import io.dcloud.nineoldandroids.view.ViewHelper;

/* compiled from: DHImageView.java */
/* loaded from: classes.dex */
public class e extends ImageView {
    boolean a;
    public long b;
    public long c;
    public Bitmap d;
    boolean e;

    public boolean a() {
        return this.e;
    }

    public void a(boolean z) {
        this.e = z;
    }

    public e(Context context) {
        super(context);
        this.a = true;
        this.b = 0L;
        this.c = 0L;
        this.e = false;
    }

    public Bitmap b() {
        return this.d;
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        if (this.e) {
            a(bitmap);
            return;
        }
        a(this.d);
        this.d = bitmap;
        if (bitmap != null) {
            this.b = bitmap.getWidth();
            this.c = bitmap.getHeight();
        } else {
            this.b = 0L;
            this.c = 0L;
        }
        super.setImageBitmap(bitmap);
    }

    public void a(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        bitmap.recycle();
    }

    public void c() {
        ViewHelper.setAlpha(this, 1.0f);
        ViewHelper.setScaleX(this, 1.0f);
        ViewHelper.setScaleY(this, 1.0f);
        ViewHelper.setRotationX(this, 0.0f);
        ViewHelper.setRotationY(this, 0.0f);
        ViewHelper.setTranslationX(this, 0.0f);
        ViewHelper.setTranslationY(this, 0.0f);
        ViewHelper.setX(this, 0.0f);
        ViewHelper.setY(this, 0.0f);
        ViewHelper.setScrollX(this, 0);
        ViewHelper.setScrollY(this, 0);
        this.e = false;
        setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        requestLayout();
    }

    public void b(boolean z) {
        this.a = z;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.a) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }
}
