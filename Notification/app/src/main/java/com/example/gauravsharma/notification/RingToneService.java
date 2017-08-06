package com.example.gauravsharma.notification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Gaurav Sharma on 10-06-2017.
 */

public class RingToneService extends Service{

    MediaPlayer mediaPlayer;
    boolean isRunning;
    private int startId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        String state=intent.getStringExtra("extra");

        assert state!=null;
        switch (state) {
            case "on":
                startId = 1;
                break;
            case "off":
                startId = 0;
                Log.e("Start ID is ", state);
                break;
            default:
                startId = 0;
                break;
        }

        // if else statements

        // if there is no music playing, and the user pressed "alarm on"
        // music should start playing
        if (!this.isRunning && startId == 1) {
            Toast.makeText(this, "if there is no music playing, and the user pressed \"alarm on\"", Toast.LENGTH_LONG).show();

            mediaPlayer=MediaPlayer.create(this,R.raw.song);
            mediaPlayer.start();

            this.isRunning = true;
            this.startId = 0;

        }

        // if there is music playing, and the user pressed "alarm off"
        // music should stop playing
        else if (this.isRunning && startId == 0) {
            Log.e("there is music, ", "and you want end");
            Toast.makeText(this, "there is music,and you want end", Toast.LENGTH_LONG).show();

            // stop the ringtone
            mediaPlayer.stop();
            mediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        // these are if the user presses random buttons
        // just to bug-proof the app
        // if there is no music playing, and the user pressed "alarm off"
        // do nothing
        else if (!this.isRunning && startId == 0) {
            Toast.makeText(this, "there is no music,and you want end", Toast.LENGTH_LONG).show();
            this.isRunning = false;
            this.startId = 0;

        }

        // if there is music playing and the user pressed "alarm on"
        // do nothing
        else if (this.isRunning && startId == 1) {
            Log.e("there is music, ", "and you want start");

            this.isRunning = true;
            this.startId = 1;

        }

        // can't think of anything else, just to catch the odd event
        else {
            Log.e("else ", "somehow you reached this");

        }
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning=false;
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
