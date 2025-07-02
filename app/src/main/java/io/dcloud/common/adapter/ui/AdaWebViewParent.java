package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.dcloud.android.v4.widget.SwipeRefreshLayout;
import com.dcloud.android.widget.AbsoluteLayout;

import org.json.JSONObject;

import io.dcloud.common.adapter.ui.fresh.PullToRefreshBase;
import io.dcloud.common.adapter.ui.fresh.PullToRefreshWebView;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;

/* loaded from: classes.dex */
public class AdaWebViewParent extends AdaContainerFrameItem {
    AdaWebview mAdaWebview;
    PullToRefreshWebViewExt mPullReFreshViewImpl;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AdaWebViewParent(Context context) {
        super(context);
        this.mPullReFreshViewImpl = null;
        this.mAdaWebview = null;
        PullToRefreshWebViewExt pullToRefreshWebViewExt = new PullToRefreshWebViewExt(context);
        this.mPullReFreshViewImpl = pullToRefreshWebViewExt;
        pullToRefreshWebViewExt.setPullLoadEnabled(false);
        this.mPullReFreshViewImpl.setInterceptTouchEventEnabled(false);
        setMainView(this.mPullReFreshViewImpl);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fillsWithWebview(AdaWebview adaWebview) {
        this.mAdaWebview = adaWebview;
        this.mPullReFreshViewImpl.setRefreshableView((WebView) adaWebview.obtainMainView());
        this.mPullReFreshViewImpl.addRefreshableView((WebView) adaWebview.obtainMainView());
        this.mPullReFreshViewImpl.setAppId(this.mAdaWebview.obtainApp().obtainAppId());
        getChilds().add(adaWebview);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void parsePullToReFresh(JSONObject jSONObject) {
        AdaFrameView adaFrameView = (AdaFrameView) this.mAdaWebview.obtainFrameView();
        if (adaFrameView.mBounceView != null) {
            return;
        }
        boolean parseBoolean = Boolean.parseBoolean(JSONUtil.getString(jSONObject, AbsoluteConst.PULL_REFRESH_SUPPORT));
        String optString = jSONObject != null ? jSONObject.optString("style", "default") : "default";
        if (parseBoolean) {
            try {
                if ("circle".equals(optString)) {
                    if (adaFrameView.mCircleRefreshView == null) {
                        adaFrameView.mCircleRefreshView = new SwipeRefreshLayout(this.mAdaWebview.getContext(), null, false);
                        adaFrameView.mCircleRefreshView.onInit(this.mAdaWebview.obtainWebview(), this.mAdaWebview.mWebViewImpl);
                        adaFrameView.mCircleRefreshView.parseData(jSONObject, adaFrameView.mViewOptions.width, adaFrameView.mViewOptions.height, this.mAdaWebview.getScale());
                    }
                    adaFrameView.mCircleRefreshView.setRefreshEnable(true);
                    if (adaFrameView.mRefreshView != null) {
                        this.mPullReFreshViewImpl.setInterceptTouchEventEnabled(false);
                        return;
                    }
                    return;
                }
                if (adaFrameView.mRefreshView == null) {
                    adaFrameView.mRefreshView = new RefreshView(adaFrameView, this.mAdaWebview);
                }
                if (adaFrameView.mCircleRefreshView != null) {
                    adaFrameView.mCircleRefreshView.setRefreshEnable(false);
                }
                adaFrameView.mRefreshView.parseJsonOption(jSONObject);
                initPullView(adaFrameView.mRefreshView, adaFrameView.mRefreshView.maxPullHeight, adaFrameView.mRefreshView.changeStateHeight);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.mPullReFreshViewImpl.setInterceptTouchEventEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endPullRefresh() {
        if (((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView != null && ((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView.isRefreshEnable()) {
            ((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView.endRefresh();
        } else {
            this.mPullReFreshViewImpl.onPullDownRefreshComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void beginPullRefresh() {
        if (((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView != null && ((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView.isRefreshEnable()) {
            ((AdaFrameView) this.mAdaWebview.obtainFrameView()).mCircleRefreshView.beginRefresh();
        } else {
            this.mPullReFreshViewImpl.beginPullRefresh();
        }
    }

    public void reInit() {
        this.mPullReFreshViewImpl.refreshLoadingViewsSize();
    }

    private void initPullView(PullToRefreshBase.OnStateChangeListener onStateChangeListener, int i, int i2) {
        Logger.d(Logger.VIEW_VISIBLE_TAG, "AdaWebViewParent.initPullView changeStateHeight=" + i2);
        this.mPullReFreshViewImpl.setInterceptTouchEventEnabled(true);
        this.mPullReFreshViewImpl.setOnStateChangeListener(onStateChangeListener);
        this.mPullReFreshViewImpl.init(getContext());
        this.mPullReFreshViewImpl.setHeaderHeight(i2 > i ? i : i2);
        PullToRefreshWebViewExt pullToRefreshWebViewExt = this.mPullReFreshViewImpl;
        if (i <= i2) {
            i = i2;
        }
        pullToRefreshWebViewExt.setHeaderPullDownMaxHeight(i);
        this.mAdaWebview.setWebviewProperty(AbsoluteConst.JSON_KEY_BOUNCE, "none");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void parseBounce(JSONObject jSONObject) {
        int i;
        int i2;
        AdaFrameView adaFrameView = (AdaFrameView) this.mAdaWebview.obtainFrameView();
        if (adaFrameView.mRefreshView != null) {
            return;
        }
        if (adaFrameView.mBounceView == null) {
            adaFrameView.mBounceView = new BounceView(adaFrameView, this.mAdaWebview);
        }
        int i3 = (this.mAdaWebview.mViewOptions.height / 3) / 2;
        if (jSONObject == null) {
            adaFrameView.mBounceView.mSupports[0] = true;
            int[] iArr = adaFrameView.mBounceView.maxPullHeights;
            int i4 = this.mAdaWebview.mViewOptions.height / 3;
            iArr[0] = i4;
            int[] iArr2 = adaFrameView.mBounceView.changeStateHeights;
            int i5 = adaFrameView.mBounceView.maxPullHeights[0] / 2;
            iArr2[0] = i5;
            i = i4;
            i2 = i5;
        } else {
            adaFrameView.mBounceView.parseJsonOption(jSONObject);
            i = adaFrameView.mBounceView.maxPullHeights[0];
            i2 = adaFrameView.mBounceView.changeStateHeights[0];
        }
        if (adaFrameView.mBounceView.mSupports[0]) {
            initPullView(adaFrameView.mBounceView, i, i2);
        } else {
            this.mPullReFreshViewImpl.setInterceptTouchEventEnabled(false);
        }
        adaFrameView.mBounceView.checkOffset(adaFrameView, this.mPullReFreshViewImpl, jSONObject, i2, i);
        if (!(adaFrameView.obtainMainView() instanceof AbsoluteLayout) || jSONObject.isNull(AbsoluteConst.BOUNCE_SLIDEO_OFFSET)) {
            return;
        }
        ((AbsoluteLayout) adaFrameView.obtainMainView()).initSlideInfo(jSONObject, this.mAdaWebview.getScale(), this.mAdaWebview.getWebView().getWidth());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetBounce() {
        endPullRefresh();
        AdaFrameView adaFrameView = (AdaFrameView) this.mAdaWebview.obtainFrameView();
        if (adaFrameView.obtainMainView() instanceof AbsoluteLayout) {
            ((AbsoluteLayout) adaFrameView.obtainMainView()).reset();
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public void dispose() {
        super.dispose();
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null) {
            adaWebview.dispose();
            this.mAdaWebview = null;
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaContainerFrameItem, io.dcloud.common.adapter.ui.AdaFrameItem
    public void onResize() {
        int i;
        super.onResize();
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null) {
            AdaFrameView adaFrameView = (AdaFrameView) adaWebview.obtainFrameView();
            endPullRefresh();
            int i2 = 0;
            if (adaFrameView.mRefreshView != null) {
                i = adaFrameView.mRefreshView.maxPullHeight;
                i2 = adaFrameView.mRefreshView.changeStateHeight;
            } else if (adaFrameView.mBounceView != null) {
                i = adaFrameView.mBounceView.maxPullHeights[0];
                i2 = adaFrameView.mBounceView.changeStateHeights[0];
            } else {
                if (adaFrameView.mCircleRefreshView != null && adaFrameView.mCircleRefreshView.isRefreshEnable()) {
                    adaFrameView.mCircleRefreshView.onResize(adaFrameView.mViewOptions.width, adaFrameView.mViewOptions.height, this.mAdaWebview.getScale());
                }
                i = 0;
            }
            if (i2 != 0 && i != 0) {
                this.mPullReFreshViewImpl.setHeaderHeight(i2 > i ? i : i2);
                PullToRefreshWebViewExt pullToRefreshWebViewExt = this.mPullReFreshViewImpl;
                if (i > i2) {
                    i2 = i;
                }
                pullToRefreshWebViewExt.setHeaderPullDownMaxHeight(i2);
                this.mPullReFreshViewImpl.refreshLoadingViewsSize();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class PullToRefreshWebViewExt extends PullToRefreshWebView {
        public PullToRefreshWebViewExt(Context context) {
            super(context);
        }

        @Override // android.view.View
        public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
            super.setLayoutParams(layoutParams);
        }

        @Override // io.dcloud.common.adapter.ui.fresh.PullToRefreshBase, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return (AdaWebViewParent.this.mAdaWebview.obtainMainView().getVisibility() == View.GONE) || super.onTouchEvent(motionEvent);
        }
    }

    public String toString() {
        return this.mAdaWebview.toString();
    }
}
