package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;

import java.util.List;

interface WalletFragmentInteractor {
    List<History> getHistoryList();
    void setHistoryList(List<History> historyList);
    void getHistoryList(WalletFragmentInteractorImpl.GetHistoryListCallBack callBack);
    String getAddress();
    void getBalance(WalletFragmentInteractorImpl.GetBalanceCallBack callBack);
}