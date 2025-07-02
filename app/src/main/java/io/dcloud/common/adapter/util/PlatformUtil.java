package io.dcloud.common.adapter.util;

import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;

/* loaded from: classes.dex */
public class PlatformUtil extends SP {
    private static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static boolean APS_COVER = false;
    public static final byte ASSETS_RESOUCE = 0;
    private static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
    public static final byte FILE_RESOUCE = 2;
    public static final byte SRC_RESOUCE = 1;
    private static final String URI_HTC_LAUNCER = "content://com.htc.launcher.settings/favorites?notify=true";
    private static final String URI_SAMSUNG_LAUNCER = "content://com.sec.android.app.twlauncher.settings/favorites?notify=true";
    private static int _SCREEN_CONTENT_HEIGHT;
    private static int _SCREEN_HEIGHT;
    private static int _SCREEN_STATUSBAR_HEIGHT;
    private static int _SCREEN_WIDTH;
    private static int[] _blackpixels;
    private static int[] _pixels;
    private static final String[] PROJECTION = {"_id", AbsoluteConst.JSON_KEY_TITLE, "iconResource"};
    private static int MAX_SPAN_IN_ONE_SCREEN = 16;

    public static void init(Context context) {
        DeviceInfo.sApplicationContext = context;
    }

    public static InputStream getResInputStream(String str) {
        try {
            return AndroidResources.sAssetMgr.open(useAndroidPath(str), 2);
        } catch (FileNotFoundException unused) {
            Logger.e("PlatformUtil.getResInputStream FileNotFoundException pFilePath=" + str);
            return null;
        } catch (IOException unused2) {
            Logger.e("PlatformUtil.getResInputStream IOException pFilePath=" + str);
            return null;
        } catch (RuntimeException unused3) {
            Logger.e("PlatformUtil.getResInputStream RuntimeException pFilePath=" + str);
            return null;
        }
    }

    public static String[] listResFiles(String str) {
        try {
            return AndroidResources.sAssetMgr.list(useAndroidPath(str));
        } catch (IOException e) {
            Logger.w("PlatformUtil.listResFiles pPath=" + str, e);
            return null;
        }
    }

    public static String[] listFsAppsFiles(String str) {
        try {
            return new File(str).list();
        } catch (Exception e) {
            Logger.w("PlatformUtil.listResFiles pPath=" + str, e);
            return null;
        }
    }

    private static String useAndroidPath(String str) {
        return StringUtil.trimString(StringUtil.trimString(str, '/'), '\\');
    }

    public static void openFileBySystem(Context context, String str, String str2, ICallBack iCallBack) {
        boolean hasAppInstalled = TextUtils.isEmpty(str2) ^ true ? hasAppInstalled(context, str2) : false;
        try {
            Intent intent = new Intent();
            if (hasAppInstalled) {
                intent.setPackage(str2);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.intent.action.VIEW");
            String mimeType = PdrUtil.getMimeType(str);
            if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                str = str.substring(7);
            }
            File file = new File(str);
            if (file.exists()) {
                intent.setDataAndType(Uri.fromFile(file), mimeType.toLowerCase());
                context.startActivity(intent);
            } else if (iCallBack != null) {
                iCallBack.onCallBack(-1, String.format(DOMException.JSON_ERROR_INFO, 0, DOMException.MSG_NOT_FOUND_FILE));
            }
        } catch (ActivityNotFoundException e) {
            Logger.w(e);
            if (iCallBack != null) {
                iCallBack.onCallBack(-1, String.format(DOMException.JSON_ERROR_INFO, 1, DOMException.MSG_NOT_FOUND_3TH));
            }
        }
    }

    public static int SCREEN_WIDTH(Context context) {
        if (_SCREEN_WIDTH == 0) {
            _SCREEN_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
        }
        return _SCREEN_WIDTH;
    }

    public static int SCREEN_HEIGHT(Context context) {
        if (_SCREEN_HEIGHT == 0) {
            _SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        }
        return _SCREEN_HEIGHT;
    }

    public static int MESURE_SCREEN_STATUSBAR_HEIGHT(Activity activity) {
        if (_SCREEN_STATUSBAR_HEIGHT == 0) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            _SCREEN_STATUSBAR_HEIGHT = rect.top;
            int height = rect.height();
            _SCREEN_CONTENT_HEIGHT = height;
            if (_SCREEN_STATUSBAR_HEIGHT < 0 || height > SCREEN_HEIGHT(activity)) {
                _SCREEN_STATUSBAR_HEIGHT = 0;
                _SCREEN_CONTENT_HEIGHT = SCREEN_HEIGHT(activity);
            }
        }
        return _SCREEN_STATUSBAR_HEIGHT;
    }

    public static int MESURE_SCREEN_CONTENT_HEIGHT(Activity activity) {
        if (_SCREEN_CONTENT_HEIGHT == 0) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            _SCREEN_STATUSBAR_HEIGHT = rect.top;
            int height = rect.height();
            _SCREEN_CONTENT_HEIGHT = height;
            if (_SCREEN_STATUSBAR_HEIGHT < 0 || height > SCREEN_HEIGHT(activity)) {
                _SCREEN_STATUSBAR_HEIGHT = 0;
                _SCREEN_CONTENT_HEIGHT = SCREEN_HEIGHT(activity);
            }
        }
        return _SCREEN_CONTENT_HEIGHT;
    }

    public static void RESET_H_W() {
        _SCREEN_WIDTH = 0;
        _SCREEN_HEIGHT = 0;
        _SCREEN_STATUSBAR_HEIGHT = 0;
        _SCREEN_CONTENT_HEIGHT = 0;
        _pixels = null;
    }

    public static Bitmap captureView(View view) {
        return captureView(view, false, (Rect) null);
    }

    public static Bitmap captureView(View view, Bitmap.Config config) {
        return captureView(view, false, (Rect) null, config);
    }

    public static Bitmap captureView(View view, boolean z, Rect rect) {
        return captureView(view, z, rect, Bitmap.Config.RGB_565);
    }

    public static Bitmap captureView(View view, boolean z, Rect rect, Bitmap.Config config) {
        int width = rect != null ? rect.width() : view.getMeasuredWidth();
        int height = rect != null ? rect.height() : view.getMeasuredHeight();
        if (width == 0) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(createBitmap);
        if (rect != null) {
            canvas.translate(-rect.left, -rect.top);
        }
        view.draw(canvas);
        if (!z || !isWhiteBitmap(createBitmap)) {
            return createBitmap;
        }
        createBitmap.recycle();
        return null;
    }

    private static int[] GET_WIHTE_LINE(int i) {
        if (_pixels == null) {
            _pixels = new int[i];
            int i2 = 0;
            while (true) {
                int[] iArr = _pixels;
                if (i2 >= iArr.length) {
                    break;
                }
                iArr[i2] = -1;
                i2++;
            }
        }
        return _pixels;
    }

    private static int[] GET_BLACK_LINE(int i) {
        if (_blackpixels == null) {
            _blackpixels = new int[i];
            int i2 = 0;
            while (true) {
                int[] iArr = _blackpixels;
                if (i2 >= iArr.length) {
                    break;
                }
                iArr[i2] = -16777216;
                i2++;
            }
        }
        return _blackpixels;
    }

    public static boolean isWhiteBitmap(Bitmap bitmap) {
        return isWhiteBitmap(bitmap, false);
    }

    public static boolean isWhiteBitmap(Bitmap bitmap, boolean z) {
        int height = bitmap.getHeight();
        int[] iArr = new int[height];
        bitmap.getPixels(iArr, 0, 1, bitmap.getWidth() / 4, 0, 1, height);
        boolean equals = Arrays.equals(iArr, GET_WIHTE_LINE(height));
        bitmap.getPixels(iArr, 0, 1, bitmap.getWidth() / 2, 0, 1, height);
        boolean equals2 = equals & Arrays.equals(iArr, GET_WIHTE_LINE(height));
        bitmap.getPixels(iArr, 0, 1, (bitmap.getWidth() * 3) / 4, 0, 1, height);
        boolean equals3 = Arrays.equals(iArr, GET_WIHTE_LINE(height)) & equals2;
        return z ? equals3 | Arrays.equals(iArr, GET_BLACK_LINE(height)) : equals3;
    }

    public static boolean isLineWhiteBitmap(Bitmap bitmap, boolean z) {
        int width = bitmap.getWidth();
        int[] iArr = new int[width];
        bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), 1);
        boolean equals = Arrays.equals(iArr, GET_WIHTE_LINE(width));
        return z ? equals | Arrays.equals(iArr, GET_BLACK_LINE(width)) : equals;
    }

    public static void destroyDrawingCache(View view) {
        view.destroyDrawingCache();
    }

    public static Bitmap captureView(View view, String str) {
        return captureView(view, (Rect) null, str);
    }

    public static Bitmap captureView(View view, Rect rect, String str) {
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            boolean z = rect != null;
            if (z) {
                view.layout(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
            } else {
                view.layout(0, 0, width, height);
            }
            view.setDrawingCacheEnabled(true);
            Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
            createBitmap.setDensity(view.getContext().getResources().getDisplayMetrics().densityDpi);
            if (!PdrUtil.isEmpty(str)) {
                PdrUtil.saveBitmapToFile(createBitmap, str);
            }
            view.setDrawingCacheEnabled(false);
            if (z) {
                view.layout(0, 0, width, height);
            }
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void launchApplication(Context context, String str, String str2, HashMap<String, Object> hashMap) throws Exception {
        Intent intent;
        if (PdrUtil.isEmpty(str2)) {
            intent = new Intent("android.intent.action.MAIN");
        } else {
            intent = new Intent(str2);
        }
        if (!PdrUtil.isEmpty(str) && !setPackageName(context, intent, str)) {
            throw new RuntimeException();
        }
        intent.setFlags(FLAG_ACTIVITY_RESET_TASK_IF_NEEDED & Intent.FLAG_ACTIVITY_NEW_TASK);
        if (hashMap != null && !hashMap.isEmpty()) {
            for (String str3 : hashMap.keySet()) {
                Object obj = hashMap.get(str3);
                if (obj instanceof Integer) {
                    intent.putExtra(str3, ((Integer) obj).intValue());
                } else if (obj instanceof String) {
                    intent.putExtra(str3, (String) obj);
                } else if (obj instanceof Boolean) {
                    intent.putExtra(str3, ((Boolean) obj).booleanValue());
                }
            }
        }
        context.startActivity(intent);
    }

    public static void openURL(Context context, String str, String str2) throws Exception {
        Intent parseUri = Intent.parseUri(str, 0);
        if (!PdrUtil.isEmpty(str2)) {
            parseUri.setPackage(str2);
        }
        parseUri.setSelector((Intent) null);
        parseUri.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(parseUri);
    }

    public static PackageInfo parseApkInfo(Context context, String str) throws Exception {
        return context.getPackageManager().getPackageArchiveInfo(str, PackageManager.GET_ACTIVITIES);
    }

    public static boolean setPackageName(Context context, Intent intent, String str) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage == null) {
            return false;
        }
        intent.setClassName(str, launchIntentForPackage.getComponent().getClassName());
        return true;
    }

    /**
     * Читання файла з assets або файлової системи.
     *
     * @param path Шлях до файлу: напр. "io/dcloud/all.js"
     * @param mode 1 = assets, інше = абсолютний шлях
     * @return вміст файлу у вигляді byte[]
     */
    public static byte[] getFileContent(String path, int mode) {
        InputStream is = null;
        try {
            if (mode == 1) {
                // mode 1 → читаємо з assets
                Context context = DCloudApplication.getInstance();
                is = context.getAssets().open(path);
            } else {
                // інші режими → читаємо з файлової системи
                File file = new File(path);
                if (!file.exists()) return null;
                is = new FileInputStream(file);
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] tmp = new byte[4096];
            int len;
            while ((len = is.read(tmp)) != -1) {
                buffer.write(tmp, 0, len);
            }
            return buffer.toByteArray();

        } catch (IOException e) {
            Log.e("PlatformUtil", "getFileContent failed: " + path, e);
            return null;
        } finally {
            if (is != null) {
                try { is.close(); } catch (IOException ignored) {}
            }
        }
    }

    public static InputStream getInputStream(String str, int i) {
        InputStream resourceAsStream;
        InputStream inputStream = null;
        if (DeviceInfo.sDeviceRootDir != null && str.startsWith(DeviceInfo.sDeviceRootDir)) {
            i = 2;
        }
        if (i == 0) {
            resourceAsStream = getResInputStream(str);
        } else if (i == 1) {
            resourceAsStream = PlatformUtil.class.getClassLoader().getResourceAsStream(str);
        } else {
            if (i == 2) {
                try {
                    inputStream = new FileInputStream(str);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return inputStream;
        }
        inputStream = resourceAsStream;
        return inputStream;
    }

    public static InputStream getInputStream(String str) {
        if (str == null) {
            return null;
        }
        return getInputStream(str, (DeviceInfo.sDeviceRootDir == null || !str.startsWith(DeviceInfo.sDeviceRootDir)) ? 0 : 2);
    }

    public static Object invokeMethod(String str, String str2) {
        return invokeMethod(str, str2, null, new Class[0], new Object[0]);
    }

    public static Object invokeMethod(String str, String str2, Object obj) {
        return invokeMethod(str, str2, obj, new Class[0], new Object[0]);
    }

    public static Object invokeFieldValue(String str, String str2, Object obj) {
        Class<?> cls = null;
        Field field;
        if (obj != null) {
            try {
                cls = obj.getClass();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            cls = null;
        }
        if (cls == null) {
            try {
                cls = Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            if (cls != null && (field = cls.getField(str2)) != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static Object invokeMethod(String className, String methodName, Object instance, Class[] paramTypes, Object[] params) {
        Object result = null;
        String error = null;

        try {
            // Отримуємо клас за іменем
            Class<?> clazz = Class.forName(className);

            // Отримуємо метод за назвою і параметрами
            java.lang.reflect.Method method = clazz.getMethod(methodName, paramTypes);

            if (method != null) {
                // Дозволяємо виклик приватних/захищених методів
                method.setAccessible(true);

                // Викликаємо метод на екземплярі з параметрами
                result = method.invoke(instance, params);
            }
        } catch (NoSuchMethodException e) {
            error = "NoSuchMethodException";
        } catch (ClassNotFoundException e) {
            error = "ClassNotFoundException";
        } catch (Exception e) {
            error = "Exception";
        }

        // Якщо сталася помилка і метод не "getJsContent" — логгируем її
        if (error != null && !"getJsContent".equals(methodName)) {
            String logMsg = error + " " + className + " " + methodName;
            io.dcloud.common.adapter.util.Logger.e("platform", logMsg);
        }

        return result;
        /*
            r0 = 0
            java.lang.Class r1 = java.lang.Class.forName(r3)     // Catch: java.lang.Exception -> L16 java.lang.NoSuchMethodException -> L19 java.lang.ClassNotFoundException -> L1c
            java.lang.reflect.Method r6 = r1.getMethod(r4, r6)     // Catch: java.lang.Exception -> L16 java.lang.NoSuchMethodException -> L19 java.lang.ClassNotFoundException -> L1c
            if (r6 == 0) goto L14
            r1 = 1
            r6.setAccessible(r1)     // Catch: java.lang.Exception -> L16 java.lang.NoSuchMethodException -> L19 java.lang.ClassNotFoundException -> L1c
            java.lang.Object r5 = r6.invoke(r5, r7)     // Catch: java.lang.Exception -> L16 java.lang.NoSuchMethodException -> L19 java.lang.ClassNotFoundException -> L1c
            goto L21
        L14:
            r5 = r0
            goto L21
        L16:
            java.lang.String r5 = "Exception"
            goto L1e
        L19:
            java.lang.String r5 = "NoSuchMethodException"
            goto L1e
        L1c:
            java.lang.String r5 = "ClassNotFoundException"
        L1e:
            r2 = r0
            r0 = r5
            r5 = r2
        L21:
            if (r0 == 0) goto L4f
            java.lang.String r6 = "getJsContent"
            boolean r6 = r6.equals(r4)
            if (r6 != 0) goto L4f
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r7 = " "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r3 = r6.append(r3)
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "platform"
            io.dcloud.common.adapter.util.Logger.e(r4, r3)
        L4f:
            return r5
        */
//        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(java.lang.String, java.lang.String, java.lang.Object, java.lang.Class[], java.lang.Object[]):java.lang.Object");
    }

    public static boolean isEmulator() {
        return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
    }

    public static boolean hasAppInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void createShortut(Context context, String str, String str2, int i, boolean z) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setClassName(context, str2);
        Intent intent2 = new Intent(ACTION_INSTALL_SHORTCUT);
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent2.putExtra("android.intent.extra.shortcut.NAME", str);
        intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(context, i));
        intent2.putExtra(EXTRA_SHORTCUT_DUPLICATE, z);
        context.sendBroadcast(intent2);
    }

    public static void createShortut(Context context, String str, String str2, Bitmap bitmap, boolean z) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setClassName(context, str2);
        Intent intent2 = new Intent(ACTION_INSTALL_SHORTCUT);
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent2.putExtra("android.intent.extra.shortcut.NAME", str);
        intent2.putExtra("android.intent.extra.shortcut.ICON", bitmap);
        intent2.putExtra(EXTRA_SHORTCUT_DUPLICATE, z);
        context.sendBroadcast(intent2);
    }

    public void delShortcut(String str, String str2, String str3) {
        Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        intent.putExtra("android.intent.extra.shortcut.NAME", str);
        Intent intent2 = new Intent("android.intent.action.MAIN");
        intent2.addCategory("android.intent.category.DEFAULT");
        intent2.setComponent(new ComponentName(str2, str3));
        intent.putExtra("android.intent.extra.shortcut.INTENT", intent2);
        DeviceInfo.sApplicationContext.sendBroadcast(intent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0032, code lost:
    
        r0.close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean queryHTCShortCut(String r9) {
        /*
            r0 = 0
            r1 = 0
            android.content.Context r2 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            java.lang.String r2 = "content://com.htc.launcher.settings/favorites?notify=true"
            android.net.Uri r4 = android.net.Uri.parse(r2)     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            java.lang.String[] r5 = io.dcloud.common.adapter.util.PlatformUtil.PROJECTION     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            java.lang.String r6 = "title=?"
            r2 = 1
            java.lang.String[] r7 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            r7[r1] = r9     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            r8 = r0
            java.lang.String r8 = (java.lang.String) r8     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            android.database.Cursor r0 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            if (r0 == 0) goto L30
            boolean r9 = r0.moveToFirst()     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            if (r9 != 0) goto L27
            goto L30
        L27:
            r0.close()     // Catch: java.lang.Throwable -> L36 java.lang.Exception -> L38
            if (r0 == 0) goto L2f
            r0.close()
        L2f:
            return r2
        L30:
            if (r0 == 0) goto L35
            r0.close()
        L35:
            return r1
        L36:
            r9 = move-exception
            goto L5b
        L38:
            r9 = move-exception
            java.lang.String r2 = "PlatformUtil"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L36
            r3.<init>()     // Catch: java.lang.Throwable -> L36
            java.lang.String r4 = "queryHTCShortCut error:"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L36
            java.lang.String r9 = r9.getMessage()     // Catch: java.lang.Throwable -> L36
            java.lang.StringBuilder r9 = r3.append(r9)     // Catch: java.lang.Throwable -> L36
            java.lang.String r9 = r9.toString()     // Catch: java.lang.Throwable -> L36
            io.dcloud.common.adapter.util.Logger.e(r2, r9)     // Catch: java.lang.Throwable -> L36
            if (r0 == 0) goto L5a
            r0.close()
        L5a:
            return r1
        L5b:
            if (r0 == 0) goto L60
            r0.close()
        L60:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.PlatformUtil.queryHTCShortCut(java.lang.String):boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002f, code lost:
    
        if (r1 != null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0056, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0053, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0051, code lost:
    
        if (r1 == null) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean queryDefaultShortcut(String r9) {
        /*
            r0 = 0
            r1 = 0
            android.content.Context r2 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            java.lang.String r2 = "content://com.android.launcher2.settings/favorites?notify=false"
            android.net.Uri r4 = android.net.Uri.parse(r2)     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            java.lang.String[] r5 = io.dcloud.common.adapter.util.PlatformUtil.PROJECTION     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            java.lang.String r6 = "title=?"
            r2 = 1
            java.lang.String[] r7 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            r7[r0] = r9     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            r8 = r1
            java.lang.String r8 = (java.lang.String) r8     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            android.database.Cursor r1 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            if (r1 == 0) goto L2f
            boolean r9 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            if (r9 == 0) goto L2f
            r1.close()     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            if (r1 == 0) goto L2e
            r1.close()
        L2e:
            return r2
        L2f:
            if (r1 == 0) goto L56
            goto L53
        L32:
            r9 = move-exception
            goto L57
        L34:
            r9 = move-exception
            java.lang.String r2 = "PlatformUtil"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L32
            r3.<init>()     // Catch: java.lang.Throwable -> L32
            java.lang.String r4 = "queryHTCShortCut error:"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L32
            java.lang.String r9 = r9.getMessage()     // Catch: java.lang.Throwable -> L32
            java.lang.StringBuilder r9 = r3.append(r9)     // Catch: java.lang.Throwable -> L32
            java.lang.String r9 = r9.toString()     // Catch: java.lang.Throwable -> L32
            io.dcloud.common.adapter.util.Logger.e(r2, r9)     // Catch: java.lang.Throwable -> L32
            if (r1 == 0) goto L56
        L53:
            r1.close()
        L56:
            return r0
        L57:
            if (r1 == 0) goto L5c
            r1.close()
        L5c:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.PlatformUtil.queryDefaultShortcut(java.lang.String):boolean");
    }

    private static int queryMaxLauncherScreenCount(Context context) {
        String str = (String) null;
        Cursor query = context.getContentResolver().query(Uri.parse(URI_SAMSUNG_LAUNCER), new String[]{"MAX(screen)"}, str, (String[]) null, str);
        if (query != null) {
            try {
                query.moveToNext();
                return query.getInt(0) + 1;
            } catch (Exception e) {
                Logger.e("PlatformUtil", "Samsung Launcher" + e);
            } finally {
                query.close();
            }
        }
        return -1;
    }

    public static boolean checkLauncherScreenSpace(Context context) {
        String str = (String) null;
        Cursor query = context.getContentResolver().query(Uri.parse(URI_SAMSUNG_LAUNCER), new String[]{"screen", "spanX", "spanY"}, str, (String[]) null, str);
        if (query == null) {
            return true;
        }
        int queryMaxLauncherScreenCount = queryMaxLauncherScreenCount(context);
        Logger.e("PlatformUtil", "Samsung Launcher: max screen num = " + queryMaxLauncherScreenCount);
        int columnIndexOrThrow = query.getColumnIndexOrThrow("spanX");
        int columnIndexOrThrow2 = query.getColumnIndexOrThrow("spanY");
        int i = queryMaxLauncherScreenCount * MAX_SPAN_IN_ONE_SCREEN;
        while (query.moveToNext()) {
            try {
                try {
                    i -= query.getInt(columnIndexOrThrow) * query.getInt(columnIndexOrThrow2);
                } catch (Exception e) {
                    Logger.e("PlatformUtil", "Check Launcher space" + e);
                    query.close();
                    i = 0;
                }
            } finally {
                query.close();
            }
        }
        return i > 0;
    }

    public static String getFileContent4S(String str) {
        return new String(getFileContent(str, 0)).replace("p", "");
    }

    public static boolean checkGTAndYoumeng() {
        return checkClass("io.dcloud.feature.apsGt.GTPushService") || checkClass("io.dcloud.feature.statistics.UmengStatisticsMgr");
    }

    public static boolean checkClass(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
