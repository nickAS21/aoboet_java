package com.dcloud.android.v4.view.accessibility;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityWindowInfo;

/* loaded from: classes.dex */
class AccessibilityWindowInfoCompatApi21 {
    AccessibilityWindowInfoCompatApi21() {
    }

    public static Object obtain() {
        return AccessibilityWindowInfo.obtain();
    }

    public static Object obtain(Object obj) {
        return AccessibilityWindowInfo.obtain((AccessibilityWindowInfo) obj);
    }

    public static int getType(Object obj) {
        return ((AccessibilityWindowInfo) obj).getType();
    }

    public static int getLayer(Object obj) {
        return ((AccessibilityWindowInfo) obj).getLayer();
    }

    public static Object getRoot(Object obj) {
        return ((AccessibilityWindowInfo) obj).getRoot();
    }

    public static Object getParent(Object obj) {
        return ((AccessibilityWindowInfo) obj).getParent();
    }

    public static int getId(Object obj) {
        return ((AccessibilityWindowInfo) obj).getId();
    }

    public static void getBoundsInScreen(Object obj, Rect rect) {
        ((AccessibilityWindowInfo) obj).getBoundsInScreen(rect);
    }

    public static boolean isActive(Object obj) {
        return ((AccessibilityWindowInfo) obj).isActive();
    }

    public static boolean isFocused(Object obj) {
        return ((AccessibilityWindowInfo) obj).isFocused();
    }

    public static boolean isAccessibilityFocused(Object obj) {
        return ((AccessibilityWindowInfo) obj).isAccessibilityFocused();
    }

    public static int getChildCount(Object obj) {
        return ((AccessibilityWindowInfo) obj).getChildCount();
    }

    public static Object getChild(Object obj, int i) {
        return ((AccessibilityWindowInfo) obj).getChild(i);
    }

    public static void recycle(Object obj) {
        ((AccessibilityWindowInfo) obj).recycle();
    }
}
