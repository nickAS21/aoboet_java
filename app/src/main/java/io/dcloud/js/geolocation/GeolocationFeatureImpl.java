package io.dcloud.js.geolocation;

import android.text.TextUtils;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;

/* loaded from: classes.dex */
public class GeolocationFeatureImpl implements IFeature {
    private a a;

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(final IWebview iWebview, final String str, final String[] strArr) {
        if (iWebview.getActivity().getApplicationInfo().targetSdkVersion < 23) {
            this.a.a(iWebview, str, strArr);
            return null;
        }
        final IApp obtainApp = iWebview.obtainApp();
        final String convertNativePermission = PermissionUtil.convertNativePermission("LOCATION");
        final int hashCode = iWebview.hashCode();
        obtainApp.registerSysEventListener(new ISysEventListener() { // from class: io.dcloud.js.geolocation.GeolocationFeatureImpl.1
            @Override // io.dcloud.common.DHInterface.ISysEventListener
            public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                int checkSelfPermission;
                Object[] objArr = (Object[]) obj;
                int intValue = ((Integer) objArr[0]).intValue();
                int[] iArr = (int[]) objArr[2];
                if (ISysEventListener.SysEventType.onRequestPermissionsResult == sysEventType && intValue == hashCode) {
                    obtainApp.unregisterSysEventListener(this, ISysEventListener.SysEventType.onRequestPermissionsResult);
                    if (iArr.length > 0) {
                        checkSelfPermission = iArr[0];
                    } else {
                        checkSelfPermission = iWebview.obtainApp().checkSelfPermission(convertNativePermission);
                    }
                    if (TextUtils.equals("authorized", PermissionUtil.convert5PlusValue(checkSelfPermission))) {
                        GeolocationFeatureImpl.this.a.a(iWebview, str, strArr);
                    } else {
                        JSUtil.execCallback(iWebview, strArr[0], DOMException.toJSON(22, DOMException.MSG_GEOLOCATION_PERMISSION_ERROR), JSUtil.ERROR, true, false);
                    }
                }
                return true;
            }
        }, ISysEventListener.SysEventType.onRequestPermissionsResult);
        obtainApp.requestPermissions(new String[]{convertNativePermission}, hashCode);
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.a = new a(absMgr);
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
        if (TextUtils.isEmpty(str)) {
            this.a.a();
        }
    }
}
