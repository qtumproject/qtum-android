package com.pixelplex.qtum.model.gson.qstore;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kirillvolkov on 10.08.17.
 */

public class QstoreBuyResponse {
    @SerializedName("address")
    public String address;

    @SerializedName("amount")
    public Float amount;

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("request_id")
    public String requestId;
}
