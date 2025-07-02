package io.dcloud.common.adapter.io;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public abstract class AdaService {
    static final String TAG = "AdaService";
    static HashMap<String, AdaService> mServicesHandler = new HashMap<>(2);
    protected Context mContextWrapper;
    private String mServiceName;

    public abstract void onDestroy();

    public abstract void onExecute();

    /* JADX INFO: Access modifiers changed from: protected */
    public AdaService(Context context, String str) {
        this.mContextWrapper = null;
        this.mServiceName = null;
        this.mContextWrapper = context;
        this.mServiceName = str;
    }

    public final void startMiniServer() {
        mServicesHandler.put(this.mServiceName, this);
        Intent intent = new Intent(this.mContextWrapper, (Class<?>) MiniServerService.class);
        intent.putExtra(AbsoluteConst.MINI_SERVER_NAME, this.mServiceName);
        this.mContextWrapper.startService(intent);
        Logger.d(TAG, "pname=" + this.mContextWrapper.getPackageName() + " startMiniServer");
    }

    public final void stopMiniServer() {
        Intent intent = new Intent(this.mContextWrapper, (Class<?>) MiniServerService.class);
        intent.putExtra(AbsoluteConst.MINI_SERVER_NAME, this.mServiceName);
        this.mContextWrapper.stopService(intent);
        Logger.d(TAG, "pname=" + this.mContextWrapper.getPackageName() + " stopMiniServer");
    }

    public static final AdaService getServiceListener(String str) {
        return mServicesHandler.get(str);
    }

    public static final void removeServiceListener(String str) {
        mServicesHandler.remove(str);
    }
}
