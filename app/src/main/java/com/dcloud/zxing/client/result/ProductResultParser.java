package com.dcloud.zxing.client.result;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.Result;
import com.dcloud.zxing.oned.UPCEReader;

/* loaded from: classes.dex */
public final class ProductResultParser extends ResultParser {
    @Override // com.dcloud.zxing.client.result.ResultParser
    public ProductParsedResult parse(Result result) {
        BarcodeFormat barcodeFormat = result.getBarcodeFormat();
        if (barcodeFormat != BarcodeFormat.UPC_A && barcodeFormat != BarcodeFormat.UPC_E && barcodeFormat != BarcodeFormat.EAN_8 && barcodeFormat != BarcodeFormat.EAN_13) {
            return null;
        }
        String massagedText = getMassagedText(result);
        int length = massagedText.length();
        for (int i = 0; i < length; i++) {
            char charAt = massagedText.charAt(i);
            if (charAt < '0' || charAt > '9') {
                return null;
            }
        }
        return new ProductParsedResult(massagedText, barcodeFormat == BarcodeFormat.UPC_E ? UPCEReader.convertUPCEtoUPCA(massagedText) : massagedText);
    }
}
