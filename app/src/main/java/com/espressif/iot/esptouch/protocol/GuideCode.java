package com.espressif.iot.esptouch.protocol;

import com.espressif.iot.esptouch.task.ICodeData;
import com.espressif.iot.esptouch.util.ByteUtil;

/* loaded from: classes.dex */
public class GuideCode implements ICodeData {
    public static final int GUIDE_CODE_LEN = 4;

    @Override // com.espressif.iot.esptouch.task.ICodeData
    public byte[] getBytes() {
        throw new RuntimeException("DataCode don't support getBytes()");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        char[] u8s = getU8s();
        for (int i = 0; i < 4; i++) {
            String convertU8ToHexString = ByteUtil.convertU8ToHexString(u8s[i]);
            sb.append("0x");
            if (convertU8ToHexString.length() == 1) {
                sb.append("0");
            }
            sb.append(convertU8ToHexString).append(" ");
        }
        return sb.toString();
    }

    @Override // com.espressif.iot.esptouch.task.ICodeData
    public char[] getU8s() {
        return new char[]{515, 514, 513, 512};
    }
}
