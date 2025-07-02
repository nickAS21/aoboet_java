package io.src.dcloud.adapter;

import android.app.Activity;
import android.content.Context;

import io.dcloud.RInformation;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.util.BaseInfo;

/* loaded from: classes.dex */
public class DCloudAdapterUtil implements IReflectAble {
    public static void Plugin2Host_closeAppStreamSplash(String str) {
    }

    public static void Plugin2Host_finishActivity(String str) {
    }

    public static String getDcloudDownloadService() {
        return "io.dcloud.streamdownload.DownloadService";
    }

    public static String getPageName() {
        return "";
    }

    public static boolean isAutoCreateShortCut() {
        return true;
    }

    public static boolean isPlugin() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static IActivityHandler getIActivityHandler(Activity activity) {
        if (activity instanceof IActivityHandler) {
            return (IActivityHandler) activity;
        }
        return null;
    }

    public static String getRuntimeJsPath() {
        return BaseInfo.sRuntimeJsPath;
    }

    public static int getImageOnLoadingId(Context context) {
        return RInformation.STREAMAPP_DRAWABLE_APPDEFULTICON;
    }

    public static Class<?> getDownloadServiceClass() {
        try {
            return Class.forName(getDcloudDownloadService());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getImagePickNoMediaId(Context context) {
        return RInformation.DRAWABLE_IMAGE_PICK_NO_MEDIA;
    }
}
