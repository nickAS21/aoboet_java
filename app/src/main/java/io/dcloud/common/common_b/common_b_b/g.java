package io.dcloud.common.common_b.common_b_b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.DeviceInfo;

/* compiled from: DebugView.java */
/* loaded from: classes.dex */
class g extends AdaFrameItem {
    static g i;
    Bitmap a;
    int b;
    int c;
    int d;
    int e;
    int f;
    int g;
    Paint h;

    public g(Context context) {
        super(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void paint(Canvas canvas) {
        if (DeviceInfo.sStatusBarHeight == 0) {
            Rect rect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            DeviceInfo.sStatusBarHeight = rect.top;
        }
        int i2 = (this.g - DeviceInfo.sStatusBarHeight) - this.e;
        this.c = i2;
        canvas.drawBitmap(this.a, this.b, i2, this.h);
    }

    private void b() {
        this.d = this.a.getWidth();
        int height = this.a.getHeight();
        this.e = height;
        this.b = this.f - this.d;
        this.c -= height;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void onResize() {
        b();
    }

    public static void a() {
        g gVar = i;
        if (gVar != null) {
            gVar.dispose();
            i = null;
        }
    }
}
