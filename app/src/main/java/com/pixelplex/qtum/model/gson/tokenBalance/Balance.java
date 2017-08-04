package com.pixelplex.qtum.model.gson.tokenBalance;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Balance {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("balance")
    @Expose
    private Float balance;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getBalance() {
        return (balance != null)? balance : 0;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

}
