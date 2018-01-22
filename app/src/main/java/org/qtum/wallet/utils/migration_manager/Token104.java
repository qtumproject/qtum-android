package org.qtum.wallet.utils.migration_manager;

import com.google.gson.annotations.SerializedName;

import org.qtum.wallet.model.contract.ContractCreationStatus;

import java.math.BigDecimal;
import java.math.MathContext;

public class Token104 extends Contract104 {

    @SerializedName("lastBalance")
    private BigDecimal mLastBalance;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("decimal_units")
    private Integer decimalUnits;

    @SerializedName("name")
    private String name;

    public Token104(String contractAddress, String templateUiid, ContractCreationStatus contractCreationStatus, String date, String senderAddress, String contractName) {
        super(contractAddress, templateUiid, contractCreationStatus, date, senderAddress, contractName);
        this.mIsSubscribe = true;
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