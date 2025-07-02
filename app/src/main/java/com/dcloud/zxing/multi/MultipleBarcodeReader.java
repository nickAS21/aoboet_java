package com.dcloud.zxing.multi;

import com.dcloud.zxing.BinaryBitmap;
import com.dcloud.zxing.DecodeHintType;
import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.Result;

import java.util.Map;

/* loaded from: classes.dex */
public interface MultipleBarcodeReader {
    Result[] decodeMultiple(BinaryBitmap binaryBitmap) throws NotFoundException;

    Result[] decodeMultiple(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException;
}
