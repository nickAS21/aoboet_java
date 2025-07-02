package io.dcloud.common.adapter.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public class MessageHandler {
    private static Handler myHandler = new Handler(Looper.getMainLooper()) { // from class: io.dcloud.common.adapter.util.MessageHandler.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                Object[] objArr = (Object[]) message.obj;
                if (objArr[0] instanceof IMessages) {
                    ((IMessages) objArr[0]).execute(objArr[1]);
                }
            } catch (Exception e) {
                Logger.e("MessageHandler.handleMessage e=" + e);
            }
        }
    };

    /* loaded from: classes.dex */
    public interface IMessages {
        void execute(Object obj);
    }

    public static void removeCallbacksAndMessages() {
    }

    public static void sendMessage(IMessages iMessages, Object obj) {
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.obj = new Object[]{iMessages, obj};
        myHandler.sendMessage(obtain);
    }

    public static void sendMessage(IMessages iMessages, long j, Object obj) {
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.obj = new Object[]{iMessages, obj};
        myHandler.sendMessageDelayed(obtain, j);
    }

    public static void postDelayed(Runnable runnable, long j) {
        myHandler.postDelayed(runnable, j);
    }

    public static void removeCallbacks(Runnable runnable) {
        myHandler.removeCallbacks(runnable);
    }
}
