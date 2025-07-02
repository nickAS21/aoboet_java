package com.dcloud.zxing.aztec;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.EncodeHintType;
import com.dcloud.zxing.Writer;
import com.dcloud.zxing.aztec.encoder.Encoder;
import com.dcloud.zxing.common.BitMatrix;

import java.nio.charset.Charset;
import java.util.Map;

/* loaded from: classes.dex */
public final class AztecWriter implements Writer {
    private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

    @Override // com.dcloud.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) {
        return encode(str, barcodeFormat, DEFAULT_CHARSET, 33);
    }

    @Override // com.dcloud.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) {
        String str2 = (String) map.get(EncodeHintType.CHARACTER_SET);
        Number number = (Number) map.get(EncodeHintType.ERROR_CORRECTION);
        return encode(str, barcodeFormat, str2 == null ? DEFAULT_CHARSET : Charset.forName(str2), number == null ? 33 : number.intValue());
    }

    private static BitMatrix encode(String str, BarcodeFormat barcodeFormat, Charset charset, int i) {
        if (barcodeFormat != BarcodeFormat.AZTEC) {
            throw new IllegalArgumentException("Can only encode AZTEC, but got " + barcodeFormat);
        }
        return Encoder.encode(str.getBytes(charset), i).getMatrix();
    }
}
