package com.demo.smarthome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity;
import com.lt.batteryManage.R;

/* loaded from: classes.dex */
public class WelcomeActivity extends Activity {
    Runnable r = new Runnable() { // from class: com.demo.smarthome.activity.WelcomeActivity.1
        @Override // java.lang.Runnable
        public void run() {
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, EsptouchDemoActivity.class);
            WelcomeActivity.this.startActivity(intent);
            WelcomeActivity.this.finish();
        }
    };

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(this.r, 3000L);
    }
}
