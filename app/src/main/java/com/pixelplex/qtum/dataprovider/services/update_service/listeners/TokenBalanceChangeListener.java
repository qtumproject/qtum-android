package com.pixelplex.qtum.dataprovider.services.update_service.listeners;


import com.pixelplex.qtum.model.gson.token_balance.TokenBalance;

public interface TokenBalanceChangeListener {
    void onBalanceChange(TokenBalance tokenBalance);
}
