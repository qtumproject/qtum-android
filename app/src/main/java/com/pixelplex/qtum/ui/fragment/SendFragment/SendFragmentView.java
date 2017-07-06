package com.pixelplex.qtum.ui.fragment.SendFragment;

import android.support.v4.app.Fragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SendFragmentView extends BaseFragmentView{
    void openInnerFragmentForResult(Fragment fragment);
    void qrCodeRecognitionToolBar();
    void sendToolBar();
    void updateData(String publicAddress, double amount);
    void errorRecognition();
    void setProgressBar();
    void updateAvailableBalance(String balance);
    void setUpCurrencyField(String currency);
    Fragment getFragment();
    void hideCurrencyField();

    void setAdressAndAmount(String address, String anount);
}
