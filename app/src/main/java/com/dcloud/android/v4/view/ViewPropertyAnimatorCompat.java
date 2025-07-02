package com.dcloud.android.v4.view;

import android.view.View;
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public class ViewPropertyAnimatorCompat {
    static final ViewPropertyAnimatorCompatImpl IMPL;
    static final int LISTENER_TAG_ID = 2113929216;
    private static final String TAG = "ViewAnimatorCompat";
    private WeakReference<View> mView;
    private Runnable mStartAction = null;
    private Runnable mEndAction = null;
    private int mOldLayerType = -1;

    /* loaded from: classes.dex */
    interface ViewPropertyAnimatorCompatImpl {
        void alpha(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void alphaBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void cancel(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        long getDuration(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        Interpolator getInterpolator(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        long getStartDelay(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        void rotation(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void scaleX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void scaleXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void scaleY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void scaleYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void setDuration(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, long j);

        void setInterpolator(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Interpolator interpolator);

        void setListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener);

        void setStartDelay(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, long j);

        void setUpdateListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorUpdateListener viewPropertyAnimatorUpdateListener);

        void start(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        void translationX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationZ(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationZBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void withEndAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable);

        void withLayer(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        void withStartAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable);

        void x(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void xBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void y(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void yBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void z(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void zBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewPropertyAnimatorCompat(View view) {
        this.mView = new WeakReference<>(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class BaseViewPropertyAnimatorCompatImpl implements ViewPropertyAnimatorCompatImpl {
        WeakHashMap<View, Runnable> mStarterMap = null;

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public long getDuration(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            return 0L;
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public Interpolator getInterpolator(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            return null;
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public long getStartDelay(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            return 0L;
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setDuration(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, long j) {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setInterpolator(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Interpolator interpolator) {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setStartDelay(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, long j) {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setUpdateListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorUpdateListener viewPropertyAnimatorUpdateListener) {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationZ(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationZBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withLayer(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void z(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void zBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
        }

        BaseViewPropertyAnimatorCompatImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void alpha(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withEndAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable) {
            viewPropertyAnimatorCompat.mEndAction = runnable;
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void alphaBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotation(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void cancel(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void x(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void xBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void y(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void yBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void start(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            removeStartMessage(view);
            startAnimation(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withStartAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable) {
            viewPropertyAnimatorCompat.mStartAction = runnable;
            postStartMessage(viewPropertyAnimatorCompat, view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
            view.setTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID, viewPropertyAnimatorListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startAnimation(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            Object tag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
            ViewPropertyAnimatorListener viewPropertyAnimatorListener = tag instanceof ViewPropertyAnimatorListener ? (ViewPropertyAnimatorListener) tag : null;
            Runnable runnable = viewPropertyAnimatorCompat.mStartAction;
            Runnable runnable2 = viewPropertyAnimatorCompat.mEndAction;
            if (runnable != null) {
                runnable.run();
            }
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationStart(view);
                viewPropertyAnimatorListener.onAnimationEnd(view);
            }
            if (runnable2 != null) {
                runnable2.run();
            }
            WeakHashMap<View, Runnable> weakHashMap = this.mStarterMap;
            if (weakHashMap != null) {
                weakHashMap.remove(view);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class Starter implements Runnable {
            WeakReference<View> mViewRef;
            ViewPropertyAnimatorCompat mVpa;

            private Starter(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
                this.mViewRef = new WeakReference<>(view);
                this.mVpa = viewPropertyAnimatorCompat;
            }

            @Override // java.lang.Runnable
            public void run() {
                View view = this.mViewRef.get();
                if (view != null) {
                    BaseViewPropertyAnimatorCompatImpl.this.startAnimation(this.mVpa, view);
                }
            }
        }

        private void removeStartMessage(View view) {
            Runnable runnable;
            WeakHashMap<View, Runnable> weakHashMap = this.mStarterMap;
            if (weakHashMap == null || (runnable = weakHashMap.get(view)) == null) {
                return;
            }
            view.removeCallbacks(runnable);
        }

        private void postStartMessage(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            WeakHashMap<View, Runnable> weakHashMap = this.mStarterMap;
            Runnable runnable = weakHashMap != null ? weakHashMap.get(view) : null;
            if (runnable == null) {
                runnable = new Starter(viewPropertyAnimatorCompat, view);
                if (this.mStarterMap == null) {
                    this.mStarterMap = new WeakHashMap<>();
                }
                this.mStarterMap.put(view, runnable);
            }
            view.removeCallbacks(runnable);
            view.post(runnable);
        }
    }

    /* loaded from: classes.dex */
    static class ICSViewPropertyAnimatorCompatImpl extends BaseViewPropertyAnimatorCompatImpl {
        WeakHashMap<View, Integer> mLayerMap = null;

        ICSViewPropertyAnimatorCompatImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setDuration(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, long j) {
            ViewPropertyAnimatorCompatICS.setDuration(view, j);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void alpha(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.alpha(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.translationX(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.translationY(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public long getDuration(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            return ViewPropertyAnimatorCompatICS.getDuration(view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setInterpolator(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Interpolator interpolator) {
            ViewPropertyAnimatorCompatICS.setInterpolator(view, interpolator);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setStartDelay(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, long j) {
            ViewPropertyAnimatorCompatICS.setStartDelay(view, j);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public long getStartDelay(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            return ViewPropertyAnimatorCompatICS.getStartDelay(view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void alphaBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.alphaBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotation(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.rotation(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.rotationBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.rotationX(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.rotationXBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.rotationY(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.rotationYBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.scaleX(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.scaleXBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.scaleY(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.scaleYBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void cancel(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            ViewPropertyAnimatorCompatICS.cancel(view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void x(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.x(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void xBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.xBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void y(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.y(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void yBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.yBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.translationXBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatICS.translationYBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void start(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            ViewPropertyAnimatorCompatICS.start(view);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
            view.setTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID, viewPropertyAnimatorListener);
            ViewPropertyAnimatorCompatICS.setListener(view, new MyVpaListener(viewPropertyAnimatorCompat));
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withEndAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable) {
            ViewPropertyAnimatorCompatICS.setListener(view, new MyVpaListener(viewPropertyAnimatorCompat));
            viewPropertyAnimatorCompat.mEndAction = runnable;
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withStartAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable) {
            ViewPropertyAnimatorCompatICS.setListener(view, new MyVpaListener(viewPropertyAnimatorCompat));
            viewPropertyAnimatorCompat.mStartAction = runnable;
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withLayer(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            viewPropertyAnimatorCompat.mOldLayerType = ViewCompat.getLayerType(view);
            ViewPropertyAnimatorCompatICS.setListener(view, new MyVpaListener(viewPropertyAnimatorCompat));
        }

        /* loaded from: classes.dex */
        static class MyVpaListener implements ViewPropertyAnimatorListener {
            ViewPropertyAnimatorCompat mVpa;

            MyVpaListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) {
                this.mVpa = viewPropertyAnimatorCompat;
            }

            @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorListener
            public void onAnimationStart(View view) {
                if (this.mVpa.mOldLayerType >= 0) {
                    ViewCompat.setLayerType(view, 2, null);
                }
                if (this.mVpa.mStartAction != null) {
                    this.mVpa.mStartAction.run();
                }
                Object tag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                ViewPropertyAnimatorListener viewPropertyAnimatorListener = tag instanceof ViewPropertyAnimatorListener ? (ViewPropertyAnimatorListener) tag : null;
                if (viewPropertyAnimatorListener != null) {
                    viewPropertyAnimatorListener.onAnimationStart(view);
                }
            }

            @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorListener
            public void onAnimationEnd(View view) {
                if (this.mVpa.mOldLayerType >= 0) {
                    ViewCompat.setLayerType(view, this.mVpa.mOldLayerType, null);
                    this.mVpa.mOldLayerType = -1;
                }
                if (this.mVpa.mEndAction != null) {
                    this.mVpa.mEndAction.run();
                }
                Object tag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                ViewPropertyAnimatorListener viewPropertyAnimatorListener = tag instanceof ViewPropertyAnimatorListener ? (ViewPropertyAnimatorListener) tag : null;
                if (viewPropertyAnimatorListener != null) {
                    viewPropertyAnimatorListener.onAnimationEnd(view);
                }
            }

            @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorListener
            public void onAnimationCancel(View view) {
                Object tag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                ViewPropertyAnimatorListener viewPropertyAnimatorListener = tag instanceof ViewPropertyAnimatorListener ? (ViewPropertyAnimatorListener) tag : null;
                if (viewPropertyAnimatorListener != null) {
                    viewPropertyAnimatorListener.onAnimationCancel(view);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    static class JBViewPropertyAnimatorCompatImpl extends ICSViewPropertyAnimatorCompatImpl {
        JBViewPropertyAnimatorCompatImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
            ViewPropertyAnimatorCompatJB.setListener(view, viewPropertyAnimatorListener);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withStartAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable) {
            ViewPropertyAnimatorCompatJB.withStartAction(view, runnable);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withEndAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable) {
            ViewPropertyAnimatorCompatJB.withEndAction(view, runnable);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withLayer(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            ViewPropertyAnimatorCompatJB.withLayer(view);
        }
    }

    /* loaded from: classes.dex */
    static class JBMr2ViewPropertyAnimatorCompatImpl extends JBViewPropertyAnimatorCompatImpl {
        JBMr2ViewPropertyAnimatorCompatImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public Interpolator getInterpolator(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view) {
            return ViewPropertyAnimatorCompatJellybeanMr2.getInterpolator(view);
        }
    }

    /* loaded from: classes.dex */
    static class KitKatViewPropertyAnimatorCompatImpl extends JBMr2ViewPropertyAnimatorCompatImpl {
        KitKatViewPropertyAnimatorCompatImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setUpdateListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorUpdateListener viewPropertyAnimatorUpdateListener) {
            ViewPropertyAnimatorCompatKK.setUpdateListener(view, viewPropertyAnimatorUpdateListener);
        }
    }

    /* loaded from: classes.dex */
    static class LollipopViewPropertyAnimatorCompatImpl extends KitKatViewPropertyAnimatorCompatImpl {
        LollipopViewPropertyAnimatorCompatImpl() {
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationZ(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatLollipop.translationZ(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationZBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatLollipop.translationZBy(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void z(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatLollipop.z(view, f);
        }

        @Override // com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, com.dcloud.android.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void zBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f) {
            ViewPropertyAnimatorCompatLollipop.zBy(view, f);
        }
    }

    static {
        ViewPropertyAnimatorCompatImpl impl1;
        impl1 = new LollipopViewPropertyAnimatorCompatImpl();
        impl1 = new KitKatViewPropertyAnimatorCompatImpl();
        impl1 = new JBMr2ViewPropertyAnimatorCompatImpl();
        impl1 = new JBViewPropertyAnimatorCompatImpl();
        IMPL = impl1;
    }

    public ViewPropertyAnimatorCompat setDuration(long j) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setDuration(this, view, j);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat alpha(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.alpha(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat alphaBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.alphaBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationX(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationX(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationY(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationY(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withEndAction(Runnable runnable) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.withEndAction(this, view, runnable);
        }
        return this;
    }

    public long getDuration() {
        View view = this.mView.get();
        if (view != null) {
            return IMPL.getDuration(this, view);
        }
        return 0L;
    }

    public ViewPropertyAnimatorCompat setInterpolator(Interpolator interpolator) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setInterpolator(this, view, interpolator);
        }
        return this;
    }

    public Interpolator getInterpolator() {
        View view = this.mView.get();
        if (view != null) {
            return IMPL.getInterpolator(this, view);
        }
        return null;
    }

    public ViewPropertyAnimatorCompat setStartDelay(long j) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setStartDelay(this, view, j);
        }
        return this;
    }

    public long getStartDelay() {
        View view = this.mView.get();
        if (view != null) {
            return IMPL.getStartDelay(this, view);
        }
        return 0L;
    }

    public ViewPropertyAnimatorCompat rotation(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotation(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationX(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationX(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationXBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationXBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationY(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationY(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationYBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationYBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleX(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.scaleX(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleXBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.scaleXBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleY(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.scaleY(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleYBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.scaleYBy(this, view, f);
        }
        return this;
    }

    public void cancel() {
        View view = this.mView.get();
        if (view != null) {
            IMPL.cancel(this, view);
        }
    }

    public ViewPropertyAnimatorCompat x(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.x(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat xBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.xBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat y(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.y(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat yBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.yBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationXBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationXBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationYBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationYBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationZBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationZBy(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationZ(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationZ(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat z(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.z(this, view, f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat zBy(float f) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.zBy(this, view, f);
        }
        return this;
    }

    public void start() {
        View view = this.mView.get();
        if (view != null) {
            IMPL.start(this, view);
        }
    }

    public ViewPropertyAnimatorCompat withLayer() {
        View view = this.mView.get();
        if (view != null) {
            IMPL.withLayer(this, view);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withStartAction(Runnable runnable) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.withStartAction(this, view, runnable);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setListener(ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setListener(this, view, viewPropertyAnimatorListener);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setUpdateListener(ViewPropertyAnimatorUpdateListener viewPropertyAnimatorUpdateListener) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setUpdateListener(this, view, viewPropertyAnimatorUpdateListener);
        }
        return this;
    }
}
