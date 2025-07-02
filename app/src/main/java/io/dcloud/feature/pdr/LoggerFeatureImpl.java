package io.dcloud.feature.pdr;

import java.io.IOException;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;

/* loaded from: classes.dex */
public class LoggerFeatureImpl implements IFeature {
    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (str.equals("logLevel")) {
            JsLogger.a(iWebview.obtainFrameView().obtainApp().obtainAppLog());
            if (strArr[0].equals("LOG")) {
                JsLogger.a("LOG", strArr[1]);
                return null;
            }
            if (strArr[0].equals("ERROR")) {
                JsLogger.b("ERROR", strArr[1]);
                return null;
            }
            if (strArr[0].equals("WARN")) {
                JsLogger.c("WARN", strArr[1]);
                return null;
            }
            if (strArr[0].equals("INFO")) {
                JsLogger.f("INFO", strArr[1]);
                return null;
            }
            strArr[0].equals("ASSERT");
            return null;
        }
        if (!str.equals("clear")) {
            return null;
        }
        try {
            DHFile.deleteFile(iWebview.obtainFrameView().obtainApp().obtainAppLog());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
