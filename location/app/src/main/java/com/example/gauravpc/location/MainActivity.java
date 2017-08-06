package com.example.gauravpc.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    double myLat, myLang;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    MyLocListener myLocListener;
    double minDistanceForDytila=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView getLocation = (TextView) findViewById(R.id.text);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        //This is called if user has denied the permission before
                        //In this case I am just asking the permission again
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    }
                    else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        Log.e("yes", "yes");
                    }

                } else {
                    int off = 0;
                    try {
                        off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (off == 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilder.setMessage("Oops! GPS is OFF... would you like to enable it ?");
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(onGPS);
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getLocation.setText("NO permission granted!");
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {

                        myLocListener = new MyLocListener(MainActivity.this);
                        if (myLocListener.canGetLocation()) {
                            myLat = myLocListener.getLat();
                            myLang = myLocListener.getLang();
                            Toast.makeText(getApplicationContext(), "You Location : " + myLat + " " + myLang, Toast.LENGTH_SHORT).show();
                        }

                        //to find the distance between my location and dytila kitchen
                        double dk_lat = 12.968015, dk_lang = 79.155108;
                        int r = 6371;
                        double latD = Math.toRadians(dk_lat - myLat);
                        double longD = Math.toRadians(dk_lang - myLang);
                        double a = Math.sin(latD / 2) * Math.sin(latD / 2) + Math.cos(Math.toRadians(dk_lat)) * Math.cos(Math.toRadians(myLat))
                                * Math.sin(longD / 2) * Math.sin(longD / 2);
                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                        double distance = r * c * 1000;//convert to meters
                        if(distance<minDistanceForDytila){
                            getLocation.setText("your distance from kitchen : " + df2.format(distance) + "m\n\tYou can order Food");
                        }
                        else{
                            getLocation.setText("your distance from kitchen : " + df2.format(distance) + "m\n\tSorry dytila is not available in your area!");
                        }

                        //end

                    }

                }
            }
        });
    }
}

