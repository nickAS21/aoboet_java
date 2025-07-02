package io.dcloud.feature.barcode;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;

/* loaded from: classes.dex */
public class BarcodeFeatureImpl implements IFeature {
    BarcodeProxy a = null;

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        this.a.a(iWebview, str, strArr);
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.a = new BarcodeProxy();
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
        this.a.a();
    }
}
