package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class History {

    @SerializedName("block_time")
    @Expose
    private Integer blockTime;
    @SerializedName("block_height")
    @Expose
    private Integer blockHeight;
    @SerializedName("block_hash")
    @Expose
    private String blockHash;
    @SerializedName("tx_hash")
    @Expose
    private String txHash;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("vout")
    @Expose
    private List<Vout> vout = null;
    @SerializedName("vin")
    @Expose
    private List<Vin> vin = null;

    private double changeInBalance;

    public Integer getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Integer blockTime) {
        this.blockTime = blockTime;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Vout> getVout() {
        return vout;
    }

    public void setVout(List<Vout> vout) {
        this.vout = vout;
    }

    public List<Vin> getVin() {
        return vin;
    }

    public void setVin(List<Vin> vin) {
        this.vin = vin;
    }

    public double getChangeInBalance() {
        return changeInBalance;
    }

    public void setChangeInBalance(double changeInBalance) {
        this.changeInBalance = changeInBalance;
    }

}
