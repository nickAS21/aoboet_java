package io.dcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/* compiled from: DCloudRecevier.java */
/* loaded from: classes.dex */
class DCloudRecevier extends BroadcastReceiver {
    io.dcloud.feature.internal.reflect.BroadcastReceiver a;
    IntentFilter b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DCloudRecevier(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        this.a = null;
        this.b = null;
        this.a = broadcastReceiver;
        this.b = intentFilter;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        IntentFilter intentFilter;
        if (this.a == null || (intentFilter = this.b) == null || !intentFilter.hasAction(intent.getAction())) {
            return;
        }
        this.a.onReceive(context, intent);
    }
}
