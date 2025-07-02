package io.dcloud.common.DHInterface;

import android.content.Context;
import android.os.Bundle;

/* loaded from: classes.dex */
public interface IBoot extends ISysEventListener {
    void onPause();

    void onResume();

    void onStart(Context context, Bundle bundle, String[] strArr);

    void onStop();
}
