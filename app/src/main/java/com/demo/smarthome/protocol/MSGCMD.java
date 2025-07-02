package com.demo.smarthome.protocol;

/* loaded from: classes.dex */
public enum MSGCMD {
    CMD01((byte) 1),
    CMD02((byte) 2),
    CMD03((byte) 3),
    CMD04((byte) 4),
    CMD05((byte) 5),
    CMD06((byte) 6),
    CMD07((byte) 7),
    CMD08((byte) 8),
    CMD09((byte) 9),
    CMD0A((byte) 10),
    CMD0B((byte) 11),
    CMD0C((byte) 12),
    CMD10((byte) 16),
    CMD11((byte) 17),
    CMD12((byte) 18),
    CMD13((byte) 19),
    CMD14((byte) 20),
    CMD55((byte) 85),
    CMD56((byte) 86),
    CMD57((byte) 87),
    CMDAA((byte) -86),
    CMDAB((byte) -85),
    CMDF0((byte) -16),
    CMDEE((byte) -18),
    CMDFF((byte) -1),
    CMD00((byte) 0);

    private byte val;

    MSGCMD(byte b) {
        this.val = b;
    }

    public static MSGCMD valueOf(int i) {
        int i2 = i % 255;
        if (i2 != -86) {
            if (i2 != -85) {
                if (i2 != -18) {
                    if (i2 != -16) {
                        if (i2 != 238) {
                            if (i2 != 240) {
                                if (i2 != 255) {
                                    if (i2 != 170) {
                                        if (i2 != 171) {
                                            switch (i2) {
                                                case -1:
                                                    break;
                                                case 0:
                                                    return CMD00;
                                                case 1:
                                                    return CMD01;
                                                case 2:
                                                    return CMD02;
                                                case 3:
                                                    return CMD03;
                                                case 4:
                                                    return CMD04;
                                                case 5:
                                                    return CMD05;
                                                case 6:
                                                    return CMD06;
                                                case 7:
                                                    return CMD07;
                                                case 8:
                                                    return CMD08;
                                                case 9:
                                                    return CMD09;
                                                case 10:
                                                    return CMD0A;
                                                case 11:
                                                    return CMD0B;
                                                case 12:
                                                    return CMD0C;
                                                default:
                                                    switch (i2) {
                                                        case 16:
                                                            return CMD10;
                                                        case 17:
                                                            return CMD11;
                                                        case 18:
                                                            return CMD12;
                                                        case 19:
                                                            return CMD13;
                                                        case 20:
                                                            return CMD14;
                                                        default:
                                                            switch (i2) {
                                                                case 85:
                                                                    return CMD55;
                                                                case 86:
                                                                    return CMD56;
                                                                case 87:
                                                                    return CMD57;
                                                                default:
                                                                    return null;
                                                            }
                                                    }
                                            }
                                        }
                                    }
                                }
                                return CMDFF;
                            }
                        }
                    }
                    return CMDF0;
                }
                return CMDEE;
            }
            return CMDAB;
        }
        return CMDAA;
    }

    public byte val() {
        return this.val;
    }
}
