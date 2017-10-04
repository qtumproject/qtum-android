package org.qtum.wallet.model.gson.call_smart_contract_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CallSmartContractResponse {
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    /**
     * Constructor for unit testing
     */
    public CallSmartContractResponse() {

    }

    /**
     * Constructor for unit testing
     */
    public CallSmartContractResponse(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
