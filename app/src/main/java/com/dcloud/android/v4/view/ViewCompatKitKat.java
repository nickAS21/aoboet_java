package com.dcloud.android.v4.view;

import android.view.View;

/* loaded from: classes.dex */
class ViewCompatKitKat {
    ViewCompatKitKat() {
    }

    public static int getAccessibilityLiveRegion(View view) {
        return view.getAccessibilityLiveRegion();
    }

    public static void setAccessibilityLiveRegion(View view, int i) {
        view.setAccessibilityLiveRegion(i);
    }

    public static boolean isLaidOut(View view) {
        return view.isLaidOut();
    }

    public static boolean isAttachedToWindow(View view) {
        return view.isAttachedToWindow();
    }
}
