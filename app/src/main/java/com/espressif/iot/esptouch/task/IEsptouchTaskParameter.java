package com.espressif.iot.esptouch.task;

/* loaded from: classes.dex */
public interface IEsptouchTaskParameter {
    int getEsptouchResultIpLen();

    int getEsptouchResultMacLen();

    int getEsptouchResultOneLen();

    int getEsptouchResultTotalLen();

    long getIntervalDataCodeMillisecond();

    long getIntervalGuideCodeMillisecond();

    int getPortListening();

    String getTargetHostname();

    int getTargetPort();

    int getThresholdSucBroadcastCount();

    long getTimeoutDataCodeMillisecond();

    long getTimeoutGuideCodeMillisecond();

    long getTimeoutTotalCodeMillisecond();

    int getTotalRepeatTime();

    int getWaitUdpReceivingMillisecond();

    int getWaitUdpSendingMillisecond();

    int getWaitUdpTotalMillisecond();

    void setWaitUdpTotalMillisecond(int i);
}
