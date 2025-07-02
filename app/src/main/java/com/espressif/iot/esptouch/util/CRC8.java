package com.espressif.iot.esptouch.util;

import java.util.zip.Checksum;

/* loaded from: classes.dex */
public class CRC8 implements Checksum {
    private static final short CRC_INITIAL = 0;
    private static final short CRC_POLYNOM = 140;
    private static final short[] crcTable = new short[256];
    private final short init = CRC_INITIAL;
    private short value = CRC_INITIAL;

    static {
        for (int i = 0; i < 256; i++) {
            int i2 = i;
            for (int i3 = 0; i3 < 8; i3++) {
                i2 = (i2 & 1) != 0 ? (i2 >>> 1) ^ 140 : i2 >>> 1;
            }
            crcTable[i] = (short) i2;
        }
    }

    @Override // java.util.zip.Checksum
    public void update(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            byte b = bArr[i + i3];
            short s = this.value;
            this.value = (short) (crcTable[(b ^ s) & 255] ^ (s << 8));
        }
    }

//    @Override // java.util.zip.Checksum
    public void update(byte[] bArr) {
        update(bArr, 0, bArr.length);
    }

    @Override // java.util.zip.Checksum
    public void update(int i) {
        update(new byte[]{(byte) i}, 0, 1);
    }

    @Override // java.util.zip.Checksum
    public long getValue() {
        return this.value & 255;
    }

    @Override // java.util.zip.Checksum
    public void reset() {
        this.value = this.init;
    }
}
