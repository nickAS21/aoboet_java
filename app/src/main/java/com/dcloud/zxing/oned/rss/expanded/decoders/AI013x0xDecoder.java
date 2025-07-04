package com.dcloud.zxing.oned.rss.expanded.decoders;

import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.common.BitArray;

/* loaded from: classes.dex */
abstract class AI013x0xDecoder extends AI01weightDecoder {
    private static final int HEADER_SIZE = 5;
    private static final int WEIGHT_SIZE = 15;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AI013x0xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    @Override // com.dcloud.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder
    public String parseInformation() throws NotFoundException {
        if (getInformation().getSize() != 60) {
            throw NotFoundException.getNotFoundInstance();
        }
        StringBuilder sb = new StringBuilder();
        encodeCompressedGtin(sb, 5);
        encodeCompressedWeight(sb, 45, 15);
        return sb.toString();
    }
}
