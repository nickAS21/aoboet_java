package com.demo.smarthome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.demo.smarthome.tools.Item;
import com.demo.smarthome.tools.ItemAdapter;
import com.lt.batteryManage.R;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class SsidListAct extends Activity {
    private ListView lv;
    private ArrayList<Item> ssids;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_ssid_list);
        this.ssids = (ArrayList) getIntent().getSerializableExtra("ssids");
        this.lv = (ListView) findViewById(R.id.lv_ssid);
        this.lv.setAdapter((ListAdapter) new ItemAdapter(this, this.ssids));
        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.demo.smarthome.activity.SsidListAct.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Item item = (Item) SsidListAct.this.ssids.get(i);
                System.out.println("onClick-------------->ssid:" + item.getName() + " dbm:" + item.getDbm());
                Intent intent = new Intent();
                intent.putExtra("ssid", item.getName());
                SsidListAct.this.setResult(-1, intent);
                SsidListAct.this.finish();
            }
        });
    }
}
