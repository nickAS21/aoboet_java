package com.dcloud.zxing.aztec.decoder;

import androidx.core.view.PointerIconCompat;

import com.dcloud.zxing.FormatException;
import com.dcloud.zxing.aztec.AztecDetectorResult;
import com.dcloud.zxing.common.BitMatrix;
import com.dcloud.zxing.common.DecoderResult;

import io.dcloud.common.util.JSUtil;
import io.dcloud.feature.ui.nativeui.WaitingView;

/* loaded from: classes.dex */
public final class Decoder {
    private int codewordSize;
    private AztecDetectorResult ddata;
    private int invertedBitCount;
    private int numCodewords;
    private static final int[] NB_BITS_COMPACT = {0, 104, 240, 408, 608};
    private static final int[] NB_BITS = {0, 128, 288, 480, 704, 960, 1248, 1568, 1920, 2304, 2720, 3168, 3648, 4160, 4704, 5280, 5888, 6528, 7200, 7904, 8640, 9408, 10208, 11040, 11904, 12800, 13728, 14688, 15680, 16704, 17760, 18848, 19968};
    private static final int[] NB_DATABLOCK_COMPACT = {0, 17, 40, 51, 76};
    private static final int[] NB_DATABLOCK = {0, 21, 48, 60, 88, 120, 156, 196, 240, 230, 272, 316, 364, 416, 470, 528, 588, 652, 720, 790, 864, 940, PointerIconCompat.TYPE_GRAB, 920, 992, 1066, 1144, 1224, 1306, 1392, 1480, 1570, 1664};
    private static final String[] UPPER_TABLE = {"CTRL_PS", " ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "CTRL_LL", "CTRL_ML", "CTRL_DL", "CTRL_BS"};
    private static final String[] LOWER_TABLE = {"CTRL_PS", " ", "a", WaitingView.a, "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "CTRL_US", "CTRL_ML", "CTRL_DL", "CTRL_BS"};
    private static final String[] MIXED_TABLE = {"CTRL_PS", " ", "\u0001", "\u0002", "\u0003", "\u0004", "\u0005", "\u0006", "\u0007", "\b", "\t", "\n", "\u000b", "\f", "\r", "\u001b", "\u001c", "\u001d", "\u001e", "\u001f", "@", "\\", "^", "_", "`", "|", "~", "\u007f", "CTRL_LL", "CTRL_UL", "CTRL_PL", "CTRL_BS"};
    private static final String[] PUNCT_TABLE = {"", "\r", "\r\n", ". ", ", ", ": ", "!", JSUtil.QUOTE, "#", "$", "%", "&", "'", "(", ")", "*", "+", JSUtil.COMMA, "-", ".", "/", ":", ";", "<", "=", ">", "?", "[", "]", "{", "}", "CTRL_UL"};
    private static final String[] DIGIT_TABLE = {"CTRL_PS", " ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", JSUtil.COMMA, ".", "CTRL_UL", "CTRL_US"};

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum Table {
        UPPER,
        LOWER,
        MIXED,
        DIGIT,
        PUNCT,
        BINARY
    }

    public DecoderResult decode(AztecDetectorResult aztecDetectorResult) throws FormatException {
        this.ddata = aztecDetectorResult;
        BitMatrix bits = aztecDetectorResult.getBits();
        if (!this.ddata.isCompact()) {
            bits = removeDashedLines(this.ddata.getBits());
        }
        return new DecoderResult(null, getEncodedData(correctBits(extractBits(bits))), null, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:? A[LOOP:1: B:5:0x0023->B:34:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String getEncodedData(boolean[] r17) throws com.dcloud.zxing.FormatException {
        /*
            Method dump skipped, instructions count: 200
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing.aztec.decoder.Decoder.getEncodedData(boolean[]):java.lang.String");
    }

    private static Table getTable(char c) {
        if (c == 'B') {
            return Table.BINARY;
        }
        if (c == 'D') {
            return Table.DIGIT;
        }
        if (c == 'P') {
            return Table.PUNCT;
        }
        if (c == 'L') {
            return Table.LOWER;
        }
        if (c == 'M') {
            return Table.MIXED;
        }
        return Table.UPPER;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.dcloud.zxing.aztec.decoder.Decoder$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$dcloud$zxing$aztec$decoder$Decoder$Table;

        static {
            int[] iArr = new int[Table.values().length];
            $SwitchMap$com$dcloud$zxing$aztec$decoder$Decoder$Table = iArr;
            try {
                iArr[Table.UPPER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$aztec$decoder$Decoder$Table[Table.LOWER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$aztec$decoder$Decoder$Table[Table.MIXED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$aztec$decoder$Decoder$Table[Table.PUNCT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$aztec$decoder$Decoder$Table[Table.DIGIT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    private static String getCharacter(Table table, int i) {
        int i2 = AnonymousClass1.$SwitchMap$com$dcloud$zxing$aztec$decoder$Decoder$Table[table.ordinal()];
        if (i2 == 1) {
            return UPPER_TABLE[i];
        }
        if (i2 == 2) {
            return LOWER_TABLE[i];
        }
        if (i2 == 3) {
            return MIXED_TABLE[i];
        }
        if (i2 != 4) {
            return i2 != 5 ? "" : DIGIT_TABLE[i];
        }
        return PUNCT_TABLE[i];
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x00ec, code lost:
    
        r0 = r0 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean[] correctBits(boolean[] r14) throws com.dcloud.zxing.FormatException {
        /*
            Method dump skipped, instructions count: 245
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing.aztec.decoder.Decoder.correctBits(boolean[]):boolean[]");
    }

    private boolean[] extractBits(BitMatrix bitMatrix) throws FormatException {
        boolean[] zArr;
        int i;
        if (this.ddata.isCompact()) {
            int nbLayers = this.ddata.getNbLayers();
            int[] iArr = NB_BITS_COMPACT;
            if (nbLayers > iArr.length) {
                throw FormatException.getFormatInstance();
            }
            zArr = new boolean[iArr[this.ddata.getNbLayers()]];
            this.numCodewords = NB_DATABLOCK_COMPACT[this.ddata.getNbLayers()];
        } else {
            int nbLayers2 = this.ddata.getNbLayers();
            int[] iArr2 = NB_BITS;
            if (nbLayers2 > iArr2.length) {
                throw FormatException.getFormatInstance();
            }
            zArr = new boolean[iArr2[this.ddata.getNbLayers()]];
            this.numCodewords = NB_DATABLOCK[this.ddata.getNbLayers()];
        }
        int nbLayers3 = this.ddata.getNbLayers();
        int height = bitMatrix.getHeight();
        int i2 = 0;
        int i3 = 0;
        while (nbLayers3 != 0) {
            int i4 = 0;
            int i5 = 0;
            while (true) {
                i = height * 2;
                if (i4 >= i - 4) {
                    break;
                }
                int i6 = (i4 / 2) + i3;
                zArr[i2 + i4] = bitMatrix.get(i3 + i5, i6);
                zArr[((i + i2) - 4) + i4] = bitMatrix.get(i6, ((i3 + height) - 1) - i5);
                i5 = (i5 + 1) % 2;
                i4++;
            }
            int i7 = 0;
            for (int i8 = i + 1; i8 > 5; i8--) {
                int i9 = i - i8;
                int i10 = ((i8 / 2) + i3) - 1;
                zArr[(((height * 4) + i2) - 8) + i9 + 1] = bitMatrix.get(((i3 + height) - 1) - i7, i10);
                zArr[(((height * 6) + i2) - 12) + i9 + 1] = bitMatrix.get(i10, i3 + i7);
                i7 = (i7 + 1) % 2;
            }
            i3 += 2;
            i2 += (height * 8) - 16;
            nbLayers3--;
            height -= 4;
        }
        return zArr;
    }

    private static BitMatrix removeDashedLines(BitMatrix bitMatrix) {
        int width = ((((bitMatrix.getWidth() - 1) / 2) / 16) * 2) + 1;
        BitMatrix bitMatrix2 = new BitMatrix(bitMatrix.getWidth() - width, bitMatrix.getHeight() - width);
        int i = 0;
        for (int i2 = 0; i2 < bitMatrix.getWidth(); i2++) {
            if (((bitMatrix.getWidth() / 2) - i2) % 16 != 0) {
                int i3 = 0;
                for (int i4 = 0; i4 < bitMatrix.getHeight(); i4++) {
                    if (((bitMatrix.getWidth() / 2) - i4) % 16 != 0) {
                        if (bitMatrix.get(i2, i4)) {
                            bitMatrix2.set(i, i3);
                        }
                        i3++;
                    }
                }
                i++;
            }
        }
        return bitMatrix2;
    }

    private static int readCode(boolean[] zArr, int i, int i2) {
        int i3 = 0;
        for (int i4 = i; i4 < i + i2; i4++) {
            i3 <<= 1;
            if (zArr[i4]) {
                i3++;
            }
        }
        return i3;
    }
}
