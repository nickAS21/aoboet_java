package io.dcloud.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class ApkUtils {
    public static final int APK_DOWNGRADE = -1;
    public static final int APK_INSTALLED = 0;
    public static final int APK_UNINSTALLED = -2;
    public static final int APK_UPGRADE = 1;
    private static final int APP_TYPE_ALL = 0;
    private static final int APP_TYPE_NON_SYSTEM = 1;
    private static final int APP_TYPE_SYSTEM = 2;
    private static final String TAG = "ApkUtils";
    public static final String[] coreApks = {"com.android.phone", "com.android.inputmethod.latin", "android", "com.android.bluetooth", "com.android.certinstaller", "com.android.sidekick", "com.google.android.gsf", "com.google.android.partnersetup", "com.android.htmlviewer", "com.android.wallpaper.livepicker", "com.android.stk", "com.android.providers.userdictionary", "com.android.packageinstaller", "com.android.providers.telocation", "com.android.email", "com.android.providers.telephony", "com.android.calculator2", "com.android.providers.contacts", "com.android.browser", "com.android.monitor", "com.android.soundrecorder", "com.android.providers.media", "com.android.launcher", "com.android.calendar", "com.android.providers.calendar", "com.android.defcontainer", "com.android.settings", "com.android.providers.settings", "com.android.deskclock", "com.android.providers.drm", "com.android.providers.applications", "com.android.contacts", "com.android.gallery", "com.google.android.location", "com.android.fileexplorer", "com.android.updater", "com.android.providers.downloads.ui", "com.android.providers.downloads", "com.android.mms", "com.android.server.vpn", "com.android.providers.subscribedfeeds", "com.android.thememanager", "com.android.systemui", "com.android.wallpaper", "com.google.android.gm", "com.google.android.backup", "com.google.android.syncadapters.calendar", "com.google.android.syncadapters.contacts", "com.android.vending.updater", "com.android.vending", "com.google.android.feedback", "com.google.android.street", "com.android.setupwizard", "com.google.android.googlequicksearchbox", "com.google.android.apps.uploader", "com.android.camera", "com.google.android.apps.genie.geniewidget", "com.android.music", "com.android.musicvis", "com.google.android.voicesearch", "com.noshufou.android.su", "com.qihoo.root", "com.lbe.security.miui", "com.lbe.security.su", "com.lbe.security.shuame", "eu.chainfire.supersu", "com.miui.uac", "com.android.protips"};

    public static int getInstallState(PackageInfo packageInfo, int i) {
        if (packageInfo == null) {
            return -2;
        }
        if (packageInfo.versionCode == i) {
            return 0;
        }
        return packageInfo.versionCode < i ? 1 : -1;
    }

    public static PackageInfo getInstalledApp(Context context, PackageManager packageManager, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getInstalledAppNameByPackageInfo(Context context, PackageManager packageManager, PackageInfo packageInfo) {
        if (packageManager == null) {
            packageManager = context.getPackageManager();
        }
        return packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
    }

    public static List<PackageInfo> getInstalledApps(Context context) {
        return getInstalledApps(context, context.getPackageManager());
    }

    public static List<PackageInfo> getInstalledApps(Context context, PackageManager packageManager) {
        return getAllApps(context, packageManager, 0);
    }

    public static List<PackageInfo> getInstalledNonSystemApps(Context context, PackageManager packageManager) {
        return getAllApps(context, packageManager, 1);
    }

    public static List<PackageInfo> getInstalledSystemApps(Context context, PackageManager packageManager) {
        return getAllApps(context, packageManager, 2);
    }

    private static List<PackageInfo> getAllApps(Context context, PackageManager packageManager, int i) {
        ArrayList arrayList = new ArrayList();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(512);
        if (i == 0) {
            Iterator<PackageInfo> it = installedPackages.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
        } else if (i == 1) {
            for (PackageInfo packageInfo : installedPackages) {
                if (!isSystemApp(packageInfo.applicationInfo)) {
                    arrayList.add(packageInfo);
                }
            }
        } else if (i == 2) {
            for (PackageInfo packageInfo2 : installedPackages) {
                if (isSystemApp(packageInfo2.applicationInfo)) {
                    arrayList.add(packageInfo2);
                }
            }
        }
        return arrayList;
    }

    public static List<String> getInstalledAppPkgs(Context context) {
        ArrayList arrayList = new ArrayList();
        for (PackageInfo packageInfo : context.getPackageManager().getInstalledPackages(0)) {
            if (!isSystemApp(packageInfo.applicationInfo)) {
                arrayList.add(packageInfo.packageName);
            }
        }
        return arrayList;
    }

    public static List<PackageInfo> getInstalledPackages(PackageManager packageManager, int i) {
        return packageManager.getInstalledPackages(i);
    }

    public static boolean isApkInstalled(Context context, String str) {
        if (str == null) {
            return false;
        }
        if (str.equals(context.getPackageName())) {
            return true;
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 512) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isApkInstalled(Context context, String str, String str2) {
        if (str == null) {
            return false;
        }
        if (str.equals(context.getPackageName())) {
            return true;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 512);
            if (packageInfo == null) {
                return false;
            }
            return String.valueOf(packageInfo.versionCode).equals(str2);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void uninstall(Context context, String str) {
        try {
            Intent intent = new Intent("android.intent.action.DELETE", Uri.fromParts("package", str, null));
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean openApp(Context context, String str) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage == null) {
            return false;
        }
        context.startActivity(launchIntentForPackage);
        return true;
    }

    public static boolean isSystemApp(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & 1) <= 0 || applicationInfo.publicSourceDir.startsWith("data/dataapp") || applicationInfo.publicSourceDir.startsWith("/data/dataapp")) ? false : true;
    }

    public static String getAppSignatureMd5(Context context, String str) {
        try {
            return Md5Utils.md5LowerCase(Arrays.toString(context.getPackageManager().getPackageInfo(str, 64).signatures[0].toByteArray()));
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    private static String[] getPackageSignatures(Context context, String str) {
        try {
            PackageInfo packageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(str, 64);
            if (packageArchiveInfo == null || packageArchiveInfo.signatures == null || packageArchiveInfo.signatures.length <= 0) {
                return null;
            }
            return new String[]{HashUtils.getHash(Arrays.toString(packageArchiveInfo.signatures[0].toByteArray())).toLowerCase(), packageArchiveInfo.applicationInfo.packageName};
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static Object parsePackage(String str, int i) {
        Object invoke;
        try {
            Object newInstance = Build.VERSION.SDK_INT >= 21 ? ReflectUtils.getObjectConstructor(ReflectUtils.CLASSNAME_PAGEAGEPARSE, new Class[0]).newInstance(new Object[0]) : ReflectUtils.getObjectConstructor(ReflectUtils.CLASSNAME_PAGEAGEPARSE, String.class).newInstance(str);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics.setToDefaults();
            File file = new File(str);
            if (Build.VERSION.SDK_INT >= 21) {
                invoke = newInstance.getClass().getMethod("parsePackage", File.class, Integer.TYPE).invoke(newInstance, file, Integer.valueOf(i));
            } else {
                invoke = newInstance.getClass().getMethod("parsePackage", File.class, String.class, DisplayMetrics.class, Integer.TYPE).invoke(newInstance, file, str, displayMetrics, Integer.valueOf(i));
            }
            if (invoke == null) {
                Log.d(TAG, "---parsePackage is null------;;sourceFile=" + file.getAbsolutePath());
                return null;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                Method declaredMethod = newInstance.getClass().getDeclaredMethod("collectCertificates", ReflectUtils.classForName(ReflectUtils.CLASSNAME_PAGEAGEPARSE_PACKAGE), File.class, Integer.TYPE);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(newInstance, invoke, file, 1);
            } else {
                newInstance.getClass().getDeclaredMethod("collectCertificates", ReflectUtils.classForName(ReflectUtils.CLASSNAME_PAGEAGEPARSE_PACKAGE), Integer.TYPE).invoke(newInstance, invoke, 1);
            }
            return invoke;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getApkFileSignatureAndPackageName(Context context, String str) {
        Object obj;
        Signature[] apkSignature;
        try {
            String[] packageSignatures = getPackageSignatures(context, str);
            if (packageSignatures != null) {
                return packageSignatures;
            }
            try {
                obj = parsePackage(str, 64);
            } catch (OutOfMemoryError unused) {
                obj = null;
            }
            if (obj == null || (apkSignature = getApkSignature(obj, str)) == null || apkSignature.length <= 0) {
                return null;
            }
            return new String[]{HashUtils.getHash(Arrays.toString(apkSignature[0].toByteArray())).toLowerCase(), (String) ReflectUtils.getObjectFieldNoDeclared(ReflectUtils.getField(obj, "applicationInfo"), "packageName")};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Signature[] getApkSignature(Object obj, String str) {
        Signature[] signatureArr = new Signature[0];
        try {
            signatureArr = (Signature[]) ReflectUtils.getField(obj, "mSignatures");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
        if (signatureArr != null && signatureArr.length > 0) {
            return signatureArr;
        }
        return null;
    }

    public static String[] getApkFileSignatureAndPackageNameEx(Context context, String str) {
        Signature[] signatureArr;
        try {
            Object parsePackage = parsePackage(str, 64);
            if (parsePackage != null && (signatureArr = (Signature[]) ReflectUtils.getField(parsePackage, "mSignatures")) != null && signatureArr.length != 0) {
                return new String[]{HashUtils.getHash(Arrays.toString(signatureArr[0].toByteArray())).toLowerCase(), (String) ReflectUtils.getField(parsePackage, "packageName")};
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValidAppPackageName(String str) {
        return Pattern.compile("^[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*$").matcher(str).matches();
    }

    public static String getApkFileLable(Context context, String str) {
        Constructor<?> constructor;
        Object newInstance;
        try {
            Class<?> cls = Class.forName(ReflectUtils.CLASSNAME_PAGEAGEPARSE);
            Class<?>[] clsArr = {String.class};
            if (Build.VERSION.SDK_INT >= 21) {
                constructor = cls.getConstructor(new Class[0]);
            } else {
                constructor = cls.getConstructor(clsArr);
            }
            Object[] objArr = {str};
            if (Build.VERSION.SDK_INT >= 21) {
                newInstance = constructor.newInstance(new Object[0]);
            } else {
                newInstance = constructor.newInstance(objArr);
            }
            Log.d("DownloadUtils", "pkgParser:" + newInstance.toString());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics.setToDefaults();
            Object invoke = cls.getDeclaredMethod("parsePackage", Build.VERSION.SDK_INT >= 21 ? new Class[]{File.class, Integer.TYPE} : new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE}).invoke(newInstance, Build.VERSION.SDK_INT >= 21 ? new Object[]{new File(str), 0} : new Object[]{new File(str), str, displayMetrics, 0});
            ApplicationInfo applicationInfo = (ApplicationInfo) invoke.getClass().getDeclaredField("applicationInfo").get(invoke);
            Log.d("DownloadUtils", "pkg:" + applicationInfo.packageName + " uid=" + applicationInfo.uid);
            Class<?> cls2 = Class.forName("android.content.res.AssetManager");
            Object newInstance2 = cls2.getConstructor((Class[]) null).newInstance((Object[]) null);
            cls2.getDeclaredMethod("addAssetPath", String.class).invoke(newInstance2, str);
            Resources resources = context.getResources();
            CharSequence text = applicationInfo.labelRes != 0 ? ((Resources) Resources.class.getConstructor(newInstance2.getClass(), resources.getDisplayMetrics().getClass(), resources.getConfiguration().getClass()).newInstance(newInstance2, resources.getDisplayMetrics(), resources.getConfiguration())).getText(applicationInfo.labelRes) : null;
            Log.d("DownloadUtils", "label=" + ((Object) text));
            return text.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }
}
