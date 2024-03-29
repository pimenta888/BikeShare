package com.example.bikeshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bikeshare.Users.User;
import com.example.bikeshare.database.RideBaseHelper;
import com.example.bikeshare.database.RideCursorWrapper;
import com.example.bikeshare.database.RideDbSchema;
import com.example.bikeshare.database.RideDbSchema.BikeTable;
import com.example.bikeshare.database.RideDbSchema.RidesTable;
import com.example.bikeshare.database.RideDbSchema.UsersTable;
import com.example.bikeshare.manageBikes.Bike;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    //Rides

    public List<Ride> getRides(){
        List<Ride> rides = new ArrayList<>();

        RideCursorWrapper cursor = queryTables(RidesTable.NAME, null,null);

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

    public String getRideUser(String nameBike){
        RideCursorWrapper cursor = queryTables(RidesTable.NAME,
                RidesTable.Cols.BIKENAME + " = ? AND " + RidesTable.Cols.ENDLOCATION + " = ?",
                new String[] {nameBike, "Not finished"}
        );

        try{
            if (cursor.getCount() == 0){
                return "";
            }

            cursor.moveToFirst();
            return cursor.getRide().getUser();
        }finally {
            cursor.close();
        }
    }

    public void addRide(Ride ride){
        ContentValues rideValues = getRideContentValues(ride);
        mDatabase.insert(RidesTable.NAME, null, rideValues);
    }

    public double endRide(String what, String where){

        for (Ride ride: getRides()) {
            if(ride.getBikeName().equals(what) && ride.getEndRide().equals("Not finished")) {
                String id = ride.getId().toString();
                String bikeName = ride.getBikeName();
                String startLocation = ride.getStartRide();
                Date startDate = ride.getStartDate();
                String user = ride.getUser();

                Ride rideUpdate = new Ride(ride.getId(), bikeName, startLocation, where, user);
                rideUpdate.setStartDate(startDate);
                rideUpdate.setEndDate(new Date());

                long diffInMs = (new Date()).getTime() - startDate.getTime();
                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
                double bikePrice = 5.0;
                for (Bike bike: getBikes()) {
                    if (bikeName.equals(bike.getBikeName())){
                        bikePrice = bike.getPrice();
                    }
                }
                double ridePrice = bikePrice * diffInSec / 60;

                rideUpdate.setTotalPrice(ridePrice);
                ContentValues values = getRideContentValues(rideUpdate);

                mDatabase.update(RidesTable.NAME, values,
                        RidesTable.Cols.UUID + " = ?",
                        new String[]{id});

                return ridePrice;
            }
        }
        return 0.0;
    }

    public void removeRide(Ride rideRemove){
        String uuidString = rideRemove.getId().toString();

        mDatabase.delete(RidesTable.NAME, RidesTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    //Bikes

    public File getPhotoFile(Bike bike){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, bike.getPhotoFilename());
    }

    public List<Bike> getBikes(){
        List<Bike> bikes = new ArrayList<>();

        RideCursorWrapper cursor = queryTables( BikeTable.NAME,null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                bikes.add(cursor.getBike());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return bikes;
    }

    public Bike getBike(UUID uuid){
        RideCursorWrapper cursor = queryTables(BikeTable.NAME,
                BikeTable.Cols.UUID + " = ?",
                new String[] {uuid.toString()}
        );

        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getBike();
        }finally {
            cursor.close();
        }
    }

    public void addBikeName(Bike bike){
        ContentValues values = getBikeContentValues(bike);
        mDatabase.insert(BikeTable.NAME, null, values);
    }

    public void updateBikeName(Bike bike){
        String uuidString = bike.getBikeId().toString();
        ContentValues values = getBikeContentValues(bike);

        mDatabase.update(BikeTable.NAME, values,
                BikeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void removeBike(Bike bikeRemove){
        String uuidString = bikeRemove.getBikeId().toString();

        mDatabase.delete(BikeTable.NAME,
                BikeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public boolean bikeAvailability(Bike bike){
        RideCursorWrapper cursor = queryTables(RidesTable.NAME,
                RidesTable.Cols.BIKENAME + " = ? AND " + RidesTable.Cols.ENDLOCATION + " = ?",
                new String[]{bike.getBikeName(), "Not finished"}
                );
        try{
            if (cursor.getCount() == 0){
                return true;
            }
            return false;
        }finally {
            cursor.close();
        }
    }

    //Users

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();

        RideCursorWrapper cursor = queryTables( UsersTable.NAME,null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                users.add(cursor.getUser());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return users;
    }

    public User getUserOnline(){
        RideCursorWrapper cursor = queryTables(UsersTable.NAME,
                UsersTable.Cols.STATUS + " = ?",
                new String[] {"1"}
        );

        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getUser();
        }finally {
            cursor.close();
        }
    }

    public void addUsers(User user){
        ContentValues values = getUserContentValues(user);
        mDatabase.insert(UsersTable.NAME, null, values);
    }

    public void updateUser(User user){
        String email = user.getEmail();
        ContentValues values = getUserContentValues(user);

        mDatabase.update(UsersTable.NAME, values,
                UsersTable.Cols.EMAIL + " = ?",
                new String[] {email});
    }


    //ConvertValues

    private RideCursorWrapper queryTables(String table, String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                table,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new RideCursorWrapper(cursor);
    }

    private static ContentValues getRideContentValues(Ride ride){
        ContentValues values = new ContentValues();
        values.put(RidesTable.Cols.UUID, ride.getId().toString());
        values.put(RidesTable.Cols.BIKENAME, ride.getBikeName());
        values.put(RidesTable.Cols.STARTLOCATION, ride.getStartRide());
        values.put(RidesTable.Cols.ENDLOCATION, ride.getEndRide());
        values.put(RidesTable.Cols.STARTDATE, ride.getStartDate().getTime());
        values.put(RidesTable.Cols.ENDDATE, ride.getEndDate().getTime());
        values.put(RidesTable.Cols.USEREMAIL, ride.getUser());;
        values.put(RidesTable.Cols.TOTALPRICE, ride.priceString());

        return values;
    }

    private static ContentValues getBikeContentValues(Bike bike) {

        ContentValues values = new ContentValues();
        values.put(BikeTable.Cols.UUID, bike.getBikeId().toString());
        values.put(BikeTable.Cols.BIKENAME, bike.getBikeName());
        values.put(BikeTable.Cols.AVAILABLE, bike.isAvailable() ? 1 : 0);
        values.put(BikeTable.Cols.PRICE, bike.priceString());

        return values;
    }

    private static ContentValues getUserContentValues(User user) {

        ContentValues values = new ContentValues();
        values.put(UsersTable.Cols.EMAIL, user.getEmail());
        values.put(UsersTable.Cols.PASSWORD, user.getPassword());
        values.put(UsersTable.Cols.STATUS, user.isStatus() ? 1 : 0);
        values.put(UsersTable.Cols.MONEY, user.moneyString());

        return values;
    }
}