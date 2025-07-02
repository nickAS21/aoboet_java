package io.dcloud.common.common_b.common_b_b;

import android.content.Context;
import android.view.animation.Animation;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.Birdge;
import io.dcloud.common.util.DLGeolocation;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DHWebview.java */
/* loaded from: classes.dex */
public class f extends AdaWebview {
    float a;

    /* JADX INFO: Access modifiers changed from: protected */
    public f(Context context, AbsMgr absMgr, DHFrameView dVar) {
        super(context, dVar);
        this.a = -1.0f;
        Logger.d("dhwebview", "DHWebview0");
        dVar.m = this;
        dVar.n = getWebviewParent();
        addJsInterface("_bridge", (IJsInterface) new Birdge(new i(this)));
        addJsInterface("_dlGeolocation", (IJsInterface) new DLGeolocation(this));
        Logger.d("dhwebview", "DHWebview hashcode=" + dVar.hashCode());
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public float getScaleOfOpenerWebview() {
        float f = this.a;
        return f == -1.0f ? getScale() : f;
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public IApp obtainApp() {
        return obtainFrameView().obtainApp();
    }

    @Override // io.dcloud.common.DHInterface.IWebview
    public void show(Animation animation) {
        IFrameView obtainFrameView = obtainFrameView();
        ((DHFrameView) obtainFrameView).setVisible(true, true);
        obtainFrameView.obtainWindowMgr().processEvent(IMgr.MgrType.WindowMgr, 1, new Object[]{obtainFrameView, animation});
    }
}
