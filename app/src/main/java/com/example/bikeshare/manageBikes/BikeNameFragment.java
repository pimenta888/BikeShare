package com.example.bikeshare.manageBikes;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikeshare.BikeShareFragment;
import com.example.bikeshare.R;
import com.example.bikeshare.Ride;
import com.example.bikeshare.RidesDB;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class BikeNameFragment extends Fragment {

    private static final String ARGS_BIKE_ID = "bike_id";
    private static final int REQUEST_PHOTO = 2;

    private Bike mBike;
    private EditText mBikeName;
    private CheckBox mAvailable;
    private EditText mLockID;
    private EditText mPrice;
    private boolean correctName;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;

    private RecyclerView mEachBikeRecyclerView;
    private RidesDB mRidesDB;
    private BikeAdapter2 mAdapter;


    public static BikeNameFragment newInstance(UUID uuid){
        Bundle args = new Bundle();
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
            case android.R.id.home:
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
        mPhotoFile = RidesDB.get(getActivity()).getPhotoFile(mBike);
    }

    @Override
    public void onPause() {
        super.onPause();

        RidesDB.get(getActivity()).updateBikeName(mBike);

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bike_name, container, false);

        mEachBikeRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerEachBike);
        mEachBikeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mBikeName = (EditText) v.findViewById(R.id.bike_name);
        mBikeName.setText(mBike.getBikeName());
        mBikeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!containsBikeName(s.toString())) {
                    mBike.setBikeName(s.toString());
                    correctName = true;
                }else{
                    Toast.makeText(getActivity(),"Duplicated bike name, please choose other name", Toast.LENGTH_LONG).show();
                    correctName = false;
                }
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

        mPhotoButton = (ImageButton) v.findViewById(R.id.bike_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(captureImage,
                PackageManager.MATCH_DEFAULT_ONLY) != null && mPhotoFile != null) {
            mPhotoButton.setEnabled(true);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.bikeshare.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.bike_photo);
        updatePhotoView();

        mLockID = (EditText) v.findViewById(R.id.lockID);
        mLockID.setText(mBike.getBikeId().toString());

        mPrice = (EditText) v.findViewById(R.id.priceBike);
        double price = mBike.getPrice();
        Log.d("LocationMinha", "price: " + price);
        String priceString = "" + price;
        Log.d("LocationMinha", priceString);
        mPrice.setText(priceString);
        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double newPrice;
                if(s.toString() != null && s.toString().length() > 0){
                    try{
                        newPrice = Double.parseDouble(s.toString());
                        Log.d("LocationMinha", ""+newPrice);
                        mBike.setPrice(newPrice);
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.bikeshare.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    public boolean containsBikeName(String bikeName){
        for (Bike bike: RidesDB.get(getActivity()).getBikes()) {
            if (bike.getBikeName().equals(bikeName)) return true;
        }
        return false;
    }

    public void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PicturesUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    //Tentativa recycler view

    private void updateUI(){
        mRidesDB = RidesDB.get(getActivity());

        List<Ride> mRidesOfThisBike = new ArrayList<Ride>();
        for (Ride ride : mRidesDB.getRides()){
            if(ride.getBikeName().equals(mBike.getBikeName())) {
                mRidesOfThisBike.add(ride);
            }
        }

        if(mAdapter == null) {
            mAdapter = new BikeNameFragment.BikeAdapter2(mRidesOfThisBike);
            mEachBikeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setRides(mRidesOfThisBike);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private class BikeHolder extends RecyclerView.ViewHolder {

        private TextView mWhatBike;
        private TextView mStartRide;
        private TextView mEndRide;
        private TextView mEndDate;
        private TextView mStartDate;

        Ride mRide;

        public BikeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_ride, parent, false));

            mWhatBike = (TextView) itemView.findViewById(R.id.what_bike_list);
            mStartRide = (TextView) itemView.findViewById(R.id.start_ride_list);
            mEndRide = (TextView) itemView.findViewById(R.id.end_ride_list);
            mEndDate = (TextView) itemView.findViewById(R.id.date_end);
            mStartDate = (TextView) itemView.findViewById(R.id.date_start);

        }

        public void bind(Ride ride){
            mRide = ride;
            mWhatBike.setText(ride.getBikeName());
            mStartRide.setText(ride.getStartRide());
            mEndRide.setText(ride.getEndRide());
            SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            myFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            String startDateFormat = myFormat.format(ride.getStartDate());
            mStartDate.setText(startDateFormat);
            if(ride.getEndRide().equals("Not finished")){
                mEndDate.setText("");
            }else {
                String endDateFormat = myFormat.format(ride.getEndDate());
                mEndDate.setText(endDateFormat);
            }
        }
    }

    private class BikeAdapter2 extends RecyclerView.Adapter<BikeNameFragment.BikeHolder>{

        private List<Ride> mRides;

        public BikeAdapter2(List<Ride> rides){
            mRides = rides;
        }

        @Override
        public BikeNameFragment.BikeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BikeNameFragment.BikeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BikeNameFragment.BikeHolder holder, int position) {
            Ride ride = mRides.get(position);
            holder.bind(ride);

        }

        @Override
        public int getItemCount() {
            return mRides.size();
        }

        public void setRides(List<Ride> rides){
            mRides = rides;
        }

    }
}
