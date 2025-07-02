package com.espressif.iot.esptouch.task;

import com.espressif.iot.esptouch.IEsptouchResult;

/* loaded from: classes.dex */
public interface __IEsptouchTask {
    public static final boolean DEBUG = true;

    IEsptouchResult executeForResult() throws RuntimeException;

    void interrupt();

    boolean isCancelled();
}
