package io.dcloud.feature.pdr;

import android.content.SharedPreferences;

import java.io.File;
import java.io.IOException;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.util.JSUtil;

/* loaded from: classes.dex */
public class CoreCacheFeatureImpl implements IFeature {
    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (str.equals("clear")) {
            try {
                DHFile.deleteFile(iWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSUtil.excCallbackSuccess(iWebview, strArr[0], "");
            return null;
        }
        if (str.equals("calculate")) {
            File cacheDir = new File(iWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
            long size = cacheDir.exists() ? DHFile.getFileSize(cacheDir) : 0L;
            JSUtil.execCallback(iWebview, strArr[0], size, JSUtil.OK, false);
            return null;
        }
        if (!str.equals("setMaxSize")) {
            return null;
        }
        long parseLong = Long.parseLong(strArr[0]);
        SharedPreferences.Editor edit = iWebview.getContext().getSharedPreferences(iWebview.obtainFrameView().obtainApp().obtainAppId(), 0).edit();
        edit.putLong("maxSize", parseLong);
        edit.commit();
        return null;
    }
}
