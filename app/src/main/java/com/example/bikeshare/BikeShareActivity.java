package com.example.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bikeshare.Users.SignUp_In;
import com.example.bikeshare.Users.User;

public class BikeShareActivity extends AppCompatActivity {

    private static final String EXTRA_USER_EMAIL = "com.example.bikeshare.user_id";

    private static RidesDB mRidesDB;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);

        mUser = userSession();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById((R.id.fragment_container));

        if (fragment == null) {
            fragment = new BikeShareFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    public User userSession(){
        mRidesDB = RidesDB.get(this);
        for (User userOnline: mRidesDB.getUsers()) {
            if(userOnline.isStatus() == true){
                return userOnline;
            }
        }
        return null;
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, BikeShareActivity.class);
        return intent;
    }

}