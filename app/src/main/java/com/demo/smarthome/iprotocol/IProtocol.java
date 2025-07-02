package com.demo.smarthome.iprotocol;

import com.demo.smarthome.protocol.Buff;
import com.demo.smarthome.protocol.Msg;

import java.util.List;

/* loaded from: classes.dex */
public interface IProtocol {
    boolean MessageEnCode(Msg msg);

    boolean MessagePackData(Msg msg, String[] strArr);

    List<Msg> checkMessage(Buff buff);
}
