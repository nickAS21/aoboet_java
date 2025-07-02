package io.dcloud.common.DHInterface;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import io.dcloud.feature.internal.reflect.BroadcastReceiver;

/* loaded from: classes.dex */
public interface IActivityHandler {
    void addAppStreamTask(String str, String str2);

    void bindDCloudServices();

    void closeAppStreamSplash(String str);

    void commitActivateData(String str, String str2);

    void commitPointData(String str, String str2, String str3, int i, String str4, String str5);

    void commitPointData0(String str, int i, int i2, String str2);

    void downloadSimpleFileTask(IApp iApp, String str, String str2, String str3);

    Context getContext();

    String getUrlByFilePath(String str, String str2);

    boolean isStreamAppMode();

    boolean queryUrl(String str, String str2);

    boolean raiseFilePriority(String str, String str2);

    Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter);

    void resumeAppStreamTask(String str);

    void setViewAsContentView(View view);

    void setWebViewIntoPreloadView(View view);

    void showSplashWaiting();

    void unbindDCloudServices();

    void unregisterReceiver(BroadcastReceiver broadcastReceiver);

    void updateParam(String str, Object obj);

    void updateSplash(String str);
}
