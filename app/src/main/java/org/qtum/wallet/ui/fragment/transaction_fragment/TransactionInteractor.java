package org.qtum.wallet.ui.fragment.transaction_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.TransactionReceipt;

import io.realm.Realm;

public interface TransactionInteractor {
    History getHistory(String txHash);

    String getFullDate(long l);

    String getUnconfirmedDate();

    TransactionReceipt getHistoryReceipt(Realm realm, String txHash);
}
