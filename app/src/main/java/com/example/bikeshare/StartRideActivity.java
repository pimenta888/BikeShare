package com.example.bikeshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikeshare.manageBikes.Bike;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StartRideActivity extends AppCompatActivity {

    private static RidesDB sRidesDB;
    private SpinnerAdapter mAdapterSpinner;

    private Button mAddRide;
    private Spinner mSpinnerBikeName;
    private TextView mNewWhere;
    private Bike mBike;
    private String mBikeName;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private ArrayList<String> mPermissions = new ArrayList<>();
    private static  final int ALL_PERMISSIONS_RESULT = 1011;
    private double longitude;
    private double latitude;
    private String address;
    private int showMap = 1;

    private Ride mLast = new Ride ("", "","");

    @Override
    protected void onResume() {
        super.onResume();
        updateSpinner();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

    private void stopLocationUpdates(){
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    private String getAddress(double longitude, double latitude){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        StringBuilder stringBuilder = new StringBuilder();
        try{
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0){
                Address address = addresses.get(0);
                stringBuilder.append(address.getAddressLine(0));
            }else{
                return "No address found";
            }
        }catch (IOException ex){
            return "No address found";
        }
        return stringBuilder.toString();
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> permissions){
        ArrayList<String> result = new ArrayList<>();
        for (String permission : permissions){
            if (!hasPermission(permission)){
                result.add(permission);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return Objects.requireNonNull(this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

        sRidesDB = RidesDB.get(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAddRide = (Button) findViewById(R.id.add_button);
        mSpinnerBikeName = (Spinner) findViewById(R.id.spinner_start_ride);
        mNewWhere = (TextView) findViewById(R.id.where_text);
        mNewWhere.setKeyListener(null);

        mPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        mPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        mPermissions.add(Manifest.permission.READ_CONTACTS);

        ArrayList<String> mPermissionsToRequest = permissionsToRequest(mPermissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (mPermissionsToRequest.size() > 0) {
                requestPermissions(mPermissionsToRequest.toArray(new String[mPermissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null) return;
                for(Location location : locationResult.getLocations()){
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    address = getAddress(longitude, latitude);
                    mNewWhere.setText(address);
                }

                if(showMap == 1){
                    FragmentManager fm = getSupportFragmentManager();
                    SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latlng = new LatLng(latitude, longitude);
                            googleMap.addMarker(new MarkerOptions().position(latlng).title("Current Location"));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18f));
                        }
                    });
                    fm.beginTransaction().add(R.id.map, mapFragment).commit();
                }
                showMap = 2;
            }
        };


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

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
                    mLast.setStartRide(address);

                    sRidesDB.addRide(new Ride(mLast.getBikeName(),mLast.getStartRide(),"Not finished"));

                    mNewWhere.setText("Loading...");
                    mSpinnerBikeName.setSelection(0);
                    mAdapterSpinner = null; //to refresh the spinner
                    updateSpinner();
                    showMap = 1;
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
