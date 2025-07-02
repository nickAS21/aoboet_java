package io.dcloud.feature.barcode;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.TextUtils;
import android.widget.AbsoluteLayout;

import com.dcloud.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.barcode.barcode_b.CaptureActivityHandler;
import io.dcloud.feature.barcode.view.DetectorViewConfig;

/* compiled from: BarcodeProxy.java */
/* loaded from: classes.dex old b */
public class BarcodeProxy implements ISysEventListener {
    public static boolean a = false;
    public static Context b;
    BarcodeFrameItem barcodeFrameItem = null;
    boolean d = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(IWebview iWebview, String str, String[] strArr) {
        boolean z = false;
        if ("start".equals(str)) {
            if (!PdrUtil.isEmpty(this.barcodeFrameItem.e)) {
                JSUtil.execCallback(iWebview, this.barcodeFrameItem.d, String.format(DOMException.JSON_ERROR_INFO, 8, this.barcodeFrameItem.e), JSUtil.ERROR, true, true);
                return;
            }
            JSONObject createJSONObject = JSONUtil.createJSONObject(strArr[0]);
            if (createJSONObject != null) {
                boolean parseBoolean = PdrUtil.parseBoolean(JSONUtil.getString(createJSONObject, "conserve"), false, false);
                if (parseBoolean) {
                    this.barcodeFrameItem.g = iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), PdrUtil.getDefaultPrivateDocPath(JSONUtil.getString(createJSONObject, AbsoluteConst.JSON_KEY_FILENAME), "png"));
                    Logger.d("Filename:" + this.barcodeFrameItem.g);
                }
                this.barcodeFrameItem.b = PdrUtil.parseBoolean(JSONUtil.getString(createJSONObject, "vibrate"), true, false);
                this.barcodeFrameItem.a = !TextUtils.equals(JSONUtil.getString(createJSONObject, "sound"), "none");
                z = parseBoolean;
            }
            this.barcodeFrameItem.f = z;
            this.barcodeFrameItem.b();
            return;
        }
        if ("cancel".equals(str)) {
            this.barcodeFrameItem.d();
            return;
        }
        if ("setFlash".equals(str)) {
            this.barcodeFrameItem.b(Boolean.parseBoolean(strArr[0]));
            return;
        }
        if (IFeature.F_BARCODE.equals(str)) {
            if (!this.d) {
                IApp obtainApp = iWebview.obtainFrameView().obtainApp();
                obtainApp.registerSysEventListener(this, ISysEventListener.SysEventType.onPause);
                obtainApp.registerSysEventListener(this, ISysEventListener.SysEventType.onResume);
                this.d = true;
            }
            JSONArray createJSONArray = JSONUtil.createJSONArray(strArr[1]);
            Rect rect = DetectorViewConfig.getInstance().gatherRect;
            rect.left = PdrUtil.parseInt(JSONUtil.getString(createJSONArray, 0), 0);
            rect.top = PdrUtil.parseInt(JSONUtil.getString(createJSONArray, 1), 0);
            rect.right = rect.left + PdrUtil.parseInt(JSONUtil.getString(createJSONArray, 2), 0);
            rect.bottom = rect.top + PdrUtil.parseInt(JSONUtil.getString(createJSONArray, 3), 0);
            float scale = iWebview.getScale();
            rect.left = (int) (rect.left * scale);
            rect.top = (int) (rect.top * scale);
            rect.right = (int) (rect.right * scale);
            rect.bottom = (int) (rect.bottom * scale);
            if (rect.width() != 0 && rect.height() != 0) {
                JSONArray createJSONArray2 = !PdrUtil.isEmpty(strArr[2]) ? JSONUtil.createJSONArray(strArr[2]) : null;
                JSONObject createJSONObject2 = !PdrUtil.isEmpty(strArr[3]) ? JSONUtil.createJSONObject(strArr[3]) : null;
                AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) AdaFrameItem.LayoutParamsUtil.createLayoutParams(rect.left, rect.top, rect.width(), rect.height());
                this.barcodeFrameItem = new BarcodeFrameItem(this, iWebview, layoutParams, createJSONArray2, createJSONObject2);
                ViewOptions obtainFrameOptions = ((AdaFrameItem) iWebview.obtainFrameView()).obtainFrameOptions();
                this.barcodeFrameItem.updateViewRect((AdaFrameItem) iWebview.obtainFrameView(), new int[]{rect.left, rect.top, rect.width(), rect.height()}, new int[]{obtainFrameOptions.width, obtainFrameOptions.height});
                this.barcodeFrameItem.d = strArr[0];
                iWebview.obtainFrameView().addFrameItem(this.barcodeFrameItem, layoutParams);
                return;
            }
            Logger.e(IFeature.F_BARCODE, "LayoutParams l=" + rect.left + ";t=" + rect.top + ";r=" + rect.right + ";b=" + rect.bottom);
            return;
        }
        if ("scan".equals(str)) {
            String str2 = strArr[0];
            String convert2AbsFullPath = iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[1]);
            Result a2 = CaptureActivityHandler.a(BitmapFactory.decodeFile(convert2AbsFullPath));
            if (a2 != null) {
                JSUtil.execCallback(iWebview, str2, String.format("{type:'%s',message:'%s',file:'%s'}", a2.getBarcodeFormat().toString(), a2.getText(), convert2AbsFullPath), JSUtil.OK, true, false);
                return;
            } else {
                JSUtil.execCallback(iWebview, str2, String.format(DOMException.JSON_ERROR_INFO, 8, ""), JSUtil.ERROR, true, false);
                return;
            }
        }
        if (AbsoluteConst.EVENTS_CLOSE.equals(str)) {
            this.barcodeFrameItem.disposeAndCleanup();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a() {
        BarcodeFrameItem aVar = this.barcodeFrameItem;
        if (aVar != null) {
            aVar.f();
            this.barcodeFrameItem = null;
        }
        this.d = false;
    }

    protected void b() {
        BarcodeFrameItem aVar = this.barcodeFrameItem;
        if (aVar != null) {
            aVar.a();
        }
    }

    protected void c() {
        BarcodeFrameItem aVar = this.barcodeFrameItem;
        if (aVar != null) {
            aVar.a(true);
        }
    }

    @Override // io.dcloud.common.DHInterface.ISysEventListener
    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType == ISysEventListener.SysEventType.onResume) {
            c();
            return false;
        }
        if (sysEventType != ISysEventListener.SysEventType.onPause) {
            return false;
        }
        b();
        return false;
    }
}
