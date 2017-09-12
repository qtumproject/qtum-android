package org.qtum.wallet.model.contract;


import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Token extends Contract {

    @SerializedName("lastBalance")
    private BigDecimal mLastBalance;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("decimal_units")
    private Integer decimalUnits;

    public Token(String contractAddress, String templateUiid, Boolean hasBeenCreated, String date, String senderAddress, String contractName) {
        super(contractAddress, templateUiid, hasBeenCreated, date, senderAddress, contractName);
        this.mIsSubscribe = true;
    }

    public String getSymbol(){
        return symbol;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public void setDecimalUnits(Integer decimalUnits){
        this.decimalUnits = decimalUnits;
    }

    public Integer getDecimalUnits(){
        return this.decimalUnits;
    }

    public void setLastBalance(BigDecimal balance){
        this.mLastBalance = balance;
    }

    public BigDecimal getLastBalance(){
        return mLastBalance;
    }

    public boolean isSubscribe() {
        return mIsSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        mIsSubscribe = subscribe;
    }

    public BigDecimal getTokenBalanceWithDecimalUnits(){
        return mLastBalance.divide(new BigDecimal(Math.pow(10, (decimalUnits != null) ? decimalUnits.intValue() : 0)));
    }

}
