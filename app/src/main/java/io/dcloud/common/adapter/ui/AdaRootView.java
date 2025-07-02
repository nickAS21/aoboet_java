package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import io.dcloud.common.adapter.util.Logger;

/* loaded from: classes.dex */
public class AdaRootView extends AdaContainerFrameItem {
    FrameLayout mMyRootView;

    protected AdaRootView(Context context, FrameLayout frameLayout) {
        super(context);
        this.mMyRootView = null;
        if (frameLayout != null) {
            this.mMyRootView = frameLayout;
        } else {
            this.mMyRootView = new MyRootView(context, this);
        }
        setMainView(this.mMyRootView);
    }

    /* loaded from: classes.dex */
    class MyRootView extends FrameLayout {
        AdaRootView mProxy;

        public MyRootView(Context context, AdaRootView adaRootView) {
            super(context);
            this.mProxy = null;
            this.mProxy = adaRootView;
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            AdaRootView.this.mViewOptions.onScreenChanged();
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            AdaRootView.this.mViewOptions.onScreenChanged(i, i2);
            Logger.d(Logger.LAYOUT_TAG, "AdaRootView onSizeChanged", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
        }
    }

    /* loaded from: classes.dex */
    public static class GestureListenerImpl implements GestureOverlayView.OnGestureListener {
        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        }

        @Override // android.gesture.GestureOverlayView.OnGestureListener
        public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
            gestureOverlayView.getGesture().getStrokes();
        }
    }
}
