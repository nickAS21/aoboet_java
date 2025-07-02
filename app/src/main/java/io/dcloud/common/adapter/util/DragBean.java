package io.dcloud.common.adapter.util;

import android.view.View;

import org.json.JSONObject;

import io.dcloud.common.DHInterface.IFrameView;

/* loaded from: classes.dex */
public class DragBean {
    public JSONObject dragCurrentViewOp = null;
    public JSONObject dragBindViewOp = null;
    public IFrameView dragBindWebView = null;
    public IFrameView dragCallBackWebView = null;
    public String dragCbId = null;
    public View nativeView = null;
}
