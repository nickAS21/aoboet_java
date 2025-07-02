package io.dcloud.common.util;

import com.demo.smarthome.service.Cfg;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class StringUtil {
    public static String trimString(String str, char c) {
        if (str == null || str.equals("")) {
            return str;
        }
        int i = str.charAt(0) == c ? 1 : 0;
        int length = str.length();
        int i2 = length - 1;
        if (str.charAt(i2) == c) {
            length = i2;
        }
        return str.substring(i, length);
    }

    public static String[] trimString(String[] strArr, char c) {
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = trimString(strArr[i], c);
        }
        return strArr;
    }

    public static String convert(String str) {
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(1000);
        stringBuffer.setLength(0);
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            stringBuffer.append("\\u");
            String hexString = Integer.toHexString(charAt >>> '\b');
            if (hexString.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(hexString);
            String hexString2 = Integer.toHexString(charAt & 255);
            if (hexString2.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(hexString2);
        }
        return new String(stringBuffer);
    }

    public static String revert(String str) {
        int i;
        int i2;
        if (str == null) {
            str = "";
        }
        if (str.indexOf("\\u") == -1) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(1000);
        int i3 = 0;
        while (i3 < str.length()) {
            if (str.substring(i3).startsWith("\\u")) {
                i = i3 + 6;
                String substring = str.substring(i3, i).substring(2);
                int i4 = 0;
                for (int i5 = 0; i5 < substring.length(); i5++) {
                    char charAt = substring.charAt(i5);
                    switch (charAt) {
                        case 'a':
                            i2 = 10;
                            break;
                        case 'b':
                            i2 = 11;
                            break;
                        case 'c':
                            i2 = 12;
                            break;
                        case Cfg.DEV_UDP_SEND_DELAY /* 100 */:
                            i2 = 13;
                            break;
                        case 'e':
                            i2 = 14;
                            break;
                        case 'f':
                            i2 = 15;
                            break;
                        default:
                            i2 = charAt - '0';
                            break;
                    }
                    i4 += i2 * ((int) Math.pow(16.0d, (substring.length() - i5) - 1));
                }
                stringBuffer.append((char) i4);
            } else {
                i = i3 + 1;
                stringBuffer.append(str.charAt(i3));
            }
            i3 = i;
        }
        return stringBuffer.toString();
    }

    public static String getSCString(String str, String str2) {
        if (str != null) {
            try {
                return new JSONObject(str).optString(str2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
