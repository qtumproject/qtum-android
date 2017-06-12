package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class ContractMethodParameter implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    private String value;

    private String displayName;

    public ContractMethodParameter(String name, String type, String value){
        this.name = name;
        this.type = type;
        this. value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
