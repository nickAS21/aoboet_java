package io.dcloud.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public class PdrUtil {
    public static boolean checkAlphaTransparent(int i) {
        return (i == -1 || (i >>> 24) == 255) ? false : true;
    }

    public static String encodeURL(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException unused) {
            Logger.e("URLEncode error str=" + str);
            return URLEncoder.encode(str);
        }
    }

    public static long parseLong(String str, long j) {
        try {
            return Long.parseLong(str);
        } catch (Exception unused) {
            return j;
        }
    }

    public static Object getObject(Object[] objArr, int i) {
        try {
            return objArr[i];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean parseBoolean(String str, boolean z, boolean z2) {
        return (str == null || str.equals("")) ? z : str.equalsIgnoreCase(AbsoluteConst.TRUE) ? (!z2) & true : str.equalsIgnoreCase(AbsoluteConst.FALSE) ? z2 | false : z;
    }

    public static String getUrlPathName(String str) {
        return str != null ? URLUtil.stripAnchor(stripQuery(str)) : str;
    }

    public static String stripAnchor(String str) {
        return URLUtil.stripAnchor(str);
    }

    public static String stripQuery(String str) {
        int indexOf = str.indexOf(63);
        return indexOf != -1 ? str.substring(0, indexOf) : str;
    }

    public static String standardizedURL(String str, String str2) {
        String stripQuery = stripQuery(stripAnchor(str));
        if (str2.startsWith("./")) {
            str2 = str2.substring(2);
            int lastIndexOf = stripQuery.lastIndexOf(47);
            if (lastIndexOf >= 0) {
                return stripQuery.substring(0, lastIndexOf + 1) + str2;
            }
        }
        int indexOf = str2.indexOf("../");
        int lastIndexOf2 = stripQuery.lastIndexOf(47);
        if (!(lastIndexOf2 > -1)) {
            return str2;
        }
        String substring = stripQuery.substring(0, lastIndexOf2);
        while (indexOf > -1) {
            str2 = str2.substring(3);
            substring = substring.substring(0, substring.lastIndexOf(47));
            indexOf = str2.indexOf("../");
        }
        return substring + '/' + str2;
    }

    public static int parseInt(String str, int i) {
        if (str == null) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return i;
        }
    }

    public static float parseFloat(String str, float f) {
        if (str == null) {
            return f;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception unused) {
            return f;
        }
    }

    public static float parseFloat(String str, float f, float f2) {
        if (str == null) {
            return f2;
        }
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith("px")) {
            lowerCase = lowerCase.substring(0, lowerCase.length() - 2);
        }
        try {
            return Float.parseFloat(lowerCase);
        } catch (NumberFormatException unused) {
            if (lowerCase.endsWith("%")) {
                try {
                    return (f * Float.parseFloat(lowerCase.substring(0, lowerCase.length() - 1))) / 100.0f;
                } catch (Exception unused2) {
                    return f2;
                }
            }
            return f2;
        }
    }

    public static int convertToScreenInt(String str, int i, int i2, float f) {
        if (str == null) {
            return i2;
        }
        try {
            if (str.endsWith("px")) {
                String substring = str.substring(0, str.length() - 2);
                if (substring != null && substring.contains(".")) {
                    return (int) (Float.parseFloat(substring) * f);
                }
                return (int) (Integer.parseInt(substring) * f);
            }
            if (str.endsWith("%")) {
                try {
                    return (i * Integer.parseInt(str.substring(0, str.length() - 1))) / 100;
                } catch (NumberFormatException unused) {
                    return i2;
                }
            }
            return (int) (Double.parseDouble(str) * f);
        } catch (Exception unused2) {
            return i2;
        }
    }

    public static int parseInt(String str, int i, int i2) {
        if (str == null) {
            return i2;
        }
        try {
            String lowerCase = str.toLowerCase();
            if (lowerCase.endsWith("px")) {
                return Integer.parseInt(lowerCase.substring(0, lowerCase.length() - 2));
            }
            if (lowerCase.endsWith("%")) {
                try {
                    return (i * Integer.parseInt(lowerCase.substring(0, lowerCase.length() - 1))) / 100;
                } catch (NumberFormatException unused) {
                    return i2;
                }
            }
            if (lowerCase.startsWith("#")) {
                lowerCase = "0x" + lowerCase.substring(1);
            }
            if (lowerCase.startsWith("0x")) {
                return Integer.valueOf(lowerCase.substring(2), 16).intValue();
            }
            return Integer.parseInt(lowerCase);
        } catch (Exception unused2) {
            return i2;
        }
    }

    private static void loadProperties2HashMap(HashMap<String, String> hashMap, String str) {
        InputStream resInputStream = PlatformUtil.getResInputStream(str);
        Properties properties = new Properties();
        try {
            try {
                properties.load(resInputStream);
                Enumeration<?> propertyNames = properties.propertyNames();
                if (propertyNames != null) {
                    while (propertyNames.hasMoreElements()) {
                        String str2 = (String) propertyNames.nextElement();
                        hashMap.put(str2.toLowerCase(), (String) properties.get(str2));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            IOUtil.close(resInputStream);
        }
    }

    public static Object getKeyByValue(HashMap hashMap, Object obj, boolean z) {
        if (z && !hashMap.containsValue(obj)) {
            return null;
        }
        for (Object obj2 : hashMap.keySet()) {
            Object obj3 = hashMap.get(obj2);
            if (obj3 != null && obj3.equals(obj)) {
                return obj2;
            }
        }
        return null;
    }

    public static Object getKeyByIndex(HashMap hashMap, int i) {
        if (i < 0) {
            return null;
        }
        int i2 = 0;
        for (Object obj : hashMap.keySet()) {
            if (i == i2) {
                return obj;
            }
            i2++;
        }
        return null;
    }

    public static Object getKeyByValue(HashMap hashMap, Object obj) {
        return getKeyByValue(hashMap, obj, false);
    }

    public static void loadProperties2HashMap(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, HashMap<String, String>> hashMap3, String str) {
        XmlUtil.DHNode XML_Parser;
        InputStream resInputStream = PlatformUtil.getResInputStream(str);
        if (resInputStream == null || (XML_Parser = XmlUtil.XML_Parser(resInputStream)) == null) {
            return;
        }
        ArrayList<XmlUtil.DHNode> elements = XmlUtil.getElements(XmlUtil.getElement(XML_Parser, IApp.ConfigProperty.CONFIG_FEATURES), IApp.ConfigProperty.CONFIG_FEATURE);
        if (elements != null && !elements.isEmpty()) {
            Iterator<XmlUtil.DHNode> it = elements.iterator();
            while (it.hasNext()) {
                XmlUtil.DHNode next = it.next();
                String lowerCase = XmlUtil.getAttributeValue(next, "name").toLowerCase();
                String attributeValue = XmlUtil.getAttributeValue(next, IApp.ConfigProperty.CONFIG_VALUE);
                if (AbsoluteConst.F_UI.equals(lowerCase)) {
                    hashMap2.put("webview", attributeValue);
                }
                hashMap2.put(lowerCase, attributeValue);
                ArrayList<XmlUtil.DHNode> elements2 = XmlUtil.getElements(next, IApp.ConfigProperty.CONFIG_MODULE);
                if (elements2 != null && !elements2.isEmpty()) {
                    HashMap<String, String> hashMap4 = hashMap3.get(lowerCase);
                    if (hashMap4 == null) {
                        hashMap4 = new LinkedHashMap<>(2);
                        hashMap3.put(lowerCase, hashMap4);
                    }
                    Iterator<XmlUtil.DHNode> it2 = elements2.iterator();
                    while (it2.hasNext()) {
                        XmlUtil.DHNode next2 = it2.next();
                        hashMap4.put(XmlUtil.getAttributeValue(next2, "name").toLowerCase(), XmlUtil.getAttributeValue(next2, IApp.ConfigProperty.CONFIG_VALUE));
                    }
                }
            }
        }
        ArrayList<XmlUtil.DHNode> elements3 = XmlUtil.getElements(XmlUtil.getElement(XML_Parser, IApp.ConfigProperty.CONFIG_SERVICES), "service");
        if (elements3 == null || elements3.isEmpty()) {
            return;
        }
        Iterator<XmlUtil.DHNode> it3 = elements3.iterator();
        while (it3.hasNext()) {
            XmlUtil.DHNode next3 = it3.next();
            hashMap.put(XmlUtil.getAttributeValue(next3, "name").toLowerCase(), XmlUtil.getAttributeValue(next3, IApp.ConfigProperty.CONFIG_VALUE));
        }
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || obj.equals("") || (obj.toString().length() == 4 && obj.toString().toLowerCase().equals("null"));
    }

    public static String getNonString(String str, String str2) {
        return isEmpty(str) ? str2 : str;
    }

    public static boolean isEquals(String str, String str2) {
        if (isEmpty(str) || isEmpty(str2)) {
            return false;
        }
        return str.equalsIgnoreCase(str2);
    }

    public static boolean isContains(String str, String str2) {
        if (isEmpty(str) || isEmpty(str2)) {
            return false;
        }
        return str.contains(str2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0096, code lost:
    
        if (r0 != 7) goto L42;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int stringToColor(java.lang.String r8) {
        /*
            int r0 = r8.length()     // Catch: java.lang.Exception -> Lc7
            r1 = 16
            r2 = 7
            r3 = 4
            r4 = 0
            r5 = 3
            r6 = 1
            if (r0 == r3) goto L94
            if (r0 == r2) goto L94
            if (r0 == r5) goto L94
            r7 = 6
            if (r0 != r7) goto L16
            goto L94
        L16:
            r0 = 0
            java.lang.String r2 = "rgba"
            boolean r2 = r8.startsWith(r2)     // Catch: java.lang.Exception -> Lc7
            java.lang.String r7 = ","
            if (r2 == 0) goto L31
            r0 = 5
            int r2 = r8.length()     // Catch: java.lang.Exception -> Lc7
            int r2 = r2 - r6
            java.lang.String r8 = r8.substring(r0, r2)     // Catch: java.lang.Exception -> Lc7
            java.lang.String[] r0 = r8.split(r7)     // Catch: java.lang.Exception -> Lc7
            r8 = r6
            goto L47
        L31:
            java.lang.String r2 = "rgb"
            boolean r2 = r8.startsWith(r2)     // Catch: java.lang.Exception -> Lc7
            if (r2 == 0) goto L46
            int r0 = r8.length()     // Catch: java.lang.Exception -> Lc7
            int r0 = r0 - r6
            java.lang.String r8 = r8.substring(r3, r0)     // Catch: java.lang.Exception -> Lc7
            java.lang.String[] r0 = r8.split(r7)     // Catch: java.lang.Exception -> Lc7
        L46:
            r8 = r4
        L47:
            r2 = 255(0xff, float:3.57E-43)
            if (r0 == 0) goto L87
            r3 = 2
            if (r8 != 0) goto L61
            r8 = r0[r4]     // Catch: java.lang.Exception -> L83
            int r8 = java.lang.Integer.parseInt(r8)     // Catch: java.lang.Exception -> L83
            r4 = r0[r6]     // Catch: java.lang.Exception -> L81
            int r4 = java.lang.Integer.parseInt(r4)     // Catch: java.lang.Exception -> L81
            r0 = r0[r3]     // Catch: java.lang.Exception -> L7f
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.Exception -> L7f
            goto L8a
        L61:
            r8 = r0[r4]     // Catch: java.lang.Exception -> L83
            int r8 = java.lang.Integer.parseInt(r8)     // Catch: java.lang.Exception -> L83
            r4 = r0[r6]     // Catch: java.lang.Exception -> L81
            int r4 = java.lang.Integer.parseInt(r4)     // Catch: java.lang.Exception -> L81
            r3 = r0[r3]     // Catch: java.lang.Exception -> L7f
            int r3 = java.lang.Integer.parseInt(r3)     // Catch: java.lang.Exception -> L7f
            float r6 = (float) r2
            r0 = r0[r5]     // Catch: java.lang.Exception -> L8b
            float r0 = java.lang.Float.parseFloat(r0)     // Catch: java.lang.Exception -> L8b
            float r6 = r6 * r0
            int r0 = (int) r6
            r2 = r0
            r0 = r3
            goto L8a
        L7f:
            r3 = r2
            goto L8b
        L81:
            r3 = r2
            goto L85
        L83:
            r8 = r2
            r3 = r8
        L85:
            r4 = r3
            goto L8b
        L87:
            r8 = r2
            r0 = r8
            r4 = r0
        L8a:
            r3 = r0
        L8b:
            int r0 = r2 << 24
            int r8 = r8 << r1
            int r0 = r0 + r8
            int r8 = r4 << 8
            int r0 = r0 + r8
            int r0 = r0 + r3
            goto Lc8
        L94:
            if (r0 == r3) goto L98
            if (r0 != r2) goto L9c
        L98:
            java.lang.String r8 = r8.substring(r6)     // Catch: java.lang.Exception -> Lc7
        L9c:
            int r0 = r8.length()     // Catch: java.lang.Exception -> Lc7
            if (r0 != r5) goto Lbf
            java.lang.StringBuffer r0 = new java.lang.StringBuffer     // Catch: java.lang.Exception -> Lc7
            r0.<init>()     // Catch: java.lang.Exception -> Lc7
        La7:
            if (r4 >= r5) goto Lbb
            char r2 = r8.charAt(r4)     // Catch: java.lang.Exception -> Lc7
            java.lang.StringBuffer r2 = r0.append(r2)     // Catch: java.lang.Exception -> Lc7
            char r3 = r8.charAt(r4)     // Catch: java.lang.Exception -> Lc7
            r2.append(r3)     // Catch: java.lang.Exception -> Lc7
            int r4 = r4 + 1
            goto La7
        Lbb:
            java.lang.String r8 = r0.toString()     // Catch: java.lang.Exception -> Lc7
        Lbf:
            r0 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            int r8 = java.lang.Integer.parseInt(r8, r1)     // Catch: java.lang.Exception -> Lc7
            int r0 = r0 + r8
            goto Lc8
        Lc7:
            r0 = -1
        Lc8:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.PdrUtil.stringToColor(java.lang.String):int");
    }

    public static String getMimeType(String str) {
        MimeTypeMap singleton = MimeTypeMap.getSingleton();
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (isEmpty(fileExtensionFromUrl) && str.lastIndexOf(".") >= 0) {
            fileExtensionFromUrl = str.substring(str.lastIndexOf(".") + 1);
        }
        String mimeTypeFromExtension = singleton.getMimeTypeFromExtension(fileExtensionFromUrl);
        return TextUtils.isEmpty(mimeTypeFromExtension) ? TextUtils.isEmpty(fileExtensionFromUrl) ? "*/*" : "application/" + fileExtensionFromUrl : mimeTypeFromExtension;
    }

    public static String getExtensionFromMimeType(String str) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(str);
    }

    public static boolean isNetPath(String str) {
        return str != null && ((str.startsWith(DeviceInfo.HTTP_PROTOCOL) && !str.startsWith("http://localhost")) || (str.startsWith(DeviceInfo.HTTPS_PROTOCOL) && !str.startsWith("https://localhost")));
    }

    public static String getDownloadFilename(String str, String str2, String str3) {
        String str4 = null;
        try {
            if (!isEmpty(str)) {
                String[] split = str.substring(str.indexOf(";") + 1).trim().split("=");
                String replace = split[0].replace(JSUtil.QUOTE, "");
                String replace2 = split[1].replace(JSUtil.QUOTE, "");
                if (!isEmpty(split[1]) && isEquals(AbsoluteConst.JSON_KEY_FILENAME, replace)) {
                    if (!isEmpty(replace2)) {
                        str4 = replace2;
                    }
                }
            }
        } catch (Exception unused) {
            Logger.d("PdrUtil.getDownloadFilename " + str + " not found filename");
        }
        if (isEmpty(str4)) {
            int lastIndexOf = str3.lastIndexOf(47);
            if (lastIndexOf > 0 && lastIndexOf < str3.length() - 1) {
                str4 = str3.substring(lastIndexOf + 1);
                int indexOf = str4.indexOf("?");
                if (indexOf > 0) {
                    if (indexOf < str4.length() - 1) {
                        str4 = str4.substring(0, indexOf);
                    } else {
                        str4 = String.valueOf(System.currentTimeMillis());
                    }
                }
            } else {
                str4 = String.valueOf(System.currentTimeMillis());
            }
        }
        if (str4.indexOf(".") < 0) {
            String extensionFromMimeType = getExtensionFromMimeType(str2);
            if (!isEmpty(extensionFromMimeType)) {
                str4 = str4 + "." + extensionFromMimeType;
            }
        }
        try {
            return URLDecoder.decode(str4, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str4;
        }
    }

    public static Bitmap renderCroppedGreyscaleBitmap(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6) {
        int[] iArr = new int[i5 * i6];
        int i7 = (i4 * i) + i3;
        for (int i8 = 0; i8 < i6; i8++) {
            int i9 = i8 * i5;
            for (int i10 = 0; i10 < i5; i10++) {
                iArr[i9 + i10] = ((bArr[i7 + i10] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) * 65793) | (-16777216);
            }
            i7 += i;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i5, i6, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, i5, 0, 0, i5, i6);
        return createBitmap;
    }

    public static String getDefaultPrivateDocPath(String str, String str2) {
        if (isEmpty(str)) {
            str = AbsoluteConst.MINI_SERVER_APP_DOC + System.currentTimeMillis();
        } else if (str.endsWith("/")) {
            str = str + System.currentTimeMillis();
        }
        return !str.endsWith(new StringBuilder().append(".").append(str2).toString()) ? str + "." + str2 : str;
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String str) {
        try {
            File file = new File(str);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void alert(Activity activity, String str, Bitmap bitmap) {
        final AlertDialog create = new AlertDialog.Builder(activity).create();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(17);
        TextView textView = new TextView(activity);
        textView.setText(str);
        linearLayout.addView(textView);
        ImageView imageView = new ImageView(activity);
        imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        linearLayout.addView(imageView, new ViewGroup.LayoutParams(-2, -2));
        create.setView(linearLayout);
        create.setCanceledOnTouchOutside(false);
        create.setButton("确定", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.PdrUtil.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                create.dismiss();
            }
        });
        create.show();
    }

    public static void toast(Context context, String str, Bitmap bitmap) {
        Toast makeText = Toast.makeText(context, str, Toast.LENGTH_LONG);
        if (bitmap != null) {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            View view = makeText.getView();
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(bitmap);
            ((ViewGroup) view).addView(imageView, 0);
            makeText.setText(str + " w=" + width + ";h=" + height);
        }
        makeText.setDuration(Toast.LENGTH_LONG);
        makeText.show();
    }

    public static boolean isDeviceRootDir(String str) {
        return str.startsWith(DeviceInfo.sDeviceRootDir) || str.startsWith("/sdcard/") || str.startsWith(DeviceInfo.sDeviceRootDir.substring(1)) || str.startsWith("sdcard/");
    }

    public static String appendByDeviceRootDir(String str) {
        if (str == null || str.startsWith(DeviceInfo.sDeviceRootDir)) {
            return str;
        }
        if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            str = str.substring(7);
        }
        if (str.indexOf("sdcard/") > -1) {
            str = str.substring(str.indexOf("sdcard/") + 7);
        }
        if (!str.endsWith("/")) {
            str = str + "/";
        }
        return DeviceInfo.sDeviceRootDir + "/" + str;
    }
}
