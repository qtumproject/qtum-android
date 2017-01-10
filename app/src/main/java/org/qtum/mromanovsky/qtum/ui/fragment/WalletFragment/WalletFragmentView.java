package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.model.Transaction;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

public interface WalletFragmentView extends BaseFragmentView {
    //    void updatePublicKey(String publicKey);
    //    void updateBalance(String balance);
    void updateRecyclerView(List<Transaction> list);

    void setAdapterNull();
}
