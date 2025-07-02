package io.dcloud.common.util.net.http;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class Request {
    private InputStream input;
    private String mData;
    private String uri;

    public Request(InputStream inputStream) {
        this.input = inputStream;
    }

    public void parse() {
        int i;
        StringBuffer stringBuffer = new StringBuffer(2048);
        byte[] bArr = new byte[2048];
        try {
            i = this.input.read(bArr);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append((char) bArr[i2]);
        }
        String stringBuffer2 = stringBuffer.toString();
        this.mData = stringBuffer2;
        String parseUri = parseUri(stringBuffer2);
        this.uri = parseUri;
        this.uri = (parseUri == null || !parseUri.startsWith("/")) ? this.uri : this.uri.substring(1);
    }

    private String parseUri(String str) {
        int i;
        int indexOf;
        int indexOf2 = str.indexOf(32);
        if (indexOf2 == -1 || (indexOf = str.indexOf(32, (i = indexOf2 + 1))) <= indexOf2) {
            return null;
        }
        return str.substring(i, indexOf);
    }

    public String getUri() {
        return this.uri;
    }

    public String getData() {
        return this.mData;
    }
}
