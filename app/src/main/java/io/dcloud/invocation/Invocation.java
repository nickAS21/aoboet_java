package io.dcloud.invocation;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;

/* loaded from: classes.dex */
public class Invocation implements IFeature {
    InvProxy a;

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        return this.a.a(iWebview, str, strArr);
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.a = new InvProxy(absMgr);
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
        this.a.a(str);
    }
}
