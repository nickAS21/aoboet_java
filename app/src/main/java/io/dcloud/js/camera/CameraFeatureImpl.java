package io.dcloud.js.camera;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.ContentUriUtil;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class CameraFeatureImpl implements IFeature {
    AbsMgr a = null;

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(final IWebview iWebview, String str, String[] strArr) {
        final String str2 = strArr[0];
        if (str.equals("captureImage")) {
            try {
                final IApp obtainApp = iWebview.obtainFrameView().obtainApp();
                final String convert2AbsFullPath = obtainApp.convert2AbsFullPath(iWebview.obtainFullUrl(), CameraManager.a(strArr[1], true).a());
                if (JSUtil.checkOperateDirErrorAndCallback(iWebview, str2, convert2AbsFullPath)) {
                    return null;
                }
                File file = new File(convert2AbsFullPath);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                obtainApp.registerSysEventListener(new ISysEventListener() { // from class: io.dcloud.js.camera.CameraFeatureImpl.1
                    @Override // io.dcloud.common.DHInterface.ISysEventListener
                    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                        Object[] objArr = (Object[]) obj;
                        int intValue = ((Integer) objArr[0]).intValue();
                        int intValue2 = ((Integer) objArr[1]).intValue();
                        if (sysEventType == ISysEventListener.SysEventType.onActivityResult && intValue == CameraManager.a) {
                            if (intValue2 == -1) {
                                JSUtil.execCallback(iWebview, str2, obtainApp.convert2RelPath(convert2AbsFullPath), JSUtil.OK, false, false);
                            } else {
                                JSUtil.execCallback(iWebview, str2, DOMException.toJSON(11, "resultCode is wrong"), JSUtil.ERROR, true, false);
                            }
                            obtainApp.unregisterSysEventListener(this, sysEventType);
                        }
                        return false;
                    }
                }, ISysEventListener.SysEventType.onActivityResult);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra("output", Uri.fromFile(file));
                iWebview.getActivity().startActivityForResult(intent, CameraManager.a);
                return null;
            } catch (Exception e) {
                JSUtil.execCallback(iWebview, str2, DOMException.toJSON(11, e.getMessage()), JSUtil.ERROR, true, false);
                return null;
            }
        }
        if (str.equals("startVideoCapture")) {
            try {
                final IApp obtainApp2 = iWebview.obtainFrameView().obtainApp();
                final String convert2AbsFullPath2 = obtainApp2.convert2AbsFullPath(iWebview.obtainFullUrl(), CameraManager.a(strArr[1], false).a());
                if (JSUtil.checkOperateDirErrorAndCallback(iWebview, str2, convert2AbsFullPath2)) {
                    return null;
                }
                File file2 = new File(convert2AbsFullPath2);
                File parentFile2 = file2.getParentFile();
                if (!parentFile2.exists()) {
                    parentFile2.mkdirs();
                }
                obtainApp2.registerSysEventListener(new ISysEventListener() { // from class: io.dcloud.js.camera.CameraFeatureImpl.2
                    @Override // io.dcloud.common.DHInterface.ISysEventListener
                    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                        Object[] objArr = (Object[]) obj;
                        int intValue = ((Integer) objArr[0]).intValue();
                        int intValue2 = ((Integer) objArr[1]).intValue();
                        if (sysEventType == ISysEventListener.SysEventType.onActivityResult && intValue == CameraManager.b) {
                            if (intValue2 == -1) {
                                if (!new File(convert2AbsFullPath2).exists()) {
                                    DHFile.copyFile(ContentUriUtil.getImageAbsolutePath(obtainApp2.getActivity(), ((Intent) objArr[2]).getData()), convert2AbsFullPath2);
                                }
                                JSUtil.execCallback(iWebview, str2, obtainApp2.convert2RelPath(convert2AbsFullPath2), JSUtil.OK, false, false);
                            } else {
                                JSUtil.execCallback(iWebview, str2, null, JSUtil.ERROR, false, false);
                            }
                            obtainApp2.unregisterSysEventListener(this, sysEventType);
                        }
                        return false;
                    }
                }, ISysEventListener.SysEventType.onActivityResult);
                Intent intent2 = new Intent("android.media.action.VIDEO_CAPTURE");
                intent2.putExtra("output", Uri.fromFile(file2));
                iWebview.getActivity().startActivityForResult(intent2, CameraManager.b);
                return null;
            } catch (Exception e2) {
                JSUtil.execCallback(iWebview, str2, DOMException.toJSON(11, e2.getMessage()), JSUtil.ERROR, true, false);
                return null;
            }
        }
        if (str.equals("getCamera")) {
            return new CameraManager(PdrUtil.parseInt(strArr[1], 1)).a();
        }
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.a = absMgr;
    }
}
