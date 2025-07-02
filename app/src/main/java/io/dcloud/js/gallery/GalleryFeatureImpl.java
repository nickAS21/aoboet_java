package io.dcloud.js.gallery;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.view.PointerIconCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.ContentUriUtil;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class GalleryFeatureImpl implements IFeature {
    private static String b = "/mnt/sdcard/DCIM/Camera/";
    private static int c;
    private static int d;
    private static int e;
    private static IWebview f;
    AbsMgr a = null;

    static {
        if (Build.VERSION.SDK_INT >= 8) {
            b = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/";
        }
        c = PointerIconCompat.TYPE_CONTEXT_MENU;
        d = PointerIconCompat.TYPE_HAND;
        e = PointerIconCompat.TYPE_HELP;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        f = iWebview;
        boolean isOpt = false;
        if ("pick".equals(str)) {
            if (strArr.length >= 2) {
                String str2 = strArr[1];
                if (!PdrUtil.isEmpty(str2)) {
                    try {
                        isOpt = new JSONObject(str2).optBoolean("multiple", false);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            if (isOpt) {
                a(iWebview, str, strArr);
                return null;
            }
            b(iWebview, str, strArr);
            return null;
        }
        if (!"save".equals(str)) {
            return null;
        }
        try {
            if (strArr[0] == null) {
                throw new IOException();
            }
            String convert2AbsFullPath = iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[0]);
            String str3 = b + convert2AbsFullPath.substring(convert2AbsFullPath.lastIndexOf("/") + 1);
            boolean isOpt2 = 1 == DHFile.copyFile(convert2AbsFullPath, str3);
            String str4 = strArr[1];
            if (isOpt) {
                JSUtil.execCallback(iWebview, str4, "", JSUtil.OK, false, false);
                iWebview.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse(DeviceInfo.FILE_PROTOCOL + str3)));
                return null;
            }
            JSUtil.execCallback(iWebview, str4, DOMException.toJSON(12, "UNKOWN ERROR"), JSUtil.ERROR, true, false);
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

    private void a(final IWebview iWebview, String str, String[] strArr) {
        String str2;
        boolean z;
        JSONArray jSONArray = null;
        try {
            final String str3 = strArr[0];
            try {
                final IApp obtainApp = iWebview.obtainFrameView().obtainApp();
                try {
                    obtainApp.registerSysEventListener(new ISysEventListener() { // from class: io.dcloud.js.gallery.GalleryFeatureImpl.1
                        @Override // io.dcloud.common.DHInterface.ISysEventListener
                        public boolean onExecute(SysEventType sysEventType, Object obj) {
//                            String[] strArr2;
                            String[] strArr3 = new String[0];
                            String imageAbsolutePath;
                            Object[] objArr = (Object[]) obj;
                            int intValue = ((Integer) objArr[0]).intValue();
                            ((Integer) objArr[1]).intValue();
                            Intent intent = (Intent) objArr[2];
                            if (sysEventType == SysEventType.onActivityResult) {
                                obtainApp.unregisterSysEventListener(this, SysEventType.onActivityResult);
                                try {
                                } catch (Exception unused) {
//                                    strArr2 = null;
                                }
                                if (intValue != GalleryFeatureImpl.d) {
                                    if (intValue == GalleryFeatureImpl.e && intent != null) {
                                        ClipData clipData = intent.getClipData();
                                        if (clipData != null) {
                                            int itemCount = clipData.getItemCount();
                                            String[] strArr2 = new String[itemCount];
                                            for (int i = 0; i < itemCount; i++) {
                                                try {
                                                    strArr2[i] = ContentUriUtil.getImageAbsolutePath(iWebview.getActivity(), clipData.getItemAt(i).getUri());
                                                } catch (Exception unused2) {
                                                }
                                            }
                                        } else if (intent.getData() != null) {
                                            if (PdrUtil.isDeviceRootDir(intent.getData().getPath())) {
                                                imageAbsolutePath = intent.getData().getPath();
                                            } else {
                                                imageAbsolutePath = ContentUriUtil.getImageAbsolutePath(iWebview.getActivity(), intent.getData());
                                            }
                                            if (imageAbsolutePath != null) {
                                                String[] strArr4 = new String[1];
                                                try {
                                                    strArr4[0] = imageAbsolutePath;
                                                    strArr3 = strArr4;
                                                } catch (Exception unused3) {
//                                                    strArr2 = strArr4;
                                                }
                                            }
                                        }
//                                        strArr3 = strArr2;
                                    }
//                                    strArr3 = null;
                                } else {
                                    strArr3 = intent.getStringArrayExtra("all_path");
                                }
                                GalleryFeatureImpl.this.a(obtainApp, iWebview, str3, intent != null ? strArr3 : null, true);
                            }
                            return false;
                        }
                    }, ISysEventListener.SysEventType.onActivityResult);
                    int i = d;
                    Intent intent = new Intent();
                    String str4 = "image/*";
                    int i2 = -1;
                    if (PdrUtil.isEmpty(strArr[1])) {
                        z = true;
                    } else {
                        JSONObject createJSONObject = JSONUtil.createJSONObject(strArr[1]);
                        String string = JSONUtil.getString(createJSONObject, "filter");
                        if ("video".equals(string)) {
                            str4 = "video/*";
                        } else if ("none".equals(string)) {
                            str4 = "image/*|video/*";
                        }
                        z = createJSONObject.optBoolean("system", true);
                        i2 = createJSONObject.optInt("maximum", -1);
                        jSONArray = createJSONObject.optJSONArray("selected");
                    }
                    intent.setType(str4);
                    if (Build.VERSION.SDK_INT >= 19 && z) {
                        intent.setAction("android.intent.action.OPEN_DOCUMENT");
                        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                        intent.addCategory("android.intent.category.OPENABLE");
                        int i3 = e;
                        try {
                            boolean z2 = false;
                            Iterator<ResolveInfo> it = iWebview.getActivity().getPackageManager().queryIntentActivities(intent, 0).iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                } else if ("com.android.documentsui".equals(it.next().activityInfo.packageName)) {
                                    z2 = true;
                                    break;
                                }
                            }
                            if (!z2) {
                                Intent intent2 = new Intent();
                                try {
                                    intent2.setType(str4);
                                    i3 = d;
                                    intent2.setClassName(iWebview.getContext().getPackageName(), "io.dcloud.imagepick.CustomGalleryActivity");
                                    intent = intent2;
                                } catch (Exception e2) {
                                    intent = intent2;
                                    i = i3;
                                    e2.printStackTrace();
                                    iWebview.getActivity().startActivityForResult(intent, i);
                                }
                            }
                            i = i3;
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    } else {
                        intent.setClassName(iWebview.getContext().getPackageName(), "io.dcloud.imagepick.CustomGalleryActivity");
                        intent.putExtra("maximum", i2);
                        if (strArr.length > 2) {
                            intent.putExtra("_onMaxedId", strArr[2]);
                        }
                        if (jSONArray != null) {
                            intent.putExtra("selected", jSONArray.toString());
                        }
                    }
                    iWebview.getActivity().startActivityForResult(intent, i);
                } catch (Exception e4) {
                    str2 = str3;
                    e4.printStackTrace();
                    JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e4.getMessage()), JSUtil.ERROR, true, false);
                }
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        } catch (Exception e6) {
            e6.printStackTrace();
            str2 = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(IApp iApp, IWebview iWebview, String str, String[] strArr, boolean z) {
        String str2;
        boolean z2;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("multiple", z);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        JSONArray jSONArray = new JSONArray();
        if (strArr != null) {
            if (strArr != null) {
                for (String str3 : strArr) {
                    jSONArray.put(iApp.convert2WebviewFullPath(null, str3));
                }
            }
            try {
                jSONObject.put("files", jSONArray);
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
            z2 = true;
            str2 = "pickImage path wrong";
        } else {
            str2 = "User cancelled";
            z2 = false;
        }
        if (z2) {
            JSUtil.execCallback(iWebview, str, jSONObject, JSUtil.OK, false);
        } else {
            JSUtil.execCallback(iWebview, str, DOMException.toJSON(12, str2), JSUtil.ERROR, true, false);
        }
    }

    private void b(final IWebview iWebview, String str, String[] strArr) {
        final String str2;
        try {
            str2 = strArr[0];
            try {
                final IApp obtainApp = iWebview.obtainFrameView().obtainApp();
                obtainApp.registerSysEventListener(new ISysEventListener() { // from class: io.dcloud.js.gallery.GalleryFeatureImpl.2
                    @Override // io.dcloud.common.DHInterface.ISysEventListener
                    public boolean onExecute(SysEventType sysEventType, Object obj) {
                        String str3;
                        Uri data;
                        Object[] objArr = (Object[]) obj;
                        int intValue = ((Integer) objArr[0]).intValue();
                        ((Integer) objArr[1]).intValue();
                        Intent intent = (Intent) objArr[2];
                        if (sysEventType == SysEventType.onActivityResult) {
                            obtainApp.unregisterSysEventListener(this, SysEventType.onActivityResult);
                            if (intValue == GalleryFeatureImpl.c) {
                                if (intent == null || (data = intent.getData()) == null) {
                                    str3 = null;
                                } else if (!PdrUtil.isDeviceRootDir(data.getPath())) {
                                    str3 = GalleryFeatureImpl.this.a(iWebview.getContext(), data);
                                } else {
                                    str3 = data.getPath();
                                }
                                GalleryFeatureImpl.this.a(obtainApp, iWebview, str2, str3 != null ? new String[]{str3} : null, false);
                            }
                        }
                        return false;
                    }
                }, ISysEventListener.SysEventType.onActivityResult);
                Intent intent = new Intent("android.intent.action.PICK");
                String str3 = "image/*";
                if (!PdrUtil.isEmpty(strArr[1])) {
                    String string = JSONUtil.getString(JSONUtil.createJSONObject(strArr[1]), "filter");
                    if ("video".equals(string)) {
                        str3 = "video/*";
                    } else if ("none".equals(string)) {
                        str3 = "image/*|video/*";
                    }
                }
                intent.setType(str3);
                iWebview.getActivity().startActivityForResult(intent, c);
            } catch (Exception e2) {
                e2.printStackTrace();
                JSUtil.execCallback(iWebview, str2, DOMException.toJSON(12, e2.getMessage()), JSUtil.ERROR, true, false);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(Context context, Uri uri) {
        String[] strArr = {"_data"};
        Cursor query = context.getContentResolver().query(uri, strArr, null, null, null);
        if (query == null) {
            return null;
        }
        query.moveToFirst();
        String string = query.getString(query.getColumnIndex(strArr[0]));
        query.close();
        return string;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.a = absMgr;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
        f = null;
    }

    public static void a(String str) {
        IWebview iWebview = f;
        if (iWebview != null) {
            JSUtil.execCallback(iWebview, str, "", JSUtil.OK, true);
        }
    }
}
