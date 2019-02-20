package com.example.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndRideActivity extends AppCompatActivity {

    private Button mEndRide ;
    private TextView mLastAdded ;
    private TextView mNewWhat ;
    private TextView mNewWhere ;

    private Ride mLast = new Ride ("", "","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);

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
                    mLast.setStartRide(mNewWhere.getText().toString().trim());

                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                }
            }
        });
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, EndRideActivity.class);
        return intent;
    }

    private void updateUI(){
        mLastAdded.setText(mLast.toString());
    }
}