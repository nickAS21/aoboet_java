package io.dcloud.common.adapter.util;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class ViewOptions extends ViewRect {
    public static final int BG_NONE = -1;
    public HashMap<String, DragBean> dragData;
    public Object mTag;
    public boolean mUseHardwave;
    public String name;
    public JSONObject navigationbar;
    public JSONObject transform;
    public JSONObject transition;
    public boolean scalable = false;
    public String mInjection = AbsoluteConst.TRUE;
    public String mPlusrequire = IApp.ConfigProperty.CONFIG_RUNMODE_NORMAL;
    private String mScrollIndicator = "all";
    public float opacity = -1.0f;
    public int background = -1;
    public int maskColor = -1;
    public String strBackground = null;
    public String errorPage = null;
    public boolean mBounce = false;
    public String mCacheMode = "default";
    public String mVideoFullscree = "auto";
    public String popGesture = "none";
    public String mGeoInject = "none";
    public boolean dragH5NeedTouchEvent = false;

    public ViewOptions() {
        this.mUseHardwave = true;
        this.mUseHardwave = MobilePhoneModel.checkPhoneBanAcceleration(Build.BRAND);
    }

    public boolean hasBackground() {
        return this.background != -1;
    }

    public boolean hasMask() {
        return this.maskColor != -1;
    }

    public boolean isTransparent() {
        return PdrUtil.isEquals("transparent", this.strBackground);
    }

    public boolean hasTransparentValue() {
        if (!isTransparent() && !PdrUtil.checkAlphaTransparent(this.background)) {
            float f = this.opacity;
            if (f < 0.0f || f >= 1.0f) {
                return false;
            }
        }
        return true;
    }

    @Override // io.dcloud.common.adapter.util.ViewRect
    public void updateViewData(ViewRect viewRect) {
        super.updateViewData(viewRect);
    }

    @Override // io.dcloud.common.adapter.util.ViewRect
    public boolean updateViewData(JSONObject jSONObject) {
        boolean updateViewData = super.updateViewData(jSONObject);
        if (jSONObject != null) {
            if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_SCROLLINDICATOR)) {
                this.mScrollIndicator = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_SCROLLINDICATOR);
            }
            if (!jSONObject.isNull("background")) {
                try {
                    String lowerCase = JSONUtil.getString(jSONObject, "background").toLowerCase();
                    this.strBackground = lowerCase;
                    this.background = PdrUtil.stringToColor(lowerCase);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!jSONObject.isNull(AbsoluteConst.JSON_KEY_MASK)) {
                try {
                    this.maskColor = PdrUtil.stringToColor(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_MASK).toLowerCase());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (!jSONObject.isNull(AbsoluteConst.JSON_KEY_CACHEMODE)) {
                this.mCacheMode = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_CACHEMODE);
            }
            this.mUseHardwave = PdrUtil.parseBoolean(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_HARDWARE_ACCELERATED), this.mUseHardwave, false);
            this.opacity = PdrUtil.parseFloat(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_OPACITY), this.opacity);
            this.scalable = PdrUtil.parseBoolean(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_SCALABLE), this.scalable, false);
            this.transition = JSONUtil.getJSONObject(jSONObject, AbsoluteConst.JSON_KEY_TRANSITION);
            this.transform = JSONUtil.getJSONObject(jSONObject, AbsoluteConst.JSON_KEY_TRANSFORM);
            this.errorPage = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_ERROR_PAGE);
            this.mInjection = JSONUtil.getString(jSONObject, "injection");
            this.mPlusrequire = jSONObject.optString("plusrequire", this.mPlusrequire);
            this.popGesture = jSONObject.optString("popGesture", this.popGesture);
            this.mGeoInject = jSONObject.optString("geolocation", this.mGeoInject);
            String string = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_BOUNCE);
            if ("vertical".equalsIgnoreCase(string) || "horizontal".equalsIgnoreCase(string) || "all".equalsIgnoreCase(string)) {
                this.mBounce = true;
            } else {
                this.mBounce = false;
            }
            this.mVideoFullscree = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN);
            if (jSONObject.has("navigationbar")) {
                JSONUtil.combinJSONObject(this.navigationbar, JSONUtil.getJSONObject(jSONObject, "navigationbar"));
            }
        }
        return updateViewData;
    }

    public String getScrollIndicator() {
        return this.mScrollIndicator;
    }

    public static ViewOptions createViewOptionsData(ViewOptions viewOptions, ViewRect viewRect) {
        return createViewOptionsData(viewOptions, null, viewRect);
    }

    public static ViewOptions createViewOptionsData(ViewOptions viewOptions, ViewRect viewRect, ViewRect viewRect2) {
        if (viewOptions == null) {
            return null;
        }
        ViewOptions viewOptions2 = new ViewOptions();
        if (viewRect != null) {
            viewOptions2.setFrameParentViewRect(viewRect);
        }
        viewOptions2.mWebviewScale = viewOptions.mWebviewScale;
        viewOptions2.setParentViewRect(viewRect2);
        viewOptions2.updateViewData(viewOptions.mJsonViewOption);
        return viewOptions2;
    }

    public static String checkOptionsErrorPage(IWebview iWebview, String str) {
        String str2 = "none";
        if (!TextUtils.isEmpty(str) && !URLUtil.isNetworkUrl(str)) {
            if ("none".equals(str)) {
                return str;
            }
            if (!new File(iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), str)).exists()) {
                String obtainConfigProperty = iWebview.obtainApp().obtainConfigProperty("error");
                if (!"none".equals(obtainConfigProperty)) {
                    str2 = iWebview.obtainApp().convert2WebviewFullPath(null, obtainConfigProperty);
                }
            } else {
                return iWebview.obtainApp().convert2WebviewFullPath(iWebview.obtainFullUrl(), str);
            }
        } else {
            String obtainConfigProperty2 = iWebview.obtainApp().obtainConfigProperty("error");
            if (!"none".equals(obtainConfigProperty2)) {
                return iWebview.obtainApp().convert2WebviewFullPath(null, obtainConfigProperty2);
            }
        }
        return str2;
    }

    public void setDragData(JSONObject jSONObject, JSONObject jSONObject2, IFrameView iFrameView, IFrameView iFrameView2, String str, View view) {
        try {
            if (this.dragData == null) {
                this.dragData = new HashMap<>();
            }
            DragBean dragBean = new DragBean();
            dragBean.dragCurrentViewOp = jSONObject;
            dragBean.dragBindViewOp = jSONObject2;
            dragBean.dragBindWebView = iFrameView;
            dragBean.dragCallBackWebView = iFrameView2;
            dragBean.dragCbId = str;
            dragBean.nativeView = view;
            if (jSONObject.has("direction")) {
                String string = jSONObject.getString("direction");
                if (TextUtils.isEmpty(string)) {
                    return;
                }
                this.dragData.put(string.toLowerCase(), dragBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
