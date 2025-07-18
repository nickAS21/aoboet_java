package com.dcloud.android.v4.view.accessibility;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

import java.util.List;

/* loaded from: classes.dex */
class AccessibilityNodeProviderCompatKitKat {

    /* loaded from: classes.dex */
    interface AccessibilityNodeInfoBridge {
        Object createAccessibilityNodeInfo(int i);

        List<Object> findAccessibilityNodeInfosByText(String str, int i);

        Object findFocus(int i);

        boolean performAction(int i, int i2, Bundle bundle);
    }

    AccessibilityNodeProviderCompatKitKat() {
    }

    public static Object newAccessibilityNodeProviderBridge(final AccessibilityNodeInfoBridge accessibilityNodeInfoBridge) {
        return new AccessibilityNodeProvider() { // from class: com.dcloud.android.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.1
            @Override // android.view.accessibility.AccessibilityNodeProvider
            public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
                return createAccessibilityNodeInfo(i);
            }

            @Override // android.view.accessibility.AccessibilityNodeProvider
            public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i) {
                return findAccessibilityNodeInfosByText(str, i);
            }

            @Override // android.view.accessibility.AccessibilityNodeProvider
            public boolean performAction(int i, int i2, Bundle bundle) {
                return performAction(i, i2, bundle);
            }

            @Override // android.view.accessibility.AccessibilityNodeProvider
            public AccessibilityNodeInfo findFocus(int i) {
                return findFocus(i);
            }
        };
    }
}
