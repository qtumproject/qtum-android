package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;


interface ReceiveFragmentPresenter {
    void changeAmount(String s);
    void onCopyWalletAddressClick();
    void onChooseAnotherAddressClick();
    void changeAddress();
}
