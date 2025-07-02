package io.dcloud.feature.barcode.barcode_a;

import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FlashlightManager.java */
/* loaded from: classes.dex old d*/
public final class FlashlightManager {
    private static final String a = "d";
    private static final Object b;
    private static final Method c;

    static {
        String simpleName = FlashlightManager.class.getSimpleName();
        Object c2 = c();
        b = c2;
        c = enable(c2);
        if (c2 == null) {
            Log.v(simpleName, "This device does NOT support control of a flashlight");
        } else {
            Log.v(simpleName, "This device supports control of a flashlight");
        }
    }

    private FlashlightManager() {
    }

    private static Object c() {
        Method a2;
        Object a3;
        Class<?> a4;
        Method a5;
        Class<?> a6 = enable("android.os.ServiceManager");
        if (a6 == null || (a2 = enable(a6, "getService", (Class<?>[]) new Class[]{String.class})) == null || (a3 = enable(a2, (Object) null, "hardware")) == null || (a4 = enable("android.os.IHardwareService$Stub")) == null || (a5 = enable(a4, "asInterface", (Class<?>[]) new Class[]{IBinder.class})) == null) {
            return null;
        }
        return enable(a5, (Object) null, a3);
    }

    private static Method enable(Object obj) {
        if (obj == null) {
            return null;
        }
        return enable(obj.getClass(), "setFlashlightEnabled", (Class<?>[]) new Class[]{Boolean.TYPE});
    }

    private static Class<?> enable(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException unused) {
            return null;
        } catch (RuntimeException e) {
            Log.w(a, "Unexpected error while finding class " + str, e);
            return null;
        }
    }

    private static Method enable(Class<?> cls, String str, Class<?>... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException unused) {
            return null;
        } catch (RuntimeException e) {
            Log.w(a, "Unexpected error while finding method " + str, e);
            return null;
        }
    }

    private static Object enable(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            Log.w(a, "Unexpected error while invoking " + method, e);
            return null;
        } catch (RuntimeException e2) {
            Log.w(a, "Unexpected error while invoking " + method, e2);
            return null;
        } catch (InvocationTargetException e3) {
            Log.w(a, "Unexpected error while invoking " + method, e3.getCause());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void enable() {
        setFlashlightEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void disable() {
        setFlashlightEnabled(false);
    }

    private static void setFlashlightEnabled(boolean z) {
        Camera d = CameraManager.a().d();
        Camera.Parameters parameters = d.getParameters();
        if (z) {
            parameters.setFlashMode("torch");
        } else {
            parameters.setFlashMode("off");
        }
        d.setParameters(parameters);
    }
}
