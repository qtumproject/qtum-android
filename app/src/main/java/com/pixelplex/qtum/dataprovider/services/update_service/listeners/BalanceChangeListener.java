package com.pixelplex.qtum.dataprovider.services.update_service.listeners;


import java.math.BigDecimal;

public interface BalanceChangeListener {
    void onChangeBalance(BigDecimal unconfirmedBalance, BigDecimal balance);
}
