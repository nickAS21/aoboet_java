package com.nostra13.dcloudimageloader.core.assist;

import android.graphics.Bitmap;
import android.view.View;

/* loaded from: classes.dex */
public class SyncImageLoadingListener extends SimpleImageLoadingListener {
    private Bitmap loadedImage;

    @Override // com.nostra13.dcloudimageloader.core.assist.SimpleImageLoadingListener, com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
    public void onLoadingComplete(String str, View view, Bitmap bitmap) {
        this.loadedImage = bitmap;
    }

    public Bitmap getLoadedBitmap() {
        return this.loadedImage;
    }
}
