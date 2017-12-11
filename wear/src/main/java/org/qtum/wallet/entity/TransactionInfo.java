package org.qtum.wallet.entity;

import java.math.BigDecimal;

/**
 * Created by kirillvolkov on 22.11.2017.
 */

public abstract class TransactionInfo {
    public abstract String getAddress();

    public abstract BigDecimal getValue();

    public abstract boolean isOwnAddress();
}
