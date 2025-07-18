package com.dcloud.zxing.oned;

import com.dcloud.zxing.BarcodeFormat;
import com.dcloud.zxing.DecodeHintType;
import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.ReaderException;
import com.dcloud.zxing.Result;
import com.dcloud.zxing.common.BitArray;
import com.dcloud.zxing.oned.rss.RSS14Reader;
import com.dcloud.zxing.oned.rss.expanded.RSSExpandedReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/* loaded from: classes.dex */
public final class MultiFormatOneDReader extends OneDReader {
    private final OneDReader[] readers;

    public MultiFormatOneDReader(Map<DecodeHintType, ?> map) {
        Collection collection = map == null ? null : (Collection) map.get(DecodeHintType.POSSIBLE_FORMATS);
        boolean z = (map == null || map.get(DecodeHintType.ASSUME_CODE_39_CHECK_DIGIT) == null) ? false : true;
        ArrayList arrayList = new ArrayList();
        if (collection != null) {
            if (collection.contains(BarcodeFormat.EAN_13) || collection.contains(BarcodeFormat.UPC_A) || collection.contains(BarcodeFormat.EAN_8) || collection.contains(BarcodeFormat.UPC_E)) {
                arrayList.add(new MultiFormatUPCEANReader(map));
            }
            if (collection.contains(BarcodeFormat.CODE_39)) {
                arrayList.add(new Code39Reader(z));
            }
            if (collection.contains(BarcodeFormat.CODE_93)) {
                arrayList.add(new Code93Reader());
            }
            if (collection.contains(BarcodeFormat.CODE_128)) {
                arrayList.add(new Code128Reader());
            }
            if (collection.contains(BarcodeFormat.ITF)) {
                arrayList.add(new ITFReader());
            }
            if (collection.contains(BarcodeFormat.CODABAR)) {
                arrayList.add(new CodaBarReader());
            }
            if (collection.contains(BarcodeFormat.RSS_14)) {
                arrayList.add(new RSS14Reader());
            }
            if (collection.contains(BarcodeFormat.RSS_EXPANDED)) {
                arrayList.add(new RSSExpandedReader());
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add(new MultiFormatUPCEANReader(map));
            arrayList.add(new Code39Reader());
            arrayList.add(new CodaBarReader());
            arrayList.add(new Code93Reader());
            arrayList.add(new Code128Reader());
            arrayList.add(new ITFReader());
            arrayList.add(new RSS14Reader());
            arrayList.add(new RSSExpandedReader());
        }
        this.readers = (OneDReader[]) arrayList.toArray(new OneDReader[arrayList.size()]);
    }

    @Override // com.dcloud.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Map<DecodeHintType, ?> map) throws NotFoundException {
        for (OneDReader oneDReader : this.readers) {
            try {
                return oneDReader.decodeRow(i, bitArray, map);
            } catch (ReaderException unused) {
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    @Override // com.dcloud.zxing.oned.OneDReader, com.dcloud.zxing.Reader
    public void reset() {
        for (OneDReader oneDReader : this.readers) {
            oneDReader.reset();
        }
    }
}
