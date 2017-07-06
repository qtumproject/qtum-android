package com.pixelplex.qtum.model.backup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractJSON {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contractCreationAddres")
    @Expose
    private String contractCreationAddres;
    @SerializedName("contractAddress")
    @Expose
    private String contractAddress;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("publishDate")
    @Expose
    private String publishDate;
    @SerializedName("template")
    @Expose
    private Long template;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public ContractJSON(String name, String contractCreationAddres, String contractAddress, String type, String publishDate, Long template, Boolean isActive) {
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

    public Long getTemplate() {
        return template;
    }

    public void setTemplate(Long template) {
        this.template = template;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
