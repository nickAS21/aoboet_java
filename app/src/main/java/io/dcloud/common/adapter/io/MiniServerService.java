package io.dcloud.common.adapter.io;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public class MiniServerService extends Service {
    String mServiceName = null;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (DeviceInfo.sDeviceSdkVer > 5 && intent != null) {
            this.mServiceName = intent.getStringExtra(AbsoluteConst.MINI_SERVER_NAME);
            startServer();
        }
        return super.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i) {
        if (DeviceInfo.sDeviceSdkVer <= 5 && intent != null) {
            this.mServiceName = intent.getStringExtra(AbsoluteConst.MINI_SERVER_NAME);
            startServer();
        }
        super.onStart(intent, i);
    }

    private void startServer() {
        AdaService serviceListener = AdaService.getServiceListener(this.mServiceName);
        if (serviceListener != null) {
            serviceListener.onExecute();
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        AdaService serviceListener = AdaService.getServiceListener(this.mServiceName);
        if (serviceListener != null) {
            serviceListener.onDestroy();
            AdaService.getServiceListener(this.mServiceName);
        }
        super.onDestroy();
    }
}
