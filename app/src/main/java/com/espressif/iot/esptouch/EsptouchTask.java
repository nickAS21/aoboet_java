package com.espressif.iot.esptouch;

import android.content.Context;

import com.espressif.iot.esptouch.task.EsptouchTaskParameter;
import com.espressif.iot.esptouch.task.IEsptouchTaskParameter;
import com.espressif.iot.esptouch.task.__EsptouchTask;

/* loaded from: classes.dex */
public class EsptouchTask implements IEsptouchTask {
    public __EsptouchTask _mEsptouchTask;
    private IEsptouchTaskParameter _mParameter;

    public EsptouchTask(String str, String str2, String str3, boolean z, Context context) {
        this._mParameter = new EsptouchTaskParameter();
        this._mEsptouchTask = new __EsptouchTask(str, str2, str3, context, this._mParameter, z);
    }

    public EsptouchTask(String str, String str2, String str3, boolean z, int i, Context context) {
        EsptouchTaskParameter esptouchTaskParameter = new EsptouchTaskParameter();
        this._mParameter = esptouchTaskParameter;
        esptouchTaskParameter.setWaitUdpTotalMillisecond(i);
        this._mEsptouchTask = new __EsptouchTask(str, str2, str3, context, this._mParameter, z);
    }

    @Override // com.espressif.iot.esptouch.IEsptouchTask
    public void interrupt() {
        this._mEsptouchTask.interrupt();
    }

    @Override // com.espressif.iot.esptouch.IEsptouchTask
    public IEsptouchResult executeForResult() throws RuntimeException {
        return this._mEsptouchTask.executeForResult();
    }

    @Override // com.espressif.iot.esptouch.IEsptouchTask
    public boolean isCancelled() {
        return this._mEsptouchTask.isCancelled();
    }
}
