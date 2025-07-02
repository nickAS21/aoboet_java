package io.dcloud.common.util;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.util.ArrayList;

import io.dcloud.RInformation;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public class ErrorDialogUtil {
    private static ArrayList<String> list = new ArrayList<>();

    public static Dialog getLossDialog(final IWebview iWebview, String str, final String str2, final String str3) {
        AlertDialog.Builder builder;
        if (iWebview.obtainApp().isStreamApp() || list.contains(str3)) {
            return null;
        }
        list.add(str3);
        if (Build.VERSION.SDK_INT < 11) {
            builder = new AlertDialog.Builder(iWebview.getActivity());
        } else {
            builder = new AlertDialog.Builder(iWebview.getActivity(), RInformation.FEATURE_LOSS_STYLE);
        }
        AlertDialog create = builder.setTitle("HTML5+ Runtime").setIcon(R.drawable.ic_dialog_info).setMessage(str).setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: io.dcloud.common.util.ErrorDialogUtil.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(str2));
                iWebview.getActivity().startActivity(intent);
            }
        }).setNegativeButton(AbsoluteConst.STREAMAPP_UPD_ZHCancel, (DialogInterface.OnClickListener) null).create();
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: io.dcloud.common.util.ErrorDialogUtil.2
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                ErrorDialogUtil.list.remove(str3);
            }
        });
        return create;
    }
}
