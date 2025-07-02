package io.dcloud.feature.barcode.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.AbsoluteLayout;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.feature.barcode.barcode_a.CameraManager;
import io.dcloud.feature.barcode.barcode_b.CaptureActivityHandler;
import io.dcloud.feature.barcode.barcode_b.IBarHandler;
import io.dcloud.feature.barcode.barcode_b.InactivityTimer;

/* loaded from: classes.dex */
public class BarcodeView extends AbsoluteLayout implements SurfaceHolder.Callback, IReflectAble, IBarHandler {
    static BarcodeView f;
    SurfaceView a;
    String b;
    boolean c;
    String d;
    Activity e;
    public String errorMsg;
    ScanListener g;
    private CaptureActivityHandler h;
    private ViewfinderView i;
    private boolean j;
    private Vector<BarcodeFormat> k;
    private String l;
    private InactivityTimer m;
    private MediaPlayer n;
    private boolean o;
    private boolean p;
    private boolean q;
    private final MediaPlayer.OnCompletionListener r;

    /* loaded from: classes.dex */
    public interface ScanListener extends IReflectAble {
        void onCompleted(String str, Bitmap bitmap);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public BarcodeView(Activity activity, Rect rect) {
        super(activity);
        int height;
        int width;
        this.b = null;
        this.q = false;
        this.errorMsg = null;
        this.c = false;
        this.d = null;
        this.r = new MediaPlayer.OnCompletionListener() { // from class: io.dcloud.feature.barcode.view.BarcodeView.1
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
            }
        };
        this.g = null;
        f = this;
        this.e = activity;
        this.a = new SurfaceView(activity);
        this.i = new ViewfinderView(activity, this);
        this.j = false;
        this.m = new InactivityTimer(activity);
        CameraManager.a(activity.getApplication());
        CameraManager.a = activity.getResources().getDisplayMetrics().widthPixels;
        CameraManager.b = activity.getResources().getDisplayMetrics().heightPixels;
        Rect rect2 = DetectorViewConfig.getInstance().gatherRect;
        Point a = CameraManager.a(rect2.width(), rect2.height());
        if (a == null) {
            return;
        }
        int width2 = rect.width();
        int i = (a.x * width2) / a.y;
        if (rect.top == 0 && i > rect.height()) {
            height = rect.height() - i;
            DetectorViewConfig.detectorRectOffestTop = height;
        } else {
            if (i > rect.height()) {
                i = rect.height();
                width2 = (a.y * i) / a.x;
                height = 0;
            } else {
                height = (rect.height() - i) / 2;
                DetectorViewConfig.detectorRectOffestTop = height;
            }
            if (rect.width() - width2 > 0) {
                width = (rect.width() - width2) / 2;
                DetectorViewConfig.detectorRectOffestLeft = width;
                this.a.setClickable(false);
                addView(this.a, new AbsoluteLayout.LayoutParams(width2, i, width, height));
                DetectorViewConfig.getInstance().initSurfaceViewRect(width, height, width2, i);
                addView(this.i);
                c();
                onResume(false);
            }
        }
        width = 0;
        this.a.setClickable(false);
        addView(this.a, new AbsoluteLayout.LayoutParams(width2, i, width, height));
        DetectorViewConfig.getInstance().initSurfaceViewRect(width, height, width2, i);
        addView(this.i);
        c();
        onResume(false);
    }

    public void autoFocus() {
        this.h.d();
    }

    public void dispose() {
        onPause();
        DetectorViewConfig.clearData();
        this.a = null;
    }

    public void onResume(boolean z) {
        SurfaceHolder holder = this.a.getHolder();
        if (this.j) {
            a(holder);
        } else {
            holder.addCallback(this);
            holder.setType(3);
        }
        this.o = true;
        if (((AudioManager) this.e.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() != 2) {
            this.o = false;
        }
        a();
        this.p = true;
        if (z && this.q) {
            this.q = false;
            start();
        }
    }

    public void onPause() {
        CaptureActivityHandler bVar = this.h;
        if (bVar != null) {
            bVar.b();
            this.h = null;
        }
        CameraManager.a().e();
        boolean z = this.q;
        cancel();
        this.q = z;
    }

    public void start() {
        if (this.q) {
            return;
        }
        getViewfinderView().b();
        CaptureActivityHandler bVar = this.h;
        if (bVar != null) {
            bVar.c();
        }
        this.q = true;
    }

    public void setFlash(boolean z) {
        CameraManager.a().a(z);
    }

    public void cancel() {
        if (this.q) {
            CaptureActivityHandler bVar = this.h;
            if (bVar != null) {
                bVar.e();
            }
            getViewfinderView().a();
            this.q = false;
        }
    }

    public void onDestroy() {
        this.m.b();
        this.j = false;
        this.k = null;
        this.l = null;
    }

    private void a(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.a().a(surfaceHolder);
            CaptureActivityHandler bVar = this.h;
            if (bVar == null) {
                CaptureActivityHandler bVar2 = new CaptureActivityHandler(this, this.k, this.l);
                this.h = bVar2;
                if (!this.q || bVar2 == null) {
                    return;
                }
                bVar2.c();
                return;
            }
            bVar.a();
        } catch (IOException e) {
            this.errorMsg = e.getMessage();
        } catch (RuntimeException e2) {
            this.errorMsg = e2.getMessage();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (this.j) {
            return;
        }
        this.j = true;
        a(surfaceHolder);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.j = false;
    }

    @Override // io.dcloud.feature.barcode.b.g
    public ViewfinderView getViewfinderView() {
        return this.i;
    }

    @Override // android.view.View, io.dcloud.feature.barcode.b.g
    public Handler getHandler() {
        return this.h;
    }

    @Override // io.dcloud.feature.barcode.b.g
    public boolean isRunning() {
        return this.q;
    }

    @Override // io.dcloud.feature.barcode.b.g
    public void drawViewfinder() {
        this.i.c();
    }

    @Override // io.dcloud.feature.barcode.b.g
    public void handleDecode(Result result, Bitmap bitmap) {
        this.m.a();
        b();
        ScanListener scanListener = this.g;
        if (scanListener != null) {
            scanListener.onCompleted(result.getText(), bitmap);
        }
    }

    private void a() {
        if (this.o && this.n == null) {
            this.e.setVolumeControlStream(3);
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.n = mediaPlayer;
            mediaPlayer.setAudioStreamType(3);
            this.n.setOnCompletionListener(this.r);
            try {
                AssetFileDescriptor openFd = this.e.getResources().getAssets().openFd(AbsoluteConst.RES_BEEP);
                this.n.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                openFd.close();
                this.n.setVolume(0.1f, 0.1f);
                this.n.prepare();
            } catch (IOException unused) {
                this.n = null;
            }
        }
    }

    private void b() {
        MediaPlayer mediaPlayer;
        if (this.o && (mediaPlayer = this.n) != null) {
            mediaPlayer.start();
        }
        if (this.p) {
            ((Vibrator) this.e.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(200L);
        }
    }

    public void setScanListener(ScanListener scanListener) {
        this.g = scanListener;
    }

    public ScanListener getScanListener() {
        return this.g;
    }

    private void c() {
        Vector<BarcodeFormat> vector = new Vector<>();
        this.k = vector;
        vector.add(BarcodeFormat.EAN_13);
        this.k.add(BarcodeFormat.EAN_8);
        this.k.add(BarcodeFormat.QR_CODE);
    }

    public Result decode(Bitmap bitmap) {
        return CaptureActivityHandler.a(bitmap);
    }
}
