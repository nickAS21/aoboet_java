package io.dcloud.feature.barcode.barcode_b;

import com.dcloud.zxing.BarcodeFormat;

import java.util.Vector;
import java.util.regex.Pattern;

import io.dcloud.common.util.JSUtil;

/* compiled from: DecodeFormatManager.java */
/* loaded from: classes.dex old c*/
public final class DecodeFormatManager {
    static final Vector<BarcodeFormat> a;
    static final Vector<BarcodeFormat> b;
    static final Vector<BarcodeFormat> c;
    static final Vector<BarcodeFormat> d;
    private static final Pattern e = Pattern.compile(JSUtil.COMMA);

    static {
        Vector<BarcodeFormat> vector = new Vector<>(5);
        a = vector;
        vector.add(BarcodeFormat.UPC_A);
        vector.add(BarcodeFormat.UPC_E);
        vector.add(BarcodeFormat.EAN_13);
        vector.add(BarcodeFormat.EAN_8);
        vector.add(BarcodeFormat.RSS_14);
        Vector<BarcodeFormat> vector2 = new Vector<>(vector.size() + 4);
        b = vector2;
        vector2.addAll(vector);
        vector2.add(BarcodeFormat.CODE_39);
        vector2.add(BarcodeFormat.CODE_93);
        vector2.add(BarcodeFormat.CODE_128);
        vector2.add(BarcodeFormat.ITF);
        Vector<BarcodeFormat> vector3 = new Vector<>(1);
        c = vector3;
        vector3.add(BarcodeFormat.QR_CODE);
        Vector<BarcodeFormat> vector4 = new Vector<>(1);
        d = vector4;
        vector4.add(BarcodeFormat.DATA_MATRIX);
    }
}
