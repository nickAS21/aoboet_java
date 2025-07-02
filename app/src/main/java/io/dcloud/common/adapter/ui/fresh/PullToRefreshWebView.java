package io.dcloud.common.adapter.ui.fresh;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;

import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.BaseInfo;

/* loaded from: classes.dex */
public class PullToRefreshWebView extends PullToRefreshBase<WebView> {
    public PullToRefreshWebView(Context context) {
        super(context);
    }

    @Override // io.dcloud.common.adapter.ui.fresh.PullToRefreshBase
    protected boolean isReadyForPullDown() {
        Object invokeMethod;
        return (!BaseInfo.isShowTitleBar(getContext()) || ((invokeMethod = PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "isTitlebarVisible", null, new Class[]{Activity.class, String.class}, new Object[]{(Activity) getContext(), getAppId()})) != null && ((Boolean) invokeMethod).booleanValue())) && ((WebView) this.mRefreshableView).getScrollY() == 0;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.PullToRefreshBase
    protected boolean isReadyForPullUp() {
        WebView webView = (WebView) this.mRefreshableView;
        float contentHeight = (float) webView.getContentHeight() * webView.getScale();
        float webViewHeight = (float) webView.getHeight();
        float scrollY = (float) webView.getScrollY();

        return scrollY >= (float) Math.floor(contentHeight - webViewHeight);
    }

    @Override // android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
    }
}
