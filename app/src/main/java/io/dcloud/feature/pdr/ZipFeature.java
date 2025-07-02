package io.dcloud.feature.pdr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ZipUtils;

/* loaded from: classes.dex */
public class ZipFeature implements IFeature {
    HashMap<String, ArrayList<String[]>> a = null;

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (PdrUtil.isEquals(str, "compress")) {
            a aVar = new a();
            aVar.f = iWebview;
            aVar.a = true;
            IApp obtainApp = iWebview.obtainFrameView().obtainApp();
            aVar.b = strArr[0];
            aVar.d = obtainApp.convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[0]);
            String str2 = strArr[1];
            if (str2 == null) {
                str2 = AbsoluteConst.MINI_SERVER_APP_DOC + System.currentTimeMillis();
            }
            if (!str2.endsWith(".zip")) {
                str2 = str2 + ".zip";
            }
            aVar.c = obtainApp.convert2AbsFullPath(iWebview.obtainFullUrl(), str2);
            aVar.e = strArr[2];
            a(aVar);
            return null;
        }
        if (PdrUtil.isEquals(str, "decompress")) {
            a aVar2 = new a();
            aVar2.f = iWebview;
            aVar2.a = false;
            IApp obtainApp2 = iWebview.obtainFrameView().obtainApp();
            aVar2.c = obtainApp2.convert2AbsFullPath(iWebview.obtainFullUrl(), obtainApp2.checkPrivateDirAndCopy2Temp(strArr[0]));
            aVar2.d = obtainApp2.convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[1]);
            aVar2.e = strArr[2];
            a(aVar2);
            return null;
        }
        if (!PdrUtil.isEquals(str, "compressImage")) {
            return null;
        }
        CompressImage.compressAsync(iWebview, strArr);
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.a = new HashMap<>(1);
    }

    private void a(a aVar) {
        new Thread(aVar).start();
    }

    /* loaded from: classes.dex */
    class a implements Runnable {
        boolean a = false;
        String b = null;
        String c;
        String d;
        String e;
        IWebview f;

        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (this.a) {
                    Logger.d("compress mUnZipDirPath=" + this.d + ";mZipFilePath" + this.c);
                    File file = new File(this.d);
                    if (!file.exists()) {
                        JSUtil.excCallbackError(this.f, this.e, String.format(DOMException.JSON_ERROR_INFO, 2, this.b + " open failed:ENOENT (No such file or directory)"), true);
                    }
                    this.f.obtainFrameView().obtainApp();
                    if (JSUtil.checkOperateDirErrorAndCallback(this.f, this.e, this.c)) {
                        return;
                    }
                    File file2 = new File(this.c);
                    File[] listFiles = file.isDirectory() ? file2.listFiles() : null;
                    if (file.isFile() || listFiles == null) {
                        listFiles = new File[]{file};
                    }
                    ZipUtils.zipFiles(listFiles, file2);
                } else {
                    Logger.d("decompress mUnZipDirPath=" + this.d + ";mZipFilePath" + this.c);
                    if (JSUtil.checkOperateDirErrorAndCallback(this.f, this.e, this.d)) {
                        return;
                    }
                    File file3 = new File(this.c);
                    if (!file3.exists()) {
                        JSUtil.excCallbackError(this.f, this.e, String.format(DOMException.JSON_ERROR_INFO, 2, this.b + " open failed:ENOENT (No such file or directory)"), true);
                        return;
                    }
                    ZipUtils.upZipFile(file3, this.d);
                }
                JSUtil.excCallbackSuccess(this.f, this.e, "");
            } catch (Exception e) {
                JSUtil.excCallbackError(this.f, this.e, String.format(DOMException.JSON_ERROR_INFO, 2, e.getMessage()), true);
            }
        }
    }
}
