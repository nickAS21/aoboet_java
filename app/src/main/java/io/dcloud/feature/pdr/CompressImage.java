package io.dcloud.feature.pdr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;

/* compiled from: CompressImage.java */
/* loaded from: classes.dex old a*/
public class CompressImage {
    public static void compressAsync(final IWebview iWebview, final String[] params) {
        ThreadPool.self().addThreadTask(new Runnable() {
            @Override
            public void run() {
                performCompression(iWebview, params);
            }
        });
    }

    public static synchronized void performCompression(IWebview iWebview, String[] params) {
        // Перевірка кількості параметрів: [0] — вхідний шлях, [1] — вихідний шлях
        if (params == null || params.length < 2) {
            iWebview.evalJS("alert('Insufficient parameters for compression.')");
            return;
        }

        String inputPath = params[0];
        String outputPath = params[1];

        try {
            // Завантаження зображення з файлу
            Bitmap bitmap = BitmapFactory.decodeFile(inputPath);
            if (bitmap == null) {
                iWebview.evalJS("alert('Failed to load the image for compression.')");
                return;
            }

            // Стиснення і збереження в JPEG форматі з якістю 70%
            FileOutputStream out = new FileOutputStream(outputPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

            // Виклик JavaScript-функції з результатом
            iWebview.evalJS("window.onImageCompressed && window.onImageCompressed('" + outputPath + "');");

        } catch (Exception e) {
            // Обробка помилки
            String escaped = e.getMessage() != null ? e.getMessage().replace("'", "\\'") : "Unknown error";
            iWebview.evalJS("alert('Compression failed: " + escaped + "')");
        }
    }
    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CompressImage.java */
    /* loaded from: classes.dex */
    public static class b {
        String a;
        String b;
        String c;
        boolean d;
        int e;
        float f;
        float g;
        int h;
        int i;
        int j;
        C0007a k;
        boolean l = false;
        long m;

        b() {
        }

        public boolean a(JSONObject jSONObject, IWebview iWebview, String str) {
            this.a = jSONObject.optString(IApp.ConfigProperty.CONFIG_SRC);
            this.b = jSONObject.optString("dst");
            if (!a(iWebview, str)) {
                return false;
            }
            this.d = jSONObject.optBoolean("overwrite", false);
            this.c = jSONObject.optString(AbsoluteConst.JSON_KEY_FORMAT);
            this.e = jSONObject.optInt("quality", -1);
            a(jSONObject.optString(AbsoluteConst.JSON_KEY_WIDTH, "auto"), jSONObject.optString("height", "auto"));
            this.h = jSONObject.optInt("rotate", -1);
            JSONObject optJSONObject = jSONObject.optJSONObject("clip");
            if (optJSONObject == null) {
                return true;
            }
            C0007a c0007a = new C0007a(optJSONObject.optString("top"), optJSONObject.optString("left"), optJSONObject.optString(AbsoluteConst.JSON_KEY_WIDTH), optJSONObject.optString("height"), this.f, this.g);
            this.k = c0007a;
            if (c0007a.a()) {
                return true;
            }
            CompressImage.a(iWebview, str, DOMException.MSG_PARAMETER_ERROR, -1);
            return false;
        }

        public boolean a(IWebview iWebview, String str) {
            if (!TextUtils.isEmpty(this.a) && !TextUtils.isEmpty(this.b)) {
                String convert2AbsFullPath = iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), this.a);
                this.a = convert2AbsFullPath;
                if (!CompressImage.a(convert2AbsFullPath)) {
                    CompressImage.a(iWebview, str, DOMException.MSG_FILE_NOT_EXIST, -4);
                    return false;
                }
                this.b = iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), this.b);
                return true;
            }
            CompressImage.a(iWebview, str, DOMException.MSG_PARAMETER_ERROR, -1);
            return false;
        }

        public void a(String str, String str2) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(this.a, options);
            options.inJustDecodeBounds = false;
            this.i = options.outWidth;
            this.j = options.outHeight;
            this.m = new File(this.a).length();
            if (str.equals("auto") && str2.endsWith("auto")) {
                this.l = false;
                this.f = this.i;
                this.g = this.j;
                return;
            }
            if (str.equals("auto")) {
                this.l = true;
                int i = this.j;
                float parseFloat = PdrUtil.parseFloat(str2, i, i);
                this.g = parseFloat;
                this.f = (this.i * parseFloat) / this.j;
                return;
            }
            if (str2.equals("auto")) {
                this.l = true;
                int i2 = this.i;
                float parseFloat2 = PdrUtil.parseFloat(str, i2, i2);
                this.f = parseFloat2;
                this.g = (this.j * parseFloat2) / this.i;
                return;
            }
            this.l = true;
            int i3 = this.i;
            this.f = PdrUtil.parseFloat(str, i3, i3);
            int i4 = this.j;
            this.g = PdrUtil.parseFloat(str2, i4, i4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CompressImage.java */
    /* renamed from: io.dcloud.feature.pdr.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0007a {
        float a;
        float b;
        float c;
        float d;
        float e;
        float f;

        public C0007a(String str, String str2, String str3, String str4, float f, float f2) {
            this.e = f;
            this.f = f2;
            this.a = CompressImage.a(str, f2, 0.0f);
            float a = CompressImage.a(str2, this.e, 0.0f);
            this.b = a;
            float f3 = this.e;
            this.c = CompressImage.a(str3, f3, f3 - a);
            float f4 = this.f;
            float a2 = CompressImage.a(str4, f4, f4 - this.a);
            this.d = a2;
            float f5 = this.c;
            float f6 = this.b;
            float f7 = f5 + f6;
            float f8 = this.e;
            if (f7 > f8) {
                this.c = f8 - f6;
            }
            float f9 = this.a;
            float f10 = a2 + f9;
            float f11 = this.f;
            if (f10 > f11) {
                this.d = f11 - f9;
            }
        }

        public boolean a() {
            return this.a <= this.f && this.b <= this.e;
        }
    }

    public static void a(IWebview iWebview, String str, String str2, int i) {
        JSUtil.execCallback(iWebview, str, DOMException.toJSON(i, str2), JSUtil.ERROR, true, false);
    }

    public static void a(IWebview iWebview, String str, String str2) {
        JSUtil.execCallback(iWebview, str, str2, JSUtil.OK, true, false);
    }

    public static boolean a(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                if (file.length() >= 5) {
                    return true;
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static Bitmap.CompressFormat b(String str) {
        if (str.contains(".jpg") || str.contains(".jpeg")) {
            return Bitmap.CompressFormat.JPEG;
        }
        return Bitmap.CompressFormat.PNG;
    }

    public static long a(String str, Bitmap bitmap, boolean z, int i) {
        File file = new File(str);
        if (file.exists()) {
            if (file.length() < 1) {
                file.delete();
            } else {
                if (!z) {
                    return -1L;
                }
                file.delete();
            }
        } else if (c(str)) {
            file = new File(str);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(b(str), i, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return file.length();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1L;
        } catch (IOException e2) {
            e2.printStackTrace();
            return -1L;
        }
    }

    public static boolean c(String str) {
        int lastIndexOf;
        if (!TextUtils.isEmpty(str) && (lastIndexOf = str.lastIndexOf("/")) != -1 && lastIndexOf != 0) {
            try {
                File file = new File(str.substring(0, lastIndexOf));
                if (file.exists()) {
                    return true;
                }
                file.mkdirs();
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static float a(String str, float f, float f2) {
        return str.equals("auto") ? f2 : PdrUtil.parseFloat(str, f, f2);
    }
}
