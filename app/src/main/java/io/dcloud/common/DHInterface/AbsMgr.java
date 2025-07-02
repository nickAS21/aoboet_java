package io.dcloud.common.DHInterface;

import android.content.Context;

/* loaded from: classes.dex */
public abstract class AbsMgr implements IMgr {
    protected Context mContextWrapper;
    protected ICore mCore;
    protected boolean mIsStreamAppMode;
    protected String mMgrName;
    protected IMgr.MgrType mMgrType;

    public void dispose() {
    }

    public void onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbsMgr(ICore iCore, String str, IMgr.MgrType mgrType) {
        this.mContextWrapper = null;
        this.mCore = null;
        this.mMgrName = null;
        this.mIsStreamAppMode = false;
        this.mCore = iCore;
        this.mIsStreamAppMode = iCore.isStreamAppMode();
        this.mMgrName = str;
        this.mMgrType = mgrType;
        this.mContextWrapper = iCore.obtainContext();
    }

    public final boolean checkMgrId(IMgr.MgrType mgrType) {
        return this.mMgrType == mgrType;
    }

    public final Context getContext() {
        return this.mContextWrapper;
    }

    public final boolean isStreamAppMode() {
        return this.mIsStreamAppMode;
    }
}
