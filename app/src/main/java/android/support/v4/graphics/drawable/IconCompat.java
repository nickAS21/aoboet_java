package android.support.v4.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Preconditions;
import android.util.Log;

import androidx.versionedparcelable.CustomVersionedParcelable;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public class IconCompat extends CustomVersionedParcelable {
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final String EXTRA_INT1 = "int1";
    private static final String EXTRA_INT2 = "int2";
    private static final String EXTRA_OBJ = "obj";
    private static final String EXTRA_TINT_LIST = "tint_list";
    private static final String EXTRA_TINT_MODE = "tint_mode";
    private static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_UNKNOWN = -1;
    public byte[] mData;
    public int mInt1;
    public int mInt2;
    Object mObj1;
    public Parcelable mParcelable;
    public ColorStateList mTintList = null;
    PorterDuff.Mode mTintMode = DEFAULT_TINT_MODE;
    public String mTintModeStr;
    public int mType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface IconType {
    }

    private static String typeToString(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? "UNKNOWN" : "BITMAP_MASKABLE" : "URI" : "DATA" : "RESOURCE" : "BITMAP";
    }

    public static IconCompat createWithResource(Context context, int i) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        return createWithResource(context.getResources(), context.getPackageName(), i);
    }

    public static IconCompat createWithResource(Resources resources, String str, int i) {
        if (str == null) {
            throw new IllegalArgumentException("Package must not be null.");
        }
        if (i == 0) {
            throw new IllegalArgumentException("Drawable resource ID must not be 0");
        }
        IconCompat iconCompat = new IconCompat(2);
        iconCompat.mInt1 = i;
        if (resources != null) {
            try {
                iconCompat.mObj1 = resources.getResourceName(i);
            } catch (Resources.NotFoundException unused) {
                throw new IllegalArgumentException("Icon resource cannot be found");
            }
        } else {
            iconCompat.mObj1 = str;
        }
        return iconCompat;
    }

    public static IconCompat createWithBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        IconCompat iconCompat = new IconCompat(1);
        iconCompat.mObj1 = bitmap;
        return iconCompat;
    }

    public static IconCompat createWithAdaptiveBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        IconCompat iconCompat = new IconCompat(5);
        iconCompat.mObj1 = bitmap;
        return iconCompat;
    }

    public static IconCompat createWithData(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }
        IconCompat iconCompat = new IconCompat(3);
        iconCompat.mObj1 = bArr;
        iconCompat.mInt1 = i;
        iconCompat.mInt2 = i2;
        return iconCompat;
    }

    public static IconCompat createWithContentUri(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        IconCompat iconCompat = new IconCompat(4);
        iconCompat.mObj1 = str;
        return iconCompat;
    }

    public static IconCompat createWithContentUri(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        return createWithContentUri(uri.toString());
    }

    public IconCompat() {
    }

    private IconCompat(int i) {
        this.mType = i;
    }

    public int getType() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return getType((Icon) this.mObj1);
        }
        return this.mType;
    }

    public String getResPackage() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return getResPackage((Icon) this.mObj1);
        }
        if (this.mType != 2) {
            throw new IllegalStateException("called getResPackage() on " + this);
        }
        return ((String) this.mObj1).split(":", -1)[0];
    }

    public int getResId() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return getResId((Icon) this.mObj1);
        }
        if (this.mType != 2) {
            throw new IllegalStateException("called getResId() on " + this);
        }
        return this.mInt1;
    }

    public Uri getUri() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return getUri((Icon) this.mObj1);
        }
        return Uri.parse((String) this.mObj1);
    }

    public IconCompat setTint(int i) {
        return setTintList(ColorStateList.valueOf(i));
    }

    public IconCompat setTintList(ColorStateList colorStateList) {
        this.mTintList = colorStateList;
        return this;
    }

    public IconCompat setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        return this;
    }

    public Icon toIcon() {
        Icon createWithBitmap;
        int i = this.mType;
        if (i == -1) {
            return (Icon) this.mObj1;
        }
        if (i == 1) {
            createWithBitmap = Icon.createWithBitmap((Bitmap) this.mObj1);
        } else if (i == 2) {
            createWithBitmap = Icon.createWithResource(getResPackage(), this.mInt1);
        } else if (i == 3) {
            createWithBitmap = Icon.createWithData((byte[]) this.mObj1, this.mInt1, this.mInt2);
        } else if (i == 4) {
            createWithBitmap = Icon.createWithContentUri((String) this.mObj1);
        } else if (i == 5) {
            if (Build.VERSION.SDK_INT >= 26) {
                createWithBitmap = Icon.createWithAdaptiveBitmap((Bitmap) this.mObj1);
            } else {
                createWithBitmap = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, false));
            }
        } else {
            throw new IllegalArgumentException("Unknown type");
        }
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            createWithBitmap.setTintList(colorStateList);
        }
        PorterDuff.Mode mode = this.mTintMode;
        if (mode != DEFAULT_TINT_MODE) {
            createWithBitmap.setTintMode(mode);
        }
        return createWithBitmap;
    }

    public void checkResource(Context context) {
        if (this.mType == 2) {
            String str = (String) this.mObj1;
            if (str.contains(":")) {
                String str2 = str.split(":", -1)[1];
                String str3 = str2.split("/", -1)[0];
                String str4 = str2.split("/", -1)[1];
                String str5 = str.split(":", -1)[0];
                int identifier = getResources(context, str5).getIdentifier(str4, str3, str5);
                if (this.mInt1 != identifier) {
                    Log.i(TAG, "Id has changed for " + str5 + "/" + str4);
                    this.mInt1 = identifier;
                }
            }
        }
    }

    public Drawable loadDrawable(Context context) {
        checkResource(context);
        if (Build.VERSION.SDK_INT >= 23) {
            return toIcon().loadDrawable(context);
        }
        Drawable loadDrawableInner = loadDrawableInner(context);
        if (loadDrawableInner != null && (this.mTintList != null || this.mTintMode != DEFAULT_TINT_MODE)) {
            loadDrawableInner.mutate();
            DrawableCompat.setTintList(loadDrawableInner, this.mTintList);
            DrawableCompat.setTintMode(loadDrawableInner, this.mTintMode);
        }
        return loadDrawableInner;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0090  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.graphics.drawable.Drawable loadDrawableInner(android.content.Context r8) {
        /*
            Method dump skipped, instructions count: 250
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.drawable.IconCompat.loadDrawableInner(android.content.Context):android.graphics.drawable.Drawable");
    }

    private static Resources getResources(Context context, String str) {
        if ("android".equals(str)) {
            return Resources.getSystem();
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 8192);
            if (applicationInfo != null) {
                return packageManager.getResourcesForApplication(applicationInfo);
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, String.format("Unable to find pkg=%s for icon", str), e);
            return null;
        }
    }

    public void addToShortcutIntent(Intent intent, Drawable drawable, Context context) {
        Bitmap bitmap;
        checkResource(context);
        int i = this.mType;
        if (i == 1) {
            bitmap = (Bitmap) this.mObj1;
            if (drawable != null) {
                bitmap = bitmap.copy(bitmap.getConfig(), true);
            }
        } else if (i == 2) {
            try {
                Context createPackageContext = context.createPackageContext(getResPackage(), 0);
                if (drawable == null) {
                    intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(createPackageContext, this.mInt1));
                    return;
                }
                Drawable drawable2 = ContextCompat.getDrawable(createPackageContext, this.mInt1);
                if (drawable2.getIntrinsicWidth() > 0 && drawable2.getIntrinsicHeight() > 0) {
                    bitmap = Bitmap.createBitmap(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    drawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    drawable2.draw(new Canvas(bitmap));
                }
                int launcherLargeIconSize = ((ActivityManager) createPackageContext.getSystemService("activity")).getLauncherLargeIconSize();
                bitmap = Bitmap.createBitmap(launcherLargeIconSize, launcherLargeIconSize, Bitmap.Config.ARGB_8888);
                drawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                drawable2.draw(new Canvas(bitmap));
            } catch (PackageManager.NameNotFoundException e) {
                throw new IllegalArgumentException("Can't find package " + this.mObj1, e);
            }
        } else if (i == 5) {
            bitmap = createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, true);
        } else {
            throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
        }
        if (drawable != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            drawable.setBounds(width / 2, height / 2, width, height);
            drawable.draw(new Canvas(bitmap));
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", bitmap);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        int i = this.mType;
        if (i != -1) {
            if (i != 1) {
                if (i != 2) {
                    if (i == 3) {
                        bundle.putByteArray(EXTRA_OBJ, (byte[]) this.mObj1);
                    } else if (i != 4) {
                        if (i != 5) {
                            throw new IllegalArgumentException("Invalid icon");
                        }
                    }
                }
                bundle.putString(EXTRA_OBJ, (String) this.mObj1);
            }
            bundle.putParcelable(EXTRA_OBJ, (Bitmap) this.mObj1);
        } else {
            bundle.putParcelable(EXTRA_OBJ, (Parcelable) this.mObj1);
        }
        bundle.putInt(EXTRA_TYPE, this.mType);
        bundle.putInt(EXTRA_INT1, this.mInt1);
        bundle.putInt(EXTRA_INT2, this.mInt2);
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            bundle.putParcelable(EXTRA_TINT_LIST, colorStateList);
        }
        PorterDuff.Mode mode = this.mTintMode;
        if (mode != DEFAULT_TINT_MODE) {
            bundle.putString(EXTRA_TINT_MODE, mode.name());
        }
        return bundle;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x002c, code lost:
    
        if (r1 != 5) goto L23;
     */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String toString() {
        /*
            r5 = this;
            int r0 = r5.mType
            r1 = -1
            if (r0 != r1) goto Lc
            java.lang.Object r0 = r5.mObj1
            java.lang.String r0 = java.lang.String.valueOf(r0)
            return r0
        Lc:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Icon(typ="
            r0.<init>(r1)
            int r1 = r5.mType
            java.lang.String r1 = typeToString(r1)
            java.lang.StringBuilder r0 = r0.append(r1)
            int r1 = r5.mType
            r2 = 1
            if (r1 == r2) goto L81
            r3 = 2
            if (r1 == r3) goto L56
            r2 = 3
            if (r1 == r2) goto L3b
            r2 = 4
            if (r1 == r2) goto L2f
            r2 = 5
            if (r1 == r2) goto L81
            goto La4
        L2f:
            java.lang.String r1 = " uri="
            java.lang.StringBuilder r1 = r0.append(r1)
            java.lang.Object r2 = r5.mObj1
            r1.append(r2)
            goto La4
        L3b:
            java.lang.String r1 = " len="
            java.lang.StringBuilder r1 = r0.append(r1)
            int r2 = r5.mInt1
            r1.append(r2)
            int r1 = r5.mInt2
            if (r1 == 0) goto La4
            java.lang.String r1 = " off="
            java.lang.StringBuilder r1 = r0.append(r1)
            int r2 = r5.mInt2
            r1.append(r2)
            goto La4
        L56:
            java.lang.String r1 = " pkg="
            java.lang.StringBuilder r1 = r0.append(r1)
            java.lang.String r3 = r5.getResPackage()
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r3 = " id="
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            int r4 = r5.getResId()
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r2[r3] = r4
            java.lang.String r3 = "0x%08x"
            java.lang.String r2 = java.lang.String.format(r3, r2)
            r1.append(r2)
            goto La4
        L81:
            java.lang.String r1 = " size="
            java.lang.StringBuilder r1 = r0.append(r1)
            java.lang.Object r2 = r5.mObj1
            android.graphics.Bitmap r2 = (android.graphics.Bitmap) r2
            int r2 = r2.getWidth()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "x"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.Object r2 = r5.mObj1
            android.graphics.Bitmap r2 = (android.graphics.Bitmap) r2
            int r2 = r2.getHeight()
            r1.append(r2)
        La4:
            android.content.res.ColorStateList r1 = r5.mTintList
            if (r1 == 0) goto Lb2
            java.lang.String r1 = " tint="
            r0.append(r1)
            android.content.res.ColorStateList r1 = r5.mTintList
            r0.append(r1)
        Lb2:
            android.graphics.PorterDuff$Mode r1 = r5.mTintMode
            android.graphics.PorterDuff$Mode r2 = android.support.v4.graphics.drawable.IconCompat.DEFAULT_TINT_MODE
            if (r1 == r2) goto Lc3
            java.lang.String r1 = " mode="
            java.lang.StringBuilder r1 = r0.append(r1)
            android.graphics.PorterDuff$Mode r2 = r5.mTintMode
            r1.append(r2)
        Lc3:
            java.lang.String r1 = ")"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.drawable.IconCompat.toString():java.lang.String");
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void onPreParceling(boolean z) {
        this.mTintModeStr = this.mTintMode.name();
        int i = this.mType;
        if (i == -1) {
            if (z) {
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            }
            this.mParcelable = (Parcelable) this.mObj1;
            return;
        }
        if (i != 1) {
            if (i == 2) {
                this.mData = ((String) this.mObj1).getBytes(Charset.forName("UTF-16"));
                return;
            }
            if (i == 3) {
                this.mData = (byte[]) this.mObj1;
                return;
            } else if (i == 4) {
                this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                return;
            } else if (i != 5) {
                return;
            }
        }
        if (z) {
            Bitmap bitmap = (Bitmap) this.mObj1;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
            this.mData = byteArrayOutputStream.toByteArray();
            return;
        }
        this.mParcelable = (Parcelable) this.mObj1;
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void onPostParceling() {
        this.mTintMode = PorterDuff.Mode.valueOf(this.mTintModeStr);
        int i = this.mType;
        if (i == -1) {
            Parcelable parcelable = this.mParcelable;
            if (parcelable != null) {
                this.mObj1 = parcelable;
                return;
            }
            throw new IllegalArgumentException("Invalid icon");
        }
        if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    this.mObj1 = this.mData;
                    return;
                } else if (i != 4) {
                    if (i != 5) {
                        return;
                    }
                }
            }
            this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
            return;
        }
        Parcelable parcelable2 = this.mParcelable;
        if (parcelable2 != null) {
            this.mObj1 = parcelable2;
            return;
        }
        byte[] bArr = this.mData;
        this.mObj1 = bArr;
        this.mType = 3;
        this.mInt1 = 0;
        this.mInt2 = bArr.length;
    }

    public static IconCompat createFromBundle(Bundle bundle) {
        int i = bundle.getInt(EXTRA_TYPE);
        IconCompat iconCompat = new IconCompat(i);
        iconCompat.mInt1 = bundle.getInt(EXTRA_INT1);
        iconCompat.mInt2 = bundle.getInt(EXTRA_INT2);
        if (bundle.containsKey(EXTRA_TINT_LIST)) {
            iconCompat.mTintList = (ColorStateList) bundle.getParcelable(EXTRA_TINT_LIST);
        }
        if (bundle.containsKey(EXTRA_TINT_MODE)) {
            iconCompat.mTintMode = PorterDuff.Mode.valueOf(bundle.getString(EXTRA_TINT_MODE));
        }
        if (i != -1 && i != 1) {
            if (i != 2) {
                if (i == 3) {
                    iconCompat.mObj1 = bundle.getByteArray(EXTRA_OBJ);
                    return iconCompat;
                }
                if (i != 4) {
                    if (i != 5) {
                        Log.w(TAG, "Unknown type " + i);
                        return null;
                    }
                }
            }
            iconCompat.mObj1 = bundle.getString(EXTRA_OBJ);
            return iconCompat;
        }
        iconCompat.mObj1 = bundle.getParcelable(EXTRA_OBJ);
        return iconCompat;
    }

    public static IconCompat createFromIcon(Context context, Icon icon) {
        Preconditions.checkNotNull(icon);
        int type = getType(icon);
        if (type == 2) {
            String resPackage = getResPackage(icon);
            try {
                return createWithResource(getResources(context, resPackage), resPackage, getResId(icon));
            } catch (Resources.NotFoundException unused) {
                throw new IllegalArgumentException("Icon resource cannot be found");
            }
        }
        if (type == 4) {
            return createWithContentUri(getUri(icon));
        }
        IconCompat iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = icon;
        return iconCompat;
    }

    public static IconCompat createFromIcon(Icon icon) {
        Preconditions.checkNotNull(icon);
        int type = getType(icon);
        if (type == 2) {
            return createWithResource(null, getResPackage(icon), getResId(icon));
        }
        if (type == 4) {
            return createWithContentUri(getUri(icon));
        }
        IconCompat iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = icon;
        return iconCompat;
    }

    private static int getType(Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            return icon.getType();
        }
        try {
            return ((Integer) icon.getClass().getMethod("getType", new Class[0]).invoke(icon, new Object[0])).intValue();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon type " + icon, e);
            return -1;
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "Unable to get icon type " + icon, e2);
            return -1;
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to get icon type " + icon, e3);
            return -1;
        }
    }

    private static String getResPackage(Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            return icon.getResPackage();
        }
        try {
            return (String) icon.getClass().getMethod("getResPackage", new Class[0]).invoke(icon, new Object[0]);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon package", e);
            return null;
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "Unable to get icon package", e2);
            return null;
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to get icon package", e3);
            return null;
        }
    }

    private static int getResId(Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            return icon.getResId();
        }
        try {
            return ((Integer) icon.getClass().getMethod("getResId", new Class[0]).invoke(icon, new Object[0])).intValue();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon resource", e);
            return 0;
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "Unable to get icon resource", e2);
            return 0;
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to get icon resource", e3);
            return 0;
        }
    }

    private static Uri getUri(Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            return icon.getUri();
        }
        try {
            return (Uri) icon.getClass().getMethod("getUri", new Class[0]).invoke(icon, new Object[0]);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon uri", e);
            return null;
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "Unable to get icon uri", e2);
            return null;
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to get icon uri", e3);
            return null;
        }
    }

    static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap bitmap, boolean z) {
        int min = (int) (Math.min(bitmap.getWidth(), bitmap.getHeight()) * DEFAULT_VIEW_PORT_SCALE);
        Bitmap createBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(3);
        float f = min;
        float f2 = 0.5f * f;
        float f3 = ICON_DIAMETER_FACTOR * f2;
        if (z) {
            float f4 = BLUR_FACTOR * f;
            paint.setColor(0);
            paint.setShadowLayer(f4, 0.0f, f * KEY_SHADOW_OFFSET_FACTOR, 1023410176);
            canvas.drawCircle(f2, f2, f3, paint);
            paint.setShadowLayer(f4, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(f2, f2, f3, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate((-(bitmap.getWidth() - min)) / 2, (-(bitmap.getHeight() - min)) / 2);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(f2, f2, f3, paint);
        canvas.setBitmap(null);
        return createBitmap;
    }
}
