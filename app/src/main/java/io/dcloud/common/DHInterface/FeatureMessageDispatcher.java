package io.dcloud.common.DHInterface;

import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.common.adapter.util.MessageHandler;

/* loaded from: classes.dex */
public class FeatureMessageDispatcher {
    public static ArrayList<MessageListener> sFeatureMessage = new ArrayList<>();

    @Deprecated
    /* loaded from: classes.dex */
    public interface MessageListener {
        void onReceiver(Object obj);
    }

    public static void registerListener(MessageListener messageListener) {
        sFeatureMessage.add(messageListener);
    }

    public static void unregisterListener(MessageListener messageListener) {
        sFeatureMessage.remove(messageListener);
    }

    public static void dispatchMessage(Object obj, final Object obj2) {
        try {
            Iterator<MessageListener> it = sFeatureMessage.iterator();
            while (it.hasNext()) {
                MessageListener next = it.next();
                if (next instanceof StrongMessageListener) {
                    if (obj.equals(((StrongMessageListener) next).mFlag)) {
                        MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.DHInterface.FeatureMessageDispatcher.1
                            @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                            public void execute(Object obj3) {
                                ((MessageListener) obj3).onReceiver(obj2);
                            }
                        }, next);
                    }
                } else if (obj2 != null) {
                    MessageHandler.sendMessage(new MessageHandler.IMessages() { // from class: io.dcloud.common.DHInterface.FeatureMessageDispatcher.2
                        @Override // io.dcloud.common.adapter.util.MessageHandler.IMessages
                        public void execute(Object obj3) {
                            ((MessageListener) obj3).onReceiver(obj2);
                        }
                    }, next);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void dispatchMessage(Object obj) {
        dispatchMessage(null, obj);
    }

    /* loaded from: classes.dex */
    public static abstract class StrongMessageListener implements MessageListener {
        Object mFlag;

        @Override // io.dcloud.common.DHInterface.FeatureMessageDispatcher.MessageListener
        public abstract void onReceiver(Object obj);

        public StrongMessageListener(Object obj) {
            this.mFlag = obj;
        }
    }
}
