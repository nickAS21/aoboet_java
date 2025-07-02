package io.dcloud.feature.nativeObj;

import android.text.TextUtils;

import java.util.HashMap;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;

/* loaded from: classes.dex */
public class FeatureImpl implements IFeature, IWaiter {
    private static HashMap<String, NativeBitmapMgr> a = new HashMap<>();
    private String b;

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        String obtainAppId = iWebview.obtainApp().obtainAppId();
        this.b = obtainAppId;
        if (!a.containsKey(obtainAppId)) {
            a.put(this.b, new NativeBitmapMgr());
        }
        return a.get(this.b).a(iWebview, str, strArr);
    }

    public static void a(String str, NativeView eVar) {
        if (a.containsKey(str)) {
            a.get(str).a(eVar);
        }
    }

    public static INativeBitmap a(String str, String str2) {
        if (a.containsKey(str)) {
            return a.get(str).a(str2);
        }
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
        if (TextUtils.isEmpty(str) || !a.containsKey(str)) {
            return;
        }
        a.get(str).b();
        a.remove(str);
    }

    @Override // io.dcloud.common.DHInterface.IWaiter
    public Object doForFeature(String str, Object obj) {
        if (str.equals("getNativeBitmap")) {
            String[] strArr = (String[]) obj;
            return a(strArr[0], strArr[1]);
        }
        String obtainAppId = ((IFrameView) ((Object[]) obj)[0]).obtainApp().obtainAppId();
        if (!a.containsKey(obtainAppId)) {
            a.put(obtainAppId, new NativeBitmapMgr());
        }
        if (a.containsKey(obtainAppId)) {
            return a.get(obtainAppId).a(str, obj);
        }
        return a.get(this.b).a(str, obj);
    }
}
