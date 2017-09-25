package org.qtum.wallet.model.gson;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;


public class FeePerKb {
    @SerializedName("fee_per_kb")
    BigDecimal feePerKb;

    public BigDecimal getFeePerKb() {
        return feePerKb;
    }
}
