package io.dcloud.common.util;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.dcloud.RInformation;
import io.dcloud.common.constant.AbsoluteConst;

/* loaded from: classes.dex */
public class NotificationUtil {
    public static void createCustomNotification(Context context, String str, Bitmap bitmap, String str2, String str3, int i, PendingIntent pendingIntent) {
        Context applicationContext = context.getApplicationContext();
        if (BaseInfo.isForQihooHelper(applicationContext)) {
            showNotification(applicationContext, str2, str3, pendingIntent, bitmap, i);
            return;
        }
        Notification notification = new Notification(RInformation.DRAWABLE_ICON, str, System.currentTimeMillis());
        RemoteViews remoteViews = new RemoteViews(applicationContext.getPackageName(), RInformation.LAYOUT_CUSTION_NOTIFICATION_DCLOUD);
        remoteViews.setImageViewBitmap(RInformation.ID_IMAGE_NOTIFICATION_DCLOUD, bitmap);
        remoteViews.setTextViewText(RInformation.ID_TITLE_NOTIFICATION_DCLOUD, str2);
        remoteViews.setTextViewText(RInformation.ID_TEXT_NOTIFICATION, str3);
        String formatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String shortTime = formatted.substring(formatted.length() - 8, formatted.length() - 3); // HH:mm
        remoteViews.setTextViewText(RInformation.ID_TIME_NOTIFICATION_DCLOUD, shortTime);        notification.contentView = remoteViews;
        notification.contentIntent = pendingIntent;
        notification.flags |= 16;
        notification.defaults = 1;
        ((NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(i, notification);
    }

    public static void showNotification(Context context, String str, String str2, PendingIntent pendingIntent, Bitmap bitmap, int i) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        try {
            notification.icon = context.createPackageContext(context.getPackageName(), Context.CONTEXT_IGNORE_SECURITY).getResources().getIdentifier(AbsoluteConst.JSON_KEY_ICON, R.drawable.class.getSimpleName(), context.getPackageName());
        } catch (Exception unused) {
        }
        notification.largeIcon = bitmap;
        notification.defaults = 1;
        notification.flags = 16;
//        notification.setLatestEventInfo(context, str, str2, pendingIntent);
        notificationManager.notify(i, notification);
    }

    public static void cancelNotification(Context context, int i) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(i);
    }
}
