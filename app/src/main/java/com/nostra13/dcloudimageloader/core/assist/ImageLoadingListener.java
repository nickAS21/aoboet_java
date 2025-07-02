package com.nostra13.dcloudimageloader.core.assist;

import android.graphics.Bitmap;
import android.view.View;

/* loaded from: classes.dex */
public interface ImageLoadingListener {
    void onLoadingCancelled(String str, View view);

    void onLoadingComplete(String str, View view, Bitmap bitmap);

    void onLoadingFailed(String str, View view, FailReason failReason);

    void onLoadingStarted(String str, View view);
}
