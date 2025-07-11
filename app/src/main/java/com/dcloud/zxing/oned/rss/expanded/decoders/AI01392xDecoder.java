package com.dcloud.zxing.oned.rss.expanded.decoders;

import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.common.BitArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AI01392xDecoder extends AI01decoder {
    private static final int HEADER_SIZE = 8;
    private static final int LAST_DIGIT_SIZE = 2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AI01392xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    @Override // com.dcloud.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder
    public String parseInformation() throws NotFoundException {
        if (getInformation().getSize() < 48) {
            throw NotFoundException.getNotFoundInstance();
        }
        StringBuilder sb = new StringBuilder();
        encodeCompressedGtin(sb, 8);
        int extractNumericValueFromBitArray = getGeneralDecoder().extractNumericValueFromBitArray(48, 2);
        sb.append("(392");
        sb.append(extractNumericValueFromBitArray);
        sb.append(')');
        sb.append(getGeneralDecoder().decodeGeneralPurposeField(50, null).getNewString());
        return sb.toString();
    }
}
