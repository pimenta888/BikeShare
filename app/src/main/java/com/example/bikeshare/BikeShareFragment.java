package com.example.bikeshare;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BikeShareFragment extends Fragment {

    private RecyclerView mBikeRecyclerView;
    private BikeAdapter mAdapter;

    private Button mAddRideButton;
    private Button mEndRideButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_bike_share, container, false);

        mBikeRecyclerView = (RecyclerView) view.findViewById(R.id.bike_recycler_view);
        mBikeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBikeRecyclerView.addItemDecoration(new DividerItemDecoration(mBikeRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        /*Display API Level */
        TextView Level = (TextView) view.findViewById(R.id.api_level);
        Level.setText("API Level " + Build.VERSION.SDK_INT);

        mAddRideButton = (Button) view.findViewById(R.id.main_add_ride_button);
        mAddRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StartRideActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        mEndRideButton = (Button) view.findViewById(R.id.main_end_ride_button);
        mEndRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EndRideActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        updateUI();

        return view;
    }

    private void updateUI(){
        RidesDB ridesDB = RidesDB.get(getActivity());
        List<Ride> rides = ridesDB.getRidesDB();

        mAdapter = new BikeAdapter(rides);
        mBikeRecyclerView.setAdapter(mAdapter);
    }

    private class BikeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mWhatBike;
        private TextView mStartRide;
        private TextView mEndRide;
        private Ride mRide;

        private int ITEM_STATE = 0;
        private final int ITEM_CLICKED_ONCE = 0;
        private final int ITEM_CLICKED_TWICE = 1;

        public BikeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_ride, parent, false));
            itemView.setOnClickListener(this);

            mWhatBike = (TextView) itemView.findViewById(R.id.what_bike_list);
            mStartRide = (TextView) itemView.findViewById(R.id.start_ride_list);
            mEndRide = (TextView) itemView.findViewById(R.id.end_ride_list);

        }

        public void bind(Ride ride){

            mRide = ride;
            mWhatBike.setText(ride.getBikeName());
            mStartRide.setText(ride.getStartRide());
            mEndRide.setText(ride.getEndRide());
        }

        @Override
        public void onClick(View v) {

            if(ITEM_STATE==ITEM_CLICKED_ONCE){

                mWhatBike.setTextColor(Color.RED);
                mStartRide.setTextColor(Color.RED);
                mEndRide.setTextColor(Color.RED);

                mWhatBike.setTextSize(18);
                mStartRide.setTextSize(18);
                mEndRide.setTextSize(18);

                ITEM_STATE = ITEM_CLICKED_TWICE;

            } else if(ITEM_STATE==ITEM_CLICKED_TWICE){

                mWhatBike.setTextColor(Color.GRAY);
                mStartRide.setTextColor(Color.GRAY);
                mEndRide.setTextColor(Color.GRAY);

                mWhatBike.setTextSize(14);
                mStartRide.setTextSize(14);
                mEndRide.setTextSize(14);

                ITEM_STATE = ITEM_CLICKED_ONCE;

            }
        }
    }

    private class BikeAdapter extends RecyclerView.Adapter<BikeHolder>{

        private List<Ride> mRides;

        public BikeAdapter(List<Ride> rides){
            mRides = rides;
        }

        @Override
        public BikeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BikeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BikeHolder holder, int position) {
            Ride ride = mRides.get(position);
            holder.bind(ride);

        }

        @Override
        public int getItemCount() {
            return mRides.size();
        }

    }
}
