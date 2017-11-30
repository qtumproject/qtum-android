package org.qtum.wallet.model.contract;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContractMethod {

    @SerializedName("constant")
    private boolean constant;

    @SerializedName("inputs")
    private List<ContractMethodParameter> inputParams;

    @SerializedName("name")
    private String name;

    @SerializedName("outputs")
    private List<ContractMethodParameter> outputParams;

    @SerializedName("payable")
    private boolean payable;

    @SerializedName("type")
    private String type;

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
