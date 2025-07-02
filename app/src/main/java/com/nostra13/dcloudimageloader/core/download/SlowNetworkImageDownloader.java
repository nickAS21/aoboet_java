package com.nostra13.dcloudimageloader.core.download;

import com.nostra13.dcloudimageloader.core.assist.FlushedInputStream;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class SlowNetworkImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    public SlowNetworkImageDownloader(ImageDownloader imageDownloader) {
        this.wrappedDownloader = imageDownloader;
    }

    @Override // com.nostra13.dcloudimageloader.core.download.ImageDownloader
    public InputStream getStream(String str, Object obj) throws IOException {
        InputStream stream = this.wrappedDownloader.getStream(str, obj);
        int ordinal = ImageDownloader.Scheme.ofUri(str).ordinal();
        return (ordinal == 1 || ordinal == 2) ? new FlushedInputStream(stream) : stream;
    }
}
