package io.dcloud.common.common_b.common_b_b;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.widget.FrameLayout;


/* compiled from: DHImageView.java */
/* loaded from: classes.dex  old e*/
public class DHImageView extends androidx.appcompat.widget.AppCompatImageView {
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

    public DHImageView(Context context) {
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
        this.setAlpha(1.0f);
        this.setScaleX(1.0f);
        this.setScaleY(1.0f);
        this.setRotationX(0.0f);
        this.setRotationY(0.0f);
        this.setTranslationX(0.0f);
        this.setTranslationY(0.0f);
        this.setX(0.0f);
        this.setY(0.0f);
        this.setScrollX(0);
        this.setScrollY(0);
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
