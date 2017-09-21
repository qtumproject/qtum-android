package org.qtum.wallet.ui.fragment.send_fragment;


import org.qtum.wallet.model.Currency;

public interface SendFragmentPresenter {
    void onClickQrCode();
    void isQrCodeRecognition(boolean isQrCodeRecognition);
    void onResponse(String publicAddress, double amount, String tokenAddress);
    void onResponseError();
    void onCurrencyClick();
    void send(String from, String address, String amount, Currency currency, String fee);
}
