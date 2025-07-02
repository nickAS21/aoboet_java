package com.dcloud.zxing.datamatrix.encoder;

/* loaded from: classes.dex */
final class DataMatrixSymbolInfo144 extends SymbolInfo {
    @Override // com.dcloud.zxing.datamatrix.encoder.SymbolInfo
    public int getDataLengthForInterleavedBlock(int i) {
        return i <= 8 ? 156 : 155;
    }

    @Override // com.dcloud.zxing.datamatrix.encoder.SymbolInfo
    public int getInterleavedBlockCount() {
        return 10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DataMatrixSymbolInfo144() {
        super(false, 1558, 620, 22, 22, 36);
        this.rsBlockData = -1;
        this.rsBlockError = 62;
    }
}
