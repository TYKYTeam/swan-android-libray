package com.tyky.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.tyky.notification.activity.H5WebviewActivity;

import androidx.core.app.NotificationCompat;

/**
 * 通知栏工具
 */
public class NotifyUtil {

    private static String channel_id="myChannelId";
    private static String channel_name="新消息";
    private static String description = "新消息通知";
    private static int notifyId = 0;
    private static NotificationManager notificationManager;


    public static void createNotificationChannel(){
        if (notificationManager != null) {
            return;
        }
        //Android8.0(API26)以上需要调用下列方法，但低版本由于支持库旧，不支持调用
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id,channel_name,importance);
            channel.setDescription(description);
            notificationManager = (NotificationManager) ActivityUtils.getTopActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }else{
            notificationManager = (NotificationManager) ActivityUtils.getTopActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    public static void sendNotification(String title,String text,String url){
        createNotificationChannel();

        Activity topActivity = ActivityUtils.getTopActivity();
        Intent intent = new Intent(topActivity, H5WebviewActivity.class);
        intent.putExtra("url", url);
        //适配android12以上版本
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            pendingIntent = PendingIntent.getActivity(topActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(topActivity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        Notification notification = new NotificationCompat.Builder(topActivity,channel_id)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(ResourceUtils.getDrawableIdByName("ic_launcher"))
                .setLargeIcon(BitmapFactory.decodeResource(topActivity.getResources(),ResourceUtils.getDrawableIdByName("ic_launcher")))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notifyId++,notification);
    }

    public static void sendNotification(String title,String text){
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(ActivityUtils.getTopActivity(),channel_id)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(ResourceUtils.getDrawableIdByName("ic_launcher"))
                .setLargeIcon(BitmapFactory.decodeResource(ActivityUtils.getTopActivity().getResources(),ResourceUtils.getDrawableIdByName("ic_launcher")))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        notificationManager.notify(notifyId++,notification);
    }

    public static void sendNotification(String title,String text,int priority){
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(ActivityUtils.getTopActivity(),channel_id)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(ResourceUtils.getDrawableIdByName("ic_launcher"))
                .setLargeIcon(BitmapFactory.decodeResource(ActivityUtils.getTopActivity().getResources(),ResourceUtils.getDrawableIdByName("ic_launcher")))
                .setPriority(priority)
                .build();
        notificationManager.notify(notifyId++,notification);
    }

    public static void sendNotification(String title, String text, int priority, PendingIntent pendingIntent){
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(ActivityUtils.getTopActivity(),channel_id)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(ResourceUtils.getDrawableIdByName("ic_launcher"))
                .setLargeIcon(BitmapFactory.decodeResource(ActivityUtils.getTopActivity().getResources(),ResourceUtils.getDrawableIdByName("ic_launcher")))
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(notifyId++,notification);
    }
}

