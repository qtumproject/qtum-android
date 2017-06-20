package com.pixelplex.qtum.datastorage.model;

/**
 * Created by max-v on 5/31/2017.
 */

public class ContractTemplate {
    private String name;
    private long date;
    private String type;
    private long uiid;

    public ContractTemplate(String name, long date, String contractType, long uiid){
        this.name = name;
        this.date = date;
        this.type = contractType;
        this.uiid = uiid;
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
        return type;
    }

    public void setContractType(String contractType) {
        this.type = contractType;
    }

    public long getUiid() {
        return uiid;
    }

    public void setUiid(long uiid) {
        this.uiid = uiid;
    }
}
