package com.pixelplex.qtum.model.backup;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TemplateJSON {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("bytecode")
    @Expose
    private String bytecode;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("date_create")
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

    public TemplateJSON(String source, String bytecode, String uuid, String creationDate, String abi, String type, String name) {
        this.source = source;
        this.bytecode = bytecode;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public boolean isFull(){
        return (!TextUtils.isEmpty(bytecode) && !TextUtils.isEmpty(abi) || !TextUtils.isEmpty(source));
    }

    public boolean getValidity(){
        return (!TextUtils.isEmpty(source) || !TextUtils.isEmpty(abi)) && !TextUtils.isEmpty(uuid) && !TextUtils.isEmpty(creationDate) && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(name);
    }

}