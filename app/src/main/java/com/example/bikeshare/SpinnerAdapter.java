package com.example.bikeshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bikeshare.manageBikes.Bike;
import com.example.bikeshare.manageBikes.PicturesUtils;

import java.io.File;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Bike> {

    private Bike mBike;
    private File mPhotoFile;

    public SpinnerAdapter (Context context, List<Bike> bikeList){

        super (context, 0 , bikeList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_bike_photo, parent, false);
        }

        ImageView imageViewPhoto = convertView.findViewById(R.id.spinner_image_bike);
        TextView textBikeName = convertView.findViewById(R.id.spinner_bike_name);

        mBike = getItem(position);
        mPhotoFile = RidesDB.get(getContext()).getPhotoFile(mBike);
        Uri uri = FileProvider.getUriForFile(getContext(), "com.example.bikeshare.fileprovider", mPhotoFile);
        getContext().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        if(mPhotoFile == null || !mPhotoFile.exists()){
            imageViewPhoto.setImageDrawable(null);
        } else {
            Bitmap bitmap = PicturesUtils.getScaledBitmap(mPhotoFile.getPath(), (Activity) getContext());
            imageViewPhoto.setImageBitmap(bitmap);
        }
        imageViewPhoto.setVisibility(imageViewPhoto != null ? View.VISIBLE : View.GONE);
        textBikeName.setText(mBike.getBikeName());

        return convertView;
    }
}
