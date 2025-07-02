package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;

/* loaded from: classes.dex */
public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
    private static final float OFFSET_RADIO = 2.5f;
    private static final int SCROLL_DURATION = 150;
    final int DOWN;
    final int LEFT;
    final int RIGHT;
    final int UP;
    private String mAppId;
    boolean mBeginPullRefresh;
    private boolean mCanDoPullDownEvent;
    private int mFooterHeight;
    private LoadingLayout mFooterLayout;
    private int mHeaderHeight;
    private LoadingLayout mHeaderLayout;
    private int mHeaderPullDownMaxHeight;
    private boolean mInterceptEventEnable;
    private boolean mIsHandledTouchEvent;
    private float mLastMotionX;
    private float mLastMotionY;
    float mLastMotionY_pullup;
    OnPullUpListener mOnPullUpListener;
    OnStateChangeListener mOnStateChangeListener;
    private ILoadingLayout.State mPullDownState;
    private boolean mPullLoadEnabled;
    private boolean mPullRefreshEnabled;
    private ILoadingLayout.State mPullUpState;
    private OnRefreshListener<T> mRefreshListener;
    T mRefreshableView;
    private boolean mScrollLoadEnabled;
    private PullToRefreshBase<T>.SmoothScrollRunnable mSmoothScrollRunnable;
    private int mTouchSlop;

    /* loaded from: classes.dex */
    public interface OnPullUpListener {
        void onPlusScrollBottom();
    }

    /* loaded from: classes.dex */
    public interface OnRefreshListener<V extends View> {
        void onPullDownToRefresh(PullToRefreshBase<V> pullToRefreshBase);

        void onPullUpToRefresh(PullToRefreshBase<V> pullToRefreshBase);
    }

    /* loaded from: classes.dex */
    public interface OnStateChangeListener {
        void onStateChanged(ILoadingLayout.State state, boolean z);
    }

    private int getDirectionByAngle(double d) {
        if (d < -45.0d && d > -135.0d) {
            return 0;
        }
        if (d >= 45.0d && d < 135.0d) {
            return 1;
        }
        if (d >= 135.0d || d <= -135.0d) {
            return 2;
        }
        return (d < -45.0d || d > 45.0d) ? -1 : 3;
    }

    protected LoadingLayout createFooterLoadingLayout(Context context) {
        return null;
    }

    protected long getSmoothScrollDuration() {
        return 150L;
    }

    protected abstract boolean isReadyForPullDown();

    protected abstract boolean isReadyForPullUp();

    public void setAppId(String str) {
        this.mAppId = str;
    }

    public String getAppId() {
        return this.mAppId;
    }

    public PullToRefreshBase(Context context) {
        super(context);
        this.mLastMotionY = -1.0f;
        this.mLastMotionX = -1.0f;
        this.mCanDoPullDownEvent = false;
        this.mPullRefreshEnabled = true;
        this.mPullLoadEnabled = false;
        this.mScrollLoadEnabled = false;
        this.mInterceptEventEnable = true;
        this.mIsHandledTouchEvent = false;
        this.mPullDownState = ILoadingLayout.State.NONE;
        this.mPullUpState = ILoadingLayout.State.NONE;
        this.mLastMotionY_pullup = -1.0f;
        this.UP = 0;
        this.DOWN = 1;
        this.LEFT = 2;
        this.RIGHT = 3;
        this.mBeginPullRefresh = false;
    }

    public void init(Context context) {
        setOrientation(1);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mHeaderLayout = createHeaderLoadingLayout(context);
        this.mFooterLayout = createFooterLoadingLayout(context);
        addHeaderAndFooter(context);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                PullToRefreshBase.this.refreshLoadingViewsSize();
                PullToRefreshBase.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        smoothScrollTo(0);
    }

    public void setHeaderHeight(int i) {
        this.mHeaderHeight = i;
    }

    public void setHeaderPullDownMaxHeight(int i) {
        this.mHeaderPullDownMaxHeight = i;
    }

    public void refreshLoadingViewsSize() {
        int i = this.mHeaderHeight;
        LoadingLayout loadingLayout = this.mFooterLayout;
        int contentSize = loadingLayout != null ? loadingLayout.getContentSize() : 0;
        if (i < 0) {
            i = 0;
        }
        if (contentSize < 0) {
            contentSize = 0;
        }
        this.mHeaderHeight = i;
        this.mFooterHeight = contentSize;
        LoadingLayout loadingLayout2 = this.mHeaderLayout;
        int measuredHeight = loadingLayout2 != null ? loadingLayout2.getMeasuredHeight() : 0;
        Logger.d(Logger.VIEW_VISIBLE_TAG, "PullToRefreshBase.refreshLoadingViewsSize mHeaderHeight=" + this.mHeaderHeight + ";headerHeight=" + measuredHeight);
        LoadingLayout loadingLayout3 = this.mFooterLayout;
        int measuredHeight2 = loadingLayout3 != null ? loadingLayout3.getMeasuredHeight() : 0;
        if (measuredHeight2 == 0) {
            measuredHeight2 = this.mFooterHeight;
        }
        int paddingLeft = getPaddingLeft();
        getPaddingTop();
        int paddingRight = getPaddingRight();
        getPaddingBottom();
        setPadding(paddingLeft, -measuredHeight, paddingRight, -measuredHeight2);
    }

    @Override // android.view.View
    protected final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        refreshLoadingViewsSize();
        refreshRefreshableViewSize(i, i2);
        post(new Runnable() { // from class: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.2
            @Override // java.lang.Runnable
            public void run() {
                PullToRefreshBase.this.requestLayout();
            }
        });
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i) {
        if (1 != i) {
            throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
        }
        super.setOrientation(i);
    }

    private boolean handlePullUpEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 1) {
            float y = motionEvent.getY() - this.mLastMotionY_pullup;
            this.mLastMotionY_pullup = y;
            if (y < -3.0f && isReadyForPullUp()) {
                this.mOnPullUpListener.onPlusScrollBottom();
                return false;
            }
        } else if (action == 0) {
            this.mLastMotionY_pullup = motionEvent.getY();
        }
        return false;
    }

    private double getAngle(float f, float f2, float f3, float f4) {
        return (Math.atan2(f4 - f2, f3 - f) * 180.0d) / 3.141592653589793d;
    }

    private boolean canDoPullDownEvent(float f, float f2) {
        float f3 = this.mLastMotionY;
        if (f2 < f3) {
            return true;
        }
        if (!this.mCanDoPullDownEvent) {
            this.mCanDoPullDownEvent = 1 == getDirectionByAngle(getAngle(this.mLastMotionX, f3, f, f2));
        }
        return this.mCanDoPullDownEvent;
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (BaseInfo.isShowTitleBar(getContext()) && (isPullLoadEnabled() || isPullRefreshEnabled())) {
            this.mRefreshableView.onTouchEvent(motionEvent);
        }
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }
        if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 3 || action == 1) {
            this.mIsHandledTouchEvent = false;
            this.mCanDoPullDownEvent = false;
            return false;
        }
        if (action != 0 && this.mIsHandledTouchEvent) {
            return true;
        }
        if (action == 0) {
            this.mLastMotionY = motionEvent.getY();
            this.mLastMotionX = motionEvent.getX();
            this.mIsHandledTouchEvent = false;
            this.mCanDoPullDownEvent = false;
        } else if (action == 2 && canDoPullDownEvent(motionEvent.getX(), motionEvent.getY())) {
            float y = motionEvent.getY() - this.mLastMotionY;
            if (Math.abs(y) > this.mTouchSlop || isPullRefreshing() || !isPullLoading()) {
                this.mLastMotionY = motionEvent.getY();
                if (isPullRefreshEnabled() && isReadyForPullDown()) {
                    z = Math.abs(getScrollYValue()) > 0 || y > 0.5f;
                    this.mIsHandledTouchEvent = z;
                    if (z) {
                        this.mRefreshableView.onTouchEvent(motionEvent);
                        requestDisallowInterceptTouchEvent(true);
                    }
                } else if (isPullLoadEnabled() && isReadyForPullUp()) {
                    z = Math.abs(getScrollYValue()) > 0 || y < -0.5f;
                    this.mIsHandledTouchEvent = z;
                    if (z) {
                        requestDisallowInterceptTouchEvent(true);
                    }
                }
            }
        }
        boolean z2 = this.mIsHandledTouchEvent;
        return z2 ? z2 : super.onTouchEvent(motionEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0029, code lost:
    
        if (r0 != 3) goto L51;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            android.content.Context r0 = r4.getContext()
            boolean r0 = io.dcloud.common.util.BaseInfo.isShowTitleBar(r0)
            if (r0 == 0) goto L1b
            boolean r0 = r4.isPullLoadEnabled()
            if (r0 != 0) goto L16
            boolean r0 = r4.isPullRefreshEnabled()
            if (r0 == 0) goto L1b
        L16:
            T extends android.view.View r0 = r4.mRefreshableView
            r0.onTouchEvent(r5)
        L1b:
            int r0 = r5.getAction()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto La0
            if (r0 == r1) goto L61
            r3 = 2
            if (r0 == r3) goto L2d
            r5 = 3
            if (r0 == r5) goto L61
            goto La8
        L2d:
            float r0 = r5.getY()
            float r3 = r4.mLastMotionY
            float r0 = r0 - r3
            float r5 = r5.getY()
            r4.mLastMotionY = r5
            boolean r5 = r4.isPullRefreshEnabled()
            r3 = 1075838976(0x40200000, float:2.5)
            if (r5 == 0) goto L4d
            boolean r5 = r4.isReadyForPullDown()
            if (r5 == 0) goto L4d
            float r0 = r0 / r3
            r4.pullHeaderLayout(r0)
            goto La9
        L4d:
            boolean r5 = r4.isPullLoadEnabled()
            if (r5 == 0) goto L5e
            boolean r5 = r4.isReadyForPullUp()
            if (r5 == 0) goto L5e
            float r0 = r0 / r3
            r4.pullFooterLayout(r0)
            goto La9
        L5e:
            r4.mIsHandledTouchEvent = r2
            goto La8
        L61:
            boolean r5 = r4.mIsHandledTouchEvent
            if (r5 == 0) goto L9b
            r4.mIsHandledTouchEvent = r2
            boolean r5 = r4.isReadyForPullDown()
            if (r5 == 0) goto L80
            boolean r5 = r4.mPullRefreshEnabled
            if (r5 == 0) goto L7b
            io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r5 = r4.mPullDownState
            io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r0 = io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State.RELEASE_TO_REFRESH
            if (r5 != r0) goto L7b
            r4.startRefreshing()
            goto L7c
        L7b:
            r1 = r2
        L7c:
            r4.resetHeaderLayout()
            goto L9c
        L80:
            boolean r5 = r4.isReadyForPullUp()
            if (r5 == 0) goto L9b
            boolean r5 = r4.isPullLoadEnabled()
            if (r5 == 0) goto L96
            io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r5 = r4.mPullUpState
            io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r0 = io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State.RELEASE_TO_REFRESH
            if (r5 != r0) goto L96
            r4.startLoading()
            goto L97
        L96:
            r1 = r2
        L97:
            r4.resetFooterLayout()
            goto L9c
        L9b:
            r1 = r2
        L9c:
            r4.requestDisallowInterceptTouchEvent(r2)
            goto La9
        La0:
            float r5 = r5.getY()
            r4.mLastMotionY = r5
            r4.mIsHandledTouchEvent = r2
        La8:
            r1 = r2
        La9:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public void setPullRefreshEnabled(boolean z) {
        this.mPullRefreshEnabled = z;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public void setPullLoadEnabled(boolean z) {
        this.mPullLoadEnabled = z;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public void setScrollLoadEnabled(boolean z) {
        this.mScrollLoadEnabled = z;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public boolean isPullRefreshEnabled() {
        return this.mPullRefreshEnabled && this.mHeaderLayout != null;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public boolean isPullLoadEnabled() {
        return this.mPullLoadEnabled && this.mFooterLayout != null;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public boolean isScrollLoadEnabled() {
        return this.mScrollLoadEnabled;
    }

    public void setOnOnPullUpListener(OnPullUpListener onPullUpListener) {
        this.mOnPullUpListener = onPullUpListener;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public void setOnRefreshListener(OnRefreshListener<T> onRefreshListener) {
        this.mRefreshListener = onRefreshListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
    }

    public void beginPullRefresh() {
        if (this.mBeginPullRefresh) {
            return;
        }
        postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.3
            int deltaY = 0;

            @Override // java.lang.Runnable
            public void run() {
                int abs = Math.abs(PullToRefreshBase.this.getScrollYValue());
                if (PullToRefreshBase.this.isPullRefreshEnabled() && PullToRefreshBase.this.isReadyForPullDown()) {
                    if (abs < PullToRefreshBase.this.mHeaderHeight) {
                        PullToRefreshBase.this.pullHeaderLayout(this.deltaY / PullToRefreshBase.OFFSET_RADIO);
                        this.deltaY += 3;
                        PullToRefreshBase.this.postDelayed(this, 5L);
                    } else {
                        PullToRefreshBase.this.startRefreshing();
                        PullToRefreshBase.this.mBeginPullRefresh = false;
                    }
                }
            }
        }, 5L);
        this.mBeginPullRefresh = true;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public void onPullDownRefreshComplete() {
        if (isPullRefreshing()) {
            this.mPullDownState = ILoadingLayout.State.RESET;
            onStateChanged(ILoadingLayout.State.RESET, true);
            postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.4
                @Override // java.lang.Runnable
                public void run() {
                    PullToRefreshBase.this.setInterceptTouchEventEnabled(true);
                    PullToRefreshBase.this.mHeaderLayout.setState(ILoadingLayout.State.RESET);
                }
            }, getSmoothScrollDuration());
            resetHeaderLayout();
            setInterceptTouchEventEnabled(false);
        }
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public void onPullUpRefreshComplete() {
        if (isPullLoading()) {
            this.mPullUpState = ILoadingLayout.State.RESET;
            onStateChanged(ILoadingLayout.State.RESET, false);
            postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.5
                @Override // java.lang.Runnable
                public void run() {
                    PullToRefreshBase.this.setInterceptTouchEventEnabled(true);
                    PullToRefreshBase.this.mFooterLayout.setState(ILoadingLayout.State.RESET);
                }
            }, getSmoothScrollDuration());
            resetFooterLayout();
            setInterceptTouchEventEnabled(false);
        }
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public T getRefreshableView() {
        return this.mRefreshableView;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public LoadingLayout getHeaderLoadingLayout() {
        return this.mHeaderLayout;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public LoadingLayout getFooterLoadingLayout() {
        return this.mFooterLayout;
    }

    @Override // io.dcloud.common.adapter.ui.fresh.IPullToRefresh
    public void setLastUpdatedLabel(CharSequence charSequence) {
        LoadingLayout loadingLayout = this.mHeaderLayout;
        if (loadingLayout != null) {
            loadingLayout.setLastUpdatedLabel(charSequence);
        }
        LoadingLayout loadingLayout2 = this.mFooterLayout;
        if (loadingLayout2 != null) {
            loadingLayout2.setLastUpdatedLabel(charSequence);
        }
    }

    public void doPullRefreshing(final boolean z, long j) {
        postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.6
            @Override // java.lang.Runnable
            public void run() {
                int i = -PullToRefreshBase.this.mHeaderHeight;
                int i2 = z ? PullToRefreshBase.SCROLL_DURATION : 0;
                PullToRefreshBase.this.startRefreshing();
                PullToRefreshBase.this.smoothScrollTo(i, i2, 0L);
            }
        }, j);
    }

    public void setRefreshableView(T t) {
        this.mRefreshableView = t;
    }

    protected LoadingLayout createHeaderLoadingLayout(Context context) {
        LoadingLayout loadingLayout = this.mHeaderLayout;
        return loadingLayout == null ? new HeaderLoadingLayout(context) : loadingLayout;
    }

    protected void refreshRefreshableViewSize(int i, int i2) {
        T t = this.mRefreshableView;
        if (t != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) t.getLayoutParams();
            if (layoutParams.height != i2) {
                layoutParams.height = i2;
                this.mRefreshableView.requestLayout();
            }
        }
    }

    public void addRefreshableView(T t) {
        addView(t, new LinearLayout.LayoutParams(-1, -1));
    }

    protected void addHeaderAndFooter(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        LoadingLayout loadingLayout = this.mHeaderLayout;
        LoadingLayout loadingLayout2 = this.mFooterLayout;
        if (loadingLayout != null) {
            if (this == loadingLayout.getParent()) {
                removeView(loadingLayout);
            }
            addView(loadingLayout, 0, layoutParams);
        }
        if (loadingLayout2 != null) {
            if (this == loadingLayout2.getParent()) {
                removeView(loadingLayout2);
            }
            addView(loadingLayout2, -1, layoutParams);
        }
    }

    protected void pullHeaderLayout(float f) {
        int scrollYValue = getScrollYValue();
        if (f <= 0.0f || Math.abs(scrollYValue) < this.mHeaderPullDownMaxHeight) {
            if (f < 0.0f && scrollYValue - f >= 0.0f) {
                setScrollTo(0, 0);
                return;
            }
            setScrollBy(0, -((int) f));
            if (this.mHeaderLayout != null && this.mHeaderHeight != 0) {
                this.mHeaderLayout.onPull(Math.abs(getScrollYValue()) / this.mHeaderHeight);
            }
            int abs = Math.abs(getScrollYValue());
            if (!isPullRefreshEnabled() || isPullRefreshing()) {
                return;
            }
            if (abs > this.mHeaderHeight) {
                this.mPullDownState = ILoadingLayout.State.RELEASE_TO_REFRESH;
            } else {
                this.mPullDownState = ILoadingLayout.State.PULL_TO_REFRESH;
            }
            this.mHeaderLayout.setState(this.mPullDownState);
            onStateChanged(this.mPullDownState, true);
        }
    }

    protected void pullFooterLayout(float f) {
        int scrollYValue = getScrollYValue();
        if (f > 0.0f && scrollYValue - f <= 0.0f) {
            setScrollTo(0, 0);
            return;
        }
        setScrollBy(0, -((int) f));
        if (this.mFooterLayout != null && this.mFooterHeight != 0) {
            this.mFooterLayout.onPull(Math.abs(getScrollYValue()) / this.mFooterHeight);
        }
        int abs = Math.abs(getScrollYValue());
        if (!isPullLoadEnabled() || isPullLoading()) {
            return;
        }
        if (abs > this.mFooterHeight) {
            this.mPullUpState = ILoadingLayout.State.RELEASE_TO_REFRESH;
        } else {
            this.mPullUpState = ILoadingLayout.State.PULL_TO_REFRESH;
        }
        this.mFooterLayout.setState(this.mPullUpState);
        onStateChanged(this.mPullUpState, false);
    }

    protected void resetHeaderLayout() {
        int abs = Math.abs(getScrollYValue());
        boolean isPullRefreshing = isPullRefreshing();
        if (!isPullRefreshing || abs > this.mHeaderHeight) {
            if (isPullRefreshing) {
                smoothScrollTo(-this.mHeaderHeight);
            } else {
                smoothScrollTo(0);
            }
        }
    }

    protected void resetFooterLayout() {
        int abs = Math.abs(getScrollYValue());
        boolean isPullLoading = isPullLoading();
        if (isPullLoading && abs <= this.mFooterHeight) {
            smoothScrollTo(0);
        } else if (isPullLoading) {
            smoothScrollTo(this.mFooterHeight);
        } else {
            smoothScrollTo(0);
        }
    }

    protected boolean isPullRefreshing() {
        return this.mPullDownState == ILoadingLayout.State.REFRESHING;
    }

    protected boolean isPullLoading() {
        return this.mPullUpState == ILoadingLayout.State.REFRESHING;
    }

    protected void startRefreshing() {
        if (isPullRefreshing()) {
            return;
        }
        this.mPullDownState = ILoadingLayout.State.REFRESHING;
        onStateChanged(ILoadingLayout.State.REFRESHING, true);
        LoadingLayout loadingLayout = this.mHeaderLayout;
        if (loadingLayout != null) {
            loadingLayout.setState(ILoadingLayout.State.REFRESHING);
        }
        if (this.mRefreshListener != null) {
            postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.7
                @Override // java.lang.Runnable
                public void run() {
                    PullToRefreshBase.this.mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
                }
            }, getSmoothScrollDuration());
        }
    }

    protected void startLoading() {
        if (isPullLoading()) {
            return;
        }
        this.mPullUpState = ILoadingLayout.State.REFRESHING;
        onStateChanged(ILoadingLayout.State.REFRESHING, false);
        LoadingLayout loadingLayout = this.mFooterLayout;
        if (loadingLayout != null) {
            loadingLayout.setState(ILoadingLayout.State.REFRESHING);
        }
        if (this.mRefreshListener != null) {
            postDelayed(new Runnable() { // from class: io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.8
                @Override // java.lang.Runnable
                public void run() {
                    PullToRefreshBase.this.mRefreshListener.onPullUpToRefresh(PullToRefreshBase.this);
                }
            }, getSmoothScrollDuration());
        }
    }

    protected void onStateChanged(ILoadingLayout.State state, boolean z) {
        OnStateChangeListener onStateChangeListener = this.mOnStateChangeListener;
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChanged(state, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScrollTo(int i, int i2) {
        scrollTo(i, i2);
    }

    private void setScrollBy(int i, int i2) {
        scrollBy(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getScrollYValue() {
        return getScrollY();
    }

    public void smoothScrollTo(int i) {
        smoothScrollTo(i, getSmoothScrollDuration(), 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void smoothScrollTo(int i, long j, long j2) {
        PullToRefreshBase<T>.SmoothScrollRunnable smoothScrollRunnable = this.mSmoothScrollRunnable;
        if (smoothScrollRunnable != null) {
            smoothScrollRunnable.stop();
        }
        int scrollYValue = getScrollYValue();
        boolean z = scrollYValue != i;
        if (z) {
            this.mSmoothScrollRunnable = new SmoothScrollRunnable(scrollYValue, i, j);
        }
        if (z) {
            if (j2 > 0) {
                postDelayed(this.mSmoothScrollRunnable, j2);
            } else {
                post(this.mSmoothScrollRunnable);
            }
        }
    }

    public void setInterceptTouchEventEnabled(boolean z) {
        this.mInterceptEventEnable = z;
    }

    public boolean isInterceptTouchEventEnabled() {
        return this.mInterceptEventEnable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class SmoothScrollRunnable implements Runnable {
        private final long mDuration;
        private final int mScrollFromY;
        private final int mScrollToY;
        private boolean mContinueRunning = true;
        private long mStartTime = -1;
        private int mCurrentY = -1;
        private final Interpolator mInterpolator = new DecelerateInterpolator();

        public SmoothScrollRunnable(int i, int i2, long j) {
            this.mScrollFromY = i;
            this.mScrollToY = i2;
            this.mDuration = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mDuration <= 0) {
                PullToRefreshBase.this.setScrollTo(0, this.mScrollToY);
                return;
            }
            if (this.mStartTime == -1) {
                this.mStartTime = System.currentTimeMillis();
            } else {
                int round = this.mScrollFromY - Math.round((this.mScrollFromY - this.mScrollToY) * this.mInterpolator.getInterpolation(((float) Math.max(Math.min(((System.currentTimeMillis() - this.mStartTime) * 1000) / this.mDuration, 1000L), 0L)) / 1000.0f));
                this.mCurrentY = round;
                PullToRefreshBase.this.setScrollTo(0, round);
            }
            if (!this.mContinueRunning || this.mScrollToY == this.mCurrentY) {
                return;
            }
            PullToRefreshBase.this.postDelayed(this, 16L);
        }

        public void stop() {
            this.mContinueRunning = false;
            PullToRefreshBase.this.removeCallbacks(this);
        }
    }
}
