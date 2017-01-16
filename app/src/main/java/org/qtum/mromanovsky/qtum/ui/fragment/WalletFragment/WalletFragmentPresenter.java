package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


public interface WalletFragmentPresenter {
    void onClickReceive();
    void onRefresh();
    void openTransactionFragment(int position);
}
