package com.pixelplex.qtum.dataprovider.listeners;


import com.pixelplex.qtum.model.gson.tokenBalance.TokenBalance;

public interface TokenBalanceChangeListener {
    void onBalanceChange(TokenBalance tokenBalance);
}
