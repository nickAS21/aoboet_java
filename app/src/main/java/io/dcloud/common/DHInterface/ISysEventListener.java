package io.dcloud.common.DHInterface;

/* loaded from: classes.dex */
public interface ISysEventListener {

    /* loaded from: classes.dex */
    public enum SysEventType {
        AllSystemEvent,
        onActivityResult,
        onCreateOptionMenu,
        onKeyDown,
        onKeyUp,
        onSaveInstanceState,
        onKeyLongPress,
        onStart,
        onResume,
        onWebAppStop,
        onWebAppSaveState,
        onWebAppTrimMemory,
        onWebAppBackground,
        onWebAppPause,
        onWebAppForeground,
        onStop,
        onPause,
        onDeviceNetChanged,
        onSimStateChanged,
        onNewIntent,
        onConfigurationChanged,
        onKeyboardShow,
        onKeyboardHide,
        onRequestPermissionsResult
    }

    boolean onExecute(SysEventType sysEventType, Object obj);
}
