package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
    private View mContainer;
    private ILoadingLayout.State mCurState;
    private ILoadingLayout.State mPreState;

    protected abstract View createLoadingView(Context context, AttributeSet attributeSet);

    public abstract int getContentSize();

    protected void init(Context context, AttributeSet attributeSet) {
    }

    protected void onNoMoreData() {
    }

    @Override // io.dcloud.common.adapter.ui.fresh.ILoadingLayout
    public void onPull(float f) {
    }

    protected void onPullToRefresh() {
    }

    protected void onRefreshing() {
    }

    protected void onReleaseToRefresh() {
    }

    protected void onReset() {
    }

    public void setLastUpdatedLabel(CharSequence charSequence) {
    }

    public void setLoadingDrawable(Drawable drawable) {
    }

    public void setPullLabel(CharSequence charSequence) {
    }

    public void setRefreshingLabel(CharSequence charSequence) {
    }

    public void setReleaseLabel(CharSequence charSequence) {
    }

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LoadingLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurState = ILoadingLayout.State.NONE;
        this.mPreState = ILoadingLayout.State.NONE;
        init(context, attributeSet);
    }

    public void show(boolean z) {
        ViewGroup.LayoutParams layoutParams;
        if (z == (getVisibility() == 0) || (layoutParams = this.mContainer.getLayoutParams()) == null) {
            return;
        }
        if (z) {
            layoutParams.height = -2;
        } else {
            layoutParams.height = 0;
        }
        setVisibility(z ? 0 : 4);
    }

    @Override // io.dcloud.common.adapter.ui.fresh.ILoadingLayout
    public void setState(ILoadingLayout.State state) {
        ILoadingLayout.State state2 = this.mCurState;
        if (state2 != state) {
            this.mPreState = state2;
            this.mCurState = state;
            onStateChanged(state, state2);
        }
    }

    @Override // io.dcloud.common.adapter.ui.fresh.ILoadingLayout
    public ILoadingLayout.State getState() {
        return this.mCurState;
    }

    protected ILoadingLayout.State getPreState() {
        return this.mPreState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: io.dcloud.common.adapter.ui.fresh.LoadingLayout$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State;

        static {
            int[] iArr = new int[ILoadingLayout.State.values().length];
            $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State = iArr;
            try {
                iArr[ILoadingLayout.State.RESET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State[ILoadingLayout.State.RELEASE_TO_REFRESH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State[ILoadingLayout.State.PULL_TO_REFRESH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State[ILoadingLayout.State.REFRESHING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State[ILoadingLayout.State.NO_MORE_DATA.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onStateChanged(ILoadingLayout.State state, ILoadingLayout.State state2) {
        int i = AnonymousClass1.$SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State[state.ordinal()];
        if (i == 1) {
            onReset();
            return;
        }
        if (i == 2) {
            onReleaseToRefresh();
            return;
        }
        if (i == 3) {
            onPullToRefresh();
        } else if (i == 4) {
            onRefreshing();
        } else {
            if (i != 5) {
                return;
            }
            onNoMoreData();
        }
    }
}
