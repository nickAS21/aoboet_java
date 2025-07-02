package com.demo.smarthome.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes.dex */
public class DateTools {
    public static String getNowTimeString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static long getNowTimeByLastTimeDifference(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long j = -1;
        try {
            Date date = new Date(System.currentTimeMillis());
            Date parse = simpleDateFormat.parse(str);
            j = (date.getTime() - parse.getTime()) / 1000;
            System.out.println("newTime:" + date.toString() + "  d2:" + parse.toString() + "   strTime:" + str + "    val:" + j);
            return j;
        } catch (Exception unused) {
            return j;
        }
    }
}
