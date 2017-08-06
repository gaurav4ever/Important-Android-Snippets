package com.example.gauravsharma.notification;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Gaurav Sharma on 10-06-2017.
 */

public class AlarmReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("hi","up");

        //
        String intent_string=intent.getStringExtra("extra");

        //create an intent to the ring tone service class
        Intent i=new Intent(context,RingToneService.class);
        i.putExtra("extra",intent_string);
        context.startService(i);

    }
}
