package com.espressif.iot.esptouch.udp;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

/* loaded from: classes.dex */
public class UDPSocketServer {
    private static final String TAG = "UDPSocketServer";
    private final byte[] buffer;
    private Context mContext;
    private volatile boolean mIsClosed;
    private WifiManager.MulticastLock mLock;
    private DatagramPacket mReceivePacket;
    private DatagramSocket mServerSocket;

    private synchronized void acquireLock() {
        WifiManager.MulticastLock multicastLock = this.mLock;
        if (multicastLock != null && !multicastLock.isHeld()) {
            this.mLock.acquire();
        }
    }

    private synchronized void releaseLock() {
        WifiManager.MulticastLock multicastLock = this.mLock;
        if (multicastLock != null) {
            if (multicastLock.isHeld()) {
                try {
                    this.mLock.release();
                } catch (Throwable unused) {
                }
            }
        }
    }

    public UDPSocketServer(int i, int i2, Context context) {
        this.mContext = context;
        byte[] bArr = new byte[64];
        this.buffer = bArr;
        this.mReceivePacket = new DatagramPacket(bArr, 64);
        try {
            DatagramSocket datagramSocket = new DatagramSocket(i);
            this.mServerSocket = datagramSocket;
            datagramSocket.setSoTimeout(i2);
            this.mIsClosed = false;
            this.mLock = ((WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE)).createMulticastLock("test wifi");
            Log.d(TAG, "mServerSocket is created, socket read timeout: " + i2 + ", port: " + i);
        } catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();
        }
    }

    public boolean setSoTimeout(int i) {
        try {
            this.mServerSocket.setSoTimeout(i);
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte receiveOneByte() {
        Log.d(TAG, "receiveOneByte() entrance");
        try {
            acquireLock();
            this.mServerSocket.receive(this.mReceivePacket);
            Log.d(TAG, "receive: " + (this.mReceivePacket.getData()[0] + 0));
            return this.mReceivePacket.getData()[0];
        } catch (IOException e) {
            e.printStackTrace();
            return Byte.MIN_VALUE;
        }
    }

    public byte[] receiveSpecLenBytes(int i) {
        Log.d(TAG, "receiveSpecLenBytes() entrance: len = " + i);
        try {
            acquireLock();
            this.mServerSocket.receive(this.mReceivePacket);
            byte[] copyOf = Arrays.copyOf(this.mReceivePacket.getData(), this.mReceivePacket.getLength());
            Log.d(TAG, "received len : " + copyOf.length);
            for (int i2 = 0; i2 < copyOf.length; i2++) {
                Log.e(TAG, "recDatas[" + i2 + "]:" + ((int) copyOf[i2]));
            }
            Log.e(TAG, "receiveSpecLenBytes: " + new String(copyOf));
            if (copyOf.length == i) {
                return copyOf;
            }
            Log.w(TAG, "received len is different from specific len, return null");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void interrupt() {
        Log.i(TAG, "USPSocketServer is interrupt");
        close();
    }

    public synchronized void close() {
        if (!this.mIsClosed) {
            Log.e(TAG, "mServerSocket is closed");
            this.mServerSocket.close();
            releaseLock();
            this.mIsClosed = true;
        }
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
