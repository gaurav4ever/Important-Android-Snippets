package com.example.gauravpc.locatinbasedservice;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //variable to find nearest dytila kitchen
    private ProgressDialog pDialog;
    double myLat, myLang;
    MyLocListener myLocListener;
    List<MapModel> mapModelList=new ArrayList<>();

    TextView responseTextView,buttonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseTextView=(TextView)findViewById(R.id.reponseText);
        buttonTextView=(TextView)findViewById(R.id.button);

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDytilaKitchen();
            }
        });
    }

    //functions to find nearest dytila kitchens
    public void findDytilaKitchen(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //user did not gave permission or did not gave any response
                //Ask again for permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //Ask for permission first time
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                alertDialogBuilder.setMessage("We are unable to find any location near you because GPS is OFF... would you like to enable it ?");
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
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {

                myLocListener = new MyLocListener(MainActivity.this);
                if (myLocListener.canGetLocation()) {
                    myLat = myLocListener.getLat();
                    myLang = myLocListener.getLang();
                }

                findKitchens();
            }
        }
    }
    public void findKitchens(){

        final double minD[]={5000};
        final double min_lat[] = new double[1],min_lang[]= new double[1];
        final String[] kitchen_id = new String[1];
        final String[] add = new String[1];
        final String[] mobile = new String[1];
        String url="https://dytila.herokuapp.com/api/places_kitchens";
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Finding Nearest location...");
        pDialog.setCancelable(false);

        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray parentArray=response.getJSONArray("kitchens");

                    double dk_lat,dk_lang;

                    for(int i=0;i<parentArray.length();i++){
                        JSONObject finalObject=parentArray.getJSONObject(i);
                        MapModel mapModel=new MapModel();
                        mapModel.setId(finalObject.getString("kitchen_id"));
                        mapModel.setLat(finalObject.getString("latitude"));
                        mapModel.setLang(finalObject.getString("longitude"));
                        mapModel.setAdd(finalObject.getString("address"));
                        mapModel.setMobile(finalObject.getString("mobile"));

                        dk_lat=Double.parseDouble(finalObject.getString("latitude"));
                        dk_lang=Double.parseDouble(finalObject.getString("longitude"));
                        int r = 6371;
                        double latD = Math.toRadians(dk_lat - myLat);
                        double longD = Math.toRadians(dk_lang - myLang);
                        double a = Math.sin(latD / 2) * Math.sin(latD / 2) + Math.cos(Math.toRadians(dk_lat)) * Math.cos(Math.toRadians(myLat))
                                * Math.sin(longD / 2) * Math.sin(longD / 2);
                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                        double distance = r * c * 1000;//convert to meters
                        if (distance < minD[0]) {
                            kitchen_id[0]=finalObject.getString("kitchen_id");
                            minD[0]=distance;
                            min_lat[0]=dk_lat;
                            min_lang[0]=dk_lang;
                            add[0] =finalObject.getString("address");
                            mobile[0]=finalObject.getString("mobile");
                        }

                        mapModelList.add(mapModel);
                    }
                    pDialog.dismiss();
                    postExecute(kitchen_id[0],minD[0],min_lat[0],min_lang[0],add[0],mobile[0]);
                    //check if user distance is less than 500m from any dytila kitchen
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Some Thing Went Wrong! Please Try Again!", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Some Thing Went Wrong! Please Try Again!",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void postExecute(String kitchen_id,double minD,double lat,double lang,String add,String mobile){

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        if(minD==5000){
            responseTextView.setText("No location found");
        }
        else{
            responseTextView.setText("Distance :"+minD+"m\n\nLatitude : "+lat+"\nLongitude : "+lang+"\n\nAddress : "+add);
        }
    }
    //End functions
}
