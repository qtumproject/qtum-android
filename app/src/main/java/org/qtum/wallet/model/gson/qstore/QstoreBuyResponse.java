package org.qtum.wallet.model.gson.qstore;

import com.google.gson.annotations.SerializedName;

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
