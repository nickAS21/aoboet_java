package com.dcloud.zxing.oned;

import java.util.Arrays;

/* loaded from: classes.dex */
public final class CodaBarWriter extends OneDimensionalCodeWriter {
    private static final char[] START_CHARS = {'A', 'B', 'C', 'D'};
    private static final char[] END_CHARS = {'T', 'N', '*', 'E'};

    @Override // com.dcloud.zxing.oned.OneDimensionalCodeWriter
    public boolean[] encode(String str) {
        int i;
        char[] cArr = START_CHARS;
        if (!CodaBarReader.arrayContains(cArr, Character.toUpperCase(str.charAt(0)))) {
            throw new IllegalArgumentException("Codabar should start with one of the following: " + Arrays.toString(cArr));
        }
        char[] cArr2 = END_CHARS;
        if (!CodaBarReader.arrayContains(cArr2, Character.toUpperCase(str.charAt(str.length() - 1)))) {
            throw new IllegalArgumentException("Codabar should end with one of the following: " + Arrays.toString(cArr2));
        }
        int i2 = 20;
        char[] cArr3 = {'/', ':', '+', '.'};
        for (int i3 = 1; i3 < str.length() - 1; i3++) {
            if (Character.isDigit(str.charAt(i3)) || str.charAt(i3) == '-' || str.charAt(i3) == '$') {
                i2 += 9;
            } else {
                if (!CodaBarReader.arrayContains(cArr3, str.charAt(i3))) {
                    throw new IllegalArgumentException("Cannot encode : '" + str.charAt(i3) + '\'');
                }
                i2 += 10;
            }
        }
        boolean[] zArr = new boolean[i2 + (str.length() - 1)];
        int i4 = 0;
        for (int i5 = 0; i5 < str.length(); i5++) {
            char upperCase = Character.toUpperCase(str.charAt(i5));
            if (i5 == str.length() - 1) {
                if (upperCase == '*') {
                    upperCase = 'C';
                } else if (upperCase == 'E') {
                    upperCase = 'D';
                } else if (upperCase == 'N') {
                    upperCase = 'B';
                } else if (upperCase == 'T') {
                    upperCase = 'A';
                }
            }
            int i6 = 0;
            while (true) {
                if (i6 >= CodaBarReader.ALPHABET.length) {
                    i = 0;
                    break;
                }
                if (upperCase == CodaBarReader.ALPHABET[i6]) {
                    i = CodaBarReader.CHARACTER_ENCODINGS[i6];
                    break;
                }
                i6++;
            }
            int i7 = 0;
            int i8 = 0;
            boolean z = true;
            while (i7 < 7) {
                zArr[i4] = z;
                i4++;
                if (((i >> (6 - i7)) & 1) == 0 || i8 == 1) {
                    z = !z;
                    i7++;
                    i8 = 0;
                } else {
                    i8++;
                }
            }
            if (i5 < str.length() - 1) {
                zArr[i4] = false;
                i4++;
            }
        }
        return zArr;
    }
}
