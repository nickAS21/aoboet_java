package io.dcloud.feature.internal.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/* compiled from: DBackGround.java */
/* loaded from: classes.dex old b*/
public class DBackGround extends View {
    Path A;
    int B;
    private DisplayMetrics C;
    Bitmap a;
    int b;
    int c;
    int d;
    int e;
    int f;
    int g;
    int h;
    int i;
    int j;
    int k;
    int l;
    int m;
    int n;
    int o;
    int p;
    int q;
    int r;
    int s;
    int t;
    int u;
    int v;
    int w;
    RectF x;
    RectF y;
    Paint z;

    public DBackGround(Context context) {
        super(context);
        this.a = null;
        this.z = new Paint();
        this.A = new Path();
        this.B = -1;
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        this.B = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a(float f) {
        if (this.C == null) {
            this.C = getResources().getDisplayMetrics();
        }
        return TypedValue.applyDimension(1, f, this.C);
    }

    public void a(Bitmap bitmap) {
        Bitmap bitmap2 = this.a;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.a.recycle();
        }
        this.a = bitmap;
    }

    public void a(Bitmap bitmap, int i, int i2, int i3, int i4, int i5) {
        int left = getLeft();
        int top = getTop();
        this.a = bitmap;
        this.b = i;
        this.c = i2;
        this.f = i3;
        int i6 = i - (i3 * 8);
        this.d = i6;
        int i7 = i2 - (i3 * 8);
        this.e = i7;
        this.k = ((i - i6) / 2) + left;
        this.l = ((i2 - i7) / 2) + top;
        this.x = new RectF(this.k, this.l, this.k + this.d, this.l + this.e);
        this.A.reset();
        this.A.addRoundRect(this.x, this.k + (this.d / 2), this.l + (this.e / 2), Path.Direction.CCW);
        int i8 = this.b;
        int i9 = this.f;
        this.j = (i8 / 2) - i9;
        this.v = i5;
        this.w = i4;
        this.h = (i8 / 2) + left;
        int i10 = this.c;
        this.i = (i10 / 2) + top;
        this.s = 270;
        this.u = 270;
        this.g = i9;
        int i11 = (i9 - i9) / 2;
        int i12 = (left + i9) - i11;
        this.m = i12;
        int i13 = (top + i9) - i11;
        this.n = i13;
        int i14 = i11 * 2;
        int i15 = (i8 - (i9 * 2)) + i14;
        this.q = i15;
        int i16 = (i10 - (i9 * 2)) + i14;
        this.r = i16;
        this.o = i12 + i15;
        this.p = i13 + i16;
        this.y = new RectF(this.m, this.n, this.o, this.p);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(this.b, this.c);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        b(canvas);
        c(canvas);
        a(canvas);
    }

    private void a(Canvas canvas) {
        this.z.reset();
        this.z.setAntiAlias(true);
        this.z.setStyle(Paint.Style.STROKE);
        this.z.setStrokeWidth(this.g);
        this.z.setColor(this.v);
        canvas.drawArc(this.y, this.s, this.t, false, this.z);
        int i = (int) (this.t + 2.0f);
        this.t = i;
        if (i > 360) {
            this.t = i - 360;
        }
        invalidate();
    }

    private void b(Canvas canvas) {
        this.z.reset();
        Bitmap bitmap = this.a;
        if (bitmap != null && !bitmap.isRecycled()) {
            canvas.save();
            canvas.clipPath(this.A);
            this.z.setAntiAlias(true);
            canvas.drawBitmap(this.a, (Rect) null, this.x, this.z);
            canvas.restore();
            this.z.setStrokeWidth((this.f * 4) + (Build.VERSION.SDK_INT > 19 ? 3 : 40));
            this.z.setStyle(Paint.Style.STROKE);
            this.z.setAntiAlias(true);
            this.z.setColor(this.B);
            float centerX = this.x.left + (this.x.width() / 2.0f);
            float centerY = this.x.top + (this.x.height() / 2.0f);
            float radiusAdjustment = (this.f * 4) / 2f;
            float strokeOffset = (Build.VERSION.SDK_INT > 19 ? 3 : 40) / 8f;
            float radius = (this.x.width() / 2.0f) + radiusAdjustment - strokeOffset;
            canvas.drawCircle(centerX, centerY, radius, this.z);
            return;
        }
        this.z.setColor(-1118482);
        this.z.setAntiAlias(true);
        this.z.setStyle(Paint.Style.FILL);
        canvas.drawCircle(this.x.left + (this.x.width() / 2.0f), this.x.top + (this.x.height() / 2.0f), this.x.width() / 2.0f, this.z);
    }

    private void c(Canvas canvas) {
        this.z.reset();
        this.z.setStrokeWidth(this.f);
        this.z.setStyle(Paint.Style.STROKE);
        this.z.setAntiAlias(true);
        this.z.setColor(this.w);
        canvas.drawCircle(this.h, this.i, this.j, this.z);
    }
}
