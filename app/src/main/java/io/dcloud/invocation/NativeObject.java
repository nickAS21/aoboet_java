package io.dcloud.invocation;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.AbsoluteConst;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NativeObject.java */
/* loaded from: classes.dex old c*/
public class NativeObject {
    InvProxy a;
    Class b;
    Object c;
    String d;

    public void a() {
    }

    public NativeObject(IWebview iWebview, InvProxy aVar, Class cls, String str, JSONArray jSONArray) throws Exception {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.a = aVar;
        this.b = cls;
        this.d = str;
        this.c = a(iWebview, aVar, cls, jSONArray);
    }

    public NativeObject(InvProxy aVar, Class cls, String str, Object obj) {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.a = aVar;
        this.b = cls;
        this.d = str;
        this.c = obj;
    }

    public static Object a(IWebview iWebview, InvProxy aVar, Class cls, JSONArray jSONArray) throws Exception {
        Constructor<?> constructor;
        if (jSONArray != null) {
            Object[] a2 = a(iWebview, aVar, jSONArray);
            int i = 0;
            Class<?>[] clsArr = (Class[]) a2[0];
            Object[] objArr = (Object[]) a2[1];
            try {
                constructor = cls.getConstructor(clsArr);
            } catch (NoSuchMethodException unused) {
                Constructor<?>[] constructors = cls.getConstructors();
                while (true) {
                    if (i >= constructors.length) {
                        constructor = null;
                        break;
                    }
                    Class<?>[] parameterTypes = constructors[i].getParameterTypes();
                    if (parameterTypes.length == clsArr.length && a(parameterTypes, clsArr, objArr)) {
                        constructor = constructors[i];
                        break;
                    }
                    i++;
                }
            }
            if (constructor != null) {
                return constructor.newInstance(objArr);
            }
            return null;
        }
        return cls.newInstance();
    }

    public static void a(IWebview iWebview, InvProxy aVar, Class cls, Object obj, String str, JSONArray jSONArray) {
        Object[] a2 = a(iWebview, aVar, jSONArray);
        try {
            Field field = cls.getField(str);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, ((Object[]) a2[1])[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object a(Class cls, Object obj, String str) {
        try {
            Field field = cls.getField(str);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object a(IWebview iWebview, String str, JSONArray jSONArray) throws Exception {
        return b(iWebview, this.a, this.b, this.c, str, jSONArray);
    }

    public static final Object b(IWebview iWebview, InvProxy aVar, Class cls, Object obj, String str, JSONArray jSONArray) throws Exception {
        Object[] a2 = a(iWebview, aVar, jSONArray);
        Class[] clsArr = (Class[]) a2[0];
        Object[] objArr = (Object[]) a2[1];
        Method a3 = a(cls, str, clsArr, objArr);
        if (a3.getReturnType().equals(Void.class)) {
            a3.invoke(obj, objArr);
            return null;
        }
        return a3.invoke(obj, objArr);
    }

    static Method a(Class cls, String str, Class[] clsArr, Object[] objArr) {
        Method method = null;
        boolean equals = "getClass".equals(str);
        Class cls2 = cls;
        while (true) {
            if (equals || cls2 != Object.class) {
                try {
                    method = cls.getMethod(str, clsArr);
                } catch (NoSuchMethodException unused) {
                }
                if (method == null) {
                    continue;
//                    cls2 = cls2.getSuperclass();
                } else {
                    method.setAccessible(true);
                    return method;
                }
            } else {
                return b(cls, str, clsArr, objArr);
            }
        }
    }

    private static Method b(Class cls, String str, Class[] clsArr, Object[] objArr) {
        int length;
        try {
            Method[] declaredMethods = cls.getDeclaredMethods();
            for (int i = 0; i < declaredMethods.length; i++) {
                Method method = declaredMethods[i];
                if (method.getName().equals(str) && (length = method.getParameterTypes().length) == clsArr.length && (length == 0 || a(method.getParameterTypes(), clsArr, objArr))) {
                    return method;
                }
            }
            if (cls != Object.class) {
                return b(cls.getSuperclass(), str, clsArr, objArr);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean a(Class[] clsArr, Class[] clsArr2, Object[] objArr) {
        boolean z = false;
        for (int i = 0; i < clsArr.length; i++) {
            Class cls = clsArr2[i];
            Class cls2 = clsArr[i];
            z = cls == null ? !(b(cls2) || Boolean.TYPE.equals(cls2) || Boolean.class.equals(cls2) || Character.TYPE.equals(cls2) || Character.class.equals(cls2)) : cls2.isAssignableFrom(cls) || a(cls2, clsArr2, i) || c(cls2, clsArr2, i) || b(cls2, clsArr2, i) || (cls2.isArray() && cls2.isArray() && a(cls2, clsArr2, objArr, i));
            if (!z) {
                return z;
            }
        }
        return z;
    }

    static boolean a(Class cls, Class[] clsArr, int i) {
        boolean a2 = a(cls, clsArr[i]);
        if (a2) {
            clsArr[i] = cls;
        }
        return a2;
    }

    static boolean a(Class cls, Class cls2) {
        return (Number.class.isAssignableFrom(cls) && Number.class.isAssignableFrom(cls2)) || (b(cls) && b(cls2));
    }

    static Object[] a(Class cls) {
        String name = cls.getName();
        Object[] objArr = new Object[2];
        Class<?> cls2 = null;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i >= name.length()) {
                break;
            }
            char charAt = name.charAt(i);
            if (charAt == '[') {
                i2++;
            } else if (charAt == 'B') {
                cls2 = Byte.TYPE;
            } else if (charAt == 'I') {
                cls2 = Integer.TYPE;
            } else if (charAt == 'F') {
                cls2 = Float.TYPE;
            } else if (charAt == 'D') {
                cls2 = Double.TYPE;
            } else if (charAt == 'Z') {
                cls2 = Boolean.TYPE;
            } else if (charAt == 'J') {
                cls2 = Long.TYPE;
            } else if (charAt == 'S') {
                cls2 = Short.TYPE;
            } else if (charAt == 'L') {
                try {
                    cls2 = Class.forName(name.substring(i + 1));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    cls2 = Class.forName(name);
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
            i++;
        }
        objArr[0] = Integer.valueOf(i2);
        objArr[1] = cls2;
        return objArr;
    }

    static boolean a(Class cls, Class[] clsArr, Object[] objArr, int i) {
        if (cls == clsArr[i]) {
            return true;
        }
        Object[] a2 = a(cls);
        Object[] a3 = a(clsArr[i]);
        int parseInt = Integer.parseInt(String.valueOf(a2[0]));
        Class<?> cls2 = (Class) a2[1];
        int parseInt2 = Integer.parseInt(String.valueOf(a3[0]));
        Class cls3 = (Class) a3[1];
        if (parseInt == parseInt2 && (c(cls2, cls3) || b(cls2, cls3) || a((Class) cls2, cls3) || cls3.isAssignableFrom(cls2))) {
            try {
                Object a4 = a(objArr[i], cls2, parseInt, new a(parseInt));
                if (a4 != null) {
                    clsArr[i] = cls;
                    objArr[i] = a4;
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: NativeObject.java */
    /* loaded from: classes.dex */
    public static class a {
        private int[] a;

        a(int i) {
            this.a = null;
            this.a = new int[i];
        }

        int[] a(int i) {
            int[] iArr = this.a;
            if (i > iArr.length) {
                return null;
            }
            return Arrays.copyOfRange(iArr, iArr.length - i, iArr.length);
        }

        void a(int i, int i2) {
            int[] iArr = this.a;
            if (i > iArr.length) {
                return;
            }
            iArr[iArr.length - i] = i2;
        }
    }

    static Object a(Object obj, Class cls, int i, a aVar) throws Exception {
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            Object obj2 = null;
            for (int i2 = 0; i2 < length; i2++) {
                try {
                    Object a2 = a(Array.get(obj, i2), cls, i - 1, aVar);
                    if (obj2 == null) {
                        aVar.a(i, length);
                        if (i == 1) {
                            obj2 = Array.newInstance((Class<?>) cls, length);
                        } else {
                            obj2 = Array.newInstance((Class<?>) cls, aVar.a(i));
                        }
                    }
                    Array.set(obj2, i2, a2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w("test", "i=" + i2 + ";mAw=" + i);
                }
            }
            return obj2;
        }
        return a(obj, cls);
    }

    static boolean b(Class cls) {
        return cls.equals(Byte.TYPE) || cls.equals(Byte.class) || cls.equals(Integer.TYPE) || cls.equals(Integer.class) || cls.equals(Short.TYPE) || cls.equals(Short.class) || cls.equals(Float.TYPE) || cls.equals(Float.class) || cls.equals(Long.TYPE) || cls.equals(Long.class) || cls.equals(Double.TYPE) || cls.equals(Double.class);
    }

    static boolean b(Class cls, Class cls2) {
        return c(cls) && c(cls2);
    }

    static boolean b(Class cls, Class[] clsArr, int i) {
        boolean b = b(cls, clsArr[i]);
        if (b) {
            clsArr[i] = cls;
        }
        return b;
    }

    static boolean c(Class cls) {
        return Character.TYPE.equals(cls) || Character.class.equals(cls);
    }

    static boolean c(Class cls, Class cls2) {
        return d(cls) && d(cls2);
    }

    static boolean c(Class cls, Class[] clsArr, int i) {
        boolean c = c(cls, clsArr[i]);
        if (c) {
            clsArr[i] = cls;
        }
        return c;
    }

    static boolean d(Class cls) {
        return Boolean.TYPE.equals(cls) || Boolean.class.equals(cls);
    }

    static Object a(Object obj, Class cls) {
        if (cls == obj.getClass() || obj == null) {
            return obj;
        }
        if (cls == Byte.TYPE || cls == Byte.class) {
            return Byte.valueOf(Byte.parseByte(String.valueOf(obj)));
        }
        if (cls == Integer.TYPE || cls == Integer.class) {
            return Integer.valueOf(Integer.parseInt(String.valueOf(obj)));
        }
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return Boolean.valueOf(Boolean.parseBoolean(String.valueOf(obj)));
        }
        if (cls == Short.TYPE || cls == Short.class) {
            return Short.valueOf(Short.parseShort(String.valueOf(obj)));
        }
        if (cls == Long.TYPE || cls == Long.class) {
            return Long.valueOf(Long.parseLong(String.valueOf(obj)));
        }
        if (cls == Double.TYPE || cls == Double.class) {
            return Double.valueOf(Double.parseDouble(String.valueOf(obj)));
        }
        if (cls == Float.TYPE || cls == Float.class) {
            return Float.valueOf(Float.parseFloat(String.valueOf(obj)));
        }
        if (cls == Character.TYPE || cls == Character.class) {
            return String.valueOf(obj);
        }
        return cls.cast(obj);
    }

    static Class e(Class cls) {
        if (cls == Integer.class) {
            return Integer.TYPE;
        }
        return cls == Boolean.class ? Boolean.TYPE : cls;
    }

    static Object[] a(IWebview iWebview, InvProxy aVar, Object obj) {
        Object[] objArr = new Object[2];
        if (obj instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray) obj;
            int length = jSONArray.length();
            Object obj2 = null;
            for (int i = 0; i < length; i++) {
                Object[] a2 = a(iWebview, aVar, jSONArray.opt(i));
                Class<?> cls = a2[1].getClass();
                if (i == 0) {
                    obj2 = Array.newInstance((Class<?>) e(a2[1].getClass()), length);
                }
                if (cls == Boolean.class) {
                    Array.setBoolean(obj2, i, ((Boolean) a2[1]).booleanValue());
                } else if (cls == Byte.class) {
                    Array.setByte(obj2, i, ((Byte) a2[1]).byteValue());
                } else if (cls == Double.class) {
                    Array.setDouble(obj2, i, ((Double) a2[1]).doubleValue());
                } else if (cls == Float.class) {
                    Array.setFloat(obj2, i, ((Float) a2[1]).floatValue());
                } else if (cls == Integer.class) {
                    Array.setInt(obj2, i, ((Integer) a2[1]).intValue());
                } else if (cls == Long.class) {
                    Array.setLong(obj2, i, ((Long) a2[1]).longValue());
                } else if (cls instanceof Object) {
                    Array.set(obj2, i, a2[1]);
                }
            }
            if (obj2 != null) {
                objArr[0] = obj2.getClass();
                objArr[1] = obj2;
            }
        } else if (obj instanceof JSONObject) {
            JSONObject jSONObject = (JSONObject) obj;
            String optString = jSONObject.optString("type");
            Object opt = jSONObject.opt(IApp.ConfigProperty.CONFIG_VALUE);
            if (optString.equals("object")) {
                NativeObject a3 = aVar.a(iWebview, String.valueOf(opt));
                objArr[0] = a3.b;
                objArr[1] = a3.c;
            } else if (optString.equals("string")) {
                objArr[0] = String.class;
                objArr[1] = opt;
            } else if (optString.equals("number")) {
                if (opt instanceof Integer) {
                    objArr[0] = Integer.TYPE;
                    objArr[1] = Integer.valueOf(((Integer) opt).intValue());
                } else if (opt instanceof Double) {
                    objArr[0] = Double.class;
                    objArr[1] = (Double) opt;
                } else if (opt instanceof Float) {
                    objArr[0] = Float.class;
                    objArr[1] = (Float) opt;
                } else if (opt instanceof Long) {
                    objArr[0] = Long.class;
                    objArr[1] = (Long) opt;
                }
            } else if (optString.equals("boolean")) {
                objArr[0] = Boolean.class;
                objArr[1] = (Boolean) opt;
            } else if ("JSBObject".equals(jSONObject.optString("__TYPE__"))) {
                NativeObject a4 = aVar.a(iWebview, jSONObject.optString(AbsoluteConst.JSON_KEY_UUID));
                objArr[0] = a4.b;
                objArr[1] = a4.c;
            }
        } else {
            objArr[0] = obj.getClass();
            objArr[1] = obj;
        }
        return objArr;
    }

    protected static Object[] a(IWebview iWebview, InvProxy aVar, JSONArray jSONArray) {
        int length;
        Object[] objArr = new Object[2];
        if (jSONArray != null) {
            length = jSONArray.length();
        } else {
            length = 0;
        }
        Class[] clsArr = new Class[length];
        Object[] objArr2 = new Object[length];
        for (int i = 0; i < length; i++) {
            Object[] a2 = null;
            try {
                a2 = a(iWebview, aVar, jSONArray.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            clsArr[i] = (Class) a2[0];
            objArr2[i] = a2[1];
        }
        objArr[0] = clsArr;
        objArr[1] = objArr2;
        return objArr;
    }

    public boolean equals(Object obj) {
        return super.equals(obj) || this.c.equals(obj);
    }
}
