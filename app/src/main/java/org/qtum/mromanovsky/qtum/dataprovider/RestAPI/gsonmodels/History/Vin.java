
package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vin extends TransactionInfo{

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("address")
    @Expose
    private String address;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
