package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;
import org.qtum.mromanovsky.qtum.utils.Transaction;

import java.util.List;

public interface WalletFragmentView extends BaseFragmentView{
//    void updatePublicKey(String publicKey);
//    void updateBalance(String balance);
    void updateRecyclerView(List<Transaction> list);
}
