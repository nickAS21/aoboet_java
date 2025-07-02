package com.nostra13.dcloudimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;

/* loaded from: classes.dex */
public class ImageNonViewAware implements ImageAware {
    protected final ImageSize imageSize;
    protected final ViewScaleType scaleType;

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public View getWrappedView() {
        return null;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public boolean isCollected() {
        return false;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public boolean setImageBitmap(Bitmap bitmap) {
        return true;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public boolean setImageDrawable(Drawable drawable) {
        return true;
    }

    public ImageNonViewAware(ImageSize imageSize, ViewScaleType viewScaleType) {
        this.imageSize = imageSize;
        this.scaleType = viewScaleType;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public int getWidth() {
        return this.imageSize.getWidth();
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public int getHeight() {
        return this.imageSize.getHeight();
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public ViewScaleType getScaleType() {
        return this.scaleType;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public int getId() {
        return super.hashCode();
    }
}
