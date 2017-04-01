package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;



import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface WalletFragmentView extends BaseFragmentView {
    void updateHistory(List<History> historyList);
    void setAdapterNull();
    void updateBalance(double balance);
    void updatePubKey(String pubKey);
    void startRefreshAnimation();
    void stopRefreshRecyclerAnimation();
    void addHistory(int positionStart, int itemCount, List<History> historyList);
    void loadNewHistory();
    void notifyNewHistory();
}
