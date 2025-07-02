package com.nostra13.dcloudimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;
import com.nostra13.dcloudimageloader.core.imageaware.ImageViewAware;
import com.nostra13.dcloudimageloader.utils.L;

/* loaded from: classes.dex */
public class RoundedBitmapDisplayer implements BitmapDisplayer {
    private final int roundPixels;

    public RoundedBitmapDisplayer(int i) {
        this.roundPixels = i;
    }

    @Override // com.nostra13.dcloudimageloader.core.display.BitmapDisplayer
    public Bitmap display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }
        Bitmap roundCorners = roundCorners(bitmap, (ImageViewAware) imageAware, this.roundPixels);
        imageAware.setImageBitmap(roundCorners);
        return roundCorners;
    }

    public static Bitmap roundCorners(Bitmap bitmap, ImageViewAware imageViewAware, int i) {
        int i2;
        int i3;
        Rect rect;
        int i4;
        int i5;
        Rect rect2;
        Rect rect3 = null;
        int i6;
        int i7;
        Rect rect4 = null;
        ImageView wrappedView = imageViewAware.getWrappedView();
        if (wrappedView == null) {
            L.w("View is collected probably. Can't round bitmap corners without view properties.", new Object[0]);
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = imageViewAware.getWidth();
        int height2 = imageViewAware.getHeight();
        if (width2 <= 0) {
            width2 = width;
        }
        if (height2 <= 0) {
            height2 = height;
        }
        ImageView.ScaleType scaleType = wrappedView.getScaleType();
        if (scaleType == null) {
            return bitmap;
        }
        int ordinal = scaleType.ordinal();
        try {
            if (ordinal == 1) {
                float f = width;
                float f2 = height;
                if (width2 / height2 > f / f2) {
                    i3 = Math.min(height2, height);
                    i2 = (int) (f / (f2 / i3));
                } else {
                    int min = Math.min(width2, width);
                    int i8 = (int) (f2 / (f / min));
                    i2 = min;
                    i3 = i8;
                }
                int i9 = (width2 - i2) / 2;
                int i10 = (height2 - i3) / 2;
                Rect rect5 = new Rect(0, 0, width, height);
                rect = new Rect(i9, i10, i2 + i9, i3 + i10);
                i4 = width2;
                i5 = height2;
                rect2 = rect5;
            } else {
                if (ordinal == 5) {
                    float f3 = width2;
                    float f4 = height2;
                    float f5 = width;
                    float f6 = height;
                    if (f3 / f4 > f5 / f6) {
                        int i11 = (int) (f4 * (f5 / f3));
                        i6 = (height - i11) / 2;
                        height = i11;
                        i7 = 0;
                    } else {
                        int i12 = (int) (f3 * (f6 / f4));
                        int i13 = (width - i12) / 2;
                        i6 = 0;
                        width = i12;
                        i7 = i13;
                    }
                    i4 = width;
                    i5 = height;
                    rect2 = new Rect(i7, i6, i7 + width, i6 + height);
                    rect3 = new Rect(0, 0, width, height);
                    return getRoundedCornerBitmap(bitmap, i, rect2, rect3, i4, i5);
                }
                if (ordinal == 6) {
                    rect4 = new Rect(0, 0, width, height);
                    rect = new Rect(0, 0, width2, height2);
                } else if (ordinal != 7 && ordinal != 8) {
                    float f7 = width2;
                    float f8 = height2;
                    float f9 = width;
                    float f10 = height;
                    if (f7 / f8 > f9 / f10) {
                        width2 = (int) (f9 / (f10 / f8));
                    } else {
                        height2 = (int) (f10 / (f9 / f7));
                    }
                    rect4 = new Rect(0, 0, width, height);
                    rect = new Rect(0, 0, width2, height2);
                } else {
                    int min2 = Math.min(width2, width);
                    int min3 = Math.min(height2, height);
                    int i14 = (width - min2) / 2;
                    int i15 = (height - min3) / 2;
                    Rect rect6 = new Rect(i14, i15, i14 + min2, i15 + min3);
                    rect = new Rect(0, 0, min2, min3);
                    i4 = min2;
                    i5 = min3;
                    rect2 = rect6;
                }
                i4 = width2;
                i5 = height2;
                rect2 = rect4;
            }
            return getRoundedCornerBitmap(bitmap, i, rect2, rect3, i4, i5);
        } catch (OutOfMemoryError e) {
            L.e(e, "Can't create bitmap with rounded corners. Not enough memory.", new Object[0]);
            return bitmap;
        }
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int i, Rect rect, Rect rect2, int i2, int i3) {
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(rect2);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-16777216);
        float f = i;
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return createBitmap;
    }
}
