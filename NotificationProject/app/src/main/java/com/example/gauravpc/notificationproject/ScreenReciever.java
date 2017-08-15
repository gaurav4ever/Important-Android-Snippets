package com.example.gauravpc.notificationproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Gaurav Pc on 8/16/2017.
 */

public class ScreenReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, NotificationService.class);
        context.startService(background);
    }
}
