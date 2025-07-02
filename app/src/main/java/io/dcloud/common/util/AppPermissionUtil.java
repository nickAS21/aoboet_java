package io.dcloud.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;

/* loaded from: classes.dex */
public class AppPermissionUtil {
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_ASK = 4;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_IGNORED = 1;
    public static final int MODE_UNKNOWN = -1;
    public static HashMap<String, Integer> mXiaoMiCode19OPSIDs = new HashMap<>();
    public static HashMap<String, Integer> mXiaoMiCode21OPSIDs = new HashMap<>();
    public static HashMap<String, Integer> mXiaoMiCode23OPSIDs = new HashMap<>();
    public static String OP_INSTALL_SHORTCUT = "op_install_shortcut";

    static {
        mXiaoMiCode19OPSIDs.put("op_install_shortcut", 60);
        mXiaoMiCode21OPSIDs.put(OP_INSTALL_SHORTCUT, 63);
        mXiaoMiCode23OPSIDs.put(OP_INSTALL_SHORTCUT, 10017);
    }

    public static int getShotCutOpId() {
        if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.XIAOMI)) {
            return (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) && Build.VERSION.SDK_INT == 23) ? 16777216 : -1;
        }
        switch (Build.VERSION.SDK_INT) {
            case 19:
                return mXiaoMiCode19OPSIDs.get(OP_INSTALL_SHORTCUT).intValue();
            case 20:
            default:
                return -1;
            case 21:
            case 22:
                return mXiaoMiCode21OPSIDs.get(OP_INSTALL_SHORTCUT).intValue();
            case 23:
                return mXiaoMiCode23OPSIDs.get(OP_INSTALL_SHORTCUT).intValue();
        }
    }

    public static int checkOp(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return -1;
        }
        Object systemService = context.getSystemService(Context.APP_OPS_SERVICE);
        Class<?> cls = systemService.getClass();
        try {
            int intValue = ((Integer) cls.getDeclaredField("OP_INSTALL_SHORTCUT").get(cls)).intValue();
            Method declaredMethod = cls.getDeclaredMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
            String packageName = context.getPackageName();
            if (BaseInfo.isForQihooHelper(context)) {
                packageName = "com.qihoo.appstore";
            }
            return ((Integer) declaredMethod.invoke(systemService, Integer.valueOf(intValue), Integer.valueOf(Binder.getCallingUid()), packageName)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean checkShortcutOps(IApp iApp, Activity activity, String str, String str2) {
        SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
        if (getCheckShortcutOps(activity) != 1) {
            return true;
        }
        showShortCutOpsDialog(iApp, activity, str, orCreateBundle);
        return false;
    }

    public static void showShortCutOpsDialog(final IApp iApp, final Activity activity, final String str, final SharedPreferences sharedPreferences) {
        String str2;
        AlertDialog.Builder initDialogTheme = DialogUtil.initDialogTheme(activity, !BaseInfo.isForQihooHelper(activity));
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
            str2 = "桌面快捷方式权限被禁用，无法在桌面安装本应用。请点击“前往设置权限”打开应用信息界面-权限管理-桌面快捷方式-允许。";
        } else {
            str2 = Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) ? "桌面快捷方式权限被禁用，无法在桌面安装本应用。请点击“前往设置权限”打开应用信息界面-权限-设置单项权限-创建桌面快捷方式-允许." : "桌面快捷方式权限被禁用，无法在桌面安装本应用。请点击“前往设置权限”-在打开的界面滚动到最下方-点击权限管理-滚动到最下方-点击桌面快捷方式-允许。";
        }
        initDialogTheme.setTitle("开启“桌面快捷方式”权限").setMessage(str2).setPositiveButton("前往设置权限", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.AppPermissionUtil.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri parse = Uri.parse("package:" + activity.getPackageName());
                if (BaseInfo.isForQihooHelper(activity)) {
                    parse = Uri.parse("package:com.qihoo.appstore");
                }
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", parse);
                sharedPreferences.edit().putBoolean(str + SP.IS_CREATE_SHORTCUT, true).commit();
                activity.startActivity(intent);
            }
        }).setNegativeButton("不在桌面安装", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.AppPermissionUtil.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AppPermissionUtil.againShortcutOpsDialog(iApp, activity, str, iApp.obtainAppName());
            }
        });
        AlertDialog create = initDialogTheme.create();
        create.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: io.dcloud.common.util.AppPermissionUtil.3
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                AppPermissionUtil.againShortcutOpsDialog(iApp, activity, str, iApp.obtainAppName());
            }
        });
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    public static void againShortcutOpsDialog(final IApp iApp, final Activity activity, final String str, String str2) {
        final IApp app = iApp;
        final SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
        AlertDialog.Builder initDialogTheme = DialogUtil.initDialogTheme(activity, !BaseInfo.isForQihooHelper(activity));
        initDialogTheme.setTitle("开启“桌面快捷方式”权限").setMessage("本应用无法安装到桌面，以后无法从桌面启动本应用。强烈建议配置桌面快捷方式权限!").setPositiveButton("前往设置权限", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.AppPermissionUtil.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri parse = Uri.parse("package:" + activity.getPackageName());
                if (BaseInfo.isForQihooHelper(activity)) {
                    parse = Uri.parse("package:com.qihoo.appstore");
                }
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", parse);
                orCreateBundle.edit().putBoolean(str + SP.IS_CREATE_SHORTCUT, true).commit();
                activity.startActivity(intent);
            }
        }).setNegativeButton("不在桌面安装", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.AppPermissionUtil.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ShortCutUtil.commitShortcut(app, 12, true, false, false, 0);
                if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU) || Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI)) {
                    ShortCutUtil.createShortcutToDeskTop(app, false);
                }
            }
        });
        AlertDialog create = initDialogTheme.create();
        create.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: io.dcloud.common.util.AppPermissionUtil.6
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                ShortCutUtil.commitShortcut(app, 12, true, false, false, 0);
            }
        });
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    public static int getCheckShortcutOps(Activity activity) {
        if (-1 != getShotCutOpId()) {
            return checkOp(activity);
        }
        return 0;
    }

    public static boolean isFlymeShortcutallowAllow(Context context, Intent intent) {
        int flymeShortcutPid = getFlymeShortcutPid();
        return flymeShortcutPid == -1 || getFlymePermissionGranted(context, flymeShortcutPid, intent) != 1;
    }

    private static int getFlymePermissionGranted(Context context, int i, Intent intent) {
        try {
            Class<?> cls = Class.forName("meizu.security.IFlymePermissionService$Stub");
            Class<?> cls2 = Class.forName("android.os.ServiceManager");
            Object invoke = cls.getDeclaredMethod("asInterface", IBinder.class).invoke(cls, (IBinder) cls2.getDeclaredMethod("getService", String.class).invoke(cls2, "flyme_permission"));
            Method method = invoke.getClass().getMethod("noteIntentOperation", Integer.TYPE, Integer.TYPE, String.class, Intent.class);
            int callingPid = Binder.getCallingPid();
            String packageName = context.getPackageName();
            if (BaseInfo.isForQihooHelper(context)) {
                packageName = "com.qihoo.appstore";
            }
            return ((Integer) method.invoke(invoke, Integer.valueOf(i), Integer.valueOf(callingPid), packageName, intent)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getFlymeShortcutPid() {
        try {
            Class<?> cls = Class.forName("meizu.security.FlymePermissionManager");
            Field declaredField = cls.getDeclaredField("OP_SEND_SHORTCUT_BROADCAST");
            declaredField.setAccessible(true);
            return ((Integer) declaredField.get(cls)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isEmuiShortcutallowAllow() {
        int shotCutOpId = 0;
        try {
            shotCutOpId = getShotCutOpId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (-1 == shotCutOpId) {
            return true;
        }
        Class<?> cls = null;
        try {
            cls = Class.forName("com.huawei.hsm.permission.StubController");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            return ((Integer) cls.getDeclaredMethod("holdForGetPermissionSelection", Integer.TYPE, Integer.TYPE, Integer.TYPE, String.class).invoke(cls, Integer.valueOf(shotCutOpId), Integer.valueOf(Binder.getCallingUid()), Integer.valueOf(Binder.getCallingPid()), null)).intValue() != 2;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static int checkPermission(Context context, String str) {
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
            return !isFlymeShortcutallowAllow(context, ShortCutUtil.getHeadShortCutIntent(str)) ? 1 : 3;
        }
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.XIAOMI)) {
            int checkOp = checkOp(context);
            if (checkOp == 0 || checkOp == 1) {
                return checkOp;
            }
            if (checkOp == 3 || checkOp == 4) {
                return 2;
            }
        } else if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI)) {
            return !isEmuiShortcutallowAllow() ? 1 : 3;
        }
        return 4;
    }
}
