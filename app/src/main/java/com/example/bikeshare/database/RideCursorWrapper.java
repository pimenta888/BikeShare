package com.example.bikeshare.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.bikeshare.Ride;
import com.example.bikeshare.database.RideDbSchema.RidesTable;

import java.util.Date;
import java.util.UUID;

public class RideCursorWrapper extends CursorWrapper {
    public RideCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Ride getRide(){
        String uuidString = getString(getColumnIndex(RidesTable.Cols.UUID));
        String bikeName = getString(getColumnIndex(RidesTable.Cols.BIKENAME));
        String startLocation = getString(getColumnIndex(RidesTable.Cols.STARTLOCATION));
        String endLocation = getString(getColumnIndex(RidesTable.Cols.ENDLOCATION));
        long startDate = getLong(getColumnIndex(RidesTable.Cols.STARTDATE));
        long endDate = getLong(getColumnIndex(RidesTable.Cols.ENDDATE));

        Ride ride = new Ride(UUID.fromString(uuidString), bikeName, startLocation, endLocation);
        ride.setStartDate(new Date(startDate));
        ride.setEndDate(new Date(endDate));

        return ride;
    }
}

