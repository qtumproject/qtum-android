package com.pixelplex.qtum.model.contract;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ContractMethod{

    @SerializedName("constant")
    public boolean constant;

    @SerializedName("inputs")
    public List<ContractMethodParameter> inputParams;

    @SerializedName("name")
    public String name;

    @SerializedName("outputs")
    public List<ContractMethodParameter> outputParams;

    @SerializedName("payable")
    public boolean payable;

    @SerializedName("type")
    public String type;

}
