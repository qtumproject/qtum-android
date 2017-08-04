package com.pixelplex.qtum.model;

import com.pixelplex.qtum.utils.CurrentNetParams;

import org.bitcoinj.crypto.DeterministicKey;

import java.math.BigDecimal;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public class DeterministicKeyWithTokenBalance {
    private DeterministicKey mKey;
    private String address;

    private BigDecimal balance;

    public DeterministicKeyWithTokenBalance(DeterministicKey key){
        mKey = key;
        address = key.toAddress(CurrentNetParams.getNetParams()).toString();
    }

    public DeterministicKey getKey() {
        return mKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void addBalance(BigDecimal balance) {
        if(this.balance == null){
            setBalance(balance);
            return;
        }
       this.balance = this.balance.add(balance);
    }
}
