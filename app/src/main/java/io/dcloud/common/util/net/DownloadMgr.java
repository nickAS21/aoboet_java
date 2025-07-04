package io.dcloud.common.util.net;

import io.dcloud.common.adapter.util.Logger;

/* loaded from: classes.dex */
public class DownloadMgr {
    private static DownloadMgr mDownloadMgr;
    private NetWorkLoop mDownloadLoop;

    private DownloadMgr() {
        NetWorkLoop netWorkLoop = new NetWorkLoop();
        this.mDownloadLoop = netWorkLoop;
        netWorkLoop.startThreadPool();
    }

    public static DownloadMgr getDownloadMgr() {
        if (mDownloadMgr == null) {
            mDownloadMgr = new DownloadMgr();
        }
        return mDownloadMgr;
    }

    public void addQuestTask(NetWork netWork) {
        this.mDownloadLoop.addNetWork(netWork);
    }

    public void removeTask(NetWork netWork) {
        this.mDownloadLoop.removeNetWork(netWork);
    }

    public void dispose() {
        Logger.d("DownloadMgr: dispose");
        this.mDownloadLoop.dispose();
        mDownloadMgr = null;
    }
}
