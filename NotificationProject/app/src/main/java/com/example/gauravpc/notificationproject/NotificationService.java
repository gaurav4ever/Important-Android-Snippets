package com.example.gauravpc.notificationproject;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Gaurav Pc on 8/16/2017.
 */

public class NotificationService extends Service {
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 10;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(this,ScreenReciever.class);
        sendBroadcast(intent);
    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask,Your_X_SECS * 1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void run() {

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        NotificationCompat.Builder  mBuilder =
                                new NotificationCompat.Builder(getApplicationContext());
                        mBuilder.setAutoCancel(true);
                        mBuilder.setContentTitle("New Message");
                        mBuilder.setContentText("You have   new message.");
                        mBuilder.setTicker("New message from PayMe..");
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        mBuilder.setSound(alarmSound);
                        notificationManager.notify(0, mBuilder.build());

                    }
                });
            }
        };
    }
}
