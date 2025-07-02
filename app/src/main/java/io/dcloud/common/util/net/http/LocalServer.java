package io.dcloud.common.util.net.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.adapter.util.Logger;

/* loaded from: classes.dex */
public class LocalServer implements Runnable {
    AbsMgr mNetMgr;
    int mPort;
    ServerSocket mServerSocket;
    final String TAG = "miniserver";
    String HOST = "127.0.0.1";
    boolean mRunning = false;

    public LocalServer(AbsMgr absMgr, int i) {
        this.mPort = 13131;
        this.mServerSocket = null;
        this.mNetMgr = null;
        this.mPort = i;
        this.mNetMgr = absMgr;
        try {
            this.mServerSocket = new ServerSocket(i, 1, InetAddress.getByName(this.HOST));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        if (this.mServerSocket != null) {
            Logger.d("miniserver", "close serversocket port=" + this.mPort);
            try {
                this.mServerSocket.close();
                this.mServerSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.mRunning = false;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.mRunning = true;
        while (this.mRunning) {
            Logger.d("miniserver", this.mPort + " serversocket running...");
            try {
                Socket accept = this.mServerSocket.accept();
                Logger.d("miniserver", "mPort=" + this.mPort + ";socket=" + accept);
                new Response(accept, this.mNetMgr);
            } catch (Exception e) {
                e.printStackTrace();
                this.mRunning = false;
                Logger.w("Exception stop mPort=" + this.mPort + ";socket=", e);
            }
        }
    }
}
