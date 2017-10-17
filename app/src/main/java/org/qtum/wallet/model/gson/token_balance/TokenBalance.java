package org.qtum.wallet.model.gson.token_balance;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenBalance {

    @SerializedName("contract_address")
    @Expose
    private String contractAddress;
    @SerializedName("balances")
    @Expose
    private List<Balance> balances = null;

    /**
     * Constructor for unit testing
     */
    public TokenBalance() {

    }

    public TokenBalance(List<Balance> balances) {
        this.balances = balances;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public Balance getBalanceForAddress(String contractAddress) {
        if (balances != null) {
            for (Balance b : balances) {
                if (contractAddress.equals(b.getAddress())) {
                    return b;
                }
            }
        }
        return null;
    }

    public BigDecimal getMaxBalance() {
        BigDecimal maxBalance = new BigDecimal(0);
        if (balances != null && balances.size() > 0) {
            for (Balance balance : balances) {
                if (maxBalance.intValue() < balance.getBalance().intValue()) {
                    maxBalance = balance.getBalance();
                }
            }
        }
        return maxBalance;
    }

    public BigDecimal getTotalBalance() {
        BigDecimal summaryBalance = new BigDecimal(0);
        if (balances != null && balances.size() > 0) {
            for (Balance balance : balances) {
                if (balance.getBalance().intValue() > 0) {
                    summaryBalance = summaryBalance.add(balance.getBalance());
                }
            }
        }
        return summaryBalance;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

}
