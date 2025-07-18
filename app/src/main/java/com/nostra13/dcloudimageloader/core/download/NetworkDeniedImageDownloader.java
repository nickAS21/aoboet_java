package com.nostra13.dcloudimageloader.core.download;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class NetworkDeniedImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    public NetworkDeniedImageDownloader(ImageDownloader imageDownloader) {
        this.wrappedDownloader = imageDownloader;
    }

    @Override // com.nostra13.dcloudimageloader.core.download.ImageDownloader
    public InputStream getStream(String str, Object obj) throws IOException {
        int ordinal = ImageDownloader.Scheme.ofUri(str).ordinal();
        if (ordinal == 1 || ordinal == 2) {
            throw new IllegalStateException();
        }
        return this.wrappedDownloader.getStream(str, obj);
    }
}
