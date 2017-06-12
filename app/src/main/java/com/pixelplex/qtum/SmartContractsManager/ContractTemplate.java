package com.pixelplex.qtum.SmartContractsManager;

/**
 * Created by max-v on 5/31/2017.
 */

public class ContractTemplate {
    private String name;
    private long date;
    private String contractType;

    public ContractTemplate(String name, long date, String contractType){
        this.name = name;
        this.date = date;
        this.contractType = contractType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setData(long date) {
        this.date = date;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
}
