package com.dcloud.zxing;

/* loaded from: classes.dex */
public final class ChecksumException extends ReaderException {
    private static final ChecksumException instance = new ChecksumException();

    private ChecksumException() {
    }

    public static ChecksumException getChecksumInstance() {
        return instance;
    }
}
