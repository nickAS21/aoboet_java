package com.demo.smarthome.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.dcloud.common.DHInterface.IApp;

/* loaded from: classes.dex */
public class Tool {
    public static final int REC_DATA = 1;

    public static byte[] int2byte(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) (i >>> 24)};
    }

    public static boolean checkData(byte[] bArr) {
        System.out.println("data---->" + bytesToHexString(bArr));
        int i = 0;
        for (int i2 = 1; i2 < bArr.length - 1; i2++) {
            i += bArr[i2];
        }
        int i3 = i & 255;
        System.out.println("sum&0xff---------->" + i3);
        return i3 == bArr[bArr.length - 1];
    }

    public static ArrayList<Item> decode_81_data(byte[] bArr) {
        ArrayList<Item> arrayList = new ArrayList<>();
        int length = bArr.length - 6;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, 5, bArr2, 0, length);
        int i = 0;
        int i2 = 0;
        while (i < length - 1) {
            int i3 = i + 1;
            if (new String(new byte[]{bArr2[i], bArr2[i3]}).equals("\r\n")) {
                Item item = new Item();
                int i4 = (i - 2) - i2;
                byte[] bArr3 = new byte[i4];
                System.arraycopy(bArr2, i2, bArr3, 0, i4);
                item.setName(new String(bArr3).trim());
                item.setDbm(bArr2[i - 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
                arrayList.add(item);
                i2 = i + 2;
            }
            i = i3;
        }
        return arrayList;
    }

    public static int[] decode_82_data(byte[] bArr) {
        return new int[]{bArr[4] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE, bArr[5] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE};
    }

    public static byte[] generate_02_data(String str, String str2, int i) {
        try {
            byte[] bytes = (str + "\r\n" + str2).getBytes("utf-8");
            byte[] bArr = new byte[bytes.length + 2];
            bArr[0] = 2;
            bArr[1] = (byte) (i & 255);
            System.arraycopy(bytes, 0, bArr, 2, bytes.length);
            return generateCmd(bArr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] generateCmd(byte[] bArr) {
        int length = bArr.length + 4;
        byte[] bArr2 = new byte[length];
        bArr2[0] = -1;
        byte[] int2byte = int2byte(bArr.length);
        bArr2[1] = int2byte[1];
        bArr2[2] = int2byte[0];
        int i = length - 1;
        bArr2[i] = (byte) (bArr2[1] + bArr2[2]);
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr2[i2 + 3] = bArr[i2];
            bArr2[i] = (byte) (bArr2[i] + bArr[i2]);
        }
        return bArr2;
    }

    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString + " ");
        }
        return sb.toString();
    }

    public static int byteToInt2(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            i = (i << 8) | (bArr[i2] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
        }
        return i;
    }
}
