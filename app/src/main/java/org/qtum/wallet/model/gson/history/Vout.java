package org.qtum.wallet.model.gson.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Vout extends TransactionInfo {

    @SerializedName("value")
    @Expose
    private BigDecimal value;
    @SerializedName("address")
    @Expose
    private String address;
    private boolean isOwnAddress = false;

    /**
     * Constructor for unit testing
     */
    public Vout() {
    }

    /**
     * Constructor for unit testing
     */
    public Vout(String address) {
        this.address = address;
    }

    public boolean isOwnAddress() {
        return isOwnAddress;
    }

    public void setOwnAddress(boolean ownAddress) {
        isOwnAddress = ownAddress;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
