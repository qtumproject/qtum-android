package com.pixelplex.qtum.datastorage.backupmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by max-v on 6/20/2017.
 */

public class TemplateJSON {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("bytecode")
    @Expose
    private String bytecode;
    @SerializedName("uiid")
    @Expose
    private Long uiid;
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("abi")
    @Expose
    private String abi;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;

    public TemplateJSON(String source, String bytecode, Long uiid, String creationDate, String abi, String type, String name) {
        this.source = source;
        this.bytecode = bytecode;
        this.uiid = uiid;
        this.creationDate = creationDate;
        this.abi = abi;
        this.type = type;
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBytecode() {
        return bytecode;
    }

    public void setBytecode(String bytecode) {
        this.bytecode = bytecode;
    }

    public Long getUiid() {
        return uiid;
    }

    public void setUiid(Long uiid) {
        this.uiid = uiid;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}