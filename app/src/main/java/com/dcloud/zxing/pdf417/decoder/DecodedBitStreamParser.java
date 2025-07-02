package com.dcloud.zxing.pdf417.decoder;

import com.dcloud.zxing.FormatException;
import com.dcloud.zxing.common.DecoderResult;
import com.dcloud.zxing.pdf417.PDF417ResultMetadata;

import java.math.BigInteger;
import java.util.Arrays;

/* loaded from: classes.dex */
final class DecodedBitStreamParser {
    private static final int AL = 28;
    private static final int AS = 27;
    private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
    private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
    private static final int BYTE_COMPACTION_MODE_LATCH = 901;
    private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
    private static final int LL = 27;
    private static final int MACRO_PDF417_TERMINATOR = 922;
    private static final int MAX_NUMERIC_CODEWORDS = 15;
    private static final int ML = 28;
    private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
    private static final int NUMBER_OF_SEQUENCE_CODEWORDS = 2;
    private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
    private static final int PAL = 29;
    private static final int PL = 25;
    private static final int PS = 29;
    private static final int TEXT_COMPACTION_MODE_LATCH = 900;
    private static final char[] PUNCT_CHARS = {';', '<', '>', '@', '[', '\\', '}', '_', '`', '~', '!', '\r', '\t', ',', ':', '\n', '-', '.', '$', '/', '\"', '|', '*', '(', ')', '?', '{', '}', '\''};
    private static final char[] MIXED_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '&', '\r', '\t', ',', ':', '#', '-', '.', '$', '/', '+', '%', '*', '=', '^'};

    private static final BigInteger[] EXP900 = new BigInteger[16];
    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum Mode {
        ALPHA,
        LOWER,
        MIXED,
        PUNCT,
        ALPHA_SHIFT,
        PUNCT_SHIFT
    }

    static {
        EXP900[0] = BigInteger.ONE;
        BigInteger nineHundred = BigInteger.valueOf(900L);
        for (int i = 1; i < EXP900.length; i++) {
            EXP900[i] = EXP900[i - 1].multiply(nineHundred);
        }
    }

    private DecodedBitStreamParser() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DecoderResult decode(int[] iArr, String str) throws FormatException {
        int byteCompaction;
        int i = 2;
        StringBuilder sb = new StringBuilder(iArr.length * 2);
        int i2 = iArr[1];
        PDF417ResultMetadata pDF417ResultMetadata = new PDF417ResultMetadata();
        while (i < iArr[0]) {
            if (i2 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                byteCompaction = byteCompaction(i2, iArr, i, sb);
            } else if (i2 == BYTE_COMPACTION_MODE_LATCH_6) {
                byteCompaction = byteCompaction(i2, iArr, i, sb);
            } else if (i2 != 928) {
                switch (i2) {
                    case TEXT_COMPACTION_MODE_LATCH /* 900 */:
                        byteCompaction = textCompaction(iArr, i, sb);
                        break;
                    case BYTE_COMPACTION_MODE_LATCH /* 901 */:
                        byteCompaction = byteCompaction(i2, iArr, i, sb);
                        break;
                    case NUMERIC_COMPACTION_MODE_LATCH /* 902 */:
                        byteCompaction = numericCompaction(iArr, i, sb);
                        break;
                    default:
                        byteCompaction = textCompaction(iArr, i - 1, sb);
                        break;
                }
            } else {
                byteCompaction = decodeMacroBlock(iArr, i, pDF417ResultMetadata);
            }
            if (byteCompaction < iArr.length) {
                i = byteCompaction + 1;
                i2 = iArr[byteCompaction];
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (sb.length() == 0) {
            throw FormatException.getFormatInstance();
        }
        DecoderResult decoderResult = new DecoderResult(null, sb.toString(), null, str);
        decoderResult.setOther(pDF417ResultMetadata);
        return decoderResult;
    }

    private static int decodeMacroBlock(int[] iArr, int i, PDF417ResultMetadata pDF417ResultMetadata) throws FormatException {
        if (i + 2 > iArr[0]) {
            throw FormatException.getFormatInstance();
        }
        int[] iArr2 = new int[2];
        int i2 = 0;
        while (i2 < 2) {
            iArr2[i2] = iArr[i];
            i2++;
            i++;
        }
        pDF417ResultMetadata.setSegmentIndex(Integer.parseInt(decodeBase900toBase10(iArr2, 2)));
        StringBuilder sb = new StringBuilder();
        int textCompaction = textCompaction(iArr, i, sb);
        pDF417ResultMetadata.setFileId(sb.toString());
        if (iArr[textCompaction] == BEGIN_MACRO_PDF417_OPTIONAL_FIELD) {
            int i3 = textCompaction + 1;
            int[] iArr3 = new int[iArr[0] - i3];
            boolean z = false;
            int i4 = 0;
            while (i3 < iArr[0] && !z) {
                int i5 = i3 + 1;
                int i6 = iArr[i3];
                if (i6 < TEXT_COMPACTION_MODE_LATCH) {
                    iArr3[i4] = i6;
                    i3 = i5;
                    i4++;
                } else if (i6 == MACRO_PDF417_TERMINATOR) {
                    pDF417ResultMetadata.setLastSegment(true);
                    z = true;
                    i3 = i5 + 1;
                } else {
                    throw FormatException.getFormatInstance();
                }
            }
            pDF417ResultMetadata.setOptionalData(Arrays.copyOf(iArr3, i4));
            return i3;
        }
        if (iArr[textCompaction] != MACRO_PDF417_TERMINATOR) {
            return textCompaction;
        }
        pDF417ResultMetadata.setLastSegment(true);
        return textCompaction + 1;
    }

    private static int textCompaction(int[] iArr, int i, StringBuilder sb) {
        int[] iArr2 = new int[(iArr[0] - i) << 1];
        int[] iArr3 = new int[(iArr[0] - i) << 1];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i4 < TEXT_COMPACTION_MODE_LATCH) {
                iArr2[i2] = i4 / 30;
                iArr2[i2 + 1] = i4 % 30;
                i2 += 2;
            } else if (i4 != MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                if (i4 != 928) {
                    switch (i4) {
                        case TEXT_COMPACTION_MODE_LATCH /* 900 */:
                            iArr2[i2] = TEXT_COMPACTION_MODE_LATCH;
                            i2++;
                            break;
                        case BYTE_COMPACTION_MODE_LATCH /* 901 */:
                        case NUMERIC_COMPACTION_MODE_LATCH /* 902 */:
                            break;
                        default:
                            switch (i4) {
                            }
                            break;
                    }
                }
                i3--;
                z = true;
            } else {
                iArr2[i2] = MODE_SHIFT_TO_BYTE_COMPACTION_MODE;
                i = i3 + 1;
                iArr3[i2] = iArr[i3];
                i2++;
            }
            i = i3;
        }
        decodeTextCompaction(iArr2, iArr3, i2, sb);
        return i;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0024. Please report as an issue. */
    private static void decodeTextCompaction(int[] iArr, int[] iArr2, int i, StringBuilder sb) {
        Mode mode;
        int i2;
        Mode mode2 = Mode.ALPHA;
        Mode mode3 = Mode.ALPHA;
        for (int i3 = 0; i3 < i; i3++) {
            int i4 = iArr[i3];
            char c = ' ';
            switch (AnonymousClass1.$SwitchMap$com$dcloud$zxing$pdf417$decoder$DecodedBitStreamParser$Mode[mode2.ordinal()]) {
                case 1:
                    if (i4 < 26) {
                        i2 = i4 + 65;
                        c = (char) i2;
                        break;
                    } else if (i4 != 26) {
                        if (i4 == 27) {
                            mode2 = Mode.LOWER;
                        } else if (i4 == 28) {
                            mode2 = Mode.MIXED;
                        } else if (i4 == 29) {
                            mode = Mode.PUNCT_SHIFT;
                            c = 0;
                            Mode mode4 = mode;
                            mode3 = mode2;
                            mode2 = mode4;
                            break;
                        } else if (i4 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                            sb.append((char) iArr2[i3]);
                        } else if (i4 == TEXT_COMPACTION_MODE_LATCH) {
                            mode2 = Mode.ALPHA;
                        }
                        c = 0;
                        break;
                    }
                    break;
                case 2:
                    if (i4 < 26) {
                        i2 = i4 + 97;
                        c = (char) i2;
                        break;
                    } else if (i4 != 26) {
                        if (i4 != 27) {
                            if (i4 == 28) {
                                mode2 = Mode.MIXED;
                            } else if (i4 == 29) {
                                mode = Mode.PUNCT_SHIFT;
                            } else if (i4 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                sb.append((char) iArr2[i3]);
                            } else if (i4 == TEXT_COMPACTION_MODE_LATCH) {
                                mode2 = Mode.ALPHA;
                            }
                            c = 0;
                            break;
                        } else {
                            mode = Mode.ALPHA_SHIFT;
                        }
                        c = 0;
                        Mode mode42 = mode;
                        mode3 = mode2;
                        mode2 = mode42;
                        break;
                    }
                    break;
                case 3:
                    if (i4 < 25) {
                        c = MIXED_CHARS[i4];
                        break;
                    } else {
                        if (i4 == 25) {
                            mode2 = Mode.PUNCT;
                        } else if (i4 != 26) {
                            if (i4 == 27) {
                                mode2 = Mode.LOWER;
                            } else if (i4 == 28) {
                                mode2 = Mode.ALPHA;
                            } else if (i4 == 29) {
                                mode = Mode.PUNCT_SHIFT;
                                c = 0;
                                Mode mode422 = mode;
                                mode3 = mode2;
                                mode2 = mode422;
                                break;
                            } else if (i4 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                sb.append((char) iArr2[i3]);
                            } else if (i4 == TEXT_COMPACTION_MODE_LATCH) {
                                mode2 = Mode.ALPHA;
                            }
                        }
                        c = 0;
                        break;
                    }
                case 4:
                    if (i4 < 29) {
                        c = PUNCT_CHARS[i4];
                        break;
                    } else {
                        if (i4 == 29) {
                            mode2 = Mode.ALPHA;
                        } else if (i4 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                            sb.append((char) iArr2[i3]);
                        } else if (i4 == TEXT_COMPACTION_MODE_LATCH) {
                            mode2 = Mode.ALPHA;
                        }
                        c = 0;
                        break;
                    }
                case 5:
                    if (i4 < 26) {
                        c = (char) (i4 + 65);
                    } else if (i4 != 26) {
                        if (i4 == TEXT_COMPACTION_MODE_LATCH) {
                            mode2 = Mode.ALPHA;
                            c = 0;
                            break;
                        }
                        mode2 = mode3;
                        c = 0;
                    }
                    mode2 = mode3;
                    break;
                case 6:
                    if (i4 < 29) {
                        c = PUNCT_CHARS[i4];
                        mode2 = mode3;
                        break;
                    } else {
                        if (i4 == 29) {
                            mode2 = Mode.ALPHA;
                        } else {
                            if (i4 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                sb.append((char) iArr2[i3]);
                            } else if (i4 == TEXT_COMPACTION_MODE_LATCH) {
                                mode2 = Mode.ALPHA;
                            }
                            mode2 = mode3;
                        }
                        c = 0;
                        break;
                    }
                default:
                    c = 0;
                    break;
            }
            if (c != 0) {
                sb.append(c);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.dcloud.zxing.pdf417.decoder.DecodedBitStreamParser$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$dcloud$zxing$pdf417$decoder$DecodedBitStreamParser$Mode;

        static {
            int[] iArr = new int[Mode.values().length];
            $SwitchMap$com$dcloud$zxing$pdf417$decoder$DecodedBitStreamParser$Mode = iArr;
            try {
                iArr[Mode.ALPHA.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$pdf417$decoder$DecodedBitStreamParser$Mode[Mode.LOWER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$pdf417$decoder$DecodedBitStreamParser$Mode[Mode.MIXED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$pdf417$decoder$DecodedBitStreamParser$Mode[Mode.PUNCT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$pdf417$decoder$DecodedBitStreamParser$Mode[Mode.ALPHA_SHIFT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$pdf417$decoder$DecodedBitStreamParser$Mode[Mode.PUNCT_SHIFT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private static int byteCompaction(int i, int[] iArr, int i2, StringBuilder sb) {
        int i3;
        int i4 = MACRO_PDF417_TERMINATOR;
        int i5 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
        long j = 900;
        int i6 = 6;
        if (i != BYTE_COMPACTION_MODE_LATCH) {
            if (i != BYTE_COMPACTION_MODE_LATCH_6) {
                return i2;
            }
            int i7 = i2;
            boolean z = false;
            int i8 = 0;
            long j2 = 0;
            while (i7 < iArr[0] && !z) {
                int i9 = i7 + 1;
                int i10 = iArr[i7];
                if (i10 < TEXT_COMPACTION_MODE_LATCH) {
                    i8++;
                    j2 = (j2 * 900) + i10;
                } else if (i10 == TEXT_COMPACTION_MODE_LATCH || i10 == BYTE_COMPACTION_MODE_LATCH || i10 == NUMERIC_COMPACTION_MODE_LATCH || i10 == BYTE_COMPACTION_MODE_LATCH_6 || i10 == 928 || i10 == i5 || i10 == i4) {
                    i7 = i9 - 1;
                    z = true;
                    if (i8 % 5 != 0 && i8 > 0) {
                        char[] cArr = new char[6];
                        for (int i11 = 0; i11 < 6; i11++) {
                            cArr[5 - i11] = (char) (j2 & 255);
                            j2 >>= 8;
                        }
                        sb.append(cArr);
                        i8 = 0;
                    }
                    i4 = MACRO_PDF417_TERMINATOR;
                    i5 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
                }
                i7 = i9;
                if (i8 % 5 != 0) {
                }
                i4 = MACRO_PDF417_TERMINATOR;
                i5 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
            }
            return i7;
        }
        char[] cArr2 = new char[6];
        int[] iArr2 = new int[6];
        int i12 = i2 + 1;
        boolean z2 = false;
        int i13 = 0;
        int i14 = iArr[i2];
        long j3 = 0;
        while (i12 < iArr[0] && !z2) {
            int i15 = i13 + 1;
            iArr2[i13] = i14;
            j3 = (j3 * j) + i14;
            int i16 = i12 + 1;
            i14 = iArr[i12];
            if (i14 == TEXT_COMPACTION_MODE_LATCH || i14 == BYTE_COMPACTION_MODE_LATCH || i14 == NUMERIC_COMPACTION_MODE_LATCH || i14 == BYTE_COMPACTION_MODE_LATCH_6 || i14 == 928 || i14 == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || i14 == MACRO_PDF417_TERMINATOR) {
                i12 = i16 - 1;
                i14 = i14;
                i13 = i15;
                j = 900;
                i6 = 6;
                z2 = true;
            } else {
                if (i15 % 5 != 0 || i15 <= 0) {
                    i14 = i14;
                    i13 = i15;
                    i12 = i16;
                } else {
                    int i17 = 0;
                    while (i17 < i6) {
                        cArr2[5 - i17] = (char) (j3 % 256);
                        j3 >>= 8;
                        i17++;
                        i14 = i14;
                        i6 = 6;
                    }
                    sb.append(cArr2);
                    i12 = i16;
                    i13 = 0;
                }
                j = 900;
                i6 = 6;
            }
        }
        if (i12 != iArr[0] || i14 >= TEXT_COMPACTION_MODE_LATCH) {
            i3 = i13;
        } else {
            i3 = i13 + 1;
            iArr2[i13] = i14;
        }
        for (int i18 = 0; i18 < i3; i18++) {
            sb.append((char) iArr2[i18]);
        }
        return i12;
    }

    private static int numericCompaction(int[] iArr, int i, StringBuilder sb) throws FormatException {
        int[] iArr2 = new int[15];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i3 == iArr[0]) {
                z = true;
            }
            if (i4 < TEXT_COMPACTION_MODE_LATCH) {
                iArr2[i2] = i4;
                i2++;
            } else if (i4 == TEXT_COMPACTION_MODE_LATCH || i4 == BYTE_COMPACTION_MODE_LATCH || i4 == BYTE_COMPACTION_MODE_LATCH_6 || i4 == 928 || i4 == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || i4 == MACRO_PDF417_TERMINATOR) {
                i3--;
                z = true;
            }
            if (i2 % 15 == 0 || i4 == NUMERIC_COMPACTION_MODE_LATCH || z) {
                sb.append(decodeBase900toBase10(iArr2, i2));
                i2 = 0;
            }
            i = i3;
        }
        return i;
    }

    private static String decodeBase900toBase10(int[] iArr, int i) throws FormatException {
        BigInteger bigInteger = BigInteger.ZERO;
        for (int i2 = 0; i2 < i; i2++) {
            bigInteger = bigInteger.add(EXP900[(i - i2) - 1].multiply(BigInteger.valueOf(iArr[i2])));
        }
        String bigInteger2 = bigInteger.toString();
        if (bigInteger2.charAt(0) != '1') {
            throw FormatException.getFormatInstance();
        }
        return bigInteger2.substring(1);
    }
}
