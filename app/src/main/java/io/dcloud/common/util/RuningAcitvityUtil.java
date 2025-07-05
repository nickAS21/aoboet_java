package io.dcloud.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class RuningAcitvityUtil {
    private static HashMap<String, Activity> RuningActivitys = new HashMap<>();

    public static void putRuningActivity(Activity activity) {
        if (RuningActivitys.containsValue(activity)) {
            return;
        }
        RuningActivitys.put(activity.getComponentName().getClassName(), activity);
    }

    public static void removeRuningActivity(String str) {
        if (RuningActivitys.containsKey(str)) {
            RuningActivitys.remove(str);
        }
    }

    public static Activity getTopRuningActivity(Activity activity) {
        Activity value;
        if (BaseInfo.isForQihooHelper(activity)) {
            Iterator<Map.Entry<String, Activity>> it = RuningActivitys.entrySet().iterator();
            return (!it.hasNext() || (value = it.next().getValue()) == null || value.isFinishing()) ? activity : value;
        }
        ActivityManager.RunningTaskInfo runningTaskInfo = ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0);
        return RuningActivitys.containsKey(runningTaskInfo.topActivity.getClassName()) ? RuningActivitys.get(runningTaskInfo.topActivity.getClassName()) : activity;
    }
}
