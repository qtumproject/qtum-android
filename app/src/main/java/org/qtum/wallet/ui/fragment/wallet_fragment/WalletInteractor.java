package org.qtum.wallet.ui.fragment.wallet_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.TransactionReceipt;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

public interface WalletInteractor {

    Observable<HistoryResponse> getHistoryList(int limit, int offset);

    Observable<List<TransactionReceipt>> getTransactionReceipt(String txHash);

    String getAddress();

    List<String> getAddresses();

    void updateHistoryInRealm(List<History> histories);

    void updateHistoryInRealm(History histories);

    void updateReceiptInRealm(TransactionReceipt transactionReceipt);

    TransactionReceipt getReceiptByRxhHashFromRealm(String txHash);

    void setUpHistoryReceipt(String txHash, boolean isContract);

    void addHistoryInDbChangeListener(HistoryInDbChangeListener listener);

    int getHistoryDbCount();

    List<History> getHistorySubList(int startIndex, int length);

    History getHistory(String txHash);
}