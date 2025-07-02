package io.dcloud.common.util.net;

import android.content.IntentFilter;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.net.http.LocalServer;
import io.dcloud.common.util.net.http.MiniServer;

/* loaded from: classes.dex */
public class NetMgr extends AbsMgr implements IMgr.NetEvent {
    DownloadMgr mDownloadMgr;
    MiniServer mLocalServer;
    LocalServer mMiniServer;
    NetCheckReceiver mNetCheckReceiver;
    UploadMgr mUploadMgr;

    public NetMgr(ICore iCore) {
        super(iCore, "netmgr", IMgr.MgrType.NetMgr);
        this.mNetCheckReceiver = null;
        this.mLocalServer = null;
        this.mMiniServer = null;
        startMiniServer();
        this.mUploadMgr = UploadMgr.getUploadMgr();
        this.mDownloadMgr = DownloadMgr.getDownloadMgr();
        this.mNetCheckReceiver = new NetCheckReceiver(this);
        IntentFilter intentFilter = new IntentFilter(NetCheckReceiver.netACTION);
        intentFilter.addAction(NetCheckReceiver.simACTION);
        getContext().registerReceiver(this.mNetCheckReceiver, intentFilter);
    }

    protected void startMiniServer() {
        PdrUtil.isEquals(getContext().getPackageName(), "io.dcloud.HBuilder");
        LocalServer localServer = new LocalServer(this, 13131);
        this.mMiniServer = localServer;
        localServer.start();
    }

    @Override // io.dcloud.common.DHInterface.IMgr
    public Object processEvent(IMgr.MgrType mgrType, int i, Object obj) {
        try {
            if (checkMgrId(mgrType)) {
                return null;
            }
            return this.mCore.dispatchEvent(mgrType, i, obj);
        } catch (Throwable th) {
            Logger.w("NetMgr.processEvent", th);
            return null;
        }
    }

    @Override // io.dcloud.common.DHInterface.AbsMgr
    public void onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType == ISysEventListener.SysEventType.onPause) {
            LocalServer localServer = this.mMiniServer;
            if (localServer != null) {
                localServer.stop();
                this.mMiniServer = null;
                return;
            }
            return;
        }
        if (sysEventType == ISysEventListener.SysEventType.onResume && this.mMiniServer == null) {
            LocalServer localServer2 = new LocalServer(this, 13131);
            this.mMiniServer = localServer2;
            localServer2.start();
        }
    }

    @Override // io.dcloud.common.DHInterface.AbsMgr
    public void dispose() {
        MiniServer miniServer = this.mLocalServer;
        if (miniServer != null) {
            miniServer.stopMiniServer();
        }
        LocalServer localServer = this.mMiniServer;
        if (localServer != null) {
            localServer.stop();
        }
        UploadMgr uploadMgr = this.mUploadMgr;
        if (uploadMgr != null) {
            uploadMgr.dispose();
        }
        DownloadMgr downloadMgr = this.mDownloadMgr;
        if (downloadMgr != null) {
            downloadMgr.dispose();
        }
        getContext().unregisterReceiver(this.mNetCheckReceiver);
    }
}
