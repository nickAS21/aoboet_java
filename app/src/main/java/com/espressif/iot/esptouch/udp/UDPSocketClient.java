package com.espressif.iot.esptouch.udp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class UDPSocketClient {
    private static final String TAG = "UDPSocketClient";
    private volatile boolean mIsClosed;
    private volatile boolean mIsStop;
    private DatagramSocket mSocket;

    public UDPSocketClient() {
        try {
            this.mSocket = new DatagramSocket();
            this.mIsStop = false;
            this.mIsClosed = false;
        } catch (SocketException e) {
            Log.e(TAG, "SocketException");
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public void interrupt() {
        Log.i(TAG, "USPSocketClient is interrupt");
        this.mIsStop = true;
    }

    public synchronized void close() {
        if (!this.mIsClosed) {
            this.mSocket.close();
            this.mIsClosed = true;
        }
    }

    public void sendData(byte[][] bArr, String str, int i, long j) {
        sendData(bArr, 0, bArr.length, str, i, j);
    }

    public void sendData(byte[][] bArr, int i, int i2, String str, int i3, long j) {
        if (bArr == null || bArr.length <= 0) {
            Log.e(TAG, "sendData(): data == null or length <= 0");
            return;
        }
        for (int i4 = i; !this.mIsStop && i4 < i + i2; i4++) {
            if (bArr[i4].length != 0) {
                try {
                    this.mSocket.send(new DatagramPacket(bArr[i4], bArr[i4].length, InetAddress.getByName(str), i3));
                } catch (UnknownHostException e) {
                    Log.e(TAG, "sendData(): UnknownHostException");
                    e.printStackTrace();
                    this.mIsStop = true;
                } catch (IOException unused) {
                    Log.e(TAG, "sendData(): IOException, but just ignore it");
                }
                try {
                    Thread.sleep(j);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                    Log.e(TAG, "sendData is Interrupted");
                    this.mIsStop = true;
                }
            }
        }
        if (this.mIsStop) {
            close();
        }
    }
}
