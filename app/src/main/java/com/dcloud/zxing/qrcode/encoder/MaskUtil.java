package com.dcloud.zxing.qrcode.encoder;

/* loaded from: classes.dex */
final class MaskUtil {
    private static final int N1 = 3;
    private static final int N2 = 3;
    private static final int N3 = 40;
    private static final int N4 = 10;

    private MaskUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int applyMaskPenaltyRule1(ByteMatrix byteMatrix) {
        return applyMaskPenaltyRule1Internal(byteMatrix, true) + applyMaskPenaltyRule1Internal(byteMatrix, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int applyMaskPenaltyRule2(ByteMatrix byteMatrix) {
        byte[][] array = byteMatrix.getArray();
        int width = byteMatrix.getWidth();
        int height = byteMatrix.getHeight();
        int i = 0;
        for (int i2 = 0; i2 < height - 1; i2++) {
            int i3 = 0;
            while (i3 < width - 1) {
                byte b = array[i2][i3];
                int i4 = i3 + 1;
                if (b == array[i2][i4]) {
                    int i5 = i2 + 1;
                    if (b == array[i5][i3] && b == array[i5][i4]) {
                        i++;
                    }
                }
                i3 = i4;
            }
        }
        return i * 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int applyMaskPenaltyRule3(ByteMatrix matrix) {
        int penalty = 0;
        byte[][] array = matrix.getArray();
        int height = matrix.getHeight();
        int width = matrix.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x + 6 < width; x++) {
                if (array[y][x] == 1 &&
                        array[y][x + 1] == 0 &&
                        array[y][x + 2] == 1 &&
                        array[y][x + 3] == 1 &&
                        array[y][x + 4] == 1 &&
                        array[y][x + 5] == 0 &&
                        array[y][x + 6] == 1) {

                    if ((x + 10 < width &&
                            array[y][x + 7] == 0 &&
                            array[y][x + 8] == 0 &&
                            array[y][x + 9] == 0 &&
                            array[y][x + 10] == 0) ||
                            (x >= 4 &&
                                    array[y][x - 1] == 0 &&
                                    array[y][x - 2] == 0 &&
                                    array[y][x - 3] == 0 &&
                                    array[y][x - 4] == 0)) {
                        penalty += 40;
                    }
                }
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y + 6 < height; y++) {
                if (array[y][x] == 1 &&
                        array[y + 1][x] == 0 &&
                        array[y + 2][x] == 1 &&
                        array[y + 3][x] == 1 &&
                        array[y + 4][x] == 1 &&
                        array[y + 5][x] == 0 &&
                        array[y + 6][x] == 1) {

                    if ((y + 10 < height &&
                            array[y + 7][x] == 0 &&
                            array[y + 8][x] == 0 &&
                            array[y + 9][x] == 0 &&
                            array[y + 10][x] == 0) ||
                            (y >= 4 &&
                                    array[y - 1][x] == 0 &&
                                    array[y - 2][x] == 0 &&
                                    array[y - 3][x] == 0 &&
                                    array[y - 4][x] == 0)) {
                        penalty += 40;
                    }
                }
            }
        }

        return penalty;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int applyMaskPenaltyRule4(ByteMatrix byteMatrix) {
        byte[][] array = byteMatrix.getArray();
        int width = byteMatrix.getWidth();
        int height = byteMatrix.getHeight();
        int i = 0;
        for (int i2 = 0; i2 < height; i2++) {
            byte[] bArr = array[i2];
            for (int i3 = 0; i3 < width; i3++) {
                if (bArr[i3] == 1) {
                    i++;
                }
            }
        }
        return ((int) (Math.abs((i / (byteMatrix.getHeight() * byteMatrix.getWidth())) - 0.5d) * 20.0d)) * 10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0001. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0047 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean getDataMaskBit(int r1, int r2, int r3) {
        /*
            r0 = 1
            switch(r1) {
                case 0: goto L41;
                case 1: goto L42;
                case 2: goto L3e;
                case 3: goto L3a;
                case 4: goto L33;
                case 5: goto L2c;
                case 6: goto L25;
                case 7: goto L1d;
                default: goto L4;
            }
        L4:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r0 = "Invalid mask pattern: "
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            r2.<init>(r1)
            throw r2
        L1d:
            int r1 = r3 * r2
            int r1 = r1 % 3
            int r3 = r3 + r2
            r2 = r3 & 1
            goto L37
        L25:
            int r3 = r3 * r2
            r1 = r3 & 1
            int r3 = r3 % 3
            int r1 = r1 + r3
            goto L38
        L2c:
            int r3 = r3 * r2
            r1 = r3 & 1
            int r3 = r3 % 3
            int r1 = r1 + r3
            goto L44
        L33:
            int r1 = r3 >>> 1
            int r2 = r2 / 3
        L37:
            int r1 = r1 + r2
        L38:
            r1 = r1 & r0
            goto L44
        L3a:
            int r3 = r3 + r2
            int r1 = r3 % 3
            goto L44
        L3e:
            int r1 = r2 % 3
            goto L44
        L41:
            int r3 = r3 + r2
        L42:
            r1 = r3 & 1
        L44:
            if (r1 != 0) goto L47
            goto L48
        L47:
            r0 = 0
        L48:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing.qrcode.encoder.MaskUtil.getDataMaskBit(int, int, int):boolean");
    }

    private static int applyMaskPenaltyRule1Internal(ByteMatrix byteMatrix, boolean z) {
        int height = z ? byteMatrix.getHeight() : byteMatrix.getWidth();
        int width = z ? byteMatrix.getWidth() : byteMatrix.getHeight();
        byte[][] array = byteMatrix.getArray();
        int i = 0;
        for (int i2 = 0; i2 < height; i2++) {
            byte b = -1;
            int i3 = 0;
            for (int i4 = 0; i4 < width; i4++) {
                byte b2 = z ? array[i2][i4] : array[i4][i2];
                if (b2 == b) {
                    i3++;
                } else {
                    if (i3 >= 5) {
                        i += (i3 - 5) + 3;
                    }
                    i3 = 1;
                    b = b2;
                }
            }
            if (i3 >= 5) {
                i += (i3 - 5) + 3;
            }
        }
        return i;
    }
}
