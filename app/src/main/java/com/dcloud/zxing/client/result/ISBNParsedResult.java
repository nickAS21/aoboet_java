package com.dcloud.zxing.client.result;

/* loaded from: classes.dex */
public final class ISBNParsedResult extends ParsedResult {
    private final String isbn;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ISBNParsedResult(String str) {
        super(ParsedResultType.ISBN);
        this.isbn = str;
    }

    public String getISBN() {
        return this.isbn;
    }

    @Override // com.dcloud.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        return this.isbn;
    }
}
