package com.espressif.iot.esptouch;

/* loaded from: classes.dex */
public interface IEsptouchTask {
    IEsptouchResult executeForResult() throws RuntimeException;

    void interrupt();

    boolean isCancelled();
}
