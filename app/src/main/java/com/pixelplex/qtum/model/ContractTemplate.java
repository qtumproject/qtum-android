package com.pixelplex.qtum.model;

import com.pixelplex.qtum.model.backup.TemplateJSON;

public class ContractTemplate {
    private String name;
    private String date;
    private String type;
    private String uuid;
    private boolean isFullContractTemplate;

    public ContractTemplate(String name, String date, String contractType, String uuid){
        this.name = name;
        this.date = date;
        this.type = contractType;
        this.uuid = uuid;
        isFullContractTemplate = true;

    }

    public ContractTemplate(TemplateJSON templateJSON){
        this.name = templateJSON.getName();
        this.date = templateJSON.getCreationDate();
        this.type = templateJSON.getType();
        this.uuid = templateJSON.getUuid();
        isFullContractTemplate = true;
    }

    public boolean isSelectedABI() {
        return selectedABI;
    }

    public void setSelectedABI(boolean selectedABI) {
        this.selectedABI = selectedABI;
    }

    private boolean selectedABI;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isFullContractTemplate() {
        return isFullContractTemplate;
    }

    public void setFullContractTemplate(boolean fullContractTemplate) {
        isFullContractTemplate = fullContractTemplate;
    }
}