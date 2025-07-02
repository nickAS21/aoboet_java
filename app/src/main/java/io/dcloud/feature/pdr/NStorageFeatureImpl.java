package io.dcloud.feature.pdr;

import android.content.SharedPreferences;

import java.util.HashMap;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class NStorageFeatureImpl implements IFeature {
    HashMap<String, SharedPreferences> a = new HashMap<>(1);

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        String obtainAppId = iWebview.obtainFrameView().obtainApp().obtainAppId();
        SharedPreferences sharedPreferences = this.a.get(obtainAppId);
        if (sharedPreferences == null) {
            sharedPreferences = a(obtainAppId);
            this.a.put(obtainAppId, sharedPreferences);
        }
        if ("getLength".equals(str)) {
            return JSUtil.wrapJsVar(b(sharedPreferences));
        }
        if ("getItem".equals(str)) {
            String str2 = strArr[0];
            return sharedPreferences.contains(str2) ? "string:" + a(sharedPreferences, str2) : "null:";
        }
        if ("setItem".equals(str)) {
            a(sharedPreferences, strArr[0], strArr[1]);
        } else if ("removeItem".equals(str)) {
            b(sharedPreferences, strArr[0]);
        } else if ("clear".equals(str)) {
            a(sharedPreferences);
        } else if (IApp.ConfigProperty.CONFIG_KEY.equals(str)) {
            return JSUtil.wrapJsVar(a(sharedPreferences, Integer.parseInt(strArr[0])), true);
        }
        return null;
    }

    SharedPreferences a(String str) {
        return PlatformUtil.getOrCreateBundle(str + "_storages");
    }

    String a(SharedPreferences sharedPreferences, String str) {
        return sharedPreferences.getString(str, null);
    }

    void a(SharedPreferences sharedPreferences, String str, String str2) {
        PlatformUtil.setBundleData(sharedPreferences, str, str2);
    }

    void b(SharedPreferences sharedPreferences, String str) {
        PlatformUtil.removeBundleData(sharedPreferences, str);
    }

    void a(SharedPreferences sharedPreferences) {
        PlatformUtil.clearBundle(sharedPreferences);
    }

    String a(SharedPreferences sharedPreferences, int i) {
        return String.valueOf(PdrUtil.getKeyByIndex((HashMap) sharedPreferences.getAll(), i));
    }

    int b(SharedPreferences sharedPreferences) {
        try {
            return sharedPreferences.getAll().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
