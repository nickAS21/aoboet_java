package io.dcloud.feature.barcode.barcode_b;

import android.graphics.Bitmap;
import android.os.Handler;

import com.dcloud.zxing.Result;

import io.dcloud.feature.barcode.view.ViewfinderView;

/* compiled from: IBarHandler.java */
/* loaded from: classes.dex */
public interface IBarHandler {
    void drawViewfinder();

    Handler getHandler();

    ViewfinderView getViewfinderView();

    void handleDecode(Result result, Bitmap bitmap);

    boolean isRunning();
}
