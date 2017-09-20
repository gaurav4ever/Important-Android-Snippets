package com.example.gauravpc.locatinbasedservice;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class AllLocations extends AppCompatActivity {

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_locations);

        pDialog = new ProgressDialog(AllLocations.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        favMealsListView=(ListView)findViewById(R.id.favMealsList);
        boolean check = isNetworkAvailable();
        if (check == true) {
            getData(userid_val);
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
