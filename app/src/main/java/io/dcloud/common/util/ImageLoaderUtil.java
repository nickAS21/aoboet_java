package io.dcloud.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.nostra13.dcloudimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.dcloudimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.ImageLoader;
import com.nostra13.dcloudimageloader.core.ImageLoaderConfiguration;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.nostra13.dcloudimageloader.core.assist.QueueProcessingType;
import com.nostra13.dcloudimageloader.core.decode.BaseImageDecoder;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;

import java.io.File;

import io.dcloud.common.adapter.util.DeviceInfo;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* loaded from: classes.dex */
public class ImageLoaderUtil {
    public static void initImageLoader(Context context) {
        if (ImageLoader.getInstance().isInited()) {
            return;
        }
        File file = new File(getIconLoaclfolder());
        if (!file.exists()) {
            file.mkdirs();
        }
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(400, 400).tasksProcessingOrder(QueueProcessingType.LIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2097152)).memoryCacheSize(2097152).threadPriority(3).threadPoolSize(3).discCacheFileCount(100).denyCacheImageMultipleSizesInMemory().defaultDisplayImageOptions(getIconDisplayOptions(context)).discCache(new UnlimitedDiscCache(file)).imageDownloader(new BaseImageDownloader(context)).imageDecoder(new BaseImageDecoder(false)).build());
    }

    public static String getIconLoaclfolder() {
        return DeviceInfo.sBaseFsRootPath + "icons/";
    }

    public static void updateIcon(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        File file = ImageLoader.getInstance().getDiscCache().get(str);
        if (file.exists()) {
            file.delete();
        }
        ImageLoader.getInstance().loadImage(str, null);
    }

    public static DisplayImageOptions getIconDisplayOptions(Context context) {
        return new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading(DCloudAdapterUtil.getImageOnLoadingId(context)).build();
    }
}
