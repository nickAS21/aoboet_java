package io.dcloud.common.common_b.common_b_b;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;

/* compiled from: AndroidBug5497Workaround.java */
/* loaded from: classes.dex */
public class AndroidBug5497Workaround {
    private static AndroidBug5497Workaround a;
    private View b;
    private int c;
    private FrameLayout.LayoutParams d;
    private boolean e;

    public static void a(FrameLayout frameLayout) {
        if (a == null) {
            a = new AndroidBug5497Workaround(frameLayout);
        }
    }

    private AndroidBug5497Workaround(FrameLayout frameLayout) {
        this.e = false;
        this.b = frameLayout;
        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: io.dcloud.common.b.b.a.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                AndroidBug5497Workaround.this.b();
            }
        });
        this.d = (FrameLayout.LayoutParams) this.b.getLayoutParams();
        this.e = AndroidResources.checkImmersedStatusBar(frameLayout.getContext());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        int c = c();
        if (c != this.c) {
            int height = this.b.getRootView().getHeight();
            int i = height - c;
            if (i > height / 4) {
                this.d.height = (height - i) + (this.e ? DeviceInfo.sStatusBarHeight : 0);
            } else {
                this.d.height = height;
            }
            this.b.requestLayout();
            this.c = c;
        }
    }

    private int c() {
        Rect rect = new Rect();
        this.b.getWindowVisibleDisplayFrame(rect);
        return rect.bottom - rect.top;
    }

    public static void a() {
        a = null;
    }
}
