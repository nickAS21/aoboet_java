package io.dcloud.common.common_a;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import io.dcloud.common.DHInterface.IAppInfo;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.IWebAppRootView;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

/* compiled from: WebAppInfo.java */
/* loaded from: classes.dex  old e*/
public class WebAppInfo implements IAppInfo {
    Activity Z = null;
    protected IWebAppRootView aa = null;
    private IOnCreateSplashView a = null;
    public int ab = 0;
    public int ac = 0;
    public int ad = 0;
    public int ae = 0;
    public int af = 0;
    protected boolean ag = false;
    private boolean b = false;
    private int c = 0;
    ViewRect ah = new ViewRect();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        this.ac = displayMetrics.heightPixels;
        this.Z = activity;
        this.ah.mJsonViewOption = JSONUtil.createJSONObject("{}");
        this.ad = PdrUtil.parseInt(PlatformUtil.getBundleData(BaseInfo.PDR, "StatusBarHeight"), 0);
        this.ab = displayMetrics.widthPixels;
        this.af = displayMetrics.heightPixels - this.ad;
        Logger.i("WebAppInfo", "init() get sStatusBarHeight=" + this.ad);
    }

    public int checkSelfPermission(String str) {
        Activity activity;
        if (Build.VERSION.SDK_INT < 23 || (activity = this.Z) == null || str == null) {
            return -100;
        }
        return ((Integer) PlatformUtil.invokeMethod(activity.getClass().getName(), "checkSelfPermission", this.Z, new Class[]{str.getClass()}, new Object[]{str})).intValue();
    }

    public void requestPermissions(String[] strArr, int i) {
        Activity activity;
        if (Build.VERSION.SDK_INT < 23 || (activity = this.Z) == null || strArr == null) {
            return;
        }
        PlatformUtil.invokeMethod(activity.getClass().getName(), "requestPermissions", this.Z, new Class[]{strArr.getClass(), Integer.TYPE}, new Object[]{strArr, Integer.valueOf(i)});
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public Activity getActivity() {
        return this.Z;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public ViewRect getAppViewRect() {
        return this.ah;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public IWebAppRootView obtainWebAppRootView() {
        return this.aa;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public void setWebAppRootView(IWebAppRootView iWebAppRootView) {
        this.aa = iWebAppRootView;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public void setFullScreen(boolean z) {
        if (BaseInfo.sGlobalFullScreen != z) {
            this.ag = z;
            Window window = getActivity().getWindow();
            if (z) {
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= 1024;
                window.setAttributes(attributes);
            } else {
                WindowManager.LayoutParams attributes2 = window.getAttributes();
                attributes2.flags &= -1025;
                window.setAttributes(attributes2);
            }
            updateScreenInfo(this.ag ? 2 : 3);
        }
        BaseInfo.sGlobalFullScreen = z;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public boolean isFullScreen() {
        return this.ag;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public void updateScreenInfo(int i) {
        if (!this.ag && this.ad == 0) {
            Rect rect = new Rect();
            this.Z.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int i2 = rect.top;
            this.ad = i2;
            if (i2 > 0) {
                PlatformUtil.setBundleData(BaseInfo.PDR, "StatusBarHeight", String.valueOf(this.ad));
            }
        }
        if (i == 2) {
            DisplayMetrics displayMetrics = this.Z.getResources().getDisplayMetrics();
            this.ab = displayMetrics.widthPixels;
            this.af = displayMetrics.heightPixels;
        } else {
            this.ab = this.aa.obtainMainView().getWidth();
            this.af = this.aa.obtainMainView().getHeight();
        }
        this.ah.onScreenChanged(this.ab, this.af);
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public void setRequestedOrientation(final String str) {
        try {
            MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.a.e.1
                @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                public void execute(Object obj) {
                    if ("landscape".equals(str)) {
                        WebAppInfo.this.Z.setRequestedOrientation(6);
                        return;
                    }
                    if ("landscape-primary".equals(str)) {
                        WebAppInfo.this.Z.setRequestedOrientation(0);
                        return;
                    }
                    if ("landscape-secondary".equals(str)) {
                        WebAppInfo.this.Z.setRequestedOrientation(8);
                        return;
                    }
                    if ("portrait".equals(str)) {
                        WebAppInfo.this.Z.setRequestedOrientation(7);
                        return;
                    }
                    if ("portrait-primary".equals(str)) {
                        WebAppInfo.this.Z.setRequestedOrientation(1);
                    } else if ("portrait-secondary".equals(str)) {
                        WebAppInfo.this.Z.setRequestedOrientation(9);
                    } else {
                        WebAppInfo.this.Z.setRequestedOrientation(4);
                    }
                }
            }, 48L, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public int getRequestedOrientation() {
        return this.Z.getRequestedOrientation();
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public boolean isVerticalScreen() {
        return this.Z.getResources().getConfiguration().orientation == 1;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public void setRequestedOrientation(int i) {
        this.Z.setRequestedOrientation(i);
    }

    @Override // io.dcloud.common.DHInterface.IType_IntValue
    public int getInt(int i) {
        if (i == 0) {
            return this.ab;
        }
        if (i == 1) {
            return this.af;
        }
        if (i != 2) {
            return -1;
        }
        return this.ac;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public void setMaskLayer(boolean z) {
        this.b = z;
        if (z) {
            this.c++;
            return;
        }
        int i = this.c - 1;
        this.c = i;
        if (i < 0) {
            this.c = 0;
        }
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public int getMaskLayerCount() {
        return this.c;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public void clearMaskLayerCount() {
        this.c = 0;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public void setOnCreateSplashView(IOnCreateSplashView iOnCreateSplashView) {
        this.a = iOnCreateSplashView;
    }

    @Override // io.dcloud.common.DHInterface.IAppInfo
    public IOnCreateSplashView getOnCreateSplashView() {
        return this.a;
    }
}
