package io.dcloud;

import android.content.Context;
import android.text.TextUtils;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.BaseInfo;
import io.src.dcloud.adapter.DCloudAdapterUtil;

/* loaded from: classes.dex */
public class PdrR implements IReflectAble {
    private static String a;
    public static int DRAWABLE_SPLASH = RInformation.getInt("drawable", AbsoluteConst.STREAMAPP_KEY_SPLASH);
    public static int DRAWABLE_ICON = AndroidResources.mApplicationInfo.applicationInfo.icon;
    public static int LAYOUT_SNOW_WHITE_PROGRESS = RInformation.getInt("layout", "dcloud_snow_white_progress");
    public static int LAYOUT_SNOW_BLACK_PROGRESS = RInformation.getInt("layout", "dcloud_snow_black_progress");
    public static int ID_PROGRESSBAR = RInformation.getInt("id", "progressBar");
    public static int FEATURE_LOSS_STYLE = RInformation.getInt("style", "featureLossDialog");
//    public static int ID_ICON_SPLASH = RInformation.getInt("id", "iv_icon_splash_dcloud");
//    public static int ID_TEXT_COPYRIGHT_SPLASH = RInformation.getInt("id", "tv_copyright_splash_dcloud");
//    public static int ID_TEXT_LOADING_SPLASH = RInformation.getInt("id", "tv_loading_splash_dcloud");
//    public static int ID_TEXT_NAME_SPLASH = RInformation.getInt("id", "tv_name_splash_dcloud");
    public static int LAYOUT_CUSTION_NOTIFICATION_DCLOUD = RInformation.getInt("layout", "dcloud_custom_notification");
    public static int ID_IMAGE_NOTIFICATION_DCLOUD = RInformation.getInt("id", "image");
    public static int ID_TITLE_NOTIFICATION_DCLOUD = RInformation.getInt("id", AbsoluteConst.JSON_KEY_TITLE);
    public static int ID_TEXT_NOTIFICATION = RInformation.getInt("id", "text");
    public static int ID_TIME_NOTIFICATION_DCLOUD = RInformation.getInt("id", "time");
    public static int DRAWABLE_DCLOUD_DIALOG_SHAPE = RInformation.getInt("drawable", "dcloud_dialog_shape");
    public static int LAYOUT_DIALOG_LAYOUT_DCLOUD_DIALOG = RInformation.getInt("layout", "dcloud_dialog");
    public static int ID_DCLOUD_DIALOG_ROOTVIEW = RInformation.getInt("id", "dcloud_dialog_rootview");
    public static int ID_DCLOUD_DIALOG_TITLE = RInformation.getInt("id", "dcloud_dialog_title");
    public static int ID_DCLOUD_DIALOG_ICON = RInformation.getInt("id", "dcloud_dialog_icon");
    public static int ID_DCLOUD_DIALOG_MSG = RInformation.getInt("id", "dcloud_dialog_msg");
    public static int ID_DCLOUD_DIALOG_BTN1 = RInformation.getInt("id", "dcloud_dialog_btn1");
    public static int ID_DCLOUD_DIALOG_BTN2 = RInformation.getInt("id", "dcloud_dialog_btn2");
    public static int STYLE_DIALOG_DCLOUD_DEFALUT_DIALOG = RInformation.getInt("style", "dcloud_defalut_dialog");
    public static int STYLE_DIALOG_STYLE_DCLOUD_ANIM_DIALOG_WINDOW_IN_OUT = RInformation.getInt("style", "dcloud_anim_dialog_window_in_out");
    public static int ANIM_DIALOG_ANIM_DCLOUD_SLIDE_IN_FROM_TOP = RInformation.getInt("anim", "dcloud_slide_in_from_top");
    public static int ANIM_DIALOG_ANIM_DCLOUD_SLIDE_OUT_TO_TOP = RInformation.getInt("anim", "dcloud_slide_out_to_top");
    public static int STREAMAPP_DELETE_THEME = RInformation.getInt("style", "streamDelete19Dialog");
    public static int STREAMAPP_DRAWABLE_APPDEFULTICON = RInformation.getInt("drawable", "dcloud_streamapp_icon_appdefault");
    public static int DRAWBLE_PROGRESSBAR_BLACK_DCLOUD = RInformation.getInt("drawable", "dcloud_snow_black_progress");
    public static int DRAWBLE_PROGRESSBAR_WHITE_DCLOUD = RInformation.getInt("drawable", "dcloud_snow_white_progress");
    public static int DRAWEBL_SHADOW_LEFT = RInformation.getInt("drawable", "dcloud_shadow_left");

    public static void init(Context context) {
        if (context != null) {
            a = context.getPackageName();
        }
    }

    public static void checkInit() {
        if (TextUtils.isEmpty(a)) {
            if (DCloudAdapterUtil.isPlugin() && !TextUtils.isEmpty(DCloudAdapterUtil.getPageName())) {
                a = DCloudAdapterUtil.getPageName();
            } else if (DeviceInfo.sApplicationContext != null) {
                init(DeviceInfo.sApplicationContext);
            } else if (DCloudApplication.getInstance() != null) {
                init(DCloudApplication.getInstance());
            }
        }
    }

    public static int getInt(String str, String str2) {
        try {
            checkInit();
            return Class.forName(a + ".R$" + str).getField(str2).getInt(null);
        } catch (NoSuchFieldException e) {
            Logger.e("Not found " + a + ".R$" + str + "." + str2);
            if (!BaseInfo.ISDEBUG) {
                return 0;
            }
            e.printStackTrace();
            return 0;
        } catch (Exception e2) {
            Logger.e("StreamSDK", "come into exception e.getMessage()=====" + e2.getMessage() + "    type=====" + str + "    name=====" + str2 + "sPackageName==" + a);
            if (!BaseInfo.ISDEBUG) {
                return 0;
            }
            e2.printStackTrace();
            return 0;
        }
    }

    public static int[] getIntArray(String str, String str2) {
        try {
            checkInit();
            return (int[]) Class.forName(a + ".R$" + str).getField(str2).get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Logger.e("Not found " + a + ".R." + str + "." + str2);
            return null;
        } catch (Exception e2) {
            Logger.e("Not init RInfomation sPackageName=" + a);
            e2.printStackTrace();
            return null;
        }
    }
}
