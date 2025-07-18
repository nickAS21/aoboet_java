package io.dcloud.common.DHInterface;

/* loaded from: classes.dex */
public interface IMgr {

    /* loaded from: classes.dex */
    public interface AppEvent {
        public static final int APP_GET_APP_BY_APPID = 6;
        public static final int APP_GET_LAST_USE = 19;
        public static final int APP_GET_PROPERTY = 5;
        public static final int APP_REMOVE_STREAM_APP = 17;
        public static final int CHECK_AUTHORIZED = 15;
        public static final int CREATE_UNSTRICT_APP = 8;
        public static final int CREATE_WEBAPP = 14;
        public static final int DO_AUTHORIZE = 16;
        public static final int EXECUTE_SYS_EVENT = 1;
        public static final int GET_CUR_RUNNING_APPID = 11;
        public static final int GET_STATUS_BY_APPID = 12;
        public static final int INSTALL_APP = 4;
        public static final int MINISERVER_GET_RES_IN_STREAM = 2;
        public static final int OBTAIN_APP_LAUNCH_PAGE = 9;
        public static final int ON_STOP_APP = 13;
        public static final int RE_START_APP = 3;
        public static final int SOCKET_NATIVE_COMMAND = 7;
        public static final int START_APP = 0;
        public static final int STOP_APP = 10;
        public static final int STREAM_START_APP = 20;
        public static final int TAB_CHANGE_APP = 21;
        public static final int W2A_RESOURCE_DOWNLOAD = 18;
    }

    /* loaded from: classes.dex */
    public interface FeatureEvent {
        public static final int CALL_WAITER_DO_SOMETHING = 10;
        public static final int CHECK_FEATURE_PERMISSION = 8;
        public static final int CLEAR_DATA_BY_APPID = 3;
        public static final int EXECUTE_JAVASCRIPT = 1;
        public static final int HAS_FEATURE_PLUGIN = 9;
        public static final int LOAD_PROPERTIES = 0;
        public static final int OBTAIN_FEATURE_EXT_HASHMAP = 4;
        public static final int OBTAIN_FEATURE_JS = 2;
        public static final int REGISTER_JS_API = 5;
        public static final int REQUEST_ALL_FEATURE = 7;
        public static final int REQUEST_FEATURE = 6;
    }

    /* loaded from: classes.dex */
    public interface MgrEvent extends AppEvent, FeatureEvent, NetEvent, WindowEvent {
    }

    /* loaded from: classes.dex */
    public enum MgrType {
        AppMgr,
        NetMgr,
        FeatureMgr,
        WindowMgr
    }

    /* loaded from: classes.dex */
    public interface NetEvent {
    }

    /* loaded from: classes.dex */
    public interface WindowEvent {
        public static final int ACTIVE_APP_ROOT_VIEW = 5;
        public static final int ACTIVE_APP_ROOT_VIEW_BY_WAP_PAGE = 41;
        public static final int ATTACH_WEBVIEW = 16;
        public static final int AUTO_POP = 27;
        public static final int AUTO_PUSH = 28;
        public static final int CLEAR_DATA_FINISH_ACTIVITY = 19;
        public static final int CLOSE_APP_WAP_PAGE = 42;
        public static final int CLOSE_SPLASH_SCREEN = 11;
        public static final int CLOSE_WINDOW = 2;
        public static final int CREATE_APP_ROOT_VIEW = 4;
        public static final int CREATE_WIDNOW = 3;
        public static final int DEBUG = -1;
        public static final int DESTROY_APP_ROOT_VIEW = 25;
        public static final int GET_APP_FIRST_VIEW = 9;
        public static final int GET_APP_FRAMEVIEW_STACK = 6;
        public static final int GET_CUR_FRAMEVIEW = 18;
        public static final int HAS_OTHER_ACTIVE_APP = 32;
        public static final int HIDE_MASK_VIEW = 30;
        public static final int HIDE_SHOW_WINDOW = 24;
        public static final int HIDE_WINDOW = 23;
        public static final int OBTAIN_APP_TOP_PAGE = 44;
        public static final int OBTAIN_APP_WAP_PAGE = 43;
        public static final int OBTAIN_LAUNCH_PAGE = 17;
        public static final int POP_FRAME_VIEW = 21;
        public static final int RELOAD_ALL = 13;
        public static final int RELOAD_CURRENT = 12;
        public static final int RELOAD_DIRECT_FRAME_VIEW = 14;
        public static final int RESTART_APP_ROOT_VIEW = 10;
        public static final int SET_PARENT = 8;
        public static final int SET_PRELOAD_PARENT = 31;
        public static final int SET_UN_PARENT = 22;
        public static final int SHOW_BEHIND = 45;
        public static final int SHOW_LOADING = 0;
        public static final int SHOW_MASK_VIEW = 29;
        public static final int SHOW_WIDNOW = 1;
        public static final int SORT_FRAMEVIEW_BY_ZINDEX = 26;
        public static final int TRAFFIC_FREE_CHANGE = 33;
        public static final int WEBAPP_QUIT = 20;
        public static final int WINDOW_REMOVE_STREAM_APP = 40;
        public static final int WIN_SET_OPTION = 7;
    }

    Object processEvent(MgrType mgrType, int i, Object obj);
}
