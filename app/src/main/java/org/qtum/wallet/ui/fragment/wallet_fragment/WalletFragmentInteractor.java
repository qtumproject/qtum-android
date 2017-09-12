package org.qtum.wallet.ui.fragment.wallet_fragment;


import org.qtum.wallet.model.gson.history.History;

import java.util.List;

interface WalletFragmentInteractor {
    List<History> getHistoryList();
    void getHistoryList(int STATE, int limit, int offset, WalletFragmentInteractorImpl.GetHistoryListCallBack callBack);
    String getAddress();
    int getTotalHistoryItem();
    void addToHistoryList(History history);
    Integer setHistory(History history);
}