package io.dcloud.common.adapter.util;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public class DownloadUtil extends BroadcastReceiver {
    private static DownloadUtil m;
    private DownloadManager mDownloader;
    HashMap<Long, MyRequest> rs;

    private DownloadUtil(Context context) {
        this.rs = null;
        this.mDownloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter filter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.registerReceiver(this, filter, Context.RECEIVER_NOT_EXPORTED);
        }

        this.rs = new HashMap<>(2);
    }

    public static long startRequest(Context context, String str, String str2, String str3, String str4, ICallBack iCallBack) {
        if (m == null) {
            m = new DownloadUtil(context);
        }
        MyRequest myRequest = new MyRequest(str, str2, iCallBack);
        try {
            if (!DHFile.isExist(str3)) {
                new File(str3).mkdirs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (str3.startsWith(DeviceInfo.sDeviceRootDir)) {
            str3 = str3.substring(DeviceInfo.sDeviceRootDir.length() + 1);
        }
        myRequest.m.setTitle(str4);
        myRequest.m.setDestinationInExternalPublicDir(str3, str4);
        long enqueue = m.mDownloader.enqueue(myRequest.m);
        myRequest.id = enqueue;
        m.rs.put(Long.valueOf(enqueue), myRequest);
        return enqueue;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.v("intent", "" + intent.getLongExtra("extra_download_id", 0L));
        try {
            queryDownloadStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryDownloadStatus() {
        Set<Long> keySet = this.rs.keySet();
        int size = keySet.size();
        Long[] lArr = new Long[size];
        keySet.toArray(lArr);
        for (int i = size - 1; i >= 0; i--) {
            long longValue = lArr[i].longValue();
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(longValue);
            Cursor query2 = this.mDownloader.query(query);
            if (query2.moveToFirst()) {
                @SuppressLint("Range") int i2 = query2.getInt(query2.getColumnIndex("status"));
                if (i2 != 1) {
                    if (i2 != 2) {
                        if (i2 == 4) {
                            Log.v("down", "STATUS_PAUSED");
                        } else if (i2 == 8) {
                            Log.v("down", "下载完成");
                            String str = null;
                            try {
                                str = this.mDownloader.getUriForDownloadedFile(longValue).getPath();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MyRequest remove = this.rs.remove(Long.valueOf(longValue));
                            if (remove.back != null) {
                                remove.back.onCallBack(0, str);
                            }
                        } else if (i2 == 16) {
                            Log.v("down", "STATUS_FAILED");
                            this.mDownloader.remove(longValue);
                            this.rs.remove(Long.valueOf(longValue));
                        }
                    }
                    Log.v("down", "STATUS_RUNNING");
                }
                Log.v("down", "STATUS_PENDING");
                Log.v("down", "STATUS_RUNNING");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MyRequest {
        ICallBack back;
        long id;
        DownloadManager.Request m;

        MyRequest(String str, String str2, ICallBack iCallBack) {
            this.m = null;
            this.back = null;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
            this.m = request;
            request.setMimeType(str2);
            this.back = iCallBack;
        }
    }
}
