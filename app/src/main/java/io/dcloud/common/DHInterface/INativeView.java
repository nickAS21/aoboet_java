package io.dcloud.common.DHInterface;

import android.view.View;

import org.json.JSONObject;

/* loaded from: classes.dex */
public interface INativeView {
    int getStyleLeft();

    int getStyleWidth();

    String getViewId();

    String getViewUUId();

    View obtanMainView();

    void setStyleLeft(int i);

    JSONObject toJSON();
}
