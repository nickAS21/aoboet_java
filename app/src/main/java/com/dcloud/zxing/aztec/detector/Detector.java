package com.dcloud.zxing.aztec.detector;

import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.ResultPoint;
import com.dcloud.zxing.aztec.AztecDetectorResult;
import com.dcloud.zxing.common.BitMatrix;
import com.dcloud.zxing.common.GridSampler;
import com.dcloud.zxing.common.detector.MathUtils;
import com.dcloud.zxing.common.detector.WhiteRectangleDetector;
import com.dcloud.zxing.common.reedsolomon.GenericGF;
import com.dcloud.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.dcloud.zxing.common.reedsolomon.ReedSolomonException;

/* loaded from: classes.dex */
public final class Detector {
    private boolean compact;
    private final BitMatrix image;
    private int nbCenterLayers;
    private int nbDataBlocks;
    private int nbLayers;
    private int shift;

    public Detector(BitMatrix bitMatrix) {
        this.image = bitMatrix;
    }

    public AztecDetectorResult detect() {
        Point[] bullEyeCornerPoints = null;
        try {
            bullEyeCornerPoints = getBullEyeCornerPoints(getMatrixCenter());
            extractParameters(bullEyeCornerPoints);
            ResultPoint[] matrixCornerPoints = getMatrixCornerPoints(bullEyeCornerPoints);
            BitMatrix bitMatrix = this.image;
            int i = this.shift;
            return new AztecDetectorResult(sampleGrid(bitMatrix, matrixCornerPoints[i % 4], matrixCornerPoints[(i + 3) % 4], matrixCornerPoints[(i + 2) % 4], matrixCornerPoints[(i + 1) % 4]), matrixCornerPoints, this.compact, this.nbDataBlocks, this.nbLayers);

        } catch (NotFoundException | ReedSolomonException e) {
            throw new RuntimeException(e);
        }
    }

    private void extractParameters(Point[] pointArr) throws NotFoundException, ReedSolomonException {
        boolean[] zArr;
        int i = this.nbCenterLayers * 2;
        int i2 = 0;
        int i3 = i + 1;
        boolean[] sampleLine = sampleLine(pointArr[0], pointArr[1], i3);
        boolean[] sampleLine2 = sampleLine(pointArr[1], pointArr[2], i3);
        boolean[] sampleLine3 = sampleLine(pointArr[2], pointArr[3], i3);
        boolean[] sampleLine4 = sampleLine(pointArr[3], pointArr[0], i3);
        if (sampleLine[0] && sampleLine[i]) {
            this.shift = 0;
        } else if (sampleLine2[0] && sampleLine2[i]) {
            this.shift = 1;
        } else if (sampleLine3[0] && sampleLine3[i]) {
            this.shift = 2;
        } else if (sampleLine4[0] && sampleLine4[i]) {
            this.shift = 3;
        } else {
            throw NotFoundException.getNotFoundInstance();
        }
        if (this.compact) {
            boolean[] zArr2 = new boolean[28];
            for (int i4 = 0; i4 < 7; i4++) {
                int i5 = i4 + 2;
                zArr2[i4] = sampleLine[i5];
                zArr2[i4 + 7] = sampleLine2[i5];
                zArr2[i4 + 14] = sampleLine3[i5];
                zArr2[i4 + 21] = sampleLine4[i5];
            }
            zArr = new boolean[28];
            while (i2 < 28) {
                zArr[i2] = zArr2[((this.shift * 7) + i2) % 28];
                i2++;
            }
        } else {
            boolean[] zArr3 = new boolean[40];
            for (int i6 = 0; i6 < 11; i6++) {
                if (i6 < 5) {
                    int i7 = i6 + 2;
                    zArr3[i6] = sampleLine[i7];
                    zArr3[i6 + 10] = sampleLine2[i7];
                    zArr3[i6 + 20] = sampleLine3[i7];
                    zArr3[i6 + 30] = sampleLine4[i7];
                }
                if (i6 > 5) {
                    int i8 = i6 + 2;
                    zArr3[i6 - 1] = sampleLine[i8];
                    zArr3[i6 + 9] = sampleLine2[i8];
                    zArr3[i6 + 19] = sampleLine3[i8];
                    zArr3[i6 + 29] = sampleLine4[i8];
                }
            }
            zArr = new boolean[40];
            while (i2 < 40) {
                zArr[i2] = zArr3[((this.shift * 10) + i2) % 40];
                i2++;
            }
        }
        correctParameterData(zArr, this.compact);
        getParameters(zArr);
    }

    private ResultPoint[] getMatrixCornerPoints(Point[] pointArr) throws NotFoundException {
        float f = (((this.nbLayers * 2) + (this.nbLayers > 4 ? 1 : 0)) + ((this.nbLayers - 4) / 8)) / (this.nbCenterLayers * 2.0f);
        int x = pointArr[0].getX() - pointArr[2].getX();
        int i = x + (x > 0 ? 1 : -1);
        int y = pointArr[0].getY() - pointArr[2].getY();
        int i2 = y + (y > 0 ? 1 : -1);
        float f2 = i * f;
        int round = MathUtils.round(pointArr[2].getX() - f2);
        float f3 = i2 * f;
        int round2 = MathUtils.round(pointArr[2].getY() - f3);
        int round3 = MathUtils.round(pointArr[0].getX() + f2);
        int round4 = MathUtils.round(pointArr[0].getY() + f3);
        int x2 = pointArr[1].getX() - pointArr[3].getX();
        int i3 = x2 + (x2 > 0 ? 1 : -1);
        int y2 = pointArr[1].getY() - pointArr[3].getY();
        int i4 = y2 + (y2 > 0 ? 1 : -1);
        float f4 = i3 * f;
        int round5 = MathUtils.round(pointArr[3].getX() - f4);
        float f5 = f * i4;
        int round6 = MathUtils.round(pointArr[3].getY() - f5);
        int round7 = MathUtils.round(pointArr[1].getX() + f4);
        int round8 = MathUtils.round(pointArr[1].getY() + f5);
        if (isValid(round3, round4) && isValid(round7, round8) && isValid(round, round2) && isValid(round5, round6)) {
            return new ResultPoint[]{new ResultPoint(round3, round4), new ResultPoint(round7, round8), new ResultPoint(round, round2), new ResultPoint(round5, round6)};
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static void correctParameterData(boolean[] zArr, boolean z) throws NotFoundException, ReedSolomonException {
        int i;
        int i2;
        if (z) {
            i = 7;
            i2 = 2;
        } else {
            i = 10;
            i2 = 4;
        }
        int i3 = i - i2;
        int[] iArr = new int[i];
        int i4 = 0;
        while (true) {
            if (i4 >= i) {
                break;
            }
            int i5 = 1;
            for (int i6 = 1; i6 <= 4; i6++) {
                if (zArr[((4 * i4) + 4) - i6]) {
                    iArr[i4] = iArr[i4] + i5;
                }
                i5 <<= 1;
            }
            i4++;
        }
        new ReedSolomonDecoder(GenericGF.AZTEC_PARAM).decode(iArr, i3);
        for (int i7 = 0; i7 < i2; i7++) {
            int i8 = 1;
            for (int i9 = 1; i9 <= 4; i9++) {
                zArr[((i7 * 4) + 4) - i9] = (iArr[i7] & i8) == i8;
                i8 <<= 1;
            }
        }
    }

    private Point[] getBullEyeCornerPoints(Point point) throws NotFoundException {
        this.nbCenterLayers = 1;
        Point point2 = point;
        Point point3 = point2;
        Point point4 = point3;
        boolean z = true;
        while (this.nbCenterLayers < 9) {
            Point firstDifferent = getFirstDifferent(point, z, 1, -1);
            Point firstDifferent2 = getFirstDifferent(point2, z, 1, 1);
            Point firstDifferent3 = getFirstDifferent(point3, z, -1, 1);
            Point firstDifferent4 = getFirstDifferent(point4, z, -1, -1);
            if (this.nbCenterLayers > 2) {
                double distance = (distance(firstDifferent4, firstDifferent) * this.nbCenterLayers) / (distance(point4, point) * (this.nbCenterLayers + 2));
                if (distance < 0.75d || distance > 1.25d || !isWhiteOrBlackRectangle(firstDifferent, firstDifferent2, firstDifferent3, firstDifferent4)) {
                    break;
                }
            }
            z = !z;
            this.nbCenterLayers++;
            point4 = firstDifferent4;
            point = firstDifferent;
            point2 = firstDifferent2;
            point3 = firstDifferent3;
        }
        int i = this.nbCenterLayers;
        if (i != 5 && i != 7) {
            throw NotFoundException.getNotFoundInstance();
        }
        this.compact = i == 5;
        float f = 1.5f / ((i * 2) - 3);
        int x = point.getX() - point3.getX();
        int y = point.getY() - point3.getY();
        float f2 = x * f;
        int round = MathUtils.round(point3.getX() - f2);
        float f3 = y * f;
        int round2 = MathUtils.round(point3.getY() - f3);
        int round3 = MathUtils.round(point.getX() + f2);
        int round4 = MathUtils.round(point.getY() + f3);
        int x2 = point2.getX() - point4.getX();
        int y2 = point2.getY() - point4.getY();
        float f4 = x2 * f;
        int round5 = MathUtils.round(point4.getX() - f4);
        float f5 = f * y2;
        int round6 = MathUtils.round(point4.getY() - f5);
        int round7 = MathUtils.round(point2.getX() + f4);
        int round8 = MathUtils.round(point2.getY() + f5);
        if (isValid(round3, round4) && isValid(round7, round8) && isValid(round, round2) && isValid(round5, round6)) {
            return new Point[]{new Point(round3, round4), new Point(round7, round8), new Point(round, round2), new Point(round5, round6)};
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private Point getMatrixCenter() {
        ResultPoint resultPoint;
        ResultPoint resultPoint2;
        ResultPoint resultPoint3;
        ResultPoint resultPoint4;
        ResultPoint resultPoint5;
        ResultPoint resultPoint6;
        ResultPoint resultPoint7;
        ResultPoint resultPoint8;
        try {
            ResultPoint[] detect = new WhiteRectangleDetector(this.image).detect();
            resultPoint3 = detect[0];
            resultPoint4 = detect[1];
            resultPoint2 = detect[2];
            resultPoint = detect[3];
        } catch (NotFoundException unused) {
            int width = this.image.getWidth() / 2;
            int height = this.image.getHeight() / 2;
            int i = width + 7;
            int i2 = height - 7;
            ResultPoint resultPoint9 = getFirstDifferent(new Point(i, i2), false, 1, -1).toResultPoint();
            int i3 = height + 7;
            ResultPoint resultPoint10 = getFirstDifferent(new Point(i, i3), false, 1, 1).toResultPoint();
            int i4 = width - 7;
            ResultPoint resultPoint11 = getFirstDifferent(new Point(i4, i3), false, -1, 1).toResultPoint();
            resultPoint = getFirstDifferent(new Point(i4, i2), false, -1, -1).toResultPoint();
            resultPoint2 = resultPoint11;
            resultPoint3 = resultPoint9;
            resultPoint4 = resultPoint10;
        }
        int round = MathUtils.round((((resultPoint3.getX() + resultPoint.getX()) + resultPoint4.getX()) + resultPoint2.getX()) / 4.0f);
        int round2 = MathUtils.round((((resultPoint3.getY() + resultPoint.getY()) + resultPoint4.getY()) + resultPoint2.getY()) / 4.0f);
        try {
            ResultPoint[] detect2 = new WhiteRectangleDetector(this.image, 15, round, round2).detect();
            resultPoint5 = detect2[0];
            resultPoint6 = detect2[1];
            resultPoint7 = detect2[2];
            resultPoint8 = detect2[3];
        } catch (NotFoundException unused2) {
            int i5 = round + 7;
            int i6 = round2 - 7;
            resultPoint5 = getFirstDifferent(new Point(i5, i6), false, 1, -1).toResultPoint();
            int i7 = round2 + 7;
            resultPoint6 = getFirstDifferent(new Point(i5, i7), false, 1, 1).toResultPoint();
            int i8 = round - 7;
            resultPoint7 = getFirstDifferent(new Point(i8, i7), false, -1, 1).toResultPoint();
            resultPoint8 = getFirstDifferent(new Point(i8, i6), false, -1, -1).toResultPoint();
        }
        return new Point(MathUtils.round((((resultPoint5.getX() + resultPoint8.getX()) + resultPoint6.getX()) + resultPoint7.getX()) / 4.0f), MathUtils.round((((resultPoint5.getY() + resultPoint8.getY()) + resultPoint6.getY()) + resultPoint7.getY()) / 4.0f));
    }

    private BitMatrix sampleGrid(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4) throws NotFoundException {
        int i;
        if (this.compact) {
            i = (this.nbLayers * 4) + 11;
        } else {
            int i2 = this.nbLayers;
            i = i2 <= 4 ? (i2 * 4) + 15 : (i2 * 4) + ((((i2 - 4) / 8) + 1) * 2) + 15;
        }
        int i3 = i;
        float f = i3 - 0.5f;
        return GridSampler.getInstance().sampleGrid(bitMatrix, i3, i3, 0.5f, 0.5f, f, 0.5f, f, f, 0.5f, f, resultPoint.getX(), resultPoint.getY(), resultPoint4.getX(), resultPoint4.getY(), resultPoint3.getX(), resultPoint3.getY(), resultPoint2.getX(), resultPoint2.getY());
    }

    private void getParameters(boolean[] zArr) {
        int i;
        int i2;
        if (this.compact) {
            i = 2;
            i2 = 6;
        } else {
            i = 5;
            i2 = 11;
        }
        for (int i3 = 0; i3 < i; i3++) {
            int i4 = this.nbLayers << 1;
            this.nbLayers = i4;
            if (zArr[i3]) {
                this.nbLayers = i4 + 1;
            }
        }
        for (int i5 = i; i5 < i + i2; i5++) {
            int i6 = this.nbDataBlocks << 1;
            this.nbDataBlocks = i6;
            if (zArr[i5]) {
                this.nbDataBlocks = i6 + 1;
            }
        }
        this.nbLayers++;
        this.nbDataBlocks++;
    }

    private boolean[] sampleLine(Point point, Point point2, int i) {
        boolean[] zArr = new boolean[i];
        float distance = distance(point, point2);
        float f = distance / (i - 1);
        float x = ((point2.getX() - point.getX()) * f) / distance;
        float y = (f * (point2.getY() - point.getY())) / distance;
        float x2 = point.getX();
        float y2 = point.getY();
        for (int i2 = 0; i2 < i; i2++) {
            zArr[i2] = this.image.get(MathUtils.round(x2), MathUtils.round(y2));
            x2 += x;
            y2 += y;
        }
        return zArr;
    }

    private boolean isWhiteOrBlackRectangle(Point point, Point point2, Point point3, Point point4) {
        Point point5 = new Point(point.getX() - 3, point.getY() + 3);
        Point point6 = new Point(point2.getX() - 3, point2.getY() - 3);
        Point point7 = new Point(point3.getX() + 3, point3.getY() - 3);
        Point point8 = new Point(point4.getX() + 3, point4.getY() + 3);
        int color = getColor(point8, point5);
        return color != 0 && getColor(point5, point6) == color && getColor(point6, point7) == color && getColor(point7, point8) == color;
    }

    private int getColor(Point point, Point point2) {
        float distance = distance(point, point2);
        float x = (point2.getX() - point.getX()) / distance;
        float y = (point2.getY() - point.getY()) / distance;
        float x2 = point.getX();
        float y2 = point.getY();
        boolean z = this.image.get(point.getX(), point.getY());
        int i = 0;
        for (int i2 = 0; i2 < distance; i2++) {
            x2 += x;
            y2 += y;
            if (this.image.get(MathUtils.round(x2), MathUtils.round(y2)) != z) {
                i++;
            }
        }
        float f = i / distance;
        if (f <= 0.1f || f >= 0.9f) {
            return (f <= 0.1f) == z ? 1 : -1;
        }
        return 0;
    }

    private Point getFirstDifferent(Point point, boolean z, int i, int i2) {
        int x = point.getX() + i;
        int y = point.getY();
        while (true) {
            y += i2;
            if (!isValid(x, y) || this.image.get(x, y) != z) {
                break;
            }
            x += i;
        }
        int i3 = x - i;
        int i4 = y - i2;
        while (isValid(i3, i4) && this.image.get(i3, i4) == z) {
            i3 += i;
        }
        int i5 = i3 - i;
        while (isValid(i5, i4) && this.image.get(i5, i4) == z) {
            i4 += i2;
        }
        return new Point(i5, i4 - i2);
    }

    private boolean isValid(int i, int i2) {
        return i >= 0 && i < this.image.getWidth() && i2 > 0 && i2 < this.image.getHeight();
    }

    private static float distance(Point point, Point point2) {
        return MathUtils.distance(point.getX(), point.getY(), point2.getX(), point2.getY());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Point {
        private final int x;
        private final int y;

        ResultPoint toResultPoint() {
            return new ResultPoint(getX(), getY());
        }

        Point(int i, int i2) {
            this.x = i;
            this.y = i2;
        }

        int getX() {
            return this.x;
        }

        int getY() {
            return this.y;
        }
    }
}
