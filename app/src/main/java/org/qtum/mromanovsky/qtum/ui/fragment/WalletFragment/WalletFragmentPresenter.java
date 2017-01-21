package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


public interface WalletFragmentPresenter {
    void onClickReceive();
    void onClickQrCode();
    void onRefresh();
    void sharePubKey();
    void openTransactionFragment(int position);
}
