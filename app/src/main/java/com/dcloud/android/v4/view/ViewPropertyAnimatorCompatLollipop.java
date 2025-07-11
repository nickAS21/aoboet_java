package com.dcloud.android.v4.view;

import android.view.View;

/* loaded from: classes.dex */
class ViewPropertyAnimatorCompatLollipop {
    ViewPropertyAnimatorCompatLollipop() {
    }

    public static void translationZ(View view, float f) {
        view.animate().translationZ(f);
    }

    public static void translationZBy(View view, float f) {
        view.animate().translationZBy(f);
    }

    public static void z(View view, float f) {
        view.animate().z(f);
    }

    public static void zBy(View view, float f) {
        view.animate().zBy(f);
    }
}
