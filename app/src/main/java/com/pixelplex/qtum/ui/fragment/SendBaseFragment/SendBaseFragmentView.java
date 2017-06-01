package com.pixelplex.qtum.ui.fragment.SendBaseFragment;

import android.support.v4.app.Fragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SendBaseFragmentView extends BaseFragmentView{
    void openFragmentForResult(Fragment fragment);
    void qrCodeRecognitionToolBar();
    void sendToolBar();
    void updateData(String publicAddress, double amount);
    void errorRecognition();
    void confirmError(String errorText);
    void clearError();
    void setProgressBar();
    void updateAvailableBalance(String balance);
}
