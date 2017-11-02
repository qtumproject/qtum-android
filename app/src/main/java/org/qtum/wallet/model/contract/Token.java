package org.qtum.wallet.model.contract;


import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

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

    /**
     * Default constructor for unit testing
     */
    public Token() {
    }

    /**
     * Constructor for unit testing
     */
    public Token(boolean isSubscribe) {
        super(isSubscribe);
    }

    /**
     * Constructor for unit testing
     */
    public Token(boolean isSubscribe, String contractAddress) {
        super(isSubscribe, contractAddress);
    }

    /**
     * Constructor for unit testing
     */
    public Token(Integer decimalUnits, BigDecimal lastBalance) {
        this.decimalUnits = decimalUnits;
        this.mLastBalance = lastBalance;
    }

    /**
     * Constructor for unit testing
     */
    public Token(BigDecimal lastBalance) {
        this.mLastBalance = lastBalance;
    }

    /**
     * Constructor for unit testing
     */
    public Token(boolean isSubscribe, Boolean isHasBeenCreated) {
        super(isSubscribe);
        this.mHasBeenCreated = isHasBeenCreated;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setDecimalUnits(Integer decimalUnits) {
        this.decimalUnits = decimalUnits;
    }

    public Integer getDecimalUnits() {
        return this.decimalUnits;
    }

    public void setLastBalance(BigDecimal balance) {
        this.mLastBalance = balance;
    }

    public BigDecimal getLastBalance() {
        return mLastBalance;
    }

    public boolean isSubscribe() {
        return mIsSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        mIsSubscribe = subscribe;
    }

    public BigDecimal getTokenBalanceWithDecimalUnits() {
        return mLastBalance.divide(new BigDecimal(10).pow((decimalUnits != null) ? decimalUnits.intValue() : 0), MathContext.DECIMAL128);
    }

}
