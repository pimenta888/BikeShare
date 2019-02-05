package com.example.bikeshare;

public class Ride {
    private String mBikeName ;
    private String mStartRide ;

    public Ride ( String bikeName , String startRide ) {
        mBikeName = bikeName ;
        mStartRide = startRide ;
    }

    public String getBikeName () {
        return mBikeName ;
    }

    public void setBikeName ( String bikeName ) {
        mBikeName = bikeName ;
    }

    public String getStartRide () {
        return mStartRide ;
    }

    public void setStartRide ( String startRide ) {
        mStartRide = startRide ;
    }

    public String toString () {
        if(mStartRide.equals("") && mBikeName.equals("")){
            return "no last trip";
        }
        return mBikeName + " started here: " + mStartRide ;
    }
}
