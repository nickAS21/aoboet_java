package io.dcloud.common.util;

/* loaded from: classes.dex */
public class JsEventUtil {
    public static String eventListener_format(String str, String str2, boolean z) {
        return String.format("{evt:'%s',args:" + (z ? "'%s'" : "%s") + "}", str, str2);
    }

    public static String broadcastEvents_format(String str, String str2, boolean z, String... strArr) {
        return String.format("{evt:'%s',args:" + (z ? "'%s'" : "%s") + ",callbackId:'%s'}", str, str2, strArr);
    }
}
