package com.pixelplex.qtum.ui.fragment.WalletFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;

interface WalletFragmentPresenter {
    void onClickQrCode();
    void onRefresh();
    void sharePubKey();
    void openTransactionFragment(int position);
    void onInitialInitialize();
    void changePage(int position);
    void onLastItem(int currentItemCount);
}
