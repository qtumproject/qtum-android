package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;


public interface SendBaseFragmentPresenter {
    void onClickQrCode();
    void isQrCodeRecognition(boolean isQrCodeRecognition);
    void onResponse(String publicAddress, double amount);
}
