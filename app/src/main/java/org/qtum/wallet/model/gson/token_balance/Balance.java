package org.qtum.wallet.model.gson.token_balance;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Balance {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("balance")
    @Expose
    private BigDecimal balance;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return (balance != null)? balance : new BigDecimal(0);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
