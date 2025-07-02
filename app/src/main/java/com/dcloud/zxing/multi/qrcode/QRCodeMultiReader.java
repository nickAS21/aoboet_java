package com.dcloud.zxing.multi.qrcode;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.BinaryBitmap;
import com.dcloud.zxing.DecodeHintType;
import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.ReaderException;
import com.dcloud.zxing.Result;
import com.dcloud.zxing.ResultMetadataType;
import com.dcloud.zxing.common.DecoderResult;
import com.dcloud.zxing.common.DetectorResult;
import com.dcloud.zxing.multi.MultipleBarcodeReader;
import com.dcloud.zxing.multi.qrcode.detector.MultiDetector;
import com.dcloud.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader {
    private static final Result[] EMPTY_RESULT_ARRAY = new Result[0];

    @Override // com.dcloud.zxing.multi.MultipleBarcodeReader
    public Result[] decodeMultiple(BinaryBitmap binaryBitmap) throws NotFoundException {
        return decodeMultiple(binaryBitmap, null);
    }

    @Override // com.dcloud.zxing.multi.MultipleBarcodeReader
    public Result[] decodeMultiple(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException {
        ArrayList arrayList = new ArrayList();
        for (DetectorResult detectorResult : new MultiDetector(binaryBitmap.getBlackMatrix()).detectMulti(map)) {
            try {
                DecoderResult decode = getDecoder().decode(detectorResult.getBits(), map);
                Result result = new Result(decode.getText(), decode.getRawBytes(), detectorResult.getPoints(), BarcodeFormat.QR_CODE);
                List<byte[]> byteSegments = decode.getByteSegments();
                if (byteSegments != null) {
                    result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
                }
                String eCLevel = decode.getECLevel();
                if (eCLevel != null) {
                    result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, eCLevel);
                }
                arrayList.add(result);
            } catch (ReaderException unused) {
            }
        }
        if (arrayList.isEmpty()) {
            return EMPTY_RESULT_ARRAY;
        }
        return (Result[]) arrayList.toArray(new Result[arrayList.size()]);
    }
}
