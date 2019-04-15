package com.example.bikeshare.manageBikes;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bikeshare.R;
import com.example.bikeshare.RidesDB;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class BikeNameFragment extends Fragment {

    private static final String ARGS_BIKE_ID = "bike_id";
    private static final int REQUEST_PHOTO = 2;

    private Bike mBike;
    private EditText mBikeName;
    private CheckBox mAvailable;
    private boolean correctName;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;


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
}
