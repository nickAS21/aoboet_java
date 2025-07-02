package io.dcloud.feature.barcode.barcode_b;

import android.os.Handler;
import android.os.Looper;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.DecodeHintType;
import com.dcloud.zxing.ResultPointCallback;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/* compiled from: DecodeThread.java */
/* loaded from: classes.dex */
final class DecodeThread extends Thread {
    private final IBarHandler a;
    private final Hashtable<DecodeHintType, Object> b;
    private Handler c;
    private final CountDownLatch d = new CountDownLatch(1);

    /* JADX INFO: Access modifiers changed from: package-private */
    public DecodeThread(IBarHandler gVar, Vector<BarcodeFormat> vector, String str, ResultPointCallback resultPointCallback) {
        this.a = gVar;
        Hashtable<DecodeHintType, Object> hashtable = new Hashtable<>(3);
        this.b = hashtable;
        if (vector == null || vector.isEmpty()) {
            vector = new Vector<>();
            vector.addAll(DecodeFormatManager.b);
            vector.addAll(DecodeFormatManager.c);
            vector.addAll(DecodeFormatManager.d);
        }
        hashtable.put(DecodeHintType.POSSIBLE_FORMATS, vector);
        if (str != null) {
            hashtable.put(DecodeHintType.CHARACTER_SET, str);
        }
        hashtable.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
        hashtable.put(DecodeHintType.TRY_HARDER, new Object());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Handler a() {
        try {
            this.d.await();
        } catch (InterruptedException unused) {
        }
        return this.c;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Looper.prepare();
        this.c = new DecodeHandler(this.a, this.b);
        this.d.countDown();
        Looper.loop();
    }
}
