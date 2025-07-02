package com.dcloud.android.graphics;

import android.os.Build;

/* loaded from: classes.dex */
public class Region extends android.graphics.Region {
    private int HOLD_SCREEN_COUNT;
    int fillScreenCounter;

    public Region() {
        this(0);
    }

    public Region(int i) {
        this.HOLD_SCREEN_COUNT = 2;
        this.fillScreenCounter = 1;
        if (Build.VERSION.SDK_INT >= 21) {
            this.HOLD_SCREEN_COUNT = 1;
        } else {
            this.HOLD_SCREEN_COUNT = i;
        }
    }

    boolean fillWholeScreen() {
        return this.fillScreenCounter == this.HOLD_SCREEN_COUNT;
    }

    void count() {
        this.fillScreenCounter++;
    }
}
