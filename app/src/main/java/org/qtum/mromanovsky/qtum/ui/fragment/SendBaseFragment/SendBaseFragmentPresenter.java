package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;


interface SendBaseFragmentPresenter {
    void onClickQrCode();
    void isQrCodeRecognition(boolean isQrCodeRecognition);
    void onResponse(String publicAddress, double amount);
    void onResponseError();
    void onCurrencyClick();
    void send(String[] sendInfo);
}
