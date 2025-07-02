package com.demo.smarthome.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.demo.smarthome.tools.Item;
import com.demo.smarthome.tools.SearchSSID;
import com.demo.smarthome.tools.Tool;
import com.demo.smarthome.tools.UIUtil;
import com.lt.batteryManage.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import io.dcloud.common.DHInterface.IApp;

/* loaded from: classes.dex */
public class UserLinkActivity extends Activity implements View.OnClickListener {
    private Button backBtn;
    private ProgressDialog dialog;
    private EditText etPasd;
    private EditText etPort;
    private EditText etSsid;
    private WifiManager.MulticastLock lock;
    private SearchSSID searchSSID;
    private SendMsgThread smt;
    public final int RESQEST_SSID_LIST = 1;
    private final byte[] searchCode = {-1, 0, 1, 1, 2};
    private Handler handler = new Handler() { // from class: com.demo.smarthome.activity.UserLinkActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            byte[] bArr = (byte[]) message.obj;
            Tool.bytesToHexString(bArr);
            UserLinkActivity.this.decodeData(bArr);
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_userlink);
        WifiManager.MulticastLock createMulticastLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createMulticastLock("fawifi");
        this.lock = createMulticastLock;
        createMulticastLock.acquire();
        this.etSsid = (EditText) findViewById(R.id.et_ssid);
        this.etPasd = (EditText) findViewById(R.id.et_pasd);
        this.etPort = (EditText) findViewById(R.id.et_port);
        Button button = (Button) findViewById(R.id.morebackbtn);
        this.backBtn = button;
        button.setOnClickListener(this);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.dialog = progressDialog;
        progressDialog.setMessage("Search...");
        SearchSSID searchSSID = new SearchSSID(this.handler);
        this.searchSSID = searchSSID;
        searchSSID.start();
        SendMsgThread sendMsgThread = new SendMsgThread(this.searchSSID);
        this.smt = sendMsgThread;
        sendMsgThread.start();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String obj = this.etPort.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            UIUtil.toastShow(this, "please input port");
            return;
        }
        int parseInt = Integer.parseInt(obj);
        if (parseInt < 0 || parseInt > 65535) {
            UIUtil.toastShow(this, "please input a right port ");
            return;
        }
        this.searchSSID.setTargetPort(Integer.parseInt(obj));
        if (view.getId() == R.id.btn_search) {
            this.dialog.show();
            this.smt.putMsg(this.searchCode);
            dismiss();
        }
        if (view.getId() == R.id.morebackbtn) {
            finish();
            return;
        }
        if (view.getId() == R.id.btn_ok) {
            String obj2 = this.etSsid.getText().toString();
            String obj3 = this.etPasd.getText().toString();
            String obj4 = this.etPort.getText().toString();
            if (TextUtils.isEmpty(obj2)) {
                UIUtil.toastShow(this, "please input ssid");
            } else {
                if (TextUtils.isEmpty(obj4)) {
                    UIUtil.toastShow(this, "please input port");
                    return;
                }
                this.searchSSID.setTargetPort(Integer.parseInt(obj));
                this.smt.putMsg(Tool.generate_02_data(obj2, obj3, 0));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decodeData(byte[] bArr) {
        if ((bArr[0] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) != 255) {
            return;
        }
        int i = bArr[3] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
        if (i == 129) {
            this.dialog.dismiss();
            ArrayList<Item> decode_81_data = Tool.decode_81_data(bArr);
            if (decode_81_data.size() != 0) {
                Intent intent = new Intent(this, (Class<?>) SsidListAct.class);
                intent.putExtra("ssids", decode_81_data);
                startActivityForResult(intent, 1);
                return;
            }
            return;
        }
        if (i != 130) {
            return;
        }
        int[] decode_82_data = Tool.decode_82_data(bArr);
        if (decode_82_data[0] == 0) {
            UIUtil.toastShow(this, R.string.no_ssid);
            return;
        }
        if (decode_82_data[1] == 0) {
            UIUtil.toastShow(this, R.string.error_pasd_length);
        } else if (decode_82_data[0] == 1 && decode_82_data[1] == 1) {
            UIUtil.toastShow(this, R.string.confing_end);
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 1 || intent == null) {
            return;
        }
        this.etSsid.setText(intent.getStringExtra("ssid"));
    }

    private void dismiss() {
        this.handler.postDelayed(new Runnable() { // from class: com.demo.smarthome.activity.UserLinkActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (UserLinkActivity.this.dialog.isShowing()) {
                    UserLinkActivity.this.dialog.dismiss();
                }
            }
        }, 3000L);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.lock.release();
        this.smt.setSend(false);
        this.searchSSID.setReceive(false);
        this.searchSSID.close();
    }

    /* loaded from: classes.dex */
    private class SendMsgThread extends Thread {
        private SearchSSID ss;
        private Queue<byte[]> sendMsgQuene = new LinkedList();
        private boolean send = true;

        public SendMsgThread(SearchSSID searchSSID) {
            this.ss = searchSSID;
        }

        public synchronized void putMsg(byte[] bArr) {
            if (this.sendMsgQuene.size() == 0) {
                notify();
            }
            this.sendMsgQuene.offer(bArr);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            synchronized (this) {
                while (this.send) {
                    while (this.sendMsgQuene.size() > 0) {
                        byte[] poll = this.sendMsgQuene.poll();
                        SearchSSID searchSSID = this.ss;
                        if (searchSSID != null) {
                            searchSSID.sendMsg(poll);
                        }
                    }
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setSend(boolean z) {
            this.send = z;
        }
    }
}
