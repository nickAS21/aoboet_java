package com.dcloud.android.v4.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

/* loaded from: classes.dex */
class AccessibilityNodeInfoCompatJellybeanMr1 {
    AccessibilityNodeInfoCompatJellybeanMr1() {
    }

    public static void setLabelFor(Object obj, View view) {
        ((AccessibilityNodeInfo) obj).setLabelFor(view);
    }

    public static void setLabelFor(Object obj, View view, int i) {
        ((AccessibilityNodeInfo) obj).setLabelFor(view, i);
    }

    public static Object getLabelFor(Object obj) {
        return ((AccessibilityNodeInfo) obj).getLabelFor();
    }

    public static void setLabeledBy(Object obj, View view) {
        ((AccessibilityNodeInfo) obj).setLabeledBy(view);
    }

    public static void setLabeledBy(Object obj, View view, int i) {
        ((AccessibilityNodeInfo) obj).setLabeledBy(view, i);
    }

    public static Object getLabeledBy(Object obj) {
        return ((AccessibilityNodeInfo) obj).getLabeledBy();
    }
}
