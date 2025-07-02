package io.dcloud.feature.nativeObj;

import org.json.JSONException;
import org.json.JSONObject;

import io.dcloud.common.constant.AbsoluteConst;

/* compiled from: NativeBitmapSaveOptions.java */
/* loaded from: classes.dex  old a*/
public class NativeBitmapSaveOptions {
    public boolean a;
    public String b;
    public int c;
    public String d;
    public int e;
    public int f;
    public long g;
    public JSONObject h;
    private JSONObject i;

    public NativeBitmapSaveOptions(String str) {
        this.a = false;
        this.b = "jpg";
        this.c = 50;
        this.i = null;
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.i = jSONObject;
            this.a = jSONObject.optBoolean("overwrite", false);
            this.b = this.i.optString(AbsoluteConst.JSON_KEY_FORMAT, "jpg");
            this.c = this.i.optInt("quality", 50);
            this.h = this.i.optJSONObject("clip");
        } catch (JSONException unused) {
            this.i = new JSONObject();
        }
    }
}
