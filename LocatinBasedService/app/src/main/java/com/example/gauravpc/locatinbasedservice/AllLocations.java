package com.example.gauravpc.locatinbasedservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class AllLocations extends AppCompatActivity {

    private ProgressDialog pDialog;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_locations);

        pDialog = new ProgressDialog(AllLocations.this);
        pDialog.setMessage("Finding nearest location...");
        pDialog.setCancelable(false);

        listView=(ListView)findViewById(R.id.list);
        boolean check = isNetworkAvailable();
        if (check == true) {
            getData();
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getData(){
        String url="https://dytila.herokuapp.com/api/places_kitchens";
        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    List<MapModel> mapModelArrayList=new ArrayList<>();
                    JSONArray parentArray=response.getJSONArray("kitchens");

                    for(int i=0;i<parentArray.length();i++){
                        JSONObject finalObject=parentArray.getJSONObject(i);
                        MapModel mapModel=new MapModel();
                        mapModel.setId(finalObject.getString("kitchen_id"));
                        mapModel.setAdd(finalObject.getString("address"));
                        mapModel.setLat(finalObject.getString("latitude"));
                        mapModel.setLang(finalObject.getString("longitude"));
                        mapModel.setMobile(finalObject.getString("mobile"));
                        mapModelArrayList.add(mapModel);
                    }
                    postExecute(mapModelArrayList);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong :( Please try again!", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void postExecute(List<MapModel> result){

        FavMealModelAdapter favMealModelAdapter=new FavMealModelAdapter(getApplicationContext(),R.layout.row_location,result);
        listView.setAdapter(favMealModelAdapter);
    }
    class FavMealModelAdapter extends ArrayAdapter {
        public List<MapModel> mapModelList;
        private int resource;

        private LayoutInflater inflater;

        public FavMealModelAdapter(Context context, int resource, List<MapModel> objects) {
            super(context, resource, objects);
            mapModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_location, null);
            }

            TextView addTextView,latTextView,langTextView;

            addTextView=(TextView)convertView.findViewById(R.id.address);
            latTextView=(TextView)convertView.findViewById(R.id.lat);
            langTextView=(TextView)convertView.findViewById(R.id.lang);

            addTextView.setText(mapModelList.get(position).getAdd());
            latTextView.setText(mapModelList.get(position).getLat());
            langTextView.setText((mapModelList.get(position).getLang()));


            return convertView;
        }

    }
}
