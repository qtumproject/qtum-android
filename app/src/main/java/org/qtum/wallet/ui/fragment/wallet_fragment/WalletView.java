package org.qtum.wallet.ui.fragment.wallet_fragment;


import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface WalletView extends BaseFragmentView {
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

    WalletInteractorImpl.GetHistoryListCallBack getHistoryCallback();

    void openTransactionsFragment(int position);

}
