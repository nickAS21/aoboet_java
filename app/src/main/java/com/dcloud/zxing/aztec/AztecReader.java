package com.dcloud.zxing.aztec;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.BinaryBitmap;
import com.dcloud.zxing.DecodeHintType;
import com.dcloud.zxing.FormatException;
import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.Reader;
import com.dcloud.zxing.Result;
import com.dcloud.zxing.ResultMetadataType;
import com.dcloud.zxing.ResultPoint;
import com.dcloud.zxing.ResultPointCallback;
import com.dcloud.zxing.aztec.decoder.Decoder;
import com.dcloud.zxing.aztec.detector.Detector;
import com.dcloud.zxing.common.DecoderResult;

import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class AztecReader implements Reader {
    @Override // com.dcloud.zxing.Reader
    public void reset() {
    }

    @Override // com.dcloud.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, null);
    }

    @Override // com.dcloud.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException {
        ResultPointCallback resultPointCallback;
        AztecDetectorResult detect = new Detector(binaryBitmap.getBlackMatrix()).detect();
        ResultPoint[] points = detect.getPoints();
        if (map != null && (resultPointCallback = (ResultPointCallback) map.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK)) != null) {
            for (ResultPoint resultPoint : points) {
                resultPointCallback.foundPossibleResultPoint(resultPoint);
            }
        }
        DecoderResult decode = new Decoder().decode(detect);
        Result result = new Result(decode.getText(), decode.getRawBytes(), points, BarcodeFormat.AZTEC);
        List<byte[]> byteSegments = decode.getByteSegments();
        if (byteSegments != null) {
            result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        String eCLevel = decode.getECLevel();
        if (eCLevel != null) {
            result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, eCLevel);
        }
        return result;
    }
}
