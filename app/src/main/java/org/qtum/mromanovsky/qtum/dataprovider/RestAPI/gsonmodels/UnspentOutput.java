
package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class UnspentOutput {

    @SerializedName("amount")
    @Expose
    private BigDecimal amount;
    @SerializedName("vout")
    @Expose
    private Integer vout;
    @SerializedName("txout_scriptPubKey")
    @Expose
    private String txoutScriptPubKey;
    @SerializedName("tx_hash")
    @Expose
    private String txHash;
    @SerializedName("pubkey_hash")
    @Expose
    private String pubkeyHash;
    @SerializedName("confirmations")
    @Expose
    private Integer confirmations;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
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


    public String getPubkeyHash() {
        return pubkeyHash;
    }

    public void setPubkeyHash(String pubkeyHash) {
        this.pubkeyHash = pubkeyHash;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }
}
