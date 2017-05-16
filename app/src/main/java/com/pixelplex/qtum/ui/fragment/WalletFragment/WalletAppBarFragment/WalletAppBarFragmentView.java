package com.pixelplex.qtum.ui.fragment.WalletFragment.WalletAppBarFragment;

import android.app.Activity;
import android.support.v4.app.Fragment;


public interface WalletAppBarFragmentView{
    void openFragment(Fragment fragment);
    Activity getFragmentActivity();
    void updateBalance(String balance, String unconfirmedBalance);
    void setSymbol(String symbol);
}