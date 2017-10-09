package org.qtum.wallet.model.contract;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ContractMethod {

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

    public ContractMethod() {

    }

    public ContractMethod(boolean constant, String type, List<ContractMethodParameter> inputParams, String name, List<ContractMethodParameter> outputParams) {
        this.constant = constant;
        this.type = type;
        this.inputParams = inputParams;
        this.name = name;
        this.outputParams = outputParams;
    }

    /**
     * Constructor for unit testing
     */
    public ContractMethod(String name) {
        this.name = name;
    }

    public boolean isConstant() {
        return constant;
    }

    public List<ContractMethodParameter> getInputParams() {
        return inputParams;
    }

    public String getName() {
        return name;
    }

    public List<ContractMethodParameter> getOutputParams() {
        return outputParams;
    }

    public boolean isPayable() {
        return payable;
    }

    public String getType() {
        return type;
    }
}
