package com.dcloud.zxing.oned;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.EncodeHintType;
import com.dcloud.zxing.WriterException;
import com.dcloud.zxing.common.BitMatrix;

import java.util.Map;

/* loaded from: classes.dex */
public final class Code39Writer extends OneDimensionalCodeWriter {
    @Override // com.dcloud.zxing.oned.OneDimensionalCodeWriter, com.dcloud.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException {
        if (barcodeFormat != BarcodeFormat.CODE_39) {
            throw new IllegalArgumentException("Can only encode CODE_39, but got " + barcodeFormat);
        }
        return super.encode(str, barcodeFormat, i, i2, map);
    }

    @Override // com.dcloud.zxing.oned.OneDimensionalCodeWriter
    public boolean[] encode(String str) {
        int length = str.length();
        if (length > 80) {
            throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + length);
        }
        int[] iArr = new int[9];
        int i = length + 25;
        for (int i2 = 0; i2 < length; i2++) {
            toIntArray(Code39Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(str.charAt(i2))], iArr);
            for (int i3 = 0; i3 < 9; i3++) {
                i += iArr[i3];
            }
        }
        boolean[] zArr = new boolean[i];
        toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], iArr);
        int appendPattern = appendPattern(zArr, 0, iArr, true);
        int[] iArr2 = {1};
        int appendPattern2 = appendPattern + appendPattern(zArr, appendPattern, iArr2, false);
        for (int i4 = length - 1; i4 >= 0; i4--) {
            toIntArray(Code39Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(str.charAt(i4))], iArr);
            int appendPattern3 = appendPattern2 + appendPattern(zArr, appendPattern2, iArr, true);
            appendPattern2 = appendPattern3 + appendPattern(zArr, appendPattern3, iArr2, false);
        }
        toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], iArr);
        appendPattern(zArr, appendPattern2, iArr, true);
        return zArr;
    }

    private static void toIntArray(int i, int[] iArr) {
        for (int i2 = 0; i2 < 9; i2++) {
            int i3 = 1;
            if (((1 << i2) & i) != 0) {
                i3 = 2;
            }
            iArr[i2] = i3;
        }
    }
}
