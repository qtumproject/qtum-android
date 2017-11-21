package org.qtum.wallet.model;

import org.qtum.wallet.model.gson.UnspentOutput;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddressWithBalance {
    private List<UnspentOutput> mUnspentOutputList = new ArrayList<>();
    private String mAddress;
    private BigDecimal mBalance;

    public AddressWithBalance(String address) {
        mAddress = address;
    }

    public List<UnspentOutput> getUnspentOutputList() {
        return mUnspentOutputList;
    }

    public void setUnspentOutputList(List<UnspentOutput> unspentOutputList) {
        mUnspentOutputList = unspentOutputList;
    }

    public void setUnspentOutput(UnspentOutput unspentOutput) {
        mUnspentOutputList.add(unspentOutput);
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public BigDecimal getBalance() {
        if (mBalance == null) {
            mBalance = new BigDecimal(0.0);
            for (UnspentOutput unspentOutput : mUnspentOutputList) {
                mBalance = mBalance.add(unspentOutput.getAmount());
            }
        }
        return mBalance;
    }

    public void setBalance(BigDecimal balance) {
        this.mBalance = balance;
    }
}
