package org.qtum.wallet.ui.fragment.overview_fragment;


import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.model.gson.token_history.TokenHistory;

public interface OverviewIteractor {
    History getHistory(String txHash);
    TokenHistory getTokenHistory(String txHash);
    TransactionReceipt getReceiptByRxhHashFromRealm(String txHash);
}
