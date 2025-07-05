package io.dcloud.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.nostra13.dcloudimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

import io.dcloud.RInformation;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.constant.StringConst;

/* loaded from: classes.dex */
public class ShortCutUtil_En {
    public static final String ACTION_DESKTOP_LINK = "com.qihoo.browser.action.SHORTCUT2";
    public static final String NOPERMISSIONS = "nopermissions";
    private static final String PLUGIN_INTENT_PLUGIN_ACTIVITY = "com.qihoo.browser.pluginIntent.activity";
    private static final String PLUGIN_INTENT_PLUGIN_NAME = "com.qihoo.browser.pluginIntent.name";
    private static final String PLUGIN_INTENT_TYPE = "com.qihoo.browser.pluginIntent";
    private static final String SHORTCUT_SRC_QIHOO = "short_cut_src_qihoo";
    private static final String SHORTCUT_SRC_STREAM_APPS = "short_cut_src_stream_apps";
    public static final String SHORT_CUT_EXISTING = "short_cut_existing";
    public static final String SHORT_CUT_NONE = "short_cut_none";
    public static final String UNKNOWN = "unknown";
    static TypeRunnable mRunnable;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface TypeRunnable extends Runnable {
        String getType();

        void setType(String str);
    }

    public static void updateShortcutFromDeskTop(Activity activity, String str, String str2, Bitmap bitmap, String str3) {
        removeShortcutFromDeskTop(activity, str, str2, str3, null);
        createShortcutToDeskTop(activity, str, str2, bitmap, str3, null, false);
    }

    public static boolean removeShortcutFromDeskTop(Context context, String str, String str2, String str3, JSONObject jSONObject) {
        Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        intent.putExtra("android.intent.extra.shortcut.NAME", str2);
        intent.putExtra("duplicate", false);
        Intent intent2 = new Intent();
        if (TextUtils.isEmpty(str3)) {
            intent2 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        } else {
            intent2.setClassName(context.getPackageName(), str3);
            intent2.setAction("android.intent.action.MAIN");
            intent2.addCategory("android.intent.category.LAUNCHER");
        }
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                try {
                    String next = keys.next();
                    intent2.putExtra(next, jSONObject.getString(next));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        addShortCutSrc(context, intent2);
        intent2.putExtra(IntentConst.SHORT_CUT_APPID, str);
        intent2.putExtra(IntentConst.FROM_SHORT_CUT_STRAT, true);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.setData(Uri.parse("http://m3w.cn/s/" + str));
        intent.putExtra("android.intent.extra.shortcut.INTENT", intent2);
        context.sendBroadcast(intent);
        return true;
    }

    public static boolean createShortcutToDeskTop(Context context, String str, String str2, Bitmap bitmap, String str3, JSONObject jSONObject, boolean z) {
        return createShortcutToDeskTop(context, str, str2, bitmap, str3, jSONObject, z, false);
    }

    public static boolean createShortcutToDeskTop(IApp iApp, boolean z) {
        String path = ImageLoader.getInstance().getDiscCache().get(DataInterface.getIconImageUrl(iApp.obtainAppId(), iApp.getActivity().getResources().getDisplayMetrics().widthPixels + "")).getPath();
        if (!TextUtils.isEmpty(path) && path.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            path = path.substring(7);
        }
        Bitmap decodeFile = BitmapFactory.decodeFile(path);
        Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
        return createShortcutToDeskTop(iApp.getActivity(), iApp.obtainAppId(), iApp.obtainAppName(), decodeFile, obtainWebAppIntent != null ? obtainWebAppIntent.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME) : "", null, false, z);
    }

    public static boolean createShortcutToDeskTop(Context context, String str, String str2, Bitmap bitmap, String str3, JSONObject jSONObject, boolean z, boolean z2) {
        Intent headShortCutIntent = getHeadShortCutIntent(str2);
        Intent intent = new Intent();
        if (TextUtils.isEmpty(str3)) {
            intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        } else {
            intent.setClassName(context.getPackageName(), str3);
            intent.setAction("android.intent.action.MAIN");
            if (!BaseInfo.isForQihooHelper(context) && !BaseInfo.isStreamSDK) {
                intent.addCategory("android.intent.category.LAUNCHER");
            }
        }
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                try {
                    String next = keys.next();
                    intent.putExtra(next, jSONObject.getString(next));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        addShortCutSrc(context, intent);
        intent.putExtra(IntentConst.SHORT_CUT_APPID, str);
        intent.putExtra(IntentConst.FROM_SHORT_CUT_STRAT, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String str4 = "http://m3w.cn/s/" + str + (z2 ? "&time=" + System.currentTimeMillis() : "");
        intent.setData(Uri.parse(str4));
        if (BaseInfo.isForQihooBrowser(context)) {
            intent.setAction(ACTION_DESKTOP_LINK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setDataAndType(str4 == null ? null : Uri.parse(str4), PLUGIN_INTENT_TYPE);
            intent.putExtra(PLUGIN_INTENT_PLUGIN_NAME, DeviceInfo.SWITCH_DIRECTORY);
            intent.putExtra(PLUGIN_INTENT_PLUGIN_ACTIVITY, "io.dcloud.streamapp.StreamAppListActivity");
            intent.putExtra("type", 1);
            intent.putExtra("appid", str);
            intent.setClassName(context, "com.qihoo.browser.activity.SplashActivity");
        } else if (BaseInfo.isStreamSDK) {
            intent.setClassName(context, "io.dcloud.appstream.StreamAppMainActivity");
        }
        headShortCutIntent.putExtra("android.intent.extra.shortcut.INTENT", intent);
        headShortCutIntent.putExtra("android.intent.extra.shortcut.ICON", bitmap);
        context.sendBroadcast(headShortCutIntent);
        return true;
    }

    private static void addShortCutSrc(Context context, Intent intent) {
        if (BaseInfo.isStreamApp(context)) {
            intent.putExtra(IntentConst.SHORT_CUT_SRC, SHORTCUT_SRC_STREAM_APPS);
        } else if (BaseInfo.isForQihooHelper(context)) {
            intent.putExtra(IntentConst.SHORT_CUT_SRC, SHORTCUT_SRC_QIHOO);
        } else if (BaseInfo.isStreamSDK) {
            intent.putExtra(IntentConst.SHORT_CUT_SRC, BaseInfo.sChannel);
        }
    }

    public static boolean hasShortcut(Context context, String str) {
        if (!Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) || Build.VERSION.SDK_INT < 23) {
            return SHORT_CUT_EXISTING.equals(requestShortCut(context, str));
        }
        return false;
    }

    public static String requestShortCut(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uriFromLauncher = getUriFromLauncher(context);
        Logger.e("shortcututil", "requestShortCut: uri===" + uriFromLauncher);
        String str2 = "unknown";
        if (uriFromLauncher != null) {
            try {
                Cursor query = contentResolver.query(uriFromLauncher, new String[]{AbsoluteConst.JSON_KEY_TITLE, "intent"}, "title=? ", new String[]{str}, null);
                if (query == null || query.getCount() <= 0) {
                    str2 = SHORT_CUT_NONE;
                } else {
                    Logger.e("shortcututil", "c != null && c.getCount() > 0");
                    while (query.moveToNext()) {
//                        String string = query.getString(query.getColumnIndex("intent"));
                        int columnIndex = query.getColumnIndex("intent");
                        String string = null;
                        if (columnIndex >= 0) {
                            string = query.getString(columnIndex);
                        }
                        if (TextUtils.isEmpty(string)) {
                            str2 = SHORT_CUT_NONE;
                        } else {
                            Logger.e("shortcututil", "intent=====" + string);
                            if (string.contains(IntentConst.SHORT_CUT_APPID)) {
                                if (BaseInfo.isStreamApp(context)) {
                                    if (!string.contains(SHORTCUT_SRC_STREAM_APPS) && !string.contains("io.dcloud.appstream.StreamAppMainActivity")) {
                                    }
                                    str2 = SHORT_CUT_EXISTING;
                                } else if (BaseInfo.isForQihooHelper(context)) {
                                    if (!string.contains(SHORTCUT_SRC_QIHOO) && !string.contains("io.dcloud.appstream.StreamAppListFakeActivity")) {
                                    }
                                    str2 = SHORT_CUT_EXISTING;
                                } else {
                                    if (!BaseInfo.isBase(context)) {
                                        if (BaseInfo.isStreamSDK) {
                                            if (!string.contains(BaseInfo.sChannel) && !string.contains("io.dcloud.appstream.StreamAppMainActivity")) {
                                            }
                                        }
                                    }
                                    str2 = SHORT_CUT_EXISTING;
                                }
                            }
                        }
                    }
                }
                if (query != null && !query.isClosed()) {
                    query.close();
                }
            } catch (Exception e) {
                if (e.getMessage().contains("READ_SETTINGS")) {
                    str2 = NOPERMISSIONS;
                }
                e.printStackTrace();
            }
        }
        return str2;
    }

    public static String requestShortCutForCommit(Context context, String str) {
        return (!Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) || Build.VERSION.SDK_INT < 23) ? requestShortCut(context, str) : "unknown";
    }

    public static String getSreamAppShortName(Context context) {
        try {
            JSONArray jSONArray = new JSONArray();
            Cursor query = context.getContentResolver().query(getUriFromLauncher(context), new String[]{AbsoluteConst.JSON_KEY_TITLE, "intent"}, null, null, null);
            if (query != null && query.getCount() > 0) {
                while (query.moveToNext()) {
//                    String string = query.getString(query.getColumnIndex("intent"));
                    int columnIndex = query.getColumnIndex("intent");
                    String string = null;
                    if (columnIndex >= 0) {
                        string = query.getString(columnIndex);
                    }
//                    String string2 = query.getString(query.getColumnIndex(AbsoluteConst.JSON_KEY_TITLE));
                    int columnIndex2 = query.getColumnIndex(AbsoluteConst.JSON_KEY_TITLE);
                    String string2 = null;
                    if (columnIndex2 >= 0) {
                        string2 = query.getString(columnIndex);
                    }

                    if (!TextUtils.isEmpty(string2) && !TextUtils.isEmpty(string) && string.contains(IntentConst.SHORT_CUT_APPID)) {
                        JSONObject jSONObject = new JSONObject();
                        if (BaseInfo.isStreamApp(context)) {
                            if (string.contains(SHORTCUT_SRC_STREAM_APPS) || string.contains("io.dcloud.appstream.StreamAppMainActivity")) {
                                jSONObject.put("name", string2);
                                jSONArray.put(jSONObject);
                            }
                        } else if (BaseInfo.isForQihooHelper(context) && (string.contains(SHORTCUT_SRC_QIHOO) || string.contains("io.dcloud.appstream.StreamAppListFakeActivity"))) {
                            jSONObject.put("name", string2);
                            jSONArray.put(jSONObject);
                        }
                    }
                }
            }
            if (query != null && !query.isClosed()) {
                query.close();
            }
            return URLEncoder.encode(jSONArray.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static Uri getUriFromLauncher(Context context) {
        Logger.e("111", "getUriFromLauncher: Build.MANUFACTURER.==" + Build.MANUFACTURER);
        StringBuilder sb = new StringBuilder();
        String launcherPackageName = LauncherUtil.getLauncherPackageName(context);
        Logger.e("tag", "getUriFromLauncher: packageName" + launcherPackageName);
        if ("com.nd.android.pandahome2".equals(launcherPackageName)) {
            return Uri.parse("content://com.nd.android.launcher2.settings/com.nd.hilauncherdev/favorites?notify=true");
        }
        String authorityFromPermission = LauncherUtil.getAuthorityFromPermission(context, launcherPackageName + ".permission.READ_SETTINGS");
        Logger.e("TAG", "getUriFromLauncher: LauncherUtil.getAuthorityFromPermissionwithpackagename(" + authorityFromPermission);
        if (TextUtils.isEmpty(authorityFromPermission)) {
            authorityFromPermission = LauncherUtil.getAuthorityFromPermissionDefault(context);
            Logger.e("TAG", "getUriFromLauncher: LauncherUtil.getAuthorityFromPermissionDefault(" + authorityFromPermission);
        }
        if (!TextUtils.isEmpty(authorityFromPermission)) {
            sb.append("content://");
            sb.append(authorityFromPermission);
            if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.OPPO)) {
                sb.append("/singledesktopitems?notify=true");
            } else {
                sb.append("/favorites?notify=true");
            }
            return Uri.parse(sb.toString());
        }
        if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.QiKU)) {
            return Uri.parse("content://com.yulong.android.launcher3.compound/compoundworkspace?notify=false");
        }
        return null;
    }

    public static boolean isOpsCreateShortcut(Context context, String str) {
        SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
        boolean z = orCreateBundle.getBoolean(str + SP.IS_CREATE_SHORTCUT, false);
        if (z) {
            orCreateBundle.edit().remove(str + SP.IS_CREATE_SHORTCUT).commit();
        }
        return z;
    }

    public static boolean isRunShortCut(String str) {
        SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
        if (!orCreateBundle.getString(SP.RECORD_RUN_SHORT_CUT, "").equals(str)) {
            return false;
        }
        orCreateBundle.edit().remove(SP.RECORD_RUN_SHORT_CUT).commit();
        return true;
    }

    public static void onResumeCreateShortcut(IApp iApp) {
        boolean z;
        boolean z2;
        String path = ImageLoader.getInstance().getDiscCache().get(DataInterface.getIconImageUrl(iApp.obtainAppId(), iApp.getActivity().getResources().getDisplayMetrics().widthPixels + "")).getPath();
        int checkShortcutOps = AppPermissionUtil.getCheckShortcutOps(iApp.getActivity());
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
            z2 = AppPermissionUtil.isFlymeShortcutallowAllow(iApp.getActivity(), getHeadShortCutIntent(iApp.obtainAppName()));
            if (!z2) {
                commitShortcut(iApp, 12, true, false, true, 0);
            }
            z = false;
        } else if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI)) {
            boolean isEmuiShortcutallowAllow = AppPermissionUtil.isEmuiShortcutallowAllow();
            if (!isEmuiShortcutallowAllow) {
                commitShortcut(iApp, 12, true, false, true, 0);
            }
            z = isEmuiShortcutallowAllow;
            z2 = false;
        } else {
            if (checkShortcutOps == 1) {
                commitShortcut(iApp, 12, true, false, true, 0);
                AppPermissionUtil.checkShortcutOps(iApp, iApp.getActivity(), iApp.obtainAppId(), iApp.obtainAppName());
                if (!TextUtils.isEmpty(path) && path.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                    path = path.substring(7);
                }
                Bitmap decodeFile = BitmapFactory.decodeFile(path);
                Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
                createShortcutToDeskTop(iApp.getActivity(), iApp.obtainAppId(), iApp.obtainAppName(), decodeFile, obtainWebAppIntent != null ? obtainWebAppIntent.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME) : "", null, false);
                return;
            }
            z = false;
            z2 = false;
        }
        String str = Build.BRAND.equalsIgnoreCase(MobilePhoneModel.GOOGLE) ? Build.MANUFACTURER : Build.BRAND;
        if (MobilePhoneModel.SMARTISAN.equals(str) || ((checkShortcutOps != 1 && MobilePhoneModel.XIAOMI.equals(str)) || ((MobilePhoneModel.MEIZU.equals(str) && z2) || (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) && z)))) {
            commitShortcut(iApp, 12, true, true, true, 0);
        }
        createShortcut(iApp, path, null, false);
    }

    public static void createShortcut(IApp iApp, String str, Bitmap bitmap, boolean z) {
        Bitmap bitmap2;
        String str2 = str;
        Logger.e("StreamSDK", "come in createShortcut");
        if (iApp == null || TextUtils.isEmpty(str) || iApp.startFromShortCut() || iApp.forceShortCut().equals("none")) {
            return;
        }
        Logger.e("StreamSDK", "come out return 1");
        Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
        boolean z2 = obtainWebAppIntent != null && obtainWebAppIntent.getIntExtra(IntentConst.START_FROM, -1) == 5;
        Logger.e("StreamSDK", "isMyRuning" + z2);
        if (z2) {
            return;
        }
        Activity activity = iApp.getActivity();
        String obtainAppName = iApp.obtainAppName();
        String obtainAppId = iApp.obtainAppId();
        if (bitmap == null) {
            if (!TextUtils.isEmpty(str) && str2.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                str2 = str2.substring(7);
            }
            bitmap2 = BitmapFactory.decodeFile(str2);
        } else {
            bitmap2 = bitmap;
        }
        if (bitmap2 == null && !iApp.isStreamApp()) {
            bitmap2 = BitmapFactory.decodeResource(activity.getResources(), RInformation.DRAWABLE_ICON);
        }
        Bitmap bitmap3 = bitmap2;
        Intent obtainWebAppIntent2 = iApp.obtainWebAppIntent();
        String stringExtra = obtainWebAppIntent2 != null ? obtainWebAppIntent2.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME) : "";
        SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
        boolean z3 = orCreateBundle.getBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, true);
        if (ShortcutCreateUtil.isDisableShort(iApp.getActivity())) {
            handleDisableShort(iApp.getActivity(), obtainAppId, z3, orCreateBundle);
            orCreateBundle.edit().putBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, false).commit();
            if (iApp.isStreamApp()) {
                createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap3, stringExtra, null, false);
            }
            Logger.e("StreamSDK", "判断当前手机是否不支持创建快捷方式io.dcloud.common.util.ShortcutCreateUtil.isDisableShort(app.getActivity())" + ShortcutCreateUtil.isDisableShort(iApp.getActivity()));
            return;
        }
        if (MobilePhoneModel.isSpecialPhone(activity) && showSettingsDialog(iApp, str2, bitmap)) {
            Logger.e("StreamSDK", "检测如果当前手机为特殊对待的手机，并且未提示过创建快捷方式设置 拦截创建MobilePhoneModel.isSpecialPhone(context) && showSettingsDialog(app, filePath, bitmap)");
            return;
        }
        String string = orCreateBundle.getString(AbsoluteConst.TEST_RUN + obtainAppId, null);
        boolean z4 = !TextUtils.isEmpty(string) && string.equals("__am=t");
        if (!iApp.isCompetentStreamApp() && !z4) {
            Logger.e("StreamSDK", "流应用，并且未审核状态时，不自动创建快捷方式!app.isCompetentStreamApp() && !isTestShortCut");
            return;
        }
        if (hasShortcut(activity, obtainAppName)) {
            Logger.e("StreamSDK", "ShortCutUtil.hasShortcut(context, name)");
            return;
        }
        boolean z5 = orCreateBundle.getBoolean(obtainAppId + SP.K_CREATED_SHORTCUT, false);
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU) && !AppPermissionUtil.isFlymeShortcutallowAllow(activity, getHeadShortCutIntent(obtainAppName))) {
            AppPermissionUtil.showShortCutOpsDialog(iApp, iApp.getActivity(), obtainAppId, orCreateBundle);
            Logger.e("StreamSDK", "判断魅族快捷方式权限");
            return;
        }
        if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) && !AppPermissionUtil.isEmuiShortcutallowAllow()) {
            AppPermissionUtil.showShortCutOpsDialog(iApp, iApp.getActivity(), obtainAppId, orCreateBundle);
            Logger.e("StreamSDK", "判断华为快捷方式权限");
            return;
        }
        if (!ShortcutCreateUtil.isDuplicateLauncher(activity) && iApp.forceShortCut().equals("auto") && z5) {
            Logger.e("StreamSDK", "对支持去重和已创建快捷方式 并且无法查询是否创建快捷方式的不创建桌面图标");
            return;
        }
        if (!z5) {
            SP.getOrCreateBundle("streamapp_create_shortcut").getBoolean("is_create_shortcut" + obtainAppId, false);
        }
        if (BaseInfo.isForQihooHelper(activity) && ("H5EC86117".equalsIgnoreCase(obtainAppId) || "H5BCD03E4".equalsIgnoreCase(obtainAppId) || "H532A4BFF".equalsIgnoreCase(obtainAppId))) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(obtainAppId + "_storages", 0);
            if (sharedPreferences.contains("SHORTCUT") && Boolean.parseBoolean(sharedPreferences.getString("SHORTCUT", AbsoluteConst.FALSE))) {
                Logger.e("StreamSDK", "按照邮件要求如果是36Kr资讯  H5EC86117 SHORTCUT  true 点评外卖  H5BCD03E4 SHORTCUT true 挑食     H532A4BFF  SHORTCUT true就要去读取配置文件");
                return;
            }
        }
        if (orCreateBundle.getBoolean(SP.K_SHORT_CUT_ONE_TIPS, true)) {
            orCreateBundle.edit().putBoolean(SP.K_SHORT_CUT_ONE_TIPS, false).commit();
        }
        orCreateBundle.edit().putBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, false).commit();
        if (createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap3, stringExtra, null, false)) {
            Logger.e("StreamSDK", "come into createShortcutToDeskTop and return ture already");
            if (z) {
                if (isHasShortCut(iApp, 15000L, "auto")) {
                    return;
                } else {
                    showCreateShortCutToast(iApp);
                }
            } else if (isHasShortCut(iApp, 1000L, "auto")) {
                return;
            } else {
                showCreateShortCutToast(iApp);
            }
        }
        commitShortcut(iApp, 11, 0);
        orCreateBundle.edit().putString(obtainAppId + SP.K_CREATE_SHORTCUT_NAME, obtainAppName).commit();
        orCreateBundle.edit().putBoolean(obtainAppId + SP.K_CREATED_SHORTCUT, true).commit();
    }

    public static void commitShortcut(IApp iApp, int i, int i2) {
        commitShortcut(iApp, i, false, false, false, i2);
    }

    public static void commitShortcut(IApp iApp, int i, boolean z, boolean z2, boolean z3, int i2) {
        commitShortcut(iApp, i, z, z2, z3, i2, null);
    }

    /* JADX WARN: Type inference failed for: r8v0, types: [io.dcloud.common.util.ShortCutUtil$1] */
    public static void commitShortcut(final IApp iApp, final int i, final boolean z, final boolean z2, final boolean z3, final int i2, final String str) {
        new Thread() { // from class: io.dcloud.common.util.ShortCutUtil.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String str2;
                String str3;
                String str4 = str;
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(str4)) {
                    str4 = iApp.obtainAppName();
                }
                int checkPermission = AppPermissionUtil.checkPermission(iApp.getActivity(), str4);
                Intent intent = iApp.getActivity().getIntent();
                iApp.obtainAppVersionName();
                String str5 = StringConst.STREAMAPP_KEY_BASESERVICEURL() + "collect/startup?s=" + i + "&" + DataInterface.getUrlBaseData(iApp.getActivity(), iApp.obtainAppId(), BaseInfo.getLaunchType(iApp.getActivity().getIntent()), DataInterface.getStreamappFrom(intent)) + "&romv=" + DataInterface.getRomVersion() + "&scf=" + i2 + "&scp=" + checkPermission + "&v=" + PdrUtil.encodeURL(iApp.obtainAppVersionName());
                if (z) {
                    str3 = str5 + "&scr=" + (z2 ? 1 : 0) + "&scs=" + (z3 ? 1 : 0);
                } else {
                    String requestShortCutForCommit = ShortCutUtil.requestShortCutForCommit(iApp.getActivity(), str4);
                    if (ShortCutUtil.SHORT_CUT_EXISTING.equals(requestShortCutForCommit)) {
                        str2 = "s";
                    } else {
                        str2 = ShortCutUtil.SHORT_CUT_NONE.equals(requestShortCutForCommit) ? "n" : "u";
                    }
                    str3 = str5 + "&sc=" + str2;
                }
                try {
                    NetTool.httpGet(str3);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }.start();
    }

    public static void handleDisableShort(Activity activity, String str, boolean z, SharedPreferences sharedPreferences) {
        if (BaseInfo.isForQihooHelper(activity) && z && !ApkUtils.isApkInstalled(activity, "io.dcloud.streamapps")) {
            showDownloadStreams(activity);
        }
    }

    public static boolean showSettingsDialog(final IApp iApp, final String str, final Bitmap bitmap) {
        final IApp app = iApp;
        String str2;
        final SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
        if (!orCreateBundle.getBoolean(SP.K_SHORT_CUT_ONE_TIPS, true) && !Build.BRAND.equals(MobilePhoneModel.SMARTISAN)) {
            return false;
        }
        if (orCreateBundle.getBoolean(SP.K_SHORT_CUT_ONE_TIPS, true)) {
            orCreateBundle.edit().putBoolean(SP.K_SHORT_CUT_ONE_TIPS, false).commit();
        }
        String str3 = "Go to Settings";
        String str4 = "我设置过了";
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.QiKU)) {
            str2 = "QiKU phones need to ensure that 'Shortcuts' are enabled in settings before using stream apps. Click 'Go to Settings' to open the settings screen → Desktop → Enable Shortcuts. If there is no 'Desktop' option, please update to the latest QiKU ROM.";
            str3 = "Enable in Settings";
        } else if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.VIVO)) {
            if (!ApkUtils.isApkInstalled(iApp.getActivity(), "com.iqoo.secure") || Build.VERSION.SDK_INT < 21) {
                return false;
            }
            str2 = "VIVO手机使用流应用需先确定开启桌面快捷方式。请点击“Go to Settings”-软件管理-桌面快捷方式管理-“" + (BaseInfo.isForQihooHelper(iApp.getActivity()) ? "360手机助手" : "流应用") + "”-允许。如果软件管理界面中无“桌面快捷方式管理”项请升级到最新版ROM。";
        } else if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.SMARTISAN)) {
            str2 = "Smartisan phones using 9/16 grid view cannot install this app on the home screen. Please click 'Go to View Settings' → Desktop Settings → Choose 'Android Native' under single-panel view → Confirm in the popup.";
            str4 = "Do not install on desktop";
        } else {
            str2 = "";
        }
        AlertDialog.Builder initDialogTheme = DialogUtil.initDialogTheme(iApp.getActivity(), !BaseInfo.isForQihooHelper(iApp.getActivity()));
        initDialogTheme.setMessage(str2).setTitle("Create Desktop Shortcut Reminder").setPositiveButton(str3, new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.ShortCutUtil.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                orCreateBundle.edit().putString(SP.RECORD_RUN_SHORT_CUT, iApp.obtainAppId()).commit();
                if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.QiKU) || Build.BRAND.equalsIgnoreCase(MobilePhoneModel.SMARTISAN)) {
                    iApp.getActivity().startActivity(new Intent("android.settings.SETTINGS"));
                } else if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.VIVO)) {
                    PackageManager packageManager = iApp.getActivity().getPackageManager();
                    new Intent();
                    Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage("com.iqoo.secure");
                    launchIntentForPackage.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    launchIntentForPackage.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                    );
                    iApp.getActivity().startActivity(launchIntentForPackage);
                }
            }
        }).setNegativeButton(str4, new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.ShortCutUtil.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.SMARTISAN)) {
                    ShortCutUtil.createShortcut(app, str, bitmap, false);
                } else {
                    ShortCutUtil.commitShortcut(app, 12, true, false, false, 0);
                }
            }
        });
        AlertDialog create = initDialogTheme.create();
        create.setCanceledOnTouchOutside(false);
        create.show();
        return true;
    }

    public static boolean showSettingsDialog(final Activity activity, final String str, final String str2, final String str3, final Bitmap bitmap) {
        final SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
        AlertDialog.Builder initDialogTheme = DialogUtil.initDialogTheme(activity, !BaseInfo.isForQihooHelper(activity));
        initDialogTheme.setMessage("奇酷手机使用流应用需先确定权限开启，在设置-桌面设置中勾选快捷方式启用").setTitle("Shortcut Creation Reminder").setPositiveButton("前往设置", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.ShortCutUtil.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                orCreateBundle.edit().putString(SP.RECORD_RUN_SHORT_CUT, str).commit();
                activity.startActivity(new Intent("android.settings.SETTINGS"));
            }
        }).setNegativeButton("我设置过了", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.ShortCutUtil.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ShortCutUtil.createShortcutToDeskTop(activity, str, str2, bitmap, str3, null, false)) {
                    ShortCutUtil.runShortCutToast(activity);
                }
            }
        });
        AlertDialog create = initDialogTheme.create();
        create.setCanceledOnTouchOutside(false);
        create.show();
        return true;
    }

    public static void showDownloadStreams(final Activity activity) {
        AlertDialog.Builder initDialogTheme = DialogUtil.initDialogTheme(activity, !BaseInfo.isForQihooHelper(activity));
        initDialogTheme.setMessage("This device may not support installing apps on the home screen. You will need to launch it from the 360 Assistant next time, or you can download a standalone Stream App Manager.").setTitle("Friendly Reminder").setPositiveButton("Download Manager", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.ShortCutUtil.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://www.dcloud.io/streamapp/streamapp.apk"));
                activity.startActivity(intent);
            }
        }).setNegativeButton("I got it", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.ShortCutUtil.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog create = initDialogTheme.create();
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    public static void runShortCutToast(Activity activity) {
        if (ShortcutCreateUtil.canCreateShortcut(activity) && ShortcutCreateUtil.needToast(activity)) {
            Toast.makeText(activity, "Desktop shortcut created successfully", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ShortcutCreateUtil.isDisableShort(activity)) {
            if (ShortcutCreateUtil.isSystemLauncher(activity)) {
                Toast.makeText(activity, "Current system does not support creating desktop shortcuts", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(activity, "The current third-party launcher does not support creating desktop shortcuts", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (ShortcutCreateUtil.isDisableShort(activity) || ShortcutCreateUtil.canCreateShortcut(activity)) {
            return;
        }
        Toast.makeText(activity, "Please switch to the home screen to check whether the shortcut is created", Toast.LENGTH_SHORT).show();
    }

    public static boolean isHasShortCut(final IApp iApp, long j, String str) {
        final IApp app = iApp;
        if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.XIAOMI)) {
            return false;
        }
        TypeRunnable typeRunnable = mRunnable;
        if (typeRunnable != null) {
            if (typeRunnable.getType().equals("back") && str.equals(mRunnable.getType())) {
                return true;
            }
            removeRunHandler();
        }
        BaseInfo.isPostChcekShortCut = true;
        TypeRunnable typeRunnable2 = new TypeRunnable() { // from class: io.dcloud.common.util.ShortCutUtil.8
            String type;

            @Override // java.lang.Runnable
            public void run() {
                if (!iApp.getActivity().isFinishing()) { // ✅ правильно
                    SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");

                    if (ShortCutUtil.hasShortcut(iApp.getActivity(), iApp.obtainAppName())) {
                        orCreateBundle.edit().putString(iApp.obtainAppId() + SP.K_CREATE_SHORTCUT_NAME, iApp.obtainAppName()).commit();
                        orCreateBundle.edit().putBoolean(iApp.obtainAppId() + SP.K_CREATED_SHORTCUT, true).commit();
                        ShortCutUtil.commitShortcut(app, 11, 0);
                        ShortCutUtil.showCreateShortCutToast(app);
                    } else if (AppPermissionUtil.getCheckShortcutOps(iApp.getActivity()) == 0) {
                        ShortCutUtil.createShortcutToDeskTop(app, true);
                        ShortCutUtil.commitShortcut(app, 11, 0);
                        ShortCutUtil.showCreateShortCutToast(app);
                    } else {
                        ShortCutUtil.commitShortcut(app, 11, 0);
                        AppPermissionUtil.showShortCutOpsDialog(app, iApp.getActivity(), iApp.obtainAppId(), orCreateBundle);
                    }
                }
                ShortCutUtil.mRunnable = null;
            }

            @Override
            public String getType() {
                return this.type;
            }

            @Override
            public void setType(String str2) {
                this.type = str2;
            }
        };
        mRunnable = typeRunnable2;
        typeRunnable2.setType(str);
        MessageHandler.postDelayed(mRunnable, j);
        return true;
    }

    public static void removeRunHandler() {
        if (mRunnable != null) {
            BaseInfo.isPostChcekShortCut = false;
            MessageHandler.removeCallbacks(mRunnable);
        }
    }

    public static void showCreateShortCutToast(IApp iApp) {
        String format = String.format("\"%s\"已创建桌面图标", iApp.obtainAppName());
        if (iApp.forceShortCut().equals(AbsoluteConst.INSTALL_OPTIONS_FORCE) && !ShortcutCreateUtil.isDuplicateLauncher(iApp.getActivity())) {
            Toast.makeText(iApp.getActivity(), "“" + iApp.obtainAppName() + "”已创建桌面图标，如有重复请手动删除", Toast.LENGTH_LONG).show();
        } else if (ShortcutCreateUtil.needToast(iApp.getActivity())) {
            Toast.makeText(iApp.getActivity(), format, Toast.LENGTH_LONG).show();
        }
    }

    public static Intent getHeadShortCutIntent(String str) {
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra("android.intent.extra.shortcut.NAME", str);
        intent.putExtra("duplicate", false);
        return intent;
    }
}