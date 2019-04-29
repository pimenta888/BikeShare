package com.example.bikeshare;

import java.util.Date;
import java.util.UUID;

public class Ride {
    private UUID id;
    private String mBikeName;
    private String mStartRide ;
    private String mEndRide;
    private Date mStartDate;
    private Date mEndDate;
    private double totalPrice;

    public Ride (String bikeName , String startRide, String endRide) {
       this(UUID.randomUUID(), bikeName, startRide, endRide);
    }

    public Ride (UUID uuid, String bikeName , String startRide, String endRide) {
        id = uuid;
        mBikeName = bikeName.trim();
        mStartRide = startRide.trim();
        mEndRide = endRide.trim();
        mStartDate = new Date();
        mEndDate = mStartDate;
        totalPrice = 0.0;
    }

    public UUID getId() {
        return id;
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

    public String getEndRide() {
        return mEndRide;
    }

    public void setEndRide(String endRide) {
        mEndRide = endRide;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String priceString(){
        return totalPrice + "";
    }
}
