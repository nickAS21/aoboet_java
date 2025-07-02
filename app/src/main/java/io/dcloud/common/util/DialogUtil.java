package io.dcloud.common.util;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

import io.dcloud.RInformation;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public class DialogUtil {
    public static void showConfirm(Activity activity, String str, String str2, String[] strArr, final ICallBack iCallBack) {
        final AlertDialog create = new AlertDialog.Builder(activity).create();
        create.setTitle(str);
        create.setCanceledOnTouchOutside(false);
        create.setMessage(str2);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.DialogUtil.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -2) {
                    create.cancel();
                    create.dismiss();
                } else if (i != -3 && i == -1) {
                    create.dismiss();
                }
                iCallBack.onCallBack(i, null);
            }
        };
        create.setButton(-1, strArr[0], onClickListener);
        create.setButton(-2, strArr[1], onClickListener);
        create.show();
    }

    public static void showInstallAPKDialog(final Context context, String str, final String str2) {
        AlertDialog create = new AlertDialog.Builder(context).setMessage(str).setNegativeButton("确定", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.DialogUtil.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                NotificationUtil.cancelNotification(context, str2.hashCode());
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.fromFile(new File(str2)), "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        }).setNeutralButton(AbsoluteConst.STREAMAPP_UPD_ZHCancel, (DialogInterface.OnClickListener) null).create();
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    public static AlertDialog.Builder initDialogTheme(Activity activity, boolean z) {
        if (z || (Build.VERSION.SDK_INT == 23 && BaseInfo.isForQihooHelper5_0(activity))) {
            return new AlertDialog.Builder(activity);
        }
        if (Build.VERSION.SDK_INT < 11) {
            return new AlertDialog.Builder(activity);
        }
        if (Build.VERSION.SDK_INT < 20) {
            return new AlertDialog.Builder(activity, RInformation.STREAMAPP_DELETE_THEME);
        }
        return new AlertDialog.Builder(activity);
    }

    public static AlertDialog.Builder initDialogTheme(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return new AlertDialog.Builder(activity);
        }
        return new AlertDialog.Builder(activity, R.style.Theme_DeviceDefault_Light_Dialog);
    }

}
