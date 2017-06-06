package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.TokenBalance.TokenBalance;

public interface TokenBalanceChangeListener {
    void onBalanceChange(TokenBalance tokenBalance);
}
