package io.dcloud.js.camera;

import android.hardware.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* compiled from: CameraManager.java */
/* loaded from: classes.dex old a */
class CameraManager {
    protected static int a = 1;
    protected static int b = 2;
    List<Camera.Size> c;
    List<Integer> d;
    List<Camera.Size> e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraManager(int i) {
        Camera camera = null;
        this.c = null;
        this.d = null;
        this.e = null;
        if (i == 2) {
            try {
                if (DeviceInfo.sDeviceSdkVer >= 9) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= Camera.getNumberOfCameras()) {
                            break;
                        }
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        Camera.getCameraInfo(i2, cameraInfo);
                        if (cameraInfo.facing == 1) {
                            camera = Camera.open(i2);
                            break;
                        }
                        i2++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        camera = camera == null ? Camera.open() : camera;
        if (DeviceInfo.sDeviceSdkVer >= 11) {
            this.c = camera.getParameters().getSupportedVideoSizes();
        }
        this.e = camera.getParameters().getSupportedPictureSizes();
        if (DeviceInfo.sDeviceSdkVer >= 8) {
            this.d = camera.getParameters().getSupportedPictureFormats();
        }
        camera.release();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String a() {
        String[] d = d();
        return String.format("(function(){return{supportedImageResolutions : %s,supportedVideoResolutions : %s,supportedImageFormats : %s,supportedVideoFormats : %s};})();", c(), b(), d[0], d[1]);
    }

    private String b() {
        return (this.c == null || DeviceInfo.sDeviceSdkVer < 11) ? "[]" : b(this.c);
    }

    private String c() {
        List<Camera.Size> list = this.e;
        return list != null ? b(list) : "[]";
    }

    private String[] d() {
        List<Integer> list = this.d;
        if (list != null) {
            return a(list);
        }
        return null;
    }

    private String[] a(List<Integer> list) {
        return new String[]{"['jpg']", "['mp4']"};
    }

    private String b(List<Camera.Size> list) {
        int size = list.size();
        if (list == null || size <= 1) {
            return "[]";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for (int i = 0; i < size; i++) {
            stringBuffer.append("'" + list.get(i).width + "*" + list.get(i).height + "'");
            if (i != size - 1) {
                stringBuffer.append(JSUtil.COMMA);
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static C0010a a(String str, boolean z) {
        C0010a c0010a = new C0010a();
        if (str != null) {
            JSONObject jSONObject = null;
            try {
                jSONObject = new JSONObject(str);
            } catch (JSONException unused) {
            }
            c0010a.b = JSONUtil.getString(jSONObject, "resolution");
            String string = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_FILENAME);
            JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_FORMAT);
            String str2 = z ? "jpg" : "mp4";
            c0010a.c = str2;
            c0010a.a = PdrUtil.getDefaultPrivateDocPath(string, str2);
            c0010a.d = JSONUtil.getInt(jSONObject, "index");
        }
        return c0010a;
    }

    /* compiled from: CameraManager.java */
    /* renamed from: io.dcloud.js.camera.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    static class C0010a {
        String a;
        String b;
        String c;
        int d;

        C0010a() {
        }

        public String a() {
            return this.a;
        }
    }
}
