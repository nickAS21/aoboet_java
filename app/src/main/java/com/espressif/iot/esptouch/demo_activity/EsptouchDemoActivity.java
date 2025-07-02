package com.espressif.iot.esptouch.demo_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.lt.batteryManage.R;

/* loaded from: classes.dex */
public class EsptouchDemoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "EsptouchDemoActivity";
    private ImageButton mBtnConfirm;
    private EditText mEdtApPassword;
    private Switch mSwitchIsSsidHidden;
    private TextView mTvApSsid;
    private EspWifiAdminSimple mWifiAdmin;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.esptouch_demo_activity);
        TextView textView = (TextView) findViewById(R.id.titleSmartlink);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EsptouchDemoActivity.this.finish();
            }
        });
        this.mWifiAdmin = new EspWifiAdminSimple(this);
        this.mTvApSsid = (TextView) findViewById(R.id.tvApSssidConnected);
        this.mEdtApPassword = (EditText) findViewById(R.id.edtApPassword);
        this.mBtnConfirm = (ImageButton) findViewById(R.id.btnConfirm);
        this.mSwitchIsSsidHidden = (Switch) findViewById(R.id.switchIsSsidHidden);
        this.mBtnConfirm.setOnClickListener(this);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        String wifiConnectedSsid = this.mWifiAdmin.getWifiConnectedSsid();
        if (wifiConnectedSsid != null) {
            this.mTvApSsid.setText(wifiConnectedSsid);
        } else {
            this.mTvApSsid.setText("");
        }
        this.mBtnConfirm.setEnabled(!TextUtils.isEmpty(wifiConnectedSsid));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mBtnConfirm) {
            String charSequence = this.mTvApSsid.getText().toString();
            String obj = this.mEdtApPassword.getText().toString();
            String wifiConnectedBssid = this.mWifiAdmin.getWifiConnectedBssid();
            String str = Boolean.valueOf(this.mSwitchIsSsidHidden.isChecked()).booleanValue() ? "YES" : "NO";
            Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + charSequence + ",  mEdtApPassword = " + obj);
            new EsptouchAsyncTask2().execute(charSequence, wifiConnectedBssid, obj, str);
        }
    }

    /* loaded from: classes.dex */
    private class EsptouchAsyncTask2 extends AsyncTask<String, Void, IEsptouchResult> {
        private IEsptouchTask mEsptouchTask;
        private ProgressDialog mProgressDialog;

        private EsptouchAsyncTask2() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            ProgressDialog progressDialog = new ProgressDialog(EsptouchDemoActivity.this);
            this.mProgressDialog = progressDialog;
            progressDialog.setMessage("Esptouch is configuring, please wait for a moment...");
            this.mProgressDialog.setCanceledOnTouchOutside(false);
            this.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity.EsptouchAsyncTask2.1
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                    Log.i(EsptouchDemoActivity.TAG, "progress dialog is canceled");
                    if (EsptouchAsyncTask2.this.mEsptouchTask != null) {
                        EsptouchAsyncTask2.this.mEsptouchTask.interrupt();
                    }
                }
            });
            this.mProgressDialog.setButton(-1, "Waiting...", new DialogInterface.OnClickListener() { // from class: com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity.EsptouchAsyncTask2.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            this.mProgressDialog.show();
            this.mProgressDialog.getButton(-1).setEnabled(false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public IEsptouchResult doInBackground(String... strArr) {
            EsptouchTask esptouchTask = new EsptouchTask(strArr[0], strArr[1], strArr[2], strArr[3].equals("YES"), EsptouchDemoActivity.this);
            this.mEsptouchTask = esptouchTask;
            return esptouchTask.executeForResult();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(IEsptouchResult iEsptouchResult) {
            this.mProgressDialog.getButton(-1).setEnabled(true);
            this.mProgressDialog.getButton(-1).setText("Confirm");
            if (iEsptouchResult.isCancelled()) {
                return;
            }
            if (iEsptouchResult.isSuc()) {
                this.mProgressDialog.setMessage("Esptouch success, bssid = " + iEsptouchResult.getBssid() + ",InetAddress = " + iEsptouchResult.getInetAddress().getHostAddress());
            } else {
                this.mProgressDialog.setMessage("Esptouch fail");
            }
        }
    }
}
