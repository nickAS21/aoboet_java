package io.dcloud.feature.ui.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ShortCutUtil;
import io.dcloud.common.util.ShortcutCreateUtil;

/* loaded from: classes.dex */
public class NavigatorUIFeatureImpl implements IFeature {
    private static final String b = "NavigatorUIFeatureImpl";
    AbsMgr a;

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0207 A[Catch: Exception -> 0x028f, TRY_LEAVE, TryCatch #0 {Exception -> 0x028f, blocks: (B:63:0x0201, B:65:0x0207), top: B:62:0x0201 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x022a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0218 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0215  */
    @Override // io.dcloud.common.DHInterface.IFeature
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String execute(final io.dcloud.common.DHInterface.IWebview r22, java.lang.String r23, java.lang.String[] r24) {
        /*
            Method dump skipped, instructions count: 1213
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.navigator.NavigatorUIFeatureImpl.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    private void a(Context context, IWebview iWebview, String str, String str2) {
        String format;
        String requestShortCut = ShortCutUtil.requestShortCut(context, str2);
        if (ShortCutUtil.SHORT_CUT_EXISTING.equals(requestShortCut)) {
            format = String.format(DOMException.JSON_SHORTCUT_RESULT_INFO, "existing");
        } else if (ShortCutUtil.SHORT_CUT_NONE.equals(requestShortCut)) {
            format = String.format(DOMException.JSON_SHORTCUT_RESULT_INFO, "none");
        } else if (ShortCutUtil.NOPERMISSIONS.equals(requestShortCut)) {
            format = String.format(DOMException.JSON_SHORTCUT_RESULT_INFO, ShortCutUtil.NOPERMISSIONS);
        } else {
            format = String.format(DOMException.JSON_SHORTCUT_RESULT_INFO, "unknown");
        }
        try {
            JSUtil.execCallback(iWebview, str, new JSONObject(format), JSUtil.OK, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void b(final Context context, final IWebview iWebview, final String str, final String str2) {
        MessageHandler.postDelayed(new Runnable() { // from class: io.dcloud.feature.ui.navigator.NavigatorUIFeatureImpl.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    JSUtil.execCallback(iWebview, str, new JSONObject(String.format(DOMException.JSON_SHORTCUT_SUCCESS_INFO, ShortCutUtil.SHORT_CUT_EXISTING.equals(ShortCutUtil.requestShortCutForCommit(context, str2)) ? AbsoluteConst.TRUE : AbsoluteConst.FALSE)), JSUtil.OK, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 500L);
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.a = absMgr;
    }

    private Bitmap a(IApp iApp) {
        String b2 = b(iApp);
        if (b2 != null) {
            return BitmapFactory.decodeFile(b2);
        }
        return null;
    }

    private String b(IApp iApp) {
        Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
        return obtainWebAppIntent != null ? obtainWebAppIntent.getStringExtra(IntentConst.WEBAPP_ACTIVITY_APPICON) : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(IWebview iWebview, String str, Bitmap bitmap, String str2, String str3, JSONObject jSONObject, boolean z, boolean z2, String str4) {
        Intent obtainWebAppIntent;
        IApp obtainApp = iWebview.obtainApp();
        String obtainAppId = obtainApp.obtainAppId();
        Activity activity = iWebview.getActivity();
        SharedPreferences orCreateBundle = PlatformUtil.getOrCreateBundle("pdr");
        String obtainAppName = PdrUtil.isEmpty(str) ? obtainApp.obtainAppName() : str;
        boolean z3 = orCreateBundle.getBoolean(obtainAppId + SP.K_CREATED_SHORTCUT, false);
        String stringExtra = (!TextUtils.isEmpty(str2) || (obtainWebAppIntent = iWebview.obtainApp().obtainWebAppIntent()) == null) ? str2 : obtainWebAppIntent.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME);
        if (ShortcutCreateUtil.isDuplicateLauncher(activity)) {
            if (ShortCutUtil.createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap, stringExtra, jSONObject, true)) {
                if (!TextUtils.isEmpty(str3) && ShortcutCreateUtil.needToast(activity)) {
                    Toast.makeText(activity, str3, Toast.LENGTH_LONG).show();
                }
                ShortCutUtil.commitShortcut(obtainApp, 11, 1);
            }
        } else if (!ShortCutUtil.hasShortcut(activity, obtainAppName)) {
            if (z) {
                if (!TextUtils.isEmpty(str3)) {
                    Toast.makeText(activity, str3, Toast.LENGTH_LONG).show();
                }
                ShortCutUtil.createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap, stringExtra, jSONObject, true);
                ShortCutUtil.commitShortcut(obtainApp, 11, 1);
            } else {
                if (z3) {
                    return;
                }
                if (ShortCutUtil.createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap, stringExtra, jSONObject, true)) {
                    if (!TextUtils.isEmpty(str3) && ShortcutCreateUtil.needToast(activity)) {
                        Toast.makeText(activity, str3, Toast.LENGTH_LONG).show();
                    }
                    ShortCutUtil.commitShortcut(obtainApp, 11, 1);
                }
            }
        }
        b(iWebview.getContext(), iWebview, str4, obtainAppName);
    }

    private static String a(int i) {
        StringBuilder sb = new StringBuilder();
        String hexString = Integer.toHexString(Color.red(i));
        String hexString2 = Integer.toHexString(Color.green(i));
        String hexString3 = Integer.toHexString(Color.blue(i));
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        if (hexString2.length() == 1) {
            hexString2 = "0" + hexString2;
        }
        if (hexString3.length() == 1) {
            hexString3 = "0" + hexString3;
        }
        String upperCase = hexString.toUpperCase();
        String upperCase2 = hexString2.toUpperCase();
        String upperCase3 = hexString3.toUpperCase();
        sb.append("#");
        sb.append(upperCase);
        sb.append(upperCase2);
        sb.append(upperCase3);
        return sb.toString();
    }
}
