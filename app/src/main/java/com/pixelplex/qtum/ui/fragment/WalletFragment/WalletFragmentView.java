package com.pixelplex.qtum.ui.fragment.WalletFragment;



import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface WalletFragmentView extends BaseFragmentView {
    void updateHistory(List<History> historyList);
    void setAdapterNull();
    void updateBalance(String balance, String unconfirmedBalance);
    void updatePubKey(String pubKey);
    void startRefreshAnimation();
    void stopRefreshRecyclerAnimation();
    void addHistory(int positionStart, int itemCount, List<History> historyList);
    void loadNewHistory();
    void notifyNewHistory();
    void notifyConfirmHistory(int notifyPosition);
    void setWalletName(String walletName);
    void notifyNewToken();

}
