package io.dcloud.feature.barcode.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

import com.dcloud.zxing.ResultPoint;

import java.util.Collection;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import io.dcloud.feature.barcode.barcode_b.IBarHandler;

/* compiled from: ViewfinderView.java */
/* loaded from: classes.dex old b */
public final class ViewfinderView extends View {
    IBarHandler a;
    ShapeDrawable b;
    int c;
    Timer d;
    private final Paint e;
    private Collection<ResultPoint> f;
    private Rect g;
    private boolean h;

    public ViewfinderView(Context context, IBarHandler gVar) {
        super(context);
        this.a = null;
        this.g = null;
        this.b = null;
        this.c = 0;
        this.d = null;
        this.h = false;
        this.a = gVar;
        this.e = new Paint();
        getResources();
        this.f = new HashSet(5);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Rect detectorRect = DetectorViewConfig.getInstance().getDetectorRect();
        Rect rect = DetectorViewConfig.getInstance().gatherRect;
        if (detectorRect == null) {
            return;
        }
        a(canvas, detectorRect, rect);
        b(canvas, detectorRect);
        if (this.h) {
            a(canvas, detectorRect);
            return;
        }
        this.g = detectorRect;
        if (detectorRect != null) {
            a(canvas, detectorRect);
        }
    }

    private void a(Canvas canvas, Rect rect) {
        if (this.b == null) {
            this.b = new ShapeDrawable(new OvalShape());
            this.b.getPaint().setShader(new RadialGradient(rect.width() / 2, DetectorViewConfig.LASER_WIDTH / 2, 240.0f, DetectorViewConfig.laserColor, DetectorViewConfig.laserColor & 16777215, Shader.TileMode.CLAMP));
        }
        this.e.setAntiAlias(true);
        this.b.setBounds(rect.left, this.c, rect.left + rect.width(), this.c + DetectorViewConfig.LASER_WIDTH);
        this.b.draw(canvas);
        this.e.setShader(null);
    }

    public void a() {
        if (this.h) {
            Timer timer = this.d;
            if (timer != null) {
                timer.cancel();
                this.d = null;
            }
            this.h = false;
            d();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        Rect detectorRect = DetectorViewConfig.getInstance().getDetectorRect();
        if (this.c > detectorRect.bottom) {
            this.c = detectorRect.top;
        } else {
            this.c++;
        }
        postInvalidate();
    }

    public void b() {
        if (this.h) {
            return;
        }
        a();
        this.c = DetectorViewConfig.getInstance().getDetectorRect().top;
        Timer timer = new Timer();
        this.d = timer;
        timer.schedule(new TimerTask() { // from class: io.dcloud.feature.barcode.view.b.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ViewfinderView.this.d();
            }
        }, 0L, 10L);
        this.h = true;
    }

    private void a(Canvas canvas, Rect rect, Rect rect2) {
        this.e.setColor(DetectorViewConfig.maskColor);
        canvas.drawRect(0.0f, 0.0f, rect2.right, rect.top, this.e);
        canvas.drawRect(0.0f, rect.top, rect.left, rect.bottom, this.e);
        canvas.drawRect(rect.right, rect.top, rect2.right, rect.bottom, this.e);
        canvas.drawRect(0.0f, rect.bottom, rect2.right, rect2.bottom, this.e);
    }

    private void b(Canvas canvas, Rect rect) {
        this.e.setColor(DetectorViewConfig.cornerColor);
        int i = DetectorViewConfig.CORNER_WIDTH / 2;
        int i2 = DetectorViewConfig.CORNER_HEIGHT;
        canvas.drawRect(rect.left - i, rect.top - i, rect.left + i2, rect.top + i, this.e);
        canvas.drawRect(rect.left - i, rect.top, rect.left + i, rect.top + i2, this.e);
        canvas.drawRect(rect.right - i2, rect.top - i, rect.right + i, rect.top + i, this.e);
        canvas.drawRect(rect.right - i, rect.top, rect.right + i, rect.top + i2, this.e);
        canvas.drawRect(rect.left - i, rect.bottom - i2, rect.left + i, rect.bottom, this.e);
        canvas.drawRect(rect.left - i, rect.bottom - i, rect.left + i2, rect.bottom + i, this.e);
        canvas.drawRect(rect.right - i2, rect.bottom - i, rect.right + i, rect.bottom + i, this.e);
        canvas.drawRect(rect.right - i, rect.bottom - i2, rect.right + i, rect.bottom + i, this.e);
    }

    public void c() {
        invalidate();
    }

    public void a(ResultPoint resultPoint) {
        this.f.add(resultPoint);
    }
}
