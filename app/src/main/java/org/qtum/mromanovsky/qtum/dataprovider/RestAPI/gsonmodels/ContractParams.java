package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractParams {

    @SerializedName("symbol")
    @Expose
    private String tokenSymbol;
    @SerializedName("decimals")
    @Expose
    private Integer decimalUnits;
    @SerializedName("name")
    @Expose
    private String tokenName;
    @SerializedName("totalSupply")
    @Expose
    private Integer initialSupply;

    public ContractParams(int initialSupply, String tokenName, int decimalUnits, String tokenSymbol) {
        this.initialSupply = initialSupply;
        this.tokenName = tokenName;
        this.decimalUnits = decimalUnits;
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public Integer getDecimalUnits() {
        return decimalUnits;
    }

    public void setDecimalUnits(Integer decimalUnits) {
        this.decimalUnits = decimalUnits;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Integer getInitialSupply() {
        return initialSupply;
    }

    public void setInitialSupply(Integer initialSupply) {
        this.initialSupply = initialSupply;
    }
}
