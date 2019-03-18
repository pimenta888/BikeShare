package com.example.bikeshare.database;

public class RideDbSchema {

    public static final class RidesTable{
        public static final String NAME = "rides";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String BIKENAME = "bike";
            public static final String STARTLOCATION = "startRide";
            public static final String ENDLOCATION = "endRide";
            public static final String STARTDATE = "startDate";
            public static final String ENDDATE = "endDate";
        }
    }
}
