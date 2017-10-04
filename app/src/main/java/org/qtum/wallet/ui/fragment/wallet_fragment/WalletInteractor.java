package org.qtum.wallet.ui.fragment.wallet_fragment;


import org.qtum.wallet.model.gson.history.History;

import java.util.List;

public interface WalletInteractor {
    List<History> getHistoryList();

    void getHistoryList(int STATE, int limit, int offset, WalletInteractorImpl.GetHistoryListCallBack callBack);

    String getAddress();

    int getTotalHistoryItem();

    void addToHistoryList(History history);

    Integer setHistory(History history);

    void unSubscribe();

}