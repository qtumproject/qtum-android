package com.pixelplex.qtum.dataprovider.listeners;


import java.math.BigDecimal;

public interface BalanceChangeListener {
    void onChangeBalance(BigDecimal unconfirmedBalance, BigDecimal balance);
}
