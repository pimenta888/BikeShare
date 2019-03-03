package com.example.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndRideActivity extends AppCompatActivity {

    private static RidesDB sRidesDB;

    private Button mEndRide;
    private TextView mLastAdded;
    private TextView mNewWhat;
    private TextView mNewWhere;

    private Ride mLast = new Ride ("", "","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);

        sRidesDB = RidesDB.get(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLastAdded = (TextView) findViewById(R.id.end_last_ride);

        updateUI();

        mEndRide = (Button) findViewById(R.id.end_button);
        mNewWhat = (TextView) findViewById(R.id.end_what_text);
        mNewWhere = (TextView) findViewById(R.id.end_where_text);

        mEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mNewWhat.getText().length() > 0) && (mNewWhere.getText().length() > 0)){
                    mLast.setBikeName(mNewWhat.getText().toString().trim()); //trim remove the spaces in front and end
                    mLast.setEndRide(mNewWhere.getText().toString().trim());

                    if (containsBikeName(mLast.getBikeName())) sRidesDB.endRide(mLast.getBikeName(), mLast.getEndRide());

                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                }
            }
        });
    }

    public boolean containsBikeName(String bikeName){
        for (Ride ride: sRidesDB.getRidesDB()) {
            if (ride.getBikeName().equals(bikeName)) return true;
        }
        return false;
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, EndRideActivity.class);
        return intent;
    }

    private void updateUI(){
        mLastAdded.setText(mLast.toStringEnd());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
