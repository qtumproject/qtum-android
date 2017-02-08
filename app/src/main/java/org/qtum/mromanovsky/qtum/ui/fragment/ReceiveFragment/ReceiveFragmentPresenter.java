package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;


public interface ReceiveFragmentPresenter {
    void generateQrCode(String s);
    void onClickCopyWalletAddress();
    void onClickChooseAnotherAddress();
}
