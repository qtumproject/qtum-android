package org.qtum.wallet.dataprovider.services.update_service.listeners;

import org.qtum.wallet.model.gson.token_balance.TokenBalance;

public interface TokenBalanceChangeListener {
    void onBalanceChange(TokenBalance tokenBalance);
}
