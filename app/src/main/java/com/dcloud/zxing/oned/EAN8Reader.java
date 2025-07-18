package com.dcloud.zxing.oned;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.common.BitArray;

/* loaded from: classes.dex */
public final class EAN8Reader extends UPCEANReader {
    private final int[] decodeMiddleCounters = new int[4];

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.dcloud.zxing.oned.UPCEANReader
    public int decodeMiddle(BitArray bitArray, int[] iArr, StringBuilder sb) throws NotFoundException {
        int[] iArr2 = this.decodeMiddleCounters;
        iArr2[0] = 0;
        iArr2[1] = 0;
        iArr2[2] = 0;
        iArr2[3] = 0;
        int size = bitArray.getSize();
        int i = iArr[1];
        for (int i2 = 0; i2 < 4 && i < size; i2++) {
            sb.append((char) (decodeDigit(bitArray, iArr2, i, L_PATTERNS) + 48));
            for (int i3 : iArr2) {
                i += i3;
            }
        }
        int i4 = findGuardPattern(bitArray, i, true, MIDDLE_PATTERN)[1];
        for (int i5 = 0; i5 < 4 && i4 < size; i5++) {
            sb.append((char) (decodeDigit(bitArray, iArr2, i4, L_PATTERNS) + 48));
            for (int i6 : iArr2) {
                i4 += i6;
            }
        }
        return i4;
    }

    @Override // com.dcloud.zxing.oned.UPCEANReader
    BarcodeFormat getBarcodeFormat() {
        return BarcodeFormat.EAN_8;
    }
}
