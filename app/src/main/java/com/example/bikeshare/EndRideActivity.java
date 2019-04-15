package com.example.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikeshare.manageBikes.Bike;

import java.util.ArrayList;
import java.util.List;

public class EndRideActivity extends AppCompatActivity {

    private static RidesDB sRidesDB;
    private SpinnerAdapter mAdapterSpinner;

    private Button mEndRide;
    private TextView mLastAdded;
    private Spinner mSpinnerBikeName;
    private TextView mNewWhere;
    private Bike mBike;
    private String mBikeName;

    private Ride mLast = new Ride ("", "","");

    @Override
    protected void onResume() {
        super.onResume();
        updateSpinner();
    }

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
        mSpinnerBikeName = (Spinner) findViewById(R.id.spinner_end_ride);
        mNewWhere = (TextView) findViewById(R.id.end_where_text);

        mSpinnerBikeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBike = (Bike) parent.getItemAtPosition(position);
                mBikeName = mBike.getBikeName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!mBikeName.equals("Choose Bike")) && (mNewWhere.getText().length() > 0)){
                    mLast.setBikeName(mBikeName); //trim remove the spaces in front and end
                    mLast.setEndRide(mNewWhere.getText().toString().trim());

                    sRidesDB.endRide(mLast.getBikeName(), mLast.getEndRide());

                    mNewWhere.setText("");
                    updateUI();
                    mSpinnerBikeName.setSelection(0);
                    mAdapterSpinner = null;
                    updateSpinner();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: Bike ride not ended", Toast.LENGTH_LONG).show();
                }
            }
        });

        updateSpinner();
    }

    private void updateSpinner(){

        List<Bike> mBikesAvailableList = new ArrayList<Bike>();
        Bike noBikeSelected = new Bike("Choose Bike");
        mBikesAvailableList.add(noBikeSelected);
        for (Bike bike : sRidesDB.getBikes()){
            if(!sRidesDB.bikeAvailability(bike)) {
                mBikesAvailableList.add(bike);
            }
        }

        if(mAdapterSpinner == null) {
            mAdapterSpinner = new SpinnerAdapter(this, mBikesAvailableList);
            mSpinnerBikeName.setAdapter(mAdapterSpinner);
        }else{
            //mAdapter.setRides(mRidesList);
            mAdapterSpinner.notifyDataSetChanged();
        }
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
