package com.dcloud.zxing.multi.qrcode.detector;

import com.dcloud.zxing.DecodeHintType;
import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.ReaderException;
import com.dcloud.zxing.ResultPointCallback;
import com.dcloud.zxing.common.BitMatrix;
import com.dcloud.zxing.common.DetectorResult;
import com.dcloud.zxing.qrcode.detector.Detector;
import com.dcloud.zxing.qrcode.detector.FinderPatternInfo;

import java.util.ArrayList;
import java.util.Map;

/* loaded from: classes.dex */
public final class MultiDetector extends Detector {
    private static final DetectorResult[] EMPTY_DETECTOR_RESULTS = new DetectorResult[0];

    public MultiDetector(BitMatrix bitMatrix) {
        super(bitMatrix);
    }

    public DetectorResult[] detectMulti(Map<DecodeHintType, ?> map) throws NotFoundException {
        FinderPatternInfo[] findMulti = new MultiFinderPatternFinder(getImage(), map == null ? null : (ResultPointCallback) map.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK)).findMulti(map);
        if (findMulti.length == 0) {
            throw NotFoundException.getNotFoundInstance();
        }
        ArrayList arrayList = new ArrayList();
        for (FinderPatternInfo finderPatternInfo : findMulti) {
            try {
                arrayList.add(processFinderPatternInfo(finderPatternInfo));
            } catch (ReaderException unused) {
            }
        }
        if (arrayList.isEmpty()) {
            return EMPTY_DETECTOR_RESULTS;
        }
        return (DetectorResult[]) arrayList.toArray(new DetectorResult[arrayList.size()]);
    }
}
