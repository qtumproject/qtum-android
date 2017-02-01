
package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnspentOutput {

    @SerializedName("amount")
    @Expose
    private Long amount;
    @SerializedName("vout")
    @Expose
    private Integer vout;
    @SerializedName("tx_id")
    @Expose
    private Integer txId;
    @SerializedName("block_id")
    @Expose
    private Integer blockId;
    @SerializedName("block_height")
    @Expose
    private Integer blockHeight;
    @SerializedName("txout_id")
    @Expose
    private Integer txoutId;
    @SerializedName("txout_scriptPubKey")
    @Expose
    private String txoutScriptPubKey;
    @SerializedName("tx_hash")
    @Expose
    private String txHash;
    @SerializedName("block_hash")
    @Expose
    private String blockHash;
    @SerializedName("pubkey_hash")
    @Expose
    private String pubkeyHash;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }

    public Integer getTxId() {
        return txId;
    }

    public void setTxId(Integer txId) {
        this.txId = txId;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Integer getTxoutId() {
        return txoutId;
    }

    public void setTxoutId(Integer txoutId) {
        this.txoutId = txoutId;
    }

    public String getTxoutScriptPubKey() {
        return txoutScriptPubKey;
    }

    public void setTxoutScriptPubKey(String txoutScriptPubKey) {
        this.txoutScriptPubKey = txoutScriptPubKey;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getPubkeyHash() {
        return pubkeyHash;
    }

    public void setPubkeyHash(String pubkeyHash) {
        this.pubkeyHash = pubkeyHash;
    }

}
