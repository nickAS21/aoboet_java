package io.dcloud.js.geolocation.system;

import android.content.Context;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.js.geolocation.GeoManagerBase;

/* loaded from: classes.dex old d*/
public class LocalGeoManager extends GeoManagerBase {
    private GeoListener a;

    public LocalGeoManager(Context context) {
        super(context);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x003e A[Catch: Exception -> 0x0100, TryCatch #0 {Exception -> 0x0100, blocks: (B:3:0x0008, B:6:0x001c, B:8:0x0030, B:13:0x003e, B:16:0x0047, B:18:0x0065, B:20:0x006f, B:22:0x0089, B:26:0x0095, B:30:0x009c, B:31:0x00a0, B:33:0x00a8, B:34:0x00ad, B:36:0x00b2, B:37:0x00b7, B:39:0x00bf, B:41:0x00c8, B:42:0x00d6, B:47:0x00f3, B:49:0x00fb), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0047 A[Catch: Exception -> 0x0100, TryCatch #0 {Exception -> 0x0100, blocks: (B:3:0x0008, B:6:0x001c, B:8:0x0030, B:13:0x003e, B:16:0x0047, B:18:0x0065, B:20:0x006f, B:22:0x0089, B:26:0x0095, B:30:0x009c, B:31:0x00a0, B:33:0x00a8, B:34:0x00ad, B:36:0x00b2, B:37:0x00b7, B:39:0x00bf, B:41:0x00c8, B:42:0x00d6, B:47:0x00f3, B:49:0x00fb), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00a8 A[Catch: Exception -> 0x0100, TryCatch #0 {Exception -> 0x0100, blocks: (B:3:0x0008, B:6:0x001c, B:8:0x0030, B:13:0x003e, B:16:0x0047, B:18:0x0065, B:20:0x006f, B:22:0x0089, B:26:0x0095, B:30:0x009c, B:31:0x00a0, B:33:0x00a8, B:34:0x00ad, B:36:0x00b2, B:37:0x00b7, B:39:0x00bf, B:41:0x00c8, B:42:0x00d6, B:47:0x00f3, B:49:0x00fb), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b2 A[Catch: Exception -> 0x0100, TryCatch #0 {Exception -> 0x0100, blocks: (B:3:0x0008, B:6:0x001c, B:8:0x0030, B:13:0x003e, B:16:0x0047, B:18:0x0065, B:20:0x006f, B:22:0x0089, B:26:0x0095, B:30:0x009c, B:31:0x00a0, B:33:0x00a8, B:34:0x00ad, B:36:0x00b2, B:37:0x00b7, B:39:0x00bf, B:41:0x00c8, B:42:0x00d6, B:47:0x00f3, B:49:0x00fb), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00bf A[Catch: Exception -> 0x0100, TryCatch #0 {Exception -> 0x0100, blocks: (B:3:0x0008, B:6:0x001c, B:8:0x0030, B:13:0x003e, B:16:0x0047, B:18:0x0065, B:20:0x006f, B:22:0x0089, B:26:0x0095, B:30:0x009c, B:31:0x00a0, B:33:0x00a8, B:34:0x00ad, B:36:0x00b2, B:37:0x00b7, B:39:0x00bf, B:41:0x00c8, B:42:0x00d6, B:47:0x00f3, B:49:0x00fb), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c8 A[Catch: Exception -> 0x0100, TryCatch #0 {Exception -> 0x0100, blocks: (B:3:0x0008, B:6:0x001c, B:8:0x0030, B:13:0x003e, B:16:0x0047, B:18:0x0065, B:20:0x006f, B:22:0x0089, B:26:0x0095, B:30:0x009c, B:31:0x00a0, B:33:0x00a8, B:34:0x00ad, B:36:0x00b2, B:37:0x00b7, B:39:0x00bf, B:41:0x00c8, B:42:0x00d6, B:47:0x00f3, B:49:0x00fb), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00d6 A[Catch: Exception -> 0x0100, TryCatch #0 {Exception -> 0x0100, blocks: (B:3:0x0008, B:6:0x001c, B:8:0x0030, B:13:0x003e, B:16:0x0047, B:18:0x0065, B:20:0x006f, B:22:0x0089, B:26:0x0095, B:30:0x009c, B:31:0x00a0, B:33:0x00a8, B:34:0x00ad, B:36:0x00b2, B:37:0x00b7, B:39:0x00bf, B:41:0x00c8, B:42:0x00d6, B:47:0x00f3, B:49:0x00fb), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x009f  */
    @Override // io.dcloud.js.geolocation.GeoManagerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r18, java.lang.String r19, java.lang.String[] r20) {
        /*
            Method dump skipped, instructions count: 257
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.geolocation.system.LocalGeoManager.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    @Override // io.dcloud.js.geolocation.GeoManagerBase
    public void onDestroy() {
        GeoListener aVar = this.a;
        if (aVar != null) {
            aVar.a();
        }
        this.a = null;
    }

    public void getCurrentLocation(IWebview iWebview, String str, boolean z, int i) {
        a().a(iWebview, i, str);
    }

    public void start(IWebview iWebview, String str, String str2, boolean z, int i, int i2) {
        if (a().a(iWebview, i, str, i2)) {
            this.keySet.add(str2);
        }
    }

    GeoListener a() {
        if (this.a == null) {
            this.a = new GeoListener(this.mContext, "");
        }
        return this.a;
    }

    public void stop(String str) {
        if (this.a == null || !this.keySet.contains(str)) {
            return;
        }
        this.keySet.remove(str);
        this.a.a(a.h);
    }
}
