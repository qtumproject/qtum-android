package com.pixelplex.qtum.model.contract;


import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.List;

public class Token extends Contract {

    @SerializedName("lastBalance")
    private float mLastBalance = 0;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("decimal_units")
    private Integer decimalUnits;

    public Token(String contractAddress, String templateUiid, Boolean hasBeenCreated, String date, String senderAddress, String contractName) {
        super(contractAddress, templateUiid, hasBeenCreated, date, senderAddress, contractName);
        this.mIsSubscribe = true;
    }

    public String getSymbol(){
        return symbol;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public void setDecimalUnits(Integer decimalUnits){
        this.decimalUnits = decimalUnits;
    }

    public Integer getDecimalUnits(){
        return this.decimalUnits;
    }

    public void setLastBalance(float balance){
        this.mLastBalance = balance;
    }

    public float getLastBalance(){
        return mLastBalance;
    }

    public boolean isSubscribe() {
        return mIsSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        mIsSubscribe = subscribe;
    }

    public float getTokenBalanceWithDecimalUnits(){
        return  (mLastBalance / (float)Math.pow(10, (decimalUnits != null)? decimalUnits.intValue() : 0 ));
    }

}
