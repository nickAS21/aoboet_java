package io.dcloud.common.util.net.http;

import android.content.Context;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.adapter.io.AdaService;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public class MiniServer extends AdaService {
    final String HOST;
    final String TAG;
    AbsMgr mNetMgr;
    final int mPort;
    ArrayList<Server> mServerSockets;

    public MiniServer(Context context, AbsMgr absMgr) {
        super(context, AbsoluteConst.MINI_SERVER_NAME);
        this.TAG = "miniserver";
        this.mPort = 13130;
        this.HOST = "127.0.0.1";
        this.mNetMgr = null;
        this.mServerSockets = null;
        this.mNetMgr = absMgr;
        this.mServerSockets = new ArrayList<>(1);
    }

    @Override // io.dcloud.common.adapter.io.AdaService
    public void onExecute() {
        Server server = new Server(13130, "127.0.0.1");
        if (server.mServerSocket != null) {
            Logger.d("miniserver", "onExecute _server=" + server);
            server.start();
            this.mServerSockets.add(server);
        }
    }

    @Override // io.dcloud.common.adapter.io.AdaService
    public void onDestroy() {
        Iterator<Server> it = this.mServerSockets.iterator();
        while (it.hasNext()) {
            Server next = it.next();
            Logger.d("miniserver", "onDestroy _server=" + next + ";PORT=13130");
            next.stop();
        }
    }

    /* loaded from: classes.dex */
    class Server implements Runnable {
        boolean mRunning = false;
        ServerSocket mServerSocket;

        public Server(int i, String str) {
            this.mServerSocket = null;
            try {
                this.mServerSocket = new ServerSocket(i, 1, InetAddress.getByName(str));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

        void start() {
            new Thread(this).start();
        }

        void stop() {
            if (this.mServerSocket != null) {
                Logger.d("miniserver", "close serversocket port=13130");
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
                Logger.d("miniserver", "13130;serversocket running...");
                Socket socket = null;
                try {
                    socket = this.mServerSocket.accept();
                    Logger.d("miniserver", "mPort=13130;socket=" + socket);
                    new Response(socket, MiniServer.this.mNetMgr);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.mRunning = false;
                    Logger.d("miniserver", "Exception stop mPort=13130;socket=" + socket, e);
                }
            }
        }
    }
}
