package io.dcloud.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public final class Base64 {
    private static final char[] BASE64CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final String CRLF = "\r\n";
    private static final char PAD = '=';

    private static int decodeInt(int i) {
        if (i >= 65 && i <= 90) {
            return i - 65;
        }
        if (i >= 97 && i <= 122) {
            return (i - 97) + 26;
        }
        if (i >= 48 && i <= 57) {
            return (i - 48) + 52;
        }
        if (i == 43) {
            return 62;
        }
        if (i == 47) {
            return 63;
        }
        return i == 61 ? -2 : -1;
    }

    private static int eightbit(int i) {
        return i & 255;
    }

    private static int sixbit(int i) {
        return i & 63;
    }

    public static String encode(String str) {
        try {
            return encode(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String encode(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i += 3) {
            if (i % 57 == 0 && i != 0) {
                stringBuffer.append(CRLF);
            }
            int i2 = i + 1;
            if (bArr.length <= i2) {
                int eightbit = eightbit(bArr[i]) << 16;
                char[] cArr = BASE64CHARS;
                stringBuffer.append(cArr[sixbit(eightbit >> 18)]);
                stringBuffer.append(cArr[sixbit(eightbit >> 12)]);
                stringBuffer.append(PAD);
                stringBuffer.append(PAD);
            } else {
                int i3 = i + 2;
                if (bArr.length <= i3) {
                    int eightbit2 = (eightbit(bArr[i]) << 16) | (eightbit(bArr[i2]) << 8);
                    char[] cArr2 = BASE64CHARS;
                    stringBuffer.append(cArr2[sixbit(eightbit2 >> 18)]);
                    stringBuffer.append(cArr2[sixbit(eightbit2 >> 12)]);
                    stringBuffer.append(cArr2[sixbit(eightbit2 >> 6)]);
                    stringBuffer.append(PAD);
                } else {
                    int eightbit3 = (eightbit(bArr[i]) << 16) | (eightbit(bArr[i2]) << 8) | eightbit(bArr[i3]);
                    char[] cArr3 = BASE64CHARS;
                    stringBuffer.append(cArr3[sixbit(eightbit3 >> 18)]);
                    stringBuffer.append(cArr3[sixbit(eightbit3 >> 12)]);
                    stringBuffer.append(cArr3[sixbit(eightbit3 >> 6)]);
                    stringBuffer.append(cArr3[sixbit(eightbit3)]);
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String decode2String(String str) {
        try {
            return new String(decode2bytes(str), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (RuntimeException e2) {
            e2.printStackTrace();
            return "";
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static byte[] decode2bytes(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                int i = 0;
                int i2 = 0;
                for (byte b : str.getBytes()) {
                    if (i < 4) {
                        int decodeInt = decodeInt(b);
                        if (decodeInt == -1) {
                            continue;
                        } else {
                            if (decodeInt == -2 && i != 2 && i != 3) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                            if (decodeInt == -2 && i == 2) {
                                byteArrayOutputStream.write(eightbit(i2 >> 4));
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                try {
                                    byteArrayOutputStream.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                                return byteArray;
                            }
                            if (decodeInt == -2 && i == 3) {
                                byteArrayOutputStream.write(eightbit(i2 >> 10));
                                byteArrayOutputStream.write(eightbit(i2 >> 2));
                                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                                try {
                                    byteArrayOutputStream.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                                return byteArray2;
                            }
                            i2 = (i2 << 6) | sixbit(decodeInt);
                            i++;
                        }
                    }
                    if (i == 4) {
                        byteArrayOutputStream.write(eightbit(i2 >> 16));
                        byteArrayOutputStream.write(eightbit(i2 >> 8));
                        byteArrayOutputStream.write(eightbit(i2));
                        i = 0;
                        i2 = 0;
                    }
                }
                byte[] byteArray3 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                return byteArray3;
            } catch (Exception unused) {
                byteArrayOutputStream.close();
                return null;
            } catch (Throwable th) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
                throw th;
            }
        } catch (IOException e6) {
            e6.printStackTrace();
            return null;
        }
    }
}
