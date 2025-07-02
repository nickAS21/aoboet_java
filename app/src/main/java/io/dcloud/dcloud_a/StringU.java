package io.dcloud.dcloud_a;

import io.dcloud.feature.ui.nativeui.WaitingView;

/* compiled from: StringU.java */
/* loaded from: classes.dex old c*/
public class StringU {
    public static String a(char[] cArr) {
        return new String(cArr).replaceAll("y", "");
    }

    public static String a(char[] cArr, int i) {
        return new String(cArr, 2, cArr.length - 3).replaceAll(WaitingView.a, "");
    }
}
