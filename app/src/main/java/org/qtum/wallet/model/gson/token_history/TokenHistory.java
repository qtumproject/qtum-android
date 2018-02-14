package org.qtum.wallet.model.gson.token_history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.qtum.wallet.model.gson.history.HistoryPayType;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TokenHistory extends RealmObject{

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
    @PrimaryKey
    @SerializedName("tx_hash")
    @Expose
    private String txHash;
    @SerializedName("tx_time")
    @Expose
    private Long txTime;

    private String historyType;
    private boolean isReceiptUpdated = false;

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

    public Long getTxTime() {
        return txTime;
    }

    public void setTxTime(Long txTime) {
        this.txTime = txTime;
    }

    public void setHistoryType(HistoryPayType historyType) {
        this.historyType = historyType.name();
    }

    public HistoryPayType getHistoryType() {
        return HistoryPayType.valueOf(historyType);
    }

    public TokenHistory(){

    }

    public void setReceiptUpdated(boolean receiptUpdated) {
        isReceiptUpdated = receiptUpdated;
    }

    public boolean isReceiptUpdated() {
        return isReceiptUpdated;
    }

    //for unit_test
    public TokenHistory(String contractAddress, String from, String to, String amount, String txHash, Long txTime) {
        this.contractAddress = contractAddress;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.txHash = txHash;
        this.txTime = txTime;
    }
}
