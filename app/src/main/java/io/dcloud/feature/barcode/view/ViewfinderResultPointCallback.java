package io.dcloud.feature.barcode.view;

import com.dcloud.zxing.ResultPoint;
import com.dcloud.zxing.ResultPointCallback;

/* compiled from: ViewfinderResultPointCallback.java */
/* loaded from: classes.dex old a */
public final class ViewfinderResultPointCallback implements ResultPointCallback {
    private final ViewfinderView a;

    public ViewfinderResultPointCallback(ViewfinderView bVar) {
        this.a = bVar;
    }

    @Override // com.dcloud.zxing.ResultPointCallback
    public void foundPossibleResultPoint(ResultPoint resultPoint) {
        this.a.a(resultPoint);
    }
}
