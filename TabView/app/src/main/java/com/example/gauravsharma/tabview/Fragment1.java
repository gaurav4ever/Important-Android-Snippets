package com.example.gauravsharma.tabview;

import android.app.TabActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;

/**
 * Created by Gaurav Sharma on 18-05-2017.
 */
public class Fragment1  extends Fragment{

    private ViewPager viewPager;
    RelativeLayout Layout1,Layout2,Layout3,Layout4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment1,null);

        viewPager=(ViewPager)getActivity().findViewById(R.id.viewpager);

        Layout1=(RelativeLayout)v.findViewById(R.id.l1);
        Layout2=(RelativeLayout)v.findViewById(R.id.l1);
        Layout3=(RelativeLayout)v.findViewById(R.id.l1);
        Layout4=(RelativeLayout)v.findViewById(R.id.l1);

        Layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        return v;
    }
}
