package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.model.TransactionQTUM;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

public interface WalletFragmentView extends BaseFragmentView {
    void updateRecyclerView(List<TransactionQTUM> list);
    void setAdapterNull();
    void updateBalance(double balance);
    void updateData();
    void updatePubKey(String pubKey);
    void startRefreshAnimation();
}
