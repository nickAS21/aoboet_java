package com.demo.smarthome.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.util.Md5Utils;

/* loaded from: classes.dex */
public class StrTools {
    public static int byteToUint(byte b) {
        return b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
    }

    public static byte[] byteToSwapByte(byte[] bArr) {
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr2[i] = bArr[(length - 1) - i];
        }
        return bArr2;
    }

    public static long stringToInt(String str) {
        byte[] bytes = str.getBytes();
        byte[] bArr = new byte[bytes.length];
        long j = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] >= 48 && bytes[i] <= 57) {
                j = (j * 10) + (bytes[i] - 48);
            }
        }
        return j;
    }

    public static long bcdIntToInt(long j) {
        int[] iArr = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000};
        int i = 0;
        for (int i2 = 0; i2 < 8; i2++) {
            i = (int) (i + ((j % 16) * iArr[i2]));
            j /= 16;
        }
        return i;
    }

    public static long intToBcdInt(long j) {
        long j2 = 0;
        for (int i = 0; i < 8; i++) {
            j2 += (j % 10) << (i * 4);
            j /= 10;
        }
        return j2;
    }

    public static long idTo8ByteId(long j) {
        long j2 = 0;
        for (int i = 0; i < 8; i++) {
            j2 += (j % 10) << (i * 4);
            j /= 10;
        }
        return j2;
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
            sb.append(hexString);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String upperCase = str.toUpperCase();
        if (upperCase.length() % 2 != 0) {
            upperCase = "0" + upperCase;
        }
        int length = upperCase.length() / 2;
        char[] charArray = upperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(charArray[i2 + 1]) | (charToByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static void printHexString(byte[] bArr) {
        System.out.print("len:" + bArr.length + "  ");
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            System.out.print(hexString.toUpperCase() + " ");
        }
        System.out.println();
    }

    public static String bytes2HexString(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            str = str + hexString.toUpperCase();
        }
        return str;
    }

    public static byte[] hexStringToByte(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        char[] charArray = str.toCharArray();
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (toByte(charArray[i2 + 1]) | (toByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static final Object bytesToObject(byte[] bArr) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bArr));
        Object readObject = objectInputStream.readObject();
        objectInputStream.close();
        return readObject;
    }

    public static final byte[] objectToBytes(Serializable serializable) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(serializable);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static final String objectToHexString(Serializable serializable) throws IOException {
        return bytesToHexString(objectToBytes(serializable));
    }

    public static final Object hexStringToObject(String str) throws IOException, ClassNotFoundException {
        return bytesToObject(hexStringToByte(str));
    }

    public static String bcd2Str(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            stringBuffer.append((int) ((byte) ((bArr[i] & 240) >>> 4)));
            stringBuffer.append((int) ((byte) (bArr[i] & 15)));
        }
        return stringBuffer.toString().substring(0, 1).equalsIgnoreCase("0") ? stringBuffer.toString().substring(1) : stringBuffer.toString();
    }

    public static byte[] str2Bcd(String str) {
        int i;
        int i2;
        int length = str.length();
        if (length % 2 != 0) {
            str = "0" + str;
            length = str.length();
        }
        byte[] bArr = new byte[length];
        if (length >= 2) {
            length /= 2;
        }
        byte[] bArr2 = new byte[length];
        byte[] bytes = str.getBytes();
        for (int i3 = 0; i3 < str.length() / 2; i3++) {
            int i4 = i3 * 2;
            if (bytes[i4] >= 48 && bytes[i4] <= 57) {
                i = bytes[i4] - 48;
            } else {
                i = ((bytes[i4] >= 97 && bytes[i4] <= 122) ? bytes[i4] - 97 : bytes[i4] - 65) + 10;
            }
            int i5 = i4 + 1;
            if (bytes[i5] >= 48 && bytes[i5] <= 57) {
                i2 = bytes[i5] - 48;
            } else {
                i2 = ((bytes[i5] >= 97 && bytes[i5] <= 122) ? bytes[i5] - 97 : bytes[i5] - 65) + 10;
            }
            bArr2[i3] = (byte) ((i << 4) + i2);
        }
        return bArr2;
    }

    public static String MD5EncodeToHex(String str) {
        return bytesToHexString(MD5Encode(str));
    }

    public static byte[] MD5Encode(String str) {
        return MD5Encode(str.getBytes());
    }

    public static byte[] MD5Encode(byte[] bArr) {
        try {
            return MessageDigest.getInstance(Md5Utils.ALGORITHM).digest(bArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static String strNumToBig(String str) {
        String hexString = Long.toHexString(Long.parseLong(str));
        int length = hexString.length();
        String str2 = "";
        while (length > 0) {
            if (length < 2) {
                str2 = str2 + "0" + hexString.substring(0, length);
                length = 0;
            } else {
                str2 = str2 + hexString.substring(length - 2, length);
                length -= 2;
            }
        }
        return (str2.length() <= 2 || !str2.substring(0, 1).equals("0")) ? str2 : str2.substring(1, str2.length());
    }

    public static String strNumToHex(String str) {
        long parseLong = Long.parseLong(str);
        System.out.println("strNum:" + str);
        System.out.println("num:" + parseLong);
        String hexString = Long.toHexString(parseLong);
        System.out.println("str:" + hexString);
        return hexString;
    }

    public static String strHexNumToStr(String str) {
        long parseLong = Long.parseLong(str);
        System.out.println("strNum:" + str);
        System.out.println("num:" + parseLong);
        String hexString = Long.toHexString(parseLong);
        System.out.println("str:" + hexString);
        return hexString;
    }

    public static String byteHexNumToStr(byte[] bArr) {
        long j = 0;
        for (int length = bArr.length - 1; length >= 0; length--) {
            j += byteToUint(bArr[length]);
            if (length <= 0) {
                break;
            }
            j *= 256;
        }
        String str = j + "";
        System.out.println("b:" + bytesToHexString(bArr));
        System.out.println("str:" + str);
        return str;
    }

    public static long StrHexLowToLong(String str) {
        int i;
        String upperCase = str.replace("0x", " ").trim().toUpperCase();
        while (true) {
            if (upperCase.charAt(0) != '0') {
                break;
            }
            upperCase = upperCase.substring(1);
        }
        if (upperCase.length() % 2 == 1) {
            upperCase = "0" + upperCase;
        }
        int length = upperCase.length() / 2;
        byte[] bArr = new byte[length];
        char[] charArray = upperCase.toCharArray();
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2 * 2;
            bArr[i2] = (byte) (toByte(charArray[i3 + 1]) | (toByte(charArray[i3]) << 4));
        }
        long j = 0;
        for (i = 0; i < length; i++) {
            j = (j * 256) + (bArr[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
        }
        System.out.println("StrHexHighToLong() strVal:" + str + "  to val:" + j);
        return j;
    }

    public static long StrHexHighToLong(String str) {
        String upperCase = str.replace("0x", " ").trim().toUpperCase();
        long j = 0;
        if (upperCase.equals("")) {
            return 0L;
        }
        int length = upperCase.length();
        if (length % 2 == 1) {
            if (length == 1) {
                upperCase = "0" + upperCase;
            } else {
                int i = length - 1;
                upperCase = upperCase.substring(0, i) + "0" + upperCase.substring(i, length);
            }
        }
        int length2 = upperCase.length() / 2;
        System.out.println("len: " + length2 + " str:" + upperCase);
        byte[] bArr = new byte[length2];
        char[] charArray = upperCase.toCharArray();
        for (int i2 = 0; i2 < length2; i2++) {
            int i3 = i2 * 2;
            bArr[i2] = (byte) (toByte(charArray[i3 + 1]) | (toByte(charArray[i3]) << 4));
        }
        for (int i4 = length2 - 1; i4 >= 0; i4--) {
            j = (j * 256) + (bArr[i4] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
        }
        System.out.println("StrHexLowToLong() strVal:" + str + "  to val:" + j);
        return j;
    }
}
