package com.demo.smarthome.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public class BitmapTools {
    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] bArr) {
        if (bArr.length != 0) {
            return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        }
        return null;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i / width, i2 / height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float f) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        int i = height / 2;
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, i, width, i, matrix, false);
        Bitmap createBitmap2 = Bitmap.createBitmap(width, i + height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        float f = height;
        float f2 = width;
        float f3 = height + 4;
        canvas.drawRect(0.0f, f, f2, f3, new Paint());
        canvas.drawBitmap(createBitmap, 0.0f, f3, (Paint) null);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, bitmap.getHeight(), 0.0f, createBitmap2.getHeight() + 4, 1895825407, 16777215, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0f, f, f2, createBitmap2.getHeight() + 4, paint);
        return createBitmap2;
    }

    public static Drawable zoomDrawable(Drawable drawable, int i, int i2) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap drawableToBitmap = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(i / intrinsicWidth, i2 / intrinsicHeight);
        return new BitmapDrawable(Bitmap.createBitmap(drawableToBitmap, 0, 0, intrinsicWidth, intrinsicHeight, matrix, true));
    }
}
