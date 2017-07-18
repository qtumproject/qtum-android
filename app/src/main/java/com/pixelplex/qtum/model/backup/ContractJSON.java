package com.pixelplex.qtum.model.backup;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractJSON {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contract_creation_addres")
    @Expose
    private String contractCreationAddres;
    @SerializedName("contract_address")
    @Expose
    private String contractAddress;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("publish_date")
    @Expose
    private String publishDate;
    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;

    public ContractJSON(String name, String contractCreationAddres, String contractAddress, String type, String publishDate, String template, Boolean isActive) {
        this.name = name;
        this.contractCreationAddres = contractCreationAddres;
        this.contractAddress = contractAddress;
        this.type = type;
        this.publishDate = publishDate;
        this.template = template;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractCreationAddres() {
        return contractCreationAddres;
    }

    public void setContractCreationAddres(String contractCreationAddres) {
        this.contractCreationAddres = contractCreationAddres;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getValidity(){
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(contractCreationAddres) && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(contractAddress) && !TextUtils.isEmpty(publishDate) && !TextUtils.isEmpty(template);
    }

}
