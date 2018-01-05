package org.qtum.wallet.model.gson.history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TransactionReceipt extends RealmObject {

    @SerializedName("blockHash")
    @Expose
    private String blockHash;
    @SerializedName("blockNumber")
    @Expose
    private Integer blockNumber;
    @SerializedName("transactionHash")
    @Expose
    private String transactionHash;
    @SerializedName("transactionIndex")
    @Expose
    private Integer transactionIndex;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("cumulativeGasUsed")
    @Expose
    private Integer cumulativeGasUsed;
    @SerializedName("gasUsed")
    @Expose
    private Integer gasUsed;
    @SerializedName("contractAddress")
    @Expose
    private String contractAddress;
    @SerializedName("log")
    @Expose
    private RealmList<Log> log = null;

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public Integer getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(Integer transactionIndex) {
        this.transactionIndex = transactionIndex;
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

    public Integer getCumulativeGasUsed() {
        return cumulativeGasUsed;
    }

    public void setCumulativeGasUsed(Integer cumulativeGasUsed) {
        this.cumulativeGasUsed = cumulativeGasUsed;
    }

    public Integer getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(Integer gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public List<Log> getLog() {
        return log;
    }

    public void setLog(RealmList<Log> log) {
        this.log = log;
    }

}
