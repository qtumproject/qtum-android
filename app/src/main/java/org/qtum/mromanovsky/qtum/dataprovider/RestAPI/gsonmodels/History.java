
package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("block_time")
    @Expose
    private Long blockTime;
    @SerializedName("block_height")
    @Expose
    private Integer blockHeight;
    @SerializedName("block_hash")
    @Expose
    private String blockHash;
    @SerializedName("tx_hash")
    @Expose
    private String txHash;
    @SerializedName("txin_pos")
    @Expose
    private Integer txinPos;
    @SerializedName("amount")
    @Expose
    private Long amount;
    @SerializedName("from_address")
    @Expose
    private String fromAddress;
    @SerializedName("to_address")
    @Expose
    private String toAddress;

    public Long getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Long blockTime) {
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

    public Integer getTxinPos() {
        return txinPos;
    }

    public void setTxinPos(Integer txinPos) {
        this.txinPos = txinPos;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

}
