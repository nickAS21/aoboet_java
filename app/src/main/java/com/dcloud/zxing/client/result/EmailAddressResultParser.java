package com.dcloud.zxing.client.result;

import com.dcloud.zxing.Result;

import java.util.Map;

/* loaded from: classes.dex */
public final class EmailAddressResultParser extends ResultParser {
    @Override // com.dcloud.zxing.client.result.ResultParser
    public EmailAddressParsedResult parse(Result result) {
        String str;
        String massagedText = getMassagedText(result);
        String str2 = null;
        if (massagedText.startsWith("mailto:") || massagedText.startsWith("MAILTO:")) {
            String substring = massagedText.substring(7);
            int indexOf = substring.indexOf(63);
            if (indexOf >= 0) {
                substring = substring.substring(0, indexOf);
            }
            String urlDecode = urlDecode(substring);
            Map<String, String> parseNameValuePairs = parseNameValuePairs(massagedText);
            if (parseNameValuePairs != null) {
                if (urlDecode.length() == 0) {
                    urlDecode = parseNameValuePairs.get("to");
                }
                str2 = parseNameValuePairs.get("subject");
                str = parseNameValuePairs.get("body");
            } else {
                str = null;
            }
            return new EmailAddressParsedResult(urlDecode, str2, str, massagedText);
        }
        if (EmailDoCoMoResultParser.isBasicallyValidEmailAddress(massagedText)) {
            return new EmailAddressParsedResult(massagedText, null, null, "mailto:" + massagedText);
        }
        return null;
    }
}
