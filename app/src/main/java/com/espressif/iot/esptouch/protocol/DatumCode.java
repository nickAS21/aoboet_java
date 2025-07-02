package com.espressif.iot.esptouch.protocol;

import com.espressif.iot.esptouch.task.ICodeData;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.CRC8;
import com.espressif.iot.esptouch.util.EspNetUtil;

import java.net.InetAddress;

/* loaded from: classes.dex */
public class DatumCode implements ICodeData {
    private static final int EXTRA_HEAD_LEN = 5;
    private static final int EXTRA_LEN = 40;
    private final DataCode[] mDataCodes;

    public DatumCode(String str, String str2, String str3, InetAddress inetAddress, boolean z) {
        char length = (char) ByteUtil.getBytesByString(str3).length;
        CRC8 crc8 = new CRC8();
        crc8.update(ByteUtil.getBytesByString(str));
        char value = (char) crc8.getValue();
        crc8.reset();
        crc8.update(EspNetUtil.parseBssid2bytes(str2));
        char value2 = (char) crc8.getValue();
        char length2 = (char) ByteUtil.getBytesByString(str).length;
        String[] split = inetAddress.getHostAddress().split("\\.");
        int length3 = split.length;
        char[] cArr = new char[length3];
        for (int i = 0; i < length3; i++) {
            cArr[i] = (char) Integer.parseInt(split[i]);
        }
        int i2 = length3 + 5 + length;
        char c = (char) (length2 + i2);
        DataCode[] dataCodeArr = new DataCode[z ? c : (char) i2];
        this.mDataCodes = dataCodeArr;
        dataCodeArr[0] = new DataCode(c, 0);
        dataCodeArr[1] = new DataCode(length, 1);
        dataCodeArr[2] = new DataCode(value, 2);
        dataCodeArr[3] = new DataCode(value2, 3);
        char c2 = (char) (value2 ^ ((char) (((char) (((char) (c ^ 0)) ^ length)) ^ value)));
        dataCodeArr[4] = null;
        for (int i3 = 0; i3 < length3; i3++) {
            int i4 = i3 + 5;
            this.mDataCodes[i4] = new DataCode(cArr[i3], i4);
            c2 = (char) (c2 ^ cArr[i3]);
        }
        byte[] bytesByString = ByteUtil.getBytesByString(str3);
        int length4 = bytesByString.length;
        char[] cArr2 = new char[length4];
        for (int i5 = 0; i5 < bytesByString.length; i5++) {
            cArr2[i5] = ByteUtil.convertByte2Uint8(bytesByString[i5]);
        }
        for (int i6 = 0; i6 < length4; i6++) {
            int i7 = i6 + 5 + length3;
            this.mDataCodes[i7] = new DataCode(cArr2[i6], i7);
            c2 = (char) (c2 ^ cArr2[i6]);
        }
        byte[] bytesByString2 = ByteUtil.getBytesByString(str);
        int length5 = bytesByString2.length;
        char[] cArr3 = new char[length5];
        for (int i8 = 0; i8 < bytesByString2.length; i8++) {
            cArr3[i8] = ByteUtil.convertByte2Uint8(bytesByString2[i8]);
            c2 = (char) (c2 ^ cArr3[i8]);
        }
        if (z) {
            for (int i9 = 0; i9 < length5; i9++) {
                int i10 = i9 + 5 + length3 + length;
                this.mDataCodes[i10] = new DataCode(cArr3[i9], i10);
            }
        }
        this.mDataCodes[4] = new DataCode(c2, 4);
    }

    @Override // com.espressif.iot.esptouch.task.ICodeData
    public byte[] getBytes() {
        byte[] bArr = new byte[this.mDataCodes.length * 6];
        int i = 0;
        while (true) {
            DataCode[] dataCodeArr = this.mDataCodes;
            if (i >= dataCodeArr.length) {
                return bArr;
            }
            System.arraycopy(dataCodeArr[i].getBytes(), 0, bArr, i * 6, 6);
            i++;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : getBytes()) {
            String convertByte2HexString = ByteUtil.convertByte2HexString(b);
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
        byte[] bytes = getBytes();
        int length = bytes.length / 2;
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            cArr[i] = (char) (ByteUtil.combine2bytesToU16(bytes[i2], bytes[i2 + 1]) + '(');
        }
        return cArr;
    }
}
