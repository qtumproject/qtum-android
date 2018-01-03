package org.qtum.wallet.model.gson.token_history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenHistory {

    @SerializedName("contract_address")
    @Expose
    private String contractAddress;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("tx_hash")
    @Expose
    private String txHash;
    @SerializedName("tx_time")
    @Expose
    private Integer txTime;

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Integer getTxTime() {
        return txTime;
    }

    public void setTxTime(Integer txTime) {
        this.txTime = txTime;
    }

    //for unit_test
    public TokenHistory(String contractAddress, String from, String to, String amount, String txHash, Integer txTime) {
        this.contractAddress = contractAddress;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.txHash = txHash;
        this.txTime = txTime;
    }
}
