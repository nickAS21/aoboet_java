package com.demo.smarthome.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lt.batteryManage.R;

import java.util.List;

/* loaded from: classes.dex */
public class ItemAdapter extends BaseAdapter {
    private Context context;
    private List<Item> list;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public ItemAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.item, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.name);
        TextView textView2 = (TextView) inflate.findViewById(R.id.dbm);
        Item item = this.list.get(i);
        textView.setText(item.getName());
        textView2.setText(String.valueOf(item.getDbm()));
        return inflate;
    }
}
