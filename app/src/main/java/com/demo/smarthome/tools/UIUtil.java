package com.demo.smarthome.tools;

import android.content.Context;
import android.widget.Toast;

/* loaded from: classes.dex */
public class UIUtil {
    public static void toastShow(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void toastShow(Context context, int i) {
        Toast.makeText(context, i, Toast.LENGTH_SHORT).show();
    }
}
