package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels;

/**
 * Created by maksimromanovskij on 03.06.17.
 */

public class ContractInfo {

    private String contractAddress;
    private String templateName;
    private Boolean hasBeenCreated;
    private Long date;

    public ContractInfo(String contractAddress, String templateName, Boolean hasBeenCreated, Long date){
        this.contractAddress = contractAddress;
        this.templateName = templateName;
        this.hasBeenCreated = hasBeenCreated;
        this.date = date;
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
}
