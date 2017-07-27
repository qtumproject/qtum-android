package com.pixelplex.qtum.ui.fragment.WalletFragment;


interface WalletFragmentPresenter {
    void onClickQrCode();
    void onRefresh();
    void sharePubKey();
    void openTransactionFragment(int position);
    void onLastItem(int currentItemCount);
    void onChooseAddressClick();
}
