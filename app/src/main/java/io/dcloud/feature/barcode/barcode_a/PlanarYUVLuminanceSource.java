package io.dcloud.feature.barcode.barcode_a;

import android.graphics.Bitmap;

import com.dcloud.zxing.LuminanceSource;

import io.dcloud.common.DHInterface.IApp;

/* compiled from: PlanarYUVLuminanceSource.java */
/* loaded from: classes.dex old e */
public final class PlanarYUVLuminanceSource extends LuminanceSource {
    private final byte[] a;
    private final int b;
    private final int c;
    private final int d;
    private final int e;
    private LuminanceSource f;

    @Override // com.dcloud.zxing.LuminanceSource
    public boolean isCropSupported() {
        return true;
    }

    @Override // com.dcloud.zxing.LuminanceSource
    public boolean isRotateSupported() {
        return true;
    }

    public PlanarYUVLuminanceSource(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6) {
        super(i5, i6);
        this.f = null;
        if (i5 > i || i6 > i2) {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.width=" + i5 + ";dataWidth=" + i + ";height=" + i6 + ";dataHeight=" + i2);
        }
        this.a = bArr;
        this.b = i;
        this.c = i2;
        this.d = i3;
        this.e = i4;
    }

    @Override // com.dcloud.zxing.LuminanceSource
    public byte[] getRow(int i, byte[] bArr) {
        if (i < 0 || i >= getHeight()) {
            throw new IllegalArgumentException("Requested row is outside the image: " + i);
        }
        int width = getWidth();
        if (bArr == null || bArr.length < width) {
            bArr = new byte[width];
        }
        System.arraycopy(this.a, ((i + this.e) * this.b) + this.d, bArr, 0, width);
        return bArr;
    }

    @Override // com.dcloud.zxing.LuminanceSource
    public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();
        int i = this.b;
        if (width == i && height == this.c) {
            return this.a;
        }
        int i2 = width * height;
        byte[] bArr = new byte[i2];
        int i3 = (this.e * i) + this.d;
        if (width == i) {
            System.arraycopy(this.a, i3, bArr, 0, i2);
            return bArr;
        }
        byte[] bArr2 = this.a;
        for (int i4 = 0; i4 < height; i4++) {
            System.arraycopy(bArr2, i3, bArr, i4 * width, width);
            i3 += this.b;
        }
        return bArr;
    }

    @Override // com.dcloud.zxing.LuminanceSource
    public LuminanceSource rotateCounterClockwise() {
        if (this.f == null) {
            byte[] matrix = getMatrix();
            int height = getHeight();
            int width = getWidth();
            byte[] bArr = new byte[matrix.length];
            int i = (height - 1) * width;
            for (int i2 = 0; i2 < height; i2++) {
                int i3 = i2 * width;
                for (int i4 = 0; i4 < width; i4++) {
                    bArr[i3 + i4] = matrix[i - (i4 * width)];
                }
                i++;
            }
            this.f = new PlanarYUVLuminanceSource(bArr, width, height, 0, 0, width, height);
        }
        return this.f;
    }

    public Bitmap a(boolean z) {
        int width = getWidth();
        int height = getHeight();
        int[] iArr = new int[width * height];
        byte[] bArr = this.a;
        if (z) {
            int i = ((this.e + height) * this.b) + this.d;
            for (int i2 = 0; i2 < height; i2++) {
                int i3 = i2 * width;
                for (int i4 = 0; i4 < width; i4++) {
                    iArr[i3 + i4] = ((bArr[i - (this.b * i4)] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) * 65793) | (-16777216);
                }
                i++;
            }
        } else {
            int i5 = (this.e * this.b) + this.d;
            for (int i6 = 0; i6 < height; i6++) {
                int i7 = i6 * width;
                for (int i8 = 0; i8 < width; i8++) {
                    iArr[i7 + i8] = ((bArr[i5 + i8] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) * 65793) | (-16777216);
                }
                i5 += this.b;
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }
}
