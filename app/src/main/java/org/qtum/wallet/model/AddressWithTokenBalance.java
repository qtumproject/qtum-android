package org.qtum.wallet.model;

import org.qtum.wallet.utils.CurrentNetParams;

import org.bitcoinj.crypto.DeterministicKey;

import java.math.BigDecimal;

public class AddressWithTokenBalance {
    private String mAddress;

    private BigDecimal mBalance;

    public AddressWithTokenBalance(String address) {
        mAddress = address;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public BigDecimal getBalance() {
        return mBalance;
    }

    public void setBalance(BigDecimal balance) {
        this.mBalance = balance;
    }

    public void addBalance(BigDecimal balance) {
        if (this.mBalance == null) {
            setBalance(balance);
            return;
        }
        this.mBalance = this.mBalance.add(balance);
    }
}
