package com.dcloud.zxing.client.result;

/* loaded from: classes.dex */
public final class TextParsedResult extends ParsedResult {
    private final String language;
    private final String text;

    public TextParsedResult(String str, String str2) {
        super(ParsedResultType.TEXT);
        this.text = str;
        this.language = str2;
    }

    public String getText() {
        return this.text;
    }

    public String getLanguage() {
        return this.language;
    }

    @Override // com.dcloud.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        return this.text;
    }
}
