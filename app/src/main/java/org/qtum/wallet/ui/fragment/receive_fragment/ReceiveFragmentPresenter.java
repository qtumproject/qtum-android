package org.qtum.wallet.ui.fragment.receive_fragment;


public interface ReceiveFragmentPresenter {
    void changeAmount(String s);
    void setTokenAddress(String address);
    void onCopyWalletAddressClick();
    void onChooseAnotherAddressClick();
    void changeAddress();
}
