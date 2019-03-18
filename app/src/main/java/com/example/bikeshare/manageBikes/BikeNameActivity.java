package com.example.bikeshare.manageBikes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bikeshare.R;

import java.util.UUID;

public class BikeNameActivity extends AppCompatActivity {
    private static final String EXTRA_BIKE_ID = "com.example.bikeshare.bike_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_name);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        };
    }

    protected Fragment createFragment(){
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_BIKE_ID);
        return BikeNameFragment.newInstance(uuid);
    }

    public static Intent newIntent(Context packageContext, UUID uuid) {
        Intent intent = new Intent(packageContext, BikeNameActivity.class);
        intent.putExtra(EXTRA_BIKE_ID, uuid);
        return intent;
    }
}
