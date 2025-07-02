package io.dcloud.dcloud_a;

import android.content.Context;

import io.dcloud.common.adapter.util.PlatformUtil;

/* compiled from: EncryptionConstant.java */
/* loaded from: classes.dex old a*/
public class EncryptionConstant {
    static char[] a = {'x', 'v', '/', 't', 'E', 'C', 'D', 'b', 'i', 'A', 'D', 'x', 'L', 'w', 'y', 'q', 'b', 'e', '0', 'G', 'b', 'f', 'G', '6', 't', 'c'};
    static char[] b = {'H', 'B', 'y', 'W', 'j', 'U', 'H', 'g', 'm', '3', 'C', 'p', 'Z', 'a', 'c', 'r', 'I', 'x', 'K', 'A', '4', 'J', 'S', 'W', 'k', 'y', 'b', 's', 'y', 'Y', 'c', 'O', 'y', '3', 'D', 'v', '2', 't', 'y', 'N', 'w', 'N', 'A', '6', 'N', 'a', 'D', 'O', 'u', 'P', 'i', 'R', 'K', 'b', 'p', 'N', 'y', 'q', 'A', '0', 'W', 'y', 'l', 'K', 'F', 'e', '9', 'X', 'q', 'm', 'e'};
    private static String c = "X7YBPqS7I0cDrWg8xDIfY0YVZCGmXWQ5ugQWJrvTAemwpG4BtP7JHZQa8SEE90C9\r/jOaV/jrBnYbCXcLfwIDAQAB\r";

    public static String a() {
        return "HTML5PLUSRUNTIME";
    }

    public static String a(Context context) {
        return context == null ? "" : PlatformUtil.getFileContent4S("libso.so") + "\r" + StringU.a(a, 0) + StringU.a(b) + "\r" + c;
    }
}
