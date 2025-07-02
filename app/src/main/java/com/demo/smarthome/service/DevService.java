package com.demo.smarthome.service;

import com.demo.smarthome.device.Dev;

import java.util.List;

/* loaded from: classes.dex */
public interface DevService {
    boolean findDevById(int i);

    int findDevMaxId();

    Dev getDevById(String str);

    List<Dev> getDevList();

    boolean removeDev(String str);

    boolean saveDev(Dev dev);
}
