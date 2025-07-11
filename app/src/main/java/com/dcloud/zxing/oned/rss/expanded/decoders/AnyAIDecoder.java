package com.dcloud.zxing.oned.rss.expanded.decoders;

import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.common.BitArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AnyAIDecoder extends AbstractExpandedDecoder {
    private static final int HEADER_SIZE = 5;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnyAIDecoder(BitArray bitArray) {
        super(bitArray);
    }

    @Override // com.dcloud.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder
    public String parseInformation() throws NotFoundException {
        return getGeneralDecoder().decodeAllCodes(new StringBuilder(), 5);
    }
}
