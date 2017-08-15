package com.example.gauravpc.notificationproject;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification=new Notification.Builder(getApplicationContext())
                        .setContentTitle("hello")
                        .setContentText("yoyoyo")
                        .setSmallIcon(R.mipmap.ic_launcher).build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0,notification);
            }
        });

    }
}
