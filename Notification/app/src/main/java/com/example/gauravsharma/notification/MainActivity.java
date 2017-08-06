package com.example.gauravsharma.notification;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

import static android.R.attr.delay;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    TextView t1,t2,t3;
    TimePicker timePicker;
    Context context;
    PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context=this;

        //initialize our alarm manager
        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        //initialize our time picker
        timePicker=(TimePicker)findViewById(R.id.timePicker);

        t1=(TextView)findViewById(R.id.textView1); //alarm on
        t2=(TextView)findViewById(R.id.textView2); //alarm off
        t3=(TextView)findViewById(R.id.textView3); //status

        final Calendar calendar=Calendar.getInstance();

        final Intent i=new Intent(MainActivity.this,AlarmReciver.class);

        t1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());

                int hour=timePicker.getHour();
                int min=timePicker.getMinute();

                String hour_val = null;
                String min_val=null;

                if(hour>12){
                    hour_val=""+(hour-12);
                }else{
                    hour_val=""+hour;
                }
                if(min<10){
                    min_val="0"+min;
                }else{
                    min_val=""+min;
                }

                setAlarmText("Alarm Set : "+hour_val+":"+min_val);

                //send extra string into the intent
                i.putExtra("extra","on");

                //create a pending intent of the specified the calender time
                pi=PendingIntent.getBroadcast(getBaseContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmText("Alarm Off");
                i.putExtra("extra","off");
                alarmManager.cancel(pi);
                sendBroadcast(i);
            }
        });
    }

    private void setAlarmText(String x) {
        t3.setText(x);
    }
}
