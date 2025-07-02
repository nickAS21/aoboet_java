package android.support.v4.media;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

public interface MediaBrowserServiceImpl {
    Bundle getBrowserRootHints();

    MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo();

    void notifyChildrenChanged(MediaSessionManager.RemoteUserInfo remoteUserInfo, String str, Bundle bundle);

    void notifyChildrenChanged(String str, Bundle bundle);

    IBinder onBind(Intent intent);

    void onCreate();

    void setSessionToken(MediaSessionCompat.Token token);
}
