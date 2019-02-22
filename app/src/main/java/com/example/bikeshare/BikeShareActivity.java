package com.example.bikeshare;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BikeShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById((R.id.fragment_container));

        if (fragment == null) {
            fragment = new BikeShareFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

}