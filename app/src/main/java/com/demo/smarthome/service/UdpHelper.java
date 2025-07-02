package com.demo.smarthome.service;

import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class UdpHelper implements Runnable {
    private static WifiManager.MulticastLock lock;
    public Boolean IsThreadDisable = false;
    InetAddress mInetAddress;

    public UdpHelper(WifiManager wifiManager) {
        lock = wifiManager.createMulticastLock("UDPwifi");
    }

    public void StartListen() {
        Integer num = 8903;
        byte[] bArr = new byte[100];
        try {
            DatagramSocket datagramSocket = new DatagramSocket(num.intValue());
            datagramSocket.setBroadcast(true);
            DatagramPacket datagramPacket = new DatagramPacket(bArr, 100);
            while (!this.IsThreadDisable.booleanValue()) {
                try {
                    Log.d("UDP Demo", "\u05fc������");
                    lock.acquire();
                    datagramSocket.receive(datagramPacket);
                    Log.d("UDP Demo", datagramPacket.getAddress().getHostAddress().toString() + ":" + new String(datagramPacket.getData()).trim());
                    lock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        } catch (SocketException e2) {
            e2.printStackTrace();
        }
    }

    public static void send(String str) {
        DatagramSocket datagramSocket;
        if (str == null) {
            str = "Hello IdeasAndroid!";
        }
        Log.d("UDP Demo", "UDP��������:" + str);
        InetAddress inetAddress = null;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            datagramSocket = null;
        }
        try {
            inetAddress = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        }
        try {
            datagramSocket.send(new DatagramPacket(str.getBytes(), str.length(), inetAddress, 8904));
            datagramSocket.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        StartListen();
    }
}
