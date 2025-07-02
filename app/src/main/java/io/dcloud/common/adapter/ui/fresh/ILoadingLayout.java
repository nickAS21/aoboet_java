package io.dcloud.common.adapter.ui.fresh;

/* loaded from: classes.dex */
public interface ILoadingLayout {

    /* loaded from: classes.dex */
    public enum State {
        NONE,
        RESET,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        LOADING,
        NO_MORE_DATA
    }

    int getContentSize();

    State getState();

    void onPull(float f);

    void setState(State state);
}
