package com.pixelplex.qtum.datastorage.model;

/**
 * Created by max-v on 5/31/2017.
 */

public class ContractTemplate {
    private String name;
    private String date;
    private String type;
    private long uiid;
    private boolean isFullContractTemplate;

    public ContractTemplate(String name, String date, String contractType, long uiid){
        this.name = name;
        this.date = date;
        this.type = contractType;
        this.uiid = uiid;
        isFullContractTemplate = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setData(String date) {
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

    public boolean isFullContractTemplate() {
        return isFullContractTemplate;
    }

    public void setFullContractTemplate(boolean fullContractTemplate) {
        isFullContractTemplate = fullContractTemplate;
    }
}