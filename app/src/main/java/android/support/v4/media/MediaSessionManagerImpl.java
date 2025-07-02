package android.support.v4.media;

import android.content.Context;
public interface MediaSessionManagerImpl {
    Context getContext();

    boolean isTrustedForMediaControl(RemoteUserInfoImpl remoteUserInfoImpl);
}
