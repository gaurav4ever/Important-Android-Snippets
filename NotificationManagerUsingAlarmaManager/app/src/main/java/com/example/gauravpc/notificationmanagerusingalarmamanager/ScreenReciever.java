package com.example.gauravpc.notificationmanagerusingalarmamanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Gaurav Pc on 9/2/2017.
 */

public class ScreenReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, NotificationService.class);
        context.startService(background);
    }
}
