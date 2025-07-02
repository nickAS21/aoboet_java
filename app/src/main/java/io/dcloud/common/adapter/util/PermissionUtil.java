package io.dcloud.common.adapter.util;

import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.common_b.common_b_a.PermissionControler;

/* loaded from: classes.dex */
public class PermissionUtil implements IReflectAble {
    public static String convertNativePermission(String str) {
        return PermissionControler.d(str);
    }

    public static String checkPermission(IWebview iWebview, String[] strArr) {
        return PermissionControler.a(iWebview, strArr);
    }

    public static String convert5PlusValue(int i) {
        return PermissionControler.a(i);
    }
}
