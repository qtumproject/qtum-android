package com.pixelplex.qtum.model.gson.history;


import java.math.BigDecimal;

public abstract class TransactionInfo {
    public abstract String getAddress();
    public abstract BigDecimal getValue();
    public abstract boolean isOwnAddress();
}
