package com.dcloud.zxing.oned;

/* loaded from: classes.dex */
public abstract class UPCEANWriter extends OneDimensionalCodeWriter {
    @Override // com.dcloud.zxing.oned.OneDimensionalCodeWriter
    public int getDefaultMargin() {
        return UPCEANReader.START_END_PATTERN.length;
    }
}
