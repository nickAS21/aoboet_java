package io.dcloud.common.adapter.util;

import android.os.IBinder;

import java.lang.reflect.Method;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISmartUpdate;

/* loaded from: classes.dex */
public class InvokeExecutorHelper {
    public static InvokeExecutor AppStreamUpdateManager = create("io.dcloud.streamdownload.AppStreamUpdateManager");
    public static InvokeExecutor AppidUtils = create("io.dcloud.streamdownload.utils.AppidUtils");
    public static InvokeExecutor StorageUtils = create("io.dcloud.streamdownload.utils.StorageUtils");
    public static InvokeExecutor AppStreamUtils = create("io.dcloud.streamdownload.utils.AppStreamUtils");
    public static InvokeExecutor QihooInnerStatisticUtil = create("com.qihoo.appstore.plugin.streamapp.QihooInnerStatisticUtil");
    public static InvokeExecutor QHPushHelper = create("com.qihoo.appstore.plugin.streamapp.QHPushHelper");
    public static InvokeExecutor StreamAppListActivity = create("io.dcloud.appstream.StreamAppMainActivity");
    public static InvokeExecutor TrafficFreeHelper = create("com.qihoo.appstore.utils.trafficFree.TrafficFreeHelper");
    public static InvokeExecutor TrafficProxy = create("com.qihoo.appstore.utils.traffic.TrafficProxy");
    public static InvokeExecutor AesEncryptionUtil = create("io.dcloud.feature.encryption.AesEncryptionUtil");
    public static InvokeExecutor RSAUtils = create("io.dcloud.feature.encryption.RSAUtils");
    public static InvokeExecutor DownloadTaskListManager = create("io.dcloud.streamdownload.DownloadTaskListManager");

    public static ISmartUpdate createWebAppSmartUpdater(IApp iApp) {
        return (ISmartUpdate) createInvokeExecutor("io.dcloud.appstream.WebAppSmartUpdater", new Class[]{IApp.class}, iApp).mObj;
    }

    public static InvokeExecutor createIDownloadService(IBinder iBinder) {
        InvokeExecutor invokeExecutor = new InvokeExecutor();
        try {
            invokeExecutor.mCls = Class.forName("io.dcloud.streamdownload.IDownloadService");
            invokeExecutor.mObj = Class.forName("io.dcloud.streamdownload.IDownloadService$Stub").getMethod("asInterface", IBinder.class).invoke(null, iBinder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invokeExecutor;
    }

    public static InvokeExecutor createDownloadTaskListManager() {
        InvokeExecutor invokeExecutor = new InvokeExecutor();
        try {
            invokeExecutor.mCls = Class.forName("io.dcloud.streamdownload.DownloadTaskListManager");
            invokeExecutor.mObj = Class.forName("io.dcloud.streamdownload.DownloadTaskListManager").getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invokeExecutor;
    }

    public static InvokeExecutor create(String str) {
        InvokeExecutor invokeExecutor = new InvokeExecutor();
        try {
            invokeExecutor.mCls = Class.forName(str);
        } catch (Exception unused) {
        }
        return invokeExecutor;
    }

    public static InvokeExecutor createInvokeExecutor(String str, Class[] clsArr, Object... objArr) {
        InvokeExecutor invokeExecutor = new InvokeExecutor();
        try {
            invokeExecutor.mCls = Class.forName(str);
            invokeExecutor.mObj = invokeExecutor.mCls.getConstructor(clsArr).newInstance(objArr);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("createInvokeExecutor clsName=" + str);
        }
        return invokeExecutor;
    }

    /* loaded from: classes.dex */
    public static class InvokeExecutor {
        Class mCls = null;
        Object mObj = null;

        public final InvokeExecutor setInstance(Object obj) {
            this.mObj = obj;
            return this;
        }

        public final boolean hasObject() {
            return this.mObj != null;
        }

        public final String invoke(String str) {
            Method method;
            try {
                Class cls = this.mCls;
                return (cls == null || (method = cls.getMethod(str, new Class[0])) == null) ? "" : (String) method.invoke(this.mObj, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        public final String invoke(String str, String str2) {
            Method method;
            try {
                Class cls = this.mCls;
                if (cls != null && (method = cls.getMethod(str, String.class)) != null) {
                    return (String) method.invoke(this.mObj, str2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str2;
        }

        public final int getInt(String str) {
            try {
                Class cls = this.mCls;
                if (cls != null) {
                    return ((Integer) cls.getField(str).get(this.mObj)).intValue();
                }
                return -10000;
            } catch (Exception e) {
                e.printStackTrace();
                return -10000;
            }
        }

        public final String getString(String str) {
            try {
                Class cls = this.mCls;
                if (cls != null) {
                    return (String) cls.getField(str).get(this.mObj);
                }
                return null;
            } catch (Exception unused) {
                return null;
            }
        }

        public final boolean invoke(String str, String str2, boolean z) {
            Method method;
            try {
                Class cls = this.mCls;
                if (cls != null && (method = cls.getMethod(str, String.class)) != null) {
                    return ((Boolean) method.invoke(this.mObj, str2)).booleanValue();
                }
            } catch (Exception unused) {
            }
            return z;
        }

        public final Object invoke(String str, Class[] clsArr, Object... objArr) {
            Method method;
            try {
                Class cls = this.mCls;
                if (cls == null || (method = cls.getMethod(str, clsArr)) == null) {
                    return null;
                }
                return method.invoke(this.mObj, objArr);
            } catch (Exception unused) {
                return null;
            }
        }
    }
}
