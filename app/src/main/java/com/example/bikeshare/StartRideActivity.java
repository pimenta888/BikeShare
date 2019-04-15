package com.example.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class StartRideActivity extends AppCompatActivity {

    private static RidesDB sRidesDB;
    private SpinnerAdapter mAdapterSpinner;

    private Button mAddRide;
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
        setContentView(R.layout.activity_start_ride);

        sRidesDB = RidesDB.get(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLastAdded = (TextView) findViewById(R.id.last_ride);

        updateUI();

        mAddRide = (Button) findViewById(R.id.add_button);
        mSpinnerBikeName = (Spinner) findViewById(R.id.spinner_start_ride);
        mNewWhere = (TextView) findViewById(R.id.where_text);

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

        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!mBikeName.equals("Choose Bike")) && (mNewWhere.getText().length() > 0)){
                    mLast.setBikeName(mBikeName);
                    mLast.setStartRide(mNewWhere.getText().toString().trim());

                    sRidesDB.addRide(new Ride(mLast.getBikeName(),mLast.getStartRide(),"Not finished"));

                    mNewWhere.setText("");
                    updateUI();
                    mSpinnerBikeName.setSelection(0);
                    mAdapterSpinner = null; //to refresh the spinner
                    updateSpinner();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: Bike ride not started", Toast.LENGTH_LONG).show();
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
            if(sRidesDB.bikeAvailability(bike)) {
                mBikesAvailableList.add(bike);
            }
        }

        if(mAdapterSpinner == null) {
            mAdapterSpinner = new SpinnerAdapter(this, mBikesAvailableList);
            mSpinnerBikeName.setAdapter(mAdapterSpinner);
        }else{
            mAdapterSpinner.notifyDataSetChanged();
        }
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
