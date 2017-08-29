package com.pixelplex.qtum.ui.fragment.wallet_fragment;


interface WalletFragmentPresenter {
    void onClickQrCode();
    void onRefresh();
    void sharePubKey();
    void openTransactionFragment(int position);
    void onLastItem(int currentItemCount);
    void onChooseAddressClick();
}
