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
    private Integer decimalUnits = 0;

    @SerializedName("name")
    private String name;

    public boolean getSupportFlag(){
        return decimalUnits <= 128;
    }

    public Token(String contractAddress, String templateUiid, ContractCreationStatus contractCreationStatus, Long date, String senderAddress, String contractName) {
        super(contractAddress, templateUiid, contractCreationStatus, date, senderAddress, contractName);
        this.mIsSubscribe = true;
    }

    public Token(String contractAddress, String uiid, String contractName, ContractCreationStatus creationStatus, Long date, String senderAddress, boolean isSubscribe){
        super(contractAddress,uiid,contractName,creationStatus,date,senderAddress,isSubscribe);
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
        return this.decimalUnits == null? 0 : this.decimalUnits;
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
        if(getSupportFlag()) {
            try {
                return mLastBalance.divide(new BigDecimal(10).pow((decimalUnits != null) ? decimalUnits : 0), MathContext.DECIMAL128);
            } catch (Exception e) {
                return new BigDecimal("0");
            }
        } else {
            return new BigDecimal("0");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}