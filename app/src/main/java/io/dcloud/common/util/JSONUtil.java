package io.dcloud.common.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import io.dcloud.common.adapter.util.Logger;

/* loaded from: classes.dex */
public class JSONUtil {
    public static JSONArray createJSONArray(String str) {
        if (!str.startsWith("[")) {
            str = "[" + str;
        }
        if (!str.endsWith("]")) {
            str = str + "]";
        }
        try {
            return new JSONArray(str);
        } catch (JSONException unused) {
            return null;
        }
    }

    public static JSONObject combinJSONObject(JSONObject jSONObject, JSONObject jSONObject2) {
        Iterator<String> keys;
        if (jSONObject == null) {
            return jSONObject2;
        }
        if (jSONObject != jSONObject2 && (keys = jSONObject2.keys()) != null) {
            while (keys.hasNext()) {
                try {
                    String valueOf = String.valueOf(keys.next());
                    Object opt = jSONObject.opt(valueOf);
                    Object opt2 = jSONObject2.opt(valueOf);
                    if (opt2 != null) {
                        if (opt == null) {
                            jSONObject.putOpt(valueOf, opt2);
                        } else if (opt2 instanceof JSONObject) {
                            if (opt instanceof JSONObject) {
                                combinJSONObject((JSONObject) opt, (JSONObject) opt2);
                            } else {
                                jSONObject.putOpt(valueOf, opt2);
                            }
                        } else {
                            jSONObject.putOpt(valueOf, opt2);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jSONObject;
    }

    public static String getString(JSONObject jSONObject, String str) {
        if (jSONObject == null) {
            return null;
        }
        try {
            return jSONObject.getString(str);
        } catch (Exception unused) {
            return null;
        }
    }

    public static long getLong(JSONObject jSONObject, String str) {
        if (jSONObject != null) {
            try {
                if (jSONObject.has(str)) {
                    return jSONObject.getLong(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }

    public static boolean getBoolean(JSONObject jSONObject, String str) {
        if (jSONObject != null) {
            try {
                if (jSONObject.has(str)) {
                    return jSONObject.getBoolean(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static JSONObject createJSONObject(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException unused) {
            Logger.d("jsonutil", "JSONException pJson=" + str);
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject jSONObject, String str) {
        try {
            Object opt = jSONObject.opt(str);
            if (opt instanceof JSONObject) {
                return (JSONObject) opt;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONArray jSONArray, int i) {
        try {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONObject) {
                return (JSONObject) obj;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getString(JSONArray jSONArray, int i) {
        try {
            Object obj = jSONArray.get(i);
            if (PdrUtil.isEmpty(obj)) {
                return null;
            }
            return String.valueOf(obj);
        } catch (Exception unused) {
            return null;
        }
    }

    public static int getInt(JSONObject jSONObject, String str) {
        try {
            Integer valueOf = Integer.valueOf(jSONObject.getInt(str));
            if (valueOf instanceof Integer) {
                return valueOf.intValue();
            }
            return 0;
        } catch (Exception unused) {
            return 0;
        }
    }

    public static boolean isNull(JSONObject jSONObject, String str) {
        if (jSONObject == null) {
            return true;
        }
        try {
            return jSONObject.isNull(str);
        } catch (Exception unused) {
            return true;
        }
    }

    public static JSONArray getJSONArray(JSONArray jSONArray, int i) {
        try {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static JSONArray getJSONArray(JSONObject jSONObject, String str) {
        try {
            Object obj = jSONObject.get(str);
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static String toJSONableString(String str) {
        return str != null ? JSONObject.quote(str) : "''";
    }
}
