package com.pixelplex.qtum.datastorage.backupmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by max-v on 6/20/2017.
 */

public class Backup {

    @SerializedName("date_create")
    @Expose
    private String dateCreate;
    @SerializedName("templates")
    @Expose
    private List<Template> templates = null;
    @SerializedName("platformVersion")
    @Expose
    private String platformVersion;
    @SerializedName("fileVersion")
    @Expose
    private String fileVersion;
    @SerializedName("contracts")
    @Expose
    private List<ContractJSON> contracts = null;
    @SerializedName("platform")
    @Expose
    private String platform;

    public Backup(String dateCreate, List<Template> templates, String platformVersion, String fileVersion, List<ContractJSON> contracts, String platform) {
        this.dateCreate = dateCreate;
        this.templates = templates;
        this.platformVersion = platformVersion;
        this.fileVersion = fileVersion;
        this.contracts = contracts;
        this.platform = platform;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public List<ContractJSON> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractJSON> contracts) {
        this.contracts = contracts;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

}
