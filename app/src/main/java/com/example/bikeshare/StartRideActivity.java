package com.example.bikeshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartRideActivity extends AppCompatActivity {

    private static final String ADD_WHAT = "com.example.bikeshare.add_what";
    private static final String ADD_WHERE = "com.example.bikeshare.add_where";

    private Button mAddRide;
    private TextView mLastAdded;
    private TextView mNewWhat;
    private TextView mNewWhere;

    private Ride mLast = new Ride ("", "","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

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
                    returnResult();
                }
            }
        });
    }

    private void returnResult(){
        Intent data = new Intent();
        data.putExtra(ADD_WHAT,mLast.getBikeName());
        data.putExtra(ADD_WHERE,mLast.getStartRide());
        setResult(Activity.RESULT_OK, data);
    }

    public static String getAddWhatBike(Intent result){
        return result.getStringExtra(ADD_WHAT);
    }

    public static String getAddWhere(Intent result){
        return result.getStringExtra(ADD_WHERE);
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, StartRideActivity.class);
        return intent;
    }

    private void updateUI(){
        mLastAdded.setText(mLast.toString());
    }
}
