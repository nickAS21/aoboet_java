package io.dcloud.feature.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.PdrUtil;

/* compiled from: NWindowGroup.java */
/* loaded from: classes.dex old d*/
public class NWindowGroup extends NView implements IEventCallback {
    ArrayList<IFrameView> q;
    IFrameView r;

    @Override // io.dcloud.feature.ui.b
    public void a(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.dcloud.feature.ui.b
    public void g() {
    }

    public NWindowGroup(String str, ArrayList<IFrameView> arrayList, JSONObject jSONObject) {
        super(str);
        this.q = arrayList;
    }

    public void h() {
        this.c.b(this.r);
        Iterator<IFrameView> it = this.q.iterator();
        while (it.hasNext()) {
            IFrameView next = it.next();
            this.c.b(next);
            ((AdaFrameView) next).isChildOfFrameView = true;
        }
    }

    public void a(boolean z) {
        this.r.setVisible(z, true);
    }

    public void a(IFrameView iFrameView) {
        this.r = iFrameView;
    }

    @Override // io.dcloud.feature.ui.b
    public String a(IWebview iWebview, String str, JSONArray jSONArray) {
        try {
            if ("addEventListener".equals(str)) {
                a(jSONArray.getString(1), jSONArray.getString(0), this.b.get(iWebview));
            } else {
                "setSelectIndex".equals(str);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // io.dcloud.feature.ui.b
    public AdaFrameItem e() {
        return (AdaFrameItem) this.r;
    }

    @Override // io.dcloud.common.DHInterface.IEventCallback
    public Object onCallBack(String str, Object obj) {
        if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_PAGER_SELECTED)) {
            a(str, PdrUtil.isEmpty(obj) ? null : String.valueOf(obj), false);
        }
        return null;
    }
}
