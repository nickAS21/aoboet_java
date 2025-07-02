package io.dcloud.feature.barcode.barcode_b;

import android.graphics.Bitmap;

import com.dcloud.zxing.LuminanceSource;

/* compiled from: BitmapLuminanceSource.java */
/* loaded from: classes.dex old a */
public class BitmapLuminanceSource extends LuminanceSource {
    private byte[] a;

    /* JADX INFO: Access modifiers changed from: protected */
    public BitmapLuminanceSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());
        int width = bitmap.getWidth() * bitmap.getHeight();
        int[] iArr = new int[width];
        this.a = new byte[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(iArr, 0, getWidth(), 0, 0, getWidth(), getHeight());
        for (int i = 0; i < width; i++) {
            this.a[i] = (byte) iArr[i];
        }
    }

    @Override // com.dcloud.zxing.LuminanceSource
    public byte[] getMatrix() {
        return this.a;
    }

    @Override // com.dcloud.zxing.LuminanceSource
    public byte[] getRow(int i, byte[] bArr) {
        System.arraycopy(this.a, i * getWidth(), bArr, 0, getWidth());
        return bArr;
    }
}
