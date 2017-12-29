package org.qtum.wallet.ui.fragment.wallet_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;

import java.util.List;

import rx.Observable;

public interface WalletInteractor {
    List<History> getHistoryList();

    Observable<HistoryResponse> getHistoryList(int limit, int offset);

    String getAddress();

    int getTotalHistoryItem();

    void addToHistoryList(History history);

    Integer setHistory(History history);

    List<String> getAddresses();
}