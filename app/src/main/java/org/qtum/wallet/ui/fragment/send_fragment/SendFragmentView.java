package org.qtum.wallet.ui.fragment.send_fragment;

import android.support.v4.app.Fragment;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


interface SendFragmentView extends BaseFragmentView {
    void openInnerFragmentForResult(Fragment fragment);
    void qrCodeRecognitionToolBar();
    void sendToolBar();
    void updateData(String publicAddress, double amount);
    void errorRecognition();
    void updateBalance(String balance, String unconfirmedBalance);
    void setUpCurrencyField(Currency currency);
    Fragment getFragment();
    void hideCurrencyField();
    UpdateService getSocketService();
    void updateFee(double minFee, double maxFee);
    void updateGasPrice(int minGasPrice, int maxGasPrice);
    void updateGasLimit(int minGasLimit, int maxGasLimit);
    void setAdressAndAmount(String address, String anount);
}
