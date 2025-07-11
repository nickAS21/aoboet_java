package com.nostra13.dcloudimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;

/* loaded from: classes.dex */
public interface ImageAware {
    int getHeight();

    int getId();

    ViewScaleType getScaleType();

    int getWidth();

    View getWrappedView();

    boolean isCollected();

    boolean setImageBitmap(Bitmap bitmap);

    boolean setImageDrawable(Drawable drawable);
}
