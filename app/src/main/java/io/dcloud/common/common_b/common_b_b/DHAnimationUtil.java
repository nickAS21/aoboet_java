package io.dcloud.common.common_b.common_b_b;

import io.dcloud.common.adapter.ui.AdaFrameItem;

/* compiled from: DHAnimationUtil.java */
/* loaded from: classes.dex old b */
final class DHAnimationUtil {
    public static void a(int i, AdaFrameItem... adaFrameItemArr) {
        if (adaFrameItemArr != null) {
            for (AdaFrameItem adaFrameItem : adaFrameItemArr) {
                if (adaFrameItem != null) {
                    adaFrameItem.startAnimator(i);
                }
            }
        }
    }
}
