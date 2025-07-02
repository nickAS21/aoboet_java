package com.dcloud.zxing.client.result;

import com.dcloud.zxing.Result;

/* loaded from: classes.dex */
public final class WifiResultParser extends ResultParser {
    @Override // com.dcloud.zxing.client.result.ResultParser
    public WifiParsedResult parse(Result result) {
        String matchSinglePrefixedField;
        String massagedText = getMassagedText(result);
        if (!massagedText.startsWith("WIFI:") || (matchSinglePrefixedField = matchSinglePrefixedField("S:", massagedText, ';', false)) == null || matchSinglePrefixedField.length() == 0) {
            return null;
        }
        String matchSinglePrefixedField2 = matchSinglePrefixedField("P:", massagedText, ';', false);
        String matchSinglePrefixedField3 = matchSinglePrefixedField("T:", massagedText, ';', false);
        if (matchSinglePrefixedField3 == null) {
            matchSinglePrefixedField3 = "nopass";
        }
        return new WifiParsedResult(matchSinglePrefixedField3, matchSinglePrefixedField, matchSinglePrefixedField2, Boolean.parseBoolean(matchSinglePrefixedField("H:", massagedText, ';', false)));
    }
}
