package com.dcloud.zxing.common.detector;

import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.ResultPoint;
import com.dcloud.zxing.common.BitMatrix;

/* loaded from: classes.dex */
public final class WhiteRectangleDetector {
    private static final int CORR = 1;
    private static final int INIT_SIZE = 30;
    private final int downInit;
    private final int height;
    private final BitMatrix image;
    private final int leftInit;
    private final int rightInit;
    private final int upInit;
    private final int width;

    public WhiteRectangleDetector(BitMatrix bitMatrix) throws NotFoundException {
        this.image = bitMatrix;
        int height = bitMatrix.getHeight();
        this.height = height;
        int width = bitMatrix.getWidth();
        this.width = width;
        int i = (width - 30) >> 1;
        this.leftInit = i;
        int i2 = (width + 30) >> 1;
        this.rightInit = i2;
        int i3 = (height - 30) >> 1;
        this.upInit = i3;
        int i4 = (height + 30) >> 1;
        this.downInit = i4;
        if (i3 < 0 || i < 0 || i4 >= height || i2 >= width) {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    public WhiteRectangleDetector(BitMatrix bitMatrix, int i, int i2, int i3) throws NotFoundException {
        this.image = bitMatrix;
        int height = bitMatrix.getHeight();
        this.height = height;
        int width = bitMatrix.getWidth();
        this.width = width;
        int i4 = i >> 1;
        int i5 = i2 - i4;
        this.leftInit = i5;
        int i6 = i2 + i4;
        this.rightInit = i6;
        int i7 = i3 - i4;
        this.upInit = i7;
        int i8 = i3 + i4;
        this.downInit = i8;
        if (i7 < 0 || i5 < 0 || i8 >= height || i6 >= width) {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    public ResultPoint[] detect() throws NotFoundException {
        int i = this.leftInit;
        int i2 = this.rightInit;
        int i3 = this.upInit;
        int i4 = this.downInit;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = true;
        while (z3) {
            boolean z4 = false;
            boolean z5 = true;
            while (z5 && i2 < this.width) {
                z5 = containsBlackPoint(i3, i4, i2, false);
                if (z5) {
                    i2++;
                    z4 = true;
                }
            }
            if (i2 < this.width) {
                boolean z6 = true;
                while (z6 && i4 < this.height) {
                    z6 = containsBlackPoint(i, i2, i4, true);
                    if (z6) {
                        i4++;
                        z4 = true;
                    }
                }
                if (i4 < this.height) {
                    boolean z7 = true;
                    while (z7 && i >= 0) {
                        z7 = containsBlackPoint(i3, i4, i, false);
                        if (z7) {
                            i--;
                            z4 = true;
                        }
                    }
                    if (i >= 0) {
                        z3 = z4;
                        boolean z8 = true;
                        while (z8 && i3 >= 0) {
                            z8 = containsBlackPoint(i, i2, i3, true);
                            if (z8) {
                                i3--;
                                z3 = true;
                            }
                        }
                        if (i3 >= 0) {
                            if (z3) {
                                z2 = true;
                            }
                        }
                    }
                }
            }
            z = true;
            break;
        }
        if (z || !z2) {
            throw NotFoundException.getNotFoundInstance();
        }
        int i5 = i2 - i;
        ResultPoint resultPoint = null;
        ResultPoint resultPoint2 = null;
        for (int i6 = 1; i6 < i5; i6++) {
            resultPoint2 = getBlackPointOnSegment(i, i4 - i6, i + i6, i4);
            if (resultPoint2 != null) {
                break;
            }
        }
        if (resultPoint2 == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        ResultPoint resultPoint3 = null;
        for (int i7 = 1; i7 < i5; i7++) {
            resultPoint3 = getBlackPointOnSegment(i, i3 + i7, i + i7, i3);
            if (resultPoint3 != null) {
                break;
            }
        }
        if (resultPoint3 == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        ResultPoint resultPoint4 = null;
        for (int i8 = 1; i8 < i5; i8++) {
            resultPoint4 = getBlackPointOnSegment(i2, i3 + i8, i2 - i8, i3);
            if (resultPoint4 != null) {
                break;
            }
        }
        if (resultPoint4 == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        for (int i9 = 1; i9 < i5; i9++) {
            resultPoint = getBlackPointOnSegment(i2, i4 - i9, i2 - i9, i4);
            if (resultPoint != null) {
                break;
            }
        }
        if (resultPoint == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        return centerEdges(resultPoint, resultPoint2, resultPoint4, resultPoint3);
    }

    private ResultPoint getBlackPointOnSegment(float f, float f2, float f3, float f4) {
        int round = MathUtils.round(MathUtils.distance(f, f2, f3, f4));
        float f5 = round;
        float f6 = (f3 - f) / f5;
        float f7 = (f4 - f2) / f5;
        for (int i = 0; i < round; i++) {
            float f8 = i;
            int round2 = MathUtils.round((f8 * f6) + f);
            int round3 = MathUtils.round((f8 * f7) + f2);
            if (this.image.get(round2, round3)) {
                return new ResultPoint(round2, round3);
            }
        }
        return null;
    }

    private ResultPoint[] centerEdges(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4) {
        float x = resultPoint.getX();
        float y = resultPoint.getY();
        float x2 = resultPoint2.getX();
        float y2 = resultPoint2.getY();
        float x3 = resultPoint3.getX();
        float y3 = resultPoint3.getY();
        float x4 = resultPoint4.getX();
        float y4 = resultPoint4.getY();
        return x < ((float) this.width) / 2.0f ? new ResultPoint[]{new ResultPoint(x4 - 1.0f, y4 + 1.0f), new ResultPoint(x2 + 1.0f, y2 + 1.0f), new ResultPoint(x3 - 1.0f, y3 - 1.0f), new ResultPoint(x + 1.0f, y - 1.0f)} : new ResultPoint[]{new ResultPoint(x4 + 1.0f, y4 + 1.0f), new ResultPoint(x2 + 1.0f, y2 - 1.0f), new ResultPoint(x3 - 1.0f, y3 + 1.0f), new ResultPoint(x - 1.0f, y - 1.0f)};
    }

    private boolean containsBlackPoint(int i, int i2, int i3, boolean z) {
        if (z) {
            while (i <= i2) {
                if (this.image.get(i, i3)) {
                    return true;
                }
                i++;
            }
            return false;
        }
        while (i <= i2) {
            if (this.image.get(i3, i)) {
                return true;
            }
            i++;
        }
        return false;
    }
}
