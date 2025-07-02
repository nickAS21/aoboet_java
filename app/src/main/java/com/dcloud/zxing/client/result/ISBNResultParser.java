package com.dcloud.zxing.client.result;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.Result;

/* loaded from: classes.dex */
public final class ISBNResultParser extends ResultParser {
    @Override // com.dcloud.zxing.client.result.ResultParser
    public ISBNParsedResult parse(Result result) {
        if (result.getBarcodeFormat() != BarcodeFormat.EAN_13) {
            return null;
        }
        String massagedText = getMassagedText(result);
        if (massagedText.length() != 13) {
            return null;
        }
        if (massagedText.startsWith("978") || massagedText.startsWith("979")) {
            return new ISBNParsedResult(massagedText);
        }
        return null;
    }
}
