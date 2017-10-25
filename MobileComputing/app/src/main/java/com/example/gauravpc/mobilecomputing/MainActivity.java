package com.example.gauravpc.mobilecomputing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView addItemImage;
    String msg;
    ImageView sendImg;
    EditText itemTextView;
    DatabaseHandler db;
    ListView listView;
    MsgAdapter msgAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendImg=(ImageView) findViewById(R.id.sendmsg);
        listView=(ListView)findViewById(R.id.listView);

        db= new DatabaseHandler(MainActivity.this);

        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTextView=(EditText)findViewById(R.id.text);
                String msg=itemTextView.getText().toString();
//                tv.setText(msg);
                DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
                Date date = new Date();
                String noteDate=df.format(date);
                sendMessage(msg,noteDate);
            }
        });
        makeList();
//        msg=itemTextView.getText().toString();
//        String s=stringFromJNI(msg);

    }

    public void sendMessage(final String msg,final String noteDate){
        String url="https://buckupapp.herokuapp.com/mobile/data";
        RequestQueue requestQueue=new Volley().newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        db.addMsg(new MsgModel("gaurav","prem",msg,"key","e***"+msg,noteDate));
                        makeList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("status","failed");
                Log.d("error",""+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("user","gaurav");
                params.put("msg",msg);
                params.put("date",noteDate);
                return params;
            }
        };
        int socketTimeout = 10000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    public void makeList(){
        ArrayList<MsgModel> msgModelList=db.viewMsg();
        msgAdapter=new MsgAdapter(getApplicationContext(),R.layout.row_msg,msgModelList);
        listView.setAdapter(msgAdapter);
    }
    public static native String stringFromJNI(String msg);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }



    public class MsgAdapter extends ArrayAdapter{
        private LayoutInflater inflater;
        public ArrayList<MsgModel>msgModelList;
        private int resource;
        public MsgAdapter(Context context, int resource, ArrayList<MsgModel> msgModelList) {
            super(context, resource, msgModelList);
            this.msgModelList = msgModelList;
            this.resource = resource;
            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return msgModelList.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return msgModelList.get(position);
        }

        class ViewHolder{
            TextView username;
            TextView msg;
            TextView date;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.row_msg,null);
                viewHolder =new ViewHolder();
                viewHolder.username=(TextView)convertView.findViewById(R.id.by);
                viewHolder.msg=(TextView)convertView.findViewById(R.id.msg);
                viewHolder.date=(TextView)convertView.findViewById(R.id.date);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.username.setText(msgModelList.get(position).getFrom());
            viewHolder.msg.setText(msgModelList.get(position).getOmsg());
            String date=parseDate(msgModelList.get(position).getDate());
            DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
            DateFormat df_time = new SimpleDateFormat("HH:mm:ss");
            Date datee = new Date();
            String noteDate=parseDate(df.format(datee));
            String time=df_time.format(datee);
            viewHolder.date.setText(date+" @"+time);
            return convertView;
        }
    }
    public String parseDate(String date){
        DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        Date fetchedDate = null;
        try {
            fetchedDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(fetchedDate);
        String day_now= ""+cal.get(Calendar.DAY_OF_MONTH);
        String day_now_ordinal=ordinal(cal.get(Calendar.DAY_OF_MONTH));
        String month_now = month_date.format(cal.getTime())+"";
        String year_now= String.valueOf(cal.get(Calendar.YEAR));

        return day_now+""+day_now_ordinal+" "+month_now+" "+year_now;
    }
    public static String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return "th";
            default:
                return suffixes[i % 10];

        }
    }
}
