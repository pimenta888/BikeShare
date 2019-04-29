package com.example.bikeshare.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.bikeshare.Users.User;
import com.example.bikeshare.database.RideDbSchema.UsersTable;
import com.example.bikeshare.manageBikes.Bike;
import com.example.bikeshare.Ride;
import com.example.bikeshare.database.RideDbSchema.BikeTable;
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
        String userEmail = getString(getColumnIndex(RidesTable.Cols.USEREMAIL));
        double totalprice = getDouble(getColumnIndex(RidesTable.Cols.TOTALPRICE));

        Ride ride = new Ride(UUID.fromString(uuidString), bikeName, startLocation, endLocation, userEmail);
        ride.setStartDate(new Date(startDate));
        ride.setEndDate(new Date(endDate));
        ride.setTotalPrice(totalprice);

        return ride;
    }

    public Bike getBike(){
        String uuidString = getString(getColumnIndex(BikeTable.Cols.UUID));
        String bikeName = getString(getColumnIndex(BikeTable.Cols.BIKENAME));
        int isAvailable = getInt(getColumnIndex(BikeTable.Cols.AVAILABLE));
        double price = getDouble(getColumnIndex(BikeTable.Cols.PRICE));

        Bike bike = new Bike(UUID.fromString(uuidString), bikeName);
        bike.setAvailable(isAvailable != 0); //1 means true
        bike.setPrice(price);

        return bike;
    }

    public User getUser(){
        String email = getString(getColumnIndex(UsersTable.Cols.EMAIL));
        String password = getString(getColumnIndex(UsersTable.Cols.PASSWORD));
        double money = getDouble(getColumnIndex(UsersTable.Cols.MONEY));
        int status = getInt(getColumnIndex(UsersTable.Cols.STATUS));

        User user = new User(email, password);
        user.setMoney(money);
        user.setStatus(status != 0); //1 means true

        return user;
    }
}

