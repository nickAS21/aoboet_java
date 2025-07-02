package com.espressif.iot.esptouch;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class EsptouchResult implements IEsptouchResult {
    private final String mBssid;
    private final InetAddress mInetAddress;
    private AtomicBoolean mIsCancelled = new AtomicBoolean(false);
    private final boolean mIsSuc;

    public EsptouchResult(boolean z, String str, InetAddress inetAddress) {
        this.mIsSuc = z;
        this.mBssid = str;
        this.mInetAddress = inetAddress;
    }

    @Override // com.espressif.iot.esptouch.IEsptouchResult
    public boolean isSuc() {
        return this.mIsSuc;
    }

    @Override // com.espressif.iot.esptouch.IEsptouchResult
    public String getBssid() {
        return this.mBssid;
    }

    @Override // com.espressif.iot.esptouch.IEsptouchResult
    public boolean isCancelled() {
        return this.mIsCancelled.get();
    }

    public void setIsCancelled(boolean z) {
        this.mIsCancelled.set(z);
    }

    @Override // com.espressif.iot.esptouch.IEsptouchResult
    public InetAddress getInetAddress() {
        return this.mInetAddress;
    }
}
