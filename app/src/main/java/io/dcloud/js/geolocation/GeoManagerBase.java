package io.dcloud.js.geolocation;

import android.content.Context;

import java.util.ArrayList;

import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWebview;

/* loaded from: classes.dex */
public abstract class GeoManagerBase implements IReflectAble {
    protected ArrayList<String> keySet;
    protected Context mContext;

    public abstract String execute(IWebview iWebview, String str, String[] strArr);

    public abstract void onDestroy();

    public GeoManagerBase(Context context) {
        this.keySet = null;
        this.mContext = context;
        this.keySet = new ArrayList<>();
    }

    public boolean hasKey(String str) {
        return this.keySet.contains(str);
    }
}
