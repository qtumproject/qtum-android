package com.pixelplex.qtum.model;


public class Currency {

    private String mName;
    private boolean mIsToken;
    private String mAddress;

    public Currency(String name, boolean isToken) {
        mName = name;
        mIsToken = isToken;
    }

    public Currency(String name, boolean isToken, String address) {
        mName = name;
        mIsToken = isToken;
        mAddress = address;
    }

    public String getName() {
        return mName;
    }

    public boolean isToken() {
        return mIsToken;
    }

    public String getAddress() {
        return mAddress;
    }
}
