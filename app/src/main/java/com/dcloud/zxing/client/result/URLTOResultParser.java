package com.dcloud.zxing.client.result;

import com.dcloud.zxing.Result;

/* loaded from: classes.dex */
public final class URLTOResultParser extends ResultParser {
    @Override // com.dcloud.zxing.client.result.ResultParser
    public URIParsedResult parse(Result result) {
        int indexOf;
        String massagedText = getMassagedText(result);
        if ((massagedText.startsWith("urlto:") || massagedText.startsWith("URLTO:")) && (indexOf = massagedText.indexOf(58, 6)) >= 0) {
            return new URIParsedResult(massagedText.substring(indexOf + 1), indexOf > 6 ? massagedText.substring(6, indexOf) : null);
        }
        return null;
    }
}
