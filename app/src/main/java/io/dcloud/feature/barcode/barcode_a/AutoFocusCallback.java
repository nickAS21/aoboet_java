package io.dcloud.feature.barcode.barcode_a;

import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;

/* compiled from: AutoFocusCallback.java */
/* loaded from: classes.dex  old a*/
final class AutoFocusCallback implements Camera.AutoFocusCallback {
    private static final String a = "a";
    private Handler b;
    private int c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Handler handler, int i) {
        this.b = handler;
        this.c = i;
    }

    @Override // android.hardware.Camera.AutoFocusCallback
    public void onAutoFocus(boolean z, Camera camera) {
        Handler handler = this.b;
        if (handler != null) {
            this.b.sendMessageDelayed(handler.obtainMessage(this.c, Boolean.valueOf(z)), 200L);
            this.b = null;
            return;
        }
        Log.d(a, "Got auto-focus callback, but no handler for it");
    }
}
