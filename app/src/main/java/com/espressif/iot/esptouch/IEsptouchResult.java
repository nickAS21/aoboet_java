package com.espressif.iot.esptouch;

import java.net.InetAddress;

/* loaded from: classes.dex */
public interface IEsptouchResult {
    String getBssid();

    InetAddress getInetAddress();

    boolean isCancelled();

    boolean isSuc();
}
