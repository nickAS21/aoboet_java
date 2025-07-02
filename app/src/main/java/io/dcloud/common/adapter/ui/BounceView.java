package io.dcloud.common.adapter.ui;

import android.text.TextUtils;

import com.dcloud.android.widget.SlideLayout;

import org.json.JSONObject;

import io.dcloud.common.adapter.ui.fresh.ILoadingLayout;
import io.dcloud.common.adapter.ui.fresh.PullToRefreshBase;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class BounceView implements PullToRefreshBase.OnStateChangeListener {
    static final String STATE_CHANGED_TEMPLATE = "{status:'%s'}";
    static final String[] keys = {"top", "left", "right", "bottom"};
    int[] changeStateHeights;
    ILoadingLayout.State mCurState;
    AdaFrameView mFrameView;
    JSONObject mJSONObject;
    String[] mPositions;
    boolean[] mSupports;
    AdaWebview mWebview;
    private float mWebviewScale;
    int[] maxPullHeights;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BounceView(AdaFrameView adaFrameView, AdaWebview adaWebview) {
        String[] strArr = keys;
        this.changeStateHeights = new int[strArr.length];
        this.maxPullHeights = new int[strArr.length];
        this.mPositions = new String[strArr.length];
        this.mSupports = new boolean[strArr.length];
        this.mCurState = null;
        this.mFrameView = adaFrameView;
        this.mWebview = adaWebview;
        this.mWebviewScale = adaWebview.getScaleOfOpenerWebview();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void parseJsonOption(JSONObject jSONObject) {
        try {
            JSONObject combinJSONObject = JSONUtil.combinJSONObject(this.mJSONObject, jSONObject);
            this.mJSONObject = combinJSONObject;
            int i = 0;
            if (!combinJSONObject.isNull("position")) {
                JSONObject jSONObject2 = JSONUtil.getJSONObject(combinJSONObject, "position");
                int i2 = 0;
                while (true) {
                    String[] strArr = keys;
                    if (i2 >= strArr.length) {
                        break;
                    }
                    if (!jSONObject2.isNull(strArr[i2])) {
                        String string = JSONUtil.getString(jSONObject2, strArr[i2]);
                        if ("none".equals(string)) {
                            this.mSupports[i2] = false;
                        } else if ("auto".equals(string)) {
                            this.mSupports[i2] = true;
                            this.maxPullHeights[i2] = this.mWebview.mViewOptions.height / 3;
                            this.changeStateHeights[i2] = this.maxPullHeights[i2] / 2;
                        } else {
                            this.mSupports[i2] = true;
                            this.maxPullHeights[i2] = PdrUtil.convertToScreenInt(string, this.mWebview.mViewOptions.height, this.mWebview.mViewOptions.height / 3, this.mWebviewScale);
                            this.changeStateHeights[i2] = this.maxPullHeights[i2] / 2;
                        }
                    }
                    i2++;
                }
            } else {
                this.mSupports[0] = true;
                this.maxPullHeights[0] = this.mWebview.mViewOptions.height / 3;
                this.changeStateHeights[0] = this.maxPullHeights[0] / 2;
            }
            if (!combinJSONObject.isNull(AbsoluteConst.BOUNCE_CHANGEOFFSET)) {
                JSONObject jSONObject3 = JSONUtil.getJSONObject(combinJSONObject, AbsoluteConst.BOUNCE_CHANGEOFFSET);
                while (true) {
                    String[] strArr2 = keys;
                    if (i >= strArr2.length) {
                        return;
                    }
                    if (!jSONObject3.isNull(strArr2[i])) {
                        this.changeStateHeights[i] = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject3, strArr2[i]), this.mWebview.mViewOptions.height, this.maxPullHeights[i] / 2, this.mWebviewScale);
                    }
                    i++;
                }
            } else {
                this.changeStateHeights[0] = this.maxPullHeights[0] / 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.OnStateChangeListener
    public void onStateChanged(ILoadingLayout.State state, boolean z) {
        boolean z2 = this.mCurState != state;
        this.mCurState = state;
        if (!z2 || state == ILoadingLayout.State.RESET) {
            return;
        }
        if (state == ILoadingLayout.State.PULL_TO_REFRESH) {
            Logger.d("refresh", "BounceView PULL_TO_REFRESH");
            this.mWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_DRAG_BOUNCE, String.format(STATE_CHANGED_TEMPLATE, AbsoluteConst.BOUNCE_BEFORE_CHANGE_OFFSET));
        } else if (state == ILoadingLayout.State.RELEASE_TO_REFRESH) {
            Logger.d("refresh", "BounceView RELEASE_TO_REFRESH");
            this.mWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_DRAG_BOUNCE, String.format(STATE_CHANGED_TEMPLATE, AbsoluteConst.BOUNCE_AFTER_CHANGE_OFFSET));
        } else if (state == ILoadingLayout.State.REFRESHING) {
            Logger.d("refresh", "BounceView REFRESHING");
            this.mWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_DRAG_BOUNCE, String.format(STATE_CHANGED_TEMPLATE, AbsoluteConst.BOUNCE_DRAG_END_AFTER_CHANG_EOFFSET));
        }
    }

    public void onResize() {
        parseJsonOption(this.mJSONObject);
    }

    public void checkOffset(AdaFrameView adaFrameView, final AdaWebViewParent.PullToRefreshWebViewExt pullToRefreshWebViewExt, JSONObject jSONObject, int i, int i2) {
        JSONObject jSONObject2 = JSONUtil.getJSONObject(jSONObject, AbsoluteConst.BOUNCE_OFFSET);
        if (jSONObject2 != null) {
            String string = JSONUtil.getString(jSONObject2, "top");
            String string2 = JSONUtil.getString(jSONObject2, "left");
            String string3 = JSONUtil.getString(jSONObject2, "right");
            if (!TextUtils.isEmpty(string)) {
                int convertToScreenInt = PdrUtil.convertToScreenInt(string, this.mWebview.mViewOptions.height, i2, this.mWebview.getScale());
                if (convertToScreenInt >= i) {
                    if (convertToScreenInt <= i2) {
                        i2 = convertToScreenInt;
                    }
                    pullToRefreshWebViewExt.smoothScrollTo(-i2);
                    pullToRefreshWebViewExt.doPullRefreshing(true, 250L);
                    return;
                }
                pullToRefreshWebViewExt.smoothScrollTo(-convertToScreenInt);
                this.mWebview.obtainWebview().postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.BounceView.1
                    @Override // java.lang.Runnable
                    public void run() {
                        pullToRefreshWebViewExt.smoothScrollTo(0);
                    }
                }, 250L);
                return;
            }
            if (!TextUtils.isEmpty(string3) && (adaFrameView.obtainMainView() instanceof SlideLayout)) {
                ((SlideLayout) adaFrameView.obtainMainView()).setOffset("right", string3, this.mWebview.getScale());
            } else {
                if (TextUtils.isEmpty(string2) || !(adaFrameView.obtainMainView() instanceof SlideLayout)) {
                    return;
                }
                ((SlideLayout) adaFrameView.obtainMainView()).setOffset("left", string2, this.mWebview.getScale());
            }
        }
    }
}
