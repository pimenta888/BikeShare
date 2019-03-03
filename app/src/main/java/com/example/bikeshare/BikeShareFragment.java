package com.example.bikeshare;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BikeShareFragment extends Fragment {

    private static final int REQUEST_ADD_RIDE = 1;
    private static final int REQUEST_END_RIDE = 2;

    private final String KEY_LIST_BUTTON ="list showed";
    private final int LIST_BUTTON_CLICKED_ONCE = 0;
    private final int LIST_BUTTON_CLICKED_TWICE = 1;
    private int LIST_BUTTON_STATE;

    private RecyclerView mBikeRecyclerView;
    private BikeAdapter mAdapter;
    private LinearLayout mHeaderRecyclerView;
    private TextView mInfoDelete;

    private Button mAddRideButton;
    private Button mEndRideButton;
    private Button mListRideButton;

    private RidesDB mRidesDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_bike_share, container, false);

        if(savedInstanceState != null){
            LIST_BUTTON_STATE = savedInstanceState.getInt(KEY_LIST_BUTTON);
        }else{
            LIST_BUTTON_STATE = 0;
        }

        mInfoDelete = (TextView) view.findViewById(R.id.info_user_delete);
        mInfoDelete.setVisibility(View.GONE);
        mHeaderRecyclerView = (LinearLayout) view.findViewById(R.id.main_header_list);
        mHeaderRecyclerView.setVisibility(View.GONE);
        mBikeRecyclerView = (RecyclerView) view.findViewById(R.id.bike_recycler_view);
        mBikeRecyclerView.setVisibility(View.GONE);
        mBikeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBikeRecyclerView.addItemDecoration(new DividerItemDecoration(mBikeRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        if (LIST_BUTTON_STATE == 1){
            mInfoDelete.setVisibility(View.VISIBLE);
            mHeaderRecyclerView.setVisibility(View.VISIBLE);
            mBikeRecyclerView.setVisibility(View.VISIBLE);
        }

        /*Display API Level */
        TextView ApiLevel = (TextView) view.findViewById(R.id.api_level);
        ApiLevel.setText("API Level " + Build.VERSION.SDK_INT);

        mAddRideButton = (Button) view.findViewById(R.id.main_add_ride_button);
        mAddRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StartRideActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_ADD_RIDE);
            }
        });

        mEndRideButton = (Button) view.findViewById(R.id.main_end_ride_button);
        mEndRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EndRideActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_END_RIDE);
            }
        });

        mListRideButton = (Button) view.findViewById(R.id.main_list_button);
        mListRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LIST_BUTTON_STATE==LIST_BUTTON_CLICKED_ONCE){

                    mInfoDelete.setVisibility(View.VISIBLE);
                    mHeaderRecyclerView.setVisibility(View.VISIBLE);
                    mBikeRecyclerView.setVisibility(View.VISIBLE);

                    LIST_BUTTON_STATE = LIST_BUTTON_CLICKED_TWICE;
                } else if(LIST_BUTTON_STATE==LIST_BUTTON_CLICKED_TWICE){

                    mInfoDelete.setVisibility(View.GONE);
                    mHeaderRecyclerView.setVisibility(View.GONE);
                    mBikeRecyclerView.setVisibility(View.GONE);


                    LIST_BUTTON_STATE = LIST_BUTTON_CLICKED_ONCE;
                }
            }
        });

        updateUI();
        return view;
    }

    private void updateUI(){
        mRidesDB = RidesDB.get(getActivity());
        List<Ride> mRidesList = mRidesDB.getRidesDB();

        if(mAdapter == null) {
            mAdapter = new BikeAdapter(mRidesList);
            mBikeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_ADD_RIDE){
            if(data != null) {
                String whatAdd = StartRideActivity.getAddWhatBike(data);
                String whereAdd = StartRideActivity.getAddWhere(data);
                //Ride newRide = new Ride(whatAdd,whereAdd,"Not finished");
               // mRidesDB.addRide(newRide);
            }
        }
        if (requestCode == REQUEST_END_RIDE){
            if(data != null) {
                String whatEnd = EndRideActivity.getEndWhatBike(data);
                String whereEnd = EndRideActivity.getEndWhere(data);
                if (containsBikeName(whatEnd)) mRidesDB.endRide(whatEnd, whereEnd);
            }
        }
    }

    public boolean containsBikeName(String bikeName){
        for (Ride ride: mRidesDB.getRidesDB()) {
            if (ride.getBikeName().equals(bikeName)) return true;
        }
        return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private class BikeHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView mWhatBike;
        private TextView mStartRide;
        private TextView mEndRide;

        Ride mRide;
        private int ITEM_STATE = 0;
        private final int ITEM_CLICKED_ONCE = 0;
        private final int ITEM_CLICKED_TWICE = 1;

        public BikeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_ride, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

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

        @Override
        public boolean onLongClick(View v){
            confirmDelete(mWhatBike.getText().toString()).show();
            return false;
        }

        private AlertDialog.Builder confirmDelete(final String bikeName){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Do you wanna delete ride?")
                    .setPositiveButton(android.R.string.yes, null)
                    .setNegativeButton(android.R.string.no, null);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mRidesDB.removeRide(mRide);
                    updateUI();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return builder;
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(KEY_LIST_BUTTON, LIST_BUTTON_STATE);
    }
}
