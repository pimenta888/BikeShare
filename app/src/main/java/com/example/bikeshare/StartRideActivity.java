package com.example.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartRideActivity extends AppCompatActivity {

    private static RidesDB sRidesDB;

    private Button mAddRide;
    private TextView mLastAdded;
    private TextView mNewWhat;
    private TextView mNewWhere;

    private Ride mLast = new Ride ("", "","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

        sRidesDB = RidesDB.get(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

                    sRidesDB.addRide(new Ride(mLast.getBikeName(),mLast.getStartRide(),"Not finished"));

                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                }
            }
        });
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, StartRideActivity.class);
        return intent;
    }

    private void updateUI(){
        mLastAdded.setText(mLast.toString());
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
