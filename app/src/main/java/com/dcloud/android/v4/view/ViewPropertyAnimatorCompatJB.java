package com.dcloud.android.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/* loaded from: classes.dex */
class ViewPropertyAnimatorCompatJB {
    ViewPropertyAnimatorCompatJB() {
    }

    public static void withStartAction(View view, Runnable runnable) {
        view.animate().withStartAction(runnable);
    }

    public static void withEndAction(View view, Runnable runnable) {
        view.animate().withEndAction(runnable);
    }

    public static void withLayer(View view) {
        view.animate().withLayer();
    }

    public static void setListener(final View view, final ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (viewPropertyAnimatorListener != null) {
            view.animate().setListener(new AnimatorListenerAdapter() { // anonymous class
                @Override
                public void onAnimationCancel(Animator animator) {
                    viewPropertyAnimatorListener.onAnimationCancel(view);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    viewPropertyAnimatorListener.onAnimationEnd(view);
                }

                @Override
                public void onAnimationStart(Animator animator) {
                    viewPropertyAnimatorListener.onAnimationStart(view);
                }
            });
        } else {
            view.animate().setListener(null);
        }
    }

}
