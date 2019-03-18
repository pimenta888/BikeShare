package com.example.bikeshare.manageBikes;

import java.util.UUID;

public class Bike {
    private UUID mBikeId;
    private String mBikeName;
    private boolean mAvailable;

    public Bike(String bikeName){
        this(UUID.randomUUID(), bikeName);
    }

    public Bike(UUID id, String bikeName){
        mBikeId = id;
        mBikeName = bikeName;
        mAvailable = true;
    }

    public UUID getBikeId() {
        return mBikeId;
    }

    public void setBikeId(UUID bikeId) {
        mBikeId = bikeId;
    }

    public String getBikeName() {
        return mBikeName;
    }

    public void setBikeName(String bikeName) {
        mBikeName = bikeName;
    }

    public boolean isAvailable() {
        return mAvailable;
    }

    public void setAvailable(boolean available) {
        mAvailable = available;
    }
}
