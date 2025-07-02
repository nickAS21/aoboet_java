package io.dcloud.common.util;

import android.os.Build;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public class AESUtil {
    public static final String TAG = "AESUtils";

    public static String encrypt(String str, String str2) {
        byte[] bArr;
        try {
            bArr = encrypt(getRawKey(str.getBytes()), str2.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            bArr = null;
        }
        return toHex(bArr);
    }

    public static byte[] encrypt(String str, int i, String str2, String str3) {
        try {
            return encrypt(str.getBytes(), str3.getBytes(), "AES/CBC/PKCS5Padding", str2 != null ? str2.getBytes() : null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String str, String str2) {
        try {
            return new String(decrypt(getRawKey(str.getBytes()), toByte(str2)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getRawKey(byte[] bArr) throws Exception {
        return getRawKey(bArr, 256);
    }

    private static byte[] getRawKey(byte[] bArr, int i) throws Exception {
        SecureRandom secureRandom;
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        if (Build.VERSION.SDK_INT >= 17) {
            secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        secureRandom.setSeed(bArr);
        keyGenerator.init(i, secureRandom);
        return keyGenerator.generateKey().getEncoded();
    }

    private static byte[] encrypt(byte[] bArr, byte[] bArr2, String str, byte[] bArr3) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher cipher = Cipher.getInstance(str);
        if (bArr3 == null) {
            bArr3 = new byte[cipher.getBlockSize()];
        }
        cipher.init(1, secretKeySpec, new IvParameterSpec(bArr3));
        return cipher.doFinal(bArr2);
    }

    private static byte[] encrypt(byte[] bArr, byte[] bArr2) throws Exception {
        return encrypt(bArr, bArr2, "AES/CBC/PKCS5Padding", (byte[]) null);
    }

    private static byte[] decrypt(byte[] bArr, byte[] bArr2) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(bArr2);
    }

    private static byte[] toByte(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = Integer.valueOf(str.substring(i2, i2 + 2), 16).byteValue();
        }
        return bArr;
    }

    private static String toHex(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte b : bArr) {
            appendHex(stringBuffer, b);
        }
        return stringBuffer.toString();
    }

    private static void appendHex(StringBuffer stringBuffer, byte b) {
        stringBuffer.append("0123456789ABCDEF".charAt((b >> 4) & 15)).append("0123456789ABCDEF".charAt(b & 15));
    }
}
