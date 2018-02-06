package org.qtum.wallet.ui.fragment.transaction_fragment;

import org.qtum.wallet.model.gson.history.History;

public interface TransactionInteractor {
    History getHistory(String txHash);

    String getFullDate(long l);

    String getUnconfirmedDate();
}
