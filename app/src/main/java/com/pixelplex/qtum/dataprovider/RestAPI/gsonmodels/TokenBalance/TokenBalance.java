package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.TokenBalance;

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

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public float getTotalBalance() {
        float summaryBalance = 0;
        if(balances != null && balances.size() > 0){
            for (Balance balance: balances) {
                summaryBalance += balance.getBalance();
            }
        }
        return summaryBalance;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

}
