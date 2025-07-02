package io.dcloud.common.adapter.util;

import android.content.SharedPreferences;

import java.util.HashMap;

/* loaded from: classes.dex */
public class SP {
    public static final String CHECK_PATH_STREAMAPP = "check_path_streamapp";
    public static final String IS_CREATE_SHORTCUT = "_is_create_shortcut";
    public static final String K_COLLECTED = "_dpush_collected_";
    public static final String K_CREATED_SHORTCUT = "_created_shortcut";
    public static final String K_CREATE_SHORTCUT_NAME = "_create_shortcut_name";
    public static final String K_DEVICE_DPUSH_UUID = "_dpush_uuid_";
    public static final String K_LAST_POS = "_dpush_last_pos_";
    public static final String K_SHORT_CUT_ONE_TIPS = "short_cut_one_tips";
    public static final String K_SMART_UPDATE_NEED_UPDATE_ICON = "_smart_update_need_update_icon";
    public static final String K_SMART_UPDATE_PACKAGE_DOWNLOAD_SUCCESS = "_smart_update_packge_success";
    public static final String K_SMART_UPDATE_PARAMS = "_smart_update_need_update";
    public static final String K_STORAGES_SHORTCUT = "SHORTCUT";
    public static final String NEED_UPDATE_ICON = "_smart_update_need_update_icon";
    public static final String N_BASE = "pdr";
    private static final String N_SMART_UPDATE = "_smart_update";
    public static final String N_STORAGES = "_storages";
    public static final String RECORD_RUN_SHORT_CUT = "record_run_short_cut";
    public static final String REPAIR_FIRST_SHORT_CUT = "repaid_first_short_cut";
    public static final String SMART_UPDATE = "pdr";
    public static final String STAREMAPP_FIRST_SHORT_CUT = "_staremapp_first_short_cut";
    public static final String STREAM_APP_NOT_FOUND_SPLASH_AT_SERVER = "_no_splash_at_server";
    public static final String UPDATE_PACKAGE_DOWNLOAD_SUCCESS = "_smart_update_packge_success";
    public static final String UPDATE_PARAMS = "_smart_update_need_update";
    public static final String UPDATE_SPLASH_AUTOCLOSE = "__update_splash_autoclose";
    public static final String UPDATE_SPLASH_AUTOCLOSE_W2A = "__update_splash_autoclose_w2a";
    public static final String UPDATE_SPLASH_DELAY = "__update_splash_delay";
    public static final String UPDATE_SPLASH_DELAY_W2A = "__update_splash_delay_w2a";
    public static final String UPDATE_SPLASH_IMG_PATH = "update_splash_img_path";
    public static final String WELCOME_SPLASH_SHOW = "__welcome_splash_show";
    private static HashMap<String, SharedPreferences> mBundles;

    public static String getBundleData(String str, String str2) {
        return getOrCreateBundle(str).getString(str2, null);
    }

    public static synchronized SharedPreferences getOrCreateBundle(String str) {
        SharedPreferences sharedPreferences;
        synchronized (SP.class) {
            if (mBundles == null) {
                mBundles = new HashMap<>(1);
            }
            sharedPreferences = mBundles.get(str);
            if (sharedPreferences == null) {
                sharedPreferences = DeviceInfo.sApplicationContext.getSharedPreferences(str, 0);
                mBundles.put(str, sharedPreferences);
            }
        }
        return sharedPreferences;
    }

    public static void setBundleData(SharedPreferences sharedPreferences, String str, String str2) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static void removeBundleData(SharedPreferences sharedPreferences, String str) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(str);
        edit.commit();
    }

    public static void clearBundle(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }

    public static void setBundleData(String str, String str2, String str3) {
        SharedPreferences.Editor edit = getOrCreateBundle(str).edit();
        edit.putString(str2, str3);
        edit.commit();
    }

    public static void removeBundleData(String str, String str2) {
        SharedPreferences.Editor edit = getOrCreateBundle(str).edit();
        edit.remove(str2);
        edit.commit();
    }

    public static void clearBundle(String str) {
        SharedPreferences.Editor edit = getOrCreateBundle(str).edit();
        edit.clear();
        edit.commit();
    }
}
