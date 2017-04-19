package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenBalance.TokenBalance;

public class TokenParams {

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("decimals")
    @Expose
    private Integer decimals;
    @SerializedName("totalSupply")
    @Expose
    private Integer totalSupply;
    @SerializedName("name")
    @Expose
    private String name;

    private TokenBalance balance;

    private String address;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Integer getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Integer totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TokenBalance getBalance() {
        return balance;
    }

    public void setBalance(TokenBalance balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
