package com.pixelplex.qtum.model.gson.callSmartContractResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {


    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("output")
    @Expose
    private String output;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}