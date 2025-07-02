package com.dcloud.zxing.oned;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.EncodeHintType;
import com.dcloud.zxing.WriterException;
import com.dcloud.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public final class Code128Writer extends OneDimensionalCodeWriter {
    private static final int CODE_CODE_B = 100;
    private static final int CODE_CODE_C = 99;
    private static final int CODE_FNC_1 = 102;
    private static final int CODE_FNC_2 = 97;
    private static final int CODE_FNC_3 = 96;
    private static final int CODE_FNC_4_B = 100;
    private static final int CODE_START_B = 104;
    private static final int CODE_START_C = 105;
    private static final int CODE_STOP = 106;
    private static final char ESCAPE_FNC_1 = 241;
    private static final char ESCAPE_FNC_2 = 242;
    private static final char ESCAPE_FNC_3 = 243;
    private static final char ESCAPE_FNC_4 = 244;

    @Override // com.dcloud.zxing.oned.OneDimensionalCodeWriter, com.dcloud.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException {
        if (barcodeFormat != BarcodeFormat.CODE_128) {
            throw new IllegalArgumentException("Can only encode CODE_128, but got " + barcodeFormat);
        }
        return super.encode(str, barcodeFormat, i, i2, map);
    }

    @Override // com.dcloud.zxing.oned.OneDimensionalCodeWriter
    public boolean[] encode(String str) {
        int length = str.length();
        if (length < 1 || length > 80) {
            throw new IllegalArgumentException("Contents length should be between 1 and 80 characters, but got " + length);
        }
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt < ' ' || charAt > '~') {
                switch (charAt) {
                    case 241:
                    case 242:
                    case 243:
                    case 244:
                        break;
                    default:
                        throw new IllegalArgumentException("Bad character in input: " + charAt);
                }
            }
        }
        ArrayList<int[]> arrayList = new ArrayList();
        int i3 = 1;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i4 < length) {
            int i7 = CODE_CODE_C;
            int i8 = 100;
            if (!isDigits(str, i4, i6 == CODE_CODE_C ? 2 : 4)) {
                i7 = 100;
            }
            if (i7 == i6) {
                if (i6 != 100) {
                    switch (str.charAt(i4)) {
                        case 241:
                            i8 = CODE_FNC_1;
                            break;
                        case 242:
                            i8 = CODE_FNC_2;
                            break;
                        case 243:
                            i8 = CODE_FNC_3;
                            break;
                        case 244:
                            break;
                        default:
                            int i9 = i4 + 2;
                            i8 = Integer.parseInt(str.substring(i4, i9));
                            i4 = i9;
                            break;
                    }
                } else {
                    i8 = str.charAt(i4) - ' ';
                }
                i4++;
            } else {
                i8 = i6 == 0 ? i7 == 100 ? CODE_START_B : CODE_START_C : i7;
                i6 = i7;
            }
            arrayList.add(Code128Reader.CODE_PATTERNS[i8]);
            i5 += i8 * i3;
            if (i4 != 0) {
                i3++;
            }
        }
        arrayList.add(Code128Reader.CODE_PATTERNS[i5 % 103]);
        arrayList.add(Code128Reader.CODE_PATTERNS[CODE_STOP]);
        int i10 = 0;
        for (int[] iArr : arrayList) {
            for (int i11 : iArr) {
                i10 += i11;
            }
        }
        boolean[] zArr = new boolean[i10];
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            i += appendPattern(zArr, i, (int[]) it.next(), true);
        }
        return zArr;
    }

    private static boolean isDigits(CharSequence charSequence, int i, int i2) {
        int i3 = i2 + i;
        int length = charSequence.length();
        while (i < i3 && i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt < '0' || charAt > '9') {
                if (charAt != 241) {
                    return false;
                }
                i3++;
            }
            i++;
        }
        return i3 <= length;
    }
}
