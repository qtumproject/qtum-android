package com.pixelplex.qtum.model.contract;

import java.io.Serializable;


public class Contract implements Serializable {

    protected String contractAddress;
    protected String uiid;
    protected String contractName;
    protected Boolean hasBeenCreated;
    protected String date;
    protected String senderAddress;
    protected boolean isSubscribe;

    public Contract(String contractAddress, String uiid, Boolean hasBeenCreated, String date, String senderAddress, String contractName){
        this.contractAddress = contractAddress;
        this.uiid = uiid;
        this.hasBeenCreated = hasBeenCreated;
        this.contractName = contractName;
        this.date = date;
        this.senderAddress = senderAddress;
        this.isSubscribe = false;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getUiid() {
        return uiid;
    }

    public void setUiid(String uiid) {
        this.uiid = uiid;
    }

    public Boolean isHasBeenCreated() {
        return hasBeenCreated;
    }

    public void setHasBeenCreated(Boolean hasBeenCreated) {
        this.hasBeenCreated = hasBeenCreated;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }
}
