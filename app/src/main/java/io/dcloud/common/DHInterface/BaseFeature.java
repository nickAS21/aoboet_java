package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.common.adapter.util.Logger;

/* loaded from: classes.dex */
public class BaseFeature implements IBoot, IDPlugin, IFeature, IReflectAble, ISysEventListener {
    protected String mFeatureName = null;
    protected AbsMgr mFeatureMgr = null;
    protected Context mApplicationContext = null;
    private Context mDPluginContext = null;
    private Activity mDPluginActivity = null;
    protected ArrayList<BaseModule> mModules = null;

    @Override // io.dcloud.common.DHInterface.IFeature
    public void dispose(String str) {
    }

    public boolean doHandleAction(String str) {
        return false;
    }

    public String execute(IWebview iWebview, String str, JSONArray jSONArray) {
        return null;
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public String execute(IWebview iWebview, String str, String[] strArr) {
        return null;
    }

    public boolean isOldMode() {
        return false;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
    }

    protected void onConfigurationChanged(Configuration configuration) {
    }

    protected void onLowMemory() {
    }

    protected void onNewIntent(Intent intent) {
    }

    @Override // io.dcloud.common.DHInterface.IBoot
    public void onPause() {
    }

    public void onReceiver(Intent intent) {
    }

    @Override // io.dcloud.common.DHInterface.IBoot
    public void onResume() {
    }

    protected void onSaveInstanceState(Bundle bundle) {
    }

    @Override // io.dcloud.common.DHInterface.IBoot
    public void onStart(Context context, Bundle bundle, String[] strArr) {
    }

    @Override // io.dcloud.common.DHInterface.IBoot
    public void onStop() {
    }

    @Override // io.dcloud.common.DHInterface.IFeature
    public void init(AbsMgr absMgr, String str) {
        this.mFeatureMgr = absMgr;
        this.mApplicationContext = absMgr.getContext();
        this.mFeatureName = str;
    }

    @Override // io.dcloud.common.DHInterface.IDPlugin
    public void initDPlugin(Context context, Activity activity) {
        this.mDPluginContext = context;
        this.mDPluginActivity = activity;
    }

    @Override // io.dcloud.common.DHInterface.IDPlugin
    public Context getDPluginContext() {
        Context context = this.mDPluginContext;
        return context == null ? this.mApplicationContext : context;
    }

    @Override // io.dcloud.common.DHInterface.IDPlugin
    public Activity getDPluginActivity() {
        return this.mDPluginActivity;
    }

    public final void registerSysEvent(IWebview iWebview, ISysEventListener.SysEventType sysEventType) {
        iWebview.obtainApp().registerSysEventListener(this, sysEventType);
    }

    public final void unregisterSysEvent(IWebview iWebview, ISysEventListener.SysEventType sysEventType) {
        iWebview.obtainApp().unregisterSysEventListener(this, sysEventType);
    }

    public final void registerSysEvent(IWebview iWebview) {
        registerSysEvent(iWebview, ISysEventListener.SysEventType.AllSystemEvent);
    }

    public final void unregisterSysEvent(IWebview iWebview) {
        unregisterSysEvent(iWebview, ISysEventListener.SysEventType.AllSystemEvent);
    }

    @Override // io.dcloud.common.DHInterface.ISysEventListener
    public final boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType == ISysEventListener.SysEventType.onActivityResult) {
            Object[] objArr = (Object[]) obj;
            onActivityResult(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (Intent) objArr[2]);
        } else if (sysEventType == ISysEventListener.SysEventType.onStart) {
            Object[] objArr2 = (Object[]) obj;
            onStart((Context) objArr2[0], (Bundle) objArr2[1], (String[]) objArr2[3]);
        } else if (sysEventType == ISysEventListener.SysEventType.onPause) {
            onPause();
        } else if (sysEventType == ISysEventListener.SysEventType.onStop) {
            onStop();
        } else if (sysEventType == ISysEventListener.SysEventType.onResume) {
            onResume();
        } else if (sysEventType == ISysEventListener.SysEventType.onNewIntent) {
            onNewIntent((Intent) obj);
        } else if (sysEventType == ISysEventListener.SysEventType.onSaveInstanceState) {
            onSaveInstanceState((Bundle) obj);
        }
        return false;
    }

    public BaseModule getBaseModuleById(String str) {
        ArrayList<BaseModule> arrayList = this.mModules;
        if (arrayList == null) {
            return null;
        }
        Iterator<BaseModule> it = arrayList.iterator();
        while (it.hasNext()) {
            BaseModule next = it.next();
            if (next.id.equals(str)) {
                return next;
            }
        }
        return null;
    }

    public ArrayList<BaseModule> loadModules() {
        ArrayList<BaseModule> arrayList = this.mModules;
        if (arrayList != null) {
            return arrayList;
        }
        this.mModules = new ArrayList<>();
        HashMap <String, Object>hashMap = (HashMap) this.mFeatureMgr.processEvent(IMgr.MgrType.FeatureMgr, 4, this.mFeatureName);
        if (hashMap != null && !hashMap.isEmpty()) {
            for (String str : hashMap.keySet()) {
                try {
                    Object newInstance = Class.forName((String) hashMap.get(str)).newInstance();
                    if (newInstance instanceof BaseModule) {
                        BaseModule baseModule = (BaseModule) newInstance;
                        baseModule.init(this.mApplicationContext);
                        baseModule.name = str;
                        baseModule.featureName = this.mFeatureName;
                        if (baseModule.id == null) {
                            baseModule.id = str;
                        }
                        this.mModules.add(baseModule);
                    }
                } catch (ClassNotFoundException e) {
                    Logger.e(e.getLocalizedMessage());
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                } catch (InstantiationException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return this.mModules;
    }

    protected JSONArray toModuleJSONArray() throws JSONException {
        JSONArray jSONArray = new JSONArray();
        ArrayList<BaseModule> arrayList = this.mModules;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                jSONArray.put(this.mModules.get(i).toJSONObject());
            }
        }
        return jSONArray;
    }

    /* loaded from: classes.dex */
    public static abstract class BaseModule {
        public Context mApplicationContext;
        public String featureName = null;
        public String name = null;
        public String id = null;
        public String description = null;

        public abstract JSONObject toJSONObject() throws JSONException;

        public void init(Context context) {
            this.mApplicationContext = context;
        }

        public String getFullDescription() {
            return this.featureName + this.description;
        }
    }
}
