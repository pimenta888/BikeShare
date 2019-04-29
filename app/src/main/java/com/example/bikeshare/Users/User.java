package com.example.bikeshare.Users;

public class User {
    private String mEmail;
    private String mPassword;
    private double mMoney;
    private boolean mStatus;

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
        mMoney = 0.0;
        mStatus = false;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public double getMoney() {
        return mMoney;
    }

    public void setMoney(double money) {
        mMoney = money;
    }

    public String moneyString() {
        return String.format("%.2f", mMoney);
    }

    public boolean isStatus() {
        return mStatus;
    }

    public void setStatus(boolean status) {
        mStatus = status;
    }
}
