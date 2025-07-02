package io.src.dcloud.adapter;

import android.app.Activity;
import android.content.Intent;

import io.dcloud.common.DHInterface.IReflectAble;

/* loaded from: classes.dex */
public class DCloudBaseActivity extends Activity implements IReflectAble {
    public Activity that = this;

    public void onNewIntentImpl(Intent intent) {
    }

    @Override // android.app.Activity
    protected final void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onNewIntentImpl(intent);
    }
}
