package io.dcloud.common.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import io.dcloud.common.DHInterface.IApp;

/* loaded from: classes.dex */
public class AESHelper {
    private static final String CipherMode = "AES/ECB/PKCS5Padding";

    private static SecretKeySpec createKey(String str) {
        byte[] bArr;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(32);
        stringBuffer.append(str);
        while (stringBuffer.length() < 32) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 32) {
            stringBuffer.setLength(32);
        }
        try {
            bArr = stringBuffer.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bArr = null;
        }
        return new SecretKeySpec(bArr, "AES");
    }

    public static byte[] encrypt(byte[] bArr, String str) {
        try {
            SecretKeySpec createKey = createKey(str);
            System.out.println(createKey);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(1, createKey);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String str, String str2) {
        byte[] bArr;
        try {
            bArr = str.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            bArr = null;
        }
        return byte2hex(encrypt(bArr, str2));
    }

    public static byte[] decrypt(byte[] bArr, String str) {
        try {
            SecretKeySpec createKey = createKey(str);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(2, createKey);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String str, String str2) {
        byte[] bArr;
        try {
            bArr = hex2byte(str);
        } catch (Exception e) {
            e.printStackTrace();
            bArr = null;
        }
        byte[] decrypt = decrypt(bArr, str2);
        if (decrypt == null) {
            return null;
        }
        try {
            return new String(decrypt, "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String byte2hex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
            if (hexString.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString().toUpperCase();
    }

    private static byte[] hex2byte(String str) {
        if (str == null || str.length() < 2) {
            return new byte[0];
        }
        String lowerCase = str.toLowerCase();
        int length = lowerCase.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (Integer.parseInt(lowerCase.substring(i2, i2 + 2), 16) & 255);
        }
        return bArr;
    }
}
