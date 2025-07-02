package com.nostra13.dcloudimageloader.utils;

import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;

/* loaded from: classes.dex */
public final class ImageSizeUtils {
    private ImageSizeUtils() {
    }

    public static ImageSize defineTargetSizeForView(ImageAware imageAware, ImageSize imageSize) {
        int width = imageAware.getWidth();
        if (width <= 0) {
            width = imageSize.getWidth();
        }
        int height = imageAware.getHeight();
        if (height <= 0) {
            height = imageSize.getHeight();
        }
        return new ImageSize(width, height);
    }

    public static int computeImageSampleSize(ImageSize imageSize, ImageSize imageSize2, ViewScaleType viewScaleType, boolean z) {
        int i;
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        int width2 = imageSize2.getWidth();
        int height2 = imageSize2.getHeight();
        int i2 = width / width2;
        int i3 = height / height2;
        int i4 = AnonymousClass1.$SwitchMap$com$nostra13$dcloudimageloader$core$assist$ViewScaleType[viewScaleType.ordinal()];
        if (i4 == 1) {
            if (z) {
                i = 1;
                while (true) {
                    width /= 2;
                    if (width < width2 && height / 2 < height2) {
                        break;
                    }
                    height /= 2;
                    i *= 2;
                }
            } else {
                i = Math.max(i2, i3);
            }
        } else if (i4 != 2) {
            i = 1;
        } else if (z) {
            i = 1;
            while (true) {
                width /= 2;
                if (width < width2 || (height = height / 2) < height2) {
                    break;
                }
                i *= 2;
            }
        } else {
            i = Math.min(i2, i3);
        }
        if (i < 1) {
            return 1;
        }
        return i;
    }

    /* renamed from: com.nostra13.dcloudimageloader.utils.ImageSizeUtils$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$nostra13$dcloudimageloader$core$assist$ViewScaleType;

        static {
            int[] iArr = new int[ViewScaleType.values().length];
            $SwitchMap$com$nostra13$dcloudimageloader$core$assist$ViewScaleType = iArr;
            try {
                iArr[ViewScaleType.FIT_INSIDE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$nostra13$dcloudimageloader$core$assist$ViewScaleType[ViewScaleType.CROP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static float computeImageScale(ImageSize imageSize, ImageSize imageSize2, ViewScaleType viewScaleType, boolean z) {
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        int width2 = imageSize2.getWidth();
        int height2 = imageSize2.getHeight();
        float f = width;
        float f2 = f / width2;
        float f3 = height;
        float f4 = f3 / height2;
        if ((viewScaleType != ViewScaleType.FIT_INSIDE || f2 < f4) && (viewScaleType != ViewScaleType.CROP || f2 >= f4)) {
            width2 = (int) (f / f4);
        } else {
            height2 = (int) (f3 / f2);
        }
        if ((z || width2 >= width || height2 >= height) && (!z || width2 == width || height2 == height)) {
            return 1.0f;
        }
        return width2 / f;
    }
}
