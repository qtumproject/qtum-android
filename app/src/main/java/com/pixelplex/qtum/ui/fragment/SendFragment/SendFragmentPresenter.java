package com.pixelplex.qtum.ui.fragment.SendFragment;


public interface SendFragmentPresenter {
    void onClickQrCode();
    void isQrCodeRecognition(boolean isQrCodeRecognition);
    void onResponse(String publicAddress, double amount, String tokenAddress);
    void onResponseError();
    void onCurrencyClick();
    public void send(String[] sendInfo);
    public void send(String[] sendinfo, String pin);
}
