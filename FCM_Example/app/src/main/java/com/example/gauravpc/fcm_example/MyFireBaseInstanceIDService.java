package com.example.gauravpc.fcm_example;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Gaurav Pc on 8/8/2017.
 */

public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN="REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        String firebase_token= FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,firebase_token);
    }
}
