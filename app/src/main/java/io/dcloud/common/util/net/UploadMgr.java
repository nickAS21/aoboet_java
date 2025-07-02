package io.dcloud.common.util.net;

/* loaded from: classes.dex */
public class UploadMgr {
    private static UploadMgr mUploadMgr;
    private NetWorkLoop mUploadLoop;

    public void dispose() {
    }

    private UploadMgr() {
        NetWorkLoop netWorkLoop = new NetWorkLoop();
        this.mUploadLoop = netWorkLoop;
        netWorkLoop.startThreadPool();
    }

    public static UploadMgr getUploadMgr() {
        if (mUploadMgr == null) {
            mUploadMgr = new UploadMgr();
        }
        return mUploadMgr;
    }

    public void start(NetWork netWork) {
        this.mUploadLoop.addNetWork(netWork);
    }

    public void abort(NetWork netWork) {
        removeNetWork(netWork);
    }

    public void removeNetWork(NetWork netWork) {
        this.mUploadLoop.removeNetWork(netWork);
    }
}
