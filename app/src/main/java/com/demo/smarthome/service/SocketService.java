package com.demo.smarthome.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.demo.smarthome.device.Dev;
import com.demo.smarthome.iprotocol.IProtocol;
import com.demo.smarthome.protocol.MSGCMD;
import com.demo.smarthome.protocol.MSGCMDTYPE;
import com.demo.smarthome.protocol.Msg;
import com.demo.smarthome.protocol.PlProtocol;
import com.demo.smarthome.tools.DateTools;
import com.demo.smarthome.tools.StrTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import io.dcloud.common.util.JSUtil;

/* loaded from: classes.dex */
public class SocketService extends Service {
    private static final String TAG = "SocketService";
    int count;
    private byte[] sendStr;
    String ipAddr = "";
    int port = 0;
    boolean isLogin = false;
    private Msg msg = new Msg();
    private SocketBinder socketBinder = new SocketBinder();
    Socket socket = null;
    boolean socketThreadIsRun = true;
    byte[] buffer = new byte[1024];
    byte[] data = new byte[2048];
    int dataLength = 0;
    OutputStream socketOut = null;
    InputStream socketIn = null;
    IProtocol protocol = new PlProtocol();
    int heartbeatCount = 0;
    int HeartbeatMax = 30;
    int noDataCount = 0;
    Thread socketHeartbeatThread = new Thread() { // from class: com.demo.smarthome.service.SocketService.1
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int i = 0;
            while (SocketService.this.socketThreadIsRun) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (SocketService.this.socket != null && SocketService.this.isLogin) {
                    i++;
                    if (i >= 30) {
                        Log.i(SocketService.TAG, DateTools.getNowTimeString() + "==>heartbeatCoun  time out! socketOut:" + SocketService.this.socketOut);
                        SocketService.this.msg.setCmdType(MSGCMDTYPE.valueOf(160));
                        SocketService.this.msg.setCmd(MSGCMD.valueOf(1));
                        SocketService.this.msg.setId(Cfg.userId);
                        SocketService.this.msg.setTorken(Cfg.torken);
                        SocketService.this.msg.setDataLen(0);
                        SocketService.this.protocol.MessageEnCode(SocketService.this.msg);
                        if (SocketService.this.socketOut != null) {
                            SocketService socketService = SocketService.this;
                            socketService.sendStr = socketService.msg.getSendData();
                            try {
                                Log.i(SocketService.TAG, DateTools.getNowTimeString() + "==>���� ����ָ�\ue8e1" + StrTools.bytesToHexString(SocketService.this.sendStr));
                                SocketService.this.socketOut.write(SocketService.this.sendStr);
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        HttpConnectService.heartThrob(Cfg.userName, Cfg.torken);
                        i = 0;
                    }
                    SocketService.this.noDataCount++;
                    if (SocketService.this.noDataCount >= 90) {
                        SocketService.this.noDataCount = 0;
                        SocketService.this.socketClose();
                    }
                }
            }
        }
    };
    Thread socketThread = new Thread() { // from class: com.demo.smarthome.service.SocketService.2
        /* JADX WARN: Removed duplicated region for block: B:28:0x01cf A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:50:0x0000 A[SYNTHETIC] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 808
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.demo.smarthome.service.SocketService.AnonymousClass2.run():void");
        }
    };

    /* loaded from: classes.dex */
    public class SocketBinder extends Binder {
        public SocketBinder() {
        }

        public SocketService getService() {
            Log.i(SocketService.TAG, "getService");
            return SocketService.this;
        }
    }

    void putSocketData() {
        synchronized (this) {
        }
    }

    void gutSocketData() {
        synchronized (this) {
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "=======onBind");
        return this.socketBinder;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.ipAddr = Cfg.TCP_SERVER_URL;
        this.port = Cfg.TCP_SERVER_PORT;
        this.socketThread.start();
        this.socketHeartbeatThread.start();
        Log.i(TAG, "====onCreate");
    }

    @Override // android.app.Service
    public void onDestroy() {
        Log.i(TAG, "xxxxxxxxxxxonDestroy");
        this.socketThreadIsRun = false;
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socketClose();
        super.onDestroy();
    }

    public void myMethod() {
        Log.i(TAG, " myMethod");
        Log.i(TAG, "ip��" + this.ipAddr + "   port:" + this.port + "    strIpAddr:" + this.ipAddr + "    port:" + this.port + "      socketIsConnected:");
    }

    public void myMethod(int i) {
        Log.i(TAG, " myMethod args:" + i);
        Toast.makeText(this, "����ִ��", 0).show();
    }

    public void socketReConnect() {
        socketClose();
    }

    public boolean socketIsConnected() {
        return this.socket != null;
    }

    void socketClose() {
        try {
            OutputStream outputStream = this.socketOut;
            if (outputStream != null) {
                outputStream.close();
            }
            InputStream inputStream = this.socketIn;
            if (inputStream != null) {
                inputStream.close();
            }
            Socket socket = this.socket;
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.isLogin = false;
        this.socket = null;
        this.socketOut = null;
        this.socketIn = null;
        SendBoardCast(false);
    }

    public synchronized void socketSendMessage(Msg msg) {
        if (this.socket == null) {
            Log.i(TAG, "==socketSendMessage==xxx socket == null");
            return;
        }
        if (this.socketOut == null) {
            Log.i(TAG, "==socketSendMessage==xxx socketOut == null");
            return;
        }
        if (msg == null) {
            Log.i(TAG, "==socketSendMessage==xxx msg == null");
            return;
        }
        byte[] sendData = msg.getSendData();
        if (sendData == null) {
            return;
        }
        try {
            this.socketOut.write(sendData);
            Log.i(TAG, "=��������=socketSendMessage== send ok hex:" + StrTools.bytesToHexString(sendData));
        } catch (IOException e) {
            e.printStackTrace();
            socketClose();
        }
    }

    public synchronized void socketSendMessage(List<Msg> list) {
        Log.i(TAG, "==socketSendMessage==listMsg:" + list.size());
        Iterator<Msg> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Msg next = it.next();
            if (this.socket == null) {
                Log.i(TAG, "==socketSendMessage==xxx socket == null");
                break;
            }
            if (this.socketOut == null) {
                Log.i(TAG, "==socketSendMessage==xxx socketOut == null");
                break;
            }
            if (next == null) {
                Log.i(TAG, "==socketSendMessage==xxx msg == null");
            } else {
                byte[] sendData = next.getSendData();
                if (sendData != null) {
                    try {
                        this.socketOut.write(sendData);
                    } catch (IOException e) {
                        e.printStackTrace();
                        socketClose();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void SendBoardCast(boolean z) {
        String str;
        boolean z2;
        String str2 = " ip:" + this.ipAddr + ":" + this.port;
        Intent intent = new Intent();
        intent.setAction(Cfg.SendBoardCastName);
        if (z) {
            str = str2 + "   ���ӳɹ���";
            z2 = true;
        } else {
            str = str2 + "   ���ӶϿ���";
            z2 = false;
        }
        intent.putExtra("result", str);
        intent.putExtra("conn", z2);
        sendBroadcast(intent);
    }

    /* loaded from: classes.dex */
    public class UDPThread extends Thread {
        String Hostip = "127.0.0.1";
        int port = Cfg.DEV_UDP_SEND_PORT;

        public UDPThread() {
        }

        public String echo(String str) {
            return " adn echo:" + str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            DatagramSocket datagramSocket;
            DatagramPacket datagramPacket = new DatagramPacket(new byte[1024], 1024);
            while (true) {
                if (!SocketService.this.socketThreadIsRun) {
                    datagramSocket = null;
                    break;
                }
                try {
                    sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    InetAddress byName = InetAddress.getByName(this.Hostip);
                    System.out.println("local:" + byName);
                    try {
                        datagramSocket = new DatagramSocket(this.port);
                        String str = datagramSocket.getLocalPort() + "";
                        System.out.println("Hostip:" + this.Hostip + "   localPort:" + str);
                        String str2 = "RPL:\"" + this.Hostip + "\",\"" + str + JSUtil.QUOTE;
                        new DatagramPacket(str2.getBytes(), str2 == null ? 0 : str2.getBytes().length, byName, this.port);
                        Log.i(SocketService.TAG, "====UDP init ok");
                    } catch (SocketException e2) {
                        e2.printStackTrace();
                        System.out.println("init DatagramSocket error port:" + this.port);
                    }
                } catch (UnknownHostException e3) {
                    e3.printStackTrace();
                    System.out.println("init localAddr error Hostip:" + this.Hostip);
                }
            }
            while (SocketService.this.socketThreadIsRun) {
                try {
                    datagramSocket.receive(datagramPacket);
                    String str3 = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    System.out.println(str3);
                    String[] split = str3.split(":");
                    for (String str4 : split) {
                        Log.i(SocketService.TAG, "item1:" + str4);
                    }
                    if (split.length >= 2 && split[0].equals("RPT")) {
                        String[] split2 = split[1].split(JSUtil.COMMA);
                        for (String str5 : split2) {
                            Log.i(SocketService.TAG, "item2:" + str5);
                        }
                        if (split2.length >= 5) {
                            Log.i(SocketService.TAG, "tmp[0]:" + split2[0]);
                            Log.i(SocketService.TAG, "tmp[1]:" + split2[1]);
                            String replace = split2[0].replace('\"', ' ');
                            String replace2 = split2[1].replace('\"', ' ');
                            Log.i(SocketService.TAG, "idStr:" + replace);
                            Log.i(SocketService.TAG, "pasStrs:" + replace2);
                            StrTools.StrHexLowToLong(replace);
                            StrTools.StrHexLowToLong(replace2);
                            StrTools.StrHexHighToLong(replace);
                            StrTools.StrHexHighToLong(replace2);
                            Dev dev = new Dev();
                            dev.setId(StrTools.StrHexLowToLong(replace) + "");
                            dev.setPass(StrTools.StrHexHighToLong(replace2) + "");
                            Cfg.putDevScan(dev);
                        }
                    }
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }
}
