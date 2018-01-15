package org.qtum.wallet.model.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ExistContractResponse {

    @SerializedName("exists")
    @Expose
    private boolean isExist;

    public boolean isExist() {
        return isExist;
    }
}
