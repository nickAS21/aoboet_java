package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/* loaded from: classes.dex */
public class HeaderLoadingLayout extends LoadingLayout {
    private static final int ROTATE_ANIM_DURATION = 150;

    private void init(Context context) {
    }

    @Override // io.dcloud.common.adapter.ui.fresh.LoadingLayout
    protected void onPullToRefresh() {
    }

    @Override // io.dcloud.common.adapter.ui.fresh.LoadingLayout
    protected void onRefreshing() {
    }

    @Override // io.dcloud.common.adapter.ui.fresh.LoadingLayout
    protected void onReleaseToRefresh() {
    }

    @Override // io.dcloud.common.adapter.ui.fresh.LoadingLayout
    protected void onReset() {
    }

    @Override // io.dcloud.common.adapter.ui.fresh.LoadingLayout
    public void setLastUpdatedLabel(CharSequence charSequence) {
    }

    public HeaderLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    public HeaderLoadingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    @Override // io.dcloud.common.adapter.ui.fresh.LoadingLayout, io.dcloud.common.adapter.ui.fresh.ILoadingLayout
    public int getContentSize() {
        return (int) (getResources().getDisplayMetrics().density * 100.0f);
    }

    @Override // io.dcloud.common.adapter.ui.fresh.LoadingLayout
    protected View createLoadingView(Context context, AttributeSet attributeSet) {
        return new TextView(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.dcloud.common.adapter.ui.fresh.LoadingLayout
    public void onStateChanged(ILoadingLayout.State state, ILoadingLayout.State state2) {
        super.onStateChanged(state, state2);
    }
}
