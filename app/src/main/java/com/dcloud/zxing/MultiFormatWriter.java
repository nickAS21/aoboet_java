package com.dcloud.zxing;

import com.dcloud.zxing.aztec.AztecWriter;
import com.dcloud.zxing.common.BitMatrix;
import com.dcloud.zxing.datamatrix.DataMatrixWriter;
import com.dcloud.zxing.oned.CodaBarWriter;
import com.dcloud.zxing.oned.Code128Writer;
import com.dcloud.zxing.oned.Code39Writer;
import com.dcloud.zxing.oned.EAN13Writer;
import com.dcloud.zxing.oned.EAN8Writer;
import com.dcloud.zxing.oned.ITFWriter;
import com.dcloud.zxing.oned.UPCAWriter;
import com.dcloud.zxing.pdf417.PDF417Writer;
import com.dcloud.zxing.qrcode.QRCodeWriter;

import java.util.Map;

/* loaded from: classes.dex */
public final class MultiFormatWriter implements Writer {
    @Override // com.dcloud.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException {
        return encode(str, barcodeFormat, i, i2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.dcloud.zxing.MultiFormatWriter$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$dcloud$zxing$BarcodeFormat;

        static {
            int[] iArr = new int[BarcodeFormat.values().length];
            $SwitchMap$com$dcloud$zxing$BarcodeFormat = iArr;
            try {
                iArr[BarcodeFormat.EAN_8.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.EAN_13.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.UPC_A.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.QR_CODE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.CODE_39.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.CODE_128.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.ITF.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.PDF_417.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.CODABAR.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.DATA_MATRIX.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$dcloud$zxing$BarcodeFormat[BarcodeFormat.AZTEC.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    @Override // com.dcloud.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException {
        Writer eAN8Writer;
        switch (AnonymousClass1.$SwitchMap$com$dcloud$zxing$BarcodeFormat[barcodeFormat.ordinal()]) {
            case 1:
                eAN8Writer = new EAN8Writer();
                break;
            case 2:
                eAN8Writer = new EAN13Writer();
                break;
            case 3:
                eAN8Writer = new UPCAWriter();
                break;
            case 4:
                eAN8Writer = new QRCodeWriter();
                break;
            case 5:
                eAN8Writer = new Code39Writer();
                break;
            case 6:
                eAN8Writer = new Code128Writer();
                break;
            case 7:
                eAN8Writer = new ITFWriter();
                break;
            case 8:
                eAN8Writer = new PDF417Writer();
                break;
            case 9:
                eAN8Writer = new CodaBarWriter();
                break;
            case 10:
                eAN8Writer = new DataMatrixWriter();
                break;
            case 11:
                eAN8Writer = new AztecWriter();
                break;
            default:
                throw new IllegalArgumentException("No encoder available for format " + barcodeFormat);
        }
        return eAN8Writer.encode(str, barcodeFormat, i, i2, map);
    }
}
