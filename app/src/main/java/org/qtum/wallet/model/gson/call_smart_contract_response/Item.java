package org.qtum.wallet.model.gson.call_smart_contract_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("output")
    @Expose
    private String output;
    @SerializedName("excepted")
    @Expose
    private String excepted;
    @SerializedName("gas_used")
    @Expose
    private int gasUsed;

    /**
     * Constructor for unit testing
     */
    public Item() {
    }

    /**
     * Constructor for unit testing
     */
    public Item(String excepted) {
        this.excepted = excepted;
    }

    /**
     * Constructor for unit testing
     */
    public Item(String excepted, int gasUsed) {
        this.excepted = excepted;
        this.gasUsed = gasUsed;
    }

    public String getExcepted() {
        return excepted;
    }

    public int getGasUsed() {
        return gasUsed;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}