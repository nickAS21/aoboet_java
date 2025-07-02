package com.espressif.iot.esptouch.task;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.espressif.iot.esptouch.EsptouchResult;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.protocol.EsptouchGenerator;
import com.espressif.iot.esptouch.udp.UDPSocketClient;
import com.espressif.iot.esptouch.udp.UDPSocketServer;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.EspNetUtil;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class __EsptouchTask implements __IEsptouchTask {
    private static final int ONE_DATA_LEN = 3;
    private static final String TAG = "EsptouchTask";
    private final String mApBssid;
    private final String mApPassword;
    private final String mApSsid;
    private final Context mContext;
    private volatile EsptouchResult mEsptouchResult;
    private AtomicBoolean mIsCancelled;
    private final boolean mIsSsidHidden;
    private IEsptouchTaskParameter mParameter;
    private final UDPSocketClient mSocketClient;
    private final UDPSocketServer mSocketServer;
    private volatile boolean mIsSuc = false;
    private volatile boolean mIsInterrupt = false;
    private volatile boolean mIsExecuted = false;

    public __EsptouchTask(String str, String str2, String str3, Context context, IEsptouchTaskParameter iEsptouchTaskParameter, boolean z) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("the apSsid should be null or empty");
        }
        str3 = str3 == null ? "" : str3;
        this.mContext = context;
        this.mApSsid = str;
        this.mApBssid = str2;
        this.mApPassword = str3;
        this.mIsCancelled = new AtomicBoolean(false);
        this.mSocketClient = new UDPSocketClient();
        this.mParameter = iEsptouchTaskParameter;
        this.mSocketServer = new UDPSocketServer(this.mParameter.getPortListening(), this.mParameter.getWaitUdpTotalMillisecond(), context);
        this.mIsSsidHidden = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void __interrupt() {
        if (!this.mIsInterrupt) {
            this.mIsInterrupt = true;
            this.mSocketClient.interrupt();
            this.mSocketServer.interrupt();
            Thread.currentThread().interrupt();
        }
    }

    @Override // com.espressif.iot.esptouch.task.__IEsptouchTask
    public void interrupt() {
        Log.d(TAG, "interrupt()");
        this.mIsCancelled.set(true);
        __interrupt();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.espressif.iot.esptouch.task.__EsptouchTask$1] */
    private void __listenAsyn(final int i) {
        new Thread() { // from class: com.espressif.iot.esptouch.task.__EsptouchTask.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Log.d(__EsptouchTask.TAG, "__listenAsyn() start");
                long currentTimeMillis = System.currentTimeMillis();
                byte length = (byte) (ByteUtil.getBytesByString(__EsptouchTask.this.mApSsid + __EsptouchTask.this.mApPassword).length + 9);
                Log.i(__EsptouchTask.TAG, "expectOneByte: " + (length + 0));
                byte b = -1;
                int i2 = 0;
                while (true) {
                    if (i2 < __EsptouchTask.this.mParameter.getThresholdSucBroadcastCount()) {
                        byte[] receiveSpecLenBytes = __EsptouchTask.this.mSocketServer.receiveSpecLenBytes(i);
                        if (receiveSpecLenBytes != null) {
                            b = receiveSpecLenBytes[0];
                        }
                        if (b != length) {
                            if (i == __EsptouchTask.this.mParameter.getEsptouchResultTotalLen() && receiveSpecLenBytes == null) {
                                Log.i(__EsptouchTask.TAG, "esptouch timeout 3");
                                break;
                            }
                            Log.i(__EsptouchTask.TAG, "receive rubbish message, just ignore");
                        } else {
                            i2++;
                            Log.i(__EsptouchTask.TAG, "receive " + i2 + " correct broadcast");
                            int waitUdpTotalMillisecond = (int) (__EsptouchTask.this.mParameter.getWaitUdpTotalMillisecond() - (System.currentTimeMillis() - currentTimeMillis));
                            if (waitUdpTotalMillisecond < 0) {
                                Log.i(__EsptouchTask.TAG, "esptouch timeout");
                                break;
                            }
                            Log.i(__EsptouchTask.TAG, "mSocketServer's new timeout is " + waitUdpTotalMillisecond + " milliseconds");
                            __EsptouchTask.this.mSocketServer.setSoTimeout(waitUdpTotalMillisecond);
                            if (i2 == __EsptouchTask.this.mParameter.getThresholdSucBroadcastCount()) {
                                Log.i(__EsptouchTask.TAG, "receive enough correct broadcast");
                                if (receiveSpecLenBytes != null) {
                                    __EsptouchTask.this.mEsptouchResult = new EsptouchResult(true, ByteUtil.parseBssid(receiveSpecLenBytes, __EsptouchTask.this.mParameter.getEsptouchResultOneLen(), __EsptouchTask.this.mParameter.getEsptouchResultMacLen()), EspNetUtil.parseInetAddr(receiveSpecLenBytes, __EsptouchTask.this.mParameter.getEsptouchResultOneLen() + __EsptouchTask.this.mParameter.getEsptouchResultMacLen(), __EsptouchTask.this.mParameter.getEsptouchResultIpLen()));
                                }
                                __EsptouchTask.this.mIsSuc = true;
                            }
                        }
                    } else {
                        break;
                    }
                }
                __EsptouchTask.this.__interrupt();
                Log.i(__EsptouchTask.TAG, "esptouch finished");
                Log.d(__EsptouchTask.TAG, "__listenAsyn() finish");
            }
        }.start();
    }

    private boolean __execute(IEsptouchGenerator iEsptouchGenerator) {
        byte[][] bArr;
        long currentTimeMillis = System.currentTimeMillis();
        long timeoutTotalCodeMillisecond = currentTimeMillis - this.mParameter.getTimeoutTotalCodeMillisecond();
        byte[][] gCBytes2 = iEsptouchGenerator.getGCBytes2();
        byte[][] dCBytes2 = iEsptouchGenerator.getDCBytes2();
        long j = currentTimeMillis;
        int i = 0;
        while (!this.mIsInterrupt) {
            if (j - timeoutTotalCodeMillisecond >= this.mParameter.getTimeoutTotalCodeMillisecond()) {
                Log.d(TAG, "send gc code ");
                while (!this.mIsInterrupt && System.currentTimeMillis() - j < this.mParameter.getTimeoutGuideCodeMillisecond()) {
                    this.mSocketClient.sendData(gCBytes2, this.mParameter.getTargetHostname(), this.mParameter.getTargetPort(), this.mParameter.getIntervalGuideCodeMillisecond());
                    if (System.currentTimeMillis() - currentTimeMillis > this.mParameter.getWaitUdpSendingMillisecond()) {
                        break;
                    }
                }
                timeoutTotalCodeMillisecond = j;
                bArr = dCBytes2;
            } else {
                bArr = dCBytes2;
                this.mSocketClient.sendData(dCBytes2, i, 3, this.mParameter.getTargetHostname(), this.mParameter.getTargetPort(), this.mParameter.getIntervalDataCodeMillisecond());
                i = (i + 3) % bArr.length;
            }
            j = System.currentTimeMillis();
            if (j - currentTimeMillis > this.mParameter.getWaitUdpSendingMillisecond()) {
                break;
            }
            dCBytes2 = bArr;
        }
        return this.mIsSuc;
    }

    private void __checkTaskValid() {
        if (this.mIsExecuted) {
            throw new IllegalStateException("the Esptouch task could be executed only once");
        }
        this.mIsExecuted = true;
    }

    @Override // com.espressif.iot.esptouch.task.__IEsptouchTask
    public IEsptouchResult executeForResult() throws RuntimeException {
        __checkTaskValid();
        Log.d(TAG, "execute()");
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("Don't call the esptouch Task at Main(UI) thread directly.");
        }
        InetAddress localInetAddress = EspNetUtil.getLocalInetAddress(this.mContext);
        Log.i(TAG, "localInetAddress: " + localInetAddress);
        EsptouchGenerator esptouchGenerator = new EsptouchGenerator(this.mApSsid, this.mApBssid, this.mApPassword, localInetAddress, this.mIsSsidHidden);
        __listenAsyn(this.mParameter.getEsptouchResultTotalLen());
        EsptouchResult esptouchResult = new EsptouchResult(false, null, null);
        for (int i = 0; i < this.mParameter.getTotalRepeatTime(); i++) {
            if (__execute(esptouchGenerator)) {
                this.mEsptouchResult.setIsCancelled(this.mIsCancelled.get());
                return this.mEsptouchResult;
            }
        }
        try {
            Thread.sleep(this.mParameter.getWaitUdpReceivingMillisecond());
            __interrupt();
            esptouchResult.setIsCancelled(this.mIsCancelled.get());
            return esptouchResult;
        } catch (InterruptedException unused) {
            if (this.mIsSuc) {
                return this.mEsptouchResult;
            }
            __interrupt();
            esptouchResult.setIsCancelled(this.mIsCancelled.get());
            return esptouchResult;
        }
    }

    @Override // com.espressif.iot.esptouch.task.__IEsptouchTask
    public boolean isCancelled() {
        return this.mIsCancelled.get();
    }
}
