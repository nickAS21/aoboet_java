package com.dcloud.zxing.pdf417.detector;

import com.dcloud.zxing.ResultPoint;
import com.dcloud.zxing.common.BitMatrix;

import java.util.List;

/* loaded from: classes.dex */
public final class PDF417DetectorResult {
    private final BitMatrix bits;
    private final List<ResultPoint[]> points;

    public PDF417DetectorResult(BitMatrix bitMatrix, List<ResultPoint[]> list) {
        this.bits = bitMatrix;
        this.points = list;
    }

    public BitMatrix getBits() {
        return this.bits;
    }

    public List<ResultPoint[]> getPoints() {
        return this.points;
    }
}
