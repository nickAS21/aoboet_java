package io.dcloud.common.common_a;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nostra13.dcloudimageloader.core.ImageLoader;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import io.dcloud.RInformation;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.io.UnicodeInputStream;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ReflectUtils;
import io.dcloud.common.util.ShortCutUtil;
import io.dcloud.common.util.ThreadPool;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* compiled from: AppMgr.java */
/* loaded from: classes.dex */
public final class AppMgr extends AbsMgr implements IMgr.AppEvent {
    c a;
    ArrayList<String> b;
    ArrayList<WebApp> c;
    b d;
    Class[] e;
    private JSONObject f;
    private AlertDialog g;

    public AppMgr(ICore iCore) {
        super(iCore, Logger.AppMgr_TAG, IMgr.MgrType.AppMgr);
        this.a = null;
        this.b = new ArrayList<>(1);
        this.c = new ArrayList<>(1);
        this.d = null;
        this.e = new Class[0];
        this.f = null;
        b();
        a();
        InputStream resInputStream = PlatformUtil.getResInputStream(BaseInfo.sApiConfigPath);
        if (resInputStream != null) {
            try {
                this.f = new JSONObject(new String(IOUtil.getBytes(new UnicodeInputStream(resInputStream, Charset.defaultCharset().name()))));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        this.d = new b(this);
        if (BaseInfo.isQihooLifeHelper(getContext())) {
            PlatformUtil.invokeMethod("io.dcloud.oauth.qihoosdk.QihooOAuthService", "autoLogin", null, this.e, new Object[0]);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:196:0x046a A[Catch: all -> 0x0602, TryCatch #1 {all -> 0x0602, blocks: (B:3:0x000d, B:5:0x0013, B:10:0x002c, B:12:0x0030, B:13:0x0046, B:15:0x0052, B:18:0x005c, B:20:0x0061, B:23:0x006d, B:24:0x0072, B:26:0x0093, B:29:0x0065, B:31:0x0098, B:33:0x0034, B:34:0x009e, B:36:0x00a6, B:38:0x00b0, B:40:0x00b9, B:42:0x00cd, B:44:0x00ed, B:46:0x00f3, B:47:0x010a, B:50:0x0113, B:52:0x0119, B:54:0x012b, B:56:0x0132, B:59:0x0135, B:60:0x0159, B:63:0x0160, B:70:0x0179, B:72:0x0183, B:73:0x018c, B:75:0x019b, B:76:0x01a4, B:88:0x01a0, B:89:0x01b5, B:91:0x01ba, B:95:0x01c8, B:97:0x01d1, B:99:0x01d5, B:101:0x01db, B:102:0x01dd, B:104:0x01e5, B:106:0x01ed, B:108:0x01f3, B:110:0x01fb, B:113:0x0205, B:115:0x0209, B:117:0x021d, B:120:0x0212, B:122:0x0216, B:124:0x0222, B:126:0x0237, B:128:0x024e, B:130:0x0255, B:131:0x025c, B:133:0x0279, B:135:0x0283, B:137:0x028d, B:140:0x0293, B:142:0x02b8, B:144:0x02be, B:146:0x02ca, B:148:0x02d6, B:150:0x02dc, B:152:0x02fb, B:154:0x0301, B:155:0x030d, B:156:0x0364, B:158:0x036e, B:160:0x0374, B:163:0x0392, B:165:0x03ae, B:167:0x03b8, B:169:0x03c2, B:172:0x03d8, B:175:0x03de, B:177:0x03e6, B:180:0x040a, B:182:0x0415, B:183:0x0424, B:186:0x042a, B:192:0x0447, B:194:0x0453, B:196:0x046a, B:197:0x0471, B:200:0x0437, B:201:0x041a, B:202:0x0477, B:204:0x04c9, B:208:0x04db, B:212:0x04df, B:216:0x04ea, B:218:0x0505, B:220:0x050d, B:222:0x0522, B:224:0x0526, B:225:0x0532, B:229:0x053c, B:231:0x0540, B:233:0x0544, B:235:0x054a, B:236:0x054f, B:239:0x0557, B:241:0x05ca, B:244:0x05d8, B:245:0x05e6), top: B:2:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x015f  */
    @Override // io.dcloud.common.DHInterface.IMgr
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object processEvent(io.dcloud.common.DHInterface.IMgr.MgrType r24, int r25, java.lang.Object r26) {
        /*
            Method dump skipped, instructions count: 1594
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.a.a.processEvent(io.dcloud.common.DHInterface.IMgr$MgrType, int, java.lang.Object):java.lang.Object");
    }

    private void d(final WebApp dVar) {
        ImageLoader.getInstance().loadImage(DataInterface.getIconImageUrl(dVar.obtainAppId(), dVar.getActivity().getResources().getDisplayMetrics().widthPixels + ""), new ImageLoadingListener() { // from class: io.dcloud.common.a.a.2
            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingCancelled(String str, View view) {
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingStarted(String str, View view) {
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingFailed(String str, View view, FailReason failReason) {
                if (StringConst.canChangeHost(str)) {
                    ImageLoader.getInstance().loadImage(StringConst.changeHost(str), this);
                }
            }

            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
            public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                String path = ImageLoader.getInstance().getDiscCache().get(str).getPath();
                if (bitmap == null) {
                    if (!TextUtils.isEmpty(path) && path.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        bitmap = BitmapFactory.decodeFile(path.substring(7));
                    }
                    if (bitmap == null) {
                        bitmap = BitmapFactory.decodeResource(dVar.getActivity().getResources(), RInformation.DRAWABLE_ICON);
                    }
                }
                if (bitmap != null) {
                    try {
                        Object newInstance = ReflectUtils.getObjectConstructor("android.app.ActivityManager$TaskDescription", String.class, Bitmap.class, Integer.TYPE).newInstance(dVar.obtainAppName(), bitmap, Integer.valueOf(dVar.getActivity().getTitleColor()));
                        ReflectUtils.invokeMethod(dVar.getActivity(), "setTaskDescription", new Class[]{newInstance.getClass()}, new Object[]{newInstance});
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                Logger.e("polishing overview", "obtain appstream icon failed.");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        ArrayList<WebApp> c = this.d.c();
        if (c == null || c.size() < BaseInfo.s_Runing_App_Count_Trim) {
            return;
        }
        int size = c.size();
        for (int i = size - BaseInfo.s_Runing_App_Count_Trim; i >= 0 && i > size - BaseInfo.s_Runing_App_Count_Max; i--) {
            WebApp dVar = c.get(i);
            dVar.a(ISysEventListener.SysEventType.onWebAppTrimMemory, dVar);
        }
    }

    public void a(Activity activity, final String str, final String str2, WebApp dVar, final WebApp dVar2, WebApp dVar3, final boolean z) {
        Log.i("ylyl", "startOneApp " + str);
        BaseInfo.sLastRunApp = str;
        BaseInfo.CmtInfo cmitInfo = BaseInfo.getCmitInfo(str);
        if (cmitInfo.needUpdate) {
            cmitInfo.templateVersion = dVar2.k;
            cmitInfo.rptCrs = dVar2.isStreamApp() ? true : dVar2.t;
            cmitInfo.rptJse = dVar2.isStreamApp() ? true : dVar2.u;
            cmitInfo.plusLauncher = BaseInfo.getLaunchType(dVar2.obtainWebAppIntent());
            cmitInfo.sfd = DataInterface.getStreamappFrom(dVar2.obtainWebAppIntent());
            cmitInfo.needUpdate = false;
        }
        if (dVar2.d == 3) {
            dVar2.d = dVar2.n() ? dVar2.d : (byte) 2;
        }
        if (dVar != null && dVar != dVar2 && dVar != dVar3) {
            dVar.f();
        }
        if (dVar2.d == 1 || ((z && !dVar2.f) || !z)) {
            Logger.d(Logger.AppMgr_TAG, str + " will unrunning change to active");
            dVar2.a(activity);
            processEvent(IMgr.MgrType.WindowMgr, 4, new Object[]{dVar2, str});
            dVar2.a(new ICallBack() { // from class: io.dcloud.common.a.a.3
                @Override // io.dcloud.common.DHInterface.ICallBack
                public Object onCallBack(int i, Object obj) {
                    if (BaseInfo.s_Runing_App_Count_Trim <= AppMgr.this.d.d() && BaseInfo.s_Runing_App_Count_Trim > 0 && BaseInfo.s_Runing_App_Count_Trim < BaseInfo.s_Runing_App_Count_Max) {
                        AppMgr.this.c();
                    }
                    boolean b = z ? dVar2.b(str2) : dVar2.c(str2);
                    if (!dVar2.f && dVar2.e) {
                        dVar2.b(str2);
                    }
                    if (b) {
                        AppMgr.this.d.a(str, dVar2);
                        return null;
                    }
                    Logger.e(Logger.AppMgr_TAG, str + " run failed!!!");
                    return null;
                }
            });
        } else if (dVar2.d == 2) {
            Logger.d(Logger.AppMgr_TAG, str + " will unactive change to active");
            dVar2.e();
        } else {
            Logger.d(Logger.AppMgr_TAG, str + " is active");
        }
        if (dVar2.isStreamApp() && Build.VERSION.SDK_INT >= 21) {
            d(dVar2);
        }
        if (dVar3 == null || dVar3 == dVar2) {
            return;
        }
        dVar3.i();
        dVar3.g();
    }

    WebApp a(String str) {
        return a((Activity) null, str);
    }

    WebApp a(Activity activity, String str) {
        return a(activity, str, true);
    }

    private WebApp a(String str, boolean z) {
        return a((Activity) null, str, z);
    }

    private WebApp a(Activity activity, String str, boolean z) {
        int indexOf;
        WebApp dVar = (!this.b.contains(str) || (indexOf = this.b.indexOf(str)) < 0) ? null : this.c.get(indexOf);
        if (dVar == null && z) {
            dVar = new WebApp(this, str, (byte) 0);
            dVar.setAppDataPath(BaseInfo.sBaseFsAppsPath + str + DeviceInfo.sSeparatorChar + BaseInfo.REAL_PRIVATE_WWW_DIR);
            if (dVar.Z == null) {
                dVar.Z = activity;
            }
            if (activity != null) {
                dVar.setWebAppIntent(activity.getIntent());
                b(activity, str);
            }
            dVar.a(str, (JSONObject) null);
            if (dVar.b.a) {
                dVar.mFeaturePermission = str;
            }
            a(dVar);
        } else if (dVar != null && activity != null) {
            if (dVar.Z == null) {
                dVar.Z = activity;
            }
            if (dVar.Z.getIntent() != null) {
                dVar.setWebAppIntent(dVar.Z.getIntent());
            } else {
                dVar.Z.setIntent(dVar.obtainWebAppIntent());
            }
            if (!dVar.e) {
                dVar.a(str, (JSONObject) null);
            }
        }
        return dVar;
    }

    private void b(final Activity activity, final String str) {
        final Intent intent;
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
        if (iActivityHandler != null && iActivityHandler.isStreamAppMode() && InvokeExecutorHelper.StorageUtils.invoke("checkDirResourceComplete", InvokeExecutorHelper.AppidUtils.invoke("getAppFilePathByAppid", str), true)) {
            SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
            if (orCreateBundle.getBoolean(str + "_smart_update_packge_success", false)) {
                Logger.d("AppMgr", str + " app 安装更新包");
                InvokeExecutorHelper.AppStreamUpdateManager.invoke("implementUpdate", str);
                if (orCreateBundle.getBoolean(str + SP.K_CREATED_SHORTCUT, false) && (intent = activity.getIntent()) != null) {
                    final String string = orCreateBundle.getString(str + SP.K_CREATE_SHORTCUT_NAME, null);
                    if (TextUtils.isEmpty(string) && orCreateBundle.getBoolean(str + "_smart_update_need_update_icon", false)) {
                        ImageLoader.getInstance().loadImage(DataInterface.getIconImageUrl(str, activity.getResources().getDisplayMetrics().widthPixels + ""), new ImageLoadingListener() { // from class: io.dcloud.common.a.a.4
                            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
                            public void onLoadingCancelled(String str2, View view) {
                            }

                            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
                            public void onLoadingStarted(String str2, View view) {
                            }

                            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
                            public void onLoadingFailed(String str2, View view, FailReason failReason) {
                                if (StringConst.canChangeHost(str2)) {
                                    ImageLoader.getInstance().loadImage(StringConst.changeHost(str2), this);
                                }
                            }

                            @Override // com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener
                            public void onLoadingComplete(String str2, View view, Bitmap bitmap) {
                                ShortCutUtil.updateShortcutFromDeskTop(activity, str, string, bitmap, intent.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME));
                            }
                        });
                    }
                }
                orCreateBundle.edit().remove(str + "_smart_update_need_update").remove(str + "_smart_update_packge_success").remove(str + "_smart_update_need_update_icon").commit();
            }
        }
    }

    void a() {
        WebApp b;
        if (BaseInfo.mBaseAppInfoSet == null || BaseInfo.mBaseAppInfoSet.isEmpty()) {
            return;
        }
        Set<String> keySet = BaseInfo.mBaseAppInfoSet.keySet();
        int size = keySet.size();
        String[] strArr = new String[size];
        keySet.toArray(strArr);
        for (int i = 0; i < size; i++) {
            String str = strArr[i];
            BaseInfo.BaseAppInfo baseAppInfo = BaseInfo.mBaseAppInfoSet.get(str);
            if (!BaseInfo.mUnInstalledAppInfoSet.containsKey(str) && !c(str) && (b = b(BaseInfo.sBaseResAppsPath + str, str)) != null && b.b != null) {
                if (!b.b.a) {
                    b.c = baseAppInfo;
                    a(b);
                } else {
                    Logger.e("AppMgr", str + "  app error," + b.b);
                }
            }
        }
    }

    void a(WebApp dVar) {
        this.b.add(dVar.obtainAppId());
        this.c.add(dVar);
    }

    void b(WebApp dVar) {
        this.b.remove(dVar.mFeaturePermission);
        this.c.remove(dVar);
    }

    void b(String str) {
        WebApp b = this.d.b(str);
        if (b != null) {
            b.g();
            BaseInfo.mUnInstalledAppInfoSet.put(str, b.c);
            b(b);
        }
    }

    WebApp a(String str, String str2, String str3, byte b) {
        WebApp a = a(str, false);
        if (a == null) {
            a = new WebApp(this, str, b);
            a.d = (byte) 3;
            a.mFeaturePermission = str;
            if (!PdrUtil.isEmpty(str2)) {
                a.setAppDataPath(str2);
            }
            a.B = str3;
            a(a);
            this.d.a(str, a);
        }
        return a;
    }

    void b() {
        if (BaseInfo.mInstalledAppInfoSet == null || BaseInfo.mInstalledAppInfoSet.isEmpty()) {
            return;
        }
        Set<String> keySet = BaseInfo.mInstalledAppInfoSet.keySet();
        int size = keySet.size();
        String[] strArr = new String[size];
        keySet.toArray(strArr);
        boolean z = false;
        for (int i = 0; i < size; i++) {
            String str = strArr[i];
            if (!BaseInfo.mUnInstalledAppInfoSet.containsKey(str) && !c(str)) {
                WebApp b = b(BaseInfo.sBaseFsAppsPath + str, str);
                if (b != null && b.b != null && !b.b.a) {
                    b.A = false;
                    a(b);
                } else {
                    BaseInfo.mInstalledAppInfoSet.get(str).clearBundleData();
                    BaseInfo.mInstalledAppInfoSet.remove(str);
                    z = true;
                }
            }
        }
        if (z) {
            BaseInfo.saveInstalledAppInfo();
        }
    }

    private boolean c(String str) {
        return this.b.contains(str);
    }

    private WebApp b(String str, String str2) {
        return a(str, str2);
    }

    WebApp a(String str, String str2) {
        return a(str, str2, (JSONObject) null);
    }

    WebApp a(String str, String str2, JSONObject jSONObject) {
        Exception e;
        PackageInfo packageInfo;
        WebApp a = a(str2, false);
        if (a != null) {
            try {
                a.b.a();
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                Logger.e(Logger.AppMgr_TAG, "installWebApp " + str + " is Illegal path");
                return a;
            }
        }
        InputStream r4 = null;
        try {
            if (!DHFile.isExist(str) && !PdrUtil.isDeviceRootDir(str)) {
                boolean contains = str.substring(str.lastIndexOf(47)).contains(".wgt");
                r4 = contains ? PlatformUtil.getResInputStream(str) : null;
                if (a == null) {
                    a = new WebApp(this, str2, (byte) 1);
                }
                if (!contains && r4 == null) {
                    a.setAppDataPath(str + DeviceInfo.sSeparatorChar + BaseInfo.REAL_PRIVATE_WWW_DIR);
                    a.a(str2, jSONObject);
                    IOUtil.close(r4);
                    return a;
                }
                a.a(r4);
                IOUtil.close(r4);
                return a;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        boolean isFile = new File(str).isFile();
        if (isFile) {
            if (isFile && str.toLowerCase().endsWith(".wgtu")) {
                if (a == null) {
                    a = new WebApp(this, str2, (byte) 0);
                }
                a.b(str, jSONObject);
                a.b.c = false;
                a.b.d = false;
            } else if (isFile && str.toLowerCase().endsWith(".wgt")) {
                boolean z2 = a == null;
                a.b.d = true;
                if (z2) {
                    WebApp dVar = new WebApp(this, str2, (byte) 0);
                    try {
                        dVar.mFeaturePermission = str2;
                        dVar.setAppDataPath(BaseInfo.sBaseFsAppsPath + str2 + DeviceInfo.sSeparatorChar + BaseInfo.REAL_PRIVATE_WWW_DIR);
                        a = dVar;
                    } catch (Exception e3) {
                        e = e3;
                        a = dVar;
                        e.printStackTrace();
                        Logger.e(Logger.AppMgr_TAG, "installWebApp " + str + " is Illegal path");
                        return a;
                    }
                }
                boolean c = a.c(str, jSONObject);
                a.b.d = false;
                if (c && z2) {
                    a(a);
                }
            } else {
                try {
                    packageInfo = PlatformUtil.parseApkInfo(getContext(), str);
                } catch (Exception e4) {
                    e4.printStackTrace();
                    a.b.b = String.format(DOMException.JSON_ERROR_INFO, 10, e4.getMessage());
                    packageInfo = null;
                }
                if (packageInfo == null) {
                    a.b.a = true;
                } else {
                    a.b.a = false;
                    String str3 = packageInfo.versionName;
                    String str4 = packageInfo.packageName;
                    String obj = getContext().getPackageManager().getApplicationLabel(packageInfo.applicationInfo).toString();
                    if (obj == null) {
                        obj = "";
                    }
                    a.b.b = String.format("{pname:'%s',version:'%s',name:'%s'}", str4, str3, obj);
                    PlatformUtil.openFileBySystem(getContext(), str, null, null);
                }
            }
        } else {
            if (a != null) {
                a.a((byte) 0);
            } else {
                a = new WebApp(this, str2, (byte) 0);
            }
            a.setAppDataPath(str + DeviceInfo.sSeparatorChar + BaseInfo.REAL_PRIVATE_WWW_DIR);
            a.a(str2, jSONObject);
        }
        IOUtil.close(r4);
        return a;
    }

    @Override // io.dcloud.common.DHInterface.AbsMgr
    public void dispose() {
        ArrayList<WebApp> arrayList = this.c;
        if (arrayList != null) {
            Iterator<WebApp> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().m();
            }
        }
        this.c.clear();
        this.b.clear();
        b bVar = this.d;
        if (bVar != null) {
            bVar.e();
        }
        this.d = null;
        ThreadPool.self().addThreadTask(new Runnable() { // from class: io.dcloud.common.a.a.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    DHFile.deleteFile(StringConst.STREAMAPP_KEY_ROOTPATH + "splash_temp/");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(WebApp dVar) {
        this.d.b(dVar.mFeaturePermission);
        b(dVar);
    }
}
