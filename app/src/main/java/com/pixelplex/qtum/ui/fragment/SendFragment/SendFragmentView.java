package com.pixelplex.qtum.ui.fragment.SendFragment;

import android.support.v4.app.Fragment;

import com.pixelplex.qtum.dataprovider.services.update_service.UpdateService;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SendFragmentView extends BaseFragmentView{
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
