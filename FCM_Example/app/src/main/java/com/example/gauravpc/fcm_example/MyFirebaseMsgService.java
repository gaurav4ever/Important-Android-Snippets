package com.example.gauravpc.fcm_example;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Gaurav Pc on 8/8/2017.
 */

public class MyFirebaseMsgService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilderr=new NotificationCompat.Builder(this);
        notificationBuilderr.setContentTitle("FCM Notification");
        notificationBuilderr.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilderr.setAutoCancel(true);
        notificationBuilderr.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilderr.setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilderr.build());
    }
}
