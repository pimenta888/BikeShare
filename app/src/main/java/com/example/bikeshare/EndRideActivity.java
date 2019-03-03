package com.example.bikeshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndRideActivity extends AppCompatActivity {

    private static final String END_WHAT = "com.example.bikeshare.end_what";
    private static final String END_WHERE = "com.example.bikeshare.end_where";

    private Button mEndRide;
    private TextView mLastAdded;
    private TextView mNewWhat;
    private TextView mNewWhere;

    private Ride mLast = new Ride ("", "","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);

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

                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                    returnResult();
                }
            }
        });
    }

    public void returnResult(){
        Intent data = new Intent();
        data.putExtra(END_WHAT, mLast.getBikeName());
        data.putExtra(END_WHERE, mLast.getEndRide());
        setResult(Activity.RESULT_OK, data);
    }

    public static String getEndWhatBike(Intent result){
        return result.getStringExtra(END_WHAT);
    }

    public static String getEndWhere(Intent result){
        return  result.getStringExtra(END_WHERE);
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
