package com.example.bikeshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bikeshare.database.RideBaseHelper;
import com.example.bikeshare.database.RideCursorWrapper;
import com.example.bikeshare.database.RideDbSchema.RidesTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RidesDB {

    private static RidesDB sRidesDB;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private RidesDB(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RideBaseHelper(mContext).getWritableDatabase();

    }

    public static RidesDB get(Context context){
        if (sRidesDB == null){
            sRidesDB = new RidesDB(context);
        }
        return sRidesDB;
    }

    public List<Ride> getRides(){
        List<Ride> rides = new ArrayList<>();

        RideCursorWrapper cursor = queryRides(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                rides.add(cursor.getRide());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return rides;
    }

    public Ride getRide(UUID uuid){
        RideCursorWrapper cursor = queryRides(
                RidesTable.Cols.UUID + " = ?",
                new String[] {uuid.toString()}
        );

        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getRide();
        }finally {
            cursor.close();
        }
    }

    public void addRide(Ride ride){
        ContentValues rideValues = getContentValues(ride);
        mDatabase.insert(RidesTable.NAME, null, rideValues);
    }

    public void endRide(String what, String where){

        for (Ride ride: getRides()) {
            if(ride.getBikeName().equals(what) && ride.getEndRide().equals("Not finished")) {
                String id = ride.getId().toString();
                String bikeName = ride.getBikeName();
                String startLocation = ride.getStartRide();
                Date startDate = ride.getStartDate();

                Ride rideUpdate = new Ride(ride.getId(), bikeName, startLocation, where);
                rideUpdate.setStartDate(startDate);
                rideUpdate.setEndDate(new Date());
                ContentValues values = getContentValues(rideUpdate);
                mDatabase.update(RidesTable.NAME, values,
                        RidesTable.Cols.UUID + " = ?",
                        new String[]{id});
            }
        }
    }

    public void removeRide(Ride rideRemove){
        String uuidString = rideRemove.getId().toString();

        mDatabase.delete(RidesTable.NAME, RidesTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private RideCursorWrapper queryRides(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                RidesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new RideCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Ride ride){
        ContentValues values = new ContentValues();
        values.put(RidesTable.Cols.UUID, ride.getId().toString());
        values.put(RidesTable.Cols.BIKENAME, ride.getBikeName());
        values.put(RidesTable.Cols.STARTLOCATION, ride.getStartRide());
        values.put(RidesTable.Cols.ENDLOCATION, ride.getEndRide());
        values.put(RidesTable.Cols.STARTDATE, ride.getStartDate().getTime());
        values.put(RidesTable.Cols.ENDDATE, ride.getEndDate().getTime());

        return values;
    }
}