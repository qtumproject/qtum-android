package com.pixelplex.qtum.model.gson.store;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kirillvolkov on 10.08.17.
 */

public class IsPaidResponse {

    @SerializedName("contract_id")
    public String contractId;

    @SerializedName("request_id")
    public String requestId;

    @SerializedName("amount")
    public Float amount;

    @SerializedName("payed_at")
    public String payedAt;

    @SerializedName("from_addresses")
    public String[] fromAddresses;

}
