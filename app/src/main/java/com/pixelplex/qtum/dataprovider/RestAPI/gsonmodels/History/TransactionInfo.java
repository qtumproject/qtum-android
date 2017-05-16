package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History;


import java.math.BigDecimal;

public abstract class TransactionInfo {
    public abstract String getAddress();
    public abstract BigDecimal getValue();
    public abstract boolean isOwnAddress();
}
