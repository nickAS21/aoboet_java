package com.espressif.iot.esptouch.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import io.dcloud.common.DHInterface.IApp;

/* loaded from: classes.dex */
public class ByteUtil {
    public static final String ESPTOUCH_ENCODING_CHARSET = "UTF-8";

    public static char convertByte2Uint8(byte b) {
        return (char) (b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
    }

    public static void putString2bytes(byte[] bArr, String str, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[i3 + i4] = str.getBytes()[i4];
        }
    }

    public static byte convertUint8toByte(char c) {
        if (c <= 255) {
            return (byte) c;
        }
        throw new RuntimeException("Out of Boundary");
    }

    public static char[] convertBytes2Uint8s(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            cArr[i] = convertByte2Uint8(bArr[i]);
        }
        return cArr;
    }

    public static void putbytes2Uint8s(char[] cArr, byte[] bArr, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            cArr[i + i4] = convertByte2Uint8(bArr[i2 + i4]);
        }
    }

    public static String convertByte2HexString(byte b) {
        return Integer.toHexString(convertByte2Uint8(b));
    }

    public static String convertU8ToHexString(char c) {
        return Integer.toHexString(c);
    }

    public static byte[] splitUint8To2bytes(char c) {
        byte parseInt;
        byte b;
        if (c < 0 || c > 255) {
            throw new RuntimeException("Out of Boundary");
        }
        String hexString = Integer.toHexString(c);
        if (hexString.length() > 1) {
            b = (byte) Integer.parseInt(hexString.substring(0, 1), 16);
            parseInt = (byte) Integer.parseInt(hexString.substring(1, 2), 16);
        } else {
            parseInt = (byte) Integer.parseInt(hexString.substring(0, 1), 16);
            b = 0;
        }
        return new byte[]{b, parseInt};
    }

    public static byte combine2bytesToOne(byte b, byte b2) {
        if (b < 0 || b > 15 || b2 < 0 || b2 > 15) {
            throw new RuntimeException("Out of Boundary");
        }
        return (byte) ((b << 4) | b2);
    }

    public static char combine2bytesToU16(byte b, byte b2) {
        return (char) ((convertByte2Uint8(b) << '\b') | convertByte2Uint8(b2));
    }

    private static byte randomByte() {
        return (byte) (127 - new Random().nextInt(256));
    }

    public static byte[] randomBytes(char c) {
        byte[] bArr = new byte[c];
        for (int i = 0; i < c; i++) {
            bArr[i] = randomByte();
        }
        return bArr;
    }

    public static byte[] genSpecBytes(char c) {
        byte[] bArr = new byte[c];
        for (int i = 0; i < c; i++) {
            bArr[i] = 49;
        }
        return bArr;
    }

    public static byte[] randomBytes(byte b) {
        return randomBytes(convertByte2Uint8(b));
    }

    public static byte[] genSpecBytes(byte b) {
        return genSpecBytes(convertByte2Uint8(b));
    }

    public static String parseBssid(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            bArr2[i3] = bArr[i3 + i];
        }
        return parseBssid(bArr2);
    }

    public static String parseBssid(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            int i = b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
            String hexString = Integer.toHexString(i);
            if (i < 16) {
                hexString = "0" + hexString;
            }
            System.out.println(hexString);
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static byte[] getBytesByString(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            throw new IllegalArgumentException("the charset is invalid");
        }
    }

    private static void test_splitUint8To2bytes() {
        byte[] splitUint8To2bytes = splitUint8To2bytes((char) 20);
        if (splitUint8To2bytes[0] == 1 && splitUint8To2bytes[1] == 4) {
            System.out.println("test_splitUint8To2bytes(): pass");
        } else {
            System.out.println("test_splitUint8To2bytes(): fail");
        }
    }

    private static void test_combine2bytesToOne() {
        if (combine2bytesToOne((byte) 1, (byte) 4) == 20) {
            System.out.println("test_combine2bytesToOne(): pass");
        } else {
            System.out.println("test_combine2bytesToOne(): fail");
        }
    }

    private static void test_convertChar2Uint8() {
        if (convertByte2Uint8((byte) 97) == 'a' && convertByte2Uint8(Byte.MIN_VALUE) == 128 && convertByte2Uint8((byte) -1) == 255) {
            System.out.println("test_convertChar2Uint8(): pass");
        } else {
            System.out.println("test_convertChar2Uint8(): fail");
        }
    }

    private static void test_convertUint8toByte() {
        if (convertUint8toByte('a') == 97 && convertUint8toByte((char) 128) == Byte.MIN_VALUE && convertUint8toByte((char) 255) == -1) {
            System.out.println("test_convertUint8toByte(): pass");
        } else {
            System.out.println("test_convertUint8toByte(): fail");
        }
    }

    private static void test_parseBssid() {
        if (parseBssid(new byte[]{15, -2, 52, -102, -93, -60}).equals("0ffe349aa3c4")) {
            System.out.println("test_parseBssid(): pass");
        } else {
            System.out.println("test_parseBssid(): fail");
        }
    }

    public static void main(String[] strArr) {
        test_convertUint8toByte();
        test_convertChar2Uint8();
        test_splitUint8To2bytes();
        test_combine2bytesToOne();
        test_parseBssid();
    }
}
