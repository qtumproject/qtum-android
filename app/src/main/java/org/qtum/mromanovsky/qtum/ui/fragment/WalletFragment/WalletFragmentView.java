package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

public interface WalletFragmentView extends BaseFragmentView{
//    void updatePublicKey(String publicKey);
//    void updateBalance(String balance);
    void openFragment(Fragment fragment);
}
