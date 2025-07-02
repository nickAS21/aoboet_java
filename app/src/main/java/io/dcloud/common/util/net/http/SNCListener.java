package io.dcloud.common.util.net.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IMgr;

/* loaded from: classes.dex */
public class SNCListener {
    static final int PORT = 13132;
    public static String TAG = "SNCListener";
    ServerSocket server = null;
    AbsMgr mNetMgr = null;

    public void start(AbsMgr absMgr) {
        try {
            this.mNetMgr = absMgr;
            this.server = new ServerSocket(PORT);
            new RepackTestTask().execute(new Object[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes.dex */
    private class RepackTestTask extends AsyncTask {
        private RepackTestTask() {
        }

        @Override // android.os.AsyncTask
        protected Object doInBackground(Object... objArr) {
            Log.d(SNCListener.TAG, "listening port(13132)");
            while (true) {
                try {
                    Socket accept = SNCListener.this.server.accept();
                    Log.d(SNCListener.TAG, "Get a connection from " + accept.getRemoteSocketAddress().toString());
                    InputStream inputStream = accept.getInputStream();
                    byte[] bArr = new byte[inputStream.available()];
                    inputStream.read(bArr);
                    String str = new String(bArr);
                    Log.d(SNCListener.TAG, "Get some words" + str);
                    publishProgress(str);
                    accept.close();
                } catch (IOException e) {
                    Log.e(SNCListener.TAG, "" + e);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        @Override // android.os.AsyncTask
        protected void onProgressUpdate(Object... objArr) {
            super.onProgressUpdate(objArr);
            SNCListener.this.mNetMgr.processEvent(IMgr.MgrType.AppMgr, 7, objArr[0].toString());
        }
    }
}
