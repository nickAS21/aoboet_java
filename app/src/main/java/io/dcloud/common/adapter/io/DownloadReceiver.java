package io.dcloud.common.adapter.io;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;

/* loaded from: classes.dex */
public class DownloadReceiver extends BroadcastReceiver {
    public static String ACTION_DOWNLOAD_COMPLETED = "action.download.completed_io.dcloud";
    public static String ACTION_OPEN_FILE = "action.openfile.io.dcloud";
    public static String KEY_FILEURI = "FileUri";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_OPEN_FILE.equals(action)) {
            String stringExtra = intent.getStringExtra(KEY_FILEURI);
            Logger.d("DownloadReceiver", "action=" + ACTION_OPEN_FILE + ";" + KEY_FILEURI + "=" + stringExtra);
            PlatformUtil.openFileBySystem(context, String.valueOf(stringExtra), null, null);
            return;
        }
        Logger.d("not handle '" + action + "' cation");
    }
}
