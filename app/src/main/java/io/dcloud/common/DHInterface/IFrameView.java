package io.dcloud.common.DHInterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import io.dcloud.common.adapter.ui.AdaWebViewParent;
import io.dcloud.common.adapter.util.ViewOptions;

/* loaded from: classes.dex */
public interface IFrameView extends IContainerView, IFrameViewStatus {
    public static final byte TRANS_CUSTOM = 2;
    public static final byte TRANS_FIRST = 0;
    public static final byte TRANS_SECOND = 1;
    public static final int WIN_LAUNCH_PAGE = 2;
    public static final int WIN_SECOND_PAGE = 4;
    public static final int WIN_TYPE_COMMON = 0;
    public static final int WIN_TYPE_PAGE = 1;
    public static final int WIN_WAP_PAGE = 3;

    void animate(IWebview iWebview, String str, String str2);

    void captureSnapshot(String str, ICallBack iCallBack, ICallBack iCallBack2);

    void clearSnapshot(String str);

    void draw(View view, INativeBitmap iNativeBitmap, boolean z, Rect rect, ICallBack iCallBack, ICallBack iCallBack2);

    IFrameView findPageB();

    Context getContext();

    int getFrameType();

    void interceptTouchEvent(boolean z);

    boolean isWebviewCovered();

    IApp obtainApp();

    View obtainMainView();

    IWebAppRootView obtainWebAppRootView();

    IWebview obtainWebView();

    AdaWebViewParent obtainWebviewParent();

    AbsMgr obtainWindowMgr();

    void popFromViewStack();

    void pushToViewStack();

    void restore();

    void setAccelerationType(String str);

    void setFrameOptions_Animate(ViewOptions viewOptions);

    void setNeedRender(boolean z);

    void setSnapshot(Bitmap bitmap);

    void setVisible(boolean z, boolean z2);

    void transition(byte b);
}
