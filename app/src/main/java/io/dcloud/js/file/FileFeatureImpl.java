package io.dcloud.js.file;

import org.json.JSONException;
import org.json.JSONObject;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class FileFeatureImpl implements IFeature {
    private static String a;
    private static String b;
    private static String c;
    private static String d;
    private static String e;

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:57:0x0148, code lost:
    
        if (io.dcloud.common.adapter.io.DHFile.copyAssetsFile(r7[0], r2) != false) goto L58;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0243  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x024a  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x02b9  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02c6  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x059d  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x05a4  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0152  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x016c  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01d9  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x020e  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0215  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:47:0x014c -> B:41:0x014f). Please report as a decompilation issue!!! */
    @Override // io.dcloud.common.DHInterface.IFeature
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r25, java.lang.String r26, java.lang.String[] r27) {
        /*
            Method dump skipped, instructions count: 1884
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.file.FileFeatureImpl.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    private boolean a(String str) {
        return str.endsWith(BaseInfo.REL_PRIVATE_WWW_DIR) || str.endsWith(BaseInfo.REL_PUBLIC_DOCUMENTS_DIR) || str.endsWith(BaseInfo.REL_PUBLIC_DOWNLOADS_DIR) || str.endsWith(BaseInfo.REL_PRIVATE_DOC_DIR) || str.endsWith(AbsoluteConst.MINI_SERVER_APP_WWW) || str.endsWith("_documents/") || str.endsWith("_downloads/") || str.endsWith(AbsoluteConst.MINI_SERVER_APP_DOC);
    }

    private JSONObject b(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (str.startsWith(a)) {
            jSONObject.put("type", 1);
            jSONObject.put("fsName", "PRIVATE_WWW");
            jSONObject.put("fsRoot", a);
        } else if (str.startsWith(c)) {
            jSONObject.put("type", 2);
            jSONObject.put("fsName", "PRIVATE_DOCUMENTS");
            jSONObject.put("fsRoot", c);
        } else if (str.startsWith(d)) {
            jSONObject.put("type", 3);
            jSONObject.put("fsName", "PUBLIC_DOCUMENTS");
            jSONObject.put("fsRoot", d);
        } else if (str.startsWith(e)) {
            jSONObject.put("type", 4);
            jSONObject.put("fsName", "PUBLIC_DOWNLOADS");
            jSONObject.put("fsRoot", e);
        } else if (str.startsWith(b)) {
            jSONObject.put("type", 1);
            jSONObject.put("fsName", "PRIVATE_WWW");
            jSONObject.put("fsRoot", b);
        } else if (PdrUtil.isDeviceRootDir(str)) {
            jSONObject.put("type", 5);
            jSONObject.put("fsName", "PUBLIC_DEVICE_ROOT");
            jSONObject.put("fsRoot", DeviceInfo.sDeviceRootDir);
        }
        return jSONObject;
    }

    private int c(String str) {
        if (str.startsWith(a)) {
            return 1;
        }
        if (str.startsWith(c)) {
            return 2;
        }
        if (str.startsWith(d)) {
            return 3;
        }
        return str.startsWith(e) ? 4 : -1;
    }

    private void a(int i, IWebview iWebview, String str) {
        JSUtil.execCallback(iWebview, str, a(i), JSUtil.ERROR, true, false);
    }

    private String a(int i) {
        switch (i) {
            case 1:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "文件没有发现");
            case 2:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "没有获得授权");
            case 3:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), AbsoluteConst.STREAMAPP_UPD_ZHCancel);
            case 4:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "不允许读");
            case 5:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "编码错误");
            case 6:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "不允许修改");
            case 7:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "无效的状态");
            case 8:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "语法错误");
            case 9:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "无效的修改");
            case 10:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "执行出错");
            case 11:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "类型不匹配");
            case 12:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), "路径不存在");
            default:
                return String.format(DOMException.JSON_ERROR_INFO, Integer.valueOf(i), DOMException.MSG_UNKNOWN_ERROR);
        }
    }
}
