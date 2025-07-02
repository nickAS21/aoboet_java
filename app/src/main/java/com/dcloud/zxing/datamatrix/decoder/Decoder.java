package com.dcloud.zxing.datamatrix.decoder;

import com.dcloud.zxing.ChecksumException;
import com.dcloud.zxing.FormatException;
import com.dcloud.zxing.common.BitMatrix;
import com.dcloud.zxing.common.DecoderResult;
import com.dcloud.zxing.common.reedsolomon.GenericGF;
import com.dcloud.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.dcloud.zxing.common.reedsolomon.ReedSolomonException;

import io.dcloud.common.DHInterface.IApp;

/* loaded from: classes.dex */
public final class Decoder {
    private final ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(GenericGF.DATA_MATRIX_FIELD_256);

    public DecoderResult decode(boolean[][] zArr) throws FormatException, ChecksumException {
        int length = zArr.length;
        BitMatrix bitMatrix = new BitMatrix(length);
        for (int i = 0; i < length; i++) {
            for (int i2 = 0; i2 < length; i2++) {
                if (zArr[i][i2]) {
                    bitMatrix.set(i2, i);
                }
            }
        }
        return decode(bitMatrix);
    }

    public DecoderResult decode(BitMatrix bitMatrix) throws FormatException, ChecksumException {
        BitMatrixParser bitMatrixParser = new BitMatrixParser(bitMatrix);
        DataBlock[] dataBlocks = DataBlock.getDataBlocks(bitMatrixParser.readCodewords(), bitMatrixParser.getVersion());
        int length = dataBlocks.length;
        int i = 0;
        for (DataBlock dataBlock : dataBlocks) {
            i += dataBlock.getNumDataCodewords();
        }
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < length; i2++) {
            DataBlock dataBlock2 = dataBlocks[i2];
            byte[] codewords = dataBlock2.getCodewords();
            int numDataCodewords = dataBlock2.getNumDataCodewords();
            correctErrors(codewords, numDataCodewords);
            for (int i3 = 0; i3 < numDataCodewords; i3++) {
                bArr[(i3 * length) + i2] = codewords[i3];
            }
        }
        return DecodedBitStreamParser.decode(bArr);
    }

    private void correctErrors(byte[] bArr, int i) throws ChecksumException {
        int length = bArr.length;
        int[] iArr = new int[length];
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = bArr[i2] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
        }
        try {
            this.rsDecoder.decode(iArr, bArr.length - i);
            for (int i3 = 0; i3 < i; i3++) {
                bArr[i3] = (byte) iArr[i3];
            }
        } catch (ReedSolomonException unused) {
            throw ChecksumException.getChecksumInstance();
        }
    }
}
