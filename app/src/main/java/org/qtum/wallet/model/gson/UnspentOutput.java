
package org.qtum.wallet.model.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class UnspentOutput {

    @SerializedName("address")
    @Expose
    private String address;
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
    @SerializedName("is_stake")
    private boolean isStake;

    public boolean isOutputAvailableToPay() {
        if (isStake) {
            return confirmations > 500;
        }
        return true;
    }

    public UnspentOutput() {

    }

    /**
     * Constructor for unit testing
     */
    public UnspentOutput(Integer confirmations, boolean isStake, BigDecimal amount) {
        this.confirmations = confirmations;
        this.isStake = isStake;
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

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
