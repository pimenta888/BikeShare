package com.example.bikeshare;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BikeShareActivity extends AppCompatActivity {

    private Button mAddRide ;
    private Button mEndRide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);

        /*Display API Level */
        TextView Level = (TextView) findViewById(R.id.api_level);
        Level.setText("API Level " + Build.VERSION.SDK_INT);


        mAddRide = (Button) findViewById(R.id.main_add_ride_button);
        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StartRideActivity.newIntent(BikeShareActivity.this);
                startActivity(intent);
            }
        });

        mEndRide = (Button) findViewById(R.id.main_end_ride_button);
        mEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EndRideActivity.newIntent(BikeShareActivity.this);
                startActivity(intent);
            }
        });

    }
}