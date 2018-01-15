package org.qtum.wallet.model.contract;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.MathContext;

public class Token extends Contract {

    @SerializedName("lastBalance")
    private BigDecimal mLastBalance;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("decimal_units")
    private Integer decimalUnits;

    @SerializedName("name")
    private String name;

    public Token(String contractAddress, String templateUiid, ContractCreationStatus contractCreationStatus, String date, String senderAddress, String contractName) {
        super(contractAddress, templateUiid, contractCreationStatus, date, senderAddress, contractName);
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
    public Token(boolean isSubscribe, ContractCreationStatus contractCreationStatus) {
        super(isSubscribe);
        this.mCreationStatus = contractCreationStatus;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}