package org.qtum.wallet.ui.fragment.send_fragment;

import android.support.v4.app.Fragment;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


interface SendFragmentView extends BaseFragmentView {
    void openInnerFragmentForResult(Fragment fragment);
    void qrCodeRecognitionToolBar();
    void sendToolBar();
    void updateData(String publicAddress, double amount, String tokenAddress);
    void errorRecognition();
    void updateBalance(String balance, String unconfirmedBalance);
    void setUpCurrencyField(String currency);
    Fragment getFragment();
    void hideCurrencyField();
    UpdateService getSocketService();

    void setAdressAndAmount(String address, String anount);
}
