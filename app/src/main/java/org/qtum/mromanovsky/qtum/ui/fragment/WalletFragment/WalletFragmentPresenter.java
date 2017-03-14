package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


interface WalletFragmentPresenter {
    void onClickQrCode();
    void onRefresh();
    void sharePubKey();
    void openTransactionFragment(int position);
    void onInitialInitialize();
    void changePage();
}
