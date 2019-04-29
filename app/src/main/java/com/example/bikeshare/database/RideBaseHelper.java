package com.example.bikeshare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bikeshare.database.RideDbSchema.BikeTable;
import com.example.bikeshare.database.RideDbSchema.RidesTable;
import com.example.bikeshare.database.RideDbSchema.UsersTable;

public class RideBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "bikeShareBase.db";

    public RideBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //I am not going to put UNIQUE on BikeName to avoid sql errors
        db.execSQL("create table " + BikeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                BikeTable.Cols.UUID + ", " +
                BikeTable.Cols.BIKENAME + ", " +
                BikeTable.Cols.AVAILABLE + ", " +
                BikeTable.Cols.PRICE +
                ")"
        );

        db.execSQL("create table " + RidesTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                RidesTable.Cols.UUID + ", " +
                RidesTable.Cols.BIKENAME + ", " +
                RidesTable.Cols.STARTLOCATION + ", " +
                RidesTable.Cols.ENDLOCATION + ", " +
                RidesTable.Cols.STARTDATE + ", " +
                RidesTable.Cols.ENDDATE + ", " +
                RidesTable.Cols.USEREMAIL + ", " +
                RidesTable.Cols.TOTALPRICE +
                ")"
        );

        db.execSQL("create table " + UsersTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                UsersTable.Cols.EMAIL + ", " +
                UsersTable.Cols.PASSWORD + ", " +
                UsersTable.Cols.MONEY + ", " +
                UsersTable.Cols.STATUS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
