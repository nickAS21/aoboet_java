package io.dcloud.js.geolocation.system;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;

import io.dcloud.common.adapter.util.Logger;

/* compiled from: NetworkListener.java */
/* loaded from: classes.dex old c*/
public class NetworkListener implements LocationListener {
    private Context a;
    private LocationManager b;
    private GeoListener c;
    private boolean d = false;
    private Location e;
    private boolean f;

    public NetworkListener(Context context, GeoListener aVar) {
        this.f = false;
        this.c = aVar;
        this.a = context;
        this.b = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.f = false;
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
        this.f = false;
        if (!this.d && (this.c.k == null || !this.c.k.a())) {
            this.c.a(GeoListener.b, "The provider " + str + " is disabled", GeoListener.g);
        }
        Logger.d("NetworkListener: The provider " + str + " is disabled");
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
        Logger.d("NetworkListener: The provider " + str + " is enabled");
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
        Logger.d("NetworkListener: The status of the provider " + str + " has changed");
        if (i == 0) {
            Logger.d("NetworkListener: " + str + " is OUT OF SERVICE");
        } else if (i == 1) {
            Logger.d("NetworkListener: " + str + " is TEMPORARILY_UNAVAILABLE");
        } else {
            Logger.d("NetworkListener: " + str + " is Available");
        }
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        Logger.d("NetworkListener: The location has been updated!");
        this.d = true;
        this.e = location;
        this.c.a(location, GeoListener.g);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void a(int i) {
        if (this.f) {
            return;
        }
        this.f = true;
        this.b.requestLocationUpdates("network", i, 0.0f, this);
    }

    public void a() {
        if (this.f) {
            this.b.removeUpdates(this);
        }
        this.f = false;
    }
}
