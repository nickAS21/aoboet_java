package android.support.v4.media;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;

/* loaded from: classes.dex */
class MediaSessionManagerImplApi28 extends MediaSessionManagerImplApi21 {
    android.media.session.MediaSessionManager mObject;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaSessionManagerImplApi28(Context context) {
        super(context);
//        this.mObject = (android.media.session.MediaSessionManager) context.getSystemService("media_session");
    }

    @Override // android.support.v4.media.MediaSessionManagerImplApi21, android.support.v4.media.MediaSessionManagerImplBase, android.support.v4.media.MediaSessionManager.MediaSessionManagerImpl
    public boolean isTrustedForMediaControl(RemoteUserInfoImpl remoteUserInfoImpl) {
        return super.isTrustedForMediaControl(remoteUserInfoImpl);
//        if (remoteUserInfoImpl instanceof RemoteUserInfoImplApi28) {
//            return this.mObject.isTrustedForMediaControl(((RemoteUserInfoImplApi28) remoteUserInfoImpl).mObject);
//        }
//        return false;
    }

    /* loaded from: classes.dex */
    static final class RemoteUserInfoImplApi28 implements RemoteUserInfoImpl {
        final String mPackageName;
        final int mPid;
        final int mUid;
//        final MediaSessionManager.RemoteUserInfo mObject;

        /* JADX INFO: Access modifiers changed from: package-private */
//        public RemoteUserInfoImplApi28(String str, int i, int i2) {
//            this.mObject = new MediaSessionManager.RemoteUserInfo(str, i, i2);
//        }
//
//        /* JADX INFO: Access modifiers changed from: package-private */
//        public RemoteUserInfoImplApi28(MediaSessionManager.RemoteUserInfo remoteUserInfo) {
//            this.mObject = remoteUserInfo;
//        }

        RemoteUserInfoImplApi28(String packageName, int pid, int uid) {
            this.mPackageName = packageName;
            this.mPid = pid;
            this.mUid = uid;
        }

        RemoteUserInfoImplApi28(android.support.v4.media.MediaSessionManager.RemoteUserInfo remoteUserInfo) {
            this.mPackageName = remoteUserInfo.getPackageName();
            this.mPid = remoteUserInfo.getPid();
            this.mUid = remoteUserInfo.getUid();
        }


        @Override
        public String getPackageName() {
            return mPackageName;
        }

        @Override
        public int getPid() {
            return mPid;
        }

        @Override
        public int getUid() {
            return mUid;
        }

        @Override
        public int hashCode() {
            return ObjectsCompat.hash(mPackageName, mPid, mUid);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof RemoteUserInfoImplApi28)) return false;
            RemoteUserInfoImplApi28 other = (RemoteUserInfoImplApi28) obj;
            return mPid == other.mPid && mUid == other.mUid &&
                    mPackageName.equals(other.mPackageName);
        }
//        @Override // android.support.v4.media.MediaSessionManager.RemoteUserInfoImpl
//        public String getPackageName() {
//            return this.mObject.getPackageName();
//        }
//
//        @Override // android.support.v4.media.MediaSessionManager.RemoteUserInfoImpl
//        public int getPid() {
//            return this.mObject.getPid();
//        }
//
//        @Override // android.support.v4.media.MediaSessionManager.RemoteUserInfoImpl
//        public int getUid() {
//            return this.mObject.getUid();
//        }
//
//        public int hashCode() {
//            return ObjectsCompat.hash(this.mObject);
//        }
//
//        public boolean equals(Object obj) {
//            if (this == obj) {
//                return true;
//            }
//            if (obj instanceof RemoteUserInfoImplApi28) {
//                return this.mObject.equals(((RemoteUserInfoImplApi28) obj).mObject);
//            }
//            return false;
//        }
    }
}
