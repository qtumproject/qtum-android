package com.pixelplex.qtum.model.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SendRawTransactionResponse {
    @SerializedName("result")
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
