package com.nostra13.dcloudimageloader.core.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: classes.dex */
public class BaseImageDownloader implements ImageDownloader {
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    protected static final int BUFFER_SIZE = 32768;
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20000;
    private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";
    protected static final int MAX_REDIRECT_COUNT = 5;
    protected final int connectTimeout;
    protected final Context context;
    protected final int readTimeout;

    public BaseImageDownloader(Context context) {
        this.context = context.getApplicationContext();
        this.connectTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;
        this.readTimeout = DEFAULT_HTTP_READ_TIMEOUT;
    }

    public BaseImageDownloader(Context context, int i, int i2) {
        this.context = context.getApplicationContext();
        this.connectTimeout = i;
        this.readTimeout = i2;
    }

    /* renamed from: com.nostra13.dcloudimageloader.core.download.BaseImageDownloader$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme;

        static {
            int[] iArr = new int[ImageDownloader.Scheme.values().length];
            $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme = iArr;
            try {
                iArr[ImageDownloader.Scheme.HTTP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme[ImageDownloader.Scheme.HTTPS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme[ImageDownloader.Scheme.FILE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme[ImageDownloader.Scheme.CONTENT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme[ImageDownloader.Scheme.ASSETS.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme[ImageDownloader.Scheme.DRAWABLE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme[ImageDownloader.Scheme.UNKNOWN.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    @Override // com.nostra13.dcloudimageloader.core.download.ImageDownloader
    public InputStream getStream(String str, Object obj) throws IOException {
        switch (AnonymousClass1.$SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme[ImageDownloader.Scheme.ofUri(str).ordinal()]) {
            case 1:
            case 2:
                return getStreamFromNetwork(str, obj);
            case 3:
                return getStreamFromFile(str, obj);
            case 4:
                return getStreamFromContent(str, obj);
            case 5:
                return getStreamFromAssets(str, obj);
            case 6:
                return getStreamFromDrawable(str, obj);
            default:
                return getStreamFromOtherSource(str, obj);
        }
    }

    protected InputStream getStreamFromNetwork(String str, Object obj) throws IOException {
        HttpURLConnection createConnection = createConnection(str, obj);
        for (int i = 0; createConnection.getResponseCode() / 100 == 3 && i < 5; i++) {
            createConnection = createConnection(createConnection.getHeaderField("Location"), obj);
        }
        return new BufferedInputStream(createConnection.getInputStream(), 1195);
    }

    protected HttpURLConnection createConnection(String str, Object obj) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Uri.encode(str, ALLOWED_URI_CHARS)).openConnection();
        httpURLConnection.setConnectTimeout(this.connectTimeout);
        httpURLConnection.setReadTimeout(this.readTimeout);
        return httpURLConnection;
    }

    protected InputStream getStreamFromFile(String str, Object obj) throws IOException {
        return new BufferedInputStream(new FileInputStream(ImageDownloader.Scheme.FILE.crop(str)), 1195);
    }

    protected InputStream getStreamFromContent(String str, Object obj) throws FileNotFoundException {
        return this.context.getContentResolver().openInputStream(Uri.parse(str));
    }

    protected InputStream getStreamFromAssets(String str, Object obj) throws IOException {
        return this.context.getAssets().open(ImageDownloader.Scheme.ASSETS.crop(str));
    }

    protected InputStream getStreamFromDrawable(String str, Object obj) {
        Bitmap bitmap = ((BitmapDrawable) this.context.getResources().getDrawable(Integer.parseInt(ImageDownloader.Scheme.DRAWABLE.crop(str)))).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    protected InputStream getStreamFromOtherSource(String str, Object obj) throws IOException {
        throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, str));
    }
}
