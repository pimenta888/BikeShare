package com.example.bikeshare;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BikeShareActivity extends AppCompatActivity {

    private Button mAddRide ;
    private TextView mLastAdded ;
    private TextView mNewWhat ;
    private TextView mNewWhere ;

    private Ride mLast = new Ride ("", "");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);

        /*Display API Level */
        TextView Level = (TextView) findViewById(R.id.api_level);
        Level.setText("API Level " + Build.VERSION.SDK_INT);

        mLastAdded = (TextView) findViewById(R.id.last_ride);
        updateUI();

        mAddRide = (Button) findViewById(R.id.add_button);
        mNewWhat = (TextView) findViewById(R.id.what_text);
        mNewWhere = (TextView) findViewById(R.id.where_text);

        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mNewWhat.getText().length() > 0) && (mNewWhere.getText().length() > 0)){
                    mLast.setBikeName(mNewWhat.getText().toString().trim()); //trim remove the spaces in front and end
                    mLast.setStartRide(mNewWhere.getText().toString().trim());

                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                }
            }
        });
    }

    private void updateUI(){
        mLastAdded.setText(mLast.toString());
    }
}