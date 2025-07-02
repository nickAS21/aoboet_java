package com.dcloud.zxing.multi;

import com.dcloud.zxing.BinaryBitmap;
import com.dcloud.zxing.ChecksumException;
import com.dcloud.zxing.DecodeHintType;
import com.dcloud.zxing.FormatException;
import com.dcloud.zxing.NotFoundException;
import com.dcloud.zxing.Reader;
import com.dcloud.zxing.Result;

import java.util.Map;

/* loaded from: classes.dex */
public final class ByQuadrantReader implements Reader {
    private final Reader delegate;

    public ByQuadrantReader(Reader reader) {
        this.delegate = reader;
    }

    @Override // com.dcloud.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, ChecksumException, FormatException {
        return decode(binaryBitmap, null);
    }

    @Override // com.dcloud.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        int width = binaryBitmap.getWidth() / 2;
        int height = binaryBitmap.getHeight() / 2;
        try {
            return this.delegate.decode(binaryBitmap.crop(0, 0, width, height), map);
        } catch (NotFoundException unused) {
            try {
                return this.delegate.decode(binaryBitmap.crop(width, 0, width, height), map);
            } catch (NotFoundException unused2) {
                try {
                    return this.delegate.decode(binaryBitmap.crop(0, height, width, height), map);
                } catch (NotFoundException unused3) {
                    try {
                        return this.delegate.decode(binaryBitmap.crop(width, height, width, height), map);
                    } catch (NotFoundException unused4) {
                        return this.delegate.decode(binaryBitmap.crop(width / 2, height / 2, width, height), map);
                    }
                }
            }
        }
    }

    @Override // com.dcloud.zxing.Reader
    public void reset() {
        this.delegate.reset();
    }
}
