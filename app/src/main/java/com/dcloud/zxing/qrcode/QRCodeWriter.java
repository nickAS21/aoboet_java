package com.dcloud.zxing.qrcode;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.EncodeHintType;
import com.dcloud.zxing.Writer;
import com.dcloud.zxing.WriterException;
import com.dcloud.zxing.common.BitMatrix;
import com.dcloud.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.dcloud.zxing.qrcode.encoder.ByteMatrix;
import com.dcloud.zxing.qrcode.encoder.Encoder;
import com.dcloud.zxing.qrcode.encoder.QRCode;

import java.util.Map;

/* loaded from: classes.dex */
public final class QRCodeWriter implements Writer {
    private static final int QUIET_ZONE_SIZE = 4;

    @Override // com.dcloud.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException {
        return encode(str, barcodeFormat, i, i2, null);
    }

    @Override // com.dcloud.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException {
        if (str.length() == 0) {
            throw new IllegalArgumentException("Found empty contents");
        }
        if (barcodeFormat != BarcodeFormat.QR_CODE) {
            throw new IllegalArgumentException("Can only encode QR_CODE, but got " + barcodeFormat);
        }
        if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("Requested dimensions are too small: " + i + 'x' + i2);
        }
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int i3 = 4;
        if (map != null) {
            ErrorCorrectionLevel errorCorrectionLevel2 = (ErrorCorrectionLevel) map.get(EncodeHintType.ERROR_CORRECTION);
            if (errorCorrectionLevel2 != null) {
                errorCorrectionLevel = errorCorrectionLevel2;
            }
            Integer num = (Integer) map.get(EncodeHintType.MARGIN);
            if (num != null) {
                i3 = num.intValue();
            }
        }
        return renderResult(Encoder.encode(str, errorCorrectionLevel, map), i, i2, i3);
    }

    private static BitMatrix renderResult(QRCode qRCode, int i, int i2, int i3) {
        ByteMatrix matrix = qRCode.getMatrix();
        if (matrix == null) {
            throw new IllegalStateException();
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int i4 = i3 << 1;
        int i5 = width + i4;
        int i6 = i4 + height;
        int max = Math.max(i, i5);
        int max2 = Math.max(i2, i6);
        int min = Math.min(max / i5, max2 / i6);
        int i7 = (max - (width * min)) / 2;
        int i8 = (max2 - (height * min)) / 2;
        BitMatrix bitMatrix = new BitMatrix(max, max2);
        int i9 = 0;
        while (i9 < height) {
            int i10 = 0;
            int i11 = i7;
            while (i10 < width) {
                if (matrix.get(i10, i9) == 1) {
                    bitMatrix.setRegion(i11, i8, min, min);
                }
                i10++;
                i11 += min;
            }
            i9++;
            i8 += min;
        }
        return bitMatrix;
    }
}
