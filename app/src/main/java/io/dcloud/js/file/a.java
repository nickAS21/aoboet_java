package io.dcloud.js.file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

import io.dcloud.common.adapter.io.DHFile;

/* compiled from: JsFile.java */
/* loaded from: classes.dex */
public class a {
    protected static JSONObject a(int i, long j, long j2, String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("lastModifiedDate", j);
            jSONObject.put("type", i);
            jSONObject.put("size", j2);
            jSONObject.put("name", str);
            jSONObject.put("fullPath", str2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static JSONObject a(String str, int i, String str2, String str3, String str4) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("name", str);
            jSONObject.put("type", i);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("name", str2);
            jSONObject2.put("fullPath", str3);
            jSONObject2.put("remoteURL", str4);
            jSONObject.put("root", jSONObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static JSONObject a(String str, String str2, String str3, boolean z) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("isDirectory", z);
            jSONObject.put("isFile", !z);
            jSONObject.put("name", str);
            jSONObject.put("remoteURL", str3);
            jSONObject.put("fullPath", str2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static JSONObject a(String str, String str2, boolean z, String str3, String str4, int i, String str5) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("isDirectory", z);
            jSONObject.put("isFile", !z);
            jSONObject.put("name", str);
            jSONObject.put("remoteURL", str3);
            jSONObject.put("fullPath", str2);
            jSONObject.put("fsName", str4);
            jSONObject.put("type", i);
            jSONObject.put("fsRoot", str5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static JSONArray a(String str, String str2) {
        JSONArray jSONArray = new JSONArray();
        String str3 = str + (str.endsWith(File.separator) ? "" : File.separator);
        String str4 = str2 + (str2.endsWith(File.separator) ? "" : File.separator);
        String[] list = new File(str3).list();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                String str5 = str3 + list[i];
                File file = new File(str5);
                JSONObject jSONObject = new JSONObject();
                boolean isDirectory = file.isDirectory();
                String str6 = list[i];
                String str7 = str4 + str6;
                try {
                    jSONObject.put("isDirectory", isDirectory);
                    jSONObject.put("isFile", !isDirectory);
                    jSONObject.put("name", str6);
                    jSONObject.put("remoteURL", str7);
                    jSONObject.put("fullPath", str5);
                    jSONArray.put(jSONObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jSONArray;
    }

    public static JSONObject a(String str, int i) {
        File file = new File(str);
        return a(i, file.lastModified(), DHFile.getFileSize(file), file.getName(), str);
    }

    public static JSONObject a(String str, boolean z) {
        File file = new File(str);
        if (!file.exists()) {
            return null;
        }
        C0011a c0011a = new C0011a();
        c0011a.a = file.lastModified();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    a(file2, c0011a, z);
                }
            }
        } else {
            c0011a.b = file.length();
        }
        return c0011a.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JsFile.java */
    /* renamed from: io.dcloud.js.file.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0011a {
        long a;
        long b;
        int c = 0;
        int d = 0;

        C0011a() {
        }

        public JSONObject a() {
            try {
                return new JSONObject(String.format(Locale.ENGLISH, "{lastModifiedDate : %d,size : %d,directoryCount : %d,fileCount : %d}", Long.valueOf(this.a), Long.valueOf(this.b), Integer.valueOf(this.c), Integer.valueOf(this.d)));
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        }
    }

    public static void a(File file, C0011a c0011a, boolean z) {
        File[] listFiles;
        if (file.isDirectory()) {
            if (z && (listFiles = file.listFiles()) != null) {
                for (File file2 : listFiles) {
                    a(file2, c0011a, z);
                }
            }
            c0011a.c++;
            return;
        }
        c0011a.b += file.length();
        c0011a.d++;
    }
}
