package io.dcloud.feature.barcode.barcode_a;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;

import io.dcloud.feature.barcode.barcode_b.IBarHandler;
import io.dcloud.feature.barcode.view.DetectorViewConfig;

/* compiled from: CameraManager.java */
/* loaded from: classes.dex  old c*/
public final class CameraManager {
    public static int a = 0;
    public static int b = 0;
    static final int c;
    private static final String d = "c";
    private static CameraManager e;
    private final Context f;
    private final CameraConfigurationManager g;
    private Camera h;
    private Rect i;
    private boolean j;
    private boolean k;
    private boolean l;
    private final PreviewCallback m;
    private final AutoFocusCallback n;

    static {
        int i;
        try {
            i = Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException unused) {
            i = 10000;
        }
        c = i;
    }

    public static void a(Context context) {
        if (e == null) {
            e = new CameraManager(context);
        }
    }

    public static CameraManager a() {
        return e;
    }

    private CameraManager(Context context) {
        this.f = context;
        CameraConfigurationManager bVar = new CameraConfigurationManager(context);
        this.g = bVar;
        this.l = Integer.parseInt(Build.VERSION.SDK) > 3;
        this.m = new PreviewCallback(bVar, this.l);
        this.l = false;
        this.n = new AutoFocusCallback();
    }

    public byte[] b() {
        PreviewCallback fVar = this.m;
        if (fVar == null) {
            return null;
        }
        return fVar.a();
    }

    public void c() {
        PreviewCallback fVar = this.m;
        if (fVar != null) {
            fVar.a(null);
        }
    }

    public static Point a(int i, int i2) {
        Point point = null;
        try {
            Camera open = Camera.open();
            point = CameraConfigurationManager.a(open.getParameters(), new Point(i, i2));
            open.release();
            return point;
        } catch (Exception e2) {
            e2.printStackTrace();
            return point;
        }
    }

    public void a(SurfaceHolder surfaceHolder) throws IOException {
        if (this.h == null) {
            Camera open = Camera.open();
            this.h = open;
            if (open == null) {
                throw new IOException();
            }
            open.setPreviewDisplay(surfaceHolder);
            this.h.setDisplayOrientation(90);
            if (!this.j) {
                this.j = true;
                this.g.a(this.h);
            }
            this.g.b(this.h);
        }
    }

    public void a(boolean z) {
        if (z) {
            FlashlightManager.enable();
        } else {
            FlashlightManager.disable();
        }
    }

    public Camera d() {
        return this.h;
    }

    public void e() {
        if (this.h != null) {
            FlashlightManager.disable();
            this.h.release();
            this.h = null;
        }
    }

    public void f() {
        Camera camera = this.h;
        if (camera == null || this.k) {
            return;
        }
        camera.startPreview();
        this.k = true;
    }

    public void g() {
        Camera camera = this.h;
        if (camera == null || !this.k) {
            return;
        }
        if (!this.l) {
            camera.setPreviewCallback(null);
        }
        this.h.stopPreview();
        this.m.a(null, null, 0);
        this.n.a(null, 0);
        this.k = false;
    }

    public void a(IBarHandler gVar, Handler handler, int i) {
        if (this.h == null || !this.k) {
            return;
        }
        this.m.a(gVar, handler, i);
        if (this.l) {
            this.h.setOneShotPreviewCallback(this.m);
        } else {
            this.h.setPreviewCallback(this.m);
        }
    }

    public void a(Handler handler, int i) {
        if (this.h == null || !this.k) {
            return;
        }
        this.n.a(handler, i);
        try {
            this.h.autoFocus(this.n);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void h() {
        Camera camera = this.h;
        if (camera != null) {
            camera.cancelAutoFocus();
        }
    }

    public Rect i() {
        if (this.i == null) {
            Point a2 = this.g.a();
            Rect detectorRect = DetectorViewConfig.getInstance().getDetectorRect();
            Rect rect = DetectorViewConfig.getInstance().surfaceViewRect;
            int width = rect.width() / a2.y;
            int height = ((detectorRect.top - DetectorViewConfig.detectorRectOffestTop) * a2.x) / rect.height();
            int height2 = ((detectorRect.height() * a2.x) / rect.height()) + height;
            int width2 = ((rect.right - detectorRect.right) * a2.y) / rect.width();
            this.i = new Rect(height, width2, height2, ((detectorRect.width() * a2.x) / rect.height()) + width2);
        }
        return this.i;
    }

    public PlanarYUVLuminanceSource a(byte[] bArr, int i, int i2) {
        Rect i3 = i();
        int b2 = this.g.b();
        String c2 = this.g.c();
        if (b2 == 16 || b2 == 17) {
            return new PlanarYUVLuminanceSource(bArr, i, i2, i3.left, i3.top, i3.width(), i3.height());
        }
        if ("yuv420p".equals(c2)) {
            return new PlanarYUVLuminanceSource(bArr, i, i2, i3.left, i3.top, i3.width(), i3.height());
        }
        throw new IllegalArgumentException("Unsupported picture format: " + b2 + '/' + c2);
    }
}
