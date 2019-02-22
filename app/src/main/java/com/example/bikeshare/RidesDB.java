package com.example.bikeshare;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class RidesDB {

    private static RidesDB sRidesDB;
    private List<Ride> mAllRides;
    private Ride mLastRide;

    private RidesDB(Context context) {
        mLastRide = new Ride("", "", "");

        //just for test
        mAllRides = new ArrayList<>();
        mAllRides.add(new Ride(" Chuck Norris bike ", "ITU", " Fields "));
        mAllRides.add(new Ride(" Chuck Norris bike ", " Fields ", "Kongens Nytorv "));
        mAllRides.add(new Ride(" Bruce Lee bike ", " KobenhavnsLufthavn ", " Kobenhavns Hovedbanegard "));
        mAllRides.add(new Ride(" Chuck Norris bike ", "ITU", " Fields "));
        mAllRides.add(new Ride(" Chuck Norris bike ", " Fields ", "Kongens Nytorv "));
        mAllRides.add(new Ride(" Bruce Lee bike ", " KobenhavnsLufthavn ", " Kobenhavns Hovedbanegard "));
        mAllRides.add(new Ride(" Chuck Norris bike ", "ITU", " Fields "));
        mAllRides.add(new Ride(" Chuck Norris bike ", " Fields ", "Kongens Nytorv "));
        mAllRides.add(new Ride(" Bruce Lee bike ", " KobenhavnsLufthavn ", " Kobenhavns Hovedbanegard "));
        mAllRides.add(new Ride(" Chuck Norris bike ", "ITU", " Fields "));
        mAllRides.add(new Ride(" Chuck Norris bike ", " Fields ", "Kongens Nytorv "));
        mAllRides.add(new Ride(" Bruce Lee bike ", " KobenhavnsLufthavn ", " Kobenhavns Hovedbanegard "));
        mAllRides.add(new Ride(" Chuck Norris bike ", "ITU", " Fields "));
        mAllRides.add(new Ride(" Chuck Norris bike ", " Fields ", "Kongens Nytorv "));
        mAllRides.add(new Ride(" Bruce Lee bike ", " KobenhavnsLufthavn ", " Kobenhavns Hovedbanegard "));

    }

    public static RidesDB get(Context context){
        if (sRidesDB == null){
            sRidesDB = new RidesDB(context);
        }
        return sRidesDB;
    }

    public List<Ride> getRidesDB(){
        return mAllRides;
    }

    public void addRide(String what, String where){
        mAllRides.add(new Ride(what,where,""));
    }

    public void endRide(String what, String where){
        for (Ride ride: mAllRides) {
            if(ride.getBikeName() == what) ride.setEndRide(where);
        }
    }
}