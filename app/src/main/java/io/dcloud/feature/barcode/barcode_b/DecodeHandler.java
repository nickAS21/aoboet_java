package io.dcloud.feature.barcode.barcode_b;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.core.view.PointerIconCompat;

import com.dcloud.zxing.BinaryBitmap;
import com.dcloud.zxing.DecodeHintType;
import com.dcloud.zxing.MultiFormatReader;
import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.ReaderException;
import com.dcloud.zxing.Result;
import com.dcloud.zxing.common.HybridBinarizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.barcode.BarcodeProxy;
import io.dcloud.feature.barcode.barcode_a.CameraManager;
import io.dcloud.feature.barcode.barcode_a.PlanarYUVLuminanceSource;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DecodeHandler.java */
/* loaded from: classes.dex  old d*/
public final class DecodeHandler extends Handler {
    private static final String a = "d";
    private final IBarHandler b;
    private final MultiFormatReader c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DecodeHandler(IBarHandler gVar, Hashtable<DecodeHintType, Object> hashtable) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        this.c = multiFormatReader;
        multiFormatReader.setHints(hashtable);
        this.b = gVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1003) {
            Looper.myLooper().quit();
        } else {
            if (i != 1004) {
                return;
            }
            a((byte[]) message.obj, message.arg1, message.arg2);
        }
    }

    private void a(byte[] bArr, int i, int i2) {
        Result result;
        PlanarYUVLuminanceSource a2 = CameraManager.a().a(bArr, i, i2);
        try {
            result = this.c.decodeWithState(new BinaryBitmap(new HybridBinarizer(a2)));
            this.c.reset();
        } catch (ReaderException unused) {
            this.c.reset();
            result = null;
        } catch (Throwable th) {
            this.c.reset();
            throw th;
        }
        Result result2 = result;
        if (BarcodeProxy.a) {
            Camera.Parameters parameters = CameraManager.a().d().getParameters();
            try {
                Camera.Size previewSize = parameters.getPreviewSize();
                YuvImage yuvImage = new YuvImage(bArr, parameters.getPreviewFormat(), previewSize.width, previewSize.height, null);
                yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 90, new FileOutputStream(new File("/sdcard/1/" + System.currentTimeMillis() + "--" + previewSize.width + "*" + previewSize.height + ".jpg")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap a3 = a2.a(true);
            Rect i3 = CameraManager.a().i();
            PdrUtil.saveBitmapToFile(a3, "/sdcard/1/" + System.currentTimeMillis() + "--" + i3.left + "*" + i3.top + ".png");
            BarcodeProxy.a = false;
            PdrUtil.alert((Activity) BarcodeProxy.b, "成功 left=" + i3.left + "top:" + i3.top, a3);
        }
        if (result2 != null) {
            Message obtain = Message.obtain(this.b.getHandler(), PointerIconCompat.TYPE_HAND, result2);
            Bundle bundle = new Bundle();
            bundle.putParcelable("barcode_bitmap", a2.a(true));
            obtain.setData(bundle);
            obtain.sendToTarget();
            return;
        }
        Message.obtain(this.b.getHandler(), PointerIconCompat.TYPE_CONTEXT_MENU).sendToTarget();
    }

    public static Result a(Bitmap bitmap) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        Hashtable hashtable = new Hashtable(2);
        Vector vector = new Vector();
        if (vector.isEmpty()) {
            vector = new Vector();
            vector.addAll(DecodeFormatManager.b);
            vector.addAll(DecodeFormatManager.c);
            vector.addAll(DecodeFormatManager.d);
        }
        hashtable.put(DecodeHintType.POSSIBLE_FORMATS, vector);
        multiFormatReader.setHints(hashtable);
        try {
            return multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
