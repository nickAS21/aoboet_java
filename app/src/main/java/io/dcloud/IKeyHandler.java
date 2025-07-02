package io.dcloud;

import android.view.KeyEvent;

import io.dcloud.common.DHInterface.ISysEventListener;

/* compiled from: IKeyHandler.java */
/* loaded from: classes.dex old d*/
public interface IKeyHandler {
    boolean onKeyEventExecute(ISysEventListener.SysEventType sysEventType, int i, KeyEvent keyEvent);
}
