package com.example.gauravpc.mobilecomputing;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView addItemImage;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addItemImage=(ImageView)findViewById(R.id.add);
        addItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this);
                View parentView=getLayoutInflater().inflate(R.layout.layout_additem_todo_bottom_sheet, null);
                final EditText itemTextView=(EditText)parentView.findViewById(R.id.itemText);
                CardView addItemCardView=(CardView)parentView.findViewById(R.id.addItemCard);
                addItemCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        msg=itemTextView.getText().toString();
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(parentView);
                bottomSheetDialog.show();
            }
        });

        String s=stringFromJNI(msg);
        TextView tv = (TextView) findViewById(R.id.result);
        tv.setText(s);
    }
    public native String stringFromJNI(String msg);

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
}
