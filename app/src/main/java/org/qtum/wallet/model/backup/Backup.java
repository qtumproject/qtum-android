package org.qtum.wallet.model.backup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Backup {

    @SerializedName("date_create")
    @Expose
    private String dateCreate;
    @SerializedName("templates")
    @Expose
    private List<TemplateJSON> templates = null;
    @SerializedName("platform_version")
    @Expose
    private String platformVersion;
    @SerializedName("backup_version")
    @Expose
    private String fileVersion;
    @SerializedName("contracts")
    @Expose
    private List<ContractJSON> contracts = null;
    @SerializedName("platform")
    @Expose
    private String platform;

    public Backup(String dateCreate, List<TemplateJSON> templates, String platformVersion, String fileVersion, List<ContractJSON> contracts, String platform) {
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

    public List<TemplateJSON> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateJSON> templates) {
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
