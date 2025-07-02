package io.dcloud.feature.barcode.view;

import android.graphics.Rect;

import io.dcloud.common.DHInterface.IReflectAble;

/* loaded from: classes.dex */
public class DetectorViewConfig implements IReflectAble {
    public static int CORNER_HEIGHT = 40;
    public static int CORNER_WIDTH = 8;
    public static final int F_CORNER_COLOR = -65536;
    public static final int F_LASER_COLOR = -65536;
    public static int LASER_WIDTH = 8;
    private static DetectorViewConfig b = null;
    public static int cornerColor = -65536;
    public static int detectorRectOffestLeft = 0;
    public static int detectorRectOffestTop = 0;
    public static int laserColor = -65536;
    public static int maskColor = 1610612736;
    public static int resultPointColor = -1056964864;
    public Rect surfaceViewRect = null;
    public Rect gatherRect = new Rect();
    private Rect a = null;

    private DetectorViewConfig() {
    }

    public static DetectorViewConfig getInstance() {
        if (b == null) {
            b = new DetectorViewConfig();
        }
        return b;
    }

    public static void clearData() {
        b = null;
    }

    public void initSurfaceViewRect(int i, int i2, int i3, int i4) {
        this.surfaceViewRect = new Rect(i, i2, i3 + i, i4 + i2);
    }

    public Rect getDetectorRect() {
        if (this.a == null) {
            int width = this.gatherRect.width() - CORNER_WIDTH;
            int height = this.gatherRect.height() - CORNER_WIDTH;
            int i = (width * 6) / 10;
            if (i < 240) {
                i = 240;
            } else if (i > 640) {
                i = 640;
            }
            CORNER_HEIGHT = (i * 10) / 100;
            int i2 = (width - i) / 2;
            int i3 = (height - i) / 2;
            this.a = new Rect(i2, i3, i2 + i, i + i3);
        }
        return this.a;
    }
}
