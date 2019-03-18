package com.example.bikeshare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bikeshare.database.RideDbSchema.BikeTable;
import com.example.bikeshare.database.RideDbSchema.RidesTable;

public class RideBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

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
                BikeTable.Cols.AVAILABLE +
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
                "foreign key (" + RidesTable.Cols.BIKENAME + ") references " + BikeTable.NAME + "(" + BikeTable.Cols.BIKENAME + ")" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}