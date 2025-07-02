package io.dcloud.common.DHInterface;

import org.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import io.dcloud.common.util.JSUtil;

/* loaded from: classes.dex */
public class StandardFeature extends BaseFeature implements IReflectAble {
    private HashMap<String, Method> mInnerClassMethod = null;

    @Override // io.dcloud.common.DHInterface.BaseFeature
    public final String execute(IWebview iWebview, String str, JSONArray jSONArray) {
        return executeAction(str, iWebview, jSONArray);
    }

    private String executeAction(String str, IWebview iWebview, JSONArray jSONArray) {
        String wrapJsVar;
        Method method = this.mInnerClassMethod.get(str);
        if (method != null) {
            try {
                Object invoke = method.invoke(this, iWebview, jSONArray);
                if (invoke != null) {
                    return invoke.toString();
                }
                return null;
            } catch (IllegalAccessException e) {
                wrapJsVar = JSUtil.wrapJsVar(e.getMessage());
                e.printStackTrace();
                return wrapJsVar;
            } catch (IllegalArgumentException e2) {
                wrapJsVar = JSUtil.wrapJsVar(e2.getMessage());
                e2.printStackTrace();
                return wrapJsVar;
            } catch (InvocationTargetException e3) {
                wrapJsVar = JSUtil.wrapJsVar(e3.getMessage());
                e3.printStackTrace();
                return wrapJsVar;
            }
        }
        return JSUtil.wrapJsVar("not found the " + str + " function");
    }

    @Override // io.dcloud.common.DHInterface.BaseFeature, io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        super.init(absMgr, str);
        arrangeInnerMethod();
    }

    private void arrangeInnerMethod() {
        this.mInnerClassMethod = new HashMap<>(1);
        for (Method method : getClass().getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && isStandardFeatureMethod(method.getParameterTypes())) {
                this.mInnerClassMethod.put(method.getName(), method);
            }
        }
    }

    private boolean isStandardFeatureMethod(Class[] clsArr) {
        if (clsArr != null) {
            try {
                if (clsArr.length == 2 && clsArr[0].equals(IWebview.class)) {
                    if (clsArr[1].equals(JSONArray.class)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
