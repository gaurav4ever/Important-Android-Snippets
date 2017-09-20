package com.example.gauravpc.notificationmanagerusingalarmamanager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Gaurav Pc on 9/2/2017.
 */

public class NotificationService extends Service {
    private android.os.Handler handler;

    String TAG="Notification :";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                makeNotification();
            }
        };
        handler.post(runnable);
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(this,ScreenReciever.class);
        sendBroadcast(intent);
    }
    public void makeNotification(){

    }
}
