package com.example.bikeshare.manageBikes;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bikeshare.R;
import com.example.bikeshare.RidesDB;

import java.util.UUID;

public class BikeNameFragment extends Fragment {

    private static final String ARGS_BIKE_ID = "bike_id";

    private Bike mBike;
    private EditText mBikeName;
    private CheckBox mAvailable;

    public static BikeNameFragment newInstance(UUID uuid){
        Bundle args =new Bundle();
        args.putSerializable(ARGS_BIKE_ID, uuid);

        BikeNameFragment fragment = new BikeNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_bike_name, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_bike:
                RidesDB.get(getActivity()).removeBike(mBike);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID bikeID = (UUID) getArguments().getSerializable(ARGS_BIKE_ID);
        mBike = RidesDB.get(getActivity()).getBike(bikeID);
    }

    @Override
    public void onPause() {
        super.onPause();

        RidesDB.get(getActivity()).updateBikeName(mBike);

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bike_name, container, false);

        mBikeName = (EditText) v.findViewById(R.id.bike_name);
        mBikeName.setText(mBike.getBikeName());
        mBikeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBike.setBikeName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAvailable = (CheckBox) v.findViewById(R.id.bike_available);
        if (mBike.isAvailable()){
            mAvailable.setChecked(true);
        } else{
            mAvailable.setChecked(false);
        }
        return v;
    }
}
