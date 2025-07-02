package io.dcloud.js.geolocation.system;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;

/* compiled from: GeoListener.java */
/* loaded from: classes.dex old a*/
public class GeoListener {
    public static int a = 1;
    public static int b = 2;
    public static int c = 3;
    public static int d = 0;
    public static int e = 1;
    public static int f = 0;
    public static int g = 1;
    public static int h = 2;
    public static int i = 5000;
    String j;
    String m;
    IWebview n;
    LocationManager o;
    private Timer s;
    private C0012a t;
    private Context u;
    String p = null;
    IWebview q = null;
    int r = 0;
    GpsListener k = null;
    NetworkListener l = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GeoListener(Context context, String str) {
        this.j = str;
        this.u = context;
        this.o = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (this.s == null) {
            this.s = new Timer();
        }
    }

    public void a() {
        a(h);
    }

    private String a(Location location, String str) {
        return String.format(Locale.ENGLISH, "{latitude:%f,longitude:%f,altitude:%f,accuracy:%f,heading:%f,velocity:%f,altitudeAccuracy:%d,timestamp:new Date('%s'),coordsType:'%s'}", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()), Double.valueOf(location.getAltitude()), Float.valueOf(location.getAccuracy()), Float.valueOf(location.getBearing()), Float.valueOf(location.getSpeed()), 0, Long.valueOf(location.getTime()), str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Location location, int i2) {
        String str;
        IWebview iWebview;
        Log.i("geoListener", "successType==" + i2);
        String a2 = a(location, "wgs84");
        String str2 = this.p;
        if (str2 != null && (iWebview = this.q) != null) {
            JSUtil.excCallbackSuccess(iWebview, str2, a2, true, false);
            a(h);
            this.p = null;
            this.q = null;
        }
        IWebview iWebview2 = this.n;
        if (iWebview2 == null || (str = this.m) == null) {
            return;
        }
        JSUtil.excCallbackSuccess(iWebview2, str, a2, true, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i2, String str, int i3) {
        String str2;
        IWebview iWebview;
        Log.i("geoListener", "failType==" + i3);
        a(i3);
        String str3 = this.p;
        if (str3 != null && (iWebview = this.q) != null && this.k == null && this.l == null) {
            JSUtil.excCallbackError(iWebview, str3, DOMException.toJSON(i2, str), true);
        }
        IWebview iWebview2 = this.n;
        if (iWebview2 == null || (str2 = this.m) == null || this.k != null || this.l != null) {
            return;
        }
        JSUtil.excCallbackError(iWebview2, str2, DOMException.toJSON(i2, str), true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(IWebview iWebview, int i2, String str) {
        this.q = iWebview;
        this.p = str;
        a(i2, d);
    }

    private void b(int i2) {
        this.r += i2;
        Logger.d("GeoListener", "mUseCount=" + this.r);
    }

    private boolean a(int i2, int i3) {
        C0012a c0012a;
        if (this.r == 0) {
            if (this.k == null && this.o.isProviderEnabled("gps")) {
                this.k = new GpsListener(this.u, this);
            }
            if (this.l == null && this.o.isProviderEnabled("network")) {
                this.l = new NetworkListener(this.u, this);
            }
            GpsListener bVar = this.k;
            if (bVar != null) {
                bVar.a(i2);
            }
            NetworkListener cVar = this.l;
            if (cVar != null) {
                cVar.a(i2);
            }
            if (i3 == d) {
                c(i);
            }
        }
        if (i3 == e && this.s != null && (c0012a = this.t) != null) {
            c0012a.cancel();
        }
        b(1);
        if (this.l != null || this.k != null) {
            return true;
        }
        a(b, "No location providers available.", h);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(IWebview iWebview, int i2, String str, int i3) {
        this.n = iWebview;
        this.m = str;
        i = i3;
        return a(i2, e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i2) {
        b(-1);
        if (this.r <= 0) {
            if (i2 == f) {
                GpsListener bVar = this.k;
                if (bVar != null) {
                    bVar.b();
                    this.k = null;
                }
            } else if (i2 == g) {
                NetworkListener cVar = this.l;
                if (cVar != null) {
                    cVar.a();
                    this.l = null;
                }
            } else {
                GpsListener bVar2 = this.k;
                if (bVar2 != null) {
                    bVar2.b();
                    this.k = null;
                }
                NetworkListener cVar2 = this.l;
                if (cVar2 != null) {
                    cVar2.a();
                    this.l = null;
                }
            }
            this.r = 0;
        }
        Logger.d("GeoListener", "mUseCount=" + this.r);
    }

    private void c(int i2) {
        if (this.s != null) {
            C0012a c0012a = this.t;
            if (c0012a != null) {
                c0012a.cancel();
            }
            C0012a c0012a2 = new C0012a();
            this.t = c0012a2;
            this.s.schedule(c0012a2, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GeoListener.java */
    /* renamed from: io.dcloud.js.geolocation.system.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0012a extends TimerTask {
        C0012a() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (GeoListener.this.k == null && GeoListener.this.l == null) {
                return;
            }
            GeoListener.this.a(GeoListener.b, "No location providers available.", GeoListener.h);
        }
    }
}
