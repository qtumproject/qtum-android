package org.qtum.wallet.model;


import com.google.gson.annotations.SerializedName;

public class Currency {

    @SerializedName("name")
    private String mName;

    public Currency(String name) {
        mName = name;

    }

    public String getName() {
        return mName;
    }
}
