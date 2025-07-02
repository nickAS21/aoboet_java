package io.dcloud.common.constant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.StringUtil;

/* loaded from: classes.dex */
public class IntentConst {
    public static final String APPID = "appid";
    public static final String APP_SPLASH_PATH = "app_splash_path";
    public static final String DELETE_PUSH_BY_USER = "__by_user__";
    public static final String EXTRAS = "__extras__";
    public static final String FIRST_WEB_URL = "__first_web_url__";
    public static final String FROM_BARCODE = "from_barcode";
    public static final String FROM_PUSH = "from_push";
    public static final String FROM_SHORT_CUT_STRAT = "from_short_cut_start";
    public static final String FROM_STREAM_OPEN_AUTOCLOSE = "__from_stream_open_autoclose__";
    public static final String FROM_STREAM_OPEN_FLAG = "__from_stream_open_flag__";
    public static final String FROM_STREAM_OPEN_STYLE = "__from_stream_open_style__";
    public static final String FROM_STREAM_OPEN_TIMEOUT = "__from_stream_open_timeout__";
    public static final String INTENT_DATA = "http://update.dcloud.net.cn/apps/";
    public static final String IS_START_FIRST_WEB = "__start_first_web__";
    public static final String IS_STREAM_APP = "is_stream_app";
    public static final String IS_WEBAPP_REPLY = "__webapp_reply__";
    public static final String NAME = "__name__";
    public static final String PENDING_INTENT_MODE = "__pending_intent_mode__";
    public static final String PENDING_INTENT_MODE_ACTIVITY = "__pending_intent_mode_activity__";
    public static final String PENDING_INTENT_MODE_SERVICE = "__pending_intent_mode_service__";
    public static final String PL_AUTO_HIDE = "__plugin_auto_hide__";
    public static final String PL_AUTO_HIDE_SHOW_ACTIVITY = "__plugin_auto_hide_show_activity__";
    public static final String PL_AUTO_HIDE_SHOW_PN = "__plugin_auto_hide_show_pname__";
    public static final String PL_UPDATE = "__plugin_update__";
    public static final String PROCESS_TYPE = "_process_type_";
    public static int PROCESS_TYPE_DEFALUT = 0;
    public static int PROCESS_TYPE_HEAD = 1;
    public static final String PUSH_PAYLOAD = "__payload__";
    public static final String QIHOO_360_BROWSER_URL = "360_browser_url";
    public static final String QIHOO_START_PARAM_FROM = "from";
    public static final String QIHOO_START_PARAM_MODE = "mode";
    public static final String RUNING_STREAPP_LAUNCHER = "plus.runtime.launcher";
    public static final String SHORT_CUT_APPID = "short_cut_appid";
    public static final String SHORT_CUT_MODE = "short_cut_mode";
    public static final String SHORT_CUT_SRC = "shoort_cut_src";
    public static final String SPLASH_VIEW = "__splash_view__";
    public static final String START_FORCE_SHORT = "__sc";
    public static final String START_FROM = "__start_from__";
    public static final int START_FROM_BARCODE = 4;
    public static final int START_FROM_MYAPP = 5;
    public static final int START_FROM_PUSH = 3;
    public static final int START_FROM_SECHEME = 6;
    public static final int START_FROM_SHORT_CUT = 2;
    public static final int START_FROM_STREAM_OPEN = 1;
    public static final int START_FROM_UNKONW = -1;
    public static final String STREAMSDK_ACTIONBAR_PARAM = "__actionbar__";
    public static final String STREAM_LAUNCHER = "__launcher__";
    public static final String TEST_STREAM_APP = "__am";
    private static ArrayList<String> TO_JS_CANT_USE_KEYS = null;
    public static final String WEBAPP_ACTIVITY_APPEXTERN = "app_extern";
    public static final String WEBAPP_ACTIVITY_APPICON = "app_icon";
    public static final String WEBAPP_ACTIVITY_APPNAME = "app_name";
    public static final String WEBAPP_ACTIVITY_HAS_STREAM_SPLASH = "has_stream_splash";
    public static final String WEBAPP_ACTIVITY_HIDE_STREAM_SPLASH = "hide_stream_splash";
    public static final String WEBAPP_ACTIVITY_JUST_DOWNLOAD = "just_download";
    public static final String WEBAPP_ACTIVITY_LAUNCH_PATH = "__launch_path__";
    public static final String WEBAPP_ACTIVITY_SPLASH_MODE = "__splash_mode__";
    public static final String WEBAPP_SHORT_CUT_CLASS_NAME = "short_cut_class_name";

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        TO_JS_CANT_USE_KEYS = arrayList;
        arrayList.add(NAME);
        TO_JS_CANT_USE_KEYS.add(FROM_STREAM_OPEN_AUTOCLOSE);
        TO_JS_CANT_USE_KEYS.add(FROM_STREAM_OPEN_TIMEOUT);
        TO_JS_CANT_USE_KEYS.add(START_FROM);
        TO_JS_CANT_USE_KEYS.add(PL_AUTO_HIDE);
        TO_JS_CANT_USE_KEYS.add(PL_AUTO_HIDE_SHOW_PN);
        TO_JS_CANT_USE_KEYS.add(PL_AUTO_HIDE_SHOW_ACTIVITY);
        TO_JS_CANT_USE_KEYS.add(SPLASH_VIEW);
        TO_JS_CANT_USE_KEYS.add(FROM_STREAM_OPEN_STYLE);
        TO_JS_CANT_USE_KEYS.add(FROM_STREAM_OPEN_FLAG);
        TO_JS_CANT_USE_KEYS.add(STREAM_LAUNCHER);
        TO_JS_CANT_USE_KEYS.add(SHORT_CUT_APPID);
        TO_JS_CANT_USE_KEYS.add("appid");
        TO_JS_CANT_USE_KEYS.add("mode");
        TO_JS_CANT_USE_KEYS.add(SHORT_CUT_MODE);
        TO_JS_CANT_USE_KEYS.add(SHORT_CUT_SRC);
        TO_JS_CANT_USE_KEYS.add(TEST_STREAM_APP);
        TO_JS_CANT_USE_KEYS.add(QIHOO_START_PARAM_FROM);
        TO_JS_CANT_USE_KEYS.add(START_FORCE_SHORT);
        TO_JS_CANT_USE_KEYS.add(APP_SPLASH_PATH);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_HAS_STREAM_SPLASH);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_APPICON);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_APPEXTERN);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_APPNAME);
        TO_JS_CANT_USE_KEYS.add(FROM_SHORT_CUT_STRAT);
        TO_JS_CANT_USE_KEYS.add(FROM_BARCODE);
        TO_JS_CANT_USE_KEYS.add(FROM_PUSH);
        TO_JS_CANT_USE_KEYS.add(DELETE_PUSH_BY_USER);
        TO_JS_CANT_USE_KEYS.add(IS_STREAM_APP);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_SHORT_CUT_CLASS_NAME);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_JUST_DOWNLOAD);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_HIDE_STREAM_SPLASH);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_HAS_STREAM_SPLASH);
        TO_JS_CANT_USE_KEYS.add(FIRST_WEB_URL);
        TO_JS_CANT_USE_KEYS.add(IS_START_FIRST_WEB);
        TO_JS_CANT_USE_KEYS.add(RUNING_STREAPP_LAUNCHER);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_SPLASH_MODE);
        TO_JS_CANT_USE_KEYS.add(WEBAPP_ACTIVITY_LAUNCH_PATH);
        TO_JS_CANT_USE_KEYS.add(PROCESS_TYPE);
        TO_JS_CANT_USE_KEYS.add(IS_WEBAPP_REPLY);
    }

    public static boolean allowToHtml(String str) {
        return (TO_JS_CANT_USE_KEYS.contains(str) || str.startsWith("com.morgoo.droidplugin")) ? false : true;
    }

    public static Intent modifyStartFrom(Intent intent) {
        if (intent != null && intent.getIntExtra(START_FROM, -1) == -1) {
            if (intent.getBooleanExtra(FROM_SHORT_CUT_STRAT, false)) {
                intent.putExtra(START_FROM, 2);
            } else if (intent.getBooleanExtra(FROM_PUSH, false)) {
                intent.putExtra(START_FROM, 3);
            } else if (intent.getBooleanExtra(FROM_BARCODE, false)) {
                intent.putExtra(START_FROM, 4);
            }
        }
        return intent;
    }

    public static Intent removeArgs(Intent intent, String str) {
        Bundle extras;
        Set<String> keySet;
        if (intent == null) {
            return intent;
        }
        Uri data = intent.getData();
        if (data != null && !URLUtil.isNetworkUrl(data.toString())) {
            intent.setData(Uri.parse(data.toString().substring(0, data.toString().indexOf("://") + 3)));
        }
        if (intent.getExtras() != null && (extras = intent.getExtras()) != null && (keySet = extras.keySet()) != null) {
            int size = keySet.size();
            String[] strArr = new String[size];
            keySet.toArray(strArr);
            for (int i = size - 1; i >= 0; i--) {
                String str2 = strArr[i];
                if (allowToHtml(str2)) {
                    extras.remove(str2);
                }
            }
        }
        return intent;
    }

    public static String obtainArgs(Intent intent, String str) {
        Set<String> keySet;
        if (intent == null) {
            return "";
        }
        Uri data = intent.getData();
        if (data != null && !URLUtil.isNetworkUrl(data.toString())) {
            String launchType = BaseInfo.getLaunchType(intent);
            BaseInfo.putLauncherData(str, launchType);
            saveType(str, launchType);
            return intent.hasExtra(EXTRAS) ? intent.getStringExtra(EXTRAS) : data.toString() + "";
        }
        if (intent.getExtras() != null) {
            String launchType2 = BaseInfo.getLaunchType(intent);
            BaseInfo.putLauncherData(str, launchType2);
            saveType(str, launchType2);
            if (intent != null && intent.hasExtra(EXTRAS)) {
                return intent.getStringExtra(EXTRAS);
            }
            JSONObject jSONObject = new JSONObject();
            Bundle extras = intent.getExtras();
            if (extras != null && (keySet = extras.keySet()) != null) {
                int size = keySet.size();
                String[] strArr = new String[size];
                keySet.toArray(strArr);
                for (int i = 0; i < size; i++) {
                    String str2 = strArr[i];
                    if (allowToHtml(str2)) {
                        try {
                            jSONObject.put(str2, extras.get(str2).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (TextUtils.equals(launchType2, "push")) {
                String stringExtra = intent.getStringExtra(PUSH_PAYLOAD);
                String sCString = StringUtil.getSCString(stringExtra, START_FORCE_SHORT);
                if (!TextUtils.isEmpty(sCString)) {
                    intent.putExtra(START_FORCE_SHORT, sCString);
                }
                return TextUtils.isEmpty(stringExtra) ? "" : stringExtra;
            }
            if (jSONObject.length() > 0) {
                return jSONObject.toString();
            }
        }
        return "";
    }

    private static void saveType(String str, String str2) {
        if (TextUtils.isEmpty(SP.getBundleData("pdr", str + AbsoluteConst.LAUNCHTYPE))) {
            SP.setBundleData("pdr", str + AbsoluteConst.LAUNCHTYPE, str2);
        }
    }
}
