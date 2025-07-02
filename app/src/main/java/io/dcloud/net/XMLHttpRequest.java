package io.dcloud.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Map;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IReqListener;
import io.dcloud.common.DHInterface.IResponseListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.common.util.net.RequestData;

/* loaded from: classes.dex */
public class XMLHttpRequest implements IReqListener, IResponseListener {
    private static final int ERROR_OTHER_CODE = 1;
    private static final int ERROR_TIME_OUT_CODE = 0;
    private static final int LOADED = 4;
    private static final int RECEIVING = 3;
    private String mCallbackId;
    private int mErrorCode = -1;
    private NetWork mNetWork;
    private int mReadyState;
    private RequestData mRequestData;
    private int mState;
    private String mStatusText;
    public String mUUID;
    IWebview mWebview;

    @Override // io.dcloud.common.DHInterface.IReqListener
    public int onReceiving(InputStream inputStream) {
        return 0;
    }

    @Override // io.dcloud.common.DHInterface.IReqListener
    public void onResponsing(InputStream inputStream) {
    }

    public XMLHttpRequest(String str, String str2, String str3, IWebview iWebview) {
        this.mUUID = str;
        RequestData requestData = new RequestData(str2, str3);
        this.mRequestData = requestData;
        requestData.unTrustedCAType = iWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNTRUSTEDCA);
        this.mRequestData.addHeader(IWebview.USER_AGENT, iWebview.getWebviewProperty(IWebview.USER_AGENT));
        String webviewProperty = iWebview.getWebviewProperty(str2);
        if (!PdrUtil.isEmpty(webviewProperty)) {
            this.mRequestData.addHeader(IWebview.COOKIE, webviewProperty);
        }
        this.mNetWork = new NetWork(3, this.mRequestData, this, this);
        this.mWebview = iWebview;
    }

    public NetWork getNetWork() {
        return this.mNetWork;
    }

    public RequestData getRequestData() {
        return this.mRequestData;
    }

    public void setCallbackId(String str) {
        this.mCallbackId = str;
    }

    @Override // io.dcloud.common.DHInterface.IResponseListener
    public void onResponseState(int i, String str) {
        this.mState = i;
        this.mStatusText = str;
        Logger.d("xhr", "onResponseState pState=" + i + ";mCallbackId=" + this.mCallbackId);
    }

    @Override // io.dcloud.common.DHInterface.IReqListener
    public void onNetStateChanged(IReqListener.NetState netState, boolean z) {
        if (z) {
            this.mReadyState = 4;
            return;
        }
        int i = AnonymousClass1.$SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState[netState.ordinal()];
        if (i == 1) {
            this.mReadyState = 4;
            JSUtil.execCallback(this.mWebview, this.mCallbackId, toJSON(), JSUtil.OK, true);
            return;
        }
        if (i == 2) {
            this.mReadyState = 3;
            JSUtil.execCallback(this.mWebview, this.mCallbackId, toJSON(), JSUtil.OK, true);
        } else if (i == 3) {
            this.mReadyState = 4;
            this.mErrorCode = 1;
            JSUtil.execCallback(this.mWebview, this.mCallbackId, toJSON(), JSUtil.OK, true);
        } else {
            if (i != 4) {
                return;
            }
            this.mReadyState = 4;
            this.mErrorCode = 0;
            JSUtil.execCallback(this.mWebview, this.mCallbackId, toJSON(), JSUtil.OK, true);
        }
    }

    /* renamed from: io.dcloud.net.XMLHttpRequest$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState;

        static {
            int[] iArr = new int[IReqListener.NetState.values().length];
            $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState = iArr;
            try {
                iArr[IReqListener.NetState.NET_HANDLE_END.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState[IReqListener.NetState.NET_HANDLE_ING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState[IReqListener.NetState.NET_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState[IReqListener.NetState.NET_TIMEOUT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public JSONObject toJSON() {
        JSONObject jSONObject = new JSONObject();
        String responseText = this.mNetWork.getResponseText();
        try {
            jSONObject.put("readyState", this.mReadyState);
            jSONObject.put("status", this.mState);
            jSONObject.put("statusText", this.mStatusText);
            jSONObject.put("responseText", responseText);
            jSONObject.put("header", headersToJSON(this.mNetWork.getHeadersAndValues()));
            int i = this.mErrorCode;
            if (i > -1) {
                jSONObject.put("error", i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    private String toJsResponseText(String str) {
        return JSONUtil.toJSONableString(str);
    }

    private JSONObject headersToJSON(Map<String, String> map) {
        JSONObject jSONObject = new JSONObject();
        for (String str : map.keySet()) {
            try {
                jSONObject.put(str, map.get(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }
}
