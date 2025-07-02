package io.dcloud.common.util;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class ReflectUtils {
    public static final String CLASSNAME_APPLICATIONINFO = "android.content.pm.ApplicationInfo";
    public static final String CLASSNAME_AUDIOSYSTEM = "android.media.AudioSystem";
    public static final String CLASSNAME_ICONTENTPROVIDER = "android.content.IContentProvider";
    public static final String CLASSNAME_IMOUNTSERVICE_STUB = "android.os.storage.IMountService$Stub";
    public static final String CLASSNAME_IPACKAGEDATAOBSERVER = "android.content.pm.IPackageDataObserver";
    public static final String CLASSNAME_IPACKAGEDELETEOBSERVER = "android.content.pm.IPackageDeleteObserver";
    public static final String CLASSNAME_IPACKAGEINSTALLOBERVER = "android.content.pm.IPackageInstallObserver";
    public static final String CLASSNAME_IPACKAGEMANAGER = "android.content.pm.IPackageManager";
    public static final String CLASSNAME_IPACKAGEMANAGER_STUB = "android.content.pm.IPackageManager$Stub";
    public static final String CLASSNAME_IPACKAGESTATSOBSERVER = "android.content.pm.IPackageStatsObserver";
    public static final String CLASSNAME_PACKAGEINFO = "android.content.pm.PackageInfo";
    public static final String CLASSNAME_PACKAGEMANAGER = "android.content.pm.PackageManager";
    public static final String CLASSNAME_PAGEAGEPARSE = "android.content.pm.PackageParser";
    public static final String CLASSNAME_PAGEAGEPARSE_PACKAGE = "android.content.pm.PackageParser$Package";
    public static final String CLASSNAME_PROCESS = "android.os.Process";
    public static final String CLASSNAME_THREADS = "android.provider.Telephony$Threads";
    public static final String CLASSNAME_THUMBNAILUTILS = "android.media.ThumbnailUtils";

    public static Method getDeclaredMethod(Object obj, String str, Class<?>... clsArr) {
        for (Class<?> cls = obj.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            try {
                return cls.getDeclaredMethod(str, clsArr);
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public static Object invokeMethod(Object obj, String str, Class<?>[] clsArr, Object[] objArr) {
        Method declaredMethod = getDeclaredMethod(obj, str, clsArr);
        declaredMethod.setAccessible(true);
        if (declaredMethod == null) {
            return null;
        }
        try {
            return declaredMethod.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return null;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static Field getDeclaredField(Object obj, String str) {
        for (Class<?> cls = obj.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            try {
                return cls.getDeclaredField(str);
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public static void setFieldValue(Object obj, String str, Object obj2) {
        Field declaredField = getDeclaredField(obj, str);
        declaredField.setAccessible(true);
        try {
            declaredField.set(obj, obj2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        }
    }

    public static Object getFieldValue(Object obj, String str) {
        Field declaredField = getDeclaredField(obj, str);
        declaredField.setAccessible(true);
        try {
            return declaredField.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class classForName(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException unused) {
            throw new RuntimeException(str);
        }
    }

    public static Object stubAsInterface(String str, IBinder iBinder) {
        return stubAsInterface(classForName(str), iBinder);
    }

    public static Object stubAsInterface(Class cls, IBinder iBinder) {
        try {
            return cls.getDeclaredMethod("asInterface", IBinder.class).invoke(null, iBinder);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    public static Method getMethod(String str, String str2, Class<?>... clsArr) {
        try {
            return Class.forName(str).getDeclaredMethod(str2, clsArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr) {
        if (str != null && str.length() > 0) {
            try {
                return cls.getMethod(str, clsArr);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Object invoke(Object obj, Method method, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSystemProperties(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class, String.class).invoke(null, str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static Context getApplicationContext() {
        try {
            return (Context) Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Constructor getObjectConstructor(String str, Class... clsArr) {
        try {
            return Class.forName(str).getConstructor(clsArr);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static Object getField(Object obj, String str) throws NoSuchFieldException, IllegalAccessException {
        return prepareField(obj.getClass(), str).get(obj);
    }

    public static void setField(Object obj, String str, Object obj2) throws NoSuchFieldException, IllegalAccessException {
        prepareField(obj.getClass(), str).set(obj, obj2);
    }

    public static int getIntField(Object obj, String str) {
        try {
            return obj.getClass().getDeclaredField(str).getInt(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static Object getObjectField(Object obj, String str) {
        try {
            return obj.getClass().getDeclaredField(str).get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static Object getObjectFieldNoDeclared(Object obj, String str) {
        try {
            return obj.getClass().getField(str).get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static int getStaticIntField(String str, String str2) {
        try {
            return Class.forName(str).getDeclaredField(str2).getInt(null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        } catch (NoSuchFieldException e3) {
            throw new RuntimeException(e3);
        }
    }

    public static Object getStaticObjectField(String str, String str2) {
        try {
            return Class.forName(str).getDeclaredField(str2).get(null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        } catch (NoSuchFieldException e3) {
            throw new RuntimeException(e3);
        }
    }

    public static String getStaticStringField(String str, String str2) {
        return (String) getStaticObjectField(str, str2);
    }

    private static Field prepareField(Class<?> cls, String str) throws NoSuchFieldException {
        if (cls != null) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField;
            } finally {
                cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException();
    }

    public static void windowDismissed(InputMethodManager inputMethodManager, IBinder iBinder) {
        try {
            inputMethodManager.getClass().getMethod("windowDismissed", IBinder.class).invoke(inputMethodManager, iBinder);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    public static void modifyPushPriority(Object obj, String str, Object obj2) {
        try {
            Field declaredField = obj.getClass().getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }

    public static void modifyPushBigContentView(Object obj, String str, Object obj2) {
        try {
            Field declaredField = obj.getClass().getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }
}
