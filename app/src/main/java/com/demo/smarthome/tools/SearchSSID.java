package com.demo.smarthome.tools;

import android.os.Handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class SearchSSID extends Thread {
    private Handler handler;
    private DatagramSocket socket;
    private final String IP = "255.255.255.255";
    private int PORT = 26000;
    private int targetPort = 49000;
    private boolean receive = true;

    private void sendErrorMsg(String str) {
    }

    public SearchSSID(Handler handler) {
        this.handler = handler;
        init();
    }

    public void init() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket((SocketAddress) null);
            this.socket = datagramSocket;
            datagramSocket.setBroadcast(true);
            this.socket.setReuseAddress(true);
            this.socket.bind(new InetSocketAddress(this.PORT));
        } catch (SocketException e) {
            e.printStackTrace();
            sendErrorMsg("Search Thread init fail");
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (this.socket == null) {
            return;
        }
        try {
            byte[] bArr = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, 1024);
            while (this.receive) {
                this.socket.receive(datagramPacket);
                if (this.handler != null) {
                    int length = datagramPacket.getLength();
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, 0, bArr2, 0, length);
                    this.handler.sendMessage(this.handler.obtainMessage(1, bArr2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.socket.close();
        }
    }

    public void close() {
        DatagramSocket datagramSocket = this.socket;
        if (datagramSocket == null) {
            return;
        }
        datagramSocket.close();
    }

    public void sendMsg(byte[] bArr) {
        if (this.socket != null) {
            try {
                System.out.println("targetPort------------------->" + this.targetPort);
                this.socket.send(new DatagramPacket(bArr, bArr.length, InetAddress.getByName("255.255.255.255"), this.targetPort));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.out.println("发送失败");
            } catch (IOException e2) {
                e2.printStackTrace();
                System.out.println("发送失败");
            }
        }
    }

    public void setReceive(boolean z) {
        this.receive = z;
    }

    public void setTargetPort(int i) {
        this.targetPort = i;
    }
}
