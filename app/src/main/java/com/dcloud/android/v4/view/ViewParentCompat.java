package com.dcloud.android.v4.view;

import android.os.Build;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/* loaded from: classes.dex */
public class ViewParentCompat {
    static final ViewParentCompatImpl IMPL;

    /* loaded from: classes.dex */
    interface ViewParentCompatImpl {
        void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int i);

        boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean z);

        boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2);

        void onNestedPreScroll(ViewParent viewParent, View view, int i, int i2, int[] iArr);

        void onNestedScroll(ViewParent viewParent, View view, int i, int i2, int i3, int i4);

        void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int i);

        boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int i);

        void onStopNestedScroll(ViewParent viewParent, View view);

        boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent);
    }

    /* loaded from: classes.dex */
    static class ViewParentCompatStubImpl implements ViewParentCompatImpl {
        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int i) {
        }

        ViewParentCompatStubImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
            if (view == null) {
                return false;
            }
            ((AccessibilityManager) view.getContext().getSystemService("accessibility")).sendAccessibilityEvent(accessibilityEvent);
            return true;
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int i) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent) viewParent).onStartNestedScroll(view, view2, i);
            }
            return false;
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int i) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent) viewParent).onNestedScrollAccepted(view, view2, i);
            }
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void onStopNestedScroll(ViewParent viewParent, View view) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent) viewParent).onStopNestedScroll(view);
            }
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void onNestedScroll(ViewParent viewParent, View view, int i, int i2, int i3, int i4) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent) viewParent).onNestedScroll(view, i, i2, i3, i4);
            }
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void onNestedPreScroll(ViewParent viewParent, View view, int i, int i2, int[] iArr) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent) viewParent).onNestedPreScroll(view, i, i2, iArr);
            }
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean z) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent) viewParent).onNestedFling(view, f, f2, z);
            }
            return false;
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent) viewParent).onNestedPreFling(view, f, f2);
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    static class ViewParentCompatICSImpl extends ViewParentCompatStubImpl {
        ViewParentCompatICSImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
            return ViewParentCompatICS.requestSendAccessibilityEvent(viewParent, view, accessibilityEvent);
        }
    }

    /* loaded from: classes.dex */
    static class ViewParentCompatKitKatImpl extends ViewParentCompatICSImpl {
        ViewParentCompatKitKatImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int i) {
            ViewParentCompatKitKat.notifySubtreeAccessibilityStateChanged(viewParent, view, view2, i);
        }
    }

    /* loaded from: classes.dex */
    static class ViewParentCompatLollipopImpl extends ViewParentCompatKitKatImpl {
        ViewParentCompatLollipopImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int i) {
            return ViewParentCompatLollipop.onStartNestedScroll(viewParent, view, view2, i);
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int i) {
            ViewParentCompatLollipop.onNestedScrollAccepted(viewParent, view, view2, i);
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void onStopNestedScroll(ViewParent viewParent, View view) {
            ViewParentCompatLollipop.onStopNestedScroll(viewParent, view);
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void onNestedScroll(ViewParent viewParent, View view, int i, int i2, int i3, int i4) {
            ViewParentCompatLollipop.onNestedScroll(viewParent, view, i, i2, i3, i4);
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public void onNestedPreScroll(ViewParent viewParent, View view, int i, int i2, int[] iArr) {
            ViewParentCompatLollipop.onNestedPreScroll(viewParent, view, i, i2, iArr);
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean z) {
            return ViewParentCompatLollipop.onNestedFling(viewParent, view, f, f2, z);
        }

        @Override // com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatStubImpl, com.dcloud.android.v4.view.ViewParentCompat.ViewParentCompatImpl
        public boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
            return ViewParentCompatLollipop.onNestedPreFling(viewParent, view, f, f2);
        }
    }

    static {
        ViewParentCompatImpl impl1;
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            impl1 = new ViewParentCompatLollipopImpl();
        }
        if (i >= 19) {
            impl1 = new ViewParentCompatKitKatImpl();
        } else if (i >= 14) {
            impl1 = new ViewParentCompatICSImpl();
        } else {
            impl1 = new ViewParentCompatStubImpl();
        }
        IMPL = impl1;
    }

    private ViewParentCompat() {
    }

    public static boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
        return IMPL.requestSendAccessibilityEvent(viewParent, view, accessibilityEvent);
    }

    public static boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int i) {
        return IMPL.onStartNestedScroll(viewParent, view, view2, i);
    }

    public static void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int i) {
        IMPL.onNestedScrollAccepted(viewParent, view, view2, i);
    }

    public static void onStopNestedScroll(ViewParent viewParent, View view) {
        IMPL.onStopNestedScroll(viewParent, view);
    }

    public static void onNestedScroll(ViewParent viewParent, View view, int i, int i2, int i3, int i4) {
        IMPL.onNestedScroll(viewParent, view, i, i2, i3, i4);
    }

    public static void onNestedPreScroll(ViewParent viewParent, View view, int i, int i2, int[] iArr) {
        IMPL.onNestedPreScroll(viewParent, view, i, i2, iArr);
    }

    public static boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean z) {
        return IMPL.onNestedFling(viewParent, view, f, f2, z);
    }

    public static boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
        return IMPL.onNestedPreFling(viewParent, view, f, f2);
    }

    public static void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int i) {
        IMPL.notifySubtreeAccessibilityStateChanged(viewParent, view, view2, i);
    }
}
