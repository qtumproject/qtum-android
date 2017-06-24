package com.pixelplex.qtum.ui.fragment.WalletFragment;


import com.pixelplex.qtum.model.gson.history.History;

import java.util.List;

interface WalletFragmentInteractor {
    List<History> getHistoryList();
    void getHistoryList(int STATE, int limit, int offset, WalletFragmentInteractorImpl.GetHistoryListCallBack callBack);
    String getAddress();
    String getBalance();
    String getUnconfirmedBalance();
    int getTotalHistoryItem();
    void addToHistoryList(History history);
    Integer setHistory(History history);
    //void newToken(String tokenAddress, WalletFragmentInteractorImpl.AddToListCallBack addToListCallBack);
}