package com.dcloud.zxing.client.result;

import com.dcloud.zxing.Result;

/* loaded from: classes.dex */
public final class TelResultParser extends ResultParser {
    @Override // com.dcloud.zxing.client.result.ResultParser
    public TelParsedResult parse(Result result) {
        String massagedText = getMassagedText(result);
        if (!massagedText.startsWith("tel:") && !massagedText.startsWith("TEL:")) {
            return null;
        }
        String str = massagedText.startsWith("TEL:") ? "tel:" + massagedText.substring(4) : massagedText;
        int indexOf = massagedText.indexOf(63, 4);
        return new TelParsedResult(indexOf < 0 ? massagedText.substring(4) : massagedText.substring(4, indexOf), str, null);
    }
}
