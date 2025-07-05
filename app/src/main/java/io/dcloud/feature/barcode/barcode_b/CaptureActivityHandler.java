package io.dcloud.feature.barcode.barcode_b;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.view.PointerIconCompat;
import android.util.Log;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.Result;

import java.util.Vector;

import io.dcloud.feature.barcode.barcode_a.CameraManager;
import io.dcloud.feature.barcode.view.ViewfinderResultPointCallback;

/* compiled from: CaptureActivityHandler.java */
/* loaded from: classes.dex old b */
public final class CaptureActivityHandler extends Handler {
    private static final String a = "b";
    private final IBarHandler b;
    private final DecodeThread c;
    private CaptureActivityHandlerState d;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CaptureActivityHandler.java */
    /* loaded from: classes.dex */
    public enum CaptureActivityHandlerState {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(IBarHandler gVar, Vector<BarcodeFormat> vector, String str) {
        this.b = gVar;
        DecodeThread eVar = new DecodeThread(gVar, vector, str, new ViewfinderResultPointCallback(gVar.getViewfinderView()));
        this.c = eVar;
        eVar.start();
        this.d = CaptureActivityHandlerState.SUCCESS;
        a();
    }

    public void a() {
        CameraManager.a().f();
        this.b.drawViewfinder();
        c();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1000:
                Log.d(a, "Got auto-focus message");
                if (this.d == CaptureActivityHandlerState.PREVIEW) {
                    CameraManager.a().a(this, 1000);
                    return;
                }
                return;
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
                Log.d(a, "CODE_DECODE_FAILED");
                this.d = CaptureActivityHandlerState.PREVIEW;
                CameraManager.a().a(this.b, this.c.a(), PointerIconCompat.TYPE_WAIT);
                return;
            case PointerIconCompat.TYPE_HAND /* 1002 */:
                Log.d(a, "Got decode succeeded message");
                this.d = CaptureActivityHandlerState.SUCCESS;
                Bundle data = message.getData();
                Bitmap bitmap = data == null ? null : (Bitmap) data.getParcelable("barcode_bitmap");
                this.b.handleDecode((Result) message.obj, bitmap);
                if (bitmap != null) {
                    bitmap.recycle();
                    System.out.println("barcode.recycle");
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void b() {
        this.d = CaptureActivityHandlerState.DONE;
        CameraManager.a().g();
        Message.obtain(this.c.a(), PointerIconCompat.TYPE_HELP).sendToTarget();
        try {
            this.c.join();
        } catch (InterruptedException unused) {
        }
        e();
    }

    public void c() {
        if (this.d == CaptureActivityHandlerState.SUCCESS) {
            this.d = CaptureActivityHandlerState.PREVIEW;
            CameraManager.a().a(this.b, this.c.a(), PointerIconCompat.TYPE_WAIT);
            d();
        }
    }

    public void d() {
        CameraManager.a().a(this, 1000);
    }

    public void e() {
        removeMessages(PointerIconCompat.TYPE_HAND);
        removeMessages(PointerIconCompat.TYPE_CONTEXT_MENU);
        CameraManager.a().h();
        this.d = CaptureActivityHandlerState.SUCCESS;
    }

    public static Result a(Bitmap bitmap) {
//        return d.a(bitmap);
        return DecodeHandler.a(bitmap);
    }
}
