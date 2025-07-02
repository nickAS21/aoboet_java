package com.demo.smarthome.protocol;

/* loaded from: classes.dex */
public enum MSGCMDTYPE {
    CMDTYPE_A0((byte) -96),
    CMDTYPE_AA((byte) -86),
    CMDTYPE_AC((byte) -84),
    CMDTYPE_F0((byte) -16),
    CMDTYPE_EF((byte) -17);

    private byte val;

    MSGCMDTYPE(byte b) {
        this.val = b;
    }

    public static MSGCMDTYPE valueOf(int i) {
        int i2 = i % 255;
        System.out.println("val%0xFF:" + i2);
        if (i2 != -96) {
            if (i2 != -86) {
                if (i2 != -84) {
                    if (i2 != 160) {
                        if (i2 != 170) {
                            if (i2 != 172) {
                                if (i2 != -17) {
                                    if (i2 != -16) {
                                        if (i2 != 239) {
                                            if (i2 != 240) {
                                                return null;
                                            }
                                        }
                                    }
                                    return CMDTYPE_F0;
                                }
                                return CMDTYPE_EF;
                            }
                        }
                    }
                }
                return CMDTYPE_AC;
            }
            return CMDTYPE_AA;
        }
        return CMDTYPE_A0;
    }

    public byte val() {
        return this.val;
    }
}
