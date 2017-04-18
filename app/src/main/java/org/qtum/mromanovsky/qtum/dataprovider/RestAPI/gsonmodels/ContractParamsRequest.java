package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;

/**
 * Created by maksimromanovskij on 18.04.17.
 */

public class ContractParamsRequest {
    private long initialSupply;
    private String tokenName;
    private int decimalUnits;
    private String tokenSymbol;

    public ContractParamsRequest(long initialSupply, String tokenName, int decimalUnits, String tokenSymbol) {
        this.initialSupply = initialSupply;
        this.tokenName = tokenName;
        this.decimalUnits = decimalUnits;
        this.tokenSymbol = tokenSymbol;
    }
}
