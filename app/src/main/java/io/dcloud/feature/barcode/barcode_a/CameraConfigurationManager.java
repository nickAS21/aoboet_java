package io.dcloud.feature.barcode.barcode_a;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;

import java.util.regex.Pattern;

import io.dcloud.common.util.JSUtil;
import io.dcloud.feature.barcode.view.DetectorViewConfig;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CameraConfigurationManager.java */
/* loaded from: classes.dex  old b*/
public final class CameraConfigurationManager {
    private static final String a = "b";
    private static final Pattern b = Pattern.compile(JSUtil.COMMA);
    private final Context c;
    private Point d;
    private int e;
    private String f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraConfigurationManager(Context context) {
        this.c = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        this.e = parameters.getPreviewFormat();
        this.f = parameters.get("preview-format");
        Log.d(a, "Default preview format: " + this.e + '/' + this.f);
        Rect rect = DetectorViewConfig.getInstance().gatherRect;
        this.d = a(parameters, new Point(rect.width(), rect.height()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Log.d(a, "Setting preview size: " + this.d);
        parameters.setPreviewSize(this.d.x, this.d.y);
        a(parameters);
        b(parameters);
        camera.setParameters(parameters);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Point a() {
        return this.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String c() {
        return this.f;
    }

    public static Point a(Camera.Parameters parameters, Point point) {
        String str = parameters.get("preview-size-values");
        if (str == null) {
            str = parameters.get("preview-size-value");
        }
        Point point2 = null;
        if (str != null) {
            Log.d(a, "preview-size-values parameter: " + str);
            point2 = a(str, point);
        }
        return point2 == null ? new Point((point.x >> 3) << 3, (point.y >> 3) << 3) : point2;
    }

    private static Point a(CharSequence charSequence, Point point) {
        String[] split = b.split(charSequence);
        int length = split.length;
        int i = Integer.MAX_VALUE;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            String trim = split[i2].trim();
            int indexOf = trim.indexOf(120);
            if (indexOf < 0) {
                Log.w(a, "Bad preview-size: " + trim);
            } else {
                try {
                    int parseInt = Integer.parseInt(trim.substring(0, indexOf));
                    int parseInt2 = Integer.parseInt(trim.substring(indexOf + 1));
                    int abs = Math.abs(parseInt - point.x) + Math.abs(parseInt2 - point.y);
                    if (abs == 0) {
                        i4 = parseInt2;
                        i3 = parseInt;
                        break;
                    }
                    if (abs < i) {
                        i4 = parseInt2;
                        i = abs;
                        i3 = parseInt;
                    }
                } catch (NumberFormatException unused) {
                    Log.w(a, "Bad preview-size: " + trim);
                }
            }
            i2++;
        }
        if (i3 <= 0 || i4 <= 0) {
            return null;
        }
        return new Point(i3, i4);
    }

    private static int a(CharSequence charSequence, int i) {
        int i2 = 0;
        for (String str : b.split(charSequence)) {
            try {
                double parseDouble = Double.parseDouble(str.trim());
                int i3 = (int) (10.0d * parseDouble);
                if (Math.abs(i - parseDouble) < Math.abs(i - i2)) {
                    i2 = i3;
                }
            } catch (NumberFormatException unused) {
                return i;
            }
        }
        return i2;
    }

    private void a(Camera.Parameters parameters) {
        if (Build.MODEL.contains("Behold II") && CameraManager.c == 3) {
            parameters.set("flash-value", 1);
        } else {
            parameters.set("flash-value", 2);
        }
        parameters.set("flash-mode", "off");
    }

    private void b(Camera.Parameters parameters) {
        String str = parameters.get("zoom-supported");
        if (str == null || Boolean.parseBoolean(str)) {
            int i = 27;
            String str2 = parameters.get("max-zoom");
            if (str2 != null) {
                try {
                    int parseDouble = (int) (Double.parseDouble(str2) * 10.0d);
                    if (27 > parseDouble) {
                        i = parseDouble;
                    }
                } catch (NumberFormatException unused) {
                    Log.w(a, "Bad max-zoom: " + str2);
                }
            }
            String str3 = parameters.get("taking-picture-zoom-max");
            if (str3 != null) {
                try {
                    int parseInt = Integer.parseInt(str3);
                    if (i > parseInt) {
                        i = parseInt;
                    }
                } catch (NumberFormatException unused2) {
                    Log.w(a, "Bad taking-picture-zoom-max: " + str3);
                }
            }
            String str4 = parameters.get("mot-zoom-values");
            if (str4 != null) {
                i = a(str4, i);
            }
            String str5 = parameters.get("mot-zoom-step");
            if (str5 != null) {
                try {
                    int parseDouble2 = (int) (Double.parseDouble(str5.trim()) * 10.0d);
                    if (parseDouble2 > 1) {
                        i -= i % parseDouble2;
                    }
                } catch (NumberFormatException unused3) {
                }
            }
            if (str2 != null || str4 != null) {
                parameters.set("zoom", String.valueOf(i / 10.0d));
            }
            if (str3 != null) {
                parameters.set("taking-picture-zoom", i);
            }
        }
    }
}
