package com.dcloud.android.v4.view;

import android.view.View;
import android.view.animation.Interpolator;

/* loaded from: classes.dex */
class ViewPropertyAnimatorCompatJellybeanMr2 {
    ViewPropertyAnimatorCompatJellybeanMr2() {
    }

    public static Interpolator getInterpolator(View view) {
        return (Interpolator) view.animate().getInterpolator();
    }
}
