package org.qtum.wallet.model.gson.history;

import java.math.BigDecimal;

public interface TransactionInfo {
    String getAddress();

    BigDecimal getValue();

    String getValueString();

    boolean isOwnAddress();
}
