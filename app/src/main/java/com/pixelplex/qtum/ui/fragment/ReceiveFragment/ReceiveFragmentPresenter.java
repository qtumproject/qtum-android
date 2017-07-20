package com.pixelplex.qtum.ui.fragment.ReceiveFragment;


interface ReceiveFragmentPresenter {
    void changeAmount(String s);
    void setTokenAddress(String address);
    void onCopyWalletAddressClick();
    void onChooseAnotherAddressClick();
    void changeAddress();
}
