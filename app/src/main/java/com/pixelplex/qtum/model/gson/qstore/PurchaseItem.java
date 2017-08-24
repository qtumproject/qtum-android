package com.pixelplex.qtum.model.gson.qstore;

import com.google.gson.annotations.SerializedName;

/**
 * Created by max-v on 8/24/2017.
 */

public class PurchaseItem{

    public static final String NON_PAID_STATUS = "NON_PAID_STATUS";
    public static final String PAID_STATUS = "PAID_STATUS";
    public static final String PENDING_STATUS = "PENDING_STATUS";

    public String payStatus = PENDING_STATUS;

    public PurchaseItem(String contractId, QstoreBuyResponse buyResponse){
        this.contractId = contractId;
        this.accessToken = buyResponse.accessToken;
        this.address = buyResponse.address;
        this.amount = buyResponse.amount;
        this.requestId = buyResponse.requestId;
    }

    @SerializedName("contract_id")
    private String contractId;

    @SerializedName("address")
    private String address;

    @SerializedName("amount")
    private Float amount;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("request_id")
    private String requestId;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
