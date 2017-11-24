package org.qtum.wallet.model.gson;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractParams {

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("decimals")
    @Expose
    private String decimals;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("totalSupply")
    @Expose
    private String totalSupply;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

}
