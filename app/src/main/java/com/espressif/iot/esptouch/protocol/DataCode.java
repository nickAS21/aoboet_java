package com.espressif.iot.esptouch.protocol;

import com.espressif.iot.esptouch.task.ICodeData;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.CRC8;

/* loaded from: classes.dex */
public class DataCode implements ICodeData {
    public static final int DATA_CODE_LEN = 6;
    private static final int INDEX_MAX = 127;
    private final byte mCrcHigh;
    private final byte mCrcLow;
    private final byte mDataHigh;
    private final byte mDataLow;
    private final byte mSeqHeader;

    public DataCode(char c, int i) {
        if (i > INDEX_MAX) {
            throw new RuntimeException("index > INDEX_MAX");
        }
        byte[] splitUint8To2bytes = ByteUtil.splitUint8To2bytes(c);
        this.mDataHigh = splitUint8To2bytes[0];
        this.mDataLow = splitUint8To2bytes[1];
        CRC8 crc8 = new CRC8();
        crc8.update(ByteUtil.convertUint8toByte(c));
        crc8.update(i);
        byte[] splitUint8To2bytes2 = ByteUtil.splitUint8To2bytes((char) crc8.getValue());
        this.mCrcHigh = splitUint8To2bytes2[0];
        this.mCrcLow = splitUint8To2bytes2[1];
        this.mSeqHeader = (byte) i;
    }

    @Override // com.espressif.iot.esptouch.task.ICodeData
    public byte[] getBytes() {
        return new byte[]{0, ByteUtil.combine2bytesToOne(this.mCrcHigh, this.mDataHigh), 1, this.mSeqHeader, 0, ByteUtil.combine2bytesToOne(this.mCrcLow, this.mDataLow)};
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = getBytes();
        for (int i = 0; i < 6; i++) {
            String convertByte2HexString = ByteUtil.convertByte2HexString(bytes[i]);
            sb.append("0x");
            if (convertByte2HexString.length() == 1) {
                sb.append("0");
            }
            sb.append(convertByte2HexString).append(" ");
        }
        return sb.toString();
    }

    @Override // com.espressif.iot.esptouch.task.ICodeData
    public char[] getU8s() {
        throw new RuntimeException("DataCode don't support getU8s()");
    }
}
