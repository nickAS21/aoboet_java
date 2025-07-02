package com.demo.smarthome.protocol;

/* loaded from: classes.dex */
public enum MSGSTATE {
    MSG_SEND_ERROE((byte) 1),
    MSG_SEND_OK((byte) 0),
    MSG_SEND((byte) 2);

    private byte val;

    MSGSTATE(byte b) {
        this.val = b;
    }

    public static MSGSTATE valueOf(int i) {
        if (i == 0) {
            return MSG_SEND_OK;
        }
        if (i == 1) {
            return MSG_SEND_ERROE;
        }
        if (i != 2) {
            return null;
        }
        return MSG_SEND;
    }

    public byte val() {
        return this.val;
    }
}
