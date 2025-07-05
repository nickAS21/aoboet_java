package com.dcloud.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import io.dcloud.common.adapter.util.CanvasHelper;

/* loaded from: classes.dex */
public class RoundAngleImageView extends androidx.appcompat.widget.AppCompatImageView {
    Path path;

    public RoundAngleImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.path = new Path();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.save();
        this.path.addRoundRect(new RectF(0.0f, 0.0f, getWidth(), getHeight()), CanvasHelper.dip2px(getContext(), 8.0f), CanvasHelper.dip2px(getContext(), 8.0f), Path.Direction.CCW);
        canvas.clipPath(this.path);
        if (getDrawable() == null) {
            canvas.drawColor(-3355444);
        }
        super.onDraw(canvas);
        canvas.restore();
    }
}
