package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels;

import java.io.Serializable;

/**
 * Created by maksimromanovskij on 03.06.17.
 */

public class ContractInfo implements Serializable {

    private String contractAddress;
    private String templateName;
    private String contractName;
    private Boolean hasBeenCreated;
    private Long date;
    private Boolean isToken;
    private float lastBalance = 0;

    public String getSenderAddress() {
        return senderAddress;
    }

    String senderAddress;

    public ContractInfo(String contractAddress, String templateName, Boolean hasBeenCreated, Long date){
        this.contractAddress = contractAddress;
        this.templateName = templateName;
        this.hasBeenCreated = hasBeenCreated;
        this.date = date;
    }

    public ContractInfo(String contractAddress, String templateName, Boolean hasBeenCreated, Long date, Boolean isToken, String senderAddress, String contractName){
        this.contractAddress = contractAddress;
        this.templateName = templateName;
        this.hasBeenCreated = hasBeenCreated;
        this.contractName = contractName;
        this.date = date;
        this.isToken = isToken;
        this.senderAddress = senderAddress;
    }

    public void setLastBalance(float balance){
        this.lastBalance = balance;
    }

    public float getLastBalance(){
        return lastBalance;
    }

    public void markSmartContractAsToken(Boolean isToken) {
        this.isToken = isToken;
    }

    public Boolean isToken() {
        return this.isToken;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Boolean isHasBeenCreated() {
        return hasBeenCreated;
    }

    public void setHasBeenCreated(Boolean hasBeenCreated) {
        this.hasBeenCreated = hasBeenCreated;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
}
