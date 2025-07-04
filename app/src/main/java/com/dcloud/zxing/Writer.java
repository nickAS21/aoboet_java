package com.dcloud.zxing;

import com.dcloud.zxing.common.BitMatrix;

import java.util.Map;

/* loaded from: classes.dex */
public interface Writer {
    BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException;

    BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException;
}
