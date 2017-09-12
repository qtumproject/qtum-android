package org.qtum.wallet.model.gson.qstore;

import com.google.gson.annotations.SerializedName;


public class ContractPurchase {

    @SerializedName("contract_id")
    private String contractId;

    @SerializedName("request_id")
    private String requestId;

    @SerializedName("amount")
    private Float amount;

    @SerializedName("payed_at")
    private String payedAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("from_addresses")
    private String[] fromAddresses;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getPayedAt() {
        return payedAt;
    }

    public void setPayedAt(String payedAt) {
        this.payedAt = payedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String[] getFromAddresses() {
        return fromAddresses;
    }

    public void setFromAddresses(String[] fromAddresses) {
        this.fromAddresses = fromAddresses;
    }
}
