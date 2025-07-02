package io.dcloud.js.geolocation;

import android.content.Context;
import android.util.Log;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSUtil;

/* compiled from: GeoOptDispatcher.java */
/* loaded from: classes.dex */
public class a {
    AbsMgr a;
    GeoManagerBase b = null;
    GeoManagerBase c = null;
    GeoManagerBase d = null;
    boolean e;

    public a(AbsMgr absMgr) {
        this.a = null;
        this.e = false;
        this.a = absMgr;
        this.e = BaseInfo.isForQihooHelper(absMgr.getContext());
    }

    private GeoManagerBase a(String str) {
        GeoManagerBase geoManagerBase;
        try {
            return (!str.equals("io.dcloud.js.geolocation.amap.AMapGeoManager") || (geoManagerBase = (GeoManagerBase) PlatformUtil.invokeMethod(str, "getInstance", null, new Class[]{Context.class}, new Object[]{this.a.getContext()})) == null) ? (GeoManagerBase) Class.forName(str).getConstructor(Context.class).newInstance(this.a.getContext()) : geoManagerBase;
        } catch (Exception unused) {
            Log.w("geoLoaction", str + " exception");
            return null;
        }
    }

    public String a(IWebview iWebview, String str, String[] strArr) {
        GeoManagerBase b;
        boolean equals = "clearWatch".equals(str);
        if (equals) {
            GeoManagerBase geoManagerBase = this.c;
            if (geoManagerBase != null && geoManagerBase.hasKey(strArr[0])) {
                b = this.c;
            } else {
                GeoManagerBase geoManagerBase2 = this.b;
                if (geoManagerBase2 != null && geoManagerBase2.hasKey(strArr[0])) {
                    b = this.b;
                } else {
                    GeoManagerBase geoManagerBase3 = this.d;
                    b = (geoManagerBase3 == null || !geoManagerBase3.hasKey(strArr[0])) ? null : this.d;
                }
            }
        } else {
            b = b(strArr[4]);
        }
        if (b != null) {
            b.execute(iWebview, str, strArr);
        } else if (!equals) {
            JSUtil.execCallback(iWebview, strArr[0], DOMException.toJSON(17, DOMException.MSG_GEOLOCATION_PROVIDER_ERROR), JSUtil.ERROR, true, false);
        }
        return null;
    }

    private GeoManagerBase b(String str) {
        GeoManagerBase geoManagerBase;
        if (!"system".equals(str) && !"sytem".equals(str) && !"baidu".equals(str)) {
            geoManagerBase = this.c;
            if (geoManagerBase == null) {
                geoManagerBase = a("io.dcloud.js.geolocation.amap.AMapGeoManager");
            }
            this.c = geoManagerBase;
            if (geoManagerBase == null) {
                geoManagerBase = this.b;
                if (geoManagerBase == null) {
                    geoManagerBase = a("io.dcloud.js.geolocation.baidu.BaiduGeoManager");
                }
                this.b = geoManagerBase;
            }
        } else if ("baidu".equals(str)) {
            geoManagerBase = this.b;
            if (geoManagerBase == null) {
                geoManagerBase = a("io.dcloud.js.geolocation.baidu.BaiduGeoManager");
            }
            this.b = geoManagerBase;
            if (geoManagerBase == null) {
                geoManagerBase = this.c;
                if (geoManagerBase == null) {
                    geoManagerBase = a("io.dcloud.js.geolocation.amap.AMapGeoManager");
                }
                this.c = geoManagerBase;
            }
        } else {
            geoManagerBase = null;
        }
        if (geoManagerBase == null) {
            geoManagerBase = this.d;
            if (geoManagerBase == null) {
                geoManagerBase = a("io.dcloud.js.geolocation.system.LocalGeoManager");
            }
            this.d = geoManagerBase;
        }
        return geoManagerBase;
    }

    public void a() {
        GeoManagerBase geoManagerBase = this.b;
        if (geoManagerBase != null) {
            geoManagerBase.onDestroy();
        }
        GeoManagerBase geoManagerBase2 = this.d;
        if (geoManagerBase2 != null) {
            geoManagerBase2.onDestroy();
        }
        GeoManagerBase geoManagerBase3 = this.c;
        if (geoManagerBase3 != null) {
            geoManagerBase3.onDestroy();
        }
    }
}
