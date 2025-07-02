package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IContainerView;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.DHInterface.ITypeofAble;

/* loaded from: classes.dex */
public abstract class AdaContainerFrameItem extends AdaFrameItem implements IContainerView {
    private boolean isITypeofAble;
    public ArrayList<AdaFrameItem> mChildArrayList;
    public ArrayList<INativeView> mChildNativeViewList;

    /* JADX INFO: Access modifiers changed from: protected */
    public AdaContainerFrameItem(Context context) {
        super(context);
        this.isITypeofAble = false;
        this.mChildArrayList = null;
        this.mChildNativeViewList = null;
        this.mChildArrayList = new ArrayList<>(1);
    }

    public boolean checkITypeofAble() {
        boolean z = this.isITypeofAble;
        if (z) {
            return z;
        }
        Iterator<AdaFrameItem> it = this.mChildArrayList.iterator();
        while (it.hasNext()) {
            AdaFrameItem next = it.next();
            if (next instanceof AdaContainerFrameItem) {
                return ((AdaContainerFrameItem) next).checkITypeofAble();
            }
        }
        return false;
    }

    public void addNativeViewChild(INativeView iNativeView) {
        if (this.mChildNativeViewList == null) {
            this.mChildNativeViewList = new ArrayList<>();
        }
        if (this.mChildNativeViewList.contains(iNativeView)) {
            return;
        }
        this.mChildNativeViewList.add(iNativeView);
    }

    public void removeNativeViewChild(INativeView iNativeView) {
        ArrayList<INativeView> arrayList = this.mChildNativeViewList;
        if (arrayList == null || !arrayList.contains(iNativeView)) {
            return;
        }
        this.mChildNativeViewList.remove(iNativeView);
    }

    public ArrayList<INativeView> getChildNativeViewList() {
        return this.mChildNativeViewList;
    }

    public ViewGroup obtainMainViewGroup() {
        return (ViewGroup) this.mViewImpl;
    }

    @Override // io.dcloud.common.DHInterface.IContainerView
    public void addFrameItem(AdaFrameItem adaFrameItem, ViewGroup.LayoutParams layoutParams) {
        addFrameItem(adaFrameItem, -1, layoutParams);
    }

    @Override // io.dcloud.common.DHInterface.IContainerView
    public void addFrameItem(AdaFrameItem adaFrameItem) {
        addFrameItem(adaFrameItem, -1);
    }

    public void addFrameItem(AdaFrameItem adaFrameItem, int i, ViewGroup.LayoutParams layoutParams) {
        if (adaFrameItem instanceof ITypeofAble) {
            this.isITypeofAble = true;
        }
        if (this.mViewImpl instanceof ViewGroup) {
            View obtainMainView = adaFrameItem.obtainMainView();
            ViewParent parent = obtainMainView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(obtainMainView);
                this.mChildArrayList.remove(adaFrameItem);
            }
            ((ViewGroup) this.mViewImpl).addView(obtainMainView, i, layoutParams);
            if (i < 0 || i > this.mChildArrayList.size()) {
                i = this.mChildArrayList.size();
            }
            this.mChildArrayList.add(i, adaFrameItem);
        }
    }

    public void addFrameItem(AdaFrameItem adaFrameItem, int i) {
        ViewGroup.LayoutParams layoutParams = adaFrameItem.obtainMainView().getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-1, -1);
        }
        addFrameItem(adaFrameItem, i, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void onResize() {
        super.onResize();
        Iterator<AdaFrameItem> it = this.mChildArrayList.iterator();
        while (it.hasNext()) {
            it.next().onResize();
        }
    }

    @Override // io.dcloud.common.DHInterface.IContainerView
    public void removeFrameItem(AdaFrameItem adaFrameItem) {
        if (adaFrameItem instanceof ITypeofAble) {
            this.isITypeofAble = false;
        }
        if (this.mViewImpl != null) {
            ((ViewGroup) this.mViewImpl).removeView(adaFrameItem.obtainMainView());
            this.mChildArrayList.remove(adaFrameItem);
        }
    }

    @Override // io.dcloud.common.DHInterface.IContainerView
    public void removeAllFrameItem() {
        if (this.mViewImpl != null) {
            ((ViewGroup) this.mViewImpl).removeAllViews();
            clearView();
        }
        this.isITypeofAble = false;
    }

    public ArrayList<AdaFrameItem> getChilds() {
        return this.mChildArrayList;
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void onPopFromStack(boolean z) {
        super.onPopFromStack(z);
        ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
        if (arrayList != null) {
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onPopFromStack(z);
            }
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void onPushToStack(boolean z) {
        super.onPushToStack(z);
        ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
        if (arrayList != null) {
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onPushToStack(z);
            }
        }
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public boolean onDispose() {
        boolean onDispose = super.onDispose();
        ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
        if (arrayList != null) {
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                onDispose |= it.next().onDispose();
            }
        }
        return onDispose;
    }

    @Override // io.dcloud.common.adapter.ui.AdaFrameItem
    public void dispose() {
        super.dispose();
        clearView();
    }

    public void clearView() {
        if ((this.mViewImpl instanceof ViewGroup) && this.mViewImpl != null) {
            ((ViewGroup) this.mViewImpl).removeAllViews();
        }
        ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
        if (arrayList != null) {
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().dispose();
            }
            this.mChildArrayList.clear();
        }
        this.isITypeofAble = false;
    }
}
