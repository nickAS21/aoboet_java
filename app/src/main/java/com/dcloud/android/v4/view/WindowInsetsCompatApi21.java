package com.dcloud.android.v4.view;

import android.graphics.Rect;
import android.view.WindowInsets;

/* loaded from: classes.dex */
class WindowInsetsCompatApi21 extends WindowInsetsCompat {
    private final WindowInsets mSource;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowInsetsCompatApi21(WindowInsets windowInsets) {
        this.mSource = windowInsets;
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public int getSystemWindowInsetLeft() {
        return this.mSource.getSystemWindowInsetLeft();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public int getSystemWindowInsetTop() {
        return this.mSource.getSystemWindowInsetTop();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public int getSystemWindowInsetRight() {
        return this.mSource.getSystemWindowInsetRight();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public int getSystemWindowInsetBottom() {
        return this.mSource.getSystemWindowInsetBottom();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public boolean hasSystemWindowInsets() {
        return this.mSource.hasSystemWindowInsets();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public boolean hasInsets() {
        return this.mSource.hasInsets();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public boolean isConsumed() {
        return this.mSource.isConsumed();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public boolean isRound() {
        return this.mSource.isRound();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public WindowInsetsCompat consumeSystemWindowInsets() {
        return new WindowInsetsCompatApi21(this.mSource.consumeSystemWindowInsets());
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public WindowInsetsCompat replaceSystemWindowInsets(int i, int i2, int i3, int i4) {
        return new WindowInsetsCompatApi21(this.mSource.replaceSystemWindowInsets(i, i2, i3, i4));
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public WindowInsetsCompat replaceSystemWindowInsets(Rect rect) {
        return new WindowInsetsCompatApi21(this.mSource.replaceSystemWindowInsets(rect));
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public int getStableInsetTop() {
        return this.mSource.getStableInsetTop();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public int getStableInsetLeft() {
        return this.mSource.getStableInsetLeft();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public int getStableInsetRight() {
        return this.mSource.getStableInsetRight();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public int getStableInsetBottom() {
        return this.mSource.getStableInsetBottom();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public boolean hasStableInsets() {
        return this.mSource.hasStableInsets();
    }

    @Override // com.dcloud.android.v4.view.WindowInsetsCompat
    public WindowInsetsCompat consumeStableInsets() {
        return new WindowInsetsCompatApi21(this.mSource.consumeStableInsets());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowInsets unwrap() {
        return this.mSource;
    }
}
