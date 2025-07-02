package com.demo.smarthome.service;

import android.app.Application;

import com.demo.smarthome.device.Dev;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class Cfg extends Application {
    public static final String DEV_UDP_IPADDR = "192.168.4.1";
    public static final int DEV_UDP_PORT = 8001;
    public static final int DEV_UDP_READ_DELAY = 15;
    public static final int DEV_UDP_SEND_DELAY = 100;
    public static final int DEV_UDP_SEND_PORT = 2468;
    public static final String KEY_PASS_WORD = "password";
    public static final String KEY_USER_NAME = "username";
    public static final String SendBoardCastName = "com.demo.smarthome.service.socketconnect";
    public static final int TCP_SERVER_PORT = 6009;
    public static final String TCP_SERVER_URL = "cloud.ai-thinker.com";
    public static final String VERSION = "10001";
    public static final String WEBSERVICE_SERVER_URL = "http://cloud.ai-thinker.com/service/s.asmx";
    public static long id = 0;
    public static boolean isDeleteDev = false;
    public static boolean isLogin = false;
    public static boolean isSubmitDev = false;
    public static String regUserName = "";
    public static String regUserPass = "";
    public static boolean register = false;
    public static String savePath = "//sdcard//myImage/";
    public static String torken = "";
    public static String userName = "";
    public static byte[] userId = new byte[0];
    public static byte[] passWd = new byte[0];
    public static byte[] tcpTorken = new byte[0];
    public static List<Dev> listDev = new ArrayList();
    private static List<Dev> listDevScan = new ArrayList();
    public static String deviceId = "";
    public static String devicePwd = "";

    public static Dev getDevById(String str) {
        for (Dev dev : listDev) {
            if (dev.getId().equals(str)) {
                return dev;
            }
        }
        return null;
    }

    public static Dev getDevScan() {
        Iterator<Dev> it = listDevScan.iterator();
        if (!it.hasNext()) {
            return null;
        }
        Dev next = it.next();
        listDevScan.remove(next);
        return next;
    }

    public static void putDevScan(Dev dev) {
        if (dev == null) {
            return;
        }
        Iterator<Dev> it = listDev.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(dev.getId())) {
                return;
            }
        }
        Iterator<Dev> it2 = listDevScan.iterator();
        while (it2.hasNext()) {
            if (it2.next().getId().equals(dev.getId())) {
                return;
            }
        }
        listDevScan.add(dev);
    }

    public static void devScanClean() {
        listDevScan.clear();
    }
}
