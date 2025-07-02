package io.dcloud.invocation;

import org.json.JSONArray;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;

/* compiled from: InvocationHandlerImpl.java */
/* loaded from: classes.dex  old b*/
public class InvocationHandlerImpl implements InvocationHandler {
    String a = null;
    Object b = null;
    Class c;
    IWebview d;
    String e;
    ArrayList<String> f;

    public InvocationHandlerImpl(IWebview iWebview, String str, JSONArray jSONArray, String str2) {
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        try {
            this.d = iWebview;
            this.e = str2;
            this.f = new ArrayList<>();
            for (int i = 0; i < jSONArray.length(); i++) {
                this.f.add(JSONUtil.getString(jSONArray, i));
            }
            this.c = Class.forName(str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        }
    }

    public Object a(JSONArray jSONArray) {
        Object newProxyInstance = Proxy.newProxyInstance(this.c.getClassLoader(), new Class[]{this.c}, this);
        this.b = newProxyInstance;
        return newProxyInstance;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        StringBuffer stringBuffer = new StringBuffer("[");
        String name = method.getName();
        if (this.f.contains(name)) {
            if (objArr != null) {
                int length = objArr.length;
                for (int i = 0; i < length; i++) {
                    if (objArr[i] == null) {
                        stringBuffer.append("undefined");
                    } else {
                        stringBuffer.append(InvProxy.b(this.d, objArr[i]));
                    }
                    if (i != length - 1) {
                        stringBuffer.append(JSUtil.COMMA);
                    }
                }
            }
            stringBuffer.append("]");
            JSUtil.execCallback(this.d, this.e, String.format("{method:'%s',arguments:%s}", method.getName(), stringBuffer.toString()), JSUtil.OK, true, true);
            return null;
        }
        if ("hashCode".equals(name)) {
            return Integer.valueOf(hashCode());
        }
        if ("equals".equals(name)) {
            return Boolean.valueOf(equals(objArr));
        }
        if ("getClass".equals(name)) {
            return this.c;
        }
        if ("finalize".equals(name)) {
            finalize();
        } else if ("notify".equals(name)) {
            notify();
        } else if ("notifyAll".equals(name)) {
            notifyAll();
        } else {
            if ("toString".equals(name)) {
                return this.a + this.c.toString();
            }
            if ("wait".equals(name)) {
                if (objArr.length == 0) {
                    wait();
                } else if (objArr.length == 1) {
                    wait(Long.parseLong(String.valueOf(objArr[0])));
                } else if (objArr.length == 2) {
                    wait(Long.parseLong(String.valueOf(objArr[0])), Integer.parseInt(String.valueOf(objArr[0])));
                }
            } else {
                try {
                    return method.invoke(this.b, objArr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
