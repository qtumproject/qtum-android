package com.pixelplex.qtum.model;

import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.utils.CurrentNetParams;

import org.bitcoinj.crypto.DeterministicKey;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


public class DeterministicKeyWithBalance {
    private DeterministicKey mKey;
    private List<UnspentOutput> mUnspentOutputList;
    private String address;
    private BigDecimal balance;
    
    public DeterministicKeyWithBalance(DeterministicKey key){
        mKey = key;
        address = key.toAddress(CurrentNetParams.getNetParams()).toString();
    }

    public DeterministicKey getKey() {
        return mKey;
    }

    public void setKey(DeterministicKey key) {
        mKey = key;
    }

    public List<UnspentOutput> getUnspentOutputList() {
        return mUnspentOutputList;
    }

    public void setUnspentOutputList(List<UnspentOutput> unspentOutputList) {
        mUnspentOutputList = unspentOutputList;
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
}
