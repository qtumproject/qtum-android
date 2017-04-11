package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;

import java.util.List;

interface WalletFragmentInteractor {
    List<History> getHistoryList();
    void getHistoryList(int STATE, int limit, int offset, WalletFragmentInteractorImpl.GetHistoryListCallBack callBack);
    String getAddress();
    String getBalance();
    String getUnconfirmedBalance();
    int getTotalHistoryItem();
    void addToHistoryList(History history);
    int setHistory(History history);
}