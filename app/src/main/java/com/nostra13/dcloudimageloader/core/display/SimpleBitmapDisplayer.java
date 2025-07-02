package com.nostra13.dcloudimageloader.core.display;

import android.graphics.Bitmap;

import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;

/* loaded from: classes.dex */
public final class SimpleBitmapDisplayer implements BitmapDisplayer {
    @Override // com.nostra13.dcloudimageloader.core.display.BitmapDisplayer
    public Bitmap display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(bitmap);
        return bitmap;
    }
}
