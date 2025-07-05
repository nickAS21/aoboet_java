package com.dcloud.android.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;

import java.lang.reflect.Field;

/* loaded from: classes.dex */
class ViewCompatBase {
    private static final String TAG = "ViewCompatBase";
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;

    ViewCompatBase() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static ColorStateList getBackgroundTintList(View view) {
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView) view).getSupportBackgroundTintList();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static void setBackgroundTintList(View view, ColorStateList colorStateList) {
        if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView) view).setSupportBackgroundTintList(colorStateList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static PorterDuff.Mode getBackgroundTintMode(View view) {
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView) view).getSupportBackgroundTintMode();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView) view).setSupportBackgroundTintMode(mode);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isLaidOut(View view) {
        return view.getWidth() > 0 && view.getHeight() > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMinimumWidth(View view) {
        return view != null ? view.getMinimumWidth() : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMinimumHeight(View view) {
        return view != null ? view.getMinimumHeight() : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAttachedToWindow(View view) {
        return view.getWindowToken() != null;
    }
}
