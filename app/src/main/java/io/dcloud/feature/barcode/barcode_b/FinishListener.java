package io.dcloud.feature.barcode.barcode_b;

import android.app.Activity;
import android.content.DialogInterface;

/* compiled from: FinishListener.java */
/* loaded from: classes.dex old f */
public final class FinishListener implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener, Runnable {
    private final Activity a;

    @Override // java.lang.Runnable
    public void run() {
    }

    public FinishListener(Activity activity) {
        this.a = activity;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }
}
