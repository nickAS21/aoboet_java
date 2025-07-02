package com.nostra13.dcloudimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;
import com.nostra13.dcloudimageloader.utils.L;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class ImageViewAware implements ImageAware {
    protected boolean checkActualViewSize;
    protected Reference<ImageView> imageViewRef;

    public ImageViewAware(ImageView imageView) {
        this(imageView, true);
    }

    public ImageViewAware(ImageView imageView, boolean z) {
        this.imageViewRef = new WeakReference(imageView);
        this.checkActualViewSize = z;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public int getWidth() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int width = (!this.checkActualViewSize || layoutParams == null || layoutParams.width == -2) ? 0 : imageView.getWidth();
        if (width <= 0 && layoutParams != null) {
            width = layoutParams.width;
        }
        if (width <= 0) {
            width = getImageViewFieldValue(imageView, "mMaxWidth");
        }
        L.w("width = " + width, new Object[0]);
        return width;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public int getHeight() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int height = (!this.checkActualViewSize || layoutParams == null || layoutParams.height == -2) ? 0 : imageView.getHeight();
        if (height <= 0 && layoutParams != null) {
            height = layoutParams.height;
        }
        if (height <= 0) {
            height = getImageViewFieldValue(imageView, "mMaxHeight");
        }
        L.w("height = " + height, new Object[0]);
        return height;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public ViewScaleType getScaleType() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return null;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public ImageView getWrappedView() {
        return this.imageViewRef.get();
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public boolean isCollected() {
        return this.imageViewRef.get() == null;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public int getId() {
        ImageView imageView = this.imageViewRef.get();
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    private static int getImageViewFieldValue(Object obj, String str) {
        try {
            Field declaredField = ImageView.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            int intValue = ((Integer) declaredField.get(obj)).intValue();
            if (intValue <= 0 || intValue >= Integer.MAX_VALUE) {
                return 0;
            }
            return intValue;
        } catch (Exception e) {
            L.e(e);
            return 0;
        }
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public boolean setImageDrawable(Drawable drawable) {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return false;
        }
        imageView.setImageDrawable(drawable);
        return true;
    }

    @Override // com.nostra13.dcloudimageloader.core.imageaware.ImageAware
    public boolean setImageBitmap(Bitmap bitmap) {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return false;
        }
        imageView.setImageBitmap(bitmap);
        return true;
    }
}
