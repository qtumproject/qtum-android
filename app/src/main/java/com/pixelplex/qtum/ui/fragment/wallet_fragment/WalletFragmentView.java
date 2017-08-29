package com.pixelplex.qtum.ui.fragment.wallet_fragment;



import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface WalletFragmentView extends BaseFragmentView {
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
}
