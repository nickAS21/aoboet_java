package io.dcloud.common.adapter.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class ViewRect {
    public static final int DEFAULT_MARGIN = 0;
    public static byte DOCK_BOTTOM = 6;
    public static byte DOCK_LEFT = 3;
    public static byte DOCK_RIGHT = 4;
    public static byte DOCK_TOP = 5;
    public static byte POSITION_ABSOLUTE = 1;
    public static byte POSITION_DOCK = 2;
    public static byte POSITION_STATIC;
    public int bottom;
    public int height;
    public int left;
    public int right;
    public int top;
    public int width;
    private ViewRect mParentViewRect = null;
    private ViewRect mFrameParentViewRect = null;
    private ArrayList<ViewRect> mRelViewRectDockSet = null;
    public float mWebviewScale = 1.0f;
    public String margin = String.valueOf(0);
    private byte mPosition = POSITION_STATIC;
    private byte mDock = DOCK_TOP;
    public JSONObject mJsonViewOption = JSONUtil.createJSONObject("{}");
    private boolean doUpdate = false;
    public boolean allowUpdate = true;

    public void setUpdateAction(boolean z) {
        this.doUpdate = z;
    }

    public boolean updateViewData(JSONObject jSONObject, int i, int i2) {
        return updateViewData(jSONObject, i, i2, this.mWebviewScale);
    }

    public boolean updateViewData(JSONObject jSONObject, int i, int i2, float f) {
        boolean z;
        int i3;
        boolean z2;
        int i4;
        boolean z3;
        boolean z4;
        JSONObject jSONObject2 = jSONObject;
        JSONObject jSONObject3 = this.mJsonViewOption;
        if (jSONObject3 != null) {
            if (jSONObject3 != null) {
                JSONUtil.combinJSONObject(jSONObject3, jSONObject2);
                jSONObject2 = this.mJsonViewOption;
            } else {
                this.mJsonViewOption = jSONObject2;
            }
            int i5 = this.left;
            int i6 = this.top;
            int i7 = this.width;
            int i8 = this.height;
            boolean z5 = this.doUpdate || !jSONObject2.isNull("left");
            boolean z6 = this.doUpdate || !jSONObject2.isNull("right");
            boolean z7 = this.doUpdate || !jSONObject2.isNull("top");
            if (this.doUpdate || !jSONObject2.isNull(AbsoluteConst.JSON_KEY_WIDTH)) {
                i3 = i8;
                z2 = true;
            } else {
                i3 = i8;
                z2 = false;
            }
            if (this.doUpdate || !jSONObject2.isNull("height")) {
                i4 = i5;
                z3 = true;
            } else {
                i4 = i5;
                z3 = false;
            }
            boolean z8 = z7;
            boolean z9 = this.doUpdate || !jSONObject2.isNull("bottom");
            this.left = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "left"), i, 0, f);
            this.top = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "top"), i2, 0, f);
            this.width = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, AbsoluteConst.JSON_KEY_WIDTH), i, z2 ? this.width : i, f);
            this.height = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "height"), i2, z3 ? this.height : i2, f);
            this.right = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "right"), i, this.right, f);
            this.bottom = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "bottom"), i2, this.bottom, f);
            if (jSONObject2.isNull(AbsoluteConst.JSON_KEY_MARGIN)) {
                z4 = false;
            } else {
                String string = JSONUtil.getString(jSONObject2, AbsoluteConst.JSON_KEY_MARGIN);
                this.margin = string;
                z4 = PdrUtil.isEquals("auto", string);
            }
            if (z5) {
                if (!z2 && z6) {
                    this.width = (i - this.left) - this.right;
                }
            } else if (!z2 && z6) {
                this.left = -this.right;
            } else if (z2 && !z6 && z4) {
                this.left = (i - this.width) / 2;
            } else if (z2 && z6) {
                this.left = (i - this.width) - this.right;
            }
            if (z8) {
                if (!z3 && z9) {
                    this.height = (i2 - this.top) - this.bottom;
                }
            } else if (!z3 && z9) {
                this.top = -this.bottom;
            } else if (z3 && !z9 && z4) {
                this.top = (i2 - this.height) / 2;
            } else if (z3 && z9) {
                this.top = (i2 - this.height) - this.bottom;
            }
            layoutWithRelViewRect();
            z = false;
            layoutDockViewRect(this.mParentViewRect, this, false);
            if (i4 != this.left || i6 != this.top || i3 != this.height || i7 != this.width) {
                return true;
            }
        } else {
            z = false;
        }
        return z;
    }

    public boolean hasRectChanged(ViewRect viewRect, ViewRect viewRect2) {
        return (viewRect.left == viewRect2.left && viewRect.top == viewRect2.top && viewRect.height == viewRect2.height && viewRect.width == viewRect2.width) ? false : true;
    }

    public void putRelViewRect(ViewRect viewRect) {
        if (this.mRelViewRectDockSet == null) {
            this.mRelViewRectDockSet = new ArrayList<>();
        }
        this.mRelViewRectDockSet.add(viewRect);
    }

    public void delRelViewRect(ViewRect viewRect) {
        ArrayList<ViewRect> arrayList = this.mRelViewRectDockSet;
        if (arrayList != null) {
            arrayList.remove(viewRect);
        }
    }

    private void layoutWithRelViewRect() {
        ArrayList<ViewRect> arrayList = this.mRelViewRectDockSet;
        if (arrayList == null) {
            return;
        }
        Iterator<ViewRect> it = arrayList.iterator();
        while (it.hasNext()) {
            layoutDockViewRect(this, it.next());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0040 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0152  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void layoutDockViewRect(io.dcloud.common.adapter.util.ViewRect r16, io.dcloud.common.adapter.util.ViewRect r17, boolean r18) {
        /*
            Method dump skipped, instructions count: 351
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.ViewRect.layoutDockViewRect(io.dcloud.common.adapter.util.ViewRect, io.dcloud.common.adapter.util.ViewRect, boolean):void");
    }

    public static void layoutDockViewRect(ViewRect viewRect, ViewRect viewRect2) {
        layoutDockViewRect(viewRect, viewRect2, true);
    }

    public void updateViewData(ViewRect viewRect) {
        this.mWebviewScale = viewRect.mWebviewScale;
        this.left = viewRect.left;
        this.top = viewRect.top;
        this.width = viewRect.width;
        this.height = viewRect.height;
        this.right = viewRect.right;
        this.bottom = viewRect.bottom;
        updateViewData(viewRect.mJsonViewOption);
    }

    public void onScreenChanged(int i, int i2) {
        updateViewData(this.mJsonViewOption, i, i2);
    }

    public boolean updateViewData(JSONObject jSONObject) {
        ViewRect viewRect = this.mParentViewRect;
        return updateViewData(jSONObject, viewRect.width, viewRect.height);
    }

    public void onScreenChanged() {
        updateViewData(this.mJsonViewOption);
    }

    public void setParentViewRect(ViewRect viewRect) {
        this.mParentViewRect = viewRect;
    }

    public ViewRect getParentViewRect() {
        return this.mParentViewRect;
    }

    public void setFrameParentViewRect(ViewRect viewRect) {
        this.mFrameParentViewRect = viewRect;
        this.mFrameParentViewRect = null;
    }

    public void commitUpdate2JSONObject(boolean z, boolean z2) {
        ViewRect viewRect = this.mFrameParentViewRect;
        int i = viewRect != null ? viewRect.width : this.mParentViewRect.width;
        if (viewRect == null) {
            viewRect = this.mParentViewRect;
        }
        int i2 = viewRect.height;
        checkValueIsPercentage("left", this.left, i, z, z2);
        checkValueIsPercentage("top", this.top, i2, z, z2);
        checkValueIsPercentage(AbsoluteConst.JSON_KEY_WIDTH, this.width, i, z, z2);
        checkValueIsPercentage("height", this.height, i2, z, z2);
        checkValueIsPercentage("right", this.right, i, z, z2);
        checkValueIsPercentage("bottom", this.bottom, i2, z, z2);
    }

    public void commitUpdate2JSONObject() {
        commitUpdate2JSONObject(false, false);
    }

    public void checkValueIsPercentage(String str, int i, int i2, boolean z, boolean z2) {
        try {
            if (!this.mJsonViewOption.isNull(str) || z) {
                if ((this.mJsonViewOption.isNull(str) || this.mJsonViewOption.getString(str).indexOf("%") < 0) && !z2) {
                    this.mJsonViewOption.put(str, i / this.mWebviewScale);
                } else if (i2 > 0) {
                    this.mJsonViewOption.put(str, ((i * 100) / i2) + "%");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
