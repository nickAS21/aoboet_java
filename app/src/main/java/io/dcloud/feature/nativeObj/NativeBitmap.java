package io.dcloud.feature.nativeObj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.ImageLoader;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.nostra13.dcloudimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import pl.droidsonroids.gif.GifDrawable;

/* compiled from: NativeBitmap.java */
/* loaded from: classes.dex */
public class NativeBitmap implements INativeBitmap {
    private String a;
    private String b;
    private Bitmap c;
    private String d;
    private String e;
    private GifDrawable f;
    private IApp g;
    private ICallBack h = null;
    private ICallBack i = null;
    private Handler j = new Handler() { // from class: io.dcloud.feature.nativeObj.a.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 10) {
                if (i == 40 && NativeBitmap.this.i != null) {
                    NativeBitmap.this.i.onCallBack(0, message.obj);
                }
            } else if (NativeBitmap.this.h != null) {
                NativeBitmap.this.h.onCallBack(0, message.obj);
            }
            super.handleMessage(message);
        }
    };

    public NativeBitmap(IApp iApp, String str, String str2, String str3) {
        this.d = "jpg";
        this.a = str;
        this.b = str2;
        this.e = str3;
        this.d = b(str3);
        this.g = iApp;
    }

    public boolean a() {
        if (PdrUtil.isEmpty(this.d)) {
            return false;
        }
        return this.d.equalsIgnoreCase("gif");
    }

    @JavascriptInterface
    public String b() {
        return this.a;
    }

    @JavascriptInterface
    public void a(IWebview iWebview, Context context, String str, final ICallBack iCallBack, final ICallBack iCallBack2) {
        if (TextUtils.isEmpty(str)) {
            this.i = iCallBack2;
            Message obtainMessage = this.j.obtainMessage();
            obtainMessage.what = 40;
            obtainMessage.obj = "path不能为空";
            this.j.sendMessage(obtainMessage);
            return;
        }
        if (str.toLowerCase().startsWith(DeviceInfo.HTTP_PROTOCOL) || str.toLowerCase().startsWith(DeviceInfo.HTTPS_PROTOCOL) || str.toLowerCase().startsWith("ftp://")) {
            this.i = iCallBack2;
            Message obtainMessage2 = this.j.obtainMessage();
            obtainMessage2.what = 40;
            obtainMessage2.obj = "patch不支持网络地址，仅支持本地文件系统";
            this.j.sendMessage(obtainMessage2);
            return;
        }
        str.toLowerCase().startsWith("_");
        this.e = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), str);
        this.d = b(str);
        ImageLoader.getInstance().loadImage(d(this.e), i(), new ImageLoadingListener() { // from class: io.dcloud.feature.nativeObj.a.1
            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingCancelled(String str2, View view) {
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingStarted(String str2, View view) {
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingFailed(String str2, View view, FailReason failReason) {
                NativeBitmap.this.i = iCallBack2;
                NativeBitmap.this.j.sendEmptyMessage(40);
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingComplete(String str2, View view, Bitmap bitmap) {
                NativeBitmap.this.c = bitmap;
                NativeBitmap.this.h = iCallBack;
                NativeBitmap.this.j.sendEmptyMessage(10);
            }
        });
    }

    @JavascriptInterface
    public void a(String str, ICallBack iCallBack, ICallBack iCallBack2) {
        try {
            Bitmap a = a(str);
            this.c = a;
            if (a != null) {
                c(str);
                this.h = iCallBack;
                this.j.sendEmptyMessage(10);
            } else {
                this.i = iCallBack2;
                this.j.sendEmptyMessage(40);
            }
        } catch (Exception unused) {
            this.i = iCallBack2;
            this.j.sendEmptyMessage(40);
        }
    }

    @Override // io.dcloud.common.DHInterface.INativeBitmap
    @JavascriptInterface
    public void clear() {
        c();
        this.c = null;
        this.f = null;
    }

    public void c() {
        a(false);
    }

    public void a(boolean z) {
        if (z && TextUtils.isEmpty(this.e)) {
            return;
        }
        Bitmap bitmap = this.c;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.c.recycle();
            String d = d(this.e);
            if (!TextUtils.isEmpty(d)) {
                MemoryCacheUtil.removeFromCache(d, ImageLoader.getInstance().getMemoryCache());
            }
        }
        GifDrawable gifDrawable = this.f;
        if (gifDrawable == null || gifDrawable.isRecycled()) {
            return;
        }
        this.f.recycle();
    }

    public boolean d() {
        Bitmap bitmap = this.c;
        if (bitmap != null) {
            return bitmap.isRecycled();
        }
        GifDrawable gifDrawable = this.f;
        if (gifDrawable != null) {
            return gifDrawable.isRecycled();
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r9v10, types: [io.dcloud.feature.nativeObj.a$2] */
    @JavascriptInterface
    public void a(IApp iApp, String str, final NativeBitmapSaveOptions cVar, final float f, final ICallBack iCallBack, final ICallBack iCallBack2) {
        if (TextUtils.isEmpty(str)) {
            this.i = iCallBack2;
            Message obtainMessage = this.j.obtainMessage();
            obtainMessage.what = 40;
            obtainMessage.obj = "path不能为空";
            this.j.sendMessage(obtainMessage);
            return;
        }
        if (str.toLowerCase().startsWith(DeviceInfo.HTTP_PROTOCOL) || str.toLowerCase().startsWith(DeviceInfo.HTTPS_PROTOCOL) || str.toLowerCase().startsWith("ftp://")) {
            this.i = iCallBack2;
            Message obtainMessage2 = this.j.obtainMessage();
            obtainMessage2.what = 40;
            obtainMessage2.obj = "patch不支持网络地址，仅支持本地文件系统";
            this.j.sendMessage(obtainMessage2);
            return;
        }
        final String str2 = this.e;
        this.e = str;
        new Thread() { // from class: io.dcloud.feature.nativeObj.a.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    NativeBitmap.this.getBitmap();
                    if (cVar.h != null) {
                        Bitmap createBitmap = Bitmap.createBitmap(NativeBitmap.this.c, PdrUtil.convertToScreenInt(cVar.h.optString("left"), NativeBitmap.this.c.getWidth(), 0, f), PdrUtil.convertToScreenInt(cVar.h.optString("top"), NativeBitmap.this.c.getHeight(), 0, f), PdrUtil.convertToScreenInt(cVar.h.optString(AbsoluteConst.JSON_KEY_WIDTH), NativeBitmap.this.c.getWidth(), NativeBitmap.this.c.getWidth(), f), PdrUtil.convertToScreenInt(cVar.h.optString("height"), NativeBitmap.this.c.getHeight(), NativeBitmap.this.c.getHeight(), f));
                        NativeBitmap aVar = NativeBitmap.this;
                        aVar.a(aVar.e, createBitmap, cVar);
                        createBitmap.recycle();
                        NativeBitmap.this.e = str2;
                    } else {
                        NativeBitmap aVar2 = NativeBitmap.this;
                        aVar2.a(aVar2.e, NativeBitmap.this.c, cVar);
                    }
                    NativeBitmap.this.h = iCallBack;
                    Message obtainMessage3 = NativeBitmap.this.j.obtainMessage();
                    obtainMessage3.what = 10;
                    obtainMessage3.obj = cVar;
                    NativeBitmap.this.j.sendMessage(obtainMessage3);
                } catch (Exception e) {
                    NativeBitmap.this.i = iCallBack2;
                    NativeBitmap.this.j.sendEmptyMessage(40);
                    Logger.e("mabo", "saveFile: " + e.toString());
                }
            }
        }.start();
    }

    @JavascriptInterface
    public String e() {
        return "data:image/" + ("jpg".equals(this.d) ? "jepg" : this.d) + ";base64," + a(this.c);
    }

    @JavascriptInterface
    public String f() {
        return "{\"id\":\"" + this.a + "\",\"__id__\":\"" + this.b + JSUtil.QUOTE + "}";
    }

    @Override // io.dcloud.common.DHInterface.INativeBitmap
    public void setBitmap(Bitmap bitmap) {
        this.c = bitmap;
    }

    @Override // io.dcloud.common.DHInterface.INativeBitmap
    public Bitmap getBitmap() {
        if (d()) {
            String d = d(this.e);
            if (TextUtils.isEmpty(d)) {
                return null;
            }
            this.c = ImageLoader.getInstance().loadImageSync(d, i());
        }
        return this.c;
    }

    public GifDrawable g() {
        GifDrawable gifDrawable = this.f;
        if ((gifDrawable == null || (gifDrawable != null && gifDrawable.isRecycled())) && !PdrUtil.isEmpty(this.e) && this.g != null) {
            File file = new File(this.e);
            if (this.g.obtainRunningAppMode() == 1 && !file.exists()) {
                String str = this.e;
                if (str.startsWith("/")) {
                    String str2 = this.e;
                    str = str2.substring(1, str2.length());
                }
                try {
                    this.f = new GifDrawable(this.g.getActivity().getAssets(), str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.exists()) {
                try {
                    this.f = new GifDrawable(file);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return this.f;
    }

    private Bitmap a(String str) {
        if (str.indexOf(JSUtil.COMMA) != -1) {
            str = str.substring(str.indexOf(JSUtil.COMMA));
        }
        byte[] decode = Base64.decode(str, 2);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    private String a(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(h(), 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2);
    }

    private Bitmap.CompressFormat h() {
        if ("png".equals(this.d)) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, Bitmap bitmap, NativeBitmapSaveOptions cVar) throws Exception {
        if (bitmap == null || bitmap.getHeight() == 0 || bitmap.getWidth() == 0) {
            return;
        }
        File file = new File(str.substring(0, str.lastIndexOf("/")));
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.createNewFile();
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
        bitmap.compress("png".equals(b(str)) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, cVar.c, bufferedOutputStream);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        cVar.d = file2.getAbsolutePath();
        cVar.e = bitmap.getWidth();
        cVar.f = bitmap.getHeight();
        cVar.g = file2.length();
    }

    private String b(String str) {
        return TextUtils.isEmpty(str) ? str : str.substring(str.lastIndexOf(".") + 1, str.length()).toLowerCase();
    }

    private void c(String str) {
        if (str.indexOf(JSUtil.COMMA) != -1) {
            this.d = str.split(JSUtil.COMMA)[0].replace("data:image/", "").replace(";base64", "");
        }
    }

    private String d(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (this.g == null || new File(str).exists() || str.startsWith("/storage") || this.g.obtainRunningAppMode() != 1) {
            return str.contains(DeviceInfo.FILE_PROTOCOL) ? str : DeviceInfo.FILE_PROTOCOL + str;
        }
        if (str.startsWith("/")) {
            str = str.substring(1, str.length());
        }
        return ImageDownloader.Scheme.ASSETS.wrap(str);
    }

    private DisplayImageOptions i() {
        return new DisplayImageOptions.Builder().cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.NONE).build();
    }
}
