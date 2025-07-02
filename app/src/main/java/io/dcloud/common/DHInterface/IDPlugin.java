package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.content.Context;

/* loaded from: classes.dex */
public interface IDPlugin extends IBoot, IFeature {
    Activity getDPluginActivity();

    Context getDPluginContext();

    void initDPlugin(Context context, Activity activity);
}
