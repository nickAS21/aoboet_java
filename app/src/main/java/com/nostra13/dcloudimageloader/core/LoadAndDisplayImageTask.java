package com.nostra13.dcloudimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;

import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;
import com.nostra13.dcloudimageloader.core.decode.ImageDecoder;
import com.nostra13.dcloudimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;
import com.nostra13.dcloudimageloader.utils.IoUtils;
import com.nostra13.dcloudimageloader.utils.L;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class LoadAndDisplayImageTask implements Runnable {
    private static final int BUFFER_SIZE = 32768;
    private static final String ERROR_POST_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PROCESSOR_FOR_DISC_CACHE_NULL = "Bitmap processor for disc cache returned null [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISC = "Cache image on disc [%s]";
    private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
    private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISC_CACHE = "Load image from disc cache [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
    private static final String LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISC = "Process image before cache on disc [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    private final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    final ImageAware imageAware;
    private final ImageLoadingInfo imageLoadingInfo;
    final ImageLoadingListener listener;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    private final ImageDownloader slowNetworkDownloader;
    private final ImageSize targetSize;
    final String uri;
    private final boolean writeLogs;
    private LoadedFrom loadedFrom = LoadedFrom.NETWORK;
    private boolean imageAwareCollected = false;

    public LoadAndDisplayImageTask(ImageLoaderEngine imageLoaderEngine, ImageLoadingInfo imageLoadingInfo, Handler handler) {
        this.engine = imageLoaderEngine;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
        ImageLoaderConfiguration imageLoaderConfiguration = imageLoaderEngine.configuration;
        this.configuration = imageLoaderConfiguration;
        this.downloader = imageLoaderConfiguration.downloader;
        this.networkDeniedDownloader = imageLoaderConfiguration.networkDeniedDownloader;
        this.slowNetworkDownloader = imageLoaderConfiguration.slowNetworkDownloader;
        this.decoder = imageLoaderConfiguration.decoder;
        this.writeLogs = imageLoaderConfiguration.writeLogs;
        this.uri = imageLoadingInfo.uri;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.imageAware = imageLoadingInfo.imageAware;
        this.targetSize = imageLoadingInfo.targetSize;
        this.options = imageLoadingInfo.options;
        this.listener = imageLoadingInfo.listener;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (waitIfPaused() || delayIfNeed()) {
            return;
        }
        ReentrantLock reentrantLock = this.imageLoadingInfo.loadFromUriLock;
        log(LOG_START_DISPLAY_IMAGE_TASK);
        if (reentrantLock.isLocked()) {
            log(LOG_WAITING_FOR_IMAGE_LOADED);
        }
        reentrantLock.lock();
        try {
            if (checkTaskIsNotActual()) {
                return;
            }
            Bitmap bitmap = this.configuration.memoryCache.get(this.memoryCacheKey);
            if (bitmap == null) {
                bitmap = tryLoadBitmap();
                if (this.imageAwareCollected) {
                    return;
                }
                if (bitmap == null) {
                    return;
                }
                if (!checkTaskIsNotActual() && !checkTaskIsInterrupted()) {
                    if (this.options.shouldPreProcess()) {
                        log(LOG_PREPROCESS_IMAGE);
                        bitmap = this.options.getPreProcessor().process(bitmap);
                        if (bitmap == null) {
                            L.e("Pre-processor returned null [%s]", new Object[0]);
                        }
                    }
                    if (bitmap != null && this.options.isCacheInMemory()) {
                        log(LOG_CACHE_IMAGE_IN_MEMORY);
                        this.configuration.memoryCache.put(this.memoryCacheKey, bitmap);
                    }
                }
                return;
            }
            this.loadedFrom = LoadedFrom.MEMORY_CACHE;
            log(LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING);
            if (bitmap != null && this.options.shouldPostProcess()) {
                log(LOG_POSTPROCESS_IMAGE);
                bitmap = this.options.getPostProcessor().process(bitmap);
                if (bitmap == null) {
                    L.e("Pre-processor returned null [%s]", this.memoryCacheKey);
                }
            }
            reentrantLock.unlock();
            if (checkTaskIsNotActual() || checkTaskIsInterrupted()) {
                return;
            }
            DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bitmap, this.imageLoadingInfo, this.engine, this.loadedFrom);
            displayBitmapTask.setLoggingEnabled(this.writeLogs);
            if (this.options.isSyncLoading()) {
                displayBitmapTask.run();
            } else {
                this.handler.post(displayBitmapTask);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    private boolean waitIfPaused() {
        AtomicBoolean pause = this.engine.getPause();
        synchronized (pause) {
            if (pause.get()) {
                log(LOG_WAITING_FOR_RESUME);
                try {
                    pause.wait();
                    log(LOG_RESUME_AFTER_PAUSE);
                } catch (InterruptedException unused) {
                    L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
                    return true;
                }
            }
        }
        return checkTaskIsNotActual();
    }

    private boolean delayIfNeed() {
        if (!this.options.shouldDelayBeforeLoading()) {
            return false;
        }
        log(LOG_DELAY_BEFORE_LOADING, Integer.valueOf(this.options.getDelayBeforeLoading()), this.memoryCacheKey);
        try {
            Thread.sleep(this.options.getDelayBeforeLoading());
            return checkTaskIsNotActual();
        } catch (InterruptedException unused) {
            L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
            return true;
        }
    }

    private boolean checkTaskIsNotActual() {
        return checkViewCollected() || checkViewReused();
    }

    private boolean checkViewCollected() {
        if (!this.imageAware.isCollected()) {
            return false;
        }
        this.imageAwareCollected = true;
        log(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED);
        fireCancelEvent();
        return true;
    }

    private boolean checkViewReused() {
        boolean z = !this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware));
        if (z) {
            log(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED);
            fireCancelEvent();
        }
        return z;
    }

    private boolean checkTaskIsInterrupted() {
        boolean interrupted = Thread.interrupted();
        if (interrupted) {
            log(LOG_TASK_INTERRUPTED);
        }
        return interrupted;
    }

    private Bitmap tryLoadBitmap() {
        Bitmap bitmap = null;
        File cacheFile = getImageFileInDiscCache();

        try {
            // 1. Спроба завантаження з кешу
            if (cacheFile.exists()) {
                log(LOG_LOAD_IMAGE_FROM_DISC_CACHE);
                this.loadedFrom = LoadedFrom.DISC_CACHE;
                bitmap = decodeImage(ImageDownloader.Scheme.FILE.wrap(cacheFile.getAbsolutePath()));

                if (this.imageAwareCollected) return null;

                if (isValidBitmap(bitmap)) {
                    return bitmap;
                }
            }

            // 2. Завантаження з мережі
            log(LOG_LOAD_IMAGE_FROM_NETWORK);
            this.loadedFrom = LoadedFrom.NETWORK;

            String sourceUri = this.options.isCacheOnDisc()
                    ? tryCacheImageOnDisc(cacheFile)
                    : this.uri;

            if (checkTaskIsNotActual()) return null;

            bitmap = decodeImage(sourceUri);

            if (this.imageAwareCollected) return null;

            if (isValidBitmap(bitmap)) {
                return bitmap;
            } else {
                fireFailEvent(FailReason.FailType.DECODING_ERROR, null);
                return null;
            }

        } catch (IOException e) {
            L.e(e);
            fireFailEvent(FailReason.FailType.IO_ERROR, e);
            if (cacheFile.exists()) {
                cacheFile.delete();  // очищення зіпсованого кешу
            }
            return bitmap;
        } catch (IllegalStateException e) {
            fireFailEvent(FailReason.FailType.NETWORK_DENIED, null);
            return null;
        } catch (OutOfMemoryError e) {
            L.e(e);
            fireFailEvent(FailReason.FailType.OUT_OF_MEMORY, e);
            return null;
        } catch (Throwable t) {
            L.e(t);
            fireFailEvent(FailReason.FailType.UNKNOWN, t);
            return null;
        }
    }

    private boolean isValidBitmap(Bitmap bmp) {
        return bmp != null && bmp.getWidth() > 0 && bmp.getHeight() > 0;
    }


    private File getImageFileInDiscCache() {
        File parentFile;
        File file = this.configuration.discCache.get(this.uri);
        File parentFile2 = file.getParentFile();
        if ((parentFile2 == null || (!parentFile2.exists() && !parentFile2.mkdirs())) && (parentFile = (file = this.configuration.reserveDiscCache.get(this.uri)).getParentFile()) != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        return file;
    }

    private Bitmap decodeImage(String str) throws IOException {
        ViewScaleType scaleType;
        if (checkViewCollected() || (scaleType = this.imageAware.getScaleType()) == null) {
            return null;
        }
        return this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, str, this.targetSize, scaleType, getDownloader(), this.options));
    }

    private String tryCacheImageOnDisc(File file) {
        log(LOG_CACHE_IMAGE_ON_DISC);
        try {
            int i = this.configuration.maxImageWidthForDiscCache;
            int i2 = this.configuration.maxImageHeightForDiscCache;
            if (!((i > 0 || i2 > 0) ? downloadSizedImage(file, i, i2) : false)) {
                downloadImage(file);
            }
            this.configuration.discCache.put(this.uri, file);
            return ImageDownloader.Scheme.FILE.wrap(file.getAbsolutePath());
        } catch (IOException e) {
            L.e(e);
            if (file.exists()) {
                file.delete();
            }
            return this.uri;
        }
    }

    private boolean downloadSizedImage(File file, int i, int i2) throws IOException {
        Bitmap decode = this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, this.uri, new ImageSize(i, i2), ViewScaleType.FIT_INSIDE, getDownloader(), new DisplayImageOptions.Builder().cloneFrom(this.options).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build()));
        if (decode == null) {
            return false;
        }
        if (this.configuration.processorForDiscCache != null) {
            log(LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISC);
            decode = this.configuration.processorForDiscCache.process(decode);
            if (decode == null) {
                L.e(ERROR_PROCESSOR_FOR_DISC_CACHE_NULL, this.memoryCacheKey);
                return false;
            }
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 1195);
        try {
            boolean compress = decode.compress(this.configuration.imageCompressFormatForDiscCache, this.configuration.imageQualityForDiscCache, bufferedOutputStream);
            IoUtils.closeSilently(bufferedOutputStream);
            decode.recycle();
            return compress;
        } catch (Throwable th) {
            IoUtils.closeSilently(bufferedOutputStream);
            throw th;
        }
    }

    private void downloadImage(File file) throws IOException {
        InputStream stream = getDownloader().getStream(this.uri, this.options.getExtraForDownloader());
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 1195);
            try {
                IoUtils.copyStream(stream, bufferedOutputStream);
            } finally {
                IoUtils.closeSilently(bufferedOutputStream);
            }
        } finally {
            IoUtils.closeSilently(stream);
        }
    }

    private void fireFailEvent(final FailReason.FailType failType, final Throwable th) {
        if (Thread.interrupted()) {
            return;
        }
        if (this.options.isSyncLoading()) {
            this.listener.onLoadingFailed(this.uri, this.imageAware.getWrappedView(), new FailReason(failType, th));
        } else {
            this.handler.post(new Runnable() { // from class: com.nostra13.dcloudimageloader.core.LoadAndDisplayImageTask.1
                @Override // java.lang.Runnable
                public void run() {
                    if (LoadAndDisplayImageTask.this.options.shouldShowImageOnFail()) {
                        LoadAndDisplayImageTask.this.imageAware.setImageDrawable(LoadAndDisplayImageTask.this.options.getImageOnFail(LoadAndDisplayImageTask.this.configuration.resources));
                    }
                    LoadAndDisplayImageTask.this.listener.onLoadingFailed(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), new FailReason(failType, th));
                }
            });
        }
    }

    private void fireCancelEvent() {
        if (Thread.interrupted()) {
            return;
        }
        if (this.options.isSyncLoading()) {
            this.listener.onLoadingCancelled(this.uri, this.imageAware.getWrappedView());
        } else {
            this.handler.post(new Runnable() { // from class: com.nostra13.dcloudimageloader.core.LoadAndDisplayImageTask.2
                @Override // java.lang.Runnable
                public void run() {
                    LoadAndDisplayImageTask.this.listener.onLoadingCancelled(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView());
                }
            });
        }
    }

    private ImageDownloader getDownloader() {
        if (this.engine.isNetworkDenied()) {
            return this.networkDeniedDownloader;
        }
        if (this.engine.isSlowNetwork()) {
            return this.slowNetworkDownloader;
        }
        return this.downloader;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getLoadingUri() {
        return this.uri;
    }

    private void log(String str) {
        if (this.writeLogs) {
            L.d(str, this.memoryCacheKey);
        }
    }

    private void log(String str, Object... objArr) {
        if (this.writeLogs) {
            L.d(str, objArr);
        }
    }
}
