package io.dcloud.js.geolocation.system;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;

import io.dcloud.common.adapter.util.Logger;

/* compiled from: GpsListener.java */
/* loaded from: classes.dex old b*/
public class GpsListener implements LocationListener {
    private Context b;
    private LocationManager c;
    private GeoListener d;
    private Location f;
    private boolean g;
    private boolean e = false;
    long a = System.currentTimeMillis();

    public GpsListener(Context context, GeoListener aVar) {
        this.g = false;
        this.d = aVar;
        this.b = context;
        this.c = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.g = false;
    }

    private void a(boolean z) {
        this.e = z;
        if (z) {
            this.a = System.currentTimeMillis();
        }
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
        this.g = false;
        if (this.e) {
            return;
        }
        this.d.a(GeoListener.b, "GPS provider disabled.", GeoListener.f);
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
        Logger.d("GpsListener: The provider " + str + " is enabled");
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
        Logger.d("GpsListener: The status of the provider " + str + " has changed");
        if (i == 0) {
            Logger.d("GpsListener: " + str + " is OUT OF SERVICE");
            this.d.a(GeoListener.b, "GPS out of service.", GeoListener.f);
        } else if (i == 1) {
            Logger.d("GpsListener: " + str + " is TEMPORARILY_UNAVAILABLE");
        } else {
            Logger.d("GpsListener: " + str + " is Available");
        }
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        Logger.d("GpsListener: The location has been updated!");
        a(true);
        this.f = location;
        this.d.a(location, GeoListener.f);
    }

    public boolean a() {
        if (!(System.currentTimeMillis() - this.a < 10000)) {
            this.e = false;
        }
        return this.e;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void a(int i) {
        if (this.g) {
            return;
        }
        this.g = true;
        this.c.requestLocationUpdates("gps", i, 0.0f, this);
    }

    public void b() {
        if (this.g) {
            this.c.removeUpdates(this);
        }
        this.g = false;
    }
}
