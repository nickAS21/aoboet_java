package io.dcloud.feature.barcode.barcode_a;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;

import io.dcloud.feature.barcode.barcode_b.IBarHandler;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PreviewCallback.java */
/* loaded from: classes.dex old f */
public final class PreviewCallback implements Camera.PreviewCallback {
    private static final String a = "f";
    private final CameraConfigurationManager b;
    private final boolean c;
    private Handler d;
    private IBarHandler e;
    private int f;
    private byte[] g = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PreviewCallback(CameraConfigurationManager bVar, boolean z) {
        this.b = bVar;
        this.c = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(IBarHandler gVar, Handler handler, int i) {
        this.e = gVar;
        this.d = handler;
        this.f = i;
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        IBarHandler gVar = this.e;
        if (gVar == null || !gVar.isRunning()) {
            return;
        }
        Point a2 = this.b.a();
        if (!this.c) {
            camera.setPreviewCallback(null);
        }
        Handler handler = this.d;
        if (handler != null) {
            handler.obtainMessage(this.f, a2.x, a2.y, bArr).sendToTarget();
            this.d = null;
        } else {
            Log.d(a, "Got preview callback, but no handler for it");
        }
        this.g = bArr;
    }

    public byte[] a() {
        return this.g;
    }

    public void a(byte[] bArr) {
        this.g = bArr;
    }
}
