package com.pixelplex.qtum.model.gson.store;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kirillvolkov on 10.08.17.
 */

public class ContractPurchaseResponse extends IsPaidResponse {
    @SerializedName("created_at")
    public String createDate;
}
