package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.content.Context;

/* loaded from: classes.dex */
public interface ICore {

    /* loaded from: classes.dex */
    public interface ICoreEvent {
        public static final int CHECK_IS_IBOOT_SERVICES = 1;
        public static final int GET_SDK_MODE = -1;
        public static final int WEBAPP_QUIT = 0;
        public static final int WEBAPP_START = 2;
    }

    /* loaded from: classes.dex */
    public interface ICoreStatusListener {
        void onCoreInitEnd(ICore iCore);

        void onCoreReady(ICore iCore);

        boolean onCoreStop();
    }

    Object dispatchEvent(IMgr.MgrType mgrType, int i, Object obj);

    boolean isStreamAppMode();

    Context obtainContext();

    boolean onActivityExecute(Activity activity, ISysEventListener.SysEventType sysEventType, Object obj);

    void removeStreamApp(String str);

    void setIsStreamAppMode(boolean z);
}
