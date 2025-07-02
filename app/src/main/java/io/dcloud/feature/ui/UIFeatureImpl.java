package io.dcloud.feature.ui;

import org.json.JSONArray;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.BaseFeature;
import io.dcloud.common.DHInterface.IWebview;

/* loaded from: classes.dex */
public class UIFeatureImpl extends BaseFeature {
    UIWidgetMgr a = null;

    @Override // io.dcloud.common.DHInterface.BaseFeature
    public String execute(IWebview iWebview, String str, JSONArray jSONArray) {
        return this.a.a(iWebview, str, jSONArray);
    }

    @Override // io.dcloud.common.DHInterface.BaseFeature, io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.a = new UIWidgetMgr(absMgr, str);
    }

    @Override // io.dcloud.common.DHInterface.BaseFeature, io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
        this.a.b(str);
    }
}
