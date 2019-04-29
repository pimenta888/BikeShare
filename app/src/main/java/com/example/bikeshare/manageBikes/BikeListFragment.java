package com.example.bikeshare.manageBikes;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bikeshare.R;
import com.example.bikeshare.Ride;
import com.example.bikeshare.RidesDB;
import com.example.bikeshare.Users.User;

import java.io.File;
import java.util.List;

public class BikeListFragment extends Fragment {

    private RecyclerView mBikeRecyclerView;
    private BikeNameAdapter mAdapter;
    private RidesDB mRidesDB;

    private TextView mEmptyTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_bikes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_bike:
                List<Bike> mBikesList = mRidesDB.getBikes();
                int count = mBikesList.size();

                Bike bike = new Bike("Bike #" + count);
                RidesDB.get(getActivity()).addBikeName(bike);
                Intent intent = BikeNameActivity.newIntent(getActivity(), bike.getBikeId());
                startActivity(intent);
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_bikes, container, false);

        mBikeRecyclerView = (RecyclerView) view.findViewById(R.id.bikeName_recycler_view);
        mBikeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBikeRecyclerView.addItemDecoration(new DividerItemDecoration(mBikeRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        mEmptyTextView = (TextView) view.findViewById(R.id.empty_text);


        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        updateUI();
    }

    private void updateUI(){
        mRidesDB = RidesDB.get(getActivity());
        List<Bike> mBikesList = mRidesDB.getBikes();

        if (mBikesList.size() == 0) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mBikeRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.GONE);
            mBikeRecyclerView.setVisibility(View.VISIBLE);
        }

        if(mAdapter == null) {
            mAdapter = new BikeNameAdapter(mBikesList);
            mBikeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setBikes(mBikesList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class BikeNameHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mBikeNameTitle;
        private ImageView mAvailableImage;
        private ImageView mUnavailableImage;
        private ImageView mBikePicture;
        private TextView mUserUsing;
        private Bike mBike;
        private File mPhotoFile;

        public BikeNameHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_bike, parent, false));
            itemView.setOnClickListener(this);

            mBikeNameTitle = (TextView) itemView.findViewById(R.id.bike_title);
            mAvailableImage = (ImageView) itemView.findViewById(R.id.imageView);
            mUnavailableImage = (ImageView) itemView.findViewById(R.id.imageView2);
            mBikePicture = (ImageView) itemView.findViewById(R.id.imageView3);
            mUserUsing = (TextView) itemView.findViewById(R.id.userUsing);
        }

        public void bind(Bike bike){
            mBike = bike;
            mBike.setAvailable(mRidesDB.bikeAvailability(bike));
            mBikeNameTitle.setText(mBike.getBikeName());
            mAvailableImage.setVisibility(bike.isAvailable() ? View.VISIBLE : View.GONE);
            mUnavailableImage.setVisibility(!bike.isAvailable() ? View.VISIBLE : View.GONE);
            if(!bike.isAvailable()){
                Log.d("ola", "var: "+mBike.getBikeName());
                Log.d("ola", "var: "+mRidesDB.getRideUser(mBike.getBikeName()));
                String userOfBike = mRidesDB.getRideUser(mBike.getBikeName());
                mUserUsing.setText(userOfBike + " is using");
            }
            mPhotoFile = RidesDB.get(getActivity()).getPhotoFile(mBike);
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.bikeshare.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            if(mPhotoFile == null || !mPhotoFile.exists()){
                mBikePicture.setImageDrawable(null);
            } else {
                Bitmap bitmap = PicturesUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
                mBikePicture.setImageBitmap(bitmap);
            }
            mBikePicture.setVisibility(mBikePicture != null ? View.VISIBLE : View.GONE);

        }


        @Override
        public void onClick(View v) {
            Intent intent = BikeNameActivity.newIntent(getActivity(), mBike.getBikeId());
            startActivity(intent);
        }
    }

    private class BikeNameAdapter extends RecyclerView.Adapter<BikeNameHolder>{

        private List<Bike> mBikes;

        public BikeNameAdapter(List<Bike> bikes){
            mBikes = bikes;
        }

        @Override
        public BikeNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new BikeNameHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BikeNameHolder holder, int position) {
            Bike bike = mBikes.get(position);
            holder.bind(bike);
        }

        @Override
        public int getItemCount() {
            return mBikes.size();
        }
        public void setBikes(List<Bike> bikes){
            mBikes = bikes;
        }

    }
}
