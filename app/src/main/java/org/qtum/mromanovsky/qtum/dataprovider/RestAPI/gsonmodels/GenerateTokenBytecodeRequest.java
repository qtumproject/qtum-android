package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;


public class GenerateTokenBytecodeRequest {

    private long initialSupply;
    private String tokenName;
    private int decimalUnits;
    private String tokenSymbol;

    public GenerateTokenBytecodeRequest(long initialSupply, String tokenName, int decimalUnits, String tokenSymbol) {
        this.initialSupply = initialSupply;
        this.tokenName = tokenName;
        this.decimalUnits = decimalUnits;
        this.tokenSymbol = tokenSymbol;
    }
}
