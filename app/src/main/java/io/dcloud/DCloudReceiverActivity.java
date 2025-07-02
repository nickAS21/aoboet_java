package io.dcloud;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.util.ImageLoaderUtil;
import io.dcloud.feature.internal.reflect.BroadcastReceiver;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import io.src.dcloud.adapter.DCloudBaseActivity;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DCloudReceiverActivity.java */
/* loaded from: classes.dex */
public abstract class DCloudReceiverActivity extends DCloudBaseActivity implements IActivityHandler, IReflectAble {
    private static String c;
    static final InvokeExecutorHelper.InvokeExecutor e;
    static final int f;
    static final int g;
    static final int h;
    static final int i;
    static final int j;
    static final Class[] k;
    InvokeExecutorHelper.InvokeExecutor d;
    private HashMap<String, DCloudRecevier> a = new HashMap<>();
    private ServiceConnection b = new ServiceConnection() { // from class: io.dcloud.b.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DCloudReceiverActivity.this.d = InvokeExecutorHelper.createIDownloadService(iBinder);
            Logger.d(Logger.StreamApp_TAG, "onServiceConnected ;tService =" + (DCloudReceiverActivity.this.d != null));
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            DCloudReceiverActivity.this.d = null;
        }
    };
    Class[] l = {String.class, Integer.TYPE, String.class, String.class};
    final Class[] m = {String.class, Integer.TYPE, Integer.TYPE, String.class};
    final Class[] n = {String.class, String.class, String.class, Integer.TYPE, String.class, String.class};
    final Class[] o = {String.class, String.class};

    public boolean isStreamAppMode() {
        return true;
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AndroidResources.initAndroidResources(this.that);
        ImageLoaderUtil.initImageLoader(this.that);
        if (isStreamAppMode()) {
            bindDCloudServices();
        }
    }

    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String str, Handler handler) {
        Intent intent;
        DCloudRecevier cVar = new DCloudRecevier(broadcastReceiver, intentFilter);
        try {
            intent = registerReceiver(cVar, intentFilter, str, handler);
            try {
                this.a.put(broadcastReceiver.toString(), cVar);
            } catch (Exception e2) {
                e2.printStackTrace();
                return intent;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            intent = null;
        }
        return intent;
    }

    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        Intent intent;
        DCloudRecevier cVar = new DCloudRecevier(broadcastReceiver, intentFilter);
        try {
            intent = registerReceiver(cVar, intentFilter);
            try {
                this.a.put(broadcastReceiver.toString(), cVar);
            } catch (Exception e2) {
                e2.printStackTrace();
                return intent;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            intent = null;
        }
        return intent;
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        DCloudRecevier remove = this.a.remove(broadcastReceiver.toString());
        if (remove != null) {
            unregisterReceiver(remove);
        }
    }

    private void a() {
        Iterator<DCloudRecevier> it = this.a.values().iterator();
        while (it.hasNext()) {
            unregisterReceiver(it.next());
        }
        this.a.clear();
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        try {
            a();
            if (isStreamAppMode()) {
                b();
                unbindDCloudServices();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void b() {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), DCloudAdapterUtil.getDcloudDownloadService());
        intent.setAction("com.qihoo.appstore.plugin.streamapp.ACTION_PUSH_TO_PLUGIN");
        intent.putExtra("_____flag____", true);
        intent.putExtra("_____collect____", true);
        startService(intent);
    }

    public void unbindDCloudServices() {
        unbindService(this.b);
    }

    public void bindDCloudServices() {
        Class<?> cls;
        try {
            cls = Class.forName(DCloudAdapterUtil.getDcloudDownloadService());
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            cls = null;
        }
        if (cls != null) {
            bindService(new Intent(this.that, cls), this.b, Context.BIND_AUTO_CREATE);
            Logger.d(Logger.StreamApp_TAG, "bindDCloudServices");
        }
    }

    public boolean raiseFilePriority(String str, String str2) {
        Logger.d(Logger.StreamApp_TAG, "raiseFilePriority filePath=" + str + ";tService =" + (this.d != null));
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.d;
        if (invokeExecutor != null) {
            try {
                return invokeExecutor.invoke("setCurrentPage", getUrlByFilePath(str2, str), false);
            } catch (Exception e2) {
                Logger.d(Logger.StreamApp_TAG, "addAppStreamTask filePath=" + str, e2);
            }
        }
        return false;
    }

    static {
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = InvokeExecutorHelper.AppStreamUtils;
        e = invokeExecutor;
        f = invokeExecutor != null ? invokeExecutor.getInt("PRIORITY_MIN") : 0;
        g = invokeExecutor != null ? invokeExecutor.getInt("CONTRACT_STATUS_SUCCESS") : 0;
        h = invokeExecutor != null ? invokeExecutor.getInt("CONTRACT_TYPE_WAP2APP_INDEX_ZIP") : 0;
        i = invokeExecutor != null ? invokeExecutor.getInt("CONTRACT_TYPE_STREAM_MAIN_PAGE") : 0;
        j = invokeExecutor != null ? invokeExecutor.getInt("CONTRACT_TYPE_STREAM_APP") : 0;
        k = new Class[]{String.class, Integer.TYPE, String.class, String.class};
        c = "www/";
    }

    public void addAppStreamTask(String str, String str2) {
        Logger.d(Logger.StreamApp_TAG, "addAppStreamTask filePath=" + str + ";tService =" + (this.d != null));
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.d;
        if (invokeExecutor != null) {
            try {
                invokeExecutor.invoke("addSimpleFileTask", k, getUrlByFilePath(str2, str), Integer.valueOf(f), str, str2);
            } catch (Exception e2) {
                Logger.d(Logger.StreamApp_TAG, "addAppStreamTask filePath=" + str, e2);
            }
        }
    }

    public boolean queryUrl(String str, String str2) {
        try {
            boolean z = ((Integer) e.invoke("checkPageOrResourceStatus", new Class[]{String.class, String.class}, str, str2)).intValue() == g;
            Logger.d(Logger.StreamApp_TAG, "queryUrl filePath=" + str + ";ret =" + z);
            return z;
        } catch (Exception e2) {
            e2.printStackTrace();
            return true;
        }
    }

    public void resumeAppStreamTask(String str) {
        Logger.d(Logger.MAIN_TAG, "resumeAppStreamTask app=" + str + ";tService =" + (this.d != null));
        try {
            InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.d;
            if (invokeExecutor != null) {
                invokeExecutor.invoke("resumeDownload", str);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void downloadSimpleFileTask(IApp iApp, String str, String str2, String str3) {
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.d;
        if (invokeExecutor != null) {
            invokeExecutor.invoke("addSimpleFileTask", this.l, str, Integer.valueOf(f), str2, str3);
        }
    }

    public String getUrlByFilePath(String str, String str2) {
        String baseUrl = DataInterface.getBaseUrl();
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.d;
        if (invokeExecutor != null) {
            baseUrl = invokeExecutor.invoke("getAppRootUrl", str);
            if (TextUtils.isEmpty(baseUrl)) {
                try {
                    byte[] readAll = DHFile.readAll((String) InvokeExecutorHelper.AppStreamUtils.invoke("getJsonFilePath", new Class[]{String.class}, str));
                    baseUrl = new JSONObject(readAll != null ? new String(readAll) : "").getString("rooturl");
                } catch (Exception unused) {
                }
            }
        }
        return baseUrl + str2.substring(str2.indexOf(c) + c.length());
    }

    public Context getContext() {
        return this.that;
    }

    public void commitPointData0(String str, int i2, int i3, String str2) {
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.d;
        if (invokeExecutor != null) {
            invokeExecutor.invoke("commitPointData0", this.m, str, Integer.valueOf(i2), Integer.valueOf(i3), str2);
        }
    }

    public void commitPointData(String str, String str2, String str3, int i2, String str4, String str5) {
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.d;
        if (invokeExecutor != null) {
            invokeExecutor.invoke("commitPointData", this.n, str, str2, str3, Integer.valueOf(i2), str4, str5);
        }
    }

    public void commitActivateData(String str, String str2) {
        InvokeExecutorHelper.InvokeExecutor invokeExecutor = this.d;
        if (invokeExecutor != null) {
            invokeExecutor.invoke("commitActivateData", this.o, str, str2);
        }
    }
}
