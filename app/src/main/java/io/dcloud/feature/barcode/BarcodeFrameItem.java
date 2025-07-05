package io.dcloud.feature.barcode;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import androidx.core.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.barcode.barcode_a.CameraManager;
import io.dcloud.feature.barcode.barcode_b.CaptureActivityHandler;
import io.dcloud.feature.barcode.barcode_b.IBarHandler;
import io.dcloud.feature.barcode.barcode_b.InactivityTimer;
import io.dcloud.feature.barcode.view.DetectorViewConfig;
import io.dcloud.feature.barcode.view.ViewfinderView;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BarcodeFrameItem.java */
/* loaded from: classes.dex  old a*/
public class BarcodeFrameItem extends AdaFrameItem implements SurfaceHolder.Callback, IBarHandler {
    static BarcodeFrameItem i;
    boolean a;
    boolean b;
    SurfaceView surfaceView;
    String d;
    public String e;
    boolean f;
    String g;
    BarcodeProxy proxy;
    boolean j;
    int requestedOrientation;
    private CaptureActivityHandler l;
    private ViewfinderView m;
    private boolean n;
    private Vector<BarcodeFormat> o;
    private String p;
    private InactivityTimer q;
    private MediaPlayer r;
    private Context s;
    private IWebview webview;
    private IApp iapp;
    private boolean v;
    private boolean w;
    private Bitmap x;
    private final MediaPlayer.OnCompletionListener completionListener;

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder mHolder, int format, int mWidth, int mHeight) {
        if (mHolder.getSurface() == null) {
            // surface ще не створено
            return;
        }

        try {
            // Зупинити попередній превʼю
            CameraManager.a().b(); // stopPreview(), або свій метод
        } catch (Exception e) {
            // Якщо превʼю ще не стартувало — нічого страшного
            e.printStackTrace();
        }

        try {
            // Знову привʼязати Surface та запустити превʼю
            CameraManager.a().a(mHolder);   // setPreviewDisplay(holder) + startPreview()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:12:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x00e5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public BarcodeFrameItem(BarcodeProxy proxy, io.dcloud.common.DHInterface.IWebview webview, android.widget.AbsoluteLayout.LayoutParams layoutParams, org.json.JSONArray formatsArray, org.json.JSONObject configJson) {
        super(webview.getActivity());
        Activity activity = webview.getActivity();
        this.proxy = proxy;
        this.webview = webview;
        this.configureSupportedFormats(formatsArray);
        this.applyScanStyle(configJson, activity.getWindow().getDecorView());
        this.completionListener = createDefaultCompletionListener();
        this.surfaceView = new SurfaceView(activity);
        this.surfaceView.getHolder().addCallback(this);
//        this.getRootView().addView(this.surfaceView, layoutParams);
        // тимчасовий костиль): додавати напряму в Activity
        // Але тоді surfaceView буде "не всередині" твоєї BarcodeFrameItem, а глобально в Activity. Це не завжди прийнятно — UI буде «змішаний».
        ViewGroup root = activity.findViewById(android.R.id.content);
        root.addView(this.surfaceView, layoutParams);

    }
        /*
            Method dump skipped, instructions count: 267
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
//        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.barcode.a.<init>(io.dcloud.feature.barcode.b, io.dcloud.common.DHInterface.IWebview, android.widget.AbsoluteLayout$LayoutParams, org.json.JSONArray, org.json.JSONObject):void");
//    }

        private void applyScanStyle(JSONObject jSONObject, View view) {
        DetectorViewConfig.laserColor = -65536;
        DetectorViewConfig.cornerColor = -65536;
        if (!TextUtils.isEmpty(jSONObject.optString("scanbarColor"))) {
            int stringToColor = PdrUtil.stringToColor(jSONObject.optString("scanbarColor"));
            if (stringToColor == -1) {
                stringToColor = DetectorViewConfig.laserColor;
            }
            DetectorViewConfig.laserColor = stringToColor;
        }
        if (!TextUtils.isEmpty(jSONObject.optString("frameColor"))) {
            int stringToColor2 = PdrUtil.stringToColor(jSONObject.optString("frameColor"));
            if (stringToColor2 == -1) {
                stringToColor2 = DetectorViewConfig.laserColor;
            }
            DetectorViewConfig.cornerColor = stringToColor2;
        }
        if (TextUtils.isEmpty(jSONObject.optString("background"))) {
            return;
        }
        int stringToColor3 = PdrUtil.stringToColor(jSONObject.optString("background"));
        if (stringToColor3 == -1) {
            stringToColor3 = DetectorViewConfig.laserColor;
        }
        view.setBackgroundColor(stringToColor3);
    }

    private void a(IWebview iWebview) {
        iWebview.obtainFrameView().addFrameViewListener(new IEventCallback() { // from class: io.dcloud.feature.barcode.a.1
            @Override // io.dcloud.common.DHInterface.IEventCallback
            public Object onCallBack(String str, Object obj) {
                if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WEBVIEW_HIDE) || PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE)) {
                    BarcodeFrameItem.this.a();
                    return null;
                }
                if (!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_SHOW_ANIMATION_END)) {
                    return null;
                }
                BarcodeFrameItem.this.a(true);
                return null;
            }
        });
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void dispose() {
        super.dispose();
        Logger.d(IFeature.F_BARCODE, "dispose");
        a();
        DetectorViewConfig.clearData();
        this.proxy.barcodeFrameItem = null;
        this.surfaceView = null;
        Bitmap bitmap = this.x;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.x.recycle();
            this.x = null;
        }
        CameraManager.a().c();
        setRequestedOrientation();
    }

    private void getRequestedOrientation() {
        this.requestedOrientation = this.iapp.getRequestedOrientation();
    }

    private void setRequestedOrientation() {
        this.iapp.setRequestedOrientation(this.requestedOrientation);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(boolean z) {
        SurfaceHolder holder = this.surfaceView.getHolder();
        if (this.x != null && this.w && z) {
            this.surfaceView.setBackground(new BitmapDrawable(this.s.getResources(), this.x));
        }
        if (this.n) {
            addView(holder);
        } else {
            holder.addCallback(this);
            holder.setType(3);
        }
        if (((AudioManager) this.s.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() != 2) {
            this.a = false;
        }
        i();
        if (z && this.v) {
            this.v = false;
            b();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a() {
        CaptureActivityHandler bVar = this.l;
        if (bVar != null) {
            bVar.b();
            this.l = null;
        }
        CameraManager.a().e();
        boolean z = this.v;
        c();
        this.v = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void b() {
        if (this.v) {
            return;
        }
        getViewfinderView().b();
        CaptureActivityHandler bVar = this.l;
        if (bVar != null) {
            bVar.c();
        } else {
            a(false);
        }
        if (this.w) {
            this.surfaceView.setBackground(null);
            Bitmap bitmap = this.x;
            if (bitmap != null && !bitmap.isRecycled()) {
                this.x.recycle();
                this.x = null;
            }
            CameraManager.a().c();
            this.surfaceView.postInvalidate();
            addView(this.surfaceView.getHolder());
        }
        this.v = true;
        this.w = false;
    }

    public void b(boolean z) {
        CameraManager.a().a(z);
    }

    protected void c() {
        if (this.v) {
            CaptureActivityHandler bVar = this.l;
            if (bVar != null) {
                bVar.e();
            }
            getViewfinderView().a();
            this.v = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void d() {
        if (this.v) {
            CaptureActivityHandler bVar = this.l;
            if (bVar != null) {
                bVar.b();
                this.l = null;
            }
            getViewfinderView().a();
            CameraManager.a().h();
            CameraManager.a().g();
            byte[] b = CameraManager.a().b();
            Camera d = CameraManager.a().d();
            if (b != null && d != null) {
                this.x = a(b, d);
            }
            CameraManager.a().e();
            this.v = false;
            this.w = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void disposeAndCleanup() {
        dispose();
        setMainView(null);
        System.gc();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void f() {
        this.q.b();
        this.n = false;
        this.o = null;
        this.p = null;
    }

    private void addView(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.a().a(surfaceHolder);
            CaptureActivityHandler bVar = this.l;
            if (bVar == null) {
                CaptureActivityHandler bVar2 = new CaptureActivityHandler(this, this.o, this.p);
                this.l = bVar2;
                if (!this.v || bVar2 == null) {
                    return;
                }
                bVar2.c();
                return;
            }
            bVar.a();
        } catch (IOException e) {
            this.e = e.getMessage();
        } catch (RuntimeException e2) {
            this.e = e2.getMessage();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (this.n) {
            return;
        }
        this.n = true;
        if (this.w) {
            return;
        }
        addView(surfaceHolder);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.n = false;
    }

    @Override // io.dcloud.feature.barcode.b.g
    public ViewfinderView getViewfinderView() {
        return this.m;
    }

    @Override // io.dcloud.feature.barcode.b.g
    public Handler getHandler() {
        return this.l;
    }

    @Override // io.dcloud.feature.barcode.b.g
    public boolean isRunning() {
        return this.v;
    }

    @Override // io.dcloud.feature.barcode.b.g
    public void drawViewfinder() {
        this.m.c();
    }

    @Override // io.dcloud.feature.barcode.b.g
    public void handleDecode(Result result, Bitmap bitmap) {
        String format;
        this.q.a();
        j();
        boolean saveBitmapToFile = this.f ? PdrUtil.saveBitmapToFile(bitmap, this.g) : false;
        int a = a(result.getBarcodeFormat());
        if (saveBitmapToFile) {
            String obtainAppDocPath = this.webview.obtainFrameView().obtainApp().obtainAppDocPath();
            Logger.d("doc:" + obtainAppDocPath);
            if (this.g.startsWith(obtainAppDocPath)) {
                this.g = BaseInfo.REL_PRIVATE_DOC_DIR + this.g.substring(obtainAppDocPath.length() - 1);
            }
            String convert2RelPath = this.webview.obtainFrameView().obtainApp().convert2RelPath(this.g);
            Logger.d("Filename:" + this.g + ";relPath:" + convert2RelPath);
            format = String.format("{type:%d,message:%s,file:'%s'}", Integer.valueOf(a), JSONUtil.toJSONableString(result.getText()), convert2RelPath);
        } else {
            format = String.format("{type:%d,message:%s}", Integer.valueOf(a), JSONUtil.toJSONableString(result.getText()));
        }
        JSUtil.execCallback(this.webview, this.d, format, JSUtil.OK, true, true);
        c();
    }

    private void i() {
        if (this.r == null) {
            getActivity().setVolumeControlStream(3);
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.r = mediaPlayer;
            mediaPlayer.setAudioStreamType(3);
            this.r.setOnCompletionListener(this.completionListener);
            try {
                AssetFileDescriptor openFd = this.s.getResources().getAssets().openFd(AbsoluteConst.RES_BEEP);
                this.r.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                openFd.close();
                this.r.setVolume(0.8f, 0.8f);
                this.r.prepare();
            } catch (IOException unused) {
                this.r = null;
            }
        }
    }

    private void j() {
        MediaPlayer mediaPlayer;
        if (this.a && (mediaPlayer = this.r) != null) {
            mediaPlayer.start();
        }
        if (this.b) {
            ((Vibrator) this.s.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(200L);
        }
    }

    private int a(BarcodeFormat barcodeFormat) {
        if (barcodeFormat == BarcodeFormat.QR_CODE) {
            return 0;
        }
        if (barcodeFormat == BarcodeFormat.EAN_13) {
            return 1;
        }
        if (barcodeFormat == BarcodeFormat.EAN_8) {
            return 2;
        }
        if (barcodeFormat == BarcodeFormat.AZTEC) {
            return 3;
        }
        if (barcodeFormat == BarcodeFormat.DATA_MATRIX) {
            return 4;
        }
        if (barcodeFormat == BarcodeFormat.UPC_A) {
            return 5;
        }
        if (barcodeFormat == BarcodeFormat.UPC_E) {
            return 6;
        }
        if (barcodeFormat == BarcodeFormat.CODABAR) {
            return 7;
        }
        if (barcodeFormat == BarcodeFormat.CODE_39) {
            return 8;
        }
        if (barcodeFormat == BarcodeFormat.CODE_93) {
            return 9;
        }
        if (barcodeFormat == BarcodeFormat.CODE_128) {
            return 10;
        }
        if (barcodeFormat == BarcodeFormat.ITF) {
            return 11;
        }
        if (barcodeFormat == BarcodeFormat.MAXICODE) {
            return 12;
        }
        if (barcodeFormat == BarcodeFormat.PDF_417) {
            return 13;
        }
        if (barcodeFormat == BarcodeFormat.RSS_14) {
            return 14;
        }
        if (barcodeFormat == BarcodeFormat.RSS_EXPANDED) {
            return 15;
        }
        return NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
    }

    private void configureSupportedFormats(JSONArray jSONArray) {
        int i2;
        this.o = new Vector<>();
        if (jSONArray == null || jSONArray.length() == 0) {
            this.o.add(BarcodeFormat.EAN_13);
            this.o.add(BarcodeFormat.EAN_8);
            this.o.add(BarcodeFormat.QR_CODE);
            return;
        }
        int length = jSONArray.length();
        for (int i3 = 0; i3 < length; i3++) {
            try {
                i2 = jSONArray.getInt(i3);
            } catch (JSONException e) {
                e.printStackTrace();
                i2 = -1;
            }
            if (i2 != -1) {
                this.o.add(a(i2));
            }
        }
    }

    private BarcodeFormat a(int i2) {
        switch (i2) {
            case 0:
                return BarcodeFormat.QR_CODE;
            case 1:
                return BarcodeFormat.EAN_13;
            case 2:
                return BarcodeFormat.EAN_8;
            case 3:
                return BarcodeFormat.AZTEC;
            case 4:
                return BarcodeFormat.DATA_MATRIX;
            case 5:
                return BarcodeFormat.UPC_A;
            case 6:
                return BarcodeFormat.UPC_E;
            case 7:
                return BarcodeFormat.CODABAR;
            case 8:
                return BarcodeFormat.CODE_39;
            case 9:
                return BarcodeFormat.CODE_93;
            case 10:
                return BarcodeFormat.CODE_128;
            case 11:
                return BarcodeFormat.ITF;
            case 12:
                return BarcodeFormat.MAXICODE;
            case 13:
                return BarcodeFormat.PDF_417;
            case 14:
                return BarcodeFormat.RSS_14;
            case 15:
                return BarcodeFormat.RSS_EXPANDED;
            default:
                return null;
        }
    }

    private Bitmap a(byte[] bArr, Camera camera) {
        Bitmap bitmap = null;
        try {
            Camera.Size previewSize = camera.getParameters().getPreviewSize();
            YuvImage yuvImage = new YuvImage(bArr, 17, previewSize.width, previewSize.height, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, byteArrayOutputStream);
            bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
            byteArrayOutputStream.close();
            Matrix matrix = new Matrix();
            matrix.postRotate(90.0f);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    private MediaPlayer.OnCompletionListener createDefaultCompletionListener() {
        return new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    mp.seekTo(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
